package com.ipayafrica.elipapower.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ipayafrica.elipapower.model.SerialNumber;
@Repository
public interface ISerialNumberRepository extends JpaRepository<SerialNumber, Long>{
	@Modifying
	@Query(value="UPDATE serialnumber set value = CASE WHEN value = 99999 AND name = 'seqcount' THEN 1 ELSE LAST_INSERT_ID(value + 1) END WHERE name=?1", nativeQuery = true)

	int updateNextNumber(String name);

	@Query(value = "SELECT LAST_INSERT_ID()", nativeQuery = true)
	int getLastNumber();

	SerialNumber findOneByName(@Param("name") String name);
}
