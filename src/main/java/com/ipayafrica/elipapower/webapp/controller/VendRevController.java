package com.ipayafrica.elipapower.webapp.controller;
/**
 * 
 * @author Jude Kikuyu
 * A reversal of the vend request is sent in case of a failure to receive a response 
 * within the specified  time
 * This endpoint receives the reversal request and forwards to the token server
 * <ipayMsg client="ipay" term="1" seqNum="2" time="2002­05­16 10:56:30 +0200">
 * <elecMsg ver="2.44"><vendRevReq repCount="1" origTime="2002­05­16 08:21:00 +0200">
 * <ref>136105600002</ref><origRef>136105500001</origRef></vendRevReq></elecMsg >
 * </ipayMsg>
 *
 */
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ipayafrica.elipapower.util.CreateXML;
import com.ipayafrica.elipapower.util.TokenRequest;

@RestController
public class VendRevController {
    private byte[] reqXML;
    private CreateXML createxml;

	@Autowired
	private Environment env;

	@Autowired
    private TokenRequest tokenRequest;
   


	public CreateXML getCreatexml() {
		return createxml;
	}
	@Autowired
	public void setCreatexml(CreateXML createxml) {
		this.createxml = createxml;
	}

	@Autowired
	public void setTokenRequest(TokenRequest tokenRequest) {
		this.tokenRequest = tokenRequest;
	}


	public VendRevController() {
	}

	@RequestMapping("/vendrev")
	public void reversalRequest(){
		String client = env.getProperty("company.name");
		String term = env.getProperty("company.id");
		reqXML = createxml.buildXML(client, term);
		tokenRequest.makeRequest(reqXML);

		
	}

}
