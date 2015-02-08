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
import com.ontag.mcash.admin.web.service.CountryService;
import com.ontag.mcash.dal.dao.CountryDao;
import com.ontag.mcash.dal.domain.Country;
import com.ontag.mcash.dal.domain.ReceiveSendCountry;
import com.ontag.mcash.dal.exception.DataAccessException;

@Service
@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
public class CountryServiceImpl extends GenericServiceImpl<ReceiveSendCountry> implements CountryService{

	
	protected static org.slf4j.Logger logger = LoggerFactory.getLogger(CountryServiceImpl.class);
	
	@Autowired
    private CountryDao countryDao;
	
	@PostConstruct
    void init() {
        super.init(ReceiveSendCountry.class, countryDao);
    }
	
	@Override
	public List<Country> getAllCountry(int offset, int size)throws ServiceException{
		 try {
			 	return countryDao.getAllCountry(offset, size);
			} catch (DataAccessException dae) {
	            throw translateException(dae);
	        } catch (Exception e) {
	            throw new ServiceException(ServiceException.PROCESSING_FAILED, e.getMessage(), e);
	        }
	}
	
	@Override
	public List<ReceiveSendCountry> getAllReceiveSendCountry(int offset, int size)throws ServiceException{
		try {
		 	return countryDao.getAllReceiveSendCountry(offset, size);
		} catch (DataAccessException dae) {
            throw translateException(dae);
        } catch (Exception e) {
            throw new ServiceException(ServiceException.PROCESSING_FAILED, e.getMessage(), e);
        }
	}
	
	@Override
	public List<ReceiveSendCountry> getAllCountryByTypeAndByStatus(short type, int status) throws ServiceException{
		try {
		 	return countryDao.getAllCountryByTypeAndByStatus(type, status);
		} catch (DataAccessException dae) {
            throw translateException(dae);
        } catch (Exception e) {
            throw new ServiceException(ServiceException.PROCESSING_FAILED, e.getMessage(), e);
        }
	}
	
	@Override
	public Country findById(int id)throws ServiceException{
		try {
		 	return countryDao.findById(id);
		} catch (DataAccessException dae) {
            throw translateException(dae);
        } catch (Exception e) {
            throw new ServiceException(ServiceException.PROCESSING_FAILED, e.getMessage(), e);
        }
	}
	
	@Override
	public ReceiveSendCountry findByCountryAndType(int countryId, int type) throws ServiceException{
		try {
		 	return countryDao.findByCountryAndType(countryId, type);
		} catch (DataAccessException dae) {
            throw translateException(dae);
        } catch (Exception e) {
            throw new ServiceException(ServiceException.PROCESSING_FAILED, e.getMessage(), e);
        }
	}
	
	@Override
	@Transactional(readOnly = false)
	public void activateSendReceiveCountries(Integer[] ids) throws ServiceException{
		try {
		 	countryDao.activateSendReceiveCountries(ids);
		} catch (DataAccessException dae) {
            throw translateException(dae);
        } catch (Exception e) {
            throw new ServiceException(ServiceException.PROCESSING_FAILED, e.getMessage(), e);
        }
	}
	
	@Override
	@Transactional(readOnly = false)
	public void inactivateSendReceiveCountries(Integer[] ids) throws ServiceException{
		try {
		 	countryDao.inactivateSendReceiveCountries(ids);
		} catch (DataAccessException dae) {
            throw translateException(dae);
        } catch (Exception e) {
            throw new ServiceException(ServiceException.PROCESSING_FAILED, e.getMessage(), e);
        }
	}

}
