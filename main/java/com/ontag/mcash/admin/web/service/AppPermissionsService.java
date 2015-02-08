package com.ontag.mcash.admin.web.service;

import java.util.List;

import com.ontag.mcash.admin.web.exception.ServiceException;
import com.ontag.mcash.dal.domain.AppPermissions;
import com.ontag.mcash.dal.domain.BoRoles;

public interface AppPermissionsService extends GenericService<AppPermissions>{
	
	public List<AppPermissions> getAdminPermissionsList(int offset, int size) throws ServiceException;
	public List<AppPermissions> getMerchantPermissionsList(int offset, int size) throws ServiceException;
	public List<AppPermissions> getSubMerchantPermissionsList(int offset, int size) throws ServiceException;
	public AppPermissions findById(long id)throws ServiceException;
	public List<AppPermissions> getMerchantFullPermissionsList(int offset, int size)throws ServiceException;	
}
