package com.ipayafrica.elipapower.service;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.ipayafrica.elipapower.model.TokenRequest;

public interface ITokenRequestService extends IGenericService<TokenRequest,Integer>{
	List<TokenRequest> getAllRecords();
	TokenRequest getByTokenRequestId (Integer requestId);
	TokenRequest findTokenRequestByRef(Double ref);
	TokenRequest findTokenRequestByMeterNo(String meterno);
	List<TokenRequest> findFailedRequests(Byte status, Date requestdate);
	int countTokenRequestByOref(Double ref);
	TokenRequest findTokenNotPrinted(String meterno);
	Page<Date> findFirstVendRevRequest(Double oref, Pageable pageable);
	List<Date> findFirstVendRevRequest(Double oref);


}
