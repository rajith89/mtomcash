package com.ontag.mcash.admin.web.service.impl;

import java.text.NumberFormat;
import java.util.Date;
import java.util.Formatter;
import java.util.List;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.ontag.mcash.admin.web.exception.ServiceException;
import com.ontag.mcash.admin.web.service.CardService;
import com.ontag.mcash.admin.web.util.CardGenerator;
import com.ontag.mcash.admin.web.util.Constants;
import com.ontag.mcash.admin.web.util.MailHandler;
import com.ontag.mcash.dal.dao.CashCardDao;
import com.ontag.mcash.dal.dao.MerchantDao;
import com.ontag.mcash.dal.dao.MerchantTransactionDao;
import com.ontag.mcash.dal.domain.BoUsers;
import com.ontag.mcash.dal.domain.CashCard;
import com.ontag.mcash.dal.domain.CashCardBatch;
import com.ontag.mcash.dal.domain.Merchant;
import com.ontag.mcash.dal.domain.MerchantTransaction;
import com.ontag.mcash.dal.exception.DataAccessException;


@Service
@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
public class CardServiceImpl extends GenericServiceImpl<CashCard> implements CardService{

	
	protected static org.slf4j.Logger logger = LoggerFactory.getLogger(CardServiceImpl.class);
	
	@Autowired
    private CashCardDao cardDao;
	
	@Autowired
    private CardGenerator cardGenerator;
	
	@Autowired
    private MailHandler mailHandler;
	
	@Autowired
    private MerchantDao merchantDao;
	
	@Autowired
    private MerchantTransactionDao merchantTransactionDao;

	
	@Override
	public List<Object[]> getPendingBatches(int offset, int size) throws ServiceException{
		 try {
			 	return cardDao.getPendingBatches(offset, size);
			} catch (DataAccessException dae) {
	            throw translateException(dae);
	        } catch (Exception e) {
	            throw new ServiceException(ServiceException.PROCESSING_FAILED, e.getMessage(), e);
	        }
	}
		
	@Override
	public List<Object[]> getSentBatches(int offset, int size) throws ServiceException{
		try {
		 	return cardDao.getSentBatches(offset, size);
		} catch (DataAccessException dae) {
            throw translateException(dae);
        } catch (Exception e) {
            throw new ServiceException(ServiceException.PROCESSING_FAILED, e.getMessage(), e);
        }
	}
	
	@Transactional(readOnly = false)
	@Override
	public void activateCardBatch(Integer[] ids, BoUsers userId) throws ServiceException {
		try {
						
		 	cardDao.activateCardBatch(ids, userId.getId());
		 	cardDao.activateCards(ids,  userId.getId());
		 			 	
		} catch (DataAccessException dae) {
            throw translateException(dae);
        } catch (Exception e) {
            throw new ServiceException(ServiceException.PROCESSING_FAILED, e.getMessage(), e);
        }
	}
	
	@Transactional(readOnly = false)
	@Override
	public void emailCardBatch(Integer[] ids, long userId) throws ServiceException {
		try {
			 	for(int id:ids){
			 		CashCardBatch batch = cardDao.getCardBatchById(id);
			 		Merchant merchant = batch.getMerchantId();  //merchantDao.findById(batch.getMerchantId()); 
			 		List<CashCard> cardList = cardDao.getCardByBatchId(id);
			 		
			 		//TODO implement email issue.
			 		emailCards(batch,cardList,merchant);
			 	}
			 
			 cardDao.emailCardBatch(ids, userId);
		} catch (DataAccessException dae) {
            throw translateException(dae);
        } catch (Exception e) {
            throw new ServiceException(ServiceException.PROCESSING_FAILED, e.getMessage(), e);
        }
	}
	
	@Transactional(readOnly = false)
	@Override
	public void deleteCardBatch(Integer[] ids, long userId) throws ServiceException {
		try {
				List<CashCard> cardList;
				double cardTotal=0;
				double newbalance=0;
				int noofcard = 0;
				Merchant merchant = null;
				CashCardBatch cashCardBatch = null;
				
				
				for(int id : ids){
					noofcard = 0;
					cardList = cardDao.getCardByBatchId(id);
					cashCardBatch = cardDao.getCardBatchById(id);
					
					for(CashCard cashCard:cardList){
						merchant = cashCard.getMerchantId();
						cardTotal = cardTotal + cashCard.getValue();
						noofcard++;
					}
					newbalance = merchant.getBalance() + cardTotal;
					merchant.setBalance(merchant.getBalance() + cardTotal);
					merchantDao.modify(merchant);
					
					MerchantTransaction merchantTransaction = new MerchantTransaction();
					merchantTransaction.setBalance(newbalance);
					merchantTransaction.setCrdr(MerchantTransaction.TRAN_CR);
					merchantTransaction.setDateCreated(new Date());
					merchantTransaction.setDescription("Cash Card deletion, Card Value : " + cashCardBatch.getValue() + " No of Cards : " + noofcard);
					merchantTransaction.setMerchantId(merchant);
					merchantTransaction.setTranType(MerchantTransaction.TRAN_MERCHANR_CASH_CARD_DELETION);
					merchantTransaction.setTrnxAmount(cardTotal);
					merchantTransactionDao.add(merchantTransaction);
					
				}
			
			 	cardDao.deleteCardBatch(ids, userId);
			 	
			 	
			 	
			 	
		} catch (DataAccessException dae) {
            throw translateException(dae);
        } catch (Exception e) {
            throw new ServiceException(ServiceException.PROCESSING_FAILED, e.getMessage(), e);
        }
	}
	
	@Transactional(readOnly = false)
	@Override
	public void generateCards(CashCardBatch cardBatch, int noofcard) throws ServiceException{
		//Transaction serial no is a auto generated sequence no		
		try {
	
			long lastOffCardId = cardDao.getLastOffCash();
			
			Merchant merchant = cardBatch.getMerchantId();
			
			double newbalance = merchant.getBalance() - (cardBatch.getValue() * noofcard);
			
			merchant.setBalance(newbalance);
			merchantDao.modify(merchant);
			
			MerchantTransaction merchantTransaction = new MerchantTransaction();
			merchantTransaction.setBalance(newbalance);
			merchantTransaction.setCrdr(MerchantTransaction.TRAN_DR);
			merchantTransaction.setDateCreated(new Date());
			merchantTransaction.setDescription("Cash Card creation, Card Value : " + cardBatch.getValue() + " No of Cards : " + noofcard);
			merchantTransaction.setMerchantId(merchant);
			merchantTransaction.setTranType(MerchantTransaction.TRAN_MERCHANR_CASH_CARD_CREATION);
			merchantTransaction.setTrnxAmount(cardBatch.getValue() * noofcard);
			merchantTransactionDao.add(merchantTransaction);
			
			List<CashCard> offCardList = cardGenerator.GenerateCards(noofcard,cardBatch.getValue(),cardBatch,lastOffCardId);
			 for(CashCard offCard : offCardList)
			   cardDao.addCard(offCard);
			
		} catch (DataAccessException dae) {
            throw translateException(dae);
        } catch (Exception e) {
            throw new ServiceException(ServiceException.PROCESSING_FAILED, e.getMessage(), e);
        }
	}
	
	public CashCard getCashCardById(int id) throws ServiceException{
		try {
		 	return cardDao.getCashCardById(id);
		} catch (DataAccessException dae) {
            throw translateException(dae);
        } catch (Exception e) {
            throw new ServiceException(ServiceException.PROCESSING_FAILED, e.getMessage(), e);
        }	
	}
	
	private String emailCards(CashCardBatch batch, List<CashCard> cardList, Merchant merchant) {
    	
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	    String loggedname = auth.getName();
	    
		StringBuffer buf = new StringBuffer();
		buf.append("Dear " + merchant.getFirstName() + ", <br><br>");
		buf.append("<b>Batch No   : " + batch.getId() + "</b><br>");
		buf.append("<b>No of Card : " + cardList.size() + "</b><br>");
		buf.append("<hr> <br>");
		
		
		
		buf.append("<table style='border:1px solid black;border-collapse:collapse;width:800px'>");
		
		buf.append("<tr>");
		buf.append("<th style='border:1px solid black;align:left'>SERIAL NO</th>");
		buf.append("<th style='border:1px solid black;align:left'>PIN NUMBER</th>");
		buf.append("<th style='border:1px solid black;align:left'>VERIFICATION CODE</th>");
		buf.append("<th style='border:1px solid black;align:left'>VALUE(" + batch.getCurrencyCode().getCurrencyCode() + ")</th>");
		buf.append("</tr>");

		for(CashCard card: cardList){
			buf.append("<tr>");
			buf.append("<td style='border:1px solid black;'>" + card.getSerialNo() + "</td>");
			buf.append("<td style='border:1px solid black;'>" + card.getPinNumber() + "</td>");
			buf.append("<td style='border:1px solid black;'>" + card.getVerificationCode() + "</td>");
			buf.append("<td style='border:1px solid black;'>" + card.getValue() + "</td>");
			buf.append("</tr>");
		}	
		
		buf.append("</table>");		
		buf.append("<br><br>");
		
		buf.append("<b><i>Please Note : Card will only activate after you confirm the delivery of this email. So please do an acknowledgement once you get this email.</i></b>");
		buf.append("<br><br>");		
		buf.append(loggedname);
		
	
		
    	try {
//			mailHandler.sendHtmlMail(Constants.ADMIN_EMAIL,    		
//					merchant.getEmail(),
//					Constants.CASHCARD_EMAIL_SUBJECT,
//					buf.toString(),Constants.CASH_CARD_EMAIL); //TODO Uncomment

//			mailHandler.sendHtmlMail(Constants.ADMIN_EMAIL,    		
//					Constants.CASH_CARD_EMAIL,
//					Constants.CASHCARD_EMAIL_SUBJECT,
//					buf.toString(),"");

    		
    		
    		mailHandler.sendHtmlMailNew(Constants.ADMIN_EMAIL,
    				merchant.getEmail(),
					Constants.CASHCARD_EMAIL_SUBJECT,
					buf.toString(),"");
     		
		} catch (Exception e) {
			e.printStackTrace();
		}    	
    	return "reports-menu";
    }
	
}
