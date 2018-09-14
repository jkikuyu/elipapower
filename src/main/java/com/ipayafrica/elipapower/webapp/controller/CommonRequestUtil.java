package com.ipayafrica.elipapower.webapp.controller;

import java.util.HashMap;
import java.util.Optional;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ipayafrica.elipapower.model.TokenRequest;
import com.ipayafrica.elipapower.model.TokenResponse;
import com.ipayafrica.elipapower.service.ITokenRequestService;
import com.ipayafrica.elipapower.service.ITokenResponseService;
import com.ipayafrica.elipapower.util.CreateXML;
import com.ipayafrica.elipapower.util.LogFile;
import com.ipayafrica.elipapower.util.RequestToken;
import com.ipayafrica.elipapower.util.ResponseToken;

public class CommonRequestUtil {
    protected final transient Log log = LogFactory.getLog(getClass());

    private TokenRequest tokenRequest = null;
    private TokenResponse tokenResponse = null;

	@Autowired
    private CreateXML createxml;
	@Autowired
	private ITokenRequestService iTokenRequestService;
	
	@Autowired
    private RequestToken requestToken;

	@Autowired
    private ResponseToken responseToken;
	
	@Autowired
	private ITokenResponseService iTokenResponseService;

	@Autowired
	private LogFile logfile;

	protected String makeVendRequest(String meterNo, String amount,String term, String refNo, Byte status) {
		tokenRequest = new TokenRequest();
		HashMap<String,Object> messResponse = null;
		String messJSON = null;
		ObjectMapper objectMapper = new ObjectMapper();
		byte[] reqXML= createxml.buildXML( meterNo, amount,tokenRequest, term );
		tokenRequest.setClientref(refNo);
		Double oref = tokenRequest.getRef();
		tokenRequest.setStatus(status);
		tokenRequest.setRepcount(0);
		tokenRequest.setOref(oref);
		iTokenRequestService.save(tokenRequest);
		log.info("meter No:" + meterNo);
	
		log.info("begin make request....");
		messResponse = requestToken.makeRequest(reqXML,meterNo,term);
		tokenResponse= responseToken.getTokenResponse();
		Optional<TokenResponse> optRes = Optional.ofNullable(tokenResponse);
		if (optRes.isPresent() && messResponse !=null) {
		
			tokenResponse.setMeterno(meterNo);
			
			
			messResponse.put("ref", refNo);
			try {
				messJSON = objectMapper.writeValueAsString(messResponse);
				log.info("response:"+ messJSON);
	
				
			} catch (JsonProcessingException e) {
				// TODO Auto-generated catch block
				logfile.eventLog(e.getMessage());
			}
			log.info(messJSON);
			//Byte reversal = 0;
			tokenResponse.setTokenrequest(tokenRequest);
			tokenResponse.setJsonresponse(messJSON);
			//tokenResponse.setReversal(reversal);
			iTokenResponseService.save(tokenResponse);
			Byte receipt = 1;
			tokenRequest.setReceipt(receipt);
			iTokenRequestService.save(tokenRequest);
		}
		else {
			status = 2;
			tokenRequest.setStatus(status);
			iTokenRequestService.save(tokenRequest);
			messResponse = new HashMap<String,Object>();
			messResponse.put("response","System busy.Please try again later");
			messResponse.put("status","2");
			try {
				messJSON = objectMapper.writeValueAsString(messResponse);
			} catch (JsonProcessingException e) {
				// TODO Auto-generated catch block
				logfile.eventLog(e.getMessage());
			}

		}
		return messJSON;

	}
}
