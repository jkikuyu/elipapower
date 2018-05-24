package com.ipayafrica.elipapower.webapp.controller;
import java.util.HashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ipayafrica.elipapower.model.TokenRequest;
import com.ipayafrica.elipapower.model.TokenResponse;
import com.ipayafrica.elipapower.service.ITokenRequestService;
import com.ipayafrica.elipapower.service.ITokenResponseService;
import com.ipayafrica.elipapower.util.CreateXML;
import com.ipayafrica.elipapower.util.RequestToken;
import com.ipayafrica.elipapower.util.ResponseToken;

/**
 * 
 * @author Jude Kikuyu
 * This class will receives a request to get customer info
 * Request will have the meter number
 * <ipayMsg client="ipay" term="00001" seqNum="2" time="2018­-04­-16 10:57:00 +0300"> 
 * <elecMsg ver="2.44"> <custInfoReq><ref>136105700003</ref><meter>123456789</meter>
 * </custInfoReq></elecMsg></ipayMsg>
 *
 */
@RestController
public class CustomerInfoController {
    protected final transient Log log = LogFactory.getLog(getClass());

	private CreateXML createxml;
	
    private TokenRequest tokenRequest;
    
    private TokenResponse tokenResponse = null;

    
    private boolean isEmptyMeterNo =false;
	
    HashMap<String,Object> messResponse = null;


	@Autowired
	public void setCreatexml(CreateXML createxml) {
		this.createxml = createxml;
	}

	@Autowired
	private RequestToken requestToken;

	@Autowired
    private ResponseToken responseToken;
	
	@Autowired
	private ITokenRequestService iTokenRequestService;

	
	@Autowired
	private ITokenResponseService iTokenResponseService;

	@Autowired
	private Environment env;

	@RequestMapping("/customerinfo/{meterno}")
	public String customerInfoRequest(@PathVariable String meterno){
		//String meterNo = "A12C3456789";
		log.info("meter no" + meterno);
		messResponse = new HashMap<String,Object>();
		String messJSON = null;

		ObjectMapper objectMapper = new ObjectMapper();

		if(meterno==null || meterno.isEmpty()) {
			isEmptyMeterNo = true;

		}
		if (!isEmptyMeterNo) {
			String term = env.getProperty("company.id");

			tokenRequest = new TokenRequest();
			byte[] reqXML = createxml.buildXML(meterno,1,tokenRequest,term);
			tokenRequest = new TokenRequest();
			tokenRequest.setOref("OK");
			iTokenRequestService.save(tokenRequest);
			messResponse = requestToken.makeRequest(reqXML, meterno, term);
			tokenResponse= responseToken.getTokenResponse();
			tokenResponse.setMeterno(meterno);
			messResponse.put("ref", "OK");
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
		else {
			String errResponse = null;
			errResponse = isEmptyMeterNo? "Meter No is empty or  key used is wrong. ":"";
			messResponse.put("error",errResponse);
			messResponse.put("status", "0");
			isEmptyMeterNo =false;

		}
		return messJSON;
	}
}
