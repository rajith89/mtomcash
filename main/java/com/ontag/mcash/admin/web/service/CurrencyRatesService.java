package com.ontag.mcash.admin.web.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.ontag.mcash.admin.web.exception.ServiceException;
import com.ontag.mcash.dal.domain.CurrencyRates;

public interface CurrencyRatesService extends GenericService<CurrencyRates>{
	
	public List<CurrencyRates> getAllCurrencyRates(int offset, int size)throws ServiceException;	
	public CurrencyRates findById(int id)throws ServiceException;
	public void addCurrencyRatesFromCSV(Short bankId, MultipartFile file)throws ServiceException;
	public List<CurrencyRates> getActiveCurrencyRates()throws ServiceException;	
	
}
