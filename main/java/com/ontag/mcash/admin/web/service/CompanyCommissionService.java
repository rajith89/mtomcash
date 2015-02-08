package com.ontag.mcash.admin.web.service;

import com.ontag.mcash.admin.web.exception.ServiceException;
import com.ontag.mcash.dal.domain.AmountSlab;
import com.ontag.mcash.dal.domain.CompanyCommision;
import com.ontag.mcash.dal.domain.Merchant;

public interface CompanyCommissionService extends
		GenericService<CompanyCommision> {

	public void setInactive(Merchant merchant, AmountSlab amountSlabId)
			throws ServiceException;

}
