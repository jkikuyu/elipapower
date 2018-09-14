package com.ipayafrica.elipapower.model;
/**
*
* @author Jude Kikuyu
* created on 18/04/2018
* 
*/
import java.io.Serializable;
import java.util.Collection;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
@Entity
@Table(name = "ERRORCODE")
public class ErrorCode implements Serializable{


    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "ERRORCODEID")
    private Integer errorcodeid;
    
   
    @Column(name = "MESSAGECODE", nullable=false, length=20)
    private String messagecode;

    @Column(name = "DESCRIPTION" , nullable=false)
    private String description;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "errorcode")
    private Collection<TokenResponse> tokenresponses;

    
	public ErrorCode() {
		// TODO Auto-generated constructor stub
	}


	public Integer getErrorcodeid() {
		return errorcodeid;
	}


	public String getMessagecode() {
		return messagecode;
	}


	public String getDescription() {
		return description;
	}


	public void setErrorcodeid(Integer errorcodeid) {
		this.errorcodeid = errorcodeid;
	}


	public void setMessagecode(String messagecode) {
		this.messagecode = messagecode;
	}


	public void setDescription(String description) {
		this.description = description;
	}


	public Collection<TokenResponse> getTokenresponses() {
		return tokenresponses;
	}


	public void setTokenresponses(Collection<TokenResponse> tokenresponses) {
		this.tokenresponses = tokenresponses;
	}
	

}