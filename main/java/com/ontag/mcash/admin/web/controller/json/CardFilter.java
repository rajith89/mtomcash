package com.ontag.mcash.admin.web.controller.json;


public class CardFilter {

    private int merchantId;
    private int noofcard;
    private int cardvalue;
    private String currencyCode;
    
	public int getMerchantId() {
		return merchantId;
	}
	public void setMerchantId(int merchantId) {
		this.merchantId = merchantId;
	}
	public int getNoofcard() {
		return noofcard;
	}
	public void setNoofcard(int noofcard) {
		this.noofcard = noofcard;
	}
	public int getCardvalue() {
		return cardvalue;
	}
	public void setCardvalue(int cardvalue) {
		this.cardvalue = cardvalue;
	}
	public String getCurrencyCode() {
		return currencyCode;
	}
	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}

    
}
