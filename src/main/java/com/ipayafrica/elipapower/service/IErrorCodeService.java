package com.ipayafrica.elipapower.service;

import com.ipayafrica.elipapower.model.ErrorCode;

public interface IErrorCodeService extends IGenericService<ErrorCode, Long> {
	ErrorCode getByErrorCodeID (Long errorCodeId);
	int getByMessageCode (String messageCode);
}
