package com.ontag.mcash.admin.web.service;

import java.util.List;

import com.ontag.mcash.admin.web.exception.ServiceException;
import com.ontag.mcash.dal.domain.CorporateBank;


public interface CorporateBankService extends GenericService<CorporateBank>{
	
	public List<CorporateBank> getAllCorporateBank(int offset, int size)throws ServiceException;	
	public CorporateBank findById(int id)throws ServiceException;
	public void activateBanks(Short[] ids) throws ServiceException;
	public void deactivateBanks(Short[] ids) throws ServiceException;
}

