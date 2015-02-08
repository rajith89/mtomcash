package com.ontag.mcash.admin.web.service.impl;

import javax.annotation.PostConstruct;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.ontag.mcash.admin.web.exception.ServiceException;
import com.ontag.mcash.admin.web.service.CompanyCommissionService;
import com.ontag.mcash.dal.dao.CompanyCommissionDao;
import com.ontag.mcash.dal.domain.AmountSlab;
import com.ontag.mcash.dal.domain.BankCharges;
import com.ontag.mcash.dal.domain.CompanyCommision;
import com.ontag.mcash.dal.domain.Merchant;
import com.ontag.mcash.dal.exception.DataAccessException;

@Service
@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
public class CompanyCommissionServiceImpl extends
		GenericServiceImpl<CompanyCommision> implements
		CompanyCommissionService {

	protected static org.slf4j.Logger logger = LoggerFactory
			.getLogger(CompanyCommissionServiceImpl.class);

	@Autowired
	CompanyCommissionDao companyCommissionDao;

	@PostConstruct
	void init() {
		super.init(CompanyCommision.class, companyCommissionDao);
	}

	@Override
	@Transactional(readOnly = false)
	public void setInactive(Merchant merchant, AmountSlab amountSlabId)
			throws ServiceException {
		try {
			logger.debug("CompanyCommissionServiceImpl setInactive #############");
			companyCommissionDao.setInactive(merchant, amountSlabId);
		} catch (DataAccessException dae) {
			throw translateException(dae);
		} catch (Exception e) {
			throw new ServiceException(ServiceException.PROCESSING_FAILED,
					e.getMessage(), e);
		}

	}

}
