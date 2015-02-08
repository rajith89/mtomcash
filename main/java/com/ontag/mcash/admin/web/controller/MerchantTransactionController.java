package com.ontag.mcash.admin.web.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javassist.bytecode.stackmap.Liveness;

import org.hibernate.ejb.criteria.ValueHandlerFactory.DoubleValueHandler;
import org.hibernate.mapping.Value;
import org.objectweb.asm.tree.IntInsnNode;
import org.omg.CORBA.portable.ValueOutputStream;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ontag.mcash.admin.web.controller.AppTransactionController.AppTransactionListData;
import com.ontag.mcash.admin.web.service.AppTransactionService;
import com.ontag.mcash.admin.web.service.MerchantService;
import com.ontag.mcash.admin.web.service.MerchantTransactionService;
import com.ontag.mcash.admin.web.service.ReceivingMethodsService;
import com.ontag.mcash.admin.web.util.Constants;
import com.ontag.mcash.dal.domain.AppTransaction;
import com.ontag.mcash.dal.domain.Merchant;
import com.ontag.mcash.dal.domain.MerchantTransaction;
import com.ontag.mcash.dal.domain.ReceivingMethods;

@Controller
@RequestMapping("/merchanttransaction")
public class MerchantTransactionController {

	protected static org.slf4j.Logger logger = LoggerFactory
			.getLogger(MerchantTransactionController.class);

	@Autowired
	private MerchantTransactionService merchantTransactionService;

	@Autowired
	private MerchantService merchantService;

	@Autowired
	private AppTransactionService appTransactionService;

	@Autowired
	private ReceivingMethodsService receivingMethodsService;

	@RequestMapping(value = "/merchant-topup-report", method = RequestMethod.GET)
	public String getMerchantTopupReportPage(Model model) {
		logger.info("show merchant-topup-report");
		MerchantTransaction merchantTransaction = new MerchantTransaction();

		List<Merchant> merchantList = null;

		try {
			merchantList = merchantService.getAllActiveMerchant();
		} catch (Exception e) {
			e.printStackTrace();
		}

		model.addAttribute("merchantList", merchantList);
		model.addAttribute("merchantTransaction", merchantTransaction);
		model.addAttribute("screenMode", "add");

		return "merchant-topup-report";
	}

	@RequestMapping(value = "/merchant-cash-book", method = RequestMethod.GET)
	public String getMerchantCashBookPage(Model model) {
		logger.info("show merchant-cash-book");
		MerchantTransaction merchantTransaction = new MerchantTransaction();

		List<Merchant> merchantList = null;

		try {
			merchantList = merchantService.getAllActiveMerchant();
		} catch (Exception e) {
			e.printStackTrace();
		}

		model.addAttribute("merchantList", merchantList);
		model.addAttribute("merchantTransaction", merchantTransaction);
		model.addAttribute("screenMode", "add");

		return "merchant-cash-book";
	}

	@RequestMapping(value = "/merchant-topup-summary-report", method = RequestMethod.GET)
	public String getMerchantTopupSummaryReport(Model model) {
		logger.info("show merchant-topup-summary-report");
		MerchantTransaction merchantTransaction = new MerchantTransaction();
		model.addAttribute("merchantTransaction", merchantTransaction);
		return "merchant-topup-summary-report";
	}

	@RequestMapping(value = "/merchant-commission-summary-report", method = RequestMethod.GET)
	public String getMerchantCommissionSummaryReport(Model model) {
		logger.info("show merchant-commission-summary-report");
		MerchantTransaction merchantTransaction = new MerchantTransaction();
		model.addAttribute("merchantTransaction", merchantTransaction);
		return "merchant-commission-summary-report";
	}

	@RequestMapping(value = "/merchant-income-summary-report", method = RequestMethod.GET)
	public String getMerchantIncomeSummaryReport(Model model) {
		logger.info("show merchant-commission-summary-report");
		MerchantTransaction merchantTransaction = new MerchantTransaction();

		List<ReceivingMethods> receivingMethods = null;

		try {
			receivingMethods = receivingMethodsService
					.getAllActiveRecievingMethods();
			// System.out.println(receivingMethods.size());
		} catch (Exception e) {
			e.printStackTrace();
		}

		model.addAttribute("receivingMethods", receivingMethods);
		model.addAttribute("merchantTransaction", merchantTransaction);
		return "merchant-income-summary-report";
	}

	@RequestMapping(value = "/merchant-profit-summary-report", method = RequestMethod.GET)
	public String getMerchantProfitSummaryReport(Model model) {
		logger.info("show merchant-commission-summary-report");
		MerchantTransaction merchantTransaction = new MerchantTransaction();

		List<ReceivingMethods> receivingMethods = null;

		try {
			receivingMethods = receivingMethodsService
					.getAllActiveRecievingMethods();
			// System.out.println(receivingMethods.size());
		} catch (Exception e) {
			e.printStackTrace();
		}

		model.addAttribute("receivingMethods", receivingMethods);
		model.addAttribute("merchantTransaction", merchantTransaction);
		return "merchant-profit-summary-report";
	}

	@RequestMapping(value = "/merchant-commission-report", method = RequestMethod.GET)
	public String getMerchantCommissionReport(Model model) {
		logger.info("show merchant-commission-report");
		MerchantTransaction merchantTransaction = new MerchantTransaction();
		model.addAttribute("merchantTransaction", merchantTransaction);
		return "merchant-commission-report";
	}

	@RequestMapping(value = "/merchanttransactionlist.json-{dateFrom}-{dateTo}-{merchantId}", method = RequestMethod.GET, headers = Constants.REQUEST_HEADER_ACCEPT_JSON)
	public @ResponseBody
	MerchantTransactionListData[] getMerchantTransactionListData(
			@PathVariable String dateFrom, @PathVariable String dateTo,
			@PathVariable int merchantId) {
		logger.info("getMerchantTransactionListData ################## 1");

		try {

			// dateFrom = dateFrom + " 00:00:00";
			// dateTo = dateTo + " 23:59:59";

			logger.info("getMerchantTransactionListData ################## 2 : "
					+ dateFrom);
			logger.info("getMerchantTransactionListData ################## 2 : "
					+ dateTo);
			logger.info("getMerchantTransactionListData ################## 2 : "
					+ merchantId);

			if (!dateFrom.equals("") && merchantId != 0) {

				SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
				Date from = formatter.parse(dateFrom);
				Date to = formatter.parse(dateTo);
				Merchant merchant = new Merchant(merchantId);
				MerchantTransactionListData[] listdata = MerchantTransactionListData
						.getArray(merchantTransactionService
								.getMerchantTransactionByDateAndMerchant(from,
										to, merchant));

				return listdata;
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

	@RequestMapping(value = "/merchantcashbook.json-{merchantId}", method = RequestMethod.GET, headers = Constants.REQUEST_HEADER_ACCEPT_JSON)
	public @ResponseBody
	MerchantTransactionListData[] getMerchantCashBookListData(
			@PathVariable int merchantId) {
		logger.info("getMerchantCashBookListData ################## 1");
		try {

			logger.info("getMerchantCashBookListData ################## 2 : "
					+ merchantId);

			if (merchantId != 0) {

				Merchant merchant = new Merchant(merchantId);
				MerchantTransactionListData[] listdata = MerchantTransactionListData
						.getArray(merchantTransactionService
								.getMerchantTransactionByMerchant(merchant));

				return listdata;
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

	@RequestMapping(value = "/merchanttopupsummarylist.json-{dateFrom}-{dateTo}", method = RequestMethod.GET, headers = Constants.REQUEST_HEADER_ACCEPT_JSON)
	public @ResponseBody
	MerchantTopupSummaryList[] getMerchantTopupSummaryListData(
			@PathVariable String dateFrom, @PathVariable String dateTo) {
		logger.info("getMerchantTransactionListData ################## 1");
		try {

			// dateFrom = dateFrom + " 00:00:00";
			// dateTo = dateTo + " 23:59:59";

			logger.info("getMerchantTransactionListData ################## 2 : "
					+ dateFrom);
			logger.info("getMerchantTransactionListData ################## 2 : "
					+ dateTo);

			if (!dateFrom.equals("")) {

				SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
				Date from = formatter.parse(dateFrom);
				Date to = formatter.parse(dateTo);
				MerchantTopupSummaryList[] listdata = MerchantTopupSummaryList
						.getArray(merchantTransactionService
								.getMerchantTopupSummery(from, to));

				return listdata;
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

	@RequestMapping(value = "/merchantcommissionsummarylist.json-{dateFrom}-{dateTo}", method = RequestMethod.GET, headers = Constants.REQUEST_HEADER_ACCEPT_JSON)
	public @ResponseBody
	MerchantCommissionSummaryList[] getMerchantCommissionSummaryListData(
			@PathVariable String dateFrom, @PathVariable String dateTo) {
		logger.info("getMerchantCommissionSummaryListData ################## 1");
		try {

			// dateFrom = dateFrom + " 00:00:00";
			// dateTo = dateTo + " 23:59:59";

			logger.info("getMerchantCommissionSummaryListData ################## 2 : "
					+ dateFrom);
			logger.info("getMerchantCommissionSummaryListData ################## 2 : "
					+ dateTo);

			if (!dateFrom.equals("")) {

				SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
				Date from = formatter.parse(dateFrom);
				Date to = formatter.parse(dateTo);
				MerchantCommissionSummaryList[] listdata = MerchantCommissionSummaryList
						.getArray(appTransactionService
								.getMerchantCommissionSummary(from, to));

				return listdata;
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

	@RequestMapping(value = "/merchantincomesummarylist.json-{dateFrom}-{dateTo}-{type}", method = RequestMethod.GET, headers = Constants.REQUEST_HEADER_ACCEPT_JSON)
	public @ResponseBody
	MerchantIncomeSummaryList[] getMerchantIncomeSummaryListData(
			@PathVariable String dateFrom, @PathVariable String dateTo,
			@PathVariable String type) {
		logger.info("getMerchantIncomeSummaryListData ################## 1");
		try {

			// dateFrom = dateFrom + " 00:00:00";
			// dateTo = dateTo + " 23:59:59";

			logger.info("getMerchantIncomeSummaryListData ################## 2 : "
					+ dateFrom);
			logger.info("getMerchantIncomeSummaryListData ################## 2 : "
					+ dateTo);
			logger.info("getMerchantIncomeSummaryListData ################## 2 : "
					+ type);

			if (!dateFrom.equals("") && type.equals("0")) {

				SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
				Date from = formatter.parse(dateFrom);
				Date to = formatter.parse(dateTo);
				MerchantIncomeSummaryList[] listdata = MerchantIncomeSummaryList
						.getArray(appTransactionService.getIncomeSummaryByDate(
								from, to));

				return listdata;
			}

			if (dateFrom.equals("") && !type.equals("0")) {

				MerchantIncomeSummaryList[] listdata = MerchantIncomeSummaryList
						.getArray(appTransactionService
								.getIncomeSummaryByTransactionType(receivingMethodsService
										.findById(Short.parseShort(type))));

				return listdata;
			}

			if (!dateFrom.equals("") && !type.equals("0")) {

				SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
				Date from = formatter.parse(dateFrom);
				Date to = formatter.parse(dateTo);

				MerchantIncomeSummaryList[] listdata = MerchantIncomeSummaryList
						.getArray(appTransactionService
								.getIncomeSummaryByDateAndTransactionType(from,
										to, receivingMethodsService
												.findById(Short
														.parseShort(type))));

				return listdata;
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

	@RequestMapping(value = "/merchantprofitsummarylist.json-{dateFrom}-{dateTo}-{type}", method = RequestMethod.GET, headers = Constants.REQUEST_HEADER_ACCEPT_JSON)
	public @ResponseBody
	MerchantProfitSummaryList[] getMerchantProfitSummaryListData(
			@PathVariable String dateFrom, @PathVariable String dateTo,
			@PathVariable String type) {
		logger.info("getMerchantProfitSummaryListData ################## 1");
		try {

			// dateFrom = dateFrom + " 00:00:00";
			// dateTo = dateTo + " 23:59:59";

			logger.info("getMerchantProfitSummaryListData ################## 2 : "
					+ dateFrom);
			logger.info("getMerchantProfitSummaryListData ################## 2 : "
					+ dateTo);
			logger.info("getMerchantProfitSummaryListData ################## 2 : "
					+ type);

			if (!dateFrom.equals("") && type.equals("0")) {

				SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
				Date from = formatter.parse(dateFrom);
				Date to = formatter.parse(dateTo);
				MerchantProfitSummaryList[] listdata = MerchantProfitSummaryList
						.getArray(appTransactionService.getProfitByDate(from,
								to));

				return listdata;
			}

			if (dateFrom.equals("") && !type.equals("0")) {

				MerchantProfitSummaryList[] listdata = MerchantProfitSummaryList
						.getArray(appTransactionService
								.getProfitByTransactionType(receivingMethodsService
										.findById(Short.parseShort(type))));

				return listdata;
			}

			if (!dateFrom.equals("") && !type.equals("0")) {

				SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
				Date from = formatter.parse(dateFrom);
				Date to = formatter.parse(dateTo);

				MerchantProfitSummaryList[] listdata = MerchantProfitSummaryList
						.getArray(appTransactionService
								.getProfitByDateAndTransactionType(from, to,
										receivingMethodsService.findById(Short
												.parseShort(type))));

				return listdata;
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

	@RequestMapping(value = "/merchantcommissionlist.json-{dateFrom}-{dateTo}", method = RequestMethod.GET, headers = Constants.REQUEST_HEADER_ACCEPT_JSON)
	public @ResponseBody
	MerchantCommissionList[] getMerchantCommissionListData(
			@PathVariable String dateFrom, @PathVariable String dateTo) {
		logger.info("getMerchantCommissionListData ################## 1");
		try {

			// dateFrom = dateFrom + " 00:00:00";
			// dateTo = dateTo + " 23:59:59";

			logger.info("getMerchantCommissionListData ################## 2 : "
					+ dateFrom);
			logger.info("getMerchantCommissionListData ################## 2 : "
					+ dateTo);

			if (!dateFrom.equals("")) {

				SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
				Date from = formatter.parse(dateFrom);
				Date to = formatter.parse(dateTo);
				MerchantCommissionList[] listdata = MerchantCommissionList.getArray(appTransactionService.getMerchantCommissionsByDate(from, to));

				return listdata;
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

	static class MerchantTransactionListData {

		private String description;
		private String crdr;
		private String dateCreated;
		private double trnxAmount;
		private String balance;

		public String getDescription() {
			return description;
		}

		public void setDescription(String description) {
			this.description = description;
		}

		public String getDateCreated() {
			return dateCreated;
		}

		public void setDateCreated(String dateCreated) {
			this.dateCreated = dateCreated;
		}

		public double getTrnxAmount() {
			return trnxAmount;
		}

		public void setTrnxAmount(double trnxAmount) {
			this.trnxAmount = trnxAmount;
		}

		public String getCrdr() {
			return crdr;
		}

		public void setCrdr(String crdr) {
			this.crdr = crdr;
		}

		public String getBalance() {
			return balance;
		}

		public void setBalance(String balance) {
			this.balance = balance;
		}

		public static MerchantTransactionListData[] getArray(
				List<MerchantTransaction> dataList) {

			MerchantTransactionListData[] result = new MerchantTransactionListData[dataList
					.size()];
			int count = 0;
			MerchantTransaction merchantTransaction;
			for (Object object : dataList) {
				merchantTransaction = (MerchantTransaction) object;
				MerchantTransactionListData row = new MerchantTransactionListData();

				row.balance = String.valueOf(merchantTransaction.getBalance());
				row.crdr = merchantTransaction.getCrdr();
				row.description = merchantTransaction.getDescription();
				SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
				row.dateCreated = formatter.format(merchantTransaction
						.getDateCreated());
				row.trnxAmount = merchantTransaction.getTrnxAmount();

				result[count++] = row;
			}
			return result;
		}
	}

	static class MerchantTopupSummaryList {

		private String merchant;
		private double total;

		public String getMerchant() {
			return merchant;
		}

		public void setMerchant(String merchant) {
			this.merchant = merchant;
		}

		public double getTotal() {
			return total;
		}

		public void setTotal(double total) {
			this.total = total;
		}

		public static MerchantTopupSummaryList[] getArray(
				List<Object[]> dataList) {

			MerchantTopupSummaryList[] result = new MerchantTopupSummaryList[dataList
					.size()];
			int count = 0;

			for (Object[] list : dataList) {

				MerchantTopupSummaryList row = new MerchantTopupSummaryList();

				row.merchant = list[1].toString() + " " + list[2];
				row.total = (double) list[3];

				result[count++] = row;
			}
			return result;
		}
	}

	static class MerchantCommissionSummaryList {

		private String merchant;
		private double total;
		private String currency;

		public String getMerchant() {
			return merchant;
		}

		public void setMerchant(String merchant) {
			this.merchant = merchant;
		}

		public double getTotal() {
			return total;
		}

		public void setTotal(double total) {
			this.total = total;
		}

		public String getCurrency() {
			return currency;
		}

		public void setCurrency(String currency) {
			this.currency = currency;
		}

		public static MerchantCommissionSummaryList[] getArray(
				List<Object[]> dataList) {

			MerchantCommissionSummaryList[] result = new MerchantCommissionSummaryList[dataList
					.size()];
			int count = 0;

			for (Object[] list : dataList) {

				MerchantCommissionSummaryList row = new MerchantCommissionSummaryList();

				// System.out.println(list[3]);

				row.merchant = list[1].toString() + " " + list[2];
				row.total = (double) list[4];
				row.currency = list[3].toString();

				result[count++] = row;
			}
			return result;
		}
	}

	static class MerchantIncomeSummaryList {

		private String merchant;
		private double totalSendAmount;
		private double totalMerchantCommission;
		private double totalCompanyCommission;
		private double totalIncome;
		private String currency;

		public String getMerchant() {
			return merchant;
		}

		public void setMerchant(String merchant) {
			this.merchant = merchant;
		}

		public double getTotalSendAmount() {
			return totalSendAmount;
		}

		public void setTotalSendAmount(double totalSendAmount) {
			this.totalSendAmount = totalSendAmount;
		}

		public double getTotalMerchantCommission() {
			return totalMerchantCommission;
		}

		public void setTotalMerchantCommission(double totalMerchantCommission) {
			this.totalMerchantCommission = totalMerchantCommission;
		}

		public double getTotalCompanyCommission() {
			return totalCompanyCommission;
		}

		public void setTotalCompanyCommission(double totalCompanyCommission) {
			this.totalCompanyCommission = totalCompanyCommission;
		}

		public double getTotalIncome() {
			return totalIncome;
		}

		public void setTotalIncome(double totalIncome) {
			this.totalIncome = totalIncome;
		}

		public String getCurrency() {
			return currency;
		}

		public void setCurrency(String currency) {
			this.currency = currency;
		}

		public static MerchantIncomeSummaryList[] getArray(
				List<Object[]> dataList) {

			MerchantIncomeSummaryList[] result = new MerchantIncomeSummaryList[dataList
					.size()];
			int count = 0;

			for (Object[] list : dataList) {

				MerchantIncomeSummaryList row = new MerchantIncomeSummaryList();

				row.currency = list[6].toString();
				row.merchant = list[1].toString() + " " + list[2];
				row.totalSendAmount = (double) list[3] - (double) list[4]
						- (double) list[5];
				row.totalMerchantCommission = (double) list[4];
				row.totalCompanyCommission = (double) list[5];
				row.totalIncome = (double) list[3];

				result[count++] = row;
			}
			return result;
		}
	}

	static class MerchantProfitSummaryList {

		private String merchant;
		private double profit;
		private String currency;

		public double getProfit() {
			return profit;
		}

		public void setProfit(double profit) {
			this.profit = profit;
		}

		public String getMerchant() {
			return merchant;
		}

		public void setMerchant(String merchant) {
			this.merchant = merchant;
		}

		public String getCurrency() {
			return currency;
		}

		public void setCurrency(String currency) {
			this.currency = currency;
		}

		public static MerchantProfitSummaryList[] getArray(
				List<Object[]> dataList) {

			MerchantProfitSummaryList[] result = new MerchantProfitSummaryList[dataList
					.size()];
			int count = 0;

			for (Object[] list : dataList) {

				MerchantProfitSummaryList row = new MerchantProfitSummaryList();

				row.merchant = list[1].toString() + " " + list[2];
				row.profit = (double) list[3];

				row.currency = "LKR";
				// TODO Currency Code should come dynamically

				result[count++] = row;
			}
			return result;
		}
	}

	static class MerchantCommissionList {

		private String merchant;
		private double commission;
		private String currency;
		private String date;

		public String getMerchant() {
			return merchant;
		}

		public void setMerchant(String merchant) {
			this.merchant = merchant;
		}

		public double getCommission() {
			return commission;
		}

		public void setCommission(double commission) {
			this.commission = commission;
		}

		public String getCurrency() {
			return currency;
		}

		public void setCurrency(String currency) {
			this.currency = currency;
		}

		public String getDate() {
			return date;
		}

		public void setDate(String date) {
			this.date = date;
		}

		public static MerchantCommissionList[] getArray(
				List<AppTransaction> dataList) {

			MerchantCommissionList[] result = new MerchantCommissionList[dataList
					.size()];
			int count = 0;

			for (AppTransaction list : dataList) {

				MerchantCommissionList row = new MerchantCommissionList();

				// System.out.println(list[3]);

				row.merchant = list.getMerchantId().getFirstName() + " "
						+ list.getMerchantId().getLastName();
				row.commission = list.getMerchantComission();
				row.currency = list.getMerchantId().getCountryCurrency()
						.getCurrencyCodeId().getCurrencyCode();

				SimpleDateFormat formatter = new SimpleDateFormat(
						"dd-MM-yyyy h:mm a");
				row.date = formatter.format(list.getDatePerformed());
			//	System.out.println(row.merchant + " " + row.date + " " + row.commission + " " + row.currency) ;

				result[count++] = row;
			}
			return result;
		}
	}
}
