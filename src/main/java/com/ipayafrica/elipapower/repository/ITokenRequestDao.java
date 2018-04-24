package com.ipayafrica.elipapower.repository;
/**
*
* @author Jude Kikuyu
* created on 20/04/2018
* 
*/
import java.util.List;

import com.ipayafrica.elipapower.model.TokenRequest;


public interface ITokenRequestDao extends IGenericDao<TokenRequest> {
	List<TokenRequest> getAllRecords();
	TokenRequest getByTokenRequestId (Long requestId);



}
