package com.ipayafrica.elipapower.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.Repository;

import com.ipayafrica.elipapower.model.ErrorCode;
import com.ipayafrica.elipapower.repository.IErrorCodeRepository;
import com.ipayafrica.elipapower.service.IErrorCodeService;

public class ErrorCodeService implements IErrorCodeService {
	
	@Autowired
	IErrorCodeRepository iErrorCodeRepository;
	@Override
	public List<ErrorCode> getAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ErrorCode get(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ErrorCode save(ErrorCode object) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void remove(long Long) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public ErrorCode getByErrorCodeID(Long errorCodeId) {
		// TODO Auto-generated method stub
		return iErrorCodeRepository.getOne(errorCodeId);
	}

	@Override
	public int getByMessageCode(String messageCode) {
		return iErrorCodeRepository.findByMessageCode(messageCode);
	}

}
