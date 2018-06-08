package com.ipayafrica.elipapower.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ipayafrica.elipapower.model.ErrorCode;
import com.ipayafrica.elipapower.repository.IErrorCodeRepository;
import com.ipayafrica.elipapower.service.IErrorCodeService;
@Service
public class ErrorCodeService implements IErrorCodeService {
	
	@Autowired
	IErrorCodeRepository iErrorCodeRepository;
	@Override
	public List<ErrorCode> getAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ErrorCode get(Integer id) {
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
	public ErrorCode findByMessageCode( String messageCode) {
		// TODO Auto-generated method stub
		return iErrorCodeRepository.findByMessageCode(messageCode);
	}


/*	@Override
	public ErrorCode getByMessageCode(String messageCode) {
		return iErrorCodeRepository.findByMessageCode(messageCode);
	}
*/
}
