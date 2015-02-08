package com.ontag.mcash.admin.web.service;

import java.util.List;

import com.ontag.mcash.admin.web.exception.ServiceException;
import com.ontag.mcash.dal.domain.BoRoles;

public interface BoRolesService extends GenericService<BoRoles>{
	
	public List<BoRoles> getAllBoRoles(int offset, int size)throws ServiceException;	
	public BoRoles findById(int id)throws ServiceException;	
}
