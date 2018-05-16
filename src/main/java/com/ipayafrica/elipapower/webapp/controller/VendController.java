package com.ipayafrica.elipapower.webapp.controller;
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
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
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
	private boolean isEmptyMeterNo =false, isEmptyAmount=false, isEmptyRef=false;
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

	@RequestMapping(value ="/tokenreq", method=RequestMethod.POST, produces={"application/json"})
	@ResponseBody
	public String getElectricity(@RequestBody Token token){
	
	String messJSON = null;

	ObjectMapper objectMapper = new ObjectMapper();

	String meterNo = token.getMeterno();

	String amount = token.getAmount();
	Double dAmt = Double.parseDouble(amount);
	amount = String.format("%.2f", dAmt);
	String refNo = token.getRefno();
	HashMap<String,Object> messResponse = null;
	log.info("meter no:"+meterNo);
	log.info("ref:"+refNo);

	if(meterNo==null || meterNo.isEmpty()) {
		isEmptyMeterNo = true;

	}
	else if (amount==null || amount.isEmpty()){
		isEmptyAmount =true;
	}
	else if (refNo==null || refNo.isEmpty()) {
		isEmptyRef =true;
	}
	log.info("amount:"+amount);
	messResponse = new HashMap<String,Object>();

	if (!isEmptyMeterNo && !isEmptyAmount && !isEmptyRef) {
		
		tokenRequest = new TokenRequest();
	
		byte[] reqXML= createxml.buildXML( meterNo, amount,tokenRequest );
		iTokenRequestService.save(tokenRequest);
		log.info("meter No:" + meterNo);
	
	
		log.info("begin make request....");
		
		messResponse = requestToken.makeRequest(reqXML,meterNo);
		tokenResponse= responseToken.getTokenResponse();
		tokenResponse.setMeterno(meterNo);
		tokenResponse.setJsonresponse(messJSON);
		iTokenResponseService.save(tokenResponse);
	}
	else {
		String errResponse = null;
		errResponse = isEmptyMeterNo? "Meter No is empty or  key used is wrong. ":"";
		errResponse +=isEmptyRef? "Reference Number is empty or key used is wrong":"";

		errResponse +=isEmptyAmount ? "Amount is empty or key used is wrong":"";
		messResponse.put("error",errResponse);
		messResponse.put("status", "0");
		isEmptyMeterNo =false;
		isEmptyAmount=false;
		isEmptyRef=false;
	}

	try {
		messJSON = objectMapper.writeValueAsString(messResponse);
		log.info("response:"+ messJSON);

		
	} catch (JsonProcessingException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
	
	return messJSON;
	}


}

