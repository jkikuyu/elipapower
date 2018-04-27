package com.ipayafrica.elipapower.service;

import java.util.List;

import com.ipayafrica.elipapower.model.TokenRequest;

public interface ITokenRequestService extends IGenericService<TokenRequest,Long>{
	List<TokenRequest> getAllRecords();
	TokenRequest getByTokenRequestId (Long requestId);


}
