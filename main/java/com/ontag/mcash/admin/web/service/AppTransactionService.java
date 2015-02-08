package com.ontag.mcash.admin.web.service;

import java.util.Date;
import java.util.List;

import com.ontag.mcash.admin.web.exception.ServiceException;
import com.ontag.mcash.dal.domain.AppTransaction;
import com.ontag.mcash.dal.domain.Merchant;
import com.ontag.mcash.dal.domain.ReceivingMethods;
import com.ontag.mcash.dal.exception.DataAccessException;

public interface AppTransactionService extends GenericService<AppTransaction> {

	public List<AppTransaction> findByPin(String transactionPin)
			throws ServiceException;

	public List<AppTransaction> findByDate(Date from, Date to)
			throws ServiceException;

	public List<AppTransaction> findByMerchant(Merchant merchantId)
			throws ServiceException;

	public List<AppTransaction> findByMerchantAndDate(Merchant merchant,
			Date from, Date to) throws ServiceException;

	public List<Object[]> getMerchantCommissionSummary(Date from, Date to)
			throws ServiceException;

	public List<Object[]> getIncomeSummaryByDate(Date from, Date to)
			throws ServiceException;

	public List<Object[]> getIncomeSummaryByTransactionType(
			ReceivingMethods receivingMethod) throws ServiceException;

	public List<Object[]> getIncomeSummaryByDateAndTransactionType(Date from,
			Date to, ReceivingMethods receivingMethods) throws ServiceException;

	public List<Object[]> getProfitByDate(Date from, Date to)
			throws ServiceException;

	public List<Object[]> getProfitByTransactionType(
			ReceivingMethods receivingMethod) throws ServiceException;

	public List<Object[]> getProfitByDateAndTransactionType(Date from, Date to,
			ReceivingMethods receivingMethods) throws ServiceException;

	public AppTransaction findById(long transactionId) throws ServiceException;

	public List<AppTransaction> findFailTransactionByMerchant(Merchant merchant)
			throws ServiceException;

	public List<AppTransaction> findFailTransactionByMerchantAndDate(
			Merchant merchant, Date from, Date to) throws ServiceException;

	public List<AppTransaction> findFailTransactionByDate(Date from, Date to)
			throws ServiceException;

	public List<AppTransaction> getMerchantCommissionsByDate(Date from, Date to)
			throws ServiceException;
}
