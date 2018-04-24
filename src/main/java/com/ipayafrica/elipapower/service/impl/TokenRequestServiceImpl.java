package com.ipayafrica.elipapower.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ipayafrica.elipapower.model.TokenRequest;
import com.ipayafrica.elipapower.repository.ITokenRequestDao;
import com.ipayafrica.elipapower.service.ITokenRequestService;
@Service("tokenRequestService")

public class TokenRequestServiceImpl extends GenericServiceImpl<TokenRequest, Long>
implements ITokenRequestService{
	private ITokenRequestDao iTokenRequestDao = null;
	@Autowired
	public void setiTokenRequestDao(ITokenRequestDao iTokenRequestDao) {
		this.dao = iTokenRequestDao;

		this.iTokenRequestDao = iTokenRequestDao;
	}
	@Override
	public List<TokenRequest> getAllRecords() {
		// TODO Auto-generated method stub
		return iTokenRequestDao.getAllRecords();
	}

	@Override
	public TokenRequest getByTokenRequestId(Long accountId) {
		// TODO Auto-generated method stub
		return null;
	}



}
