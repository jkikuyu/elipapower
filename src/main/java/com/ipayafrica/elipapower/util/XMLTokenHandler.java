package com.ipayafrica.elipapower.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Optional;
import java.util.TimeZone;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

@Component
public class XMLTokenHandler extends DefaultHandler {

/**
 * Class parses the Power Token response and stores it in an Token object
 * @author Jude Kikuyu
 * @since 03/05/2018 
 */
    protected final transient Log log = LogFactory.getLog(getClass());

	boolean biPayMsg = false;
	boolean belecMsg = false;
	boolean bvendRes = false;
	boolean bref = false;
	boolean bres = false;
	boolean bcustInfoRes = false;
	boolean bcustomer = false;
	boolean bcontract = false;
	boolean butil = false;
	boolean bstdToken = false;
	boolean bbsstToken = false;
	boolean bdebt = false;
	boolean bfixed = false;
	boolean brtlrMsg = false;
	boolean bkeyChangeRes = false;
	boolean bkeyChangeToken = false;

	boolean boldSupGrpRef = false;
	boolean boldTariffIdx = false;
	boolean boldKeyRevNum = false;
	boolean bnewSupGrpRef = false;
	boolean bnewTariffIdx = false;
	boolean bnewKeyRevNum = false;
	boolean bcode   	  = false;
	private HashMap<String, Object> mapResponse = null;
	private String ref;
	
	private String resCode;
	private Date responseDate;
	@Autowired
	Environment env;
	public XMLTokenHandler() {
		mapResponse =new HashMap<String, Object>();
	}
	public void startElement(String uri, String localName,String qName, 
	            Attributes attributes) throws SAXException {

		//mapResponse = 
		/*format for date in date time and timezone*/
		SimpleDateFormat sdf = null;
		TimeZone tz = null;
		if (qName.equalsIgnoreCase("ipaymsg")) {
			
			tz = TimeZone.getTimeZone(env.getProperty("timezone.local"));
			sdf =  new SimpleDateFormat(env.getProperty("time.format"));
			sdf.setTimeZone(tz);

			String datetime = attributes.getValue("time");
			StringBuffer sb = new StringBuffer(datetime);
			sb.insert(10," ");
			sb.insert(sb.indexOf("+"), " ");
			datetime = sb.toString();
			try {
				responseDate = sdf.parse(datetime);
				log.info("datetime" + datetime);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			

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
			resCode = attributes.getValue("code");
			

			bres = true;
		}
		if (qName.equalsIgnoreCase("custInfoRes")) {
			bcustInfoRes = true;
		}
		if (qName.equalsIgnoreCase("customer")) {
			mapResponse.put("address", attributes.getValue("addr"));

			bcustomer = true;
		}
		if (qName.equalsIgnoreCase("contract")) {
			bcontract = true;
		}
		if (qName.equalsIgnoreCase("util")) {
			
			butil = true;
		}

		int length = attributes.getLength();

		for (int i=0; i<length; i++) {
			String name = attributes.getQName(i);
			System.out.println("Name:" + name);
			String value = attributes.getValue(i);
			System.out.println("Value:" + value);				
			

		}

		//log.info("ref " + ref);
		if (qName.equalsIgnoreCase("stdToken")) {

			mapResponse.put("units", attributes.getValue("units"));

			HashMap<String,String> mapTarrif = new HashMap<String,String>();
			mapResponse.put("tax", attributes.getValue("tax"));
			Optional<String[]> optTarrifs = Optional.ofNullable(attributes.getValue("tariff").split(":"));
			if(optTarrifs.isPresent()) {
				String[] tarrifs = optTarrifs.get();
				String key = "";
				int i = 1;
	
				for (String tarrif:tarrifs) {
					key="t" +Integer.toString(i);
					mapTarrif.put(key, tarrif);
					i++;
				}
				mapResponse.put("tarrif",mapTarrif);
				mapResponse.put("unitsType", attributes.getValue("unitsType"));
				bstdToken = true;
			}
			if (qName.equalsIgnoreCase("rctnum")) {
				mapResponse.put("rctnum", attributes.getValue("rctNum"));
			}

		}
		if (qName.equalsIgnoreCase("bsstToken")) {
			bbsstToken = true;
		}
		if (qName.equalsIgnoreCase("debt")) {
			bdebt = true;
		}
		if (qName.equalsIgnoreCase("fixed")) {
			mapResponse.put("fixedamt", attributes.getValue("amt"));
			mapResponse.put("fixedtax", attributes.getValue("tax"));

			bfixed = true;
		}

		if (qName.equalsIgnoreCase("rtlrMsg")) {
			brtlrMsg = true;
		}
		
		if (qName.equalsIgnoreCase("bkeyChangeRes")) {
			bkeyChangeRes = true;
		}
		        

		if (qName.equalsIgnoreCase("bkeyChangeToken")) {
			bkeyChangeToken = true;
		}
		if (qName.equalsIgnoreCase("boldSupGrpRef")) {
			boldSupGrpRef = true;
		}
		
		if (qName.equalsIgnoreCase("boldTariffIdx")) {
			boldTariffIdx = true;
		}
		if (qName.equalsIgnoreCase("boldKeyRevNum")) {
			boldKeyRevNum = true;
		}

		if (qName.equalsIgnoreCase("bnewSupGrpRef")) {
			bnewSupGrpRef = true;
		}
		if (qName.equalsIgnoreCase("bnewTariffIdx")) {
			bnewTariffIdx = true;
		}
		if (qName.equalsIgnoreCase("bnewKeyRevNum")) {
			bnewKeyRevNum = true;
		}
		if (qName.equalsIgnoreCase("bcode")) {
			bcode = true;
		}

	}
	public HashMap<String, Object>  getMessageMap() {
/*	    if (mapResponse.isEmpty() ||mapResponse==null) {
			log.info("hashmap empty");

	    }
	    else {

			mapResponse.forEach((k,v)->log.info("key : " + k + " value : " + v));
		
	    }
*/		
		return mapResponse;
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
			String tref;
			tref = new String(ch, start, length);
			if(tref.equals("OK")) {
				ref = "1";
			}
			else {
				ref = tref;
			}
			//ref = Double.parseDouble(s);

			bref = false;
		}
		if (bres) {
			mapResponse.put("reason",new String(ch, start, length));
			bres = false;
		}
		if (bcustInfoRes) {
			//System.out.println("custInfoRes : " + new String(ch, start, length));
			bcustInfoRes = false;
		}
		if (bcustomer) {
			mapResponse.put("customerName",new String(ch, start, length) );

			//System.out.println("customer : " + new String(ch, start, length));
			bcustomer = false;
		}
		if (bcontract) {
			//System.out.println("contract : " + new String(ch, start, length));
			bcontract = false;
		}
		if (butil) {
			//System.out.println("contract : " + new String(ch, start, length));
			butil = false;
		}
		if (bstdToken) {
			mapResponse.put("token",new String(ch, start, length));

			//System.out.println("contract : " + new String(ch, start, length));
			bstdToken = false;
		}
		if (bbsstToken) {
			//System.out.println("contract : " + new String(ch, start, length));
			bbsstToken = false;
		}
		if (bdebt) {
			//System.out.println("contract : " + new String(ch, start, length));
			bdebt = false;
		}
		if (bfixed) {
			//System.out.println("contract : " + new String(ch, start, length));
			bfixed = false;
		}

		if (brtlrMsg) {
			//System.out.println("contract : " + new String(ch, start, length));
			brtlrMsg = false;
		}
		if (bkeyChangeRes) {
			//System.out.println("contract : " + new String(ch, start, length));
			bkeyChangeRes = false;
		}
		if (bkeyChangeToken) {
			//System.out.println("contract : " + new String(ch, start, length));
			bkeyChangeToken = false;
		}
		if (boldSupGrpRef) {
			//System.out.println("contract : " + new String(ch, start, length));
			boldSupGrpRef = false;
		}
		if (boldTariffIdx) {
			//System.out.println("contract : " + new String(ch, start, length));
			boldTariffIdx = false;
		}
		if (boldKeyRevNum) {
			//System.out.println("contract : " + new String(ch, start, length));
			boldKeyRevNum = false;
		}
		if (bnewSupGrpRef) {
			//System.out.println("contract : " + new String(ch, start, length));
			bnewSupGrpRef = false;
		}
		if (bnewTariffIdx) {
			//System.out.println("contract : " + new String(ch, start, length));
			bnewTariffIdx = false;
		}
		if (bnewKeyRevNum) {
			//System.out.println("contract : " + new String(ch, start, length));
			bnewKeyRevNum = false;
		}
		if (bcode) {
			//System.out.println("contract : " + new String(ch, start, length));
			bcode = false;
}


	}
	public String getRef() {
		return ref;
	}
	public Date getResponseDate() {
		return responseDate;
	}
	public String getResCode() {
		return resCode;
	}
   
}

