package com.ontag.mcash.admin.web.service;

import java.util.List;

import com.ontag.mcash.admin.web.exception.ServiceException;
import com.ontag.mcash.dal.domain.MerchantCommision;



public interface MerchantCommissionService extends GenericService<MerchantCommision>{
	
	public List<MerchantCommision> getAllMerchantCommision(int offset, int size)throws ServiceException;	
	public MerchantCommision findById(int id)throws ServiceException;	
	public void addMerchantCommision(MerchantCommision merchantCommision)throws ServiceException;
	public MerchantCommision findByMerchantAndAmountSlab(int merchantId, int amountSlab)throws ServiceException;
}

