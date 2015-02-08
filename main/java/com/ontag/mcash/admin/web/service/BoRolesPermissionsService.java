package com.ontag.mcash.admin.web.service;

import java.util.List;

import com.ontag.mcash.admin.web.exception.ServiceException;
import com.ontag.mcash.dal.domain.AppPermissions;
import com.ontag.mcash.dal.domain.BoRoles;
import com.ontag.mcash.dal.domain.BoRolesPermission;

public interface BoRolesPermissionsService extends
		GenericService<BoRolesPermission> {

	void editBoRolesPermissions(BoRoles boRole, String permissionIds)
			throws ServiceException;

	List<BoRolesPermission> getRolesPermissionsByRoleId(int boRoleId)
			throws ServiceException;

	List<Object[]> getPermissionsByRoles(Short[] ids)
			throws ServiceException;
}
