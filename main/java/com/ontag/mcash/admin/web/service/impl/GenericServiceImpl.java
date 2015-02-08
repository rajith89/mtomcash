package com.ontag.mcash.admin.web.service.impl;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.ontag.mcash.admin.web.exception.ServiceException;
import com.ontag.mcash.admin.web.service.GenericService;
import com.ontag.mcash.dal.dao.GenericDao;
import com.ontag.mcash.dal.exception.DataAccessException;

import java.util.List;

@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
public class GenericServiceImpl<T> implements GenericService<T> {

    protected static final Logger logger = LoggerFactory.getLogger(GenericServiceImpl.class);

    private Class<? extends T> type;
    protected GenericDao<T> genericDao;

    protected void init(Class<? extends T> type, GenericDao<T> dao) {
        this.type = type;
        this.genericDao = dao;
    }

    @Override
    public ServiceException translateException(DataAccessException de) {
        switch (de.getCode()) {
            case DataAccessException.VALIDATION_FAILED:
                return new ServiceException(ServiceException.VALIDATION_FAILED, de.getMessage());
            default:
                return new ServiceException(ServiceException.PROCESSING_FAILED, de.getMessage());
        }
    }

    @Override
    public T getById(int id) throws ServiceException {
        try {
            return genericDao.getById(id);
        } catch (DataAccessException dae) {
            throw translateException(dae);
        } catch (Exception e) {
            throw new ServiceException(ServiceException.PROCESSING_FAILED, e.getMessage(), e);
        }
    }

    @Override
    @Transactional(readOnly = false)
    public T add(T obj) throws ServiceException {
    	try {
    			logger.info("Service ADD ################## 1" );
	    		genericDao.add(obj);
	    		logger.info("Service ADD ################## 2" );
	    } catch (DataAccessException dae) {
	    	logger.info("Service ADD ################## 3 : " + dae );
	        throw translateException(dae);
	    } catch (Exception e) {
	    	logger.info("Service ADD ################## 4 : " + e );
	        throw new ServiceException(ServiceException.PROCESSING_FAILED, e.getMessage(), e);
	    }
	    return obj;
    }

    @Override
    @Transactional(readOnly = false)
    public T edit(T obj) throws ServiceException {
        logger.debug("calling default method: edit({}) - no changes made", obj);
        try {
        		genericDao.modify(obj);
        } catch (DataAccessException dae) {
            throw translateException(dae);
        } catch (Exception e) {
            throw new ServiceException(ServiceException.PROCESSING_FAILED, e.getMessage(), e);
        }
        return obj;
    }

    @Override
    public List<Object> getList(int offset, int size) throws ServiceException {
        try {
        	logger.info("Service getList Implemntation ###################");
        	return genericDao.getList(offset, size);
        } catch (DataAccessException dae) {
            throw translateException(dae);
        } catch (Exception e) {
            throw new ServiceException(ServiceException.PROCESSING_FAILED, e.getMessage(), e);
        }
//    	logger.debug("Service getList Implemntation ###################");
//        return null;
    }

    @Override
    public void revoke(Integer id) throws ServiceException {
        logger.debug("calling default method: revoke{}) - no changes made", id);
    }

    @Override
    public void revokeAll(Integer[] ids) throws ServiceException {
        logger.debug("calling default method: revokeAll({}) - no changes made", ids);
    }

    @Override
    public int countAll() throws ServiceException {
        try {
            return genericDao.count();
        } catch (DataAccessException dae) {
            throw translateException(dae);
        } catch (Exception e) {
            throw new ServiceException(ServiceException.PROCESSING_FAILED, e.getMessage(), e);
        }
    }

    @Override
    public List<T> getAll() throws ServiceException {
        try {
            return genericDao.getAll();
        } catch (DataAccessException dae) {
            throw translateException(dae);
        } catch (Exception e) {
            throw new ServiceException(ServiceException.PROCESSING_FAILED, e.getMessage(), e);
        }
    }
}
