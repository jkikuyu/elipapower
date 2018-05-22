package com.ipayafrica.elipapower.util;

import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.ipayafrica.elipapower.model.TokenRequest;
import com.ipayafrica.elipapower.model.TokenResponse;
import com.ipayafrica.elipapower.service.ITokenResponseService;

@Component
public class FailedRequestBean {
	ITokenResponseService iTokenResponseService;
	public FailedRequestBean() {
		
	}
	@Scheduled(cron = "45 * * * * *" )
	public void cronJob(){
		Byte status = 2;
		List <TokenResponse>failedTokenRequests = iTokenResponseService.findFailedRequests(status);
		if (failedTokenRequests!=null)
			for(TokenResponse tokenResponse:failedTokenRequests){
				Double refNo = tokenResponse.getRef();
				TokenRequest tokenRequest = new TokenRequest();
				
				
			}
	}
}

