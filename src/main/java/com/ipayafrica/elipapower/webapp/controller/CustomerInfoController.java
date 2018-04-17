package com.ipayafrica.elipapower.webapp.controller;
import org.jdom2.Element;
/**
 * 
 * @author Jude Kikuyu
 * This class will receives a request to get customer info
 * Request will have the meter number
 *
 */
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ipayafrica.elipapower.Invariable;


@RestController
public class CustomerInfoController {
	
	@Autowired
	private Environment env;
	
	public CustomerInfoController() {
	}
	
	@RequestMapping("/customerinfo")
	public void customerInfoRequest(){
		String client = env.getProperty("company.name");
		String term = env.getProperty("company.id");
		Element ipayMsg = new Element(Invariable.IPAY);
		ipayMsg.setAttribute(Invariable.CLIENT,client);
		ipayMsg.setAttribute(Invariable.TERM, term);
		ipayMsg.setAttribute(Invariable.SEQNUM,seqNo);
		ipayMsg.setAttribute(Invariable.TIME, dtt);

		
	}

}
