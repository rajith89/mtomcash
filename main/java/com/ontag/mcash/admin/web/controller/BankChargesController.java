package com.ontag.mcash.admin.web.controller;

import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
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

import com.ontag.mcash.admin.web.controller.BoRolesController.ListData;
import com.ontag.mcash.admin.web.controller.json.DataGridData;
import com.ontag.mcash.admin.web.controller.json.PostResponse;
import com.ontag.mcash.admin.web.service.AmountSlabService;
import com.ontag.mcash.admin.web.service.BankChargesService;
import com.ontag.mcash.admin.web.service.BankCurrencyService;
import com.ontag.mcash.admin.web.service.BoUserService;
import com.ontag.mcash.admin.web.service.CorporateBankService;
import com.ontag.mcash.admin.web.service.ReceivingMethodsService;
import com.ontag.mcash.admin.web.util.Constants;
import com.ontag.mcash.dal.domain.AmountSlab;
import com.ontag.mcash.dal.domain.BankCharges;
import com.ontag.mcash.dal.domain.BankCurrency;
import com.ontag.mcash.dal.domain.BoRoles;
import com.ontag.mcash.dal.domain.BoUsers;
import com.ontag.mcash.dal.domain.CorporateBank;
import com.ontag.mcash.dal.domain.CurrencyCode;
import com.ontag.mcash.dal.domain.ReceivingMethods;

@Controller
@RequestMapping("/bankcharges")
public class BankChargesController {

	protected static org.slf4j.Logger logger = LoggerFactory
			.getLogger(BankChargesController.class);

	@Autowired
	BankChargesService bankChargesService;

	@Autowired
	CorporateBankService corporateBankService;

	@Autowired
	AmountSlabService amountSlabService;

	@Autowired
	ReceivingMethodsService receivingMethodsService;

	@Autowired
	BoUserService boUserService;

	@Autowired
	BankCurrencyService bankCurrencyService;
	
	@RequestMapping(value = "/getbankcharge-{currencycode}-{receivingmethod}-{amountslab}", method = RequestMethod.GET, headers = Constants.REQUEST_HEADER_ACCEPT_JSON)
	public @ResponseBody
	ListData getBankCharge(@PathVariable int currencycode, @PathVariable int receivingmethod, @PathVariable int amountslab) {
		ListData listData = new ListData();
		try {
			BankCharges bankCharges = bankChargesService.findByBankCurrencyReceivingMethodAmountSlab(currencycode, receivingmethod, amountslab);
			if(bankCharges != null)
				listData.amount = bankCharges.getAmount();
			else
				listData.amount = 0;
		} catch (Exception ex) {
			listData.amount = 0;
			logger.info("getReceivingMethodsList ################## 3");
			logger.error(ex.getMessage(), ex);
		}
		return listData;
	}
	
	
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String getBankChargesListPage() {
		logger.info("show bankcharges-list");
		return "bank-charges-list";
	}

	@RequestMapping(value = "add", method = RequestMethod.GET)
	public String getAddPage(Model model) {

		BankCharges bankCharges = new BankCharges();
		List<CorporateBank> corporateBankList = null;
		List<AmountSlab> amountSlabList = null;

		try {
			corporateBankList = corporateBankService.getAllCorporateBank(0, 0);
			amountSlabList = amountSlabService.getAllAmountSlab(0, 0);

		} catch (Exception ex) {
			ex.printStackTrace();
		}

		bankCharges.setId(0L);
		model.addAttribute("amountSalbList", amountSlabList);
		model.addAttribute("corporateBankList", corporateBankList);
		model.addAttribute("bankcharges", bankCharges);
		model.addAttribute("screenMode", "add");
		logger.info("show bankcharges-edit");
		return "bank-charges-edit";
	}

	@RequestMapping(value = "/list.json", method = RequestMethod.POST, headers = Constants.REQUEST_HEADER_ACCEPT_JSON)
	public @ResponseBody
	DataGridData getListData(
			@RequestParam(value = "page", defaultValue = "1") int page,
			@RequestParam(value = "rows", defaultValue = "10") int rows) {
		try {

			int start = (page - 1) * rows;
			start = start < 0 ? 0 : start;
			ListData[] listData = ListData.getArray(bankChargesService.getList(
					start, rows));

			DataGridData<ListData> d = new DataGridData<>();
			d.setRows(listData);
			d.setTotal(bankChargesService.countAll());

			logger.debug("returning start = {}", start);
			return d;

		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
		}

		return null;
	}

	@RequestMapping(value = "/receivingmethods.json-{bankId}", method = RequestMethod.GET, headers = Constants.REQUEST_HEADER_ACCEPT_JSON)
	public @ResponseBody
	ListData_receivingMethods[] getReceivingMethodsList(@PathVariable int bankId) {

		logger.info("getReceivingMethodsList ################## 1");
		try {

			logger.info("getReceivingMethodsList ################## 2 : "
					+ bankId);

			ListData_receivingMethods[] listData = ListData_receivingMethods
					.getArray(receivingMethodsService
							.getReceivingMethodsByBankId(bankId));

			for (ListData_receivingMethods listData_receivingMethods : listData) {
				System.out.println(listData_receivingMethods.getMethodName());
			}

			return listData;

		} catch (Exception ex) {
			logger.info("getReceivingMethodsList ################## 3");
			logger.error(ex.getMessage(), ex);
		}
		return null;
	}

	@RequestMapping(value = "/bankcurrency.json-{bankId}", method = RequestMethod.GET, headers = Constants.REQUEST_HEADER_ACCEPT_JSON)
	public @ResponseBody
	ListData_BankCurrency[] getBankCurrencyList(@PathVariable int bankId) {

		logger.info("getReceivingMethodsList ################## 1");
		try {

			logger.info("getReceivingMethodsList ################## 2 : "
					+ bankId);

			ListData_BankCurrency[] listData = ListData_BankCurrency
					.getArray(bankCurrencyService
							.getBankCurrencyByBankId(bankId));

			return listData;

		} catch (Exception ex) {
			logger.info("getReceivingMethodsList ################## 3");
			logger.error(ex.getMessage(), ex);
		}
		return null;
	}

	@RequestMapping(value = "save", method = RequestMethod.POST)
	public @ResponseBody
	PostResponse saveCurrencyCode(
			@ModelAttribute("bankcharges") BankCharges bankCharges,
			BindingResult result) {
		logger.info("saveBankCharges ################## 1");
		if (!result.hasErrors()) {
			logger.info("saveBankCharges ################## 2");
			try {
				logger.info("saveBankCharges ################## 3");

				System.out.println("Amount : " + bankCharges.getAmount());
				System.out.println("Slab id : "
						+ bankCharges.getAmountSlabId().getId());
				System.out.println("Receiving method : "
						+ bankCharges.getReceivingMethodId().getId());
				System.out
						.println("Currency code : "
								+ bankCurrencyService
										.findById(
												bankCharges.getBankCurrency()
														.getId())
										.getCurrencyCodeId().getCurrencyCode());

				CurrencyCode currencyCode = bankCurrencyService.findById(
						bankCharges.getBankCurrency().getId())
						.getCurrencyCodeId();

				Authentication auth = SecurityContextHolder.getContext()
						.getAuthentication();
				String userName = auth.getName();

				BoUsers loggedUser = boUserService.getUserByUserName(userName);

				bankCharges.setLastUpdateUser(loggedUser.getId());
				Date currentdate = new Date();
				bankCharges.setLastUpdateDate(currentdate);

				bankCharges
						.setStatus((short) BankCharges.BANKCHARGE_STATUS_ACTIVE);

				ReceivingMethods receivingMethods = receivingMethodsService.findById(bankCharges.getReceivingMethodId().getId());
				
				bankChargesService.setInactive(
						receivingMethods,
						bankCharges.getAmountSlabId(), currencyCode);
				bankChargesService.add(bankCharges);

			} catch (Exception e) {
				logger.error(e.getMessage(), e);
				result.addError(new ObjectError("save", e.getMessage()));
			}
		}

		PostResponse response = new PostResponse(result.getAllErrors());
		return response;
	}

	@InitBinder
	public void initBinder(WebDataBinder binder) {
		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"yyyy-MM-dd'T'HH:mm:ssZ");
		dateFormat.setLenient(false);
		binder.registerCustomEditor(Date.class, new CustomDateEditor(
				dateFormat, false));
	}

	static class ListData_receivingMethods {

		private Short id;
		private String methodName;

		public Short getId() {
			return id;
		}

		public void setId(Short id) {
			this.id = id;
		}

		public String getMethodName() {
			return methodName;
		}

		public void setMethodName(String methodName) {
			this.methodName = methodName;
		}

		public static ListData_receivingMethods[] getArray(
				List<ReceivingMethods> dataList) {

			ListData_receivingMethods[] result = new ListData_receivingMethods[dataList
					.size()];
			int count = 0;
			ReceivingMethods receivingMethods;
			for (ReceivingMethods object : dataList) {
				receivingMethods = object;
				ListData_receivingMethods row = new ListData_receivingMethods();

				row.id = receivingMethods.getId();
				row.methodName = receivingMethods.getMethodName();
				result[count++] = row;
			}
			return result;
		}
	}

	static class ListData_BankCurrency {

		private Short id;
		private String currencyCodeId;
		private String corporateBankId;

		public Short getId() {
			return id;
		}

		public void setId(Short id) {
			this.id = id;
		}

		public String getCurrencyCodeId() {
			return currencyCodeId;
		}

		public void setCurrencyCodeId(String currencyCodeId) {
			this.currencyCodeId = currencyCodeId;
		}

		public String getCorporateBankId() {
			return corporateBankId;
		}

		public void setCorporateBankId(String corporateBankId) {
			this.corporateBankId = corporateBankId;
		}

		public static ListData_BankCurrency[] getArray(
				List<BankCurrency> dataList) {

			ListData_BankCurrency[] result = new ListData_BankCurrency[dataList
					.size()];
			int count = 0;
			BankCurrency bankCurrency;
			for (BankCurrency object : dataList) {
				bankCurrency = object;
				ListData_BankCurrency row = new ListData_BankCurrency();

				row.id = bankCurrency.getId();
				row.corporateBankId = bankCurrency.getCorporateBankId()
						.getBankName();
				row.currencyCodeId = bankCurrency.getCurrencyCodeId()
						.getCurrencyCode();
				result[count++] = row;
			}
			return result;
		}
	}

	static class ListData {

		private Long id;
		private double amount;
		private String status;
		private String bank;
		private String amountSlab;
		private String currency;

		
		public String getBank() {
			return bank;
		}

		public void setBank(String bank) {
			this.bank = bank;
		}

		private String receivingMethod;
		private long lastUpdateUser;
		private Date lastUpdateDate;
		private String lastUpdateDateStr;
		
		public Long getId() {
			return id;
		}

		public void setId(Long id) {
			this.id = id;
		}

		public double getAmount() {
			return amount;
		}

		public void setAmount(double amount) {
			this.amount = amount;
		}

		public long getLastUpdateUser() {
			return lastUpdateUser;
		}

		public void setLastUpdateUser(long lastUpdateUser) {
			this.lastUpdateUser = lastUpdateUser;
		}

		public Date getLastUpdateDate() {
			return lastUpdateDate;
		}

		public void setLastUpdateDate(Date lastUpdateDate) {
			this.lastUpdateDate = lastUpdateDate;
		}

		public String getStatus() {
			return status;
		}

		public void setStatus(String status) {
			this.status = status;
		}

		public static ListData[] getArray(List<Object> dataList) {

			ListData[] result = new ListData[dataList.size()];
			int count = 0;
			BankCharges bankCharges;
			for (Object object : dataList) {
				bankCharges = (BankCharges) object;
				ListData row = new ListData();

				row.id = bankCharges.getId();
				row.amount = bankCharges.getAmount();

				row.lastUpdateUser = bankCharges.getLastUpdateUser();

				if (bankCharges.getLastUpdateDate() != null) {
					row.lastUpdateDate = bankCharges.getLastUpdateDate();
				}

				row.status = (bankCharges.getStatus() == BankCharges.BANKCHARGE_STATUS_ACTIVE) ? BankCharges.BANKCHARGE_STATUS_ACTIVE_DESC
						: BankCharges.BANKCHARGE_STATUS_INACTIVE_DESC;
				row.setReceivingMethod(bankCharges.getReceivingMethodId()
						.getMethodName());
				row.setAmountSlab(bankCharges.getAmountSlabId().getLowValue()
						+ "-" + bankCharges.getAmountSlabId().getHighValue());

				row.bank = bankCharges.getReceivingMethodId()
						.getCorporateBankId().getBankName();
				row.currency = bankCharges.getBankCurrency().getCurrencyCodeId().getCurrencyCode();
				row.lastUpdateDateStr = bankCharges.getLastUpdateDate().toString();
				result[count++] = row;
			}
			return result;
		}

		public String getAmountSlab() {
			return amountSlab;
		}

		public void setAmountSlab(String amountSlab) {
			this.amountSlab = amountSlab;
		}

		public String getReceivingMethod() {
			return receivingMethod;
		}

		public void setReceivingMethod(String receivingMethod) {
			this.receivingMethod = receivingMethod;
		}

		public String getCurrency() {
			return currency;
		}

		public void setCurrency(String currency) {
			this.currency = currency;
		}

		public String getLastUpdateDateStr() {
			return lastUpdateDateStr;
		}

		public void setLastUpdateDateStr(String lastUpdateDateStr) {
			this.lastUpdateDateStr = lastUpdateDateStr;
		}
	}
}
