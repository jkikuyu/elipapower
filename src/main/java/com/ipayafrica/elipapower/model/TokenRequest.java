package com.ipayafrica.elipapower.model;

/**
*
* @author Jude Kikuyu
* created on 18/04/2018
* 
*/
import java.io.Serializable;
import java.util.Collection;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

@Entity
@Table(name = "TOKENREQUEST")
public class TokenRequest extends Token implements Serializable{


	private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "REQUESTID")
    private Integer requestid;
    @Column(name = "METERNO", nullable=false, length=100)
    private String meterno;
    @Column(name = "SEQNUM", nullable=false)
    private Integer seqnum;
    @Column(name = "AMOUNT", precision=8, scale=2)
    private Double amt;

    @Column(name = "REQUESTXML",  nullable=false, length=1000)
    private String requestxml;
    @Column(name = "TYPE", nullable=false)
    private Byte type;
    @Column(name = "REF", precision=12)
    private Double ref;

    @Column(name="REQUESTEDBY")
    private Long requestedby;
    
    @Column(name="STATUS")
    private Byte status;
    
    @Column (name = "CLIENTREF", length = 50)
    private String clientref;

    
	@Column(name = "REQUESTDATE",  nullable=false)
    @Temporal(TemporalType.TIMESTAMP)
	
	private Date requestdate;
	
    @Column(name = "oref",nullable=false, length=50)
    private Double oref;

    @Column(name="RECEIPT")
    private Byte receipt = 0; //0 not printed 1 printed 2:No receipt

	
	@Transient
    private String amount;
	
	@Transient
    private Integer repcount = 0;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "tokenrequest")
	  private Collection<TokenResponse> tokenresponses;

	public TokenRequest() {
		
	}
	public Integer getRequestid() {
		return requestid;
	}


	public String getMeterno() {
		return meterno;
	}


	public Integer getSeqnum() {
		return seqnum;
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


	public void setRequestid(Integer requestid) {
		this.requestid = requestid;
	}


	public void setMeterno(String meterno) {
		this.meterno = meterno;
	}


	public void setSeqnum(Integer seqnum) {
		this.seqnum = seqnum;
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
    
	public Byte getStatus() {
		return status;
	}

	public void setRef(Double ref) {
		this.ref = ref;
	}
	public Double getAmt() {
		return amt;
	}
	public void setAmt(Double amt) {
		this.amt = amt;
	}
	public Double getOref() {
		return oref;
	}
	public void setOref(Double oref) {
		this.oref = oref;
	}
	public void setStatus(Byte status) {
		this.status = status;
	}
	public Integer getRepcount() {
		return repcount;
	}
	public void setRepcount(Integer repcount) {
		this.repcount = repcount;
	}
	public String getClientref() {
		return clientref;
	}
	public void setClientref(String clientref) {
		this.clientref = clientref;
	}
	public Byte getReceipt() {
		return receipt;
	}
	public void setReceipt(Byte receipt) {
		this.receipt = receipt;
	}
	public Collection<TokenResponse> getTokenresponses() {
		return tokenresponses;
	}
	public void setTokenresponses(Collection<TokenResponse> tokenresponses) {
		this.tokenresponses = tokenresponses;
	}



}
