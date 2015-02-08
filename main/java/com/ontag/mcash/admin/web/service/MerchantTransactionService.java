package com.ontag.mcash.admin.web.service;

import java.util.Date;
import java.util.List;

import com.ontag.mcash.admin.web.controller.json.MerchantFilter;
import com.ontag.mcash.admin.web.exception.ServiceException;
import com.ontag.mcash.dal.domain.MerchantTransaction;
import com.ontag.mcash.dal.domain.Merchant;

public interface MerchantTransactionService extends
		GenericService<MerchantTransaction> {
	
	public void topupMerchant(Merchant merchant, MerchantFilter merchantFilter,
			MerchantTransaction merchantTransaction) throws ServiceException;

	public List<MerchantTransaction> getAllMerchantTransactionByCrDr(String crdr)
			throws ServiceException;

	public List<MerchantTransaction> getMerchantTransactionByDateAndMerchant(
			Date from, Date to, Merchant merchant) throws ServiceException;

	public List<MerchantTransaction> getMerchantTransactionByMerchant(
			Merchant merchant) throws ServiceException;

	public List<Object[]> getMerchantTopupSummery(Date from, Date to)
			throws ServiceException;
	
}
