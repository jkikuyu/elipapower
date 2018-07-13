package com.ipayafrica.elipapower.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ipayafrica.elipapower.model.TokenRequest;


public interface ITokenRequestRepository<T extends TokenRequest,PK> extends JpaRepository<T, Long>{

	@Query("SELECT t FROM TokenRequest t where t.ref = :ref")
	public TokenRequest findTokenRequestByRef(@Param("ref") Double ref);
	
	@Query("SELECT t FROM TokenRequest t where t.status = :status and t.requestdate <= :requestdate")
	public List<TokenRequest> findAllFailedRequests(@Param("status") Byte status,@Param("requestdate") Date requestdate);
	
	@Query("SELECT Count(t.oref) as repcount FROM TokenRequest t where t.status = 1 AND t.ref <> t.oref AND t.oref= :ref")
	public int countTokenRequestByOrigRef(@Param("ref") Double ref);
	
	@Query("SELECT t FROM TokenRequest t where t.status = 2 AND t.meterno = :meterno")
	public TokenRequest findTokenRequestByMeterNo(@Param("meterno") String meterno);

	@Query("SELECT t FROM TokenRequest t where t.status = 1 and t.receipt=0 AND t.ref = t.oref AND t.meterno = :meterno")
	public TokenRequest findTokenNotPrinted(@Param("meterno") String meterno);
}
