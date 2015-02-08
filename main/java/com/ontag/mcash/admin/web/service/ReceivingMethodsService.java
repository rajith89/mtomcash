package com.ontag.mcash.admin.web.service;

import java.util.List;

import com.ontag.mcash.admin.web.exception.ServiceException;
import com.ontag.mcash.dal.domain.ReceivingMethods;
import com.ontag.mcash.dal.exception.DataAccessException;

public interface ReceivingMethodsService extends GenericService<ReceivingMethods>{
	public List<ReceivingMethods> getAllReceivingMethods(int offset, int size) throws ServiceException;
	public ReceivingMethods findById(short id) throws ServiceException;
	public List<ReceivingMethods> getReceivingMethodsByBankId(int bankId)throws ServiceException;
	public List<ReceivingMethods> getAllActiveRecievingMethods() throws ServiceException;
}
