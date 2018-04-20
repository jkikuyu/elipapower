package com.ipayafrica.elipapower.service.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.ipayafrica.elipapower.service.IGenericService;


public class GenericServiceImpl<T, PK extends Serializable> implements IGenericService<T, PK> {
    /**
     * Log variable for all child classes. Uses LogFactory.getLog(getClass()) from Commons Logging
     */
    protected final Log log = LogFactory.getLog(getClass());

    /**
     * GenericDao instance, set by constructor of child classes
     */
    protected GenericRepository<T, PK> dao;

    @Autowired
    private CompassSearchHelper compass;

    public GenericServiceImpl() {
    }

    public GenericServiceImpl(GenericDao<T, PK> genericDao) {
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
    public boolean exists(PK id) {
        return dao.exists(id);
    }

    /**
     * {@inheritDoc}
     */
    public T save(T object) {
        return dao.save(object);
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

        CompassSearchCommand command = new CompassSearchCommand(q);
        CompassSearchResults compassResults = compass.search(command);
        CompassHit[] hits = compassResults.getHits();

        if (log.isDebugEnabled() && clazz != null) {
            log.debug("Filtering by type: " + clazz.getName());
        }

        for (CompassHit hit : hits) {
            if (clazz != null) {
                if (hit.data().getClass().equals(clazz)) {
                    results.add((T) hit.data());
                }
            } else {
                results.add((T) hit.data());
            }
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

	public Integer updateAll(String queryString)
	{
		return dao.updateAll(queryString);
	}
	
	public List  <T> searchByDate(String field, String dfrom, String dto,
			Class<?> klass) {
		
		return dao.searchByDate(field, dfrom, dto, klass);
		
	
	}
	
	public List  <T> findTopN ( String  q , Integer N)
	{
		
		return dao.findTopN (q ,N);
	}

	public List<Map<String, Object>> GetColumns (String objectname) {
		return this.dao.GetColumns(objectname);
	}

	public List<Object> SearchReport(String className, String creteria,
			String column, String item, String dfrom, String dto) {
		return dao.SearchReport( className,  creteria,column,  item,  dfrom,  dto);
	}



	public List<Object> getReconDetails(String tempstatus, Integer recordtype,
			String Datefrom, String DateTo, String clmn, String Criteria, String strItem) {		
		return dao.getReconDetails(tempstatus, recordtype, Datefrom, DateTo, clmn, Criteria, strItem);
	}

	public List<Object> getConfirmDetails(String status, String Datefrom, String DateTo, String clmn, String Criteria, String strItem) {		
		return dao.getConfirmDetails(status, Datefrom, DateTo, clmn, Criteria, strItem);
	}
	
	public List<Object> getFromPaymentsRecon(String status, String Datefrom, String DateTo, String clmn, String Criteria, String strItem) {		
		return dao.getFromPaymentsRecon(status, Datefrom, DateTo, clmn, Criteria, strItem);
	}
	
	
}
