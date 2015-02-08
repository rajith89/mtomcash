package com.ontag.mcash.admin.web.service;

import java.util.List;

import com.ontag.mcash.admin.web.exception.ServiceException;
import com.ontag.mcash.dal.domain.CountryCurrency;
import com.ontag.mcash.dal.domain.CurrencyCode;


public interface CurrencyService extends GenericService<CurrencyCode>{
	
	public List<CurrencyCode> getAllCurrencyCode(int offset, int size)throws ServiceException;	
	public CurrencyCode findById(int id)throws ServiceException;
	public List<CountryCurrency> getCurrencyByCountry(int countryId)throws ServiceException;
}

