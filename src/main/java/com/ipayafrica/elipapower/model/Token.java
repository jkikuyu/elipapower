package com.ipayafrica.elipapower.model;

public class Token {
	private String refNo;
	private String meterno;
	private String amount;
	public String getRefNo() {
		return refNo;
	}
	public String getAmount() {
		return amount;
	}
	public void setRefNo(String refNo) {
		this.refNo = refNo;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
	public String getMeterno() {
		return meterno;
	}
	public void setMeterno(String meterno) {
		this.meterno = meterno;
	}
}
