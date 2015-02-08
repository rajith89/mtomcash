package com.ontag.mcash.admin.web.controller;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ontag.mcash.admin.web.controller.BoRolesController.ListData;
import com.ontag.mcash.admin.web.controller.json.DataGridData;
import com.ontag.mcash.admin.web.service.AppTransactionService;
import com.ontag.mcash.admin.web.service.MerchantService;
import com.ontag.mcash.admin.web.util.Constants;
import com.ontag.mcash.dal.domain.AppTransaction;
import com.ontag.mcash.dal.domain.BankBranches;
import com.ontag.mcash.dal.domain.BankCodes;
import com.ontag.mcash.dal.domain.BankCurrency;
import com.ontag.mcash.dal.domain.BoRoles;
import com.ontag.mcash.dal.domain.CashCard;
import com.ontag.mcash.dal.domain.CorporateBank;
import com.ontag.mcash.dal.domain.Merchant;
import com.ontag.mcash.dal.domain.PaymentMethods;
import com.ontag.mcash.dal.domain.ReceiveSendCountry;
import com.ontag.mcash.dal.domain.ReceivingMethods;
import com.ontag.mcash.dal.domain.SubMerchant;

@Controller
@RequestMapping("/apptransaction")
public class AppTransactionController {

	protected static org.slf4j.Logger logger = LoggerFactory
			.getLogger(AppTransactionController.class);

	@Autowired
	private AppTransactionService appTransactionService;

	@Autowired
	private MerchantService merchantService;

	@RequestMapping(value = "/transaction-search-page", method = RequestMethod.GET)
	public String getAppTransactionSearchPage(Model model) {
		logger.info("show app-transaction-search");
		AppTransaction appTransaction = new AppTransaction();
		appTransaction.setMerchantId(null);
		List<Merchant> merchantList = null;

		try {
			merchantList = merchantService.getAllActiveMerchant();
		} catch (Exception e) {
			e.printStackTrace();
		}

		model.addAttribute("merchantList", merchantList);
		model.addAttribute("appTransaction", appTransaction);
		model.addAttribute("screenMode", "add");
		return "app-transaction-search";
	}
	
	@RequestMapping(value = "/failed-transaction-search-page", method = RequestMethod.GET)
	public String getAppTransactionFailedSearchPage(Model model) {
		logger.info("show app-transaction-search");
		AppTransaction appTransaction = new AppTransaction();
		appTransaction.setMerchantId(null);
		List<Merchant> merchantList = null;

		try {
			merchantList = merchantService.getAllActiveMerchant();
		} catch (Exception e) {
			e.printStackTrace();
		}

		model.addAttribute("merchantList", merchantList);
		model.addAttribute("appTransaction", appTransaction);
		model.addAttribute("screenMode", "add");
		return "app-transaction-search-failed";
	}

	@RequestMapping(value = "/transaction-view-page-{tranId}", method = RequestMethod.GET)
	public String getAppTransactionViewPage(Model model,
			@PathVariable int tranId) {
		AppTransactionListData listData = null;

		try {
			AppTransaction appTransaction = appTransactionService
					.findById(tranId);
			System.out.println("appTransaction : " + appTransaction);
			System.out.println("appTransaction : "
					+ appTransaction.getTransactionPin());
			listData = AppTransactionListData.getData(appTransaction);
		} catch (Exception e) {
			e.printStackTrace();
		}

		System.out.println("listData : " + listData.getTransactionPin());

		model.addAttribute("appTransaction", listData);
		return "app-transaction-view";
	}

	@RequestMapping(value = "/apptransactionlist.json-{pin}-{dateFrom}-{dateTo}-{merchantId}", method = RequestMethod.GET, headers = Constants.REQUEST_HEADER_ACCEPT_JSON)
	public @ResponseBody
	AppTransactionListData[] getAppTransactionListData(
			@PathVariable String pin, @PathVariable String dateFrom,
			@PathVariable String dateTo, @PathVariable int merchantId) {
		logger.info("getAppTransactionListData ################## 1");
		try {

			// dateFrom = dateFrom + " 00:00:00";
			// dateTo = dateTo + " 23:59:59";

			logger.info("getAppTransactionListData ################## 2 : "
					+ pin);
			logger.info("getAppTransactionListData ################## 2 : "
					+ dateFrom);
			logger.info("getAppTransactionListData ################## 2 : "
					+ dateTo);
			logger.info("getAppTransactionListData ################## 2 : "
					+ merchantId);

			if (pin.length() > 0) {
				logger.info("getAppTransactionListData from pin ################## 2 : "
						+ pin);
				AppTransactionListData[] listdata = AppTransactionListData
						.getArray(appTransactionService.findByPin(pin));
				return listdata;

			} else if (dateFrom.equals("") && merchantId != 0) {

				logger.info("getAppTransactionListData from merchant ################## 2 : "
						+ merchantId);
				Merchant merchant = new Merchant(merchantId);
				AppTransactionListData[] listdata = AppTransactionListData
						.getArray(appTransactionService
								.findByMerchant(merchant));
				return listdata;
			} else if (!dateFrom.equals("") && merchantId != 0) {
				logger.info("getAppTransactionListData from date and merchant ################## 2 : "
						+ dateFrom);
				logger.info("getAppTransactionListData from date and merchant ################## 2 : "
						+ dateTo);
				logger.info("getAppTransactionListData from date and merchant ################## 2 : "
						+ merchantId);

				SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
				Date from = formatter.parse(dateFrom);
				Date to = formatter.parse(dateTo);
				Merchant merchant = new Merchant(merchantId);
				AppTransactionListData[] listdata = AppTransactionListData
						.getArray(appTransactionService.findByMerchantAndDate(
								merchant, from, to));
				return listdata;
			} else if (!dateFrom.equals("")) {
				logger.info("getAppTransactionListData from date ################## 2 : "
						+ dateFrom);
				logger.info("getAppTransactionListData from date ################## 2 : "
						+ dateTo);
				SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
				Date from = formatter.parse(dateFrom);
				Date to = formatter.parse(dateTo);

				AppTransactionListData[] listData = AppTransactionListData
						.getArray(appTransactionService.findByDate(from, to));

				return listData;
			}

		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
		}

		return null;
	}
	
	@RequestMapping(value = "/apptransactionfaillist.json-{dateFrom}-{dateTo}-{merchantId}", method = RequestMethod.GET, headers = Constants.REQUEST_HEADER_ACCEPT_JSON)
	public @ResponseBody
	AppTransactionListData[] getAppTransactionFailListData(
			@PathVariable String dateFrom,
			@PathVariable String dateTo, @PathVariable int merchantId) {
		logger.info("getAppTransactionListData ################## 1");
		try {

			// dateFrom = dateFrom + " 00:00:00";
			// dateTo = dateTo + " 23:59:59";

			logger.info("getAppTransactionListData ################## 2 : "
					+ dateFrom);
			logger.info("getAppTransactionListData ################## 2 : "
					+ dateTo);
			logger.info("getAppTransactionListData ################## 2 : "
					+ merchantId);

			if (dateFrom.equals("") && merchantId != 0) {

				logger.info("getAppTransactionListData from merchant ################## 2 : "
						+ merchantId);
				Merchant merchant = new Merchant(merchantId);
				AppTransactionListData[] listdata = AppTransactionListData
						.getArray(appTransactionService
								.findFailTransactionByMerchant(merchant));
				return listdata;
			} else if (!dateFrom.equals("") && merchantId != 0) {
				logger.info("getAppTransactionListData from date and merchant ################## 2 : "
						+ dateFrom);
				logger.info("getAppTransactionListData from date and merchant ################## 2 : "
						+ dateTo);
				logger.info("getAppTransactionListData from date and merchant ################## 2 : "
						+ merchantId);

				SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
				Date from = formatter.parse(dateFrom);
				Date to = formatter.parse(dateTo);
				Merchant merchant = new Merchant(merchantId);
				AppTransactionListData[] listdata = AppTransactionListData
						.getArray(appTransactionService.findFailTransactionByMerchantAndDate(
								merchant, from, to));
				return listdata;
			} else if (!dateFrom.equals("")) {
				logger.info("getAppTransactionListData from date ################## 2 : "
						+ dateFrom);
				logger.info("getAppTransactionListData from date ################## 2 : "
						+ dateTo);
				SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
				Date from = formatter.parse(dateFrom);
				Date to = formatter.parse(dateTo);

				AppTransactionListData[] listData = AppTransactionListData
						.getArray(appTransactionService.findFailTransactionByDate(from, to));

				return listData;
			}

		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
		}

		return null;
	}

	public static class AppTransactionListData {

		private long id;
		private String transactionPin;
		private double trnxAmount;
		private String trnxCurrency;
		private String senderName;
		private double senderAmount;
		private String sendCurrency;
		private String senderContactNo;
		private String senderCountry;
		private String beneficiaryName;
		private String beneficiaryContactNo;
		private double beneficiaryAmount;
		private String benificiaryCurrency;
		private String wsResponceDesc;
		private double merchantComission;
		private double companyComission;
		private double bankCharge;
		private String datePerformed;
		private double receiveAmount;
		private String receivingMethod;
		private String merchantId;
		private String accountno;
		private String bank;
		private String bankbranch;
		private String sendOption;
		private double profit;
		private String status;
		private String submerchant;
		private String wsResponcecode;
		private double submerchantcommission;

		public long getId() {
			return id;
		}

		public void setId(long id) {
			this.id = id;
		}

		public String getTransactionPin() {
			return transactionPin;
		}

		public void setTransactionPin(String transactionPin) {
			this.transactionPin = transactionPin;
		}

		public double getSendAmount() {
			return trnxAmount;
		}

		public void setSendAmount(double sendAmount) {
			this.trnxAmount = sendAmount;
		}

		public String getSenderName() {
			return senderName;
		}

		public void setSenderName(String senderName) {
			this.senderName = senderName;
		}

		public String getBeneficiaryName() {
			return beneficiaryName;
		}

		public void setBeneficiaryName(String beneficiaryName) {
			this.beneficiaryName = beneficiaryName;
		}

		public String getWsResponceDesc() {
			return wsResponceDesc;
		}

		public void setWsResponceDesc(String wsResponceDesc) {
			this.wsResponceDesc = wsResponceDesc;
		}

		public double getMerchantComission() {
			return merchantComission;
		}

		public void setMerchantComission(double merchantComission) {
			this.merchantComission = merchantComission;
		}

		public double getCompanyComission() {
			return companyComission;
		}

		public void setCompanyComission(double companyComission) {
			this.companyComission = companyComission;
		}

		public double getBankCharge() {
			return bankCharge;
		}

		public void setBankCharge(double bankCharge) {
			this.bankCharge = bankCharge;
		}

		public String getDatePerformed() {
			return datePerformed;
		}

		public void setDatePerformed(String datePerformed) {
			this.datePerformed = datePerformed;
		}

		public double getReceiveAmount() {
			return receiveAmount;
		}

		public void setReceiveAmount(double receiveAmount) {
			this.receiveAmount = receiveAmount;
		}

		public String getReceivingMethod() {
			return receivingMethod;
		}

		public void setReceivingMethod(String receivingMethod) {
			this.receivingMethod = receivingMethod;
		}

		public String getMerchantId() {
			return merchantId;
		}

		public void setMerchantId(String merchantId) {
			this.merchantId = merchantId;
		}

		public String getStatus() {
			return status;
		}

		public void setStatus(String status) {
			this.status = status;
		}

		public double getTrnxAmount() {
			return trnxAmount;
		}

		public void setTrnxAmount(double trnxAmount) {
			this.trnxAmount = trnxAmount;
		}

		public double getBeneficiaryAmount() {
			return beneficiaryAmount;
		}

		public void setBeneficiaryAmount(double beneficiaryAmount) {
			this.beneficiaryAmount = beneficiaryAmount;
		}

		public String getSendOption() {
			return sendOption;
		}

		public void setSendOption(String sendOption) {
			this.sendOption = sendOption;
		}

		public double getProfit() {
			return profit;
		}

		public void setProfit(double profit) {
			this.profit = profit;
		}

		public String getSendCurrency() {
			return sendCurrency;
		}

		public void setSendCurrency(String sendCurrency) {
			this.sendCurrency = sendCurrency;
		}

		public String getBenificiaryCurrency() {
			return benificiaryCurrency;
		}

		public void setBenificiaryCurrency(String benificiaryCurrency) {
			this.benificiaryCurrency = benificiaryCurrency;
		}

		public String getTrnxCurrency() {
			return trnxCurrency;
		}

		public void setTrnxCurrency(String trnxCurrency) {
			this.trnxCurrency = trnxCurrency;
		}

		public static AppTransactionListData getData(
				AppTransaction appTransaction) {

			AppTransactionListData data = new AppTransactionListData();

			data.id = appTransaction.getId();
			data.transactionPin = appTransaction.getTransactionPin();
			data.senderName = appTransaction.getSenderName();
			data.beneficiaryName = appTransaction.getBeneficiaryName();
			data.merchantId = appTransaction.getMerchantId().getFirstName()
					+ " " + appTransaction.getMerchantId().getLastName();
			data.merchantComission = appTransaction.getMerchantComission();
			data.companyComission = appTransaction.getCompanyComission();
			data.bankCharge = appTransaction.getBankCharge();
			data.receivingMethod = appTransaction.getReceivingMethod()
					.getMethodName();

			SimpleDateFormat formatter = new SimpleDateFormat(
					"dd-MM-yyyy h:mm a");
			data.datePerformed = formatter.format(appTransaction
					.getDatePerformed());

			data.trnxAmount = appTransaction.getTrnxAmount();
			data.trnxCurrency = appTransaction.getReceiveBankCurrencyCode()
					.getCurrencyCodeId().getCurrencyCode();
			data.status = appTransaction.getWsResponceDesc();
			data.senderContactNo = appTransaction.getSenderContactNo();
			data.beneficiaryContactNo = appTransaction
					.getBeneficiaryContactNo();
			data.accountno = appTransaction.getAccountNo();
			if (appTransaction.getBankCode() != null)
				data.bank = appTransaction.getBankCode().getBankDesc();
			if (appTransaction.getBankBranchCode() != null)
				data.bankbranch = appTransaction.getBankBranchCode()
						.getBranchDesc();

			data.senderCountry = appTransaction.getSenderCountry()
					.getCountryId().getCountryDesc();
			data.status = appTransaction.getWsResponceDesc();

			return data;
		}

		public static AppTransactionListData[] getArray(
				List<AppTransaction> dataList) {

			AppTransactionListData[] result = new AppTransactionListData[dataList
					.size()];
			int count = 0;
			AppTransaction appTransaction;
			for (Object object : dataList) {
				appTransaction = (AppTransaction) object;
				AppTransactionListData row = new AppTransactionListData();

				row.id = appTransaction.getId();
				row.transactionPin = appTransaction.getTransactionPin();
				row.senderName = appTransaction.getSenderName();
				row.beneficiaryName = appTransaction.getBeneficiaryName();
				row.merchantId = appTransaction.getMerchantId().getFirstName()
						+ " " + appTransaction.getMerchantId().getLastName();
				row.merchantComission = appTransaction.getMerchantComission();
				row.companyComission = appTransaction.getCompanyComission();
				row.bankCharge = appTransaction.getBankCharge();
				row.receivingMethod = appTransaction.getReceivingMethod()
						.getMethodName();

				SimpleDateFormat formatter = new SimpleDateFormat(
						"dd-MM-yyyy h:mm a");
				row.datePerformed = formatter.format(appTransaction
						.getDatePerformed());
				
				DecimalFormat df = new DecimalFormat("###.##");
				
				
				
				row.trnxAmount = Double.parseDouble(df.format(appTransaction.getTrnxAmount())) ;
				row.trnxCurrency = appTransaction.getReceiveBankCurrencyCode()
						.getCurrencyCodeId().getCurrencyCode();
				row.status = appTransaction.getWsResponceDesc();
				row.senderAmount = Double.parseDouble(df.format(appTransaction.getSendAmount())) ;
				
				System.out.println(appTransaction.getSendAmount() + "--" + row.senderAmount);
				row.sendCurrency = appTransaction.getSendBankCurrencyCode()
						.getCurrencyCodeId().getCurrencyCode();

				row.beneficiaryAmount = appTransaction.getReceiveAmount();

				row.benificiaryCurrency = appTransaction
						.getReceiveBankCurrencyCode().getCurrencyCodeId()
						.getCurrencyCode();

				row.sendOption = appTransaction.getSendOption() == AppTransaction.OPTION_EXCLUDE_COMMISSION ? AppTransaction.OPTION_EXCLUDE_COMMISSION_DESC
						: AppTransaction.OPTION_INCLUDE_COMMISSION_DESC;

				row.profit = appTransaction.getProfit();
				if (appTransaction.getSubMerchantId() != null)
					row.submerchant = appTransaction.getSubMerchantId()
							.getFirstName()
							+ " "
							+ appTransaction.getSubMerchantId().getLastName();
				else
					row.submerchant = "";

				row.wsResponcecode = appTransaction.getWsResponeCode();
				row.submerchantcommission = appTransaction
						.getSubMerchantCommision();
				// row.profit = String
				// .valueOf(appTransaction.getCompanyComission()
				// - appTransaction.getBankCharge());

				result[count++] = row;
			}
			return result;
		}

		public String getSenderContactNo() {
			return senderContactNo;
		}

		public void setSenderContactNo(String senderContactNo) {
			this.senderContactNo = senderContactNo;
		}

		public String getBeneficiaryContactNo() {
			return beneficiaryContactNo;
		}

		public void setBeneficiaryContactNo(String beneficiaryContactNo) {
			this.beneficiaryContactNo = beneficiaryContactNo;
		}

		public String getAccountno() {
			return accountno;
		}

		public void setAccountno(String accountno) {
			this.accountno = accountno;
		}

		public String getBank() {
			return bank;
		}

		public void setBank(String bank) {
			this.bank = bank;
		}

		public String getBankbranch() {
			return bankbranch;
		}

		public void setBankbranch(String bankbranch) {
			this.bankbranch = bankbranch;
		}

		public String getSenderCountry() {
			return senderCountry;
		}

		public void setSenderCountry(String senderCountry) {
			this.senderCountry = senderCountry;
		}

		public double getSenderAmount() {
			return senderAmount;
		}

		public void setSenderAmount(double senderAmount) {
			this.senderAmount = senderAmount;
		}

		public String getSubmerchant() {
			return submerchant;
		}

		public void setSubmerchant(String submerchant) {
			this.submerchant = submerchant;
		}

		public String getWsResponcecode() {
			return wsResponcecode;
		}

		public void setWsResponcecode(String wsResponcecode) {
			this.wsResponcecode = wsResponcecode;
		}

		public double getSubmerchantcommission() {
			return submerchantcommission;
		}

		public void setSubmerchantcommission(double submerchantcommission) {
			this.submerchantcommission = submerchantcommission;
		}
	}

}
