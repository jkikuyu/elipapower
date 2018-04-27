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
import java.text.SimpleDateFormat;
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
@PropertySource("classpath:application.properties")

@Component

public class CreateXML {
    protected final transient Log log = LogFactory.getLog(getClass());

	private Element ipayMsg = null, elecMsg=null;
	byte[] reqXML=null;
	private String seqNo, refNo, num, currency, type, client, term, ver, dtt;
	int repeat = 0;
	
	@Autowired
	private Environment env;
	//Document
	Document doc=null;
	@Autowired
	LogFile logfile;
	


	//constructor
	public CreateXML() {
		seqNo = "1";
		refNo = "136105500001";
		currency = "KES";
		num  = "1";


		Date date = new Date();// date to be used in message
        

		/*format for date in date time and timezone*/
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z");
		sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
		sdf.setTimeZone(TimeZone.getTimeZone("Africa/Nairobi"));
		dtt = sdf.format(date);

	}


	/**
	 *<ipayMsg client="IPAYAFRICA" term="00001" seqNum="2" time="2018­-04­-16 10:57:00 +0300"> 
	 * <elecMsg ver="2.44"> <custInfoReq><ref>136105700003</ref><meter>123456789</meter>
	 * </custInfoReq></elecMsg></ipayMsg>

	 * @param meter
	 * @return
	 */
	public byte[] buildXML(String meterNo,int reqtype){
		reqXML= null;

		doc = new Document();
		createInitDoc();
		client = env.getProperty("company.name");
		term = env.getProperty("company.id");
        ver = env.getProperty("api.ver");
        type = env.getProperty("payment.type");
        currency = env.getProperty("currency.code");
		Element ref = new Element(Invariable.REF);
		ref.setText(refNo);
		
		
		Element meter = new Element(Invariable.METER);
		meter.setText(meterNo);
		doc.setRootElement(ipayMsg);   
		ipayMsg.addContent(elecMsg);
		

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
			String oTime = dtt; //to obtain origin time of first reversal advice
			Element vendRevReq =null;
			vendRevReq = new Element(Invariable.VENDREVREQ);
			if(repeat >1){
				repeat++;
				
				Element repCount = new Element(Invariable.REPCOUNT);
				repCount.setText(Integer.toString(repeat));
				Element orgTime = new Element(Invariable.ORIGTIME);
				orgTime.setText(oTime);
				vendRevReq.addContent(repCount);
				vendRevReq.addContent(orgTime);
			}
			vendRevReq.addContent(ref);
			//if repeat request for reversal then add the or
			if(repeat >1){
				Element origRef = new Element(Invariable.ORIGREF);
				origRef.setText(refNo);
				vendRevReq.addContent(origRef);
			}
			vendRevReq.addContent(meter);
			elecMsg.addContent(vendRevReq);
		}
		makeXML(doc);
		logfile.eventLog(reqXML.toString());
		return reqXML;
	}
	/**
	 * 

	 * @param meterNo
	 * @return
	 */
	public byte[] buildXML(String meterNo, String amount,TokenRequest tokenReq){

        reqXML = null;

        doc = new Document();
        
    	// vendreq element
    	Element vendReq = null;
    	
    	tokenReq.setAmount(Double.parseDouble(amount));
    	tokenReq.setMeterno(meterNo);
    	tokenReq.setRef(ref);
    	tokenReq.setType(type);
		createInitDoc();
		//ref
		Element ref = new Element(Invariable.REF);
		
		ref.setText(refNo);
		
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
		elecMsg.addContent(vendReq);
		vendReq = new Element(Invariable.VENDREQ);

		vendReq.addContent(ref);
		vendReq.addContent(amt);
		vendReq.addContent(numTokens);
		vendReq.addContent(meter);
		vendReq.addContent(payType);
		
		makeXML(doc);
	return reqXML;
	}
	
	/**
	 * 
	 * @param client
	 * @param seqNo
	 * @param term
	 */
	private void createInitDoc(){
        

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
	 * 
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
	private void generateRef() {
		
		/**i.) The fixed length reference  number is generated as follows
		 * "last digit of current year" + "day of the year" + "hour" + "minute" +
		 *"incremental counter val". E.g on the 18 April 2018 (138th day of 2018), at 1:02
		 *pm local time, while sending the 140'th message since the client software started
		 *up, your reference number should be: "813813020140". 
		 *ii.) All values are zero padded on the left to make up a fixed 12 digit value. 
		 *iii.)The message counter does not reset to zero at the start of every minute. If the
		 *message counter reaches 9999 it simply rolls over to 0000 again.
		**/
		dtt
		ref
	}
}
	