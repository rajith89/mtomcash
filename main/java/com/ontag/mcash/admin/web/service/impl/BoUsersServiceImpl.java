package com.ontag.mcash.admin.web.service.impl;

import java.math.BigInteger;
import java.util.ArrayList;
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
import com.ontag.mcash.admin.web.service.BoUserService;
import com.ontag.mcash.admin.web.service.CurrencyService;
import com.ontag.mcash.admin.web.util.Constants;
import com.ontag.mcash.admin.web.util.MailHandler;
import com.ontag.mcash.dal.dao.BoUsersDao;
import com.ontag.mcash.dal.dao.CurrencyDao;
import com.ontag.mcash.dal.domain.BoUsers;
import com.ontag.mcash.dal.domain.CurrencyCode;
import com.ontag.mcash.dal.domain.MerchantUsers;
import com.ontag.mcash.dal.exception.DataAccessException;

@Service
@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
public class BoUsersServiceImpl extends GenericServiceImpl<BoUsers> implements
		BoUserService {

	protected static org.slf4j.Logger logger = LoggerFactory
			.getLogger(BoUsersServiceImpl.class);

	@Autowired
	private BoUsersDao boUsersDao;

	@Autowired
	private MailHandler mailHandler;

	@PostConstruct
	void init() {
		super.init(BoUsers.class, boUsersDao);
	}

	@Override
	public List<BoUsers> getAllBoUsers(int offset, int size)
			throws ServiceException {
		try {
			logger.debug("BoUsersServiceImpl getAllBoUsers #############");
			return boUsersDao.getAllBoUsers(offset, size);
		} catch (DataAccessException dae) {
			throw translateException(dae);
		} catch (Exception e) {
			throw new ServiceException(ServiceException.PROCESSING_FAILED,
					e.getMessage(), e);
		}
	}

	public BoUsers findById(int id) throws ServiceException {
		try {
			logger.debug("BoUsersServiceImpl findById #############");
			return boUsersDao.findById(id);
		} catch (DataAccessException dae) {
			throw translateException(dae);
		} catch (Exception e) {
			throw new ServiceException(ServiceException.PROCESSING_FAILED,
					e.getMessage(), e);
		}
	}

	@Override
	public BoUsers getUserByUserName(String userName) throws ServiceException {
		try {
			return boUsersDao.findByUsername(userName);
		} catch (DataAccessException dae) {
			throw translateException(dae);
		} catch (Exception e) {
			throw new ServiceException(ServiceException.PROCESSING_FAILED,
					e.getMessage(), e);
		}
	}

	@Override
	@Transactional(readOnly = false)
	public void changePassword(BoUsers boUsers) throws ServiceException {
		try {
			boUsersDao.changePassword(boUsers);
		} catch (DataAccessException dae) {
			throw translateException(dae);
		} catch (Exception e) {
			throw new ServiceException(ServiceException.PROCESSING_FAILED,
					e.getMessage(), e);
		}
	}

	@Override
	@Transactional(readOnly = false)
	public void resetPassword(BoUsers boUsers) throws ServiceException {
		try {
			BoUsers user = boUsersDao.findByUsername(boUsers.getUserName());
			if (user == null)
				throw new Exception("User does not exist.");

			Md5PasswordEncoder encoder = new Md5PasswordEncoder();
			Random random = new Random();
			String defaultPassword = new BigInteger(52, random).toString(32);
			String defaultPasswordPin = new BigInteger(52, random).toString(32);

			user.setPasswordEnc(encoder.encodePassword(defaultPassword, null));
			user.setPasswordPinEnc(encoder.encodePassword(defaultPasswordPin,
					null));

			StringBuffer message = new StringBuffer();
			message.append("Hi " + user.getFirstName() + "<br><br>");
			message.append("Your account password at 'MtoMCash' has been reset <br>");
			message.append("and you have been issued with a new temporary password. <br><br>");
			message.append("Your current login information is now: <br>");

			message.append("Username : " + user.getUserName() + "<br>");
			message.append("Password : " + defaultPassword + "<br>");
			message.append("Password PIN : " + defaultPasswordPin + "<br>");

			mailHandler.sendHtmlMailNew(Constants.ADMIN_EMAIL, user.getEmail(),
					Constants.PASSWORD_RESET_EMAIL_SUBJECT, message.toString(),
					"");

		} catch (DataAccessException dae) {
			throw translateException(dae);
		} catch (Exception e) {
			throw new ServiceException(ServiceException.PROCESSING_FAILED,
					e.getMessage(), e);
		}

	}

	@Override
	public boolean validatePasswordPin(String username, String passwordPin)
			throws ServiceException {

		try {
			Md5PasswordEncoder encoder = new Md5PasswordEncoder();
			BoUsers boUsers = boUsersDao.findByUsername(username);
			if (boUsers.getPasswordPinEnc().equals(
					encoder.encodePassword(passwordPin, null)))
				return true;
		} catch (DataAccessException dae) {
			throw translateException(dae);
		} catch (Exception e) {
			throw new ServiceException(ServiceException.PROCESSING_FAILED,
					e.getMessage(), e);
		}
		return false;
	}

	@Override
	@Transactional(readOnly = false)
	public void activateUsers(Integer[] userIds) throws ServiceException {
		try {
			boUsersDao.activateUsers(userIds);
		} catch (DataAccessException dae) {
			throw translateException(dae);
		} catch (Exception e) {
			throw new ServiceException(ServiceException.PROCESSING_FAILED,
					e.getMessage(), e);
		}
	}

	@Override
	@Transactional(readOnly = false)
	public void deactivateUsers(Integer[] userIds) throws ServiceException {
		try {
			System.out.println("dectivate service");
			boUsersDao.deactivateUsers(userIds);
		} catch (DataAccessException dae) {
			throw translateException(dae);
		} catch (Exception e) {
			throw new ServiceException(ServiceException.PROCESSING_FAILED,
					e.getMessage(), e);
		}

	}

}
