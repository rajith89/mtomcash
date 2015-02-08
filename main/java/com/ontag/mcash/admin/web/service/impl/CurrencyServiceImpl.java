package com.ontag.mcash.admin.web.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.ontag.mcash.admin.web.exception.ServiceException;
import com.ontag.mcash.admin.web.service.CurrencyService;
import com.ontag.mcash.dal.dao.CurrencyDao;
import com.ontag.mcash.dal.domain.CountryCurrency;
import com.ontag.mcash.dal.domain.CurrencyCode;
import com.ontag.mcash.dal.exception.DataAccessException;

@Service
@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
public class CurrencyServiceImpl extends GenericServiceImpl<CurrencyCode> implements CurrencyService{

	
	protected static org.slf4j.Logger logger = LoggerFactory.getLogger(CurrencyServiceImpl.class);
	
	@Autowired
    private CurrencyDao currencyDao;
	
	@PostConstruct
    void init() {
        super.init(CurrencyCode.class, currencyDao);
    }
	
	@Override
	public List<CurrencyCode> getAllCurrencyCode(int offset, int size)throws ServiceException{
		 try {
			 	return currencyDao.getAllCurrencyCode(offset, size);
			} catch (DataAccessException dae) {
	            throw translateException(dae);
	        } catch (Exception e) {
	            throw new ServiceException(ServiceException.PROCESSING_FAILED, e.getMessage(), e);
	        }
	}
	
	@Override
	public CurrencyCode findById(int id)throws ServiceException{
		try {
		 	return currencyDao.findById(id);
		} catch (DataAccessException dae) {
            throw translateException(dae);
        } catch (Exception e) {
            throw new ServiceException(ServiceException.PROCESSING_FAILED, e.getMessage(), e);
        }
	}
	
	public List<CountryCurrency> getCurrencyByCountry(int countryId)throws ServiceException{
		try {
		 	return currencyDao.getCurrencyByCountry(countryId);
		} catch (DataAccessException dae) {
            throw translateException(dae);
        } catch (Exception e) {
            throw new ServiceException(ServiceException.PROCESSING_FAILED, e.getMessage(), e);
        }
	}
		
}
