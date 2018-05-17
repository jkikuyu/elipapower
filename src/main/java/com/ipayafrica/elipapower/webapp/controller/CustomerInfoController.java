package com.ipayafrica.elipapower.webapp.controller;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ipayafrica.elipapower.util.CreateXML;
import com.ipayafrica.elipapower.util.RequestToken;

/**
*/
@RestController
public class CustomerInfoController {
    protected final transient Log log = LogFactory.getLog(getClass());

	private CreateXML createxml;
	
    private RequestToken tokenRequest;

	@Autowired
	public void setCreatexml(CreateXML createxml) {
		this.createxml = createxml;
	}

	@Autowired
	public void setTokenRequest(RequestToken tokenRequest) {
		this.tokenRequest = tokenRequest;
	}

	public CustomerInfoController() {
	}
	
	@RequestMapping("/customerinfo/{meterno}")
	public void customerInfoRequest(@PathVariable String meterno){
		//String meterNo = "A12C3456789";
		log.info("meter no" + meterno);
		byte[] reqXML = createxml.buildXML(meterno,1);
		tokenRequest.makeRequest(reqXML, meterno);

		
	}

}
