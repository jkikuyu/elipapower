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
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "TOKENREQUEST")
public class TokenRequest implements Serializable{


	private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "REQUESTID")
    private Long requestid;
    @Column(name = "METERNO", nullable=false, length=100)
    private String meterno;
    @Column(name = "SEQNUM", nullable=false)
    private Integer seqnum;
    @Column(name = "AMOUNT", precision=8, scale=2)
    private Double amount;

    @Column(name = "REQUESTXML",  nullable=false, length=1000)
    private String requestxml;
    @Column(name = "TYPE", nullable=false)
    private Byte type;
    @Column(name = "REF", precision=12)
    private Double ref;

    @Column(name="REQUESTEDBY")
    private Long requestedby;
    
    
	@Column(name = "REQUESTDATE",  nullable=false)
    @Temporal(TemporalType.TIMESTAMP)
	
    private Date requestdate;

	public TokenRequest() {
		
	}
	public Long getRequestid() {
		return requestid;
	}


	public String getMeterno() {
		return meterno;
	}


	public Integer getSeqnum() {
		return seqnum;
	}


	public Double getAmount() {
		return amount;
	}


	public String getRequestxml() {
		return requestxml;
	}


	public Byte getType() {
		return type;
	}


	public Long getRequestedby() {
		return requestedby;
	}


	public Date getRequestdate() {
		return requestdate;
	}


	public void setRequestid(Long requestid) {
		this.requestid = requestid;
	}


	public void setMeterno(String meterno) {
		this.meterno = meterno;
	}


	public void setSeqnum(Integer seqnum) {
		this.seqnum = seqnum;
	}


	public void setAmount(Double amount) {
		this.amount = amount;
	}


	public void setRequestxml(String requestxml) {
		this.requestxml = requestxml;
	}


	public void setType(Byte type) {
		this.type = type;
	}


	public void setRequestedby(Long requestedby) {
		this.requestedby = requestedby;
	}


	public void setRequestdate(Date requestdate) {
		this.requestdate = requestdate;
	}


    public Double getRef() {
		return ref;
	}
    

	public void setRef(Double ref) {
		this.ref = ref;
	}




}
