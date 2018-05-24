package com.ipayafrica.elipapower.webapp.controller;
/**
 * 
 * @author Jude Kikuyu
 * A reversal of the vend request is sent in case of a failure to receive a response 
 * within the specified  time
 * This endpoint receives the reversal request and forwards to the token server
 * <ipayMsg client="ipay" term="1" seqNum="2" time="2018-04-­16 10:56:30 +0300">
 * <elecMsg ver="2.44"><vendRevReq repCount="1" origTime="2018-04-­16 01:56:30 +0300">
 * <ref>136105600002</ref><origRef>136105500001</origRef></vendRevReq></elecMsg >
 * </ipayMsg>
 *
 */
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ipayafrica.elipapower.model.TokenRequest;
import com.ipayafrica.elipapower.util.CreateXML;
import com.ipayafrica.elipapower.util.RequestToken;

@RestController
public class VendRevController {
    private byte[] reqXML;
    
    private TokenRequest tokenRequest;
    @Autowired
    private CreateXML createxml;

	@Autowired
    private RequestToken requestToken;
	
	


	public VendRevController() {
	}

	@RequestMapping("/vendrev")
	public void reversalRequest(){
		String term = "";

		String meterNo = "A12C3456789";
		reqXML = createxml.buildXML(meterNo, 3, tokenRequest, term);
		//tokenRequest.makeRequest(reqXML);
	
	}

}
