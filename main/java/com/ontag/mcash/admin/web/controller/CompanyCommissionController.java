package com.ontag.mcash.admin.web.controller;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.print.attribute.standard.Compression;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ontag.mcash.admin.web.controller.BankChargesController.ListData;
import com.ontag.mcash.admin.web.controller.BankChargesController.ListData_receivingMethods;
import com.ontag.mcash.admin.web.controller.json.DataGridData;
import com.ontag.mcash.admin.web.controller.json.PostResponse;
import com.ontag.mcash.admin.web.service.AmountSlabService;
import com.ontag.mcash.admin.web.service.BoUserService;
import com.ontag.mcash.admin.web.service.CompanyCommissionService;
import com.ontag.mcash.admin.web.service.MerchantService;
import com.ontag.mcash.admin.web.util.Constants;
import com.ontag.mcash.dal.domain.AmountSlab;
import com.ontag.mcash.dal.domain.BankCharges;
import com.ontag.mcash.dal.domain.BoUsers;
import com.ontag.mcash.dal.domain.CompanyCommision;
import com.ontag.mcash.dal.domain.CurrencyCode;
import com.ontag.mcash.dal.domain.Merchant;
import com.ontag.mcash.dal.domain.ReceivingMethods;

@Controller
@RequestMapping("/companycommission")
public class CompanyCommissionController {

	protected static org.slf4j.Logger logger = LoggerFactory
			.getLogger(CompanyCommissionController.class);

	@Autowired
	private MerchantService merchantService;

	@Autowired
	AmountSlabService amountSlabService;

	@Autowired
	BoUserService boUserService;

	@Autowired
	CompanyCommissionService companyCommissionService;

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String getBankChargesListPage() {
		logger.info("company-commission-list");
		return "company-commission-list";
	}

	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String getMerchantTopupReportPage(Model model) {
		logger.info("show company-commission-edit");
		CompanyCommision companyCommision = new CompanyCommision();

		List<Merchant> merchantList = null;
		List<AmountSlab> amountSlabList = null;

		try {
			merchantList = merchantService.getAllActiveMerchant();
			amountSlabList = amountSlabService.getAllAmountSlab(0, 0);
		} catch (Exception e) {
			e.printStackTrace();
		}

		model.addAttribute("amountSalbList", amountSlabList);
		model.addAttribute("merchantList", merchantList);
		model.addAttribute("companyCommision", companyCommision);
		model.addAttribute("screenMode", "add");

		return "company-commission-edit";
	}

	@RequestMapping(value = "/list.json", method = RequestMethod.POST, headers = Constants.REQUEST_HEADER_ACCEPT_JSON)
	public @ResponseBody
	DataGridData getListData(
			@RequestParam(value = "page", defaultValue = "1") int page,
			@RequestParam(value = "rows", defaultValue = "10") int rows) {
		try {

			int start = (page - 1) * rows;
			start = start < 0 ? 0 : start;
			ListData[] listData = ListData.getArray(companyCommissionService
					.getList(start, rows));

			DataGridData<ListData> d = new DataGridData<>();
			d.setRows(listData);
			d.setTotal(companyCommissionService.countAll());

			logger.debug("returning start = {}", start);
			return d;

		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
		}

		return null;
	}

	@RequestMapping(value = "save", method = RequestMethod.POST)
	public @ResponseBody
	PostResponse saveCompanyCommission(
			@ModelAttribute("companyCommision") CompanyCommision companyCommision,
			BindingResult result) {
		logger.info("saveCompanyCommission ################## 1");
		if (!result.hasErrors()) {
			logger.info("saveCompanyCommission ################## 2");
			try {
				logger.info("saveCompanyCommission ################## 3");

				System.out.println("Amount : " + companyCommision.getAmount());
				System.out.println("Slab id : "
						+ companyCommision.getAmountSlabId().getId());
				System.out.println("Merchant : "
						+ companyCommision.getMerchantId().getId());

				System.out.println("Currency : "
						+ merchantService
								.findById(
										companyCommision.getMerchantId()
												.getId()).getCountryCurrency()
								.getCurrencyCodeId().getCurrencyCode());

				Authentication auth = SecurityContextHolder.getContext()
						.getAuthentication();
				String userName = auth.getName();

				BoUsers loggedUser = boUserService.getUserByUserName(userName);
				companyCommision.setCurrencyCodeId(merchantService
						.findById(companyCommision.getMerchantId().getId())
						.getCountryCurrency().getCurrencyCodeId());
				companyCommision.setLastUpdateUser(loggedUser.getId());
				Date currentdate = new Date();
				companyCommision.setLastUpdateDate(currentdate);

				companyCommision.setStatus(CompanyCommision.STATUS_ACTIVE);

				companyCommissionService.setInactive(
						companyCommision.getMerchantId(),
						companyCommision.getAmountSlabId());
				companyCommissionService.add(companyCommision);

			} catch (Exception e) {
				logger.error(e.getMessage(), e);
				result.addError(new ObjectError("save", e.getMessage()));
			}
		}

		PostResponse response = new PostResponse(result.getAllErrors());
		return response;
	}

	static class ListData {

		private Long id;
		private String status;
		private String currencyCodeId;
		private String amountSlabId;
		private String amount;
		private String merchantId;
		private String dateupdated;

		public Long getId() {
			return id;
		}

		public void setId(Long id) {
			this.id = id;
		}

		public String getStatus() {
			return status;
		}

		public void setStatus(String status) {
			this.status = status;
		}

		public String getCurrencyCodeId() {
			return currencyCodeId;
		}

		public void setCurrencyCodeId(String currencyCodeId) {
			this.currencyCodeId = currencyCodeId;
		}

		public String getAmountSlabId() {
			return amountSlabId;
		}

		public void setAmountSlabId(String amountSlabId) {
			this.amountSlabId = amountSlabId;
		}

		public String getAmount() {
			return amount;
		}

		public void setAmount(String amount) {
			this.amount = amount;
		}

		public String getMerchantId() {
			return merchantId;
		}

		public void setMerchantId(String merchantId) {
			this.merchantId = merchantId;
		}

		public static ListData[] getArray(List<Object> dataList) {

			ListData[] result = new ListData[dataList.size()];
			int count = 0;
			CompanyCommision companyCommision;
			for (Object object : dataList) {
				companyCommision = (CompanyCommision) object;
				ListData row = new ListData();

				row.id = companyCommision.getId();
				row.amount = String.valueOf(companyCommision.getAmount())
						+ " "
						+ companyCommision.getCurrencyCodeId()
								.getCurrencyCode();
				row.status = companyCommision.getStatus() == CompanyCommision.STATUS_ACTIVE ? CompanyCommision.STATUS_ACTIVE_DESC
						: CompanyCommision.STATUS_INACTIVE_DESC;
				row.merchantId = companyCommision.getMerchantId()
						.getFirstName()
						+ " "
						+ companyCommision.getMerchantId().getLastName();
				row.amountSlabId = companyCommision.getAmountSlabId().getLowValue() + " - " + companyCommision.getAmountSlabId().getHighValue();
				row.dateupdated = companyCommision.getLastUpdateDate().toString();

				result[count++] = row;
			}
			return result;
		}

		public String getDateupdated() {
			return dateupdated;
		}

		public void setDateupdated(String dateupdated) {
			this.dateupdated = dateupdated;
		}
	}
}
