package com.ontag.mcash.admin.web.service;

import java.util.List;

import com.ontag.mcash.admin.web.exception.ServiceException;
import com.ontag.mcash.dal.domain.Merchant;


public interface MerchantService extends GenericService<Merchant>{
	
	public List<Merchant> getAllMerchant(int offset, int size)throws ServiceException;	
	public Merchant findById(int id)throws ServiceException;
	public List<Merchant> getAllActiveMerchant()throws ServiceException;
	
	public void activateMerchants(Integer[] ids)throws ServiceException;
	public void suspendMerchants(Integer[] ids)throws ServiceException;
	public void addDefaultMerchantUser(Merchant merchant)throws ServiceException;
	public List<Object[]> getMerchantBalance()throws ServiceException;
}

