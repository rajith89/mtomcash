package com.ontag.mcash.admin.web.service.impl;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import java.util.Random;

import javax.annotation.PostConstruct;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.ontag.mcash.admin.web.exception.ServiceException;
import com.ontag.mcash.admin.web.service.AppPermissionsService;
import com.ontag.mcash.admin.web.service.MerchantService;
import com.ontag.mcash.admin.web.util.MailHandler;
import com.ontag.mcash.dal.dao.MerchantDao;
import com.ontag.mcash.dal.dao.MerchantRolesPermissionsDao;
import com.ontag.mcash.dal.dao.MerchantRolesUsersDao;
import com.ontag.mcash.dal.domain.AppPermissions;
import com.ontag.mcash.dal.domain.Merchant;
import com.ontag.mcash.dal.domain.MerchantRolePermission;
import com.ontag.mcash.dal.domain.MerchantRoles;
import com.ontag.mcash.dal.domain.MerchantRolesUsers;
import com.ontag.mcash.dal.domain.MerchantUsers;
import com.ontag.mcash.dal.exception.DataAccessException;
import com.ontag.mcash.admin.web.util.Constants;

@Service
@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
public class MerchantServiceImpl extends GenericServiceImpl<Merchant> implements MerchantService {

	protected static org.slf4j.Logger logger = LoggerFactory
			.getLogger(MerchantServiceImpl.class);

	@Autowired
	private MerchantDao merchantDao;

	@Autowired
	private AppPermissionsService appPermissionsService;

	@Autowired
	private MerchantRolesPermissionsDao merchantRolesPermissionsDao;

	@Autowired
	private MerchantRolesUsersDao merchantRolesUsersDao;

	@Autowired
	private MailHandler mailHandler;

	@PostConstruct
	void init() {
		super.init(Merchant.class, merchantDao);
	}

	@Override
	public List<Merchant> getAllMerchant(int offset, int size) throws ServiceException {
		try {
			return merchantDao.getAllMerchant(offset, size);
		} catch (DataAccessException dae) {
			throw translateException(dae);
		} catch (Exception e) {
			throw new ServiceException(ServiceException.PROCESSING_FAILED,
					e.getMessage(), e);
		}
	}

	@Override
	public List<Merchant> getAllActiveMerchant() throws ServiceException {
		try {
			return merchantDao.getAllAciveMerchant();
		} catch (DataAccessException dae) {
			throw translateException(dae);
		} catch (Exception e) {
			throw new ServiceException(ServiceException.PROCESSING_FAILED,
					e.getMessage(), e);
		}
	}
	
	@Override
	public List<Object[]> getMerchantBalance()throws ServiceException{
		try {
			return merchantDao.getMerchantBalance();
		} catch (DataAccessException dae) {
			throw translateException(dae);
		} catch (Exception e) {
			throw new ServiceException(ServiceException.PROCESSING_FAILED,
					e.getMessage(), e);
		}
	}

	@Override
	public Merchant findById(int id) throws ServiceException {
		try {
			return merchantDao.findById(id);
		} catch (DataAccessException dae) {
			throw translateException(dae);
		} catch (Exception e) {
			throw new ServiceException(ServiceException.PROCESSING_FAILED,
					e.getMessage(), e);
		}
	}

	@Transactional(readOnly = false)
	@Override
	public void activateMerchants(Integer[] ids) throws ServiceException {
		try {
			merchantDao.activateMerchants(ids);
		} catch (DataAccessException dae) {
			throw translateException(dae);
		} catch (Exception e) {
			throw new ServiceException(ServiceException.PROCESSING_FAILED,
					e.getMessage(), e);
		}
	}

	@Transactional(readOnly = false)
	@Override
	public void suspendMerchants(Integer[] ids) throws ServiceException {
		try {
			merchantDao.suspendMerchants(ids);
		} catch (DataAccessException dae) {
			throw translateException(dae);
		} catch (Exception e) {
			throw new ServiceException(ServiceException.PROCESSING_FAILED,
					e.getMessage(), e);
		}
	}

	@Transactional(readOnly = false)
	@Override
	public void addDefaultMerchantUser(Merchant merchant)
			throws ServiceException {

		try {
			Random random = new Random();
			String defaultUsername = merchant.getFirstName()
					+ new BigInteger(32, random).toString(32);
			String defaultPassword = new BigInteger(52, random).toString(32);
			String defaultPasswordPin = new BigInteger(52,random).toString(32);

			List<AppPermissions> permissionList = appPermissionsService
					.getMerchantFullPermissionsList(0, 0);

			MerchantRoles defaultRole = new MerchantRoles();
			defaultRole.setMerchantId(merchant);
			defaultRole.setName("Admin");

			for (AppPermissions appPermission : permissionList) {

				MerchantRolePermission rolePermission = new MerchantRolePermission();
				rolePermission.setMerchantRoleId(defaultRole);
				rolePermission.setPermissionId(appPermission);

				try {
					merchantRolesPermissionsDao.add(rolePermission);
				} catch (DataAccessException dae) {
					throw translateException(dae);
				} catch (Exception e) {
					throw new ServiceException(
							ServiceException.PROCESSING_FAILED, e.getMessage(),
							e);
				}
			}

			MerchantUsers defaultUser = new MerchantUsers();

			defaultUser.setEmail(merchant.getEmail());
			defaultUser.setUserName(defaultUsername);
			Md5PasswordEncoder encoder = new Md5PasswordEncoder();
			defaultUser.setPasswordEnc(encoder.encodePassword(defaultPassword,
					null));
			defaultUser.setPasswordPinEnc(encoder.encodePassword(defaultPasswordPin,
					null));

			defaultUser.setMerchantId(merchant);
			defaultUser.setFirstName("Default User");
			defaultUser.setLastName("Default User");
			defaultUser.setCreatedUser(0);
			defaultUser.setLastUpdateUser(0);
			Date currentdate = new Date();
			defaultUser.setCreatedDate(currentdate);
			defaultUser.setLastUpdateDate(currentdate);
			defaultUser.setStatus(MerchantUsers.MERCHANT_STATUS_ACTIVE);

			MerchantRolesUsers merchantRolesUsers = new MerchantRolesUsers();
			merchantRolesUsers.setMerchantUsersId(defaultUser);
			merchantRolesUsers.setMerchantRolesId(defaultRole);

			merchantRolesUsersDao.addMerchantRolesUsers(merchantRolesUsers);

			StringBuffer message = new StringBuffer();
			message.append("Your MtoMCash Merchant account created successfully. Please use the following credentials to proceed.<br><br>");
			message.append("Username : " + defaultUsername + "<br>");
			message.append("Password : " + defaultPassword + "<br>");
			message.append("Password Pin : " + defaultPasswordPin + "<br><br><br>");
			message.append("help desk : ");
			message.append(Constants.HELP_DESK_NO);
			mailHandler.sendHtmlMailNew(Constants.ADMIN_EMAIL,
					merchant.getEmail(), Constants.CREDENTIAL_EMAIL_SUBJECT,
					message.toString(), "");

		} catch (DataAccessException dae) {
			throw translateException(dae);
		} catch (Exception e) {
			throw new ServiceException(ServiceException.PROCESSING_FAILED,
					e.getMessage(), e);
		}

	}
}
