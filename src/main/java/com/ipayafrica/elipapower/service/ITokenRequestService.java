package com.ipayafrica.elipapower.service;

import java.util.Date;
import java.util.List;

import com.ipayafrica.elipapower.model.TokenRequest;

public interface ITokenRequestService extends IGenericService<TokenRequest,Long>{
	List<TokenRequest> getAllRecords();
	TokenRequest getByTokenRequestId (Long requestId);
	TokenRequest findTokenRequestByRef(Double ref);
	List<TokenRequest> findFailedRequests(Byte status, Date requestdate);
	int countTokenRequestByOref(Double ref);


}
