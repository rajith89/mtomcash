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
import com.ontag.mcash.admin.web.service.CorporateBankService;
import com.ontag.mcash.dal.dao.CorporateBankDao;
import com.ontag.mcash.dal.domain.CorporateBank;
import com.ontag.mcash.dal.exception.DataAccessException;

@Service
@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
public class CorporateBankServiceImpl extends GenericServiceImpl<CorporateBank> implements CorporateBankService{

	
	protected static org.slf4j.Logger logger = LoggerFactory.getLogger(CorporateBankServiceImpl.class);
	
	@Autowired
    private CorporateBankDao corporateBankDao;
	
	@PostConstruct
    void init() {
        super.init(CorporateBank.class, corporateBankDao);
    }
	
	@Override
	public List<CorporateBank> getAllCorporateBank(int offset, int size)throws ServiceException{
		 try {
			 	return corporateBankDao.getAllCorporateBank(offset, size);
			} catch (DataAccessException dae) {
	            throw translateException(dae);
	        } catch (Exception e) {
	            throw new ServiceException(ServiceException.PROCESSING_FAILED, e.getMessage(), e);
	        }
	}
	
	public CorporateBank findById(int id)throws ServiceException{
		try {
		 	return corporateBankDao.findById(id);
		} catch (DataAccessException dae) {
            throw translateException(dae);
        } catch (Exception e) {
            throw new ServiceException(ServiceException.PROCESSING_FAILED, e.getMessage(), e);
        }
	}
	
	@Override
	@Transactional(readOnly = false)
	public void activateBanks(Short[] ids) throws ServiceException{
		try {
			corporateBankDao.activateBanks(ids);
		} catch (DataAccessException dae) {
            throw translateException(dae);
        } catch (Exception e) {
            throw new ServiceException(ServiceException.PROCESSING_FAILED, e.getMessage(), e);
        }
	}
	
	@Override
	@Transactional(readOnly = false)
	public void deactivateBanks(Short[] ids) throws ServiceException{
		try {
			System.out.println("Activate service");
			corporateBankDao.deactivateBanks(ids);
		} catch (DataAccessException dae) {
            throw translateException(dae);
        } catch (Exception e) {
            throw new ServiceException(ServiceException.PROCESSING_FAILED, e.getMessage(), e);
        }
	}
		
}
