package com.ipayafrica.elipapower.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ipayafrica.elipapower.model.TokenResponse;

public interface ITokenResponseRepository<T extends TokenResponse,PK> extends JpaRepository<TokenResponse, Long> {

}
