package com.ipayafrica.elipapower.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ipayafrica.elipapower.model.TokenRequest;


public interface ITokenRequestRepository extends JpaRepository<TokenRequest, Long>{

}
