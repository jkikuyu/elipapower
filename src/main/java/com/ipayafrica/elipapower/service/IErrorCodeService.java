package com.ipayafrica.elipapower.service;

import com.ipayafrica.elipapower.model.ErrorCode;

public interface IErrorCodeService extends IGenericService<ErrorCode, Integer> {
	ErrorCode findByMessageCode(String messageCode);
}
