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
    @Column(name = "METERNO")
    private String meterno;

    @Column(name = "ERRORCODEID")
    private String errorcodeid;
    @Column(name = "REF")
    private Long ref;
    @Column(name = "RESPONSEXML")
    private String responsetxml;

    @Column(name = "RESPONSEDATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date responsedate;
    @Column(name = "OSYSDATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date osysdate;

	public TokenResponse() {
		// TODO Auto-generated constructor stub
	}

	public Long getResponseid() {
		return responseid;
	}

	public String getMeterno() {
		return meterno;
	}

	public String getResponsetxml() {
		return responsetxml;
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

	public void setResponsetxml(String responsetxml) {
		this.responsetxml = responsetxml;
	}

	public void setResponsedate(Date responsedate) {
		this.responsedate = responsedate;
	}



}
