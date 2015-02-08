package com.ontag.mcash.admin.web.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

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

import com.ontag.mcash.admin.web.controller.AppTransactionController.AppTransactionListData;
import com.ontag.mcash.admin.web.controller.CardController.ListCardData;
import com.ontag.mcash.admin.web.controller.CurrencyController.CurrencyData;
import com.ontag.mcash.admin.web.controller.json.DataGridData;
import com.ontag.mcash.admin.web.controller.json.MerchantFilter;
import com.ontag.mcash.admin.web.controller.json.PostResponse;
import com.ontag.mcash.admin.web.service.AmountSlabService;
import com.ontag.mcash.admin.web.service.BoUserService;
import com.ontag.mcash.admin.web.service.CountryService;
import com.ontag.mcash.admin.web.service.CurrencyService;
import com.ontag.mcash.admin.web.service.MerchantCommissionService;
import com.ontag.mcash.admin.web.service.MerchantService;
import com.ontag.mcash.admin.web.service.MerchantTransactionService;
import com.ontag.mcash.admin.web.util.Constants;
import com.ontag.mcash.dal.domain.AmountSlab;
import com.ontag.mcash.dal.domain.BoUsers;
import com.ontag.mcash.dal.domain.CashCardBatch;
import com.ontag.mcash.dal.domain.Country;
import com.ontag.mcash.dal.domain.Merchant;
import com.ontag.mcash.dal.domain.MerchantCommision;
import com.ontag.mcash.dal.domain.MerchantTransaction;
import com.ontag.mcash.dal.domain.ReceiveSendCountry;

@Controller
@RequestMapping("/merchant")
public class MerchantController {

	protected static org.slf4j.Logger logger = LoggerFactory
			.getLogger(MerchantController.class);

	@Autowired
	private MerchantService merchantService;

	@Autowired
	private CountryService countryService;

	@Autowired
	private MerchantTransactionService merchantTransactionService;

	@Autowired
	private AmountSlabService amountSlabService;

	@Autowired
	MerchantCommissionService merchantCommissionService;

	@Autowired
	CurrencyService currencyService;

	@Autowired
	BoUserService boUserService;

	@InitBinder
	public void initBinder(WebDataBinder binder) {
		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"yyyy-MM-dd'T'HH:mm:ssZ");
		dateFormat.setLenient(false);
		binder.registerCustomEditor(Date.class, new CustomDateEditor(
				dateFormat, false));
	}

	@RequestMapping(value = "getmerchantcurrency-{merchantId}", method = RequestMethod.POST)
    public @ResponseBody ListCurrencyData
    getMerchantCurrency(@PathVariable int merchantId) {
		ListCurrencyData listCurrencyData = new ListCurrencyData();
    	try {
    			Merchant merchant = merchantService.findById(merchantId);
	        	 
    			listCurrencyData.currencycode = merchant.getCountryCurrency().getCurrencyCodeId().getCurrencyDesc();
            } catch (Exception e) {
            	e.printStackTrace();
            }
    	return listCurrencyData;
    }
	
//	@RequestMapping(value = "/apptransactionlist.json-{pin}-{dateFrom}-{dateTo}-{merchantId}", method = RequestMethod.GET, headers = Constants.REQUEST_HEADER_ACCEPT_JSON)
//	public @ResponseBody
//	AppTransactionListData[] getAppTransactionListData(
//			@PathVariable String pin, @PathVariable String dateFrom,
//			@PathVariable String dateTo, @PathVariable int merchantId) {
//		logger.info("getAppTransactionListData ################## 1");

	
	@RequestMapping(value = "getmerchantcommission-{merchantId}-{amountSlab}", method = RequestMethod.GET)
    public @ResponseBody ListDataMerchantCommission getMerchantCommission(@PathVariable int merchantId, @PathVariable int amountSlab) {
		ListDataMerchantCommission listDataMerchantCommission = new ListDataMerchantCommission();
    	try {
    			MerchantCommision merchantCommision = merchantCommissionService.findByMerchantAndAmountSlab(merchantId, amountSlab);
    			if(merchantCommision != null)
    				listDataMerchantCommission.amount = merchantCommision.getAmount();
    			else
    				listDataMerchantCommission.amount = 0;
            } catch (Exception e) {
            	e.printStackTrace();
            }
    	return listDataMerchantCommission;
    }
	
//	@RequestMapping(value = "getmerchantcommission_{merchantId}_{amountslab}", method = RequestMethod.GET)
//    public @ResponseBody ListDataMerchantCommission getMerchantCommission(@PathVariable int merchantId, @PathVariable int amountslabId) {
//		ListDataMerchantCommission listDataMerchantCommission = new ListDataMerchantCommission();
//    	try {
//    			MerchantCommision merchantCommision = merchantCommissionService.findByMerchantAndAmountSlab(merchantId, amountslabId);
//    			if(merchantCommision != null)
//    				listDataMerchantCommission.amount = merchantCommision.getAmount();
//    			else
//    				listDataMerchantCommission.amount = 0;
//            } catch (Exception e) {
//            	e.printStackTrace();
//            }
//    	return listDataMerchantCommission;
//    }
	
	
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String getMerchantListPage() {
		return "merchant-list";
	}

	@RequestMapping(value = "add", method = RequestMethod.GET)
	public String getAddPage(Model model) {

		Merchant merchant = new Merchant();
		List<Country> countryList = null;

		merchant.setId(0);
		merchant.setStatus(Merchant.MERCHANT_STATUS_ACTIVE);

		try {
			countryList = countryService.getAllCountry(0, 0);

		} catch (Exception ex) {
			ex.printStackTrace();
		}

		model.addAttribute("countryList", countryList);
		model.addAttribute("merchant", merchant);
		model.addAttribute("screenMode", "add");
		return "merchant-edit";
	}

	@RequestMapping(value = "edit-{merchantId}", method = RequestMethod.GET)
	public String getEditPage(Model model, @PathVariable int merchantId) {

		try {
			Merchant merchant = merchantService.findById(merchantId);			
			model.addAttribute("countryList", merchant.getCountryCode().getCountryDesc());
			
			//ReceiveSendCountry receiveSendCountry = countryService.findByCountryAndType(merchant.getCountryCode().getId(), ReceiveSendCountry.TYPE_SENDING);
			//CurrencyData[] listCurrencyData = CurrencyData.getArray(currencyService.getCurrencyByCountry(receiveSendCountry.getId()));

			model.addAttribute("currencyList", merchant.getCountryCurrency().getCurrencyCodeId().getCurrencyDesc());
			model.addAttribute("merchant", merchant);
			model.addAttribute("screenMode", "edit");

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return "merchant-edit";
	}

	@RequestMapping(value = "/list.json", method = RequestMethod.POST, headers = Constants.REQUEST_HEADER_ACCEPT_JSON)
	public @ResponseBody
	DataGridData getListData(
			@RequestParam(value = "page", defaultValue = "1") int page,
			@RequestParam(value = "rows", defaultValue = "10") int rows) {
		try {

			logger.debug("/list.json--> {}-{}", page, rows);
			int start = (page - 1) * rows;
			start = start < 0 ? 0 : start;
			ListData[] listData = ListData.getArray(merchantService.getList(
					start, rows));

			DataGridData<ListData> d = new DataGridData<>();
			d.setRows(listData);
			d.setTotal(merchantService.countAll());

			logger.debug("returning start = {}", start);
			return d;

		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
		}

		return null;
	}

	
	@RequestMapping(value = "/merchant-balance-list.json", method = RequestMethod.POST, headers = Constants.REQUEST_HEADER_ACCEPT_JSON)
	public @ResponseBody
	DataGridData getMerchantBalanceData(
			@RequestParam(value = "page", defaultValue = "1") int page,
			@RequestParam(value = "rows", defaultValue = "10") int rows) {
		try {

			logger.debug("/list.json--> {}-{}", page, rows);
			int start = (page - 1) * rows;
			start = start < 0 ? 0 : start;
			MerchantBalanceListData[] listData = MerchantBalanceListData.getArray(merchantService.getMerchantBalance());

			DataGridData<MerchantBalanceListData> d = new DataGridData<>();
			d.setRows(listData);
			d.setTotal(merchantService.countAll());

			logger.debug("returning start = {}", start);
			return d;

		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
		}

		return null;
	}

	@RequestMapping(value = "save", method = RequestMethod.POST)
	public @ResponseBody
	PostResponse saveBoUsers(@ModelAttribute("merchant") Merchant merchant,
			BindingResult result) {

		if (!result.hasErrors()) {
			Date today = new Date();
			try {

				Authentication auth = SecurityContextHolder.getContext()
						.getAuthentication();
				String name = auth.getName();
				BoUsers loggedUser = boUserService.getUserByUserName(name);

				// TODO lastupdateuser and createduser need to save
				
				System.out.println(merchant.getCountryCode().getId());
				System.out.println(merchant.getCountryCurrency().getId());
				
				
				if (merchant.getId() > 0) {
					merchant.setLastUpdateDate(today);
					merchantService.edit(merchant);
				} else {
					merchant.setCreatedDate(today);
					merchant.setLastUpdateDate(today);
					merchantService.add(merchant);
					System.out.println("add default merchant user...");
					merchantService.addDefaultMerchantUser(merchant);
				}
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
				result.addError(new ObjectError("save", e.getMessage()));
			}
		}
		PostResponse response = new PostResponse(result.getAllErrors());
		return response;
	}

	@RequestMapping(value = "/topup-page", method = RequestMethod.GET)
	public String getMerchantTopupPage(Model model) {

		try {
			
			List<Merchant> submerchantList = merchantService
					.getAllActiveMerchant();

			MerchantFilter merchantFilter = new MerchantFilter();
			merchantFilter.setMerchantId(0);
			merchantFilter.setCredit(0);
			model.addAttribute("merchantFilter", merchantFilter);
			model.addAttribute("merchantList", submerchantList);

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return "merchant-topup";
	}

	@RequestMapping(value = "topup", method = RequestMethod.POST)
	public @ResponseBody
	PostResponse topupSubMerchant(
			@ModelAttribute("merchantFilter") MerchantFilter merchantFilter,
			BindingResult result) {

		if (!result.hasErrors()) {

			try {
				Merchant merchant = merchantService.findById(merchantFilter
						.getMerchantId());

				MerchantTransaction merchantTransaction = new MerchantTransaction();
				merchantTransaction.setBalance(merchant.getBalance()
						+ merchantFilter.getCredit());
				merchantTransaction.setCrdr(MerchantTransaction.TRAN_CR);
				merchantTransaction.setDateCreated(new Date());
				merchantTransaction.setDescription(merchantFilter.getNote());
				merchantTransaction.setMerchantId(merchant);
				merchantTransaction
						.setTranType(MerchantTransaction.TRAN_TYPE_TOPUP_CREDIT);
				merchantTransaction.setTrnxAmount(merchantFilter.getCredit());

				merchantTransactionService.topupMerchant(merchant,
						merchantFilter, merchantTransaction);
			} catch (Exception e) {
				e.printStackTrace();
				logger.error(e.getMessage(), e);
				result.addError(new ObjectError("save", e.getMessage()));
			}
		}
		PostResponse response = new PostResponse(result.getAllErrors());
		return response;
	}

	@RequestMapping(value = "commission-add", method = RequestMethod.GET)
	public String getMerchantAddPage(Model model) {

		MerchantCommision comission = new MerchantCommision();
		List<Merchant> merchantList = null;
		List<AmountSlab> amountSlabList = null;

		short id = 0;
		comission.setId(id);

		try {
			merchantList = merchantService.getAllActiveMerchant();
			amountSlabList = amountSlabService.getAllAmountSlab(0, 0);

		} catch (Exception ex) {
			ex.printStackTrace();
		}

		model.addAttribute("merchantList", merchantList);
		model.addAttribute("amountSalbList", amountSlabList);
		model.addAttribute("commission", comission);
		model.addAttribute("screenMode", "add");
		return "merchant-commission-edit";
	}

	@RequestMapping(value = "save-commission", method = RequestMethod.POST)
	public @ResponseBody
	PostResponse saveMerchantCommission(
			@ModelAttribute("commission") MerchantCommision merchantCommision,
			BindingResult result) {

		if (!result.hasErrors()) {
			try {
				Authentication auth = SecurityContextHolder.getContext()
						.getAuthentication();
				String userName = auth.getName();

				BoUsers loggedUser = boUserService.getUserByUserName(userName);

				long lastUpdateUser = loggedUser.getId();
				merchantCommision
						.setStatus(MerchantCommision.MERCHANT_COMMISION_ACTIVE);
				merchantCommision.setLastUpdateUser(lastUpdateUser);
				merchantCommision.setLastUpdateDate(new Date());
				merchantCommissionService
						.addMerchantCommision(merchantCommision);
			} catch (Exception e) {
				logger.info(e.getMessage(), e);
				result.addError(new ObjectError("save", e.getMessage()));
			}
		}
		PostResponse response = new PostResponse(result.getAllErrors());
		return response;
	}

	@RequestMapping(value = "/list-merchant-commission", method = RequestMethod.GET)
	public String getMerchantCommisionListPage() {
		return "merchant-commission-list";
	}

	@RequestMapping(value = "/list-merchant-commision.json", method = RequestMethod.POST, headers = Constants.REQUEST_HEADER_ACCEPT_JSON)
	public @ResponseBody
	DataGridData getMerchantCommisionListData(
			@RequestParam(value = "page", defaultValue = "1") int page,
			@RequestParam(value = "rows", defaultValue = "10") int rows) {
		try {

			logger.debug("/list.json--> {}-{}", page, rows);
			int start = (page - 1) * rows;
			start = start < 0 ? 0 : start;
			ListDataMerchantCommission[] listData = ListDataMerchantCommission
					.getArray(merchantCommissionService
							.getAllMerchantCommision(start, rows));

			DataGridData<ListDataMerchantCommission> d = new DataGridData<>();
			d.setRows(listData);
			d.setTotal(merchantService.countAll());

			logger.debug("returning start = {}", start);
			return d;

		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
		}

		return null;
	}

	@RequestMapping(value = "activate-{merchantIds}", method = RequestMethod.POST)
	public @ResponseBody
	PostResponse postForActivate(@PathVariable String merchantIds) {

		try {
			String[] merchantIdsArr = merchantIds.split(",");
			Integer[] merchantIdIntIds = new Integer[merchantIdsArr.length];

			for (int i = 0; i < merchantIdsArr.length; i++) {
				merchantIdIntIds[i] = Integer.parseInt(merchantIdsArr[i]);
			}
			merchantService.activateMerchants(merchantIdIntIds);
		} catch (Exception e) {
			logger.info("group save Error : " + e.getMessage());
			logger.error(e.getMessage(), e);
		}

		PostResponse response = new PostResponse();

		return response;
	}

	@RequestMapping(value = "suspend-{merchantIds}", method = RequestMethod.POST)
	public @ResponseBody
	PostResponse postForSuspend(@PathVariable String merchantIds) {

		try {
			String[] merchantIdsArr = merchantIds.split(",");
			Integer[] merchantIdIntIds = new Integer[merchantIdsArr.length];

			for (int i = 0; i < merchantIdsArr.length; i++) {
				merchantIdIntIds[i] = Integer.parseInt(merchantIdsArr[i]);
			}
			merchantService.suspendMerchants(merchantIdIntIds);
		} catch (Exception e) {
			logger.info("group save Error : " + e.getMessage());
			logger.error(e.getMessage(), e);
		}

		PostResponse response = new PostResponse();

		return response;
	}

	public static class ListDataMerchantCommission {

		private int id;
		private String amountslab;
		private String merchant;
		private double amount;
		private String status;
		private String dateupdated;

		public static ListDataMerchantCommission[] getArray(
				List<MerchantCommision> dataList) {

			ListDataMerchantCommission[] result = new ListDataMerchantCommission[dataList
					.size()];
			int count = 0;
			for (MerchantCommision merchantCommision : dataList) {
				ListDataMerchantCommission row = new ListDataMerchantCommission();

				row.setId(merchantCommision.getId());
				row.setAmountslab(merchantCommision.getAmountSlab()
						.getLowValue()
						+ "-"
						+ merchantCommision.getAmountSlab().getHighValue());
				row.setMerchant(merchantCommision.getMerchantId()
						.getFirstName()
						+ " "
						+ merchantCommision.getMerchantId().getLastName());
				row.setAmount(merchantCommision.getAmount());
				row.status = (merchantCommision.getStatus() == MerchantCommision.MERCHANT_COMMISION_ACTIVE ? MerchantCommision.MERCHANT_COMMISION_ACTIVE_DESC
						: MerchantCommision.MERCHANT_COMMISION_INACTIVE_DESC);
				row.setDateupdated(merchantCommision.getLastUpdateDate().toString());
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

		public String getAmountslab() {
			return amountslab;
		}

		public void setAmountslab(String amountslab) {
			this.amountslab = amountslab;
		}

		public String getMerchant() {
			return merchant;
		}

		public void setMerchant(String merchant) {
			this.merchant = merchant;
		}

		public double getAmount() {
			return amount;
		}

		public void setAmount(double amount) {
			this.amount = amount;
		}

		public String getStatus() {
			return status;
		}

		public void setStatus(String status) {
			this.status = status;
		}

		public String getDateupdated() {
			return dateupdated;
		}

		public void setDateupdated(String dateupdated) {
			this.dateupdated = dateupdated;
		}
	}

	@RequestMapping(value = "/balance", method = RequestMethod.GET)
	public String getMerchantBalancePage() {
		return "merchant-balance";
	}

	static class ListCurrencyData {
		private String currencycode;

		public String getCurrencycode() {
			return currencycode;
		}

		public void setCurrencycode(String currencycode) {
			this.currencycode = currencycode;
		}
		
	}
	
	static class MerchantBalanceListData {
		private String firstName;
		private String lastName;
		private String telephone;
		private String merchantbalance;
		private String submerchantbalance;
		private String totalbalance;
		
		public String getFirstName() {
			return firstName;
		}
		public void setFirstName(String firstName) {
			this.firstName = firstName;
		}
		public String getLastName() {
			return lastName;
		}
		public void setLastName(String lastName) {
			this.lastName = lastName;
		}
		public String getTelephone() {
			return telephone;
		}
		public void setTelephone(String telephone) {
			this.telephone = telephone;
		}
		public String getMerchantbalance() {
			return merchantbalance;
		}
		public void setMerchantbalance(String merchantbalance) {
			this.merchantbalance = merchantbalance;
		}
		public String getSubmerchantbalance() {
			return submerchantbalance;
		}
		public void setSubmerchantbalance(String submerchantbalance) {
			this.submerchantbalance = submerchantbalance;
		}
		public String getTotalbalance() {
			return totalbalance;
		}
		public void setTotalbalance(String totalbalance) {
			this.totalbalance = totalbalance;
		}
		
		
		
		public static MerchantBalanceListData[] getArray(List<Object[]> dataList) {

			MerchantBalanceListData[] result = new MerchantBalanceListData[dataList.size()];
			int count = 0;
			double submerchantbalance = 0;
			for (Object[] object : dataList) {
				submerchantbalance = 0;
				MerchantBalanceListData row = new MerchantBalanceListData();

				row.firstName = object[0].toString();
				row.lastName = object[1].toString();
				row.telephone = object[2].toString();
				row.merchantbalance = object[3].toString() + " " + object[5].toString();
				
				if(object[4] != null)
					submerchantbalance = Double.parseDouble(object[4].toString());
					
				row.submerchantbalance = submerchantbalance + " " + object[5].toString();
				row.totalbalance =  Double.parseDouble(object[3].toString()) + submerchantbalance
						+ " " + object[5].toString();
				result[count++] = row;
			}

			return result;
		}
		
		
	}
	
	
	static class ListData {

		private int id;
		private String telephone;
		private String email;
		private String lastName;
		private String firstName;
		private String fullname;
		private String addressLine1;
		private String addressLine2;
		private String addressLine3;
		private Short status;
		private Date createdDate;
		private String statusdesc;
		private String balance;

		public static ListData[] getArray(List<Object> dataList) {

			ListData[] result = new ListData[dataList.size()];
			int count = 0;
			Merchant merchant;
			for (Object object : dataList) {
				merchant = (Merchant) object;
				ListData row = new ListData();

				row.setId(merchant.getId());
				row.telephone = merchant.getTelephone();
				row.email = merchant.getEmail();

				row.lastName = merchant.getLastName();
				row.firstName = merchant.getFirstName();
				row.fullname = merchant.getFirstName() + " "
						+ merchant.getLastName();
				row.addressLine1 = merchant.getAddressLine1();
				row.addressLine2 = merchant.getAddressLine2();
				row.addressLine3 = merchant.getAddressLine3();
				row.status = merchant.getStatus();
				row.createdDate = merchant.getCreatedDate();
				row.setStatusdesc((merchant.getStatus() == Merchant.MERCHANT_STATUS_ACTIVE ? Merchant.MERCHANT_STATUS_ACTIVE_DESC
						: Merchant.MERCHANT_STATUS_SUSPEND_DESC));
				row.balance = merchant.getBalance()
						+ " "
						+ merchant.getCountryCurrency().getCurrencyCodeId()
								.getCurrencyCode();
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

		public String getTelephone() {
			return telephone;
		}

		public void setTelephone(String telephone) {
			this.telephone = telephone;
		}

		public String getEmail() {
			return email;
		}

		public void setEmail(String email) {
			this.email = email;
		}

		public String getLastName() {
			return lastName;
		}

		public void setLastName(String lastName) {
			this.lastName = lastName;
		}

		public String getFirstName() {
			return firstName;
		}

		public void setFirstName(String firstName) {
			this.firstName = firstName;
		}

		public String getAddressLine1() {
			return addressLine1;
		}

		public void setAddressLine1(String addressLine1) {
			this.addressLine1 = addressLine1;
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

		public Short getStatus() {
			return status;
		}

		public void setStatus(Short status) {
			this.status = status;
		}

		public Date getCreatedDate() {
			return createdDate;
		}

		public void setCreatedDate(Date createdDate) {
			this.createdDate = createdDate;
		}

		public String getStatusdesc() {
			return statusdesc;
		}

		public void setStatusdesc(String statusdesc) {
			this.statusdesc = statusdesc;
		}

		public String getBalance() {
			return balance;
		}

		public void setBalance(String balance) {
			this.balance = balance;
		}

		public String getFullname() {
			return fullname;
		}

		public void setFullname(String fullname) {
			this.fullname = fullname;
		}
	}

}
