package com.ontag.mcash.admin.web.service.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.ontag.mcash.admin.web.exception.ServiceException;
import com.ontag.mcash.admin.web.service.AppTransactionService;
import com.ontag.mcash.dal.dao.AppTransactionDao;
import com.ontag.mcash.dal.domain.AppTransaction;
import com.ontag.mcash.dal.domain.Merchant;
import com.ontag.mcash.dal.domain.ReceivingMethods;
import com.ontag.mcash.dal.exception.DataAccessException;

@Service
@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
public class AppTransactionServiceImpl extends
		GenericServiceImpl<AppTransaction> implements AppTransactionService {

	@Autowired
	private AppTransactionDao appTransactionDao;

	@PostConstruct
	void init() {
		super.init(AppTransaction.class, appTransactionDao);
	}

	@Override
	public List<AppTransaction> findByPin(String transactionPin)
			throws ServiceException {
		try {
			logger.info("Service AppTransactionServiceImpl findByPin Implemntation ###################");
			return appTransactionDao.findByPin(transactionPin);
		} catch (DataAccessException dae) {
			throw translateException(dae);
		} catch (Exception e) {
			throw new ServiceException(ServiceException.PROCESSING_FAILED,
					e.getMessage(), e);
		}
	}

	@Override
	public List<AppTransaction> findByDate(Date from, Date to)
			throws ServiceException {
		try {
			logger.info("Service AppTransactionServiceImpl findByDate Implemntation ###################");
			return appTransactionDao.findByDate(from, to);
		} catch (DataAccessException dae) {
			throw translateException(dae);
		} catch (Exception e) {
			throw new ServiceException(ServiceException.PROCESSING_FAILED,
					e.getMessage(), e);
		}
	}

	@Override
	public List<AppTransaction> findByMerchant(Merchant merchantId)
			throws ServiceException {
		try {
			logger.info("Service AppTransactionServiceImpl findByMerchant Implemntation ###################");
			return appTransactionDao.findByMerchant(merchantId);
		} catch (DataAccessException dae) {
			throw translateException(dae);
		} catch (Exception e) {
			throw new ServiceException(ServiceException.PROCESSING_FAILED,
					e.getMessage(), e);
		}
	}

	@Override
	public List<AppTransaction> findByMerchantAndDate(Merchant merchant,
			Date from, Date to) throws ServiceException {
		try {
			logger.info("Service AppTransactionServiceImpl findByMerchantAndDate Implemntation ###################");
			return appTransactionDao.findByMerchantAndDate(merchant, from, to);
		} catch (DataAccessException dae) {
			throw translateException(dae);
		} catch (Exception e) {
			throw new ServiceException(ServiceException.PROCESSING_FAILED,
					e.getMessage(), e);
		}
	}

	@Override
	public List<Object[]> getMerchantCommissionSummary(Date from, Date to)
			throws ServiceException {
		try {
			logger.info("Service AppTransactionServiceImpl getMerchantCommissionSummary Implemntation ###################");
			return appTransactionDao.getMerchantCommissionSummary(from, to);
		} catch (DataAccessException dae) {
			throw translateException(dae);
		} catch (Exception e) {
			throw new ServiceException(ServiceException.PROCESSING_FAILED,
					e.getMessage(), e);
		}
	}

	@Override
	public List<Object[]> getIncomeSummaryByDate(Date from, Date to)
			throws ServiceException {
		try {
			logger.info("Service AppTransactionServiceImpl getIncomeSummaryByDate Implemntation ###################");
			return appTransactionDao.getIncomeSummaryByDate(from, to);
		} catch (DataAccessException dae) {
			throw translateException(dae);
		} catch (Exception e) {
			throw new ServiceException(ServiceException.PROCESSING_FAILED,
					e.getMessage(), e);
		}
	}

	@Override
	public List<Object[]> getIncomeSummaryByTransactionType(
			ReceivingMethods receivingMethod) throws ServiceException {
		try {
			logger.info("Service AppTransactionServiceImpl getIncomeSummaryByTransactionType Implemntation ###################");
			return appTransactionDao
					.getIncomeSummaryByTransactionType(receivingMethod);
		} catch (DataAccessException dae) {
			throw translateException(dae);
		} catch (Exception e) {
			throw new ServiceException(ServiceException.PROCESSING_FAILED,
					e.getMessage(), e);
		}
	}

	@Override
	public List<Object[]> getIncomeSummaryByDateAndTransactionType(Date from,
			Date to, ReceivingMethods receivingMethods) throws ServiceException {
		try {
			logger.info("Service AppTransactionServiceImpl getIncomeSummaryByDateAndTransactionType Implemntation ###################");
			return appTransactionDao.getIncomeSummaryByDateAndTransactionType(
					from, to, receivingMethods);
		} catch (DataAccessException dae) {
			throw translateException(dae);
		} catch (Exception e) {
			throw new ServiceException(ServiceException.PROCESSING_FAILED,
					e.getMessage(), e);
		}
	}

	@Override
	public List<Object[]> getProfitByDate(Date from, Date to)
			throws ServiceException {
		try {
			logger.info("Service AppTransactionServiceImpl getProfitByDate Implemntation ###################");
			return appTransactionDao.getProfitByDate(from, to);
		} catch (DataAccessException dae) {
			throw translateException(dae);
		} catch (Exception e) {
			throw new ServiceException(ServiceException.PROCESSING_FAILED,
					e.getMessage(), e);
		}
	}

	@Override
	public List<Object[]> getProfitByTransactionType(
			ReceivingMethods receivingMethod) throws ServiceException {
		try {
			logger.info("Service AppTransactionServiceImpl getProfitByTransactionType Implemntation ###################");
			return appTransactionDao
					.getProfitByTransactionType(receivingMethod);
		} catch (DataAccessException dae) {
			throw translateException(dae);
		} catch (Exception e) {
			throw new ServiceException(ServiceException.PROCESSING_FAILED,
					e.getMessage(), e);
		}
	}

	@Override
	public List<Object[]> getProfitByDateAndTransactionType(Date from, Date to,
			ReceivingMethods receivingMethods) throws ServiceException {
		try {
			logger.info("Service AppTransactionServiceImpl getProfitByDateAndTransactionType Implemntation ###################");
			return appTransactionDao.getProfitByDateAndTransactionType(from,
					to, receivingMethods);
		} catch (DataAccessException dae) {
			throw translateException(dae);
		} catch (Exception e) {
			throw new ServiceException(ServiceException.PROCESSING_FAILED,
					e.getMessage(), e);
		}
	}

	@Override
	public AppTransaction findById(long transactionId) throws ServiceException {
		try {
			return appTransactionDao.findById(transactionId);
		} catch (DataAccessException dae) {
			throw translateException(dae);
		} catch (Exception e) {
			throw new ServiceException(ServiceException.PROCESSING_FAILED,
					e.getMessage(), e);
		}
	}

	@Override
	public List<AppTransaction> findFailTransactionByMerchant(Merchant merchant)
			throws ServiceException {
		try {
			logger.info("Service AppTransactionServiceImpl findFailTransactionByMerchant Implemntation ###################");
			return appTransactionDao.findFailTransactionByMerchant(merchant);
		} catch (DataAccessException dae) {
			throw translateException(dae);
		} catch (Exception e) {
			throw new ServiceException(ServiceException.PROCESSING_FAILED,
					e.getMessage(), e);
		}
	}

	@Override
	public List<AppTransaction> findFailTransactionByMerchantAndDate(
			Merchant merchant, Date from, Date to) throws ServiceException {
		try {
			logger.info("Service AppTransactionServiceImpl findFailTransactionByMerchantAndDate Implemntation ###################");
			return appTransactionDao.findFailTransactionByMerchantAndDate(
					merchant, from, to);
		} catch (DataAccessException dae) {
			throw translateException(dae);
		} catch (Exception e) {
			throw new ServiceException(ServiceException.PROCESSING_FAILED,
					e.getMessage(), e);
		}
	}

	@Override
	public List<AppTransaction> findFailTransactionByDate(Date from, Date to)
			throws ServiceException {
		try {
			logger.info("Service AppTransactionServiceImpl findFailTransactionByDate Implemntation ###################");
			return appTransactionDao.findFailTransactionByDate(from, to);
		} catch (DataAccessException dae) {
			throw translateException(dae);
		} catch (Exception e) {
			throw new ServiceException(ServiceException.PROCESSING_FAILED,
					e.getMessage(), e);
		}
	}

	@Override
	public List<AppTransaction> getMerchantCommissionsByDate(Date from, Date to)
			throws ServiceException {
		try {
			logger.info("Service AppTransactionServiceImpl getMerchantCommissions Implemntation ###################");
			return appTransactionDao.getMerchantCommissionByDate(from, to);
		} catch (DataAccessException dae) {
			throw translateException(dae);
		} catch (Exception e) {
			throw new ServiceException(ServiceException.PROCESSING_FAILED,
					e.getMessage(), e);
		}
	}

}
