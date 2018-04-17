package com.ipayafrica.elipapower.webapp.controller;
/**
 * 
 * @author Jude Kikuyu
 * The vendcontroller receives a request for electricity token. Details received are the 
 * meter no, amount and unique reference. 
 * An XML document is created as per the specification (Merchant to Ipay Interface - Prepaid
	Electricity Version: 2.44b
 * The XML is passed to the token request component.
 */
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ipayafrica.elipapower.util.CreateXML;
import com.ipayafrica.elipapower.util.TokenRequest;

@RestController
public class VendController {
    protected final transient Log log = LogFactory.getLog(getClass());
    private byte[] reqXML;
    private CreateXML createxml;
    
    @Autowired
    private Environment env;

	@Autowired
    private TokenRequest tokenRequest;
   
	public VendController() {
	}
	public TokenRequest getTokenRequest() {
		return tokenRequest;
	}

	public CreateXML getCreatexml() {
		return createxml;
	}
	@Autowired
	public void setCreatexml(CreateXML createxml) {
		this.createxml = createxml;
	}

	@Autowired
	public void setTokenRequest(TokenRequest tokenRequest) {
		this.tokenRequest = tokenRequest;
	}
	
	@RequestMapping("/tokenreq")
	public void getElectricity(){
	String client = env.getProperty("company.name");
	String term = env.getProperty("company.id");
	String seqNo = "1";
	String refNo = "136105500001";
	String currCode = "KES";
	String amount = "100";
	String num = "1";
	String meterNo = "A12C3456789";
	String type = "cash";
	reqXML = createxml.buildXML(client, seqNo, term, refNo, currCode, amount, num, meterNo, type);
	tokenRequest.makeRequest(reqXML);
	}

}

