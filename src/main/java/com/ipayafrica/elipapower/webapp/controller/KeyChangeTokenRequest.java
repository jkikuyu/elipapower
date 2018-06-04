package com.ipayafrica.elipapower.webapp.controller;

import java.util.HashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
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
public class KeyChangeTokenRequest {
    protected final transient Log log = LogFactory.getLog(getClass());
    HashMap<String,Object> messResponse = null;
	private boolean isEmptyMeterNo =false, isEmptyAmount=false, isEmptyRef=false, isEmptyDemo=false;
    private TokenResponse tokenResponse = null;
    private TokenRequest tokenRequest = null;

	@Autowired
    private RequestToken requestToken;
   
	@Autowired
    private ResponseToken responseToken;

	@Autowired
    private CreateXML createxml;
	
	

	@Autowired
	private Environment env;

	@Autowired
	private ITokenResponseService iTokenResponseService;
	
	@Autowired
	private ITokenRequestService iTokenRequestService;

	@RequestMapping(value ="/keychangetokenreq", method=RequestMethod.POST, produces={"application/json"})
	@ResponseBody	public String requestKeyChangeToken(@RequestBody Token token){
		String messJSON = null;

		ObjectMapper objectMapper = new ObjectMapper();

		String meterNo = token.getMeterno();
		
		String amount = token.getAmount();
		Integer dAmt = Integer.parseInt(amount)*100; // convert to cents
		Integer demo = Integer.parseInt(token.getDemo());
		
		amount = dAmt.toString();
		String refNo = token.getRefno();
		HashMap<String,Object> messResponse = null;
		Byte status;

		if(meterNo==null || meterNo.isEmpty()) {
			isEmptyMeterNo = true;

		}
		else if (amount==null || amount.isEmpty()){
			isEmptyAmount =true; 
		}
		else if (refNo==null || refNo.isEmpty()) {
			isEmptyRef =true;
		}
		else if (demo == null || demo>1) {
			isEmptyDemo =true;
		}
		log.info("amount:"+amount);
		messResponse = new HashMap<String,Object>();
		String errResponse = null;

		if (!isEmptyMeterNo && !isEmptyAmount && !isEmptyRef &&!isEmptyDemo) {
			tokenRequest = new TokenRequest();
			status = 1;
			String term;
			if(demo == 1) {
				term = env.getProperty("dummy.id");

			}
			else {
				term = env.getProperty("company.id");
			}
			if (term==null) {
				errResponse = "internal server error";
				messResponse.put("error",errResponse);
				messResponse.put("status", "0");
				try {
					messJSON = objectMapper.writeValueAsString(messResponse);
				} catch (JsonProcessingException e) {
					e.printStackTrace();
				}
			}
			else {
				byte[] reqXML= createxml.buildXML( meterNo, 4,tokenRequest, term );

				tokenRequest.setClientref(refNo);
				Double oref = tokenRequest.getRef();
				tokenRequest.setStatus(status);
				tokenRequest.setRepcount(0);
				tokenRequest.setOref(oref);
				iTokenRequestService.save(tokenRequest);
				log.info("meter No:" + meterNo);
			
				log.info("begin make request....");
				messResponse = requestToken.makeRequest(reqXML,meterNo,term);
				if (messResponse==null) {
					status = 2;
					tokenRequest.setStatus(status);
					iTokenRequestService.save(tokenRequest);
		
				}
				else {
					tokenResponse= responseToken.getTokenResponse();
					tokenResponse.setMeterno(meterNo);
					
			
					messResponse.put("ref", token.getRefno());
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
		else {
			errResponse = null;
			errResponse = isEmptyMeterNo? "Meter No is empty or  key used is wrong. ":"";
			errResponse +=isEmptyRef? "Reference Number is empty or key used is wrong":"";

			errResponse +=isEmptyAmount ? "Amount is empty or key used is wrong":"";
			errResponse +=isEmptyDemo ? "Demo is empty or key used is wrong":"";

			messResponse.put("error",errResponse);
			messResponse.put("status", "0");
			isEmptyMeterNo =false;
			isEmptyAmount=false;
			isEmptyRef=false;
			isEmptyDemo=false;
			try {
				messJSON = objectMapper.writeValueAsString(messResponse);
			} catch (JsonProcessingException e) {
				e.printStackTrace();
			}

		}
		return messJSON;
	}


}
