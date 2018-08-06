package com.ipayafrica.elipapower.webapp.controller;
import java.util.HashMap;
import java.util.Optional;

/**
 * 
 * @author Jude Kikuyu
 * The vendcontroller receives a request for electricity token. Details received are the 
 * meter no, amount and unique reference. 
 * An XML document is created as per the specification (Merchant to Ipay Interface - Prepaid
 * Electricity Version: 2.44b
 * <ipayMsg client='IPAYAFRICA' term='00001' seqNum='0' time='".$objDateTime->
 * format('Y-m-d H:i:s O')."'><elecMsg ver='2.44'><vendReq>
 * <ref>136105500001</ref><amt cur='KES'>100</amt><numTokens>1</numTokens>
 * <meter>A12C3456789</meter><payType>cash</payType></vendReq ></elecMsg></ipayMsg>"
	
 * The XML is passed to the token request component.
 */
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ipayafrica.elipapower.model.Token;
import com.ipayafrica.elipapower.model.TokenRequest;
import com.ipayafrica.elipapower.model.TokenResponse;
import com.ipayafrica.elipapower.service.ITokenRequestService;
import com.ipayafrica.elipapower.service.ITokenResponseService;
import com.ipayafrica.elipapower.util.CreateXML;
import com.ipayafrica.elipapower.util.LogFile;
import com.ipayafrica.elipapower.util.RequestToken;
import com.ipayafrica.elipapower.util.ResponseToken;

@RestController
public class VendController extends CommonRequestUtil{
    protected final transient Log log = LogFactory.getLog(getClass());

    private CreateXML createxml;
    private TokenRequest tokenRequest = null;
	private boolean isEmptyMeterNo =false, isEmptyAmount=false, isEmptyRef=false, isEmptyDemo=false;
	@Autowired
    private RequestToken requestToken;
   
	
	@Autowired
	Environment env;
	
	public VendController() {
	}
	public RequestToken getTokenRequest() {
		return requestToken;
	}

	public CreateXML getCreatexml() {
		return createxml;
	}

	@Autowired
	public void setCreatexml(CreateXML createxml) {
		this.createxml = createxml;
	}

	@Autowired
	public void setTokenRequest(RequestToken tokenRequest) {
		this.requestToken = tokenRequest;
	}
	@Autowired
	public void setiTokenRequestService(ITokenRequestService iTokenRequestService) {
		this.iTokenRequestService = iTokenRequestService;
	}
	@Autowired
	private ITokenRequestService iTokenRequestService;

	@Autowired
	private LogFile logfile;

	@Autowired
	private ITokenResponseService iTokenResponseService;

	@RequestMapping(value ="/tokenreq", method=RequestMethod.POST, produces={"application/json"})
	@ResponseBody
	public String getElectricity(@RequestBody Token token){
	
	String messJSON = null;

	ObjectMapper objectMapper = new ObjectMapper();

	String meterNo = token.getMeterno();
	
	String amount = token.getAmount();
	Integer dAmt = Integer.parseInt(amount)*100; // convert to cents
	Integer demo = Integer.parseInt(token.getDemo());
	
	amount = dAmt.toString();
	String refNo = token.getRefno();
	HashMap<String,Object> messResponse = null;
	Byte status;

	if(meterNo==null || meterNo.isEmpty()) {
		isEmptyMeterNo = true;

	}
	else if (amount==null || amount.isEmpty()){
		isEmptyAmount =true; 
	}
	else if (refNo==null || refNo.isEmpty()) {
		isEmptyRef =true;
	}
	else if (demo == null || demo>1) {
		isEmptyDemo =true;
	}
	log.info("amount:"+amount);
	messResponse = new HashMap<String,Object>();
	String errResponse = null;

	if (!isEmptyMeterNo && !isEmptyAmount && !isEmptyRef &&!isEmptyDemo) {
		
		status = 1;
		String term;
		if(demo == 1) {
			term = env.getProperty("dummy.id");

		}
		else {
			term = env.getProperty("company.id");
		}
		if (term==null) {
			errResponse = "internal server error";
			messResponse.put("error",errResponse);
			messResponse.put("status", "0");
			try {
				messJSON = objectMapper.writeValueAsString(messResponse);
			} catch (JsonProcessingException e) {
				e.printStackTrace();
			}
		}
		else {

			tokenRequest = iTokenRequestService.findTokenRequestByMeterNo(meterNo);

			Optional<TokenRequest> optReq = Optional.ofNullable(tokenRequest);
			if(optReq.isPresent()){
					errResponse = "Meter " + meterNo + " being processed. Please try later";
					messResponse.put("response",errResponse);
					messResponse.put("status", "0");
					try {
						messJSON = objectMapper.writeValueAsString(messResponse);
					} catch (JsonProcessingException e) {
						// TODO Auto-generated catch block
						logfile.eventLog(e.getMessage());
					}
				
				
			}

			else {
				tokenRequest = iTokenRequestService.findTokenNotPrinted(meterNo);
				optReq = Optional.ofNullable(tokenRequest);
				if(optReq.isPresent()){
					Double ref = tokenRequest.getOref();
					messJSON = iTokenResponseService.getJsonResponse(ref);
					Byte receipt = 1;
					tokenRequest.setReceipt(receipt);
					tokenRequest.setStatus(status);
					iTokenRequestService.save(tokenRequest);
					
				}
				else {
					messJSON = makeVendRequest(meterNo, amount,term, refNo, status);
					
	
/*					tokenRequest = new TokenRequest();
	
					byte[] reqXML= createxml.buildXML( meterNo, amount,tokenRequest, term );
					tokenRequest.setClientref(refNo);
					Double oref = tokenRequest.getRef();
					tokenRequest.setStatus(status);
					tokenRequest.setRepcount(0);
					tokenRequest.setOref(oref);
					iTokenRequestService.save(tokenRequest);
					log.info("meter No:" + meterNo);
				
					log.info("begin make request....");
					messResponse = requestToken.makeRequest(reqXML,meterNo,term);
					tokenResponse= responseToken.getTokenResponse();
					Optional<TokenResponse> optRes = Optional.ofNullable(tokenResponse);
					if (optRes.isPresent() && messResponse !=null) {
					
						tokenResponse.setMeterno(meterNo);
						
						
						messResponse.put("ref", token.getRefno());
						try {
							messJSON = objectMapper.writeValueAsString(messResponse);
							log.info("response:"+ messJSON);
				
							
						} catch (JsonProcessingException e) {
							// TODO Auto-generated catch block
							logfile.eventLog(e.getMessage());
						}
						log.info(messJSON);
				
						tokenResponse.setJsonresponse(messJSON);
				
						iTokenResponseService.save(tokenResponse);
						Byte receipt = 1;
						tokenRequest.setReceipt(receipt);
						iTokenRequestService.save(tokenRequest);
					}
					else {
						status = 2;
						tokenRequest.setStatus(status);
						iTokenRequestService.save(tokenRequest);
					}
					*/
					
				}
			}
		}
	}
	else {
		errResponse = null;
		errResponse = isEmptyMeterNo? "Meter No is empty or  key used is wrong. ":"";
		errResponse +=isEmptyRef? "Reference Number is empty or key used is wrong":"";

		errResponse +=isEmptyAmount ? "Amount is empty or key used is wrong":"";
		errResponse +=isEmptyDemo ? "Demo is empty or key used is wrong":"";

		messResponse.put("error",errResponse);
		messResponse.put("status", "0");
		isEmptyMeterNo =false;
		isEmptyAmount=false;
		isEmptyRef=false;
		isEmptyDemo=false;
		try {
			messJSON = objectMapper.writeValueAsString(messResponse);
		} catch (JsonProcessingException e) {
			logfile.eventLog(e.getMessage());
		}

	}

	
	
	return messJSON;
	}


}

