package com.ipayafrica.elipapower.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ipayafrica.elipapower.model.ErrorCode;
import com.ipayafrica.elipapower.model.TokenRequest;

public interface IErrorCodeRepository<T extends ErrorCode,PK> extends JpaRepository<ErrorCode, Integer> {
    @Query("SELECT e FROM ErrorCode e where e.messagecode = :code") 
    ErrorCode findByMessageCode(@Param("code") String code);

}
