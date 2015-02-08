package com.ontag.mcash.admin.web.service.impl;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import au.com.bytecode.opencsv.CSVReader;

import com.ontag.mcash.admin.web.exception.ServiceException;
import com.ontag.mcash.admin.web.service.BoUserService;
import com.ontag.mcash.admin.web.service.CurrencyRatesService;
import com.ontag.mcash.dal.dao.BankCurrencyDao;
import com.ontag.mcash.dal.dao.CorporateBankDao;
import com.ontag.mcash.dal.dao.CurrencyDao;
import com.ontag.mcash.dal.dao.CurrencyRatesDao;
import com.ontag.mcash.dal.domain.BankCurrency;
import com.ontag.mcash.dal.domain.BoUsers;
import com.ontag.mcash.dal.domain.CorporateBank;
import com.ontag.mcash.dal.domain.CurrencyCode;
import com.ontag.mcash.dal.domain.CurrencyRates;
import com.ontag.mcash.dal.exception.DataAccessException;

@Service
@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
public class CurrencyRatesServiceImpl extends GenericServiceImpl<CurrencyRates>
		implements CurrencyRatesService {

	protected static org.slf4j.Logger logger = LoggerFactory
			.getLogger(CurrencyRatesServiceImpl.class);

	@Autowired
	private CurrencyRatesDao currencyRatesDao;

	@Autowired
	private CurrencyDao currencyDao;

	@Autowired
	CorporateBankDao corporateBankDao;

	@Autowired
	BankCurrencyDao bankCurrencyDao;

	@Autowired
	BoUserService boUserService;

	@PostConstruct
	void init() {
		super.init(CurrencyRates.class, currencyRatesDao);
	}

	@Override
	public List<CurrencyRates> getActiveCurrencyRates()throws ServiceException{
		try {
			return currencyRatesDao.getActiveCurrencyRates();
		} catch (DataAccessException dae) {
			throw translateException(dae);
		} catch (Exception e) {
			throw new ServiceException(ServiceException.PROCESSING_FAILED,
					e.getMessage(), e);
		}
	}
	
	@Override
	public List<CurrencyRates> getAllCurrencyRates(int offset, int size)
			throws ServiceException {
		try {
			return currencyRatesDao.getAllCurrencyRates(offset, size);
		} catch (DataAccessException dae) {
			throw translateException(dae);
		} catch (Exception e) {
			throw new ServiceException(ServiceException.PROCESSING_FAILED,
					e.getMessage(), e);
		}
	}

	@Override
	public CurrencyRates findById(int id) throws ServiceException {
		try {
			return currencyRatesDao.findById(id);
		} catch (DataAccessException dae) {
			throw translateException(dae);
		} catch (Exception e) {
			throw new ServiceException(ServiceException.PROCESSING_FAILED,
					e.getMessage(), e);
		}
	}

	@Override
	@Transactional(readOnly = false)
	public void addCurrencyRatesFromCSV(Short bankId, MultipartFile file)
			throws ServiceException {
		logger.info("CurrencyRatesServiceImpl addCurrencyRatesFromCSV ######################### 1");
		try {
			logger.info("CurrencyRatesServiceImpl addCurrencyRatesFromCSV ######################### 2");
			InputStream inputStream = file.getInputStream();
			BufferedReader bufferedReader = new BufferedReader(
					new InputStreamReader(inputStream));
			CSVReader csvReader = new CSVReader(bufferedReader);
			String[] row = null;

			while ((row = csvReader.readNext()) != null) {
				System.out.println(row[0] + " # " + row[1] + " #  " + row[2]
						+ " #  " + row[3]);

				CorporateBank corporateBank = corporateBankDao.findById(bankId);
				// corporateBank.setId(bankId);
				CurrencyRates currencyRates = new CurrencyRates();

				CurrencyCode currencyCode = currencyDao
						.getIdByCurrencyCode(row[0]);

				logger.info("Currency Code Id : ################### "
						+ currencyCode.getId());

				Object obj = currencyRatesDao.getIdByBankAndCurrency(
						corporateBank, currencyCode);

				Date datetime = new Date();
				currencyRates.setLastUpdateDate(datetime);

				Authentication auth = SecurityContextHolder.getContext()
						.getAuthentication();

				String userName = auth.getName();
				currencyRates.setLastUpdateUser(boUserService
						.getUserByUserName(userName).getId());

				if (obj != null) {
					System.out
							.println("Bank_currency found ################## id : "
									+ (short) obj);

					currencyRates.setBankCurrencyCodeId(bankCurrencyDao
							.getById((short) obj));
					currencyRates.setTtBuying(Double.parseDouble(row[1]));
					currencyRates.setOdBuying(Double.parseDouble(row[2]));
					currencyRates.setTtSelling(Double.parseDouble(row[3]));
					currencyRates.setAmount(Double.parseDouble(row[4]));
					currencyRates
							.setStatus(CurrencyRates.CURRENCYRATE_STATUS_ACTIVE);
					currencyRatesDao.setInactive(bankCurrencyDao
							.getById((short) obj));
					currencyRatesDao.add(currencyRates);

				} else {
					System.out
							.println("Bank_currency not found ##################");
					 BankCurrency bankCurrency = new BankCurrency();
					 bankCurrency.setCurrencyCodeId(currencyCode);
					 bankCurrency.setCorporateBankId(corporateBank);
					
					 bankCurrency.setDefaultbankcurrency(BankCurrency.VALUE_NOT_DEFAULT);
					 currencyRates.setBankCurrencyCodeId(bankCurrency);
					 currencyRates.setTtBuying(Double.parseDouble(row[1]));
					 currencyRates.setOdBuying(Double.parseDouble(row[2]));
					 currencyRates.setTtSelling(Double.parseDouble(row[3]));
					 currencyRates.setAmount(Double.parseDouble(row[3]));
					 currencyRates
					 .setStatus(CurrencyRates.CURRENCYRATE_STATUS_ACTIVE);
					 logger.info("Currency TEST TTTTTTTTT : ################### 9");
					
					// currencyRatesDao.add(currencyRates);
				}
			}
			csvReader.close();
		} catch (DataAccessException dae) {
			throw translateException(dae);
		} catch (Exception e) {
			throw new ServiceException(ServiceException.PROCESSING_FAILED,
					e.getMessage(), e);
		}
	}
}
