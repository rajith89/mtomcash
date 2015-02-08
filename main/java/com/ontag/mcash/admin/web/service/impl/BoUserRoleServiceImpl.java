package com.ontag.mcash.admin.web.service.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.ontag.mcash.admin.web.exception.ServiceException;
import com.ontag.mcash.admin.web.service.BoUserRoleService;
import com.ontag.mcash.admin.web.service.BoUserService;
import com.ontag.mcash.dal.dao.BoUserRoleDao;
import com.ontag.mcash.dal.domain.BoRoles;
import com.ontag.mcash.dal.domain.BoUserRole;
import com.ontag.mcash.dal.domain.BoUsers;
import com.ontag.mcash.dal.exception.DataAccessException;
import com.ontag.mcash.admin.web.util.Constants;

@Service
@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
public class BoUserRoleServiceImpl extends GenericServiceImpl<BoUserRole>
		implements BoUserRoleService {

	protected static org.slf4j.Logger logger = LoggerFactory
			.getLogger(BoUserRoleServiceImpl.class);

	@Autowired
	private BoUserRoleDao boUserRoleDao;

	@Autowired
	private BoUserService boUserService;

	@PostConstruct
	void init() {
		super.init(BoUserRole.class, boUserRoleDao);
	}

	@Override
	public List<BoUserRole> getAllBoUserRoles(int offset, int size)
			throws ServiceException {
		try {
			logger.debug("BoUserRoleServiceImpl getAllBoUserRoles #############");
			return boUserRoleDao.getAllBouserRoles(offset, size);
		} catch (DataAccessException dae) {
			throw translateException(dae);
		} catch (Exception e) {
			throw new ServiceException(ServiceException.PROCESSING_FAILED,
					e.getMessage(), e);
		}
	}

	@Override
	public BoUserRole findById(int id) throws ServiceException {
		try {
			logger.debug("BoUserRoleServiceImpl findById #############");
			return boUserRoleDao.findById(id);
		} catch (DataAccessException dae) {
			throw translateException(dae);
		} catch (Exception e) {
			throw new ServiceException(ServiceException.PROCESSING_FAILED,
					e.getMessage(), e);
		}
	}

	@Override
	@Transactional(readOnly = false)
	public void addBoUserRole(BoUsers boUser, String roleIds, String roleNames)
			throws ServiceException {
		try {
			logger.info("Service AddBoUserRoles ################## 1");

			BoUsers usernameCheck = boUserService.getUserByUserName(boUser
					.getUserName());
			if (usernameCheck != null) {
				throw new Exception(Constants.USER_ALREADY_EXIST);
			}

			Authentication auth = SecurityContextHolder.getContext()
					.getAuthentication();
			
			String userName = auth.getName();
			boUser.setCreatedUser(boUserService.getUserByUserName(userName)
					.getId());

			Md5PasswordEncoder encoder = new Md5PasswordEncoder();
			boUser.setPasswordEnc(encoder.encodePassword(
					boUser.getPasswordEnc(), null));
			
			boUser.setPasswordPinEnc(encoder.encodePassword(
					boUser.getPasswordPinEnc(), null));

			Date currentdate = new Date();
			boUser.setCreatedDate(currentdate);
			boUser.setLastUpdateDate(currentdate);

			boUser.setStatus(BoUsers.BOUSER_STATUS_ACTIVE);

			String[] roleIdsArr = roleIds.split(",");
			System.out.println("count ids ################## "
					+ roleIdsArr.length);

			String[] roleNamesArr = roleNames.split(",");
			System.out.println("count names ################## "
					+ roleNamesArr.length);

			for (int i = 0; i < roleIdsArr.length; i++) {
				System.out.println(roleIdsArr[i]);
				System.out.println(roleNamesArr[i]);

				BoUserRole boUserRole = new BoUserRole();
				BoRoles boRoles = new BoRoles();

				boRoles.setId(Short.parseShort(roleIdsArr[i]));
				boRoles.setName(roleNamesArr[i]);

				boUserRole.setBoRoleId(boRoles);
				boUserRole.setBoUserId(boUser);

				boUserRoleDao.addBoUserRole(boUserRole);
			}

			logger.info("Service AddBoUserRoles ################## 2");
		} catch (DataAccessException dae) {
			logger.info("Service AddBoUserRoles ################## 3 : " + dae);
			throw translateException(dae);
		} catch (Exception e) {
			logger.info("Service AddBoUserRoles ################## 4 : " + e);
			throw new ServiceException(ServiceException.PROCESSING_FAILED,
					e.getMessage(), e);
		}
	}

	@Override
	public List<BoUserRole> getBoUserRolesById(int boUsersId)
			throws ServiceException {
		try {
			logger.debug("BoUserRoleServiceImpl getBoUserRolesById #############");
			return boUserRoleDao.getBoUserRolesById(boUsersId);
		} catch (DataAccessException dae) {
			throw translateException(dae);
		} catch (Exception e) {
			throw new ServiceException(ServiceException.PROCESSING_FAILED,
					e.getMessage(), e);
		}
	}

	@Override
	@Transactional(readOnly = false)
	public void editBoUserRoles(BoUsers boUser, String roleIds, String roleNames)
			throws ServiceException {
		try {

			Authentication auth = SecurityContextHolder.getContext()
					.getAuthentication();
			String userName = auth.getName();
			boUser.setLastUpdateUser(boUserService.getUserByUserName(userName)
					.getId());

			Date currentdate = new Date();
			boUser.setLastUpdateDate(currentdate);

			String[] roleIdsArr = roleIds.split(",");
			System.out.println("count ids ################## "
					+ roleIdsArr.length);

			String[] roleNamesArr = roleNames.split(",");
			System.out.println("count names ################## "
					+ roleNamesArr.length);

			boUserRoleDao.deleteboUserRole(boUser);

			for (int i = 0; i < roleIdsArr.length; i++) {
				System.out.println(roleIdsArr[i]);
				System.out.println(roleNamesArr[i]);

				BoUserRole boUserRole = new BoUserRole();
				BoRoles boRoles = new BoRoles();

				boRoles.setId(Short.parseShort(roleIdsArr[i]));
				boRoles.setName(roleNamesArr[i]);

				boUserRole.setBoRoleId(boRoles);
				boUserRole.setBoUserId(boUser);

				boUserRoleDao.addBoUserRole(boUserRole);
			}

			logger.info("Service EditBoUserRoles ################## 2");
		} catch (DataAccessException dae) {
			logger.info("Service EditBoUserRoles ################## 3 : " + dae);
			throw translateException(dae);
		} catch (Exception e) {
			logger.info("Service EditBoUserRoles ################## 4 : " + e);
			throw new ServiceException(ServiceException.PROCESSING_FAILED,
					e.getMessage(), e);
		}
	}
}
