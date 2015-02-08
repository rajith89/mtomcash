package com.ontag.mcash.admin.web.service;

import java.util.List;

import com.ontag.mcash.admin.web.exception.ServiceException;
import com.ontag.mcash.dal.domain.BoUsers;
import com.ontag.mcash.dal.domain.MerchantUsers;


public interface BoUserService extends GenericService<BoUsers>{
	
	public List<BoUsers> getAllBoUsers(int offset, int size)throws ServiceException;	
	public BoUsers findById(int id)throws ServiceException;	
	public BoUsers getUserByUserName(String userName)throws ServiceException;
	public void changePassword(BoUsers boUsers) throws ServiceException;
	public boolean validatePasswordPin(String username, String passwordPin) throws ServiceException;
	public void resetPassword(BoUsers boUsers) throws ServiceException;
	public void activateUsers(Integer[] userIds)throws ServiceException;
	public void deactivateUsers(Integer[] userIds)throws ServiceException;
}

