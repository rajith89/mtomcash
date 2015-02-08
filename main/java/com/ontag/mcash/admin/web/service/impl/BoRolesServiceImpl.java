package com.ontag.mcash.admin.web.service.impl;

import java.util.List;

import javax.annotation.PostConstruct;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.ontag.mcash.admin.web.exception.ServiceException;
import com.ontag.mcash.admin.web.service.BoRolesService;
import com.ontag.mcash.dal.dao.BoRolesDao;
import com.ontag.mcash.dal.dao.CurrencyDao;
import com.ontag.mcash.dal.domain.BoRoles;
import com.ontag.mcash.dal.domain.CurrencyCode;
import com.ontag.mcash.dal.exception.DataAccessException;

@Service
@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
public class BoRolesServiceImpl extends GenericServiceImpl<BoRoles> implements BoRolesService{
	
protected static org.slf4j.Logger logger = LoggerFactory.getLogger(BoRolesServiceImpl.class);
	
	@Autowired
    private BoRolesDao boRolesDao;
	
	@PostConstruct
    void init() {
        super.init(BoRoles.class, boRolesDao);
    }

	@Override
	public List<BoRoles> getAllBoRoles(int offset, int size)
			throws ServiceException {
		try {
		 	logger.debug("BoRolesServiceImpl getAllBoRoles #############");
		 	return boRolesDao.getAllBoRoles(offset, size);
		} catch (DataAccessException dae) {
            throw translateException(dae);
        } catch (Exception e) {
            throw new ServiceException(ServiceException.PROCESSING_FAILED, e.getMessage(), e);
        }
	}

	@Override
	public BoRoles findById(int id) throws ServiceException {
		try {
		 	logger.debug("BoRolesServiceImpl findById #############");
		 	return boRolesDao.findById(id);
		} catch (DataAccessException dae) {
            throw translateException(dae);
        } catch (Exception e) {
            throw new ServiceException(ServiceException.PROCESSING_FAILED, e.getMessage(), e);
        }
	}

}
