package com.ontag.mcash.admin.web.service;

import java.util.List;

import com.ontag.mcash.admin.web.exception.ServiceException;
import com.ontag.mcash.dal.domain.BoUsers;
import com.ontag.mcash.dal.domain.CashCard;
import com.ontag.mcash.dal.domain.CashCardBatch;


public interface CardService extends GenericService<CashCard>{
	
	public List<Object[]> getPendingBatches(int offset, int size) throws ServiceException;	
	public List<Object[]> getSentBatches(int offset, int size) throws ServiceException;
	public void generateCards(CashCardBatch cashCardBatch, int noofcard) throws ServiceException ;
	public void activateCardBatch(Integer[] ids, BoUsers userId) throws ServiceException ;
	public void emailCardBatch(Integer[] ids, long userId) throws ServiceException;
	public void deleteCardBatch(Integer[] ids, long userId) throws ServiceException;
	public CashCard getCashCardById(int id) throws ServiceException;
}
