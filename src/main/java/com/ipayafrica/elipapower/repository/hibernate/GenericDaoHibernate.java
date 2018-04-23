package com.ipayafrica.elipapower.repository.hibernate;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.transform.AliasToEntityMapResultTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.boot.logging.LogFile;
import org.springframework.orm.ObjectRetrievalFailureException;
import org.springframework.orm.hibernate5.HibernateTemplate;

import com.ipayafrica.elipapower.repository.IGenericDao;




/**
 * This class serves as the Base class for all other DAOs - namely to hold
 * common CRUD methods that they might all use. You should only need to extend
 * this class when your require custom CRUD logic.
 * <p/>
 * <p>To register this class in your Spring context file, use the following XML.
 * <pre>
 *      &lt;bean id="fooDao" class="com.collections.citi.ebills.dao.hibernate.GenericDaoHibernate"&gt;
 *          &lt;constructor-arg value="com.collections.citi.ebills.model.Foo"/&gt;
 *      &lt;/bean&gt;
 * </pre>
 *
 * @author <a href="mailto:bwnoll@gmail.com">Bryan Noll</a>
 * @param <T> a type variable
 * @param <PK> the primary key for that type
 */
public class GenericDaoHibernate<T, PK extends Serializable> implements IGenericDao<T, PK> {
    /**
     * Log variable for all child classes. Uses LogFactory.getLog(getClass()) from Commons Logging
     */
    //protected final Log log = LogFactory.getLog(getClass());
    protected final transient Log log = LogFactory.getLog(getClass());
    private Class<T> persistentClass;
    private HibernateTemplate hibernateTemplate;
    private SessionFactory sessionFactory;

    /**
     * Constructor that takes in a class to see which type of entity to persist.
     * Use this constructor when subclassing.
     *
     * @param persistentClass the class type you'd like to persist
     */
    public GenericDaoHibernate(final Class<T> persistentClass) {
        this.persistentClass = persistentClass;
    }

    /**
     * Constructor that takes in a class and sessionFactory for easy creation of DAO.
     *
     * @param persistentClass the class type you'd like to persist
     * @param sessionFactory  the pre-configured Hibernate SessionFactory
     */
    public GenericDaoHibernate(final Class<T> persistentClass, SessionFactory sessionFactory) {
        this.persistentClass = persistentClass;
        this.sessionFactory = sessionFactory;
        this.hibernateTemplate = new HibernateTemplate(sessionFactory);
    }

    public HibernateTemplate getHibernateTemplate() {
        return this.hibernateTemplate;
    }

    public SessionFactory getSessionFactory() {
        return this.sessionFactory;
    }

    @Autowired
    @Required
    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
        this.hibernateTemplate = new HibernateTemplate(sessionFactory);
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public List<T> getAll() {
        return hibernateTemplate.loadAll(this.persistentClass);
    }
    /**
     * To do batch updates
     * @param queryString
     * @return
     */
    public Integer updateAll(String queryString)
    {
    	return hibernateTemplate.bulkUpdate(queryString);
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public List<T> getAllDistinct() {
        Collection result = new LinkedHashSet(getAll());
        return new ArrayList(result);
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public T get(PK id) {
        T entity = hibernateTemplate.get(this.persistentClass, id);

        if (entity == null) {
            log.warn("Uh oh, '" + this.persistentClass + "' object with id '" + id + "' not found...");
            throw new ObjectRetrievalFailureException(this.persistentClass, id);
        }

        return entity;
    }

    
    /**
     * find a set of records using a  a query
     */
    @SuppressWarnings("unchecked")
	public List <T> FindAll(String queryString)
    {    	
    	 return (List<T>) hibernateTemplate.find(queryString);
    }

 /**
  *  delete  the items using a query  
  */
    
    public int  deleteALL (String  Entity , String Property , String Value)
    {
    	 Session sess = null;
  
    	 SessionFactory fact = new 
    	 Configuration().configure().buildSessionFactory();
    	   sess = fact.openSession();
    	  
    	Query query =  sess.createQuery("delete "+ Entity +" where "+ Property +" = :" + Property);
    	query.setParameter(Property, Value);
    	return query.executeUpdate();

    }
    
    @SuppressWarnings("unchecked")
	public List  <T> findTopN ( String  q , Integer N)
    {
    	Session sess = getSessionFactory().openSession();
    	Query query = sess.createQuery(q);
    	query.setMaxResults(N);
    	return query.list();
    }
    
    
    /**
     * {@inheritDoc}
     */
 
    public boolean exists(PK id) {
        T entity = hibernateTemplate.get(this.persistentClass, id);
        return entity != null;
    }

    /**
     * {@inheritDoc}
     */
   
    public T save(T object) {
    	/*//hibernateTemplate.saveOrUpdate(object);
    	T entity = (T) hibernateTemplate.merge(object);
    	hibernateTemplate.flush();
    	return entity;*/
    	return hibernateTemplate.merge(object);
    }
    
  

    /**
     * {@inheritDoc}
     */
    public void remove(PK id) {
        hibernateTemplate.delete(this.get(id));
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public List<T> findByNamedQuery(String queryName, Map<String, Object> queryParams) {
        String[] params = new String[queryParams.size()];
        Object[] values = new Object[queryParams.size()];
        
        int index = 0;
        for (String s : queryParams.keySet()) {
            params[index] = s;
            values[index++] = queryParams.get(s);
        }

        return hibernateTemplate.findByNamedQueryAndNamedParam(queryName, params, values);
    }
    
    
    @SuppressWarnings("unchecked")
	public List  <T> search(String className, String creteria, String column,String item) {
		if(creteria.equals("like")){
			item="%"+item+"%";
		}
		
		String sql="from "+className+" v where lower(v."+column+")"+creteria+ " '"+item+"'";
		log.debug(sql);
		try{
			if (className.equals("User")) 
			{
				sql = sql + " and deleted = false";
			}
			else if (className == "Stakeholderdetails")
			{
				sql = sql + " and deleted =false";
				
			}
		if (className.equals("User"))
		{
			sql = sql + " order by v.approved asc";
		}
			return  hibernateTemplate.find(sql);
			}
			catch (Exception e) {
			 LogFile.LogError(e);
			}
			return null;
	}
    
    
	public List <T> searchByDate(String field, String dfrom, String dto,
			Class<?> klass) {
		List rslt = null;
		try{
			Field fld=klass.getDeclaredField(field);
				if(fld.getType().getSimpleName().equals("Date") || fld.getType().getSimpleName().equals("Calendar")){
					rslt = getHibernateTemplate().find("from "+klass.getName()+" where "+field+" between TO_DATE("+"'"+dfrom+"','DD/MM/YYYY'"+") and TO_DATE("+"'"+dto+"','DD/MM/YYYY'"+")");					
				}
			}catch (Exception e) {
			e.printStackTrace();
			LogFile.LogError(e);
		}
		return rslt;
	}
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> GetColumns (String objectname) {
		List<Map<String,Object>> aliasToValueMapList = new ArrayList<Map<String,Object>>();
		try 
		{
			Session session =  getSessionFactory().getCurrentSession();
			Query query=session.createSQLQuery("Select * from " + objectname + " tn ");
			query.setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE);
			aliasToValueMapList=query.list();
		}
		catch (Exception e)
		{
			// TODO: handle exception
		}
		return aliasToValueMapList;
	}

	
		    
}
