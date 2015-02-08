package com.ontag.mcash.admin.web.service.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.ontag.mcash.admin.web.controller.json.MerchantFilter;
import com.ontag.mcash.admin.web.exception.ServiceException;
import com.ontag.mcash.admin.web.service.MerchantTransactionService;
import com.ontag.mcash.admin.web.util.Constants;
import com.ontag.mcash.dal.dao.MerchantDao;
import com.ontag.mcash.dal.dao.MerchantTransactionDao;
import com.ontag.mcash.dal.dao.SubMerchantDao;
import com.ontag.mcash.dal.dao.SubMerchantTransactionDao;
import com.ontag.mcash.dal.domain.MerchantTransaction;
import com.ontag.mcash.dal.domain.Merchant;
import com.ontag.mcash.dal.domain.SubMerchantTransaction;
import com.ontag.mcash.dal.exception.DataAccessException;

@Service
@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
public class MerchantTransactionServiceImpl extends
		GenericServiceImpl<MerchantTransaction> implements
		MerchantTransactionService {

	protected static org.slf4j.Logger logger = LoggerFactory
			.getLogger(MerchantTransactionServiceImpl.class);

	@Autowired
	private MerchantTransactionDao merchantTransactionDao;

	@Autowired
	private MerchantDao merchantDao;

	@Autowired
	private SubMerchantDao subMerchantDao;

	@PostConstruct
	void init() {
		super.init(MerchantTransaction.class, merchantTransactionDao);
	}

	@Override
	@Transactional(readOnly = false)
	public void topupMerchant(Merchant merchant, MerchantFilter merchantFilter,
			MerchantTransaction merchantTransaction) throws ServiceException {

		try {
			merchant.setBalance(merchant.getBalance()
					+ merchantFilter.getCredit());
			merchantDao.modify(merchant);
			merchantTransactionDao.add(merchantTransaction);
		} catch (DataAccessException dae) {
			throw translateException(dae);
		} catch (Exception e) {
			throw new ServiceException(ServiceException.PROCESSING_FAILED,
					e.getMessage(), e);
		}

	}

	@Override
	public List<MerchantTransaction> getAllMerchantTransactionByCrDr(String crdr)
			throws ServiceException {
		try {
			return merchantTransactionDao.getAllMerchantTransactionByCrDr(crdr);
		} catch (DataAccessException dae) {
			throw translateException(dae);
		} catch (Exception e) {
			throw new ServiceException(ServiceException.PROCESSING_FAILED,
					e.getMessage(), e);
		}
	}

	@Override
	public ServiceException translateException(DataAccessException de) {
		switch (de.getCode()) {
		case DataAccessException.VALIDATION_FAILED:
			return new ServiceException(ServiceException.VALIDATION_FAILED,
					de.getMessage());
		default:
			return new ServiceException(ServiceException.PROCESSING_FAILED,
					de.getMessage());
		}
	}

	@Override
	public List<MerchantTransaction> getMerchantTransactionByDateAndMerchant(
			Date from, Date to, Merchant merchant) throws ServiceException {
		try {
			return merchantTransactionDao
					.getMerchantTransactionByDateAndMerchant(from, to, merchant);
		} catch (DataAccessException dae) {
			throw translateException(dae);
		} catch (Exception e) {
			throw new ServiceException(ServiceException.PROCESSING_FAILED,
					e.getMessage(), e);
		}
	}

	@Override
	public List<MerchantTransaction> getMerchantTransactionByMerchant(
			Merchant merchant) throws ServiceException {
		try {
			return merchantTransactionDao
					.getMerchantTransactionByMerchant(merchant);
		} catch (DataAccessException dae) {
			throw translateException(dae);
		} catch (Exception e) {
			throw new ServiceException(ServiceException.PROCESSING_FAILED,
					e.getMessage(), e);
		}
	}

	@Override
	public List<Object[]> getMerchantTopupSummery(Date from, Date to)
			throws ServiceException {
		try {
			return merchantTransactionDao.getMerchantTopupSummery(from, to);
		} catch (DataAccessException dae) {
			throw translateException(dae);
		} catch (Exception e) {
			throw new ServiceException(ServiceException.PROCESSING_FAILED,
					e.getMessage(), e);
		}
	}
}
