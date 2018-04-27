package com.ipayafrica.elipapower.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ipayafrica.elipapower.model.TokenRequest;


public interface ITokenRequestRepository<T extends TokenRequest,PK> extends JpaRepository<T, Long>{

}
