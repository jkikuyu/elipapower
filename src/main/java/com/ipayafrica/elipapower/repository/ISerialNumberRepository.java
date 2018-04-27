package com.ipayafrica.elipapower.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ipayafrica.elipapower.model.SerialNumber;

public interface ISerialNumberRepository<T extends SerialNumber, PK> extends JpaRepository<T, Long>{

}
