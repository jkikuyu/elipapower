package com.ipayafrica.elipapower.webapp.controller;
/**
 * 
 * @author Jude Kikuyu
 * The vendcontroller receives a request for electricity token. Details received are the 
 * meter no, amount and unique reference. 
 * An XML document is created as per the specification (Merchant to Ipay Interface - Prepaid
 * Electricity Version: 2.44b
 * <ipayMsg client='IPAYAFRICA' term='00001' seqNum='0' time='".$objDateTime->
 * format('Y-m-d H:i:s O')."'><elecMsg ver='2.44'><vendReq>
 * <ref>136105500001</ref><amt cur='KES'>100</amt><numTokens>1</numTokens>
 * <meter>A12C3456789</meter><payType>cash</payType></vendReq ></elecMsg></ipayMsg>"
	
 * The XML is passed to the token request component.
 */
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ipayafrica.elipapower.util.CreateXML;
import com.ipayafrica.elipapower.util.TokenRequest;

@RestController
public class VendController {
    protected final transient Log log = LogFactory.getLog(getClass());

    private CreateXML createxml;
    
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

	String meterNo = "A12C3456789";
	String amount = "100";

    
	byte[] reqXML= createxml.buildXML( meterNo, amount );
	tokenRequest.makeRequest(reqXML);
	}

}

