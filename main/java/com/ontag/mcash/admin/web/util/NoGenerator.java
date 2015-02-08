package com.ontag.mcash.admin.web.util;

import java.util.Random;
import java.util.UUID;

/**
 *
 * @author Kasun
 */
public class NoGenerator {

	public static String generateTransactionPin() {
        StringBuilder pinNo = new StringBuilder(6);
        Random rand = new Random(System.nanoTime());
        for (int i = 0; i < 10; i++) {
            int digit = rand.nextInt(10);
            pinNo.append(Integer.toString(digit));
        }
     
        return pinNo.toString();
    }
	
	public static String generatePin() {
        StringBuilder pinNo = new StringBuilder(10);
        Random rand = new Random(System.nanoTime());
        for (int i = 0; i < 10; i++) {
            int digit = rand.nextInt(10);
            pinNo.append(Integer.toString(digit));
        }
     
        return pinNo.toString();
    }

    public static String generateSerialNO() {
        StringBuilder serialNo = new StringBuilder(10);
        Random rand = new Random(System.nanoTime());
        for (int i = 0; i < 10; i++) {
            int digit = rand.nextInt(10);
            serialNo.append(Integer.toString(digit));
        }
       
        return serialNo.toString();
    }

    public static String generateVerificationNo() {
        final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        Random charaterPos = new Random(System.nanoTime());

        StringBuilder verificationNo = new StringBuilder(6);
        for (int i = 0; i < 6; i++) {
            verificationNo.append(CHARACTERS.charAt(charaterPos.nextInt(CHARACTERS.length())));
        }
        
        return verificationNo.toString();
    }
    
    public static void main(String args[]){
    	System.out.println(generateSerialNO());
    }
}