package com.ipayafrica.elipapower.util;

import java.io.IOException;
import java.io.StringReader;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

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
	@Autowired 
	XMLTokenHandler xmlTokenHandler;
	public void cleanXML(String xml) throws ParserConfigurationException, SAXException, IOException {
/*		xml = "<ipayMsgclient=\"IPAYAFRICA\"term=\"00001\"seqNum=\"10\""
				+ "time=\"2018-05-0315:20:43+0200\"><elecMsgver=\"2.48\">"
				+ "<custInfoRes><ref/><rescode=\"elec000\">OK</res>"
				+ "<customeraddr=\"Mr.JohnLinde&#xA;34Tokai,CapeTown.&#xA;7999.\""
				+ "util=\"iPayDemoUtil\"agrRef=\"8934893898934\""
				+ "type=\"STS\"locRef=\"Cape_Town\"supGrpRef=\"100405\""
				+ "tokenTechCode=\"02\"algCode=\"05\"tariffIdx=\"52\""
				+ "keyRevNum=\"1\"daysLastVend=\"19999\">AdeneJonah</customer>"
				+ "<contractagrRef=\"444444\"idNo=\"134141414141\"leRefNo=\"128\">"
				+ "AdeneJonah</contract></custInfoRes></elecMsg></ipayMsg>";
		
		xml = "<ipayMsgclient=\"IPAYAFRICA\"term=\"00001\"seqNum=\"9\""
				+ "time=\"2018-05-0309:01:27+0200\"><elecMsgver=\"2.48\">"
				+ "<vendRes><ref>812310010009</ref><rescode=\"elec001\"extCode=\"0\">"
				+ "MaximumAmountExceededMaximumAmountExceeded="
				+ "1.0Notvalid-Range10.0-9.9999999999999E13."
				+ "Verifythedataprovidedandretry</res>"
				+ "</vendRes></elecMsg></ipayMsg>";
*/
	SAXParserFactory factory = SAXParserFactory.newInstance();
	SAXParser saxParser = factory.newSAXParser();

		
    StringBuffer resXML = new StringBuffer(xml);
    int pos = resXML.indexOf("client"); 
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

    System.out.println(resXML.toString());
    InputSource is = new InputSource(new StringReader(resXML.toString()));
     saxParser.parse(is, xmlTokenHandler);

   }
}
