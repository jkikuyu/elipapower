package com.ipayafrica.elipapower.webapp.controller;

import java.nio.charset.Charset;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ipayafrica.elipapower.util.CreateXML;
import com.ipayafrica.elipapower.util.TokenRequest;

@RestController
public class VendReqController {
    protected final transient Log log = LogFactory.getLog(getClass());
    private String reqXML;

    private CreateXML createxml;
	@Autowired
    private TokenRequest tokenRequest;
   
	public VendReqController() {
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
		private void getElectricity(){
		
		String client = "IPAFRICA";
		String seqNo = "1";
		String term = "0001";
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

