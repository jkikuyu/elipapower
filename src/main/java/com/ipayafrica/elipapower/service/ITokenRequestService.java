package com.ipayafrica.elipapower.service;

import java.util.List;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Service;

import com.ipayafrica.elipapower.model.TokenRequest;

@Service
@ComponentScan({"com.ipayafrica.elipapower"})
public interface ITokenRequestService extends IGenericService<TokenRequest>{

	List<TokenRequest> getAllRecords();
	TokenRequest getByTokenRequestId (Long requestId);


}
