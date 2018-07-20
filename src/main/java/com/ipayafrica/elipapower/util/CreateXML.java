package com.ipayafrica.elipapower.util;
/**
 * 
 * @author Jude Kikuyu
 * created on 18/04/2018
 * 
 * This class is used to generate the XML document
 * The document resembles the format below
 * <ipayMsg client='IPAYAFRICA' term='00001' seqNum='0' 
 * time='2018-04-17 10:03:22 +0300'><elecMsg ver='2.44'><vendReq>
 * <ref>136105500001</ref><amt cur='KES'>100</amt><numTokens>1</numTokens><meter> 
 * A12C3456789</meter><payType>cash</payType></vendReq></elecMsg></ipayMsg>
 */
import java.io.ByteArrayOutputStream;
import java.io.StringReader;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.output.XMLOutputter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import com.ipayafrica.elipapower.Invariable;
import com.ipayafrica.elipapower.model.TokenRequest;
import com.ipayafrica.elipapower.service.ISerialNumberService;
@PropertySource("classpath:application.properties")

@Component

public class CreateXML {
    protected final transient Log log = LogFactory.getLog(getClass());

	private Element ipayMsg = null, elecMsg=null;
	byte[] reqXML=null;
	
	private String  num, currency, type,tref, seqNo, dtt, oref;
	Double refNo;

	//int repeat = 0;
	Date date = null;
	SimpleDateFormat sdf;
	TimeZone tz = null;
	@Autowired
	private Environment env;
	//Document
	Document doc=null;
	@Autowired
	LogFile logfile;
	
	@Autowired
	ISerialNumberService iSerialNumberService;
	


	//constructor
	public CreateXML() {
		num  = "1";


		//tz = TimeZone.getTimeZone("Africa/Nairobi");
		/*format for date in date time and timezone*/
		//sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z");
		//sdf.setTimeZone(tz);

	}


	/**
	 *<ipayMsg client="IPAYAFRICA" term="00001" seqNum="2" time="2018­-04­-16 10:57:00 +0300"> 
	 * <elecMsg ver="2.44"> <custInfoReq><ref>136105700003</ref><meter>123456789</meter>
	 * </custInfoReq></elecMsg></ipayMsg>

	 * @param meter
	 * @return
	 */
	public byte[] buildXML(String meterNo,int reqtype, TokenRequest tokenReq, String term){
		date = new Date();// date to be used in message

		reqXML= null;
		//sdf.setTimeZone(tz);
		//dtt = sdf.format(date);

		doc = new Document();
		createInitDoc(term);

/*		if(tokenReq == null) {

			createInitDoc(term);
		}
		else {
			
			createInitDoc("00001");

		}
*/		Element ref = new Element(Invariable.REF);
		
		
		Element meter = new Element(Invariable.METER);
		Element origRef = null;
		meter.setText(meterNo);
		doc.setRootElement(ipayMsg);   
		ipayMsg.addContent(elecMsg);
		Double dAmt = 0.0;
		
		switch(reqtype){
		case 1:
			Element custInfoReq =null;
			custInfoReq = new Element(Invariable.CUSTINFO);
			custInfoReq.addContent(ref);
			custInfoReq.addContent(meter);
			elecMsg.addContent(custInfoReq);

			break;
		case 2:
			Element vendLastReq =null;
			vendLastReq = new Element(Invariable.VENDLASTREQ);
			vendLastReq.addContent(ref);
			vendLastReq.addContent(meter);
			elecMsg.addContent(vendLastReq);

			break;
		case 3:
			Element vendRevReq =null;
			DecimalFormat df = new DecimalFormat(".#");

			Integer repcount = tokenReq.getRepcount();
			vendRevReq = new Element(Invariable.VENDREVREQ);

			origRef = new Element(Invariable.ORIGREF);
			String sref = df.format(tokenReq.getOref());
		    StringBuffer sbRefNo = new StringBuffer(sref);
		    int pos = sbRefNo.indexOf(".");
		    int end = sbRefNo.length();
		    sbRefNo.delete(pos,end);


			origRef.setText(sbRefNo.toString());
			ref.setText(tref);
			if(repcount >0){
				String	oTime= sdf.format(tokenReq.getRequestdate()); //to obtain origin time of first reversal advice

/*				Element repCount = new Element(Invariable.REPCOUNT);
				Element orgTime = new Element(Invariable.ORIGTIME);
			orgTime.setText(oTime);
				vendRevReq.addContent(orgTime);
				
				repCount.setText(Integer.toString(repeat));

				vendRevReq.addContent(repCount);
				
*/				vendRevReq.setAttribute(Invariable.REPCOUNT,repcount.toString());
				vendRevReq.setAttribute(Invariable.ORIGTIME,oTime);


			}

			vendRevReq.addContent(ref);
			vendRevReq.addContent(origRef);		

			//if repeat request for reversal then add the orig

			elecMsg.addContent(vendRevReq);
			break;
		case 4:
/*			<ipayMsg client="uattest" term="term1" seqNum="6" time="2017-05-05 16:24:06 +0200" termAuthId="F8-CA-B8-0E-27-4F">
			  <elecMsg ver="2.6a">
			    <vendReq>
			      <ref>712516240051</ref>
			      <amt cur="ZAR">100000</amt>
			      <numTokens>1</numTokens>
			      <meter supGrpRef="000402" tokenTechCode="02" algCode="07" tariffIdx="01" keyRevNum="1">01000000198</meter>
			      <payType>cash</payType>
			    </vendReq>
			  </elecMsg>
			</ipayMsg>
			
			
*/			
			String amount = tokenReq.getAmount();
	    	dAmt = Double.parseDouble(amount)/100; //reset to shillings

			Element vendReq = new Element(Invariable.VENDREQ);
			Element numTokens = new Element(Invariable.NUM);
			Element payType = new Element(Invariable.PAYTYPE);
			Element amt = new Element(Invariable.AMT);
			ref = new Element(Invariable.REF);
			ref.setText(tref);

			amt.setAttribute(Invariable.CUR, currency);
			
			amt.setText(amount);

			numTokens.setText(num);

			payType.setText(type);

			vendReq.addContent(ref);
			vendReq.addContent(amt);
			
			vendReq.addContent(numTokens);
			vendReq.addContent(meter);
			vendReq.addContent(payType);
			ipayMsg.setAttribute("termAuthId", "F8-CA-B8-0E-27-4F");
			elecMsg.setAttribute(Invariable.VER, "2.6a");
			
			meter.setAttribute("supGrpRef","000402");
			meter.setAttribute("tokenTechCode","02");
			meter.setAttribute("algCode","07");
			meter.setAttribute("tariffIdx","01");
			meter.setAttribute("keyRevNum","1");
			oref =tref;
			
			elecMsg.addContent(vendReq);

			break;

		case 5:
		/*
		 * <ipayMsg client="uattest" term="term1" seqNum="2" time="2017-05-05 16:24:14 +0200" termAuthId="F8-CA-B8-0E-27-4F">
		  <elecMsg ver="2.42">
		    <keyChangeAdvReq>
		      <ref>712516248588</ref>
		      <origRef>712516248587</origRef>
		      <adviceData>meterEngTokenRef:7861231050,kctMeterEngTokenRef:7861221050,meterType:STSMeter,msno:01000000198</adviceData>
		    </keyChangeAdvReq>
		  </elecMsg>
		</ipayMsg>
		 */
			ref = new Element(Invariable.REF);
			ref.setText(tref);
			oref =tref;

			ipayMsg.setAttribute("termAuthId", "F8-CA-B8-0E-27-4F");
			elecMsg.setAttribute(Invariable.VER, "2.42");
			meter.setAttribute("supGrpRef","000402");
			meter.setAttribute("tokenTechCode","02");
			meter.setAttribute("algCode","07");
			meter.setAttribute("tariffIdx","01");
			meter.setAttribute("keyRevNum","1");

			Element keyChangeReq =new Element(Invariable.KEYCHANGEREQ);

			Element newSupGrpRef = new Element("newSupGrpRef");
			Element newTariffIdx = new Element("newTariffIdx");
			Element newKeyRevNum =new Element("newKeyRevNum");
			
			keyChangeReq.addContent(ref);
			keyChangeReq.addContent(meter);

			keyChangeReq.addContent(newSupGrpRef);
			keyChangeReq.addContent(newTariffIdx);
			keyChangeReq.addContent(newKeyRevNum);
			elecMsg.addContent(keyChangeReq);
			break;
		case 6:
			Element keyChangeAdvReq =new Element(Invariable.KEYCHANGEADVREQ);
			Element adviceData = new Element(Invariable.ADVICEDATA);
			
			ref = new Element(Invariable.REF);
			ref.setText(tref);
			ipayMsg.setAttribute("termAuthId", "F8-CA-B8-0E-27-4F");


			adviceData.setText("meterEngTokenRef:7861231050,kctMeterEngTokenRef:"
					+ "7861221050,meterType:STSMeter,msno:01000000198");
			origRef =new Element(Invariable.ORIGREF);
			origRef.setText(oref);
			elecMsg.setAttribute(Invariable.VER, "2.42");

			keyChangeAdvReq.addContent(ref);
			keyChangeAdvReq.addContent(origRef);
			keyChangeAdvReq.addContent(adviceData);
			elecMsg.addContent(keyChangeAdvReq);



		}
		makeXML(doc);
		String sXML = new String(reqXML);
		System.out.println(sXML);
    	byte payType = 0;

		String mess = "Request: " + new String(reqXML);
    	tokenReq.setMeterno(meterNo);
    	tokenReq.setRef(refNo);
		tokenReq.setAmt(dAmt);

    	tokenReq.setRequestdate(date);
    	tokenReq.setRequestxml(sXML);
    	tokenReq.setSeqnum(Integer.parseInt(seqNo));
    	tokenReq.setType(payType);
		logfile.eventLog(mess);
		return reqXML;
		
	}
	/**
	 * 

	 * @param meterNo
	 * @return
	 */
	public byte[] buildXML(String meterNo, String amount,TokenRequest tokenReq,String term){

        reqXML = null;
		date = new Date();// date to be used in message
		//String term = env.getProperty("company.id");


        doc = new Document();
        
    	// vendreq element
    	Element vendReq = null;
		createInitDoc(term);

		//ref
		Element ref = new Element(Invariable.REF);
		ref.setText(tref);

		Element amt = new Element(Invariable.AMT);
		amt.setAttribute(Invariable.CUR, currency);
		
		amt.setText(amount);
		
		Element numTokens = new Element(Invariable.NUM);
		
		numTokens.setText(num);
		
		Element meter = new Element(Invariable.METER);
		
		meter.setText(meterNo);
		
		Element payType = new Element(Invariable.PAYTYPE);
		payType.setText(type);
		
		doc.setRootElement(ipayMsg);   
		ipayMsg.addContent(elecMsg);

		vendReq = new Element(Invariable.VENDREQ);

		vendReq.addContent(ref);
		vendReq.addContent(amt);
		vendReq.addContent(numTokens);
		vendReq.addContent(meter);
		vendReq.addContent(payType);
		elecMsg.addContent(vendReq);
		makeXML(doc);
		String sXML = new String(reqXML);
		String mess = "Request: " + sdf.format(date)+ sXML;
    	
		String key = env.getProperty("payment.key");
    	Byte paytype = (byte) Integer.parseInt(key);
    	Double dAmt = Double.parseDouble(amount)/100; //reset to shillings
    	tokenReq.setAmt(dAmt);
    	tokenReq.setMeterno(meterNo);
    	tokenReq.setRef(refNo);
    	tokenReq.setType(paytype);
    	tokenReq.setRequestdate(date);
    	tokenReq.setRequestxml(sXML);
    	tokenReq.setSeqnum(Integer.parseInt(seqNo));
		logfile.eventLog(mess);

	return reqXML;
	}
	
	/**
	 * function to create the initial XML document with the initial standard elements.
	 * @param client
	 * @param seqNo
	 * @param term
	 */
	private void createInitDoc(String term){
		String client, ver;
		client = env.getProperty("company.name");
        ver = env.getProperty("api.ver");
		tz = TimeZone.getTimeZone(env.getProperty("timezone.local"));
		sdf = new SimpleDateFormat(env.getProperty("time.format"));

		sdf.setTimeZone(tz);
		dtt = sdf.format(date);
		
		String name =env.getProperty("seq.counter");
		iSerialNumberService.updateLastNumber(name);
		Integer nextNum = iSerialNumberService.getLastNumber(); 
		seqNo = nextNum.toString();
		seqNo = ("00000"+seqNo).substring(seqNo.length());

        type = env.getProperty("payment.value");
        currency = env.getProperty("currency.code");
        //
		tref = generateRef();


		// ipayMsg element
		ipayMsg = new Element(Invariable.IPAY);
		ipayMsg.setAttribute(Invariable.CLIENT,client);
		ipayMsg.setAttribute(Invariable.TERM, term);
		ipayMsg.setAttribute(Invariable.SEQNUM,seqNo);
		ipayMsg.setAttribute(Invariable.TIME, dtt);
		// elecMsg element
		elecMsg = new Element(Invariable.ELECMSG);
		elecMsg.setAttribute(Invariable.VER, ver);
		

	}
	/**
	 * function creates the XML from a ByteArrayOutputStream, 
	 * omits the xml declaration and ensures it is UTF-8
	 * @param doc
	 */
	private void makeXML(Document doc){
        Transformer transformer = null;

		XMLOutputter xmlOutput = new XMLOutputter();
		
		String outputXML = xmlOutput.outputString(doc);
        Source xmlInput = new StreamSource(new StringReader(outputXML));
        ByteArrayOutputStream baos=new ByteArrayOutputStream();
        StreamResult streamOutXML = new StreamResult(baos);
		try {
			transformer = TransformerFactory.newInstance().newTransformer();
		} catch (TransformerConfigurationException e) {
			e.printStackTrace();
		} catch (TransformerFactoryConfigurationError e) {
			e.printStackTrace();
		}
        transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
        transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
        try {
			transformer.transform(xmlInput, streamOutXML);
		} catch (TransformerException e) {
			e.printStackTrace();
		}
        reqXML =baos.toByteArray();

	}
	/**
	 * 
	 */
	private String generateRef() {
		
		/**i.) The fixed length reference  number is generated as follows
		 * "last digit of current year" + "day of the year" + "hour" + "minute" +
		 *"incremental counter val". E.g on the 18 April 2018 (138th day of 2018), at 1:02
		 *pm local time, while sending the 140'th message since the client software started
		 *up, your reference number should be: "813813020140". 
		 *ii.) All values are zero padded on the left to make up a fixed 12 digit value. 
		 *iii.)The message counter does not reset to zero at the start of every minute. If the
		 *message counter reaches 9999 it simply rolls over to 0000 again.
		**/
		Calendar cal = Calendar.getInstance();
		refNo = 0.0;

		cal.setTime(date);
		cal.setTimeZone(tz);
		Integer year = cal.get(Calendar.YEAR);
		Integer hour = cal.get(Calendar.HOUR_OF_DAY);
		Integer min = cal.get(Calendar.MINUTE);
		String last_digit_of_year = year.toString().substring(3);
		Integer day_of_year = cal.get(Calendar.DAY_OF_YEAR);
		String name =env.getProperty("ref.counter");


		iSerialNumberService.updateLastNumber(name);
		Integer nextNum = iSerialNumberService.getLastNumber(); 
		log.info(nextNum);
		String s = last_digit_of_year+String.format("%03d",day_of_year)  +String.format("%02d",hour) 
		+String.format("%02d",min) + String.format("%04d",nextNum);
		log.info("diplay ref no");
		log.info(s);
		refNo = Double.parseDouble(s);
		return s;

		
	}
}
	