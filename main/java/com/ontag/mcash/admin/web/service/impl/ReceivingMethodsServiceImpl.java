package com.ontag.mcash.admin.web.service.impl;

import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.ontag.mcash.admin.web.exception.ServiceException;
import com.ontag.mcash.admin.web.service.ReceivingMethodsService;
import com.ontag.mcash.dal.dao.ReceivingMethodsDao;
import com.ontag.mcash.dal.domain.BoRoles;
import com.ontag.mcash.dal.domain.ReceivingMethods;
import com.ontag.mcash.dal.exception.DataAccessException;

@Service
@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
public class ReceivingMethodsServiceImpl extends GenericServiceImpl<ReceivingMethods> implements ReceivingMethodsService{

	@Autowired
	ReceivingMethodsDao receivingMethodsDao;
	
	@PostConstruct
    void init() {
        super.init(ReceivingMethods.class, receivingMethodsDao);
    }
	
	@Override
	public List<ReceivingMethods> getAllReceivingMethods(int offset, int size)
			throws ServiceException {
		try {
		 	logger.debug("ReceivingMethodsServiceImpl getAllRecievingMethods #############");
		 	return receivingMethodsDao.getAllRecievingMethods(offset, size);
		} catch (DataAccessException dae) {
            throw translateException(dae);
        } catch (Exception e) {
            throw new ServiceException(ServiceException.PROCESSING_FAILED, e.getMessage(), e);
        }
	}

	@Override
	public ReceivingMethods findById(short id) throws ServiceException {
		try {
		 	logger.debug("ReceivingMethodsServiceImpl findById #############");
		 	return receivingMethodsDao.findById(id);
		} catch (DataAccessException dae) {
            throw translateException(dae);
        } catch (Exception e) {
            throw new ServiceException(ServiceException.PROCESSING_FAILED, e.getMessage(), e);
        }
	}

	@Override
	public List<ReceivingMethods> getReceivingMethodsByBankId(int bankId)
			throws ServiceException {
		try {
		 	logger.debug("ReceivingMethodsServiceImpl getRecievingMethodsByBankId #############");
		 	return receivingMethodsDao.getReceivingMethodsByBankId(bankId);
		} catch (DataAccessException dae) {
            throw translateException(dae);
        } catch (Exception e) {
            throw new ServiceException(ServiceException.PROCESSING_FAILED, e.getMessage(), e);
        }
	}
	
	@Override
	public List<ReceivingMethods> getAllActiveRecievingMethods()
			throws ServiceException {
		try {
		 	return receivingMethodsDao.getAllActiveRecievingMethods();
		} catch (DataAccessException dae) {
            throw translateException(dae);
        } catch (Exception e) {
            throw new ServiceException(ServiceException.PROCESSING_FAILED, e.getMessage(), e);
        }
	}
}
