package com.ipayafrica.elipapower.service;

import java.util.List;

import com.ipayafrica.elipapower.model.TokenRequest;
import com.ipayafrica.elipapower.model.TokenResponse;

public interface ITokenResponseService extends IGenericService<TokenResponse,Long>{
	List<TokenResponse> getAllRecords();
	TokenResponse getByTokenResponseId (Long responsetId);
	List<TokenResponse>getAllPendingRecords();

}
