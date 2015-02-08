package com.ontag.mcash.admin.web.service.impl;

import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.ontag.mcash.admin.web.exception.ServiceException;
import com.ontag.mcash.admin.web.service.AppPermissionsService;
import com.ontag.mcash.dal.dao.AppPermissionsDao;
import com.ontag.mcash.dal.domain.AppPermissions;
import com.ontag.mcash.dal.exception.DataAccessException;

@Service
@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
public class AppPermissionsServiceImpl extends GenericServiceImpl<AppPermissions> implements AppPermissionsService{

	@Autowired
    private AppPermissionsDao appPermissionsDao;
	
	@PostConstruct
    void init() {
        super.init(AppPermissions.class, appPermissionsDao);
    }

	@Override
	public List<AppPermissions> getAdminPermissionsList(int offset, int size)
			throws ServiceException {
		try {
        	logger.info("Service getAdminPermissionsList Implemntation ###################");
        	return appPermissionsDao.getAdminPermissionsList(offset, size);
        } catch (DataAccessException dae) {
            throw translateException(dae);
        } catch (Exception e) {
            throw new ServiceException(ServiceException.PROCESSING_FAILED, e.getMessage(), e);
        }
	}

	@Override
	public AppPermissions findById(long id) throws ServiceException {
		try {
		 	logger.debug("AppPermissionsServiceImpl findById #############");
		 	return appPermissionsDao.findById(id);
		} catch (DataAccessException dae) {
            throw translateException(dae);
        } catch (Exception e) {
            throw new ServiceException(ServiceException.PROCESSING_FAILED, e.getMessage(), e);
        }
	}

	@Override
	public List<AppPermissions> getMerchantPermissionsList(int offset, int size)
			throws ServiceException {
		try {
        	logger.info("Service getMerchantPermissionsList Implemntation ###################");
        	return appPermissionsDao.getMerchantPermissionsList(offset, size);
        } catch (DataAccessException dae) {
            throw translateException(dae);
        } catch (Exception e) {
            throw new ServiceException(ServiceException.PROCESSING_FAILED, e.getMessage(), e);
        }
	}

	@Override
	public List<AppPermissions> getSubMerchantPermissionsList(int offset,
			int size) throws ServiceException {
		try {
        	logger.info("Service getSubMerchantPermissionsList Implemntation ###################");
        	return appPermissionsDao.getSubMerchantPermissionsList(offset, size);
        } catch (DataAccessException dae) {
            throw translateException(dae);
        } catch (Exception e) {
            throw new ServiceException(ServiceException.PROCESSING_FAILED, e.getMessage(), e);
        }
	}

	@Override
	public List<AppPermissions> getMerchantFullPermissionsList(int offset, int size)
			throws ServiceException {
		try {
        	logger.info("Service getMerchantFullPermissionsList Implemntation ###################");
        	return appPermissionsDao.getMerchantFullPermissionsList(offset, size);
        } catch (DataAccessException dae) {
            throw translateException(dae);
        } catch (Exception e) {
            throw new ServiceException(ServiceException.PROCESSING_FAILED, e.getMessage(), e);
        }
	}
}
