package com.ipayafrica.elipapower.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ipayafrica.elipapower.model.ErrorCode;

public interface IErrorCodeRepository extends JpaRepository<ErrorCode, Long> {
    @Query("SELECT e.errorcodeid FROM ErrorCode e where t.messagecode = :code") 
    int findByMessageCode(@Param("code") String code);

}
