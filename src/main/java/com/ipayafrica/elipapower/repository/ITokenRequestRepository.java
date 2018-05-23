package com.ipayafrica.elipapower.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ipayafrica.elipapower.model.TokenRequest;


public interface ITokenRequestRepository<T extends TokenRequest,PK> extends JpaRepository<T, Long>{

	@Query("SELECT t FROM TokenRequest t where t.ref = :ref")
	public TokenRequest findTokenRequestByRef(@Param("ref") Double ref);
	
	@Query("SELECT t FROM TokenRequest t where t.status = :status")
	public List<TokenRequest> findAllFailedRequests(@Param("status") Byte status);

}
