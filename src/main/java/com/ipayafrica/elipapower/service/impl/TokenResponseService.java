package com.ipayafrica.elipapower.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ipayafrica.elipapower.model.TokenResponse;
import com.ipayafrica.elipapower.repository.ITokenResponseRepository;
import com.ipayafrica.elipapower.service.ITokenResponseService;

@Service
public class TokenResponseService implements ITokenResponseService{

	@Autowired
	ITokenResponseRepository<TokenResponse, Long> tokenResponseRepository;

	@Override
	public List<TokenResponse> getAll() {
		List <TokenResponse> tokenResponses = new ArrayList<>();
		tokenResponseRepository.findAll().forEach(tokenResponses::add);

	
		return tokenResponses;
	}


	public TokenResponse save(TokenResponse tokenResponse) {
		tokenResponseRepository.save(tokenResponse);
		return tokenResponse;
	}

	@Override
	public void remove(long Long) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<TokenResponse> getAllRecords() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TokenResponse getByTokenResponseId(Long requestId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TokenResponse get(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TokenResponse> getAllPendingRecords() {
		// TODO Auto-generated method stub
		return null;
	}
	public String getJsonResponse(Double ref) {
		return tokenResponseRepository.getJsonResponse(ref);
	}


		 


}
