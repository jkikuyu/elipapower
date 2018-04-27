package com.ipayafrica.elipapower.service;

import org.springframework.beans.factory.annotation.Autowired;

import com.ipayafrica.elipapower.model.SerialNumber;
import com.ipayafrica.elipapower.repository.ISerialNumberRepository;

public interface ISerialNumberService extends IGenericService<SerialNumber,Long>{
	@Autowired
	ISerialNumberRepository<SerialNumber, Long> iSerialNumberRepository=null;


}
