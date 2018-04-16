package com.ipayafrica.elipapower.util;

import java.io.StringReader;
import java.io.StringWriter;
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

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.output.XMLOutputter;
import org.springframework.stereotype.Component;

import com.ipayafrica.elipapower.Invariable;;
@Component
public class CreateXML {

	public CreateXML() {
	}
	//<ipayMsg client='IPAYAFRICA' term='00001' seqNum='0' time='".$objDateTime->
	//format('Y-m-d H:i:s O')."'><elecMsg ver='2.44'><vendReq>
	//<ref>136105500001</ref><amt cur='KES'>100</amt>
	//<numTokens>1</numTokens><meter>A12C3456789</meter>
	//<payType>cash</payType></vendReq ></elecMsg></ipayMsg>";
	
	public String buildXML(String client, String seqNo,String term, String refNo,
			String currCode, String amount, String num, String meterNo,
			String type){
		Date date = new Date();// date to be used in message
		
		
		/*format for date in date time and timezone*/
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z");
		sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
		sdf.setTimeZone(TimeZone.getTimeZone("Africa/Nairobi"));
		String dtt = sdf.format(date);
		
		//Document
		Document doc = new Document();
		// ipayMsg element
		Element ipayMsg = new Element(Invariable.IPAY);
		ipayMsg.setAttribute(Invariable.CLIENT,client);
		ipayMsg.setAttribute(Invariable.SEQNUM,seqNo);
		ipayMsg.setAttribute(Invariable.TIME, dtt);
		// elecMsg element
		Element elecMsg = new Element(Invariable.ELECMSG);
		elecMsg.setAttribute(Invariable.VER,Invariable.VERION);
		// vendreq element
		Element vendReq = new Element(Invariable.VENDREQ);
		
		//ref
		Element ref = new Element(Invariable.REF);
		
		ref.setText(refNo);
		
		Element amt = new Element(Invariable.AMT);
		amt.setAttribute(Invariable.CUR, currCode);
		
		amt.setText(amount);
		
		Element numTokens = new Element(Invariable.NUMTOKEN);
		
		numTokens.setText(num);
		
		Element meter = new Element(Invariable.METER);
		
		meter.setText(meterNo);
		
		Element payType = new Element(Invariable.PAYTYPE);
		payType.setText(type);
		
		doc.setRootElement(ipayMsg);   
		ipayMsg.addContent(elecMsg);
		elecMsg.addContent(vendReq);

		vendReq.addContent(ref);
		vendReq.addContent(amt);
		vendReq.addContent(numTokens);
		vendReq.addContent(meter);
		vendReq.addContent(payType);

		XMLOutputter xmlOutput = new XMLOutputter();
		String xml = xmlOutput.outputString(doc);
        Source xmlInput = new StreamSource(new StringReader(xml));
        StreamResult streamOutXML = new StreamResult(new StringWriter());
        Transformer transformer = null;
		try {
			transformer = TransformerFactory.newInstance().newTransformer();
		} catch (TransformerConfigurationException e) {
			e.printStackTrace();
		} catch (TransformerFactoryConfigurationError e) {
			e.printStackTrace();
		}
        transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
        try {
			transformer.transform(xmlInput, streamOutXML);
		} catch (TransformerException e) {
			e.printStackTrace();
		}
		System.out.println(streamOutXML);
		return streamOutXML.toString();
	}
}
	