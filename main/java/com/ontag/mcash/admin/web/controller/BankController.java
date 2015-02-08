package com.ontag.mcash.admin.web.controller;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
import com.ontag.mcash.admin.web.service.CorporateBankService;
import com.ontag.mcash.admin.web.service.CountryService;
import com.ontag.mcash.admin.web.util.Constants;

import com.ontag.mcash.dal.domain.CorporateBank;
import com.ontag.mcash.dal.domain.ReceiveSendCountry;



@Controller
@RequestMapping("/bank")
public class BankController {
	
	protected static org.slf4j.Logger logger = LoggerFactory.getLogger(BankController.class);
	
	@Autowired
	private CorporateBankService corporateBankService;
	
	@Autowired
	private CountryService countryService;
	
	@RequestMapping(value = "/list", method = RequestMethod.GET)
    public String getCorporateBankListPage() {
        return "bank-list";
    }
	
	@RequestMapping(value = "add", method = RequestMethod.GET)
    public String getAddPage(Model model) {
		//TODO send receive country need to implement	

		try{
			
			CorporateBank corporateBank = new CorporateBank();
			short id = 0;
			corporateBank.setId(id);
	    	model.addAttribute("corporatebank", corporateBank);
	    	
	    	
	    	List<CountryData> listData = CountryData.getArray(countryService.getAllCountryByTypeAndByStatus(ReceiveSendCountry.TYPE_SENDING,ReceiveSendCountry.STATUS_ACTIVE));
			  
	    	model.addAttribute("countryList", listData);		  
			model.addAttribute("screenMode", "add");
		}catch(Exception ex){
			ex.printStackTrace();
		}
        return "bank-edit";
    }
	
	
	@RequestMapping(value = "edit-{bankId}", method = RequestMethod.GET)
    public String getEditPage(Model model, @PathVariable int bankId) {
    	
    	try{
    		System.out.println("EDIT #############");
    		CorporateBank corporateBank = corporateBankService.findById(bankId);
    		List<CountryData> listData = CountryData.getArray(countryService.getAllCountryByTypeAndByStatus(ReceiveSendCountry.TYPE_SENDING,ReceiveSendCountry.STATUS_ACTIVE));
			  
	    	model.addAttribute("countryList", listData);
    		model.addAttribute("corporatebank", corporateBank);
    		model.addAttribute("screenMode", "edit");

        }catch(Exception ex){
    		ex.printStackTrace();
    	}
        return "bank-edit";
    }
	
	
	@RequestMapping(value = "/list.json", method = RequestMethod.POST, headers = Constants.REQUEST_HEADER_ACCEPT_JSON)
    public
    @ResponseBody
    DataGridData getListData(@RequestParam(value = "page", defaultValue = "1") int page,
                             @RequestParam(value = "rows", defaultValue = "10") int rows) {
		 try {

	            logger.debug("/list.json--> {}-{}", page, rows);
	            int start = (page - 1) * rows;
	            start = start < 0 ? 0 : start;
	            ListData[] listData = ListData.getArray(corporateBankService.getList(start, rows));

	            DataGridData<ListData> d = new DataGridData<>();
	            d.setRows(listData);
	            d.setTotal(corporateBankService.countAll());

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
    PostResponse saveCurrencyCode(@ModelAttribute("corporatebank") CorporateBank corporatebank, BindingResult result) {

    	if (!result.hasErrors()) {

            try {
            	if (corporatebank.getId() > 0) {
                	corporateBankService.edit(corporatebank);
                } else {
                	corporatebank.setStatus(CorporateBank.STATUS_ACTIVE);
                	corporateBankService.add(corporatebank);
                }
            } catch (Exception e) {
            	logger.error(e.getMessage(), e);
                result.addError(new ObjectError("save", e.getMessage()));
            }
        }
        PostResponse response = new PostResponse(result.getAllErrors());
        return response;
    }
	
	@RequestMapping(value = "activate-{ids}", method = RequestMethod.POST)
    public
    @ResponseBody
    PostResponse postForActivate(@PathVariable String ids) {
       
        try {
        	String []bankIdsArr = ids.split(",");
        	Short[] bankIds = new Short[bankIdsArr.length];
	        
	        for(int i=0;i<bankIdsArr.length;i++){
	        	bankIds[i] = Short.parseShort(bankIdsArr[i]);
	        }
	        System.out.println("Activate Controller");
	        corporateBankService.activateBanks(bankIds);
        }
	     catch (Exception e) {
	    	logger.info("group save Error : " + e.getMessage());
	        logger.error(e.getMessage(), e);
	     }
         
        PostResponse response = new PostResponse();

    	return response;
    }
	
	@RequestMapping(value = "deactivate-{ids}", method = RequestMethod.POST)
    public
    @ResponseBody
    PostResponse postForInactivate(@PathVariable String ids) {
       
        try {
        	String []bankIdsArr = ids.split(",");
        	Short[] bankIds = new Short[bankIdsArr.length];
	        
	        for(int i=0;i<bankIdsArr.length;i++){
	        	bankIds[i] = Short.parseShort(bankIdsArr[i]);
	        }
	        System.out.println("Deactivate Controller");
	        corporateBankService.deactivateBanks(bankIds);
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
        private String bankName;
        private String addressLine1;
        private String addressLine2;
        private String addressLine3;
        private String telephone;
        private String contactperson;
        private short status;
        private String statusdesc;
        private String country;
        
        
        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getBankName() {
            return bankName;
        }

        public void setBankName(String bankname) {
            this.bankName = bankname;
        }

        public String getAddressLine1() {
            return addressLine1;
        }

        public void setAddressLine1(String addressLine1) {
            this.addressLine1 = addressLine1;
        }

        public static ListData[] getArray(List<Object> dataList) {
            
            ListData[] result = new ListData[dataList.size()];
            int count = 0;
            CorporateBank corporateBank;
            for (Object object : dataList) {
            	corporateBank = (CorporateBank)object;	
                ListData row = new ListData();

            	row.id = corporateBank.getId();
            	row.bankName = corporateBank.getBankName();
            	row.addressLine1 = corporateBank.getAddressLine1();
            	row.addressLine2 = corporateBank.getAddressLine2();
            	row.addressLine3 = corporateBank.getAddressLine3();
            	row.telephone = corporateBank.getTelephone();
            	row.contactperson = corporateBank.getContactPerson();
            	row.status = corporateBank.getStatus();
            	row.statusdesc = (corporateBank.getStatus() == CorporateBank.STATUS_ACTIVE) ? CorporateBank.STATUS_ACTIVE_DESC : CorporateBank.STATUS_INACTIVE_DESC;
            	row.country = corporateBank.getCountrySendReceiveId().getCountryId().getCountryDesc();
                result[count++] = row;
            }

            return result;
        }

		public String getAddressLine2() {
			return addressLine2;
		}

		public void setAddressLine2(String addressLine2) {
			this.addressLine2 = addressLine2;
		}

		public String getAddressLine3() {
			return addressLine3;
		}

		public void setAddressLine3(String addressLine3) {
			this.addressLine3 = addressLine3;
		}

		public String getTelephone() {
			return telephone;
		}

		public void setTelephone(String telephone) {
			this.telephone = telephone;
		}

		public String getContactperson() {
			return contactperson;
		}

		public void setContactperson(String contactperson) {
			this.contactperson = contactperson;
		}

		public short getStatus() {
			return status;
		}

		public void setStatus(short status) {
			this.status = status;
		}

		public String getStatusdesc() {
			return statusdesc;
		}

		public void setStatusdesc(String statusdesc) {
			this.statusdesc = statusdesc;
		}

		public String getCountry() {
			return country;
		}

		public void setCountry(String country) {
			this.country = country;
		}


    }
	
	
	public static class CountryData {
        private int id;
        private String countryname;

        public static List<CountryData> getArray(List<ReceiveSendCountry> dataList) {
            
        	List<CountryData> result = new ArrayList<CountryData>();
            int count = 0;

            for (ReceiveSendCountry country : dataList) {
            	CountryData row = new CountryData();
            	row.id = country.getId();
            	row.countryname = country.getCountryId().getCountryDesc();
            	result.add(row);
            }
            return result;
        }

		public String getCountryname() {
			return countryname;
		}

		public void setCountryname(String countryname) {
			this.countryname = countryname;
		}

		public int getId() {
			return id;
		}

		public void setId(int id) {
			this.id = id;
		}
    }

}
