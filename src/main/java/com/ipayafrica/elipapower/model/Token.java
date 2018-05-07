package com.ipayafrica.elipapower.model;

public class Token {
	private String refNo;
	private String MeterNo;
	private Double amount;
	public String getRefNo() {
		return refNo;
	}
	public String getMeterNo() {
		return MeterNo;
	}
	public Double getAmount() {
		return amount;
	}
	public void setRefNo(String refNo) {
		this.refNo = refNo;
	}
	public void setMeterNo(String meterNo) {
		MeterNo = meterNo;
	}
	public void setAmount(Double amount) {
		this.amount = amount;
	}
}
