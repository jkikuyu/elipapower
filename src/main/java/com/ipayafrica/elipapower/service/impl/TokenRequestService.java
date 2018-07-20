package com.ipayafrica.elipapower.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.ipayafrica.elipapower.model.TokenRequest;
import com.ipayafrica.elipapower.repository.ITokenRequestRepository;
import com.ipayafrica.elipapower.service.ITokenRequestService;

@Service
public class TokenRequestService implements ITokenRequestService{

	@Autowired
	ITokenRequestRepository<TokenRequest, Long> tokenRequestRepository;

	@Override
	public List<TokenRequest> getAll() {
		List <TokenRequest> tokenRequests = new ArrayList<>();
		tokenRequestRepository.findAll().forEach(tokenRequests::add);

	
		return tokenRequests;
	}

	public TokenRequest save(TokenRequest tokenRequest) {
		tokenRequestRepository.save(tokenRequest);
		return tokenRequest;
	}

	@Override
	public void remove(long Long) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<TokenRequest> getAllRecords() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TokenRequest getByTokenRequestId(Long requestId) {
		return null;
	}

	@Override
	public TokenRequest get(Long id) {
		return null;
	}

	@Override
	public TokenRequest findTokenRequestByRef(Double ref) {
		return tokenRequestRepository.findTokenRequestByRef(ref);
	}
	public TokenRequest findTokenRequestByMeterNo(String meterno) {
		return tokenRequestRepository.findTokenRequestByMeterNo(meterno);
	}


	@Override
	public List<TokenRequest> findFailedRequests(Byte status, Date requestdate) {
		return tokenRequestRepository.findAllFailedRequests(status,requestdate);
	}
		 
	@Override
	public int countTokenRequestByOref(Double ref) {
		return tokenRequestRepository.countTokenRequestByOrigRef(ref);
	}
	public TokenRequest findTokenNotPrinted(String meterno) {
		return tokenRequestRepository.findTokenNotPrinted(meterno);
	}

	@Override
	public Page<Date> findFirstVendRevRequest(Double oref, Pageable pageable) {
		// TODO Auto-generated method stub
		return tokenRequestRepository.findFirstVendRevRequest(oref,pageable);
	}
	public List<Date> findFirstVendRevRequest(Double oref){
		return tokenRequestRepository.findFirstVendRevRequest(oref);
	}


}
