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
@Table(name = "TOKENRESPONSE")

public class TokenResponse implements Serializable{
	private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "REPONSEID")
    private Long responseid;
    @Column(name = "METERNO", length=100)
    private String meterno;

    @Column(name = "ERRORCODEID")
    private  Integer errorcodeid;
    @Column(name = "REF", precision=12)
    private Double ref;
    @Column(name = "RESPONSEXML",length=1000)
    private String responsexml;
    @Column(name = "ORIGXML", length=1000)
    private String origxml;
    @Column(name = "JSONResponse")
    private String jsonresponse;

    @Column(name = "RESPONSEDATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date responsedate;
    @Column(name = "OSYSDATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date osysdate;
    @Column(name="REVERSAL") // 0 Normal vend 2 reversal
    private Byte reversal;
    

	public TokenResponse() {
		// TODO Auto-generated constructor stub
	}

	public Long getResponseid() {
		return responseid;
	}

	public String getMeterno() {
		return meterno;
	}

	public String getResponsexml() {
		return responsexml;
	}


	public Date getResponsedate() {
		return responsedate;
	}

	public Date getOsysdate() {
		return osysdate;
	}

	public void setResponseid(Long responseid) {
		this.responseid = responseid;
	}

	public void setMeterno(String meterno) {
		this.meterno = meterno;
	}

	public void setResponsexml(String responsexml) {
		this.responsexml = responsexml;
	}

	public void setResponsedate(Date responsedate) {
		this.responsedate = responsedate;
	}

	public Integer getErrorcodeid() {
		return errorcodeid;
	}

	public Double getRef() {
		return ref;
	}

	public Byte getReversal() {
		return reversal;
	}

	public void setErrorcodeid(Integer errorcodeid) {
		this.errorcodeid = errorcodeid;
	}

	public void setRef(Double ref) {
		this.ref = ref;
	}

	public void setOsysdate(Date osysdate) {
		this.osysdate = osysdate;
	}

	public void setReversal(Byte reversal) {
		this.reversal = reversal;
	}

	public String getOrigxml() {
		return origxml;
	}

	public void setOrigxml(String origxml) {
		this.origxml = origxml;
	}

	public String getJsonresponse() {
		return jsonresponse;
	}

	public void setJsonresponse(String jsonresponse) {
		this.jsonresponse = jsonresponse;
	}



}
