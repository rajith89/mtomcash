package com.ontag.mcash.admin.web.service;

import java.util.List;

import com.ontag.mcash.admin.web.exception.ServiceException;
import com.ontag.mcash.dal.domain.Country;
import com.ontag.mcash.dal.domain.ReceiveSendCountry;
import com.ontag.mcash.dal.exception.DataAccessException;


public interface CountryService extends GenericService<ReceiveSendCountry>{
	
	public List<Country> getAllCountry(int offset, int size)throws ServiceException;	
	public Country findById(int id)throws ServiceException;	
	public List<ReceiveSendCountry> getAllReceiveSendCountry(int offset, int size)throws ServiceException;	
	public ReceiveSendCountry findByCountryAndType(int countryId, int type) throws ServiceException;
	public void activateSendReceiveCountries(Integer[] ids) throws ServiceException;
	public void inactivateSendReceiveCountries(Integer[] ids) throws ServiceException;
	public List<ReceiveSendCountry> getAllCountryByTypeAndByStatus(short type, int status) throws ServiceException;
}

