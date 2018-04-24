package com.ipayafrica.elipapower.service;

import java.io.Serializable;
import java.util.List;
import java.util.Map;


public interface IGenericService<T, PK extends Serializable> {

    /**
     * Generic method used to get all objects of a particular type. This
     * is the same as lookup up all rows in a table.
     * @return List of populated objects
     */
    List<T> getAll();

    /**
     * Generic method to get an object based on class and identifier. An
     * ObjectRetrievalFailureException Runtime Exception is thrown if
     * nothing is found.
     *
     * @param id the identifier (primary key) of the object to get
     * @return a populated object
     * @see org.springframework.orm.ObjectRetrievalFailureException
     */
    T get(PK id);

    /**
     * Checks for existence of an object of type T using the id arg.
     * @param id the identifier (primary key) of the object to get
     * @return - true if it exists, false if it doesn't
     */
    //boolean exists(PK id);

    /**
     * Generic method to save an object - handles both update and insert.
     * @param object the object to save
     * @return the updated object
     */
    <S extends T> S save(S entity);

    /**
     * Generic method to delete an object based on class and id
     * @param long the identifier (primary key) of the object to remove
     */
    void remove(long Long);

    /**
     * Generic method to search for an object.
     * @param searchTerm the search term
     * @param clazz type of class to search for.
     * @return a list of matched objects
     */
    List<T> search(String searchTerm, Class clazz);
    
    int deleteALL(String  Entity , String Property , String Value);
    
    List <T> FindAll(String queryString);
    
/*   Integer updateAll(String queryString);
*/   
   List  <T> search(String className, String creteria, String column,String item);
   
   List <T> searchByDate(String field, String dfrom, String dto, Class<?> klass) ;
   

}
