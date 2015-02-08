package com.ontag.mcash.admin.web.service.impl;

import javax.annotation.PostConstruct;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.ontag.mcash.admin.web.exception.ServiceException;
import com.ontag.mcash.admin.web.service.AdministrationService;
import com.ontag.mcash.dal.dao.SystemStatusDao;
import com.ontag.mcash.dal.domain.SystemStatus;
import com.ontag.mcash.dal.exception.DataAccessException;

@Service
@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
public class AdministrationServiceImpl extends GenericServiceImpl<SystemStatus> implements AdministrationService{

	
	protected static org.slf4j.Logger logger = LoggerFactory.getLogger(AdministrationServiceImpl.class);
	
	@Autowired
    private SystemStatusDao systemStatusDao;
	
	@PostConstruct
    void init() {
        super.init(SystemStatus.class, systemStatusDao);
    }
	
	public SystemStatus getSystemStatus()throws ServiceException{
		try {
		 	return systemStatusDao.getSystemStatus();
		} catch (DataAccessException dae) {
            throw translateException(dae);
        } catch (Exception e) {
            throw new ServiceException(ServiceException.PROCESSING_FAILED, e.getMessage(), e);
        }
	}
		
}
