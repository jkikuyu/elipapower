package com.ipayafrica.elipapower.util;

import java.text.DecimalFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ipayafrica.elipapower.model.TokenRequest;
import com.ipayafrica.elipapower.model.TokenResponse;
import com.ipayafrica.elipapower.service.ITokenRequestService;
import com.ipayafrica.elipapower.service.ITokenResponseService;

@Component
public class FailedRequestBean {
    protected final transient Log log = LogFactory.getLog(getClass());

	@Autowired
	ITokenResponseService iTokenResponseService;
	@Autowired
	CreateXML createxml;
	@Autowired
	ITokenRequestService iTokenRequestService;
	@Autowired
	RequestToken requestToken;
	@Autowired
	ResponseToken responseToken;
	@Autowired
	Environment env;
	
	@Autowired
	private LogFile logfile;


	public FailedRequestBean() {
		
	}
	//@Scheduled (cron = "45 * * * * *")
	@Scheduled(initialDelay=5000, fixedDelay=15000 )
	public void cronJob(){
		Byte status = 2;
		HashMap<String,Object> messResponse = null;
		String messJSON = null;
		ObjectMapper objectMapper = new ObjectMapper();
		Date requestdate = new Date();
		log.info("date before"+requestdate.toString());
		requestdate.setTime(requestdate.getTime() - 30000);
		log.info("date after"+requestdate.toString());
		DecimalFormat df = new DecimalFormat(".#");

		List <TokenRequest>failedTokenRequests = iTokenRequestService.findFailedRequests(status,requestdate);
		if (failedTokenRequests!=null) {
			for(TokenRequest tokenRequest:failedTokenRequests){
				TokenResponse tokenResponse = new TokenResponse();
				String term = env.getProperty("dummy.id");
		        String type = env.getProperty("payment.value");

				Double dAmt = tokenRequest.getAmt() * 100;
				Double ref = tokenRequest.getRef();
				TokenRequest treq = new TokenRequest();
				/* format double 
				 * store in string buffer
				 * remove decimal part
				 */

				String sref = df.format(ref);
				Double oref = tokenRequest.getOref();
				int repcount = iTokenRequestService.countTokenRequestByOref(oref);	
			    Date date = tokenRequest.getRequestdate();

				
			    StringBuffer refNo = new StringBuffer(sref);
			    int pos = refNo.indexOf(".");
			    int end = refNo.length();
			    refNo.delete(pos,end);
			    String s = refNo.toString();
			    oref = Double.parseDouble(s);

				String amount = String.valueOf(dAmt.intValue());
				treq.setAmt(dAmt);
				treq.setRef(ref);
				treq.setRepcount(repcount);
				treq.setAmount(amount);
				treq.setOref(oref);
				treq.setRequestdate(date);
				String key = env.getProperty("payment.key");
		    	Byte paytype = (byte) Integer.parseInt(key);

				treq.setType(paytype);
				status = 1;
				treq.setStatus(status);
				//amount = dAmt.toString();
				String meterNo = tokenRequest.getMeterno();
				

				byte[] reqXML= createxml.buildXML( meterNo, 3,treq, term );
				log.info("meter No:" + meterNo);
			
			
				log.info("begin make request....");
				messResponse = new HashMap<String,Object>();
				iTokenRequestService.save(treq);

				messResponse = requestToken.makeRequest(reqXML,meterNo, term);
				if (messResponse == null) {
					logfile.eventLog("No message returned for request:-" + new String(reqXML));
				}

				else{
					
					String st=(String) messResponse.get("status");
					if(st.equals("3")) {
							status = 3;
							treq.setStatus(status);
							iTokenRequestService.save(treq);

							System.out.println("no socket available");
						}
					else {

						logfile.eventLog("response: " + new String(reqXML));
						tokenResponse= responseToken.getTokenResponse();
						tokenResponse.setMeterno(meterNo);
						messResponse.put("ref", tokenRequest.getClientref());
						try {
							messJSON = objectMapper.writeValueAsString(messResponse);
							log.info("response:"+ messJSON);
		
							
						} catch (JsonProcessingException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
						log.info(messJSON);
		
						tokenResponse.setJsonresponse(messJSON);
		
						iTokenResponseService.save(tokenResponse);
						tokenRequest.setStatus(status);
						iTokenRequestService.save(tokenRequest);
					}
				}
				
				
			}
		}
	}
}
