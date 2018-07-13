package com.ipayafrica.elipapower.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ipayafrica.elipapower.model.TokenResponse;

public interface ITokenResponseRepository<T extends TokenResponse,PK> extends JpaRepository<TokenResponse, Long> {
	
	@Query("SELECT t.jsonresponse FROM TokenResponse t where t.ref = :ref")
	public String getJsonResponse(@Param("ref") Double ref);


}
