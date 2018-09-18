package com.ipayafrica.elipapower.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ipayafrica.elipapower.model.TokenRequest;

@Repository
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
	
	@Query(value = "SELECT requestdate FROM tokenrequest t where t.status = 1 AND  t.oref = ?1", 
			countQuery = "SELECT requestdate FROM tokenrequest t where t.status = 1 AND  t.oref = ?1",nativeQuery=true)
	public Page<Date> findFirstVendRevRequest(Double oref,Pageable pageable);
	@Query("SELECT requestdate FROM TokenRequest t where t.status = 1 AND  t.oref = :oref") 
	public List<Date> findFirstVendRevRequest(@Param("oref") Double oref);

}
