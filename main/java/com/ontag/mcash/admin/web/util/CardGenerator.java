package com.ontag.mcash.admin.web.util;

import java.util.LinkedList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.ontag.mcash.dal.domain.CashCard;
import com.ontag.mcash.dal.domain.CashCardBatch;

/**
 *
 * @author Kasun
 */

@Component
public class CardGenerator {

    public List<CashCard> GenerateCards(int noOfCards, int value, CashCardBatch offCardBatch, long lastserialNo) {

        List<CashCard> cardList = new LinkedList<>();
        
        CashCard tempCard;

        String pinNo;
        String serialNo;
        String verificationNo;
        long searialno = lastserialNo;

        for (int i = 0; i < noOfCards; i++) {

        	searialno++;
            pinNo = NoGenerator.generatePin();
            serialNo = String.format("%010d", searialno);
            verificationNo = NoGenerator.generateVerificationNo();
            
//            while (CheckDB.PinNoExist(pinNo)) {
//                pinNo = NoGenerator.generatePin();
//            }
//            
//            while (CheckDB.SerialNoExist(serialNo)) {
//                serialNo = NoGenerator.generateSerialNO();
//            }
//            
//            while (CheckDB.VerificationNoExist(verificationNo)) {
//                serialNo = NoGenerator.generateSerialNO();
//            }

            tempCard = new CashCard(pinNo, serialNo, verificationNo,value,offCardBatch,offCardBatch.getMerchantId(), offCardBatch.getCurrencyCode());
            cardList.add(tempCard);
        }
        
        return cardList;
    }
}
