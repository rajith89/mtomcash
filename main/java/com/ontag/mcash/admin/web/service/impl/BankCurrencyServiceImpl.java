package com.ontag.mcash.admin.web.service.impl;

import java.util.List;

import javax.annotation.PostConstruct;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.ontag.mcash.admin.web.exception.ServiceException;
import com.ontag.mcash.admin.web.service.BankCurrencyService;
import com.ontag.mcash.dal.dao.BankCurrencyDao;
import com.ontag.mcash.dal.domain.BankCurrency;
import com.ontag.mcash.dal.exception.DataAccessException;

@Service
@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
public class BankCurrencyServiceImpl extends GenericServiceImpl<BankCurrency>
		implements BankCurrencyService {

	protected static org.slf4j.Logger logger = LoggerFactory
			.getLogger(BankCurrencyServiceImpl.class);

	@Autowired
	private BankCurrencyDao bankCurrencyDao;

	@PostConstruct
	void init() {
		super.init(BankCurrency.class, bankCurrencyDao);
	}

	@Override
	public List<BankCurrency> getBankCurrencyByBankId(int bankId)
			throws ServiceException {
		try {
			logger.debug("BankCurrencyServiceImpl getBankCurrencyByBankId #############");
			return bankCurrencyDao.getBankCurrencyByBankId(bankId);
		} catch (DataAccessException dae) {
			throw translateException(dae);
		} catch (Exception e) {
			throw new ServiceException(ServiceException.PROCESSING_FAILED,
					e.getMessage(), e);
		}
	}

	@Override
	public BankCurrency findById(Short id) throws ServiceException {
		try {
			logger.debug("BankCurrencyServiceImpl findById #############");
			return bankCurrencyDao.findById(id);
		} catch (DataAccessException dae) {
			throw translateException(dae);
		} catch (Exception e) {
			throw new ServiceException(ServiceException.PROCESSING_FAILED,
					e.getMessage(), e);
		}
	}

	
	
	
}
