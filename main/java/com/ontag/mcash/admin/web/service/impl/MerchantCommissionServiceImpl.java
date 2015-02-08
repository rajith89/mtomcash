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
import com.ontag.mcash.admin.web.service.MerchantCommissionService;
import com.ontag.mcash.dal.dao.MerchantCommissionDao;
import com.ontag.mcash.dal.domain.MerchantCommision;
import com.ontag.mcash.dal.exception.DataAccessException;

@Service
@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
public class MerchantCommissionServiceImpl extends GenericServiceImpl<MerchantCommision> implements MerchantCommissionService{

	
	protected static org.slf4j.Logger logger = LoggerFactory.getLogger(MerchantCommissionServiceImpl.class);
	
	@Autowired
    private MerchantCommissionDao merchantCommissionDao;
	
	@PostConstruct
    void init() {
        super.init(MerchantCommision.class, merchantCommissionDao);
    }
	
	@Override
	public List<MerchantCommision> getAllMerchantCommision(int offset, int size)throws ServiceException{
		 try {
			 	return merchantCommissionDao.getAllMerchantCommision(offset, size);
			} catch (DataAccessException dae) {
	            throw translateException(dae);
	        } catch (Exception e) {
	            throw new ServiceException(ServiceException.PROCESSING_FAILED, e.getMessage(), e);
	        }
	}
	
	@Override
	public MerchantCommision findById(int id)throws ServiceException{
		try {
		 	return merchantCommissionDao.findById(id);
		} catch (DataAccessException dae) {
            throw translateException(dae);
        } catch (Exception e) {
            throw new ServiceException(ServiceException.PROCESSING_FAILED, e.getMessage(), e);
        }
	}
	
	public MerchantCommision findByMerchantAndAmountSlab(int merchantId, int amountSlab)throws ServiceException{
		try {
		 	return merchantCommissionDao.findByMerchantAndAmountSlab(merchantId, amountSlab);
		} catch (DataAccessException dae) {
            throw translateException(dae);
        } catch (Exception e) {
            throw new ServiceException(ServiceException.PROCESSING_FAILED, e.getMessage(), e);
        }
	}
	
	@Override
	@Transactional(readOnly = false)
	public void addMerchantCommision(MerchantCommision merchantCommision)throws ServiceException{
		try {
			  merchantCommissionDao.deactivateMerchantCommision(merchantCommision);
		 	  merchantCommissionDao.add(merchantCommision);
		 	  
		} catch (DataAccessException dae) {
            throw translateException(dae);
        } catch (Exception e) {
            throw new ServiceException(ServiceException.PROCESSING_FAILED, e.getMessage(), e);
        }
	}

}
