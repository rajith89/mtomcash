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
import com.ontag.mcash.admin.web.service.AmountSlabService;
import com.ontag.mcash.admin.web.service.CorporateBankService;
import com.ontag.mcash.dal.dao.AmountSlabDao;
import com.ontag.mcash.dal.dao.CorporateBankDao;
import com.ontag.mcash.dal.domain.AmountSlab;
import com.ontag.mcash.dal.domain.CorporateBank;
import com.ontag.mcash.dal.exception.DataAccessException;

@Service
@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
public class AmountSlabServiceImpl extends GenericServiceImpl<AmountSlab> implements AmountSlabService{

	
	protected static org.slf4j.Logger logger = LoggerFactory.getLogger(AmountSlabServiceImpl.class);
	
	@Autowired
    private AmountSlabDao amountSlabDao;
	
	@PostConstruct
    void init() {
        super.init(AmountSlab.class, amountSlabDao);
    }
	
	@Override
	public List<AmountSlab> getAllAmountSlab(int offset, int size)throws ServiceException{
		 try {
			 	return amountSlabDao.getAllAmountSlab(offset, size);
			} catch (DataAccessException dae) {
	            throw translateException(dae);
	        } catch (Exception e) {
	            throw new ServiceException(ServiceException.PROCESSING_FAILED, e.getMessage(), e);
	        }
	}
	
	public AmountSlab findById(int id)throws ServiceException{
		try {
		 	return amountSlabDao.findById(id);
		} catch (DataAccessException dae) {
            throw translateException(dae);
        } catch (Exception e) {
            throw new ServiceException(ServiceException.PROCESSING_FAILED, e.getMessage(), e);
        }
	}
		
}
