package com.ipayafrica.elipapower.model;

/**
*
* @author Jude Kikuyu
* created on 18/04/2018
* 
*/
import java.io.Serializable;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "TOKENREQUEST")
@SequenceGenerator(allocationSize=1,name="sequence", sequenceName="TOKENREQUEST_FCSEQ")

public class TokenRequest implements Serializable{


	private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @GeneratedValue(generator="sequence",strategy=GenerationType.SEQUENCE)
    @Column(name = "REQUESTID")
    private Long requestid;
    @Column(name = "METERNO")
    private Long meterno;
    @Column(name = "SEQNUM")
    private String seqnum;
    @Column(name = "AMOUNT")
    private Double amount;

    @Column(name = "REQUESTXML")
    private String requestxml;
    @Column(name = "TYPE")
    private String type;
    @Column(name = "REF")
    private Long ref;

    @Column(name="REQUESTEDBY")
    private Long requestedby;
    
	@Column(name = "REQUESTDATE")
    @Temporal(TemporalType.TIMESTAMP)
	
    private Date requestdate;
    @Column(name = "OSYSDATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date osysdate;

	public TokenRequest() {
		
	}


	public Long getRequestid() {
		return requestid;
	}


	public Long getMeterno() {
		return meterno;
	}


	public String getSeqnum() {
		return seqnum;
	}


	public Double getAmount() {
		return amount;
	}


	public String getRequestxml() {
		return requestxml;
	}


	public String getType() {
		return type;
	}


	public Long getRequestedby() {
		return requestedby;
	}


	public Date getRequestdate() {
		return requestdate;
	}


	public Date getOsysdate() {
		return osysdate;
	}


	public void setRequestid(Long requestid) {
		this.requestid = requestid;
	}


	public void setMeterno(Long meterno) {
		this.meterno = meterno;
	}


	public void setSeqnum(String seqnum) {
		this.seqnum = seqnum;
	}


	public void setAmount(Double amount) {
		this.amount = amount;
	}


	public void setRequestxml(String requestxml) {
		this.requestxml = requestxml;
	}


	public void setType(String type) {
		this.type = type;
	}


	public void setRequestedby(Long requestedby) {
		this.requestedby = requestedby;
	}


	public void setRequestdate(Date requestdate) {
		this.requestdate = requestdate;
	}


	public void setOsysdate(Date osysdate) {
		this.osysdate = osysdate;
	}
    public Long getRef() {
		return ref;
	}


	public void setRef(Long ref) {
		this.ref = ref;
	}




}
