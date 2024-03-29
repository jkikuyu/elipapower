package com.ipayafrica.elipapower.webapp.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
/**
 * @author Jude Kikuyu
 * 
 * <ipayMsg client="ipay" term="1" seqNum="1" time="2002­05­16 10:55:50 +0200">
 * <elecMsg ver="2.44"><vendLastReq><ref>136105500002</ref>
 * <meter>A12C3456789</meter></vendLastReq ></elecMsg></ipayMsg>
 */
import org.springframework.web.bind.annotation.RestController;

import com.ipayafrica.elipapower.model.TokenRequest;
import com.ipayafrica.elipapower.util.CreateXML;
import com.ipayafrica.elipapower.util.RequestToken;

@RestController
public class VendLastReqController {

    private byte[] reqXML;
    private CreateXML createxml;

    private TokenRequest tokenRequest;
   
	@Autowired
	private RequestToken requestToken;

	public CreateXML getCreatexml() {
		return createxml;
	}
	@Autowired
	public void setCreatexml(CreateXML createxml) {
		this.createxml = createxml;
	}


	public VendLastReqController() {
	}
	
	@RequestMapping("/vendlastreq")
	public void lastRequest(){
		String term = "";
		String meterNo = "A12C3456789";
		reqXML = createxml.buildXML(meterNo, 2, tokenRequest, term);
		//tokenRequest.makeRequest(reqXML,meterNo);

		
	}


}
