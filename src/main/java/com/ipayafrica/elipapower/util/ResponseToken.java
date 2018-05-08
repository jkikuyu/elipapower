package com.ipayafrica.elipapower.util;
/**
 * @author Jude Kikuyu
 * created on 4/05/2018
 * Class to create a reponse xml that is formatted in a way that it can be parsed by SAXParser
 * Examples below
 * 	xml = "<ipayMsgclient=\"IPAYAFRICA\"term=\"00001\"seqNum=\"10\""
 *				+ "time=\"2018-05-0315:20:43+0200\"><elecMsgver=\"2.48\">"
 *			+ "<custInfoRes><ref/><rescode=\"elec000\">OK</res>"
 *				+ "<customeraddr=\"Mr.JohnLinde&#xA;34Tokai,CapeTown.&#xA;7999.\""
 *				+ "util=\"iPayDemoUtil\"agrRef=\"8934893898934\""
 *				+ "type=\"STS\"locRef=\"Cape_Town\"supGrpRef=\"100405\""
 *				+ "tokenTechCode=\"02\"algCode=\"05\"tariffIdx=\"52\""
 *				+ "keyRevNum=\"1\"daysLastVend=\"19999\">AdeneJonah</customer>"
 *				+ "<contractagrRef=\"444444\"idNo=\"134141414141\"leRefNo=\"128\">"
 *				+ "AdeneJonah</contract></custInfoRes></elecMsg></ipayMsg>";
 *		
 *		xml = "<ipayMsgclient=\"IPAYAFRICA\"term=\"00001\"seqNum=\"9\""
 *				+ "time=\"2018-05-0309:01:27+0200\"><elecMsgver=\"2.48\">"
 *				+ "<vendRes><ref>812310010009</ref><rescode=\"elec001\"extCode=\"0\">"
 *				+ "MaximumAmountExceededMaximumAmountExceeded="
 *				+ "1.0Notvalid-Range10.0-9.9999999999999E13."
 *				+ "Verifythedataprovidedandretry</res>"
 *				+ "</vendRes></elecMsg></ipayMsg>";
 * 
 */
import java.io.IOException;
import java.io.StringReader;
import java.util.Date;
import java.util.HashMap;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.ipayafrica.elipapower.model.TokenResponse;
import com.ipayafrica.elipapower.service.IErrorCodeService;

@Component
public class ResponseToken {
	/**
	 * <ipayMsgclient="IPAYAFRICA"term="00001"seqNum="7"time="2018-05-0308:04:43+0200">
	 * <elecMsgver="2.48"><custInfoRes><ref/><rescode="elec000">OK</res>
	 * <customeraddr="Mr.JohnLinde&#xA;34Tokai,CapeTown.&#xA;7999."
	 * util="iPayDemoUtil"agrRef="8934893898934"type="STS"locRef="Cape_Town"
	 * supGrpRef="100405"tokenTechCode="02"algCode="05"tariffIdx="52"
	 * keyRevNum="1"daysLastVend="19999">AdeneJonah</customer>
	 * <contractagrRef="444444"idNo="134141414141"leRefNo="128">AdeneJonah</contract>
	 * </custInfoRes></elecMsg></ipayMsg>
	 * 
	 */
    protected final transient Log log = LogFactory.getLog(getClass());
    
	private TokenResponse tokenResponse = null;
	private String xml;

	@Autowired 
	XMLTokenHandler xmlTokenHandler;
	@Autowired
	private IErrorCodeService iErrorCodeService;

	public HashMap<String, Object> cleanXML(String xml) throws ParserConfigurationException, SAXException, IOException {
	this.xml= xml;
	
	SAXParserFactory factory = SAXParserFactory.newInstance();
	SAXParser saxParser = factory.newSAXParser();
		
    StringBuffer resXML = new StringBuffer(xml);
    
    int pos = resXML.indexOf("<"); 
    if(pos>0) {
  	  resXML.delete(0, pos );
    }
    pos = resXML.indexOf("client"); 
    if(pos>0) {
  	  resXML.replace(pos, pos + 6, " client");
    }
    pos = resXML.indexOf("term"); 
    if(pos>0) {
  	  resXML.replace(pos, pos + 4, " term");
    }
    pos = resXML.indexOf("seqNum"); 
    if(pos>0) {
  	  resXML.replace(pos, pos + 6, " seqNum");
  	  
    }
    pos = resXML.indexOf("time"); 
    if(pos>0) {
  	  resXML.replace(pos, pos + 4, " time");
  	  
    }
    pos = resXML.indexOf("seqNum"); 
    if(pos>0) {
  	  resXML.replace(pos, pos + 6, " seqNum");
  	  
    }
    pos = resXML.indexOf("ver"); 
    if(pos>0) {
  	  resXML.replace(pos, pos + 3, " ver");
  	  
    }
    pos = resXML.indexOf("code"); 
    if(pos>0) {
  	  resXML.replace(pos, pos + 4, " code");
  	  
    }
    pos = resXML.indexOf("extCode"); 
    if(pos>0) {
  	  resXML.replace(pos, pos + 7, " extCode");
  	  
    }
    pos = resXML.indexOf("addr"); 
    if(pos>0) {
  	  resXML.replace(pos, pos + 4, " addr");
    }
    pos = resXML.indexOf("util"); 
    if(pos>0) {
  	  resXML.replace(pos, pos + 4, " util");
    }
    pos = resXML.indexOf("agrRef"); 
    if(pos>0) {
  	  resXML.replace(pos, pos + 6, " agrRef");
    }
    pos = resXML.indexOf("type"); 
    if(pos>0) {
  	  resXML.replace(pos, pos + 4, " type");
    }
    pos = resXML.indexOf("locRef"); 
    if(pos>0) {
  	  resXML.replace(pos, pos + 6, " locRef");
    }
    pos = resXML.indexOf("supGrpRef"); 
    if(pos>0) {
  	  resXML.replace(pos, pos + 9, " supGrpRef");
    }
    pos = resXML.indexOf("tokenTechCode"); 
    if(pos>0) {
  	  resXML.replace(pos, pos + 13, " tokenTechCode");
    }
    pos = resXML.indexOf("algCode"); 
    if(pos>0) {
  	  resXML.replace(pos, pos + 7, " algCode");
    }
    pos = resXML.indexOf("tariffIdx"); 
    if(pos>0) {
  	  resXML.replace(pos, pos + 9, " tariffIdx");
    }
    pos = resXML.indexOf("keyRevNum"); 
    if(pos>0) {
  	  resXML.replace(pos, pos + 9, " keyRevNum");
    }
    pos = resXML.indexOf("daysLastVend"); 
    if(pos>0) {
  	  resXML.replace(pos, pos + 12, " daysLastVend");
    }
    pos = resXML.indexOf("contractagrRef"); 
    if(pos>0) {
  	  resXML.replace(pos, pos + 14, "contract agrRef");
    }
    pos = resXML.indexOf("idNo"); 
    if(pos>0) {
  	  resXML.replace(pos, pos + 4, " idNo");
    }
    pos = resXML.indexOf("leRefNo"); 
    if(pos>0) {
    	resXML.replace(pos, pos + 7, " leRefNo");
    }
    
    pos = resXML.indexOf("finAdj"); 

    if(pos>0) {
    	resXML.replace(pos, pos + 6, " finAdj");
    }
    pos = resXML.indexOf("leRefNo"); 

    if(pos>0) {
    	resXML.replace(pos, pos + 7, " leRefNo");
    }
    pos = resXML.indexOf("finAdj"); 
    if(pos>0) {
  	  resXML.replace(pos, pos + 6, " finAdj");
    }


    pos = resXML.indexOf("keyRevNum"); 
    if(pos>0) {
  	  resXML.replace(pos, pos + 9, " keyRevNum");
    }
    pos = resXML.indexOf("resource"); 
    if(pos>0) {
  	  resXML.replace(pos, pos + 8, " resource");
    }
    pos = resXML.indexOf("taxRef"); 
    if(pos>0) {
  	  resXML.replace(pos, pos + 6, " taxRef");
    }
    pos = resXML.indexOf("distId"); 
    if(pos>0) {
  	  resXML.replace(pos, pos + 6, " distId");
    }
    pos = resXML.indexOf("units="); 
    if(pos>0) {
  	  resXML.replace(pos, pos + 6, " units=");
    }
    pos = resXML.indexOf("tax="); 
    if(pos>0) {
  	  resXML.replace(pos, pos + 4, " tax=");
    }
    pos = resXML.indexOf("tax=",pos+4); 
    if(pos>0) {
  	  resXML.replace(pos, pos + 4, " tax=");
    }
    pos = resXML.indexOf("tax=",pos+4); 
    if(pos>0) {
  	  resXML.replace(pos, pos + 4, " tax=");
    }

    pos = resXML.indexOf("tax=",pos+4); 
    if(pos>0) {
  	  resXML.replace(pos, pos + 4, " tax=");
    }

    pos = resXML.indexOf("tariff="); 
    if(pos>0) {
  	  resXML.replace(pos, pos + 7, " tariff=");
    }
    pos = resXML.indexOf("amt"); 
    if(pos>0) {
  	  resXML.replace(pos, pos + 3, " amt");
    }
    pos = resXML.indexOf("amt",pos+3); 
    if(pos>0) {
  	  resXML.replace(pos, pos + 3, " amt");
    }
    pos = resXML.indexOf("amt",pos+3); 
    if(pos>0) {
  	  resXML.replace(pos, pos + 3, " amt");
    }
    
    pos = resXML.indexOf("amt",pos+3); 
    if(pos>0) {
  	  resXML.replace(pos, pos + 3, " amt");
    }

    pos = resXML.indexOf("unitsType"); 
    if(pos>0) {
  	  resXML.replace(pos, pos + 9, " unitsType");
    }
    pos = resXML.indexOf("rctNum"); 
    if(pos>0) {
  	  resXML.replace(pos, pos + 6, " rctNum");
    }
    pos = resXML.indexOf("desc"); 
    if(pos>0) {
  	  resXML.replace(pos, pos + 4, " desc");
    }
    pos = resXML.indexOf("bsstDate"); 
    if(pos>0) {
  	  resXML.replace(pos, pos + 8, " bsstDate");
    }
    pos = resXML.indexOf("rem"); 
    if(pos>0) {
  	  resXML.replace(pos, pos + 3, " rem");
    }

    pos = resXML.lastIndexOf("unitsType"); 
    if(pos>0) {
  	  resXML.replace(pos, pos + 9, " unitsType");
    }
    pos = resXML.lastIndexOf("units="); 
    if(pos>0) {
  	  resXML.replace(pos, pos + 6, " units=");
    }
    pos = resXML.lastIndexOf("tariff="); 
    if(pos>0) {
  	  resXML.replace(pos, pos + 7, " tariff=");
    }
    pos = resXML.lastIndexOf("desc"); 
    if(pos>0) {
  	  resXML.replace(pos, pos + 4, " desc");
    }
    pos = resXML.lastIndexOf("desc",pos); 
    if(pos>0) {
  	  resXML.replace(pos, pos + 4, " desc");
    }
    String cleanedXML = resXML.toString();
    InputSource is = new InputSource(new StringReader(cleanedXML));
    saxParser.parse(is, xmlTokenHandler);
    tokenResponse = new TokenResponse();
    
    HashMap<String, Object> messMap = xmlTokenHandler.getMessageMap();
    tokenResponse.setOrigxml(xml);
    tokenResponse.setRef(xmlTokenHandler.getRef());
    tokenResponse.setResponsexml(cleanedXML);
    tokenResponse.setOsysdate(new Date());
    tokenResponse.setResponsedate(xmlTokenHandler.getResponseDate());
	int errorCodeId = iErrorCodeService.getByMessageCode(xmlTokenHandler.getResCode());
	tokenResponse.setErrorcodeid(errorCodeId);

    return messMap;
   }
	public TokenResponse getTokenResponse() {
		return tokenResponse;
	}
	
}
