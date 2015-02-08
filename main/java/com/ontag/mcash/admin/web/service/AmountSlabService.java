package com.ontag.mcash.admin.web.service;

import java.util.List;

import com.ontag.mcash.admin.web.exception.ServiceException;
import com.ontag.mcash.dal.domain.AmountSlab;


public interface AmountSlabService extends GenericService<AmountSlab>{
	
	public List<AmountSlab> getAllAmountSlab(int offset, int size)throws ServiceException;	
	public AmountSlab findById(int id)throws ServiceException;	
}

