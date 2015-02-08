package com.ontag.mcash.admin.web.controller;

import java.util.Collection;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ontag.mcash.admin.web.controller.json.DataGridData;
import com.ontag.mcash.admin.web.controller.json.PostResponse;
import com.ontag.mcash.admin.web.exception.ServiceException;
import com.ontag.mcash.admin.web.service.CountryService;
import com.ontag.mcash.admin.web.service.CurrencyService;
import com.ontag.mcash.admin.web.util.Constants;
import com.ontag.mcash.dal.domain.BankCodes;
import com.ontag.mcash.dal.domain.CorporateBank;
import com.ontag.mcash.dal.domain.Country;
import com.ontag.mcash.dal.domain.CountryCurrency;
import com.ontag.mcash.dal.domain.CurrencyCode;
import com.ontag.mcash.dal.domain.ReceiveSendCountry;


@Controller
@RequestMapping("/country")
public class CountryController {
	
	protected static org.slf4j.Logger logger = LoggerFactory.getLogger(CountryController.class);
	
	@Autowired
	private CountryService countryService;
	
	@RequestMapping(value = "/list-sendreceive-country", method = RequestMethod.GET)
    public String getSendreceiveCountryListPage() {
		System.out.println("list-sendreceive-country ########");
        return "sendreceive-country-list";
    }
	
	@RequestMapping(value = "add", method = RequestMethod.GET)
    public String getAddPage(Model model) {

		ReceiveSendCountry receiveSendCountry = new ReceiveSendCountry();
		List<Country> countryList = null;
		
		short id = 0;
		receiveSendCountry.setId(id);
		
		try{
    		countryList = countryService.getAllCountry(0, 0);

        }catch(Exception ex){
    		ex.printStackTrace();
    	}
		
    	model.addAttribute("countryList", countryList);
    	model.addAttribute("country", receiveSendCountry);
		model.addAttribute("screenMode", "add");
        return "sendreceive-country-edit";
    }
	

	
	@RequestMapping(value = "/list-sendreceive-country.json", method = RequestMethod.POST, headers = Constants.REQUEST_HEADER_ACCEPT_JSON)
    public
    @ResponseBody
    DataGridData getListData(@RequestParam(value = "page", defaultValue = "1") int page,
                             @RequestParam(value = "rows", defaultValue = "10") int rows) {
		 try {

	            logger.debug("/list.json--> {}-{}", page, rows);
	            int start = (page - 1) * rows;
	            start = start < 0 ? 0 : start;
	            ListData[] listData = ListData.getArray(countryService.getAllReceiveSendCountry(start, rows));

	            DataGridData<ListData> d = new DataGridData<>();
	            d.setRows(listData);
	            d.setTotal(countryService.countAll());

	            logger.debug("returning start = {}", start);
	            return d;

	        } catch (Exception ex) {
	            logger.error(ex.getMessage(), ex);
	        }

	        return null;
    }
	
	@RequestMapping(value = "save", method = RequestMethod.POST)
    public
    @ResponseBody
    PostResponse saveCurrencyCode(@ModelAttribute("country") ReceiveSendCountry receiveSendCountry, BindingResult result) {

    	if (!result.hasErrors()) {

            try {
                if (receiveSendCountry.getId() > 0) {
                	countryService.edit(receiveSendCountry);
                } else {
                	ReceiveSendCountry tmpreceiveSendCountry = countryService.findByCountryAndType(receiveSendCountry.getCountryId().getId(), receiveSendCountry.getType());
                	if(tmpreceiveSendCountry != null)
                		throw new Exception(Constants.SEND_RECEVE_COUNTRY_ALREADY_EXISIT);
                	
                	receiveSendCountry.setStatus(ReceiveSendCountry.STATUS_ACTIVE);
                	countryService.add(receiveSendCountry);
                }
            } catch (Exception e) {
            	logger.error(e.getMessage(), e);
                result.addError(new ObjectError("save", e.getMessage()));
            }
        }
        PostResponse response = new PostResponse(result.getAllErrors());
        return response;
    }
	
	
	@RequestMapping(value = "activate-{countryIds}", method = RequestMethod.POST)
    public
    @ResponseBody
    PostResponse postForActivate(@PathVariable String countryIds) {
       
        try {
        	 logger.error("Activate ############ 1");
        	String []countryIdsArr = countryIds.split(",");
        	Integer[] sendreceivecountryIds = new Integer[countryIdsArr.length];
	        logger.error("Activate ############ 2");
	        
	        for(int i=0;i<countryIdsArr.length;i++){
	        	sendreceivecountryIds[i] = Integer.parseInt(countryIdsArr[i]);
	        }       
	        logger.error("Activate ############ 3");
        	countryService.activateSendReceiveCountries(sendreceivecountryIds);
        }
	     catch (Exception e) {
	    	logger.info("group save Error : " + e.getMessage());
	        logger.error(e.getMessage(), e);
	     }
         
        PostResponse response = new PostResponse();

    	return response;
    }
	
	@RequestMapping(value = "deactivate-{countryIds}", method = RequestMethod.POST)
    public
    @ResponseBody
    PostResponse postForInactivate(@PathVariable String countryIds) {
       
        try {
        	String []countryIdsArr = countryIds.split(",");
        	Integer[] sendreceivecountryIds = new Integer[countryIdsArr.length];
	        
	        for(int i=0;i<countryIdsArr.length;i++){
	        	sendreceivecountryIds[i] = Integer.parseInt(countryIdsArr[i]);
	        }       
        	countryService.inactivateSendReceiveCountries(sendreceivecountryIds);
        }
	     catch (Exception e) {
	    	logger.info("group save Error : " + e.getMessage());
	        logger.error(e.getMessage(), e);
	     }
         
        PostResponse response = new PostResponse();

    	return response;
    }
	
	
	
	static class ListData {
		    
        private int id;
        private int status;
        private String statusdesc;
        private int type;
        private String typedesc;
        private String country;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        

        public static ListData[] getArray(List<ReceiveSendCountry> dataList) {
            
            ListData[] result = new ListData[dataList.size()];
            int count = 0;
            for (ReceiveSendCountry country : dataList) {
            	ListData row = new ListData();

            	row.id = country.getId();
            	row.status = country.getStatus();
            	row.statusdesc = (country.getStatus() == ReceiveSendCountry.STATUS_ACTIVE) ? ReceiveSendCountry.STATUS_ACTIVE_DESC : ReceiveSendCountry.STATUS_INACTIVE_DESC;
            	row.type = country.getType();
            	row.typedesc = (country.getType() == ReceiveSendCountry.TYPE_RECEIVING) ? ReceiveSendCountry.TYPE_RECEIVING_DESC : ReceiveSendCountry.TYPE_SENDING_DESC;
            	row.country = country.getCountryId().getCountryDesc();
                result[count++] = row;
            }

            return result;
        }

		public int getStatus() {
			return status;
		}

		public void setStatus(int status) {
			this.status = status;
		}

		public String getStatusdesc() {
			return statusdesc;
		}

		public void setStatusdesc(String statusdesc) {
			this.statusdesc = statusdesc;
		}

		public int getType() {
			return type;
		}

		public void setType(int type) {
			this.type = type;
		}

		public String getTypedesc() {
			return typedesc;
		}

		public void setTypedesc(String typedesc) {
			this.typedesc = typedesc;
		}

		public String getCountry() {
			return country;
		}

		public void setCountry(String country) {
			this.country = country;
		}
    }

}
