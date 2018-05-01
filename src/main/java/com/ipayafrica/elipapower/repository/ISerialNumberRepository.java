package com.ipayafrica.elipapower.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.ipayafrica.elipapower.model.SerialNumber;

public interface ISerialNumberRepository extends JpaRepository<SerialNumber, Long>{
	@Modifying
	@Query(value="UPDATE serialnumber SET value = LAST_INSERT_ID(value + 1) WHERE name =?1", nativeQuery = true)

	int updateNextNumber(String name);

	@Query(value = "SELECT LAST_INSERT_ID()", nativeQuery = true)
	int getLastNumber();

	SerialNumber findOneByName(String name);



}