package com.ontag.mcash.admin.web.controller;


import java.math.BigInteger;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.ontag.mcash.admin.web.controller.json.FileFilter;
import com.ontag.mcash.admin.web.controller.json.DataGridData;
import com.ontag.mcash.admin.web.controller.json.PostResponse;
import com.ontag.mcash.admin.web.service.CorporateBankService;
import com.ontag.mcash.admin.web.service.CurrencyRatesService;
import com.ontag.mcash.admin.web.util.Constants;
import com.ontag.mcash.dal.domain.BankCurrency;
import com.ontag.mcash.dal.domain.CorporateBank;
import com.ontag.mcash.dal.domain.CurrencyRates;

@Controller
@RequestMapping("/currencyrates")
public class CurrencyRatesController {

	protected static org.slf4j.Logger logger = LoggerFactory
			.getLogger(CurrencyRatesController.class);

	@Autowired
	CurrencyRatesService currencyRatesService;

	@Autowired
	CorporateBankService corporateBankService;

	@RequestMapping(value = "/get-currency-rates.json", method = RequestMethod.GET, headers = Constants.REQUEST_HEADER_ACCEPT_JSON)
	public @ResponseBody List<CurrencyListData> getActiveCurrenRates() {		
	  		
		try {
			List<CurrencyListData> data = CurrencyListData.getArray(currencyRatesService.getActiveCurrencyRates());
			return data;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
  		return null;
	}
	
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String getCurrencyRatesListPage() {
		logger.info("show CurrencyRates-list");
		return "currency-rates-list";
	}

	@RequestMapping(value = "add", method = RequestMethod.GET)
	public String getAddPage(Model model) {

		FileFilter currencyRatesUpload = new FileFilter();
		List<CorporateBank> corporateBankList = null;

		try {
			corporateBankList = corporateBankService.getAllCorporateBank(0, 0);

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		model.addAttribute("currencyRatesUpload", currencyRatesUpload);
		model.addAttribute("corporateBankList", corporateBankList);
		model.addAttribute("screenMode", "add");

		logger.info("show bankcharges-edit");
		return "currency-rates-edit";
	}

	@RequestMapping(value = "/list.json", method = RequestMethod.POST, headers = Constants.REQUEST_HEADER_ACCEPT_JSON)
	public @ResponseBody
	DataGridData getListData(
			@RequestParam(value = "page", defaultValue = "1") int page,
			@RequestParam(value = "rows", defaultValue = "10") int rows) {
		try {

			int start = (page - 1) * rows;
			start = start < 0 ? 0 : start;
			ListData[] listData = ListData.getArray(currencyRatesService
					.getList(start, rows));

			DataGridData<ListData> d = new DataGridData<>();
			d.setRows(listData);
			d.setTotal(currencyRatesService.countAll());

			logger.debug("returning start = {}", start);
			return d;

		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
		}

		return null;
	}

	@RequestMapping(value = "save", method = RequestMethod.POST)
	public @ResponseBody
	PostResponse saveCurrencyRate(
			@ModelAttribute("currencyRatesUpload") FileFilter csvFile,
			BindingResult result) {
		logger.info("Uploading CSV ######################### 1");

		if (!result.hasErrors()) {
			logger.info("Uploading CSV ######################### 2");
			try {
				logger.info("Uploading CSV ######################### 3");

				Short bankId = csvFile.getId();
				MultipartFile file = csvFile.getFile();

				currencyRatesService.addCurrencyRatesFromCSV(bankId,file);

				System.out.println("Selected Bank Id : " + bankId);
				System.out.println("CSV Filename : "
						+ file.getOriginalFilename());
				System.out.println("CSV FileSize : " + file.getSize());

			} catch (Exception e) {
				logger.error(e.getMessage(), e);
				result.addError(new ObjectError("save", e.getMessage()));
			}
		}

		PostResponse response = new PostResponse(result.getAllErrors());
		return response;
	}

	public static class CurrencyListData {
		private String currencycode;
		private String rate;
		
		
		public static List<CurrencyListData> getArray(List<CurrencyRates> dataList) {
            
			List<CurrencyListData> result = new ArrayList<CurrencyListData>();
            int count = 0;
            DecimalFormat df = new DecimalFormat("###.00");
            
            for (CurrencyRates currencyRate : dataList) {
            	CurrencyListData row = new CurrencyListData();
            	if(currencyRate.getBankCurrencyCodeId().getDefaultbankcurrency() == BankCurrency.VALUE_DEFAULT)
            			continue;
            	row.currencycode = currencyRate.getBankCurrencyCodeId().getCurrencyCodeId().getCurrencyCode();
            	row.rate = df.format(currencyRate.getAmount()) ;
            	result.add(row);
            }
            return result;
        }


		public String getCurrencycode() {
			return currencycode;
		}


		public void setCurrencycode(String currencycode) {
			this.currencycode = currencycode;
		}


		public String getRate() {
			return rate;
		}


		public void setRate(String rate) {
			this.rate = rate;
		}
	}
	
	static class ListData {

		private Long id;
		private String lastUpdateDate;
		private BigInteger lastUpdateUser;
		private String status;
		private double ttBuying;
		private double odBuying;
		private double ttSelling;
		private double amount;
		private String corporateBank;
		private String currencyCode;

		public Long getId() {
			return id;
		}

		public void setId(Long id) {
			this.id = id;
		}

		public String getLastUpdateDate() {
			return lastUpdateDate;
		}

		public void setLastUpdateDate(String lastUpdateDate) {
			this.lastUpdateDate = lastUpdateDate;
		}

		public BigInteger getLastUpdateUser() {
			return lastUpdateUser;
		}

		public void setLastUpdateUser(BigInteger lastUpdateUser) {
			this.lastUpdateUser = lastUpdateUser;
		}

		public String getStatus() {
			return status;
		}

		public void setStatus(String status) {
			this.status = status;
		}

		public double getTtBuying() {
			return ttBuying;
		}

		public void setTtBuying(double ttBuying) {
			this.ttBuying = ttBuying;
		}

		public double getOdBuying() {
			return odBuying;
		}

		public void setOdBuying(double odBuying) {
			this.odBuying = odBuying;
		}

		public double getTtSelling() {
			return ttSelling;
		}

		public void setTtSelling(double ttSelling) {
			this.ttSelling = ttSelling;
		}

		public String getBankCurrencyCodeId() {
			return corporateBank;
		}

		public void setBankCurrencyCodeId(String bankCurrencyCodeId) {
			this.corporateBank = bankCurrencyCodeId;
		}

		public String getCurrencyCode() {
			return currencyCode;
		}

		public void setCurrencyCode(String currencyCode) {
			this.currencyCode = currencyCode;
		}

		public static ListData[] getArray(List<Object> dataList) {

			ListData[] result = new ListData[dataList.size()];
			int count = 0;
			CurrencyRates currencyRates;
			for (Object object : dataList) {
				currencyRates = (CurrencyRates) object;
				ListData row = new ListData();

				row.id = currencyRates.getId();

				if (currencyRates.getLastUpdateDate() != null) {
					row.lastUpdateDate = currencyRates.getLastUpdateDate().toString();
				}

				row.status = (currencyRates.getStatus()
					      .equals(CurrencyRates.CURRENCYRATE_STATUS_ACTIVE)) ? CurrencyRates.CURRENCYRATE_STATUS_ACTIVE_DESC
					      : CurrencyRates.CURRENCYRATE_STATUS_INACTIVE_DESC;

				row.ttBuying = currencyRates.getTtBuying();
				row.odBuying = currencyRates.getOdBuying();
				row.ttSelling = currencyRates.getTtSelling();
				row.corporateBank = currencyRates.getBankCurrencyCodeId()
						.getCorporateBankId().getBankName();
				row.currencyCode = currencyRates.getBankCurrencyCodeId()
						.getCurrencyCodeId().getCurrencyCode();
				row.amount = currencyRates.getAmount();
				result[count++] = row;
			}
			return result;
		}

		public double getAmount() {
			return amount;
		}

		public void setAmount(double amount) {
			this.amount = amount;
		}
	}
}
