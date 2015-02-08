package com.ontag.mcash.admin.web.service;

import com.ontag.mcash.admin.web.exception.ServiceException;
import com.ontag.mcash.dal.domain.SystemStatus;


public interface AdministrationService extends GenericService<SystemStatus>{
	
	public SystemStatus getSystemStatus()throws ServiceException;	
}

