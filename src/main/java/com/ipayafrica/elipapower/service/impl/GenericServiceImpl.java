package com.ipayafrica.elipapower.service.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.ipayafrica.elipapower.repository.IGenericDao;
import com.ipayafrica.elipapower.service.IGenericService;


public class GenericServiceImpl<T, PK extends Serializable> implements IGenericService<T, PK> {
    /**
     * Log variable for all child classes. Uses LogFactory.getLog(getClass()) from Commons Logging
     */
    protected final Log log = LogFactory.getLog(getClass());

    /**
     * GenericDao instance, set by constructor of child classes
     */
    protected IGenericDao<T, PK> dao;


    public GenericServiceImpl() {
    }

    public GenericServiceImpl(IGenericDao<T, PK> genericDao) {
        this.dao = genericDao;
    }

    /**
     * {@inheritDoc}
     */
    public List<T> getAll() {
        return dao.getAll();
    }

    /**
     * {@inheritDoc}
     */
    public T get(PK id) {
        return dao.get(id);
    }

    /**
     * {@inheritDoc}
     */
/*    public boolean exists(PK id) {
        return dao.exists(id);
    }
*/
    /**
     * {@inheritDoc}
     */
    @Transactional
    public <S extends T> S save(S entity) {
        return dao.save(entity);
    }

    /**
     * {@inheritDoc}
     */
    public void remove(PK id) {
        dao.remove(id);
    }

    /**
     * {@inheritDoc}
     * <p/>
     * Search implementation using Compass.
     */
    @SuppressWarnings("unchecked")
    public List<T> search(String q, Class clazz) {
        if (q == null || "".equals(q.trim())) {
            return getAll();
        }

        List<T> results = new ArrayList<T>();
        
        q = q.replaceAll("[^\\p{L}\\p{N}]", "");
        q=q.trim();
        
        q = "*"+q+"*";


        if (log.isDebugEnabled() && clazz != null) {
            log.debug("Filtering by type: " + clazz.getName());
        }


        if (log.isDebugEnabled()) {
            log.debug("Number of results for '" + q + "': " + results.size());
        }

        return results;
    }

	public void remove(Integer integer) {
		// TODO Auto-generated method stub
		
	}

	public void remove(long Long) {
		// TODO Auto-generated method stub
		
	}


	public int deleteALL(String Entity, String Property, String Value) {
		// TODO Auto-generated method stub
		return 0;
	}
	
	public List <T> FindAll(String queryString){
		return dao.FindAll(queryString);
	}
	
	
	public List  <T> search(String className, String creteria, String column,String item)
	{
		return dao.search(className, creteria, column, item);
	}

/*	public Integer updateAll(String queryString)
	{
		return dao.updateAll(queryString);
	}*/
	
	public List  <T> searchByDate(String field, String dfrom, String dto,
			Class<?> klass) {
		
		return dao.searchByDate(field, dfrom, dto, klass);
		
	
	}
	
	
	
}
