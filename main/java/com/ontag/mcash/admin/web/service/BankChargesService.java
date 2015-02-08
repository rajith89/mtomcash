package com.ontag.mcash.admin.web.service;

import java.util.List;

import com.ontag.mcash.admin.web.exception.ServiceException;
import com.ontag.mcash.dal.domain.AmountSlab;
import com.ontag.mcash.dal.domain.BankCharges;
import com.ontag.mcash.dal.domain.CurrencyCode;
import com.ontag.mcash.dal.domain.ReceivingMethods;

public interface BankChargesService extends GenericService<BankCharges> {

	public List<BankCharges> getAllBankCharges(int offset, int size)
			throws ServiceException;

	public BankCharges findById(int id) throws ServiceException;

	public void setInactive(ReceivingMethods receivingMethodId,
			AmountSlab amountSlabId, CurrencyCode currencyCode) throws ServiceException;
	
	public BankCharges findByBankCurrencyReceivingMethodAmountSlab(int currency, int receivingmethod, int amountslab) throws ServiceException;
}
