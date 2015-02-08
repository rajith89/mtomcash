package com.ontag.mcash.admin.web.service.impl;

import java.util.List;

import javax.annotation.PostConstruct;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.ontag.mcash.admin.web.exception.ServiceException;
import com.ontag.mcash.admin.web.service.BankChargesService;
import com.ontag.mcash.dal.dao.BankChargesDao;
import com.ontag.mcash.dal.dao.BankCurrencyDao;
import com.ontag.mcash.dal.dao.CurrencyDao;
import com.ontag.mcash.dal.domain.AmountSlab;
import com.ontag.mcash.dal.domain.BankCharges;
import com.ontag.mcash.dal.domain.BankCurrency;
import com.ontag.mcash.dal.domain.CurrencyCode;
import com.ontag.mcash.dal.domain.ReceivingMethods;
import com.ontag.mcash.dal.exception.DataAccessException;

@Service
@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
public class BankChargesServiceImpl extends GenericServiceImpl<BankCharges>
		implements BankChargesService {

	protected static org.slf4j.Logger logger = LoggerFactory
			.getLogger(BankChargesServiceImpl.class);

	@Autowired
	private BankChargesDao bankChargesDao;
	@Autowired
	private CurrencyDao currencyDao;

	
	@PostConstruct
	void init() {
		
		super.init(BankCharges.class, bankChargesDao);
	}

	@Override
	public List<BankCharges> getAllBankCharges(int offset, int size)
			throws ServiceException {
		try {
			logger.debug("BankChargesServiceImpl getAllBankCharges #############");
			return bankChargesDao.getAlBankCharges(offset, size);
		} catch (DataAccessException dae) {
			throw translateException(dae);
		} catch (Exception e) {
			throw new ServiceException(ServiceException.PROCESSING_FAILED,
					e.getMessage(), e);
		}
	}

	@Override
	public BankCharges findById(int id) throws ServiceException {
		try {
			logger.debug("BankChargesServiceImpl findById #############");
			return bankChargesDao.findById(id);
		} catch (DataAccessException dae) {
			throw translateException(dae);
		} catch (Exception e) {
			throw new ServiceException(ServiceException.PROCESSING_FAILED,
					e.getMessage(), e);
		}
	}
	
	@Override
	public BankCharges findByBankCurrencyReceivingMethodAmountSlab(int currency, int receivingmethod, int amountslab) throws ServiceException{
		try {
			logger.debug("BankChargesServiceImpl findById #############");
			return bankChargesDao.findByBankCurrencyReceivingMethodAmountSlab(currency, receivingmethod, amountslab);
		} catch (DataAccessException dae) {
			throw translateException(dae);
		} catch (Exception e) {
			throw new ServiceException(ServiceException.PROCESSING_FAILED,
					e.getMessage(), e);
		}
	}

	@Override
	@Transactional(readOnly = false)
	public void setInactive(ReceivingMethods receivingMethodId,
			AmountSlab amountSlabId, CurrencyCode currencyCode)
			throws ServiceException {
		try {
			logger.debug("BankChargesServiceImpl setInactive #############");
			BankCurrency bankCurrency = currencyDao.findBankCurrencyByCrrencyAndBank(currencyCode.getId(), receivingMethodId.getCorporateBankId().getId());
			bankChargesDao.setInactive(receivingMethodId, amountSlabId,
					bankCurrency);
		} catch (DataAccessException dae) {
			throw translateException(dae);
		} catch (Exception e) {
			throw new ServiceException(ServiceException.PROCESSING_FAILED,
					e.getMessage(), e);
		}
	}

}
