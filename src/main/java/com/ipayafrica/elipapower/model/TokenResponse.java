package com.ipayafrica.elipapower.model;
/**
*
* @author Jude Kikuyu
* created on 18/04/2018
* 

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
@Table(name = "TOKENRESPONSE")
@SequenceGenerator(allocationSize=1,name="sequence", sequenceName="TOKENRESPONSE_FCSEQ")


public class TokenResponse implements Serializable{
	private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @GeneratedValue(generator="sequence",strategy=GenerationType.SEQUENCE)
    @Column(name = "REPONSEID")
    private Long responseid;
    @Column(name = "METERNO")
    private Long meterno;

    @Column(name = "RESPONSEXML")
    private String responsetxml;
    @Column(name = "TYPE")
    private Byte type;

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

	public Long getMeterno() {
		return meterno;
	}

	public String getResponsetxml() {
		return responsetxml;
	}

	public Byte getType() {
		return type;
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

	public void setMeterno(Long meterno) {
		this.meterno = meterno;
	}

	public void setResponsetxml(String responsetxml) {
		this.responsetxml = responsetxml;
	}

	public void setType(Byte type) {
		this.type = type;
	}

	public void setResponsedate(Date responsedate) {
		this.responsedate = responsedate;
	}

	public void setOsysdate(Date osysdate) {
		this.osysdate = osysdate;
	}
	

}*/
