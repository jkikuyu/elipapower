package com.ipayafrica.elipapower.webapp.controller;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ipayafrica.elipapower.util.CreateXML;
import com.ipayafrica.elipapower.util.TokenRequest;

/**
*/
@RestController
public class CustomerInfoController {
	
	private CreateXML createxml;
	
    private TokenRequest tokenRequest;

	@Autowired
	public void setCreatexml(CreateXML createxml) {
		this.createxml = createxml;
	}

	@Autowired
	public void setTokenRequest(TokenRequest tokenRequest) {
		this.tokenRequest = tokenRequest;
	}

	public CustomerInfoController() {
	}
	
	@RequestMapping("/customerinfo")
	public void customerInfoRequest(){
		String meterNo = "A12C3456789";

		byte[] reqXML = createxml.buildXML(meterNo,1);
		tokenRequest.makeRequest(reqXML);

		
	}

}
