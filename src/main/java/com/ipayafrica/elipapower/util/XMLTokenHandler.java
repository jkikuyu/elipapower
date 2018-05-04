package com.ipayafrica.elipapower.util;

import java.util.HashMap;

import org.hibernate.mapping.Map;
import org.springframework.stereotype.Component;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.fasterxml.jackson.databind.util.JSONPObject;
import com.ipayafrica.elipapower.model.TokenResponse;

@Component
public class XMLTokenHandler extends DefaultHandler {

/**
 * Class parses the Power Token response and stores it in an Token object
 * @author Jude Kikuyu
 * @since 03/05/2018 
 */

	boolean biPayMsg = false;
	boolean belecMsg = false;
	boolean bvendRes = false;
	boolean bref = false;
	boolean bres = false;
	boolean bcustInfoRes = false;
	boolean bcustomer = false;
	boolean bcontract = false;
	HashMap<String, String> map =null;
	
	public void startElement(String uri, String localName,String qName, 
	            Attributes attributes) throws SAXException {
		


		if (qName.equalsIgnoreCase("ipaymsg")) {
			biPayMsg = true;
		}

		if (qName.equalsIgnoreCase("elecmsg")) {
			belecMsg = true;
		}

		if (qName.equalsIgnoreCase("vendres")) {
			bvendRes = true;
		}

		if (qName.equalsIgnoreCase("ref")) {
			bref = true;
		}
		if (qName.equalsIgnoreCase("res")) {
			bres = true;
		}
		if (qName.equalsIgnoreCase("custInfoRes")) {
			bcustInfoRes = true;
		}
		if (qName.equalsIgnoreCase("customer")) {
			bcustomer = true;
		}
		if (qName.equalsIgnoreCase("contract")) {
			bcontract = true;
		}

		boolean isSuccess = false;


		int length = attributes.getLength();
		for (int i=0; i<length; i++) {
			String name = attributes.getQName(i);
			System.out.println("Name:" + name);
			String value = attributes.getValue(i);
			System.out.println("Value:" + value);
			if(attributes.getQName(i).equalsIgnoreCase("res") &&  attributes.getValue(i).equalsIgnoreCase("elec000")){
				isSuccess = true;
			}
		}
	}
	public String getJSONMessage() {
		return null;
	}
	
	public void endElement(String uri, String localName,
		String qName) throws SAXException {


	}

	public void characters(char ch[], int start, int length) throws SAXException {

		if (biPayMsg) {
			//System.out.println("iPayMsg : " + new String(ch, start, length));
			biPayMsg = false;
		}

		if (belecMsg) {
			//System.out.println("elecMsg : " + new String(ch, start, length));
			belecMsg= false;
		}

		if (bvendRes) {
			//System.out.println("Vend Res : " + new String(ch, start, length));
			bvendRes = false;
		}

		if (bref) {
			System.out.println("ref : " + new String(ch, start, length));
			bref = false;
		}
		if (bres) {
			System.out.println("res : " + new String(ch, start, length));
			bres = false;
		}
		if (bcustInfoRes) {
			//System.out.println("custInfoRes : " + new String(ch, start, length));
			bcustInfoRes = false;
		}
		if (bcustomer) {
			//System.out.println("customer : " + new String(ch, start, length));
			bcustomer = false;
		}
		if (bcontract) {
			//System.out.println("contract : " + new String(ch, start, length));
			bcontract = false;
		}
	}

  
   
}

