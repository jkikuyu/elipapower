package com.ipayafrica.elipapower.util;

import java.util.HashMap;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ipayafrica.elipapower.model.TokenRequest;
import com.ipayafrica.elipapower.model.TokenResponse;
import com.ipayafrica.elipapower.service.ITokenRequestService;
import com.ipayafrica.elipapower.service.ITokenResponseService;

@Component
public class FailedRequestBean {
    protected final transient Log log = LogFactory.getLog(getClass());

	@Autowired
	ITokenResponseService iTokenResponseService;
	@Autowired
	CreateXML createxml;
	@Autowired
	ITokenRequestService iTokenRequestService;
	@Autowired
	RequestToken requestToken;
	@Autowired
	ResponseToken responseToken;
	
	public FailedRequestBean() {
		
	}
	@Scheduled (cron = "45 * * * * *")
	public void cronJob(){
		Byte status = 2;
		HashMap<String,Object> messResponse = null;
		String messJSON = null;
		ObjectMapper objectMapper = new ObjectMapper();


		List <TokenRequest>failedTokenRequests = iTokenRequestService.findFailedRequests(status);
		if (failedTokenRequests!=null) {
			for(TokenRequest tokenRequest:failedTokenRequests){
				TokenResponse tokenResponse = new TokenResponse();
				Double dAmt = tokenRequest.getAmt() * 100;
				Double ref = tokenRequest.getRef();
				String refNo = ref.toString();

				String amount = String.valueOf(dAmt.intValue());

				tokenRequest.setAmount(amount);
				tokenRequest.setOref(refNo);
				//amount = dAmt.toString();
				String meterNo = tokenRequest.getMeterno();
				byte[] reqXML= createxml.buildXML( meterNo, 3,tokenRequest );
				iTokenRequestService.save(tokenRequest);
				log.info("meter No:" + meterNo);
			
			
				log.info("begin make request....");
				messResponse = new HashMap<String,Object>();

				messResponse = requestToken.makeRequest(reqXML,meterNo);
				tokenResponse= responseToken.getTokenResponse();
				tokenResponse.setMeterno(meterNo);
				messResponse.put("ref", tokenRequest.getRefno());
				try {
					messJSON = objectMapper.writeValueAsString(messResponse);
					log.info("response:"+ messJSON);

					
				} catch (JsonProcessingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				log.info(messJSON);

				tokenResponse.setJsonresponse(messJSON);

				iTokenResponseService.save(tokenResponse);
				
				
				
			}
		}
	}
}
