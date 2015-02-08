package com.ontag.mcash.admin.web.controller.json;

import java.util.Date;


public class MessageFilter {
    private Date fromDate;
    private Date toDate;
    private int userId;
    
	public Date getToDate() {
		return toDate;
	}
	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}
	public Date getFromDate() {
		return fromDate;
	}
	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
    
    
	
    
}
