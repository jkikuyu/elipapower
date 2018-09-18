package com.ipayafrica.elipapower.service;

import com.ipayafrica.elipapower.model.SerialNumber;

public interface ISerialNumberService extends IGenericService<SerialNumber,Integer>{
	
	int updateLastNumber(String name);
	int getLastNumber();
	SerialNumber getSerialNumber(String name);
}
