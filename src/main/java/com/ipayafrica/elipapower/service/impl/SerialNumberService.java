package com.ipayafrica.elipapower.service.impl;
//	https://theodoreyoung.wordpress.com/2011/04/14/jpa-counters-and-sequences/

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ipayafrica.elipapower.model.SerialNumber;
import com.ipayafrica.elipapower.repository.ISerialNumberRepository;
import com.ipayafrica.elipapower.service.ISerialNumberService;
@Service
@Transactional
public class SerialNumberService implements ISerialNumberService {
	
	@Autowired
	private ISerialNumberRepository iSerialNumberRepository;

	@Override
	public List<SerialNumber> getAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void remove(long Long) {
		// TODO Auto-generated method stub
	}

	@Override
	public int updateLastNumber(String name) {
		// TODO Auto-generated method stub
		return iSerialNumberRepository.updateNextNumber(name);
	}

	@Override
	public int getLastNumber() {
		// TODO Auto-generated method stub
		return iSerialNumberRepository.getLastNumber();
	}



	@Override
	public SerialNumber getSerialNumber(String name) {
		// TODO Auto-generated method stub
		return iSerialNumberRepository.findOneByName(name);
	}

	@Override
	public SerialNumber get(Integer id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SerialNumber save(SerialNumber serialnumber) {
		// TODO Auto-generated method stub
		iSerialNumberRepository.save(serialnumber);
		return serialnumber;
	}


}

