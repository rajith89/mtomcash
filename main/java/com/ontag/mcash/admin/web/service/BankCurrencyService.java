package com.ontag.mcash.admin.web.service;

import java.util.List;

import com.ontag.mcash.admin.web.exception.ServiceException;
import com.ontag.mcash.dal.domain.BankCurrency;

public interface BankCurrencyService extends GenericService<BankCurrency>{

	List<BankCurrency> getBankCurrencyByBankId(int bankId)throws ServiceException;

	BankCurrency findById(Short id)throws ServiceException;

}
