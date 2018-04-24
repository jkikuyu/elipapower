package com.ipayafrica.elipapower.repository;

/**
*
* @author Jude Kikuyu
* created on 20/04/2018
* 
*/
import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;

public interface IGenericDao <T extends Serializable>{
    
    T findOne(final long id);
    
    List<T> findAll();
  
    void create(final T entity);
  
    T update(final T entity);
  
    void delete(final T entity);
  
    void deleteById(final long entityId);    

}
