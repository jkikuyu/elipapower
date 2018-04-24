package com.ipayafrica.elipapower.repository.hibernate;

import java.util.List;

import com.ipayafrica.elipapower.model.TokenRequest;
import com.ipayafrica.elipapower.repository.ITokenRequestDao;

public class TokenRequestDaoHibernate extends GenericDaoHibernate<TokenRequest, Long> implements ITokenRequestDao{

	@Override
	public TokenRequest getByTokenRequestId(Long requestId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TokenRequest> getAllRecords() {
		// TODO Auto-generated method stub
		return findAll();
	}

}
