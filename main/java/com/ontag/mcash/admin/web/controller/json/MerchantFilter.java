package com.ontag.mcash.admin.web.controller.json;

import java.io.Serializable;

public class MerchantFilter implements Serializable{
	private int merchantId;
	private double credit;
	private String note;
	
	public int getMerchantId() {
		return merchantId;
	}
	public void setMerchantId(int merchantId) {
		this.merchantId = merchantId;
	}
	public double getCredit() {
		return credit;
	}
	public void setCredit(double credit) {
		this.credit = credit;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
}
