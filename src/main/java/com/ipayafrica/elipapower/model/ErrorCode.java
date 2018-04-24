package com.ipayafrica.elipapower.model;
/**
*
* @author Jude Kikuyu
* created on 18/04/2018
* 

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
@Entity
@Table(name = "ERRORCODE")
@SequenceGenerator(allocationSize=1,name="sequence", sequenceName="ERRORCODE_FCSEQ")

public class ErrorCode implements Serializable{


    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @GeneratedValue(generator="sequence",strategy=GenerationType.SEQUENCE)
    @Column(name = "ERRORCODEID")
    private Long errorcodeid;
    
   
    @Column(name = "MESSAGECODE")
    private String messagecode;

    @Column(name = "DESCRIPTION")
    private String description;
    
    
	public ErrorCode() {
		// TODO Auto-generated constructor stub
	}


	public Long getErrorcodeid() {
		return errorcodeid;
	}


	public String getMessagecode() {
		return messagecode;
	}


	public String getDescription() {
		return description;
	}


	public void setErrorcodeid(Long errorcodeid) {
		this.errorcodeid = errorcodeid;
	}


	public void setMessagecode(String messagecode) {
		this.messagecode = messagecode;
	}


	public void setDescription(String description) {
		this.description = description;
	}
	

}*/