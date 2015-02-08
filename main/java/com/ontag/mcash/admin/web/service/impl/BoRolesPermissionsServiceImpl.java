package com.ontag.mcash.admin.web.service.impl;

import java.util.List;

import javax.annotation.PostConstruct;

import org.hibernate.annotations.Parent;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.ontag.mcash.admin.web.exception.ServiceException;
import com.ontag.mcash.admin.web.service.AppPermissionsService;
import com.ontag.mcash.admin.web.service.BoRolesPermissionsService;
import com.ontag.mcash.admin.web.util.Constants;
import com.ontag.mcash.dal.dao.BoRolesPermissionsDao;
import com.ontag.mcash.dal.domain.AppPermissions;
import com.ontag.mcash.dal.domain.BoRoles;
import com.ontag.mcash.dal.domain.BoRolesPermission;
import com.ontag.mcash.dal.domain.BoUserRole;
import com.ontag.mcash.dal.exception.DataAccessException;

@Service
@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
public class BoRolesPermissionsServiceImpl extends
		GenericServiceImpl<BoRolesPermission> implements
		BoRolesPermissionsService {

	protected static org.slf4j.Logger logger = LoggerFactory
			.getLogger(BoRolesPermissionsServiceImpl.class);

	@Autowired
	private BoRolesPermissionsDao boRolesPermissionsDao;

	@Autowired
	private AppPermissionsService appPermissionsService;

	@PostConstruct
	void init() {
		super.init(BoRolesPermission.class, boRolesPermissionsDao);
	}

	@Override
	@Transactional(readOnly = false)
	public void editBoRolesPermissions(BoRoles boRole, String permissionIds)
			throws ServiceException {
		try {
			String[] permissionIdsArr = permissionIds.split(",");
			System.out.println("count ids ################## "
					+ permissionIdsArr.length);

			boRolesPermissionsDao.deleteBoRolesPermission(boRole);
			
			for (int i = 0; i < permissionIdsArr.length; i++) {
				System.out.println(permissionIdsArr[i]);

				BoRolesPermission boRolePermission = new BoRolesPermission();
				
				AppPermissions permissions = appPermissionsService
						.findById(Long.parseLong(permissionIdsArr[i]));
				
				if(permissions.getParentId()!=null){
					AppPermissions parentPermission = appPermissionsService.findById(permissions.getParentId().getId());
					
					BoRolesPermission parentCheck = boRolesPermissionsDao.findParentByRole(boRole,parentPermission);
					System.out.println(parentCheck);
					if (parentCheck == null) {
						BoRolesPermission boRolePermissionParent = new BoRolesPermission();
						boRolePermissionParent.setBoRoleId(boRole);
						boRolePermissionParent.setPermissionId(parentPermission);
						boRolesPermissionsDao.add(boRolePermissionParent);					
					}
					
				}
				
				System.out.println("Permission ################### : "
						+ permissions.getDisplayName());

				boRolePermission.setBoRoleId(boRole);
				boRolePermission.setPermissionId(permissions);

				boRolesPermissionsDao.add(boRolePermission);
			}

		} catch (DataAccessException dae) {
			logger.info("Service EditBoUserRoles ################## 3 : " + dae);
			throw translateException(dae);
		} catch (Exception e) {
			logger.info("Service EditBoUserRoles ################## 4 : " + e);
			throw new ServiceException(ServiceException.PROCESSING_FAILED,
					e.getMessage(), e);
		}
	}

	@Override
	public List<BoRolesPermission> getRolesPermissionsByRoleId(int boRoleId)
			throws ServiceException {
		try {
			logger.debug("BoRolesPermissionsServiceImpl getRolesPermissionsByRoleId #############");
			return boRolesPermissionsDao.getBoRolesPermissionsByRoleId(boRoleId);
		} catch (DataAccessException dae) {
			throw translateException(dae);
		} catch (Exception e) {
			throw new ServiceException(ServiceException.PROCESSING_FAILED,
					e.getMessage(), e);
		}
	}

	@Override
	public List<Object[]> getPermissionsByRoles(Short[] ids)
			throws ServiceException {
		try {
			logger.debug("BoRolesPermissionsServiceImpl getPermissionsByRoles #############");
			return boRolesPermissionsDao.getPermissionsByRoles(ids);
		} catch (DataAccessException dae) {
			throw translateException(dae);
		} catch (Exception e) {
			throw new ServiceException(ServiceException.PROCESSING_FAILED,
					e.getMessage(), e);
		}
	}

}
