package com.ontag.mcash.admin.web.controller;

//TODO check duplicate of card serial, pin, verification code

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;

import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ontag.mcash.admin.web.controller.json.CardFilter;
import com.ontag.mcash.admin.web.controller.json.DataGridData;
import com.ontag.mcash.admin.web.controller.json.MessageFilter;
import com.ontag.mcash.admin.web.controller.json.PostResponse;
import com.ontag.mcash.admin.web.exception.ServiceException;
import com.ontag.mcash.admin.web.service.BoUserService;
import com.ontag.mcash.admin.web.service.CardService;
import com.ontag.mcash.admin.web.service.CurrencyService;
import com.ontag.mcash.admin.web.service.MerchantService;
import com.ontag.mcash.admin.web.util.Constants;
import com.ontag.mcash.dal.domain.AppTransaction;
import com.ontag.mcash.dal.domain.BankCharges;
import com.ontag.mcash.dal.domain.BankCurrency;
import com.ontag.mcash.dal.domain.BoUsers;
import com.ontag.mcash.dal.domain.CashCard;
import com.ontag.mcash.dal.domain.CashCardBatch;
import com.ontag.mcash.dal.domain.CompanyCommision;
import com.ontag.mcash.dal.domain.CurrencyCode;
import com.ontag.mcash.dal.domain.CurrencyRates;
import com.ontag.mcash.dal.domain.Merchant;
import com.ontag.mcash.dal.domain.MerchantCommision;
import com.ontag.mcash.dal.domain.MerchantUsers;
import com.ontag.mcash.dal.domain.ReceivingMethods;



@Controller
@RequestMapping("/card")
public class CardController {

    protected static org.slf4j.Logger logger = LoggerFactory.getLogger(CardController.class);
    
    
    @Autowired
    private MerchantService merchantService;
    
    @Autowired
    private CardService cardService;
    
    @Autowired
    private CurrencyService currencyService;
    
    @Autowired
    BoUserService boUserService;
    
    @InitBinder
    public void initBinder(WebDataBinder binder) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        dateFormat.setLenient(false);
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
    }
    
    @RequestMapping(value = "sendemail_page",  method = RequestMethod.GET)
    public String getSendEmailPage(Model model, HttpServletRequest request) {
       logger.info("getSendEmailPage 11");
       List<Merchant> merchantList = null;
       try{
    	   merchantList = merchantService.getAllMerchant(0, 0);
       }catch(ServiceException ex){
    	   logger.info("Exception Occured : " + ex);
       }
       CardFilter filter = new CardFilter();
	   
	   model.addAttribute("cardfilter", filter);		
       model.addAttribute("merchantList", merchantList);
       return "card-sendemail";
    }
    
    @RequestMapping(value = "pendingbatches.json", method = RequestMethod.POST, headers = Constants.REQUEST_HEADER_ACCEPT_JSON)
    public
    @ResponseBody
    DataGridData getPendingBatchesListData(HttpServletRequest request) {
        try {
            ListData[] listData = ListData.getArray(cardService.getPendingBatches(0, 0) );
            logger.debug("Result list size is .. {}", listData.length);
            DataGridData<ListData> gridData = new DataGridData<>();
            gridData.setRows(listData);
            gridData.setTotal(listData.length);
            logger.info("creating grid data list was successfully completed ...");
            return gridData;
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }

        return null;
    }
    
    @RequestMapping(value = "activate_page",  method = RequestMethod.GET)
    public String getActivatePage(Model model, HttpServletRequest request) {
	       logger.info("getActivatePage");
	       List<Merchant> merchantList = null;
	       try{
	    	   merchantList = merchantService.getAllMerchant(0, 0);
	       }catch(ServiceException ex){
	    	   logger.info("Exception Occured : " + ex);
	       }
	       CardFilter filter = new CardFilter();
		   
		   model.addAttribute("cardfilter", filter);		
	       model.addAttribute("merchantList", merchantList);
	       return "card-activate";
    }
    
    @RequestMapping(value = "sentbatches.json", method = RequestMethod.POST, headers = Constants.REQUEST_HEADER_ACCEPT_JSON)
    public
    @ResponseBody
    DataGridData getSentBatchesListData(HttpServletRequest request) {
        try {
            ListData[] listData = ListData.getArray(cardService.getSentBatches(0, 0) );
            logger.debug("Result list size is .. {}", listData.length);
            DataGridData<ListData> gridData = new DataGridData<>();
            gridData.setRows(listData);
            gridData.setTotal(listData.length);
            return gridData;
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }

        return null;
    }

    @RequestMapping(value = "generate_page",  method = RequestMethod.GET)
    public String getCardGeneratePage(Model model, HttpServletRequest request) {
        logger.info("history");
        List<Merchant> merchantList = null;
        List<CurrencyCode> currencyList = null;
       try{
    	   merchantList = merchantService.getAllMerchant(0, 0);
    	   currencyList = currencyService.getAllCurrencyCode(0, 0);
       }catch(ServiceException ex){
    	   logger.info("Exception Occured : " + ex);
       }
       
       CardFilter filter = new CardFilter();
	   
	   model.addAttribute("dataListIsNull",true);
	   model.addAttribute("cardfilter", filter);		
       model.addAttribute("merchantList", merchantList);
       model.addAttribute("currenyList", currencyList);
       return "card-generator";
    }
    
    @RequestMapping(value = "generate-{merchantId}-{cardvalue}-{noofcard}", method = RequestMethod.POST)
    public @ResponseBody ListCardData
    generateCards(@PathVariable int merchantId, @PathVariable int cardvalue, @PathVariable int noofcard) {
    	ListCardData listCardData = new ListCardData();
    	try {
            	 Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	             String username = auth.getName();
	             BoUsers loggedUser = boUserService.getUserByUserName(username);	        	
	        	 Date today = new Date();
	             
	             Merchant merchant = merchantService.findById(merchantId);
	             
	             if((merchant.getBalance() + merchant.getCreditLimit()) < cardvalue * noofcard)
	            	 throw new Exception(Constants.MERCHANT_NOT_ENOUGH_BALANCE);
	             
	             //CurrencyCode currencyCode = currencyService.findById(currencycodeId);
	        	 CashCardBatch cashCardBatch = new CashCardBatch();
	        	 cashCardBatch.setMerchantId(merchant);
	        	 cashCardBatch.setStatus(CashCardBatch.STATUS_PENDING);
	        	 cashCardBatch.setGeneratedBy(loggedUser);
	        	 cashCardBatch.setGeneratedDate(today);
	        	 cashCardBatch.setValue(cardvalue);
	        	 cashCardBatch.setCurrencyCode(merchant.getCountryCurrency().getCurrencyCodeId());
	        	 cardService.generateCards(cashCardBatch, noofcard);
	        	 
	        	 
	        	 
	        	 listCardData.code = ListCardData.SUCCESS_CODE;
            } catch (Exception e) {
            	e.printStackTrace();
            	listCardData.code = ListCardData.FAILED_CODE;
            	listCardData.message = e.getLocalizedMessage();
            }
    	return listCardData;
    }
   
    @RequestMapping(value = "sendemail-{batchids}", method = RequestMethod.POST)
    public
    @ResponseBody
    PostResponse postForSendEmail(@ModelAttribute("cardfilter") MessageFilter messageFilter,@PathVariable String batchids) {
    	logger.info("#######inside postForSendEmail batchids : " + batchids);
        
    	try {
        	 Authentication auth = SecurityContextHolder.getContext().getAuthentication();
             String name = auth.getName();
	         BoUsers loggedUser = boUserService.getUserByUserName(name);
        	
	        String []batchArr = batchids.split(",");
	        Integer[] batchidArr = new Integer[batchArr.length];
	        
	        for(int i=0;i<batchArr.length;i++){
	        	batchidArr[i] = Integer.parseInt(batchArr[i]);
	        }
	        
	        cardService.emailCardBatch(batchidArr,loggedUser.getId());
        }
	     catch (Exception e) {
	    	logger.info("group save Error : " + e.getMessage());
	        logger.error(e.getMessage(), e);
	     }
         
        PostResponse response = new PostResponse();

    	return response;
    }
    
    @RequestMapping(value = "activate-{batchids}", method = RequestMethod.POST)
    public
    @ResponseBody
    PostResponse postForActivate(@ModelAttribute("cardfilter") MessageFilter messageFilter,@PathVariable String batchids) {
        logger.info("#######inside activate batchids : " + batchids);        
        try {
        	Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String name = auth.getName(); //get logged in username
	        BoUsers loggedUser = boUserService.getUserByUserName(name);

	        
	        String []batchArr = batchids.split(",");
	        Integer[] batchidArr = new Integer[batchArr.length];
	        
	        for(int i=0;i<batchArr.length;i++){
	        	batchidArr[i] = Integer.parseInt(batchArr[i]);
	        }                
	        
	        cardService.activateCardBatch(batchidArr,loggedUser);
        }
	    catch (Exception e) {
	    	logger.info("group save Error : " + e.getMessage());
	        logger.error(e.getMessage(), e);
	    }
         
        PostResponse response = new PostResponse();

    	return response;
    }
    
    
    @RequestMapping(value = "delete-{batchids}", method = RequestMethod.POST)
    public
    @ResponseBody
    PostResponse postForDelete(@ModelAttribute("cardfilter") MessageFilter messageFilter,@PathVariable String batchids) {
        logger.info("#######inside activate batchids : " + batchids);
        
        try {
        	 Authentication auth = SecurityContextHolder.getContext().getAuthentication();
             String name = auth.getName(); //get logged in username
	         BoUsers loggedUser = boUserService.getUserByUserName(name);
	        
	        String []batchArr = batchids.split(",");
	        Integer[] batchidArr = new Integer[batchArr.length];
	        
	        for(int i=0;i<batchArr.length;i++){
	        	batchidArr[i] = Integer.parseInt(batchArr[i]);
	        }                
	        cardService.deleteCardBatch(batchidArr,loggedUser.getId());
        }
	     catch (Exception e) {
	    	logger.info("group save Error : " + e.getMessage());
	        logger.error(e.getMessage(), e);
	     }
         
        PostResponse response = new PostResponse();

    	return response;
    }
    
    
    public static class ListCardData {
		
		public final static String SUCCESS_CODE = "00";
		public final static String FAILED_CODE = "01";
		
		private String code;
		private String message;
		
		public String getCode() {
			return code;
		}
		public void setCode(String code) {
			this.code = code;
		}
		public String getMessage() {
			return message;
		}
		public void setMessage(String message) {
			this.message = message;
		}
 
    }
    
    
    static class ListData {
        private int id;
        private String merchantName;
        private int noofCards;
        private int value;
        private String currencyCode;
        
        public String getMerchantName() {
            return merchantName;
        }

        public void setMerchantName(String merchantName) {
            this.merchantName = merchantName;
        }
        
        public int getNoofCards() {
            return noofCards;
        }
       
        public void setNoofCards(int noofCards) {
            this.noofCards = noofCards;
        }
        
        public int getId() {
            return id;
        }
       
        public void setId(int id) {
            this.id = id;
        }

        

        public static ListData[] getArray(List<Object[]> dataList) {
            ListData[] result = new ListData[dataList.size()];

            int count = 0;
            for (Object[] objects : dataList) {

                ListData row = new ListData();

                if (objects[0] != null) row.id = Integer.parseInt(objects[0].toString());
                if (objects[1] != null) row.merchantName = objects[1].toString();
                if (objects[2] != null) row.value = Integer.parseInt(objects[2].toString());
                if (objects[3] != null) row.currencyCode = objects[3].toString();
                if (objects[4] != null) row.noofCards = Integer.parseInt(objects[4].toString());
                
                result[count++] = row;
            }

            return result;
        }

		public int getValue() {
			return value;
		}

		public void setValue(int value) {
			this.value = value;
		}

		public String getCurrencyCode() {
			return currencyCode;
		}

		public void setCurrencyCode(String currencyCode) {
			this.currencyCode = currencyCode;
		}
    }


}
