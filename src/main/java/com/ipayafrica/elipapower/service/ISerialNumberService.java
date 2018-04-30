package com.ipayafrica.elipapower.service;

import com.ipayafrica.elipapower.model.SerialNumber;

public interface ISerialNumberService extends IGenericService<SerialNumber,Long>{
	
	int updateLastNumber(String Name);
	int getLastNumber();

}
