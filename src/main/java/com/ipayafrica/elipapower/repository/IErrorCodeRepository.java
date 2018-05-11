package com.ipayafrica.elipapower.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ipayafrica.elipapower.model.ErrorCode;

public interface IErrorCodeRepository extends JpaRepository<ErrorCode, Integer> {
    @Query(value = "SELECT e.errorcodeid FROM errorcode e where e.messagecode = :code",nativeQuery = true) 
    int findByMessageCode(@Param("code") String code);

}
