package com.ipayafrica.elipapower.webapp.controller;
import java.util.Date;
import java.util.HashMap;

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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ipayafrica.elipapower.model.Token;
import com.ipayafrica.elipapower.model.TokenRequest;
import com.ipayafrica.elipapower.model.TokenResponse;
import com.ipayafrica.elipapower.service.ITokenRequestService;
import com.ipayafrica.elipapower.service.ITokenResponseService;
import com.ipayafrica.elipapower.util.CreateXML;
import com.ipayafrica.elipapower.util.RequestToken;
import com.ipayafrica.elipapower.util.ResponseToken;

@RestController
public class VendController {
    protected final transient Log log = LogFactory.getLog(getClass());

    private CreateXML createxml;
    private TokenRequest tokenRequest = null;
    private TokenResponse tokenResponse = null;
	private ITokenRequestService iTokenRequestService= null;

	@Autowired
    private RequestToken requestToken;
   
	@Autowired
    private ResponseToken responseToken;
	

	
	public VendController() {
	}
	public RequestToken getTokenRequest() {
		return requestToken;
	}

	public CreateXML getCreatexml() {
		return createxml;
	}

	@Autowired
	public void setCreatexml(CreateXML createxml) {
		this.createxml = createxml;
	}

	@Autowired
	public void setTokenRequest(RequestToken tokenRequest) {
		this.requestToken = tokenRequest;
	}
	@Autowired
	public void setiTokenRequestService(ITokenRequestService iTokenRequestService) {
		this.iTokenRequestService = iTokenRequestService;
	}
	@Autowired
	private ITokenResponseService iTokenResponseService;

	@RequestMapping("/tokenreq")
	public String getElectricity(@RequestBody Token token){

	ObjectMapper objectMapper = new ObjectMapper();

	String meterNo = token.getMeterNo();
	Double dAmt = token.getAmount();
	String amount = dAmt.toString();
	log.info("amount:"+amount);

	HashMap<String,Object> messResponse = null;
	tokenRequest = new TokenRequest();

	byte[] reqXML= createxml.buildXML( meterNo, amount,tokenRequest );
	iTokenRequestService.save(tokenRequest);
	tokenResponse= responseToken.getTokenResponse();
	tokenResponse.setMeterno(meterNo);
	tokenResponse.setOsysdate(new Date());

	iTokenResponseService.save(tokenResponse);

	log.info("begin make request....");
	messResponse = new HashMap<String,Object>();
	messResponse = requestToken.makeRequest(reqXML,meterNo);
	String messJSON = null;
	try {
		messJSON = objectMapper.writeValueAsString(messResponse);
	} catch (JsonProcessingException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
	return messJSON;
	}


}

