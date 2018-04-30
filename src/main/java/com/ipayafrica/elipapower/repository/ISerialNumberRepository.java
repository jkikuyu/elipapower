package com.ipayafrica.elipapower.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.ipayafrica.elipapower.model.SerialNumber;

public interface ISerialNumberRepository<T extends SerialNumber, PK> extends JpaRepository<T, Long>{
	@Modifying
	@Query(value="UPDATE SerialNumber SET value = LAST_INSERT_ID(value + 1) WHERE name =?1", nativeQuery = true)

	int updateNextNumber(String name);

	@Query("SELECT LAST_INSERT_ID()")
	int getLastNumber();

	SerialNumber findOneByName(String name);



}
