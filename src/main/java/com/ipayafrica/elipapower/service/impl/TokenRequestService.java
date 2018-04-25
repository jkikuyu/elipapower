package com.ipayafrica.elipapower.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.ipayafrica.elipapower.model.TokenRequest;
import com.ipayafrica.elipapower.repository.ITokenRequestRepository;
import com.ipayafrica.elipapower.service.ITokenRequestService;

public class TokenRequestService implements ITokenRequestService{

	@Autowired
	ITokenRequestRepository tokenRequestRepository;

	@Override
	public List<TokenRequest> getAll() {
		List <TokenRequest> tokenRequests = new ArrayList<>();
		tokenRequestRepository.findAll().forEach(tokenRequests::add);

		// TODO Auto-generated method stub
		
		return tokenRequests;
	}

	public void save(TokenRequest tokenRequest) {
		tokenRequestRepository.save(tokenRequest);
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
		// TODO Auto-generated method stub
		return null;
	}

		 


}
