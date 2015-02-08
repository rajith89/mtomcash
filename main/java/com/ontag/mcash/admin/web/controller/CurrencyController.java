package com.ontag.mcash.admin.web.controller;

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
import com.ontag.mcash.admin.web.service.CountryService;
import com.ontag.mcash.admin.web.service.CurrencyService;
import com.ontag.mcash.admin.web.util.Constants;
import com.ontag.mcash.dal.domain.CountryCurrency;
import com.ontag.mcash.dal.domain.CurrencyCode;
import com.ontag.mcash.dal.domain.ReceiveSendCountry;
import com.ontag.mcash.dal.domain.SendBankCurrency;

@Controller
@RequestMapping("/currency")
public class CurrencyController {
	
protected static org.slf4j.Logger logger = LoggerFactory.getLogger(CountryController.class);
	
	@Autowired
	private CurrencyService currencyService;
	
	@Autowired
	private CountryService countryService;
	
	@RequestMapping(value = "/test", method = RequestMethod.GET)
    public String getCurrencyTest(Model model) {
        logger.info("show currencycode-list");
        List<CurrencyCode> currencyCodeList = null;
        try {
			currencyCodeList = currencyService.getAllCurrencyCode(0,0);
		} catch (ServiceException e) {
			e.printStackTrace();
		}
        model.addAttribute("currencycode", currencyCodeList.get(0).getCurrencyCode());
        return "hello";
    }
	
	@RequestMapping(value = "/list", method = RequestMethod.GET)
    public String getCurrencyCodeListPage() {
        logger.info("show currencycode-list");
        return "currencycode-list";
    }
	
	@RequestMapping(value = "add", method = RequestMethod.GET)
    public String getAddPage(Model model) {

		CurrencyCode currencycode = new CurrencyCode();
		short id = 0;
		currencycode.setId(id);
    	model.addAttribute("currencycode", currencycode);
		model.addAttribute("screenMode", "add");
        return "currencycode-edit";
    }
	
	@RequestMapping(value = "edit-{cuurencycodeId}", method = RequestMethod.GET)
	public String getEditPage(Model model, @PathVariable int cuurencycodeId) {
    	
    	try{
    		logger.info("getEditPage ########### 4 : " + cuurencycodeId);
    		CurrencyCode currencycode = currencyService.findById(cuurencycodeId);
    		
    		model.addAttribute("currencycode", currencycode);
    		model.addAttribute("screenMode", "edit");

        }catch(Exception ex){
    		ex.printStackTrace();
    	}
        return "currencycode-edit";
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
	            ListData[] listData = ListData.getArray(currencyService.getList(start, rows));

	            DataGridData<ListData> d = new DataGridData<>();
	            d.setRows(listData);
	            d.setTotal(currencyService.countAll());

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
    PostResponse saveCurrencyCode(@ModelAttribute("currencycode") CurrencyCode currencycode, BindingResult result) {

		 logger.info("saveCurrencyCode ################## 1" );
    	if (!result.hasErrors()) {

            try {
            	logger.info("saveCurrencyCode ################## 2" );
                if (currencycode.getId() > 0) {
                	logger.info("saveCurrencyCode ################## 3" );
                	currencyService.edit(currencycode);
                } else {
                	logger.info("saveCurrencyCode ################## 4" );
                	currencyService.add(currencycode);
                }
            } catch (Exception e) {
            	logger.error(e.getMessage(), e);
                result.addError(new ObjectError("save", e.getMessage()));
            }
        }
        PostResponse response = new PostResponse(result.getAllErrors());
        return response;
    }
	
	@RequestMapping(value = "/curency.json-{countryId}", method = RequestMethod.GET, headers = Constants.REQUEST_HEADER_ACCEPT_JSON)
	public @ResponseBody
	CurrencyData[] getCountryCurrencyList(@PathVariable int countryId) {
		
		try {
			
			ReceiveSendCountry receiveSendCountry = countryService.findByCountryAndType(countryId, ReceiveSendCountry.TYPE_SENDING);
			CurrencyData[] listCurrencyData = CurrencyData.getArray(currencyService.getCurrencyByCountry(receiveSendCountry.getId()));
			return listCurrencyData;
			
		} catch (Exception ex) {
			logger.info("getReceivingMethodsList ################## 3");
			logger.error(ex.getMessage(), ex);
			
		}
		return null;	
	}
	
	
	public static class CurrencyData {
		
		private int id;
		private String currencyName; 


		public static CurrencyData[] getArray(List<CountryCurrency> dataList) {

			CurrencyData[] result = new CurrencyData[dataList.size()];
			int count = 0;
			for (CountryCurrency countryCurrency : dataList) {
				CurrencyData row = new CurrencyData();

				row.id = countryCurrency.getId();
				row.currencyName = countryCurrency.getCurrencyCodeId().getCurrencyDesc();
				result[count++] = row;
			}
			return result;
		}


		public int getId() {
			return id;
		}


		public void setId(int id) {
			this.id = id;
		}


		public String getCurrencyName() {
			return currencyName;
		}


		public void setCurrencyName(String currencyName) {
			this.currencyName = currencyName;
		}
	}

	static class ListData {
        private int id;
        private String currencyCode;
        private String currencyDesc;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getCurrencyCode() {
            return currencyCode;
        }

        public void setCurrencyCode(String currencyCode) {
            this.currencyCode = currencyCode;
        }

        public String getCurrencyDesc() {
            return currencyDesc;
        }

        public void setCurrencyDesc(String currencyDesc) {
            this.currencyDesc = currencyDesc;
        }

        public static ListData[] getArray(List<Object> dataList) {
            
            ListData[] result = new ListData[dataList.size()];
            int count = 0;
            CurrencyCode currency;
            for (Object object : dataList) {
            	currency = (CurrencyCode)object;	
                ListData row = new ListData();

            	row.id = currency.getId();
            	row.currencyCode = currency.getCurrencyCode();
            	row.currencyDesc = currency.getCurrencyDesc();
                
                result[count++] = row;
            }

            return result;
        }
    }


}
