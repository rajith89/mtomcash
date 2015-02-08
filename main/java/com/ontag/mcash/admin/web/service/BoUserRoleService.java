package com.ontag.mcash.admin.web.service;

import java.util.List;

import com.ontag.mcash.dal.domain.BoUserRole;
import com.ontag.mcash.dal.domain.BoUsers;
import com.ontag.mcash.admin.web.exception.ServiceException;

public interface BoUserRoleService extends GenericService<BoUserRole>{
	
	public List<BoUserRole> getAllBoUserRoles(int offset,
			int size) throws ServiceException;

	public BoUserRole findById(int id) throws ServiceException;

	public void addBoUserRole(BoUsers boUser,
			String roleIds, String roleNames) throws ServiceException;

	public List<BoUserRole> getBoUserRolesById(int boUsersId)
			throws ServiceException;

	public void editBoUserRoles(BoUsers boUser,
			String roleIds, String roleNames) throws ServiceException;
}
