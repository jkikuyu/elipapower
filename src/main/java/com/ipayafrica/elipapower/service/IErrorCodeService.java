package com.ipayafrica.elipapower.service;

import com.ipayafrica.elipapower.model.ErrorCode;

public interface IErrorCodeService extends IGenericService<ErrorCode, Integer> {
	ErrorCode getByErrorCodeID (Integer errorCodeId);
	int getByMessageCode (String messageCode);
}
