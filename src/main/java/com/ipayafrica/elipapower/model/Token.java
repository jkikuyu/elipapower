package com.ipayafrica.elipapower.model;

public class Token {
	private String refno;
	private String meterno;
	private String amount;
	private String demo;
	public String getAmount() {
		return amount;
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
	public String getRefno() {
		return refno;
	}
	public void setRefno(String refno) {
		this.refno = refno;
	}
	public String getDemo() {
		return demo;
	}
	public void setDemo(String demo) {
		this.demo = demo;
	}
}
