package com.ipayafrica.elipapower.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ipayafrica.elipapower.model.TokenResponse;

public interface ITokenResponseRepository<T extends TokenResponse,PK> extends JpaRepository<TokenResponse, Long> {

	@Query("SELECT t FROM TokenResponse t where t.status = :status")
	public List<TokenResponse> findAllFailedRequests(@Param("status") Byte status);

}
