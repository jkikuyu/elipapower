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
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.classic.Session;
import org.hibernate.transform.AliasToEntityMapResultTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.orm.ObjectRetrievalFailureException;
import org.springframework.orm.hibernate3.HibernateTemplate;

import com.collections.citi.ebills.dao.GenericDao;
import com.collections.citi.ebills.model.Eattacatalog;
import com.collections.citi.ebills.model.Paidlots;
import com.collections.citi.ebills.model.Splitlots;
import com.collections.citi.ebills.util.LogFile;


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
public class GenericDaoHibernate<T, PK extends Serializable> implements GenericDao<T, PK> {
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
    	 return hibernateTemplate.find(queryString);
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

	@SuppressWarnings("unchecked")
	public List<Object> SearchReport(String className, String creteria,String column, String item, String dfrom, String dto)
	{
		  /*Session session =  getSessionFactory().getCurrentSession();*/
		  List<Object> reportList = new ArrayList<Object>();
		  try
		  {
			  String sql="";
			  if(creteria.equals("like"))
			  {
					item=item+"%";
			  }
			  sql="from "+className+" v where v."+column + " " + creteria + " '"+item+"'";
			  
			  if (dfrom !="" && dto !="")
			  {
				  if(creteria.equals(""))
				  {
					  sql ="from "+className+" v where osysdate between  TO_DATE("+"'"+dfrom+"','MM/DD/YYYY'"+") and TO_DATE("+"'"+dto+"','MM/DD/YYYY'"+")";
				  }
				  else
					  sql += " and osysdate between  TO_DATE("+"'"+dfrom+"','MM/DD/YYYY'"+") and TO_DATE("+"'"+dto+"','MM/DD/YYYY'"+")";
			  }
			  else if (dfrom !="" && dto =="")
			  {
				  if(creteria.equals(""))
				  {
					  sql ="from "+className+" v where substr(osysdate, 0 , 10) =  TO_DATE("+"'"+dfrom+"','MM/DD/YYYY'"+")";
				  }
				  else
					  sql +=  " and substr(osysdate, 0 , 10) =  TO_DATE("+"'"+dfrom+"','MM/DD/YYYY'"+")";
			  }
			  else if (dfrom =="" && dto !="")
			  {
				  if(creteria.equals(""))
				  {
					  sql ="from "+className+" v where substr(osysdate, 0 , 10) =  TO_DATE("+"'"+dto+"','MM/DD/YYYY'"+")";
				  }
				  else
					  sql +=  " and substr(osysdate, 0 , 10) =  TO_DATE("+"'"+dto+"','MM/DD/YYYY'"+")";
			  }
			  //log.debug("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@" + sql);
			  sql = sql + " order by osysdate desc";
			  reportList =  hibernateTemplate.find(sql);
		  }
		catch (Exception e) 
		{
			//log.debug("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%" + e);
			LogFile.LogError(e);
		}
		return reportList;
	}

	@SuppressWarnings("unchecked")
	public List<Object> getTeaDashBoard(String stakeholderClmn,Long Stakeholderid, String Datefrom, String DateTo, String clmn,String Criteria, String strItem) 
	{
		List<Object> reportList = new ArrayList<Object>();
		/*Session session =  getSessionFactory().getCurrentSession();*/
		
		String sql="";
		if(Criteria.equals("like") || Criteria=="like")
		  {
			if(!strItem.equals(""))
			  {
				strItem=strItem+"%";
			  }
		  }
		if(strItem.equals("") || strItem == "")
		  {
			sql="from Eattateadashboard v where v."+stakeholderClmn.toLowerCase() + "=" + Stakeholderid ;
		  }
		else
			sql="from Eattateadashboard v where v."+stakeholderClmn.toLowerCase() + "=" + Stakeholderid +" and  v."+clmn + " " + Criteria + " '"+strItem+"'";
		if (Datefrom !="" && DateTo !="")
		  {
			  if(strItem.equals("") || strItem == "")
			  {
				  sql ="from Eattateadashboard v where v."+stakeholderClmn.toLowerCase() + "=" + Stakeholderid +" and  v.datecreate between  TO_DATE("+"'"+Datefrom+"','MM/DD/YYYY'"+") and TO_DATE("+"'"+DateTo+"','MM/DD/YYYY'"+")";
			  }
			  else
				  sql += " and datecreate between  TO_DATE("+"'"+Datefrom+"','MM/DD/YYYY'"+") and TO_DATE("+"'"+DateTo+"','MM/DD/YYYY'"+")";
		  }
		  else if (Datefrom !="" && DateTo =="")
		  {
			  if(strItem.equals("") || strItem == "")
			  {
				  sql ="from Eattateadashboard v where v."+stakeholderClmn.toLowerCase() + "=" + Stakeholderid +" and substr(v.datecreate, 0 , 10)  =  TO_DATE("+"'"+Datefrom+"','MM/DD/YYYY'"+")";
			  }
			  else
				  sql +=  " and substr(v.datecreate, 0 , 10)  =  TO_DATE("+"'"+Datefrom+"','MM/DD/YYYY'"+")";
		  }
		  else if (Datefrom =="" && DateTo !="")
		  {
			  if(strItem.equals("") || strItem == "")
			  {
				  sql ="from Eattateadashboard v where v."+stakeholderClmn.toLowerCase() + "=" + Stakeholderid +" and  substr(v.datecreate, 0 , 10)  =  TO_DATE("+"'"+DateTo+"','MM/DD/YYYY'"+")";
			  }
			  else
				  sql = sql + " and substr(v.datecreate, 0 , 10)  =  TO_DATE("+"'"+DateTo+"','MM/DD/YYYY'"+")";
		  }
		sql = sql +" order by v.datecreate desc";
		reportList =  hibernateTemplate.find(sql);
		
		return reportList;
	}

	@SuppressWarnings("unchecked")
	public List<Object> getCatalogDetails(String stakeholderClmn, Long Stakeholderid, String statusId, 
			String Datefrom, String DateTo, String clmn, String Criteria, String strItem) {
		
		List<Object> reportList = new ArrayList<Object>();
		/*Session session =  getSessionFactory().getCurrentSession();*/
		
		String sql="";
		if(Criteria.equals("like") || Criteria=="like")
		  {
			if(!strItem.equals(""))
			  {
				strItem=strItem+"%";
			  }
		  }
		if(strItem.equals("") || strItem == "")
		  {
			sql="from Eattacatalog v where v."+stakeholderClmn.toLowerCase() + "=" + Stakeholderid +" and v.teastatus IN ("+ statusId +")";
		  }
		else
			sql="from Eattacatalog v where v."+stakeholderClmn.toLowerCase() + "=" + Stakeholderid +" and v.teastatus IN ("+ statusId +") and  v."+clmn + " " + Criteria + " '"+strItem+"'";
		if (Datefrom !="" && DateTo !="")
		  {
			  if(strItem.equals(""))
			  {
				  sql ="from Eattacatalog v where v."+stakeholderClmn.toLowerCase() + "=" + Stakeholderid +" and v.teastatus IN ("+ statusId +") and  v.datecreate between  TO_DATE("+"'"+Datefrom+"','MM/DD/YYYY'"+") and TO_DATE("+"'"+DateTo+"','MM/DD/YYYY'"+")";
			  }
			  else
				  sql += " and datecreate between  TO_DATE("+"'"+Datefrom+"','MM/DD/YYYY'"+") and TO_DATE("+"'"+DateTo+"','MM/DD/YYYY'"+")";
		  }
		  else if (Datefrom !="" && DateTo =="")
		  {
			  if(strItem.equals("") || strItem == "")
			  {
				  sql ="from Eattacatalog v where v."+stakeholderClmn.toLowerCase() + "=" + Stakeholderid +" and v.teastatus IN ("+ statusId +") and substr(v.datecreate, 0 , 10)  =  TO_DATE("+"'"+Datefrom+"','MM/DD/YYYY'"+")";
			  }
			  else
				  sql +=  " and substr(v.datecreate, 0 , 10)  =  TO_DATE("+"'"+Datefrom+"','MM/DD/YYYY'"+")";
		  }
		  else if (Datefrom =="" && DateTo !="")
		  {
			  if(strItem.equals(""))
			  {
				  sql ="from Eattacatalog v where v."+stakeholderClmn.toLowerCase() + "=" + Stakeholderid +" and v.teastatus IN ("+ statusId +") and  substr(v.datecreate, 0 , 10)  =  TO_DATE("+"'"+DateTo+"','MM/DD/YYYY'"+")";
			  }
			  else
				  sql = sql + " and substr(v.datecreate, 0 , 10)  =  TO_DATE("+"'"+DateTo+"','MM/DD/YYYY'"+")";
		  }
		sql = sql +" order by v.datecreate desc";
		reportList =  hibernateTemplate.find(sql);
		
		return reportList;
	}

	@SuppressWarnings("unchecked")
	public List<Object> getReconDetails(String tempstatus, Integer recordtype,
			String Datefrom, String DateTo, String clmn, String Criteria, String strItem) {

		List<Object> reportList = new ArrayList<Object>();
		/*Session session =  getSessionFactory().getCurrentSession();	*/	
		String sql="";
		
		if(Criteria.equals("like") || Criteria=="like")
		{
			if(!strItem.equals(""))
			{
				strItem=strItem+"%";
			}
		}
		if(strItem.equals("") || strItem == "")
		{
			sql="from Recontemp v where v.tempstatus='" + tempstatus +"' and v.recordtype=" + recordtype ;
		}
		else
			sql="from Recontemp v where v.tempstatus='" + tempstatus +"' and v.recordtype=" + recordtype +" and  v."+clmn + " " + Criteria + " '"+strItem+"'";
		
		if (Datefrom !="" && DateTo !="")
		{
			if(strItem.equals(""))
			{
				sql ="from Recontemp v where v.tempstatus='" + tempstatus +"' and v.recordtype=" + recordtype +" and  v.osysdate between  TO_DATE("+"'"+Datefrom+"','MM/DD/YYYY'"+") and TO_DATE("+"'"+DateTo+"','MM/DD/YYYY'"+")";
			}
			else
				sql += " and osysdate between  TO_DATE("+"'"+Datefrom+"','MM/DD/YYYY'"+") and TO_DATE("+"'"+DateTo+"','MM/DD/YYYY'"+")";
		}
		else if (Datefrom !="" && DateTo =="")
		{
			if(strItem.equals(""))
			{
				sql ="from Recontemp v where v.tempstatus='" + tempstatus +"' and v.recordtype=" + recordtype +" and substr(v.osysdate, 0 , 10)  =  TO_DATE("+"'"+Datefrom+"','MM/DD/YYYY'"+")";
			}
			else
				sql +=  " and substr(v.osysdate, 0 , 10)  =  TO_DATE("+"'"+Datefrom+"','MM/DD/YYYY'"+")";
		}
		else if (Datefrom =="" && DateTo !="")
		{
			if(strItem.equals(""))
			{
				sql ="from Recontemp v where v.tempstatus='" + tempstatus +"' and v.recordtype=" + recordtype +" and  substr(v.osysdate, 0 , 10)  =  TO_DATE("+"'"+DateTo+"','MM/DD/YYYY'"+")";
			}
			else
				sql = sql + " and substr(v.osysdate, 0 , 10)  =  TO_DATE("+"'"+DateTo+"','MM/DD/YYYY'"+")";
		}
		
		sql = sql +" order by v.osysdate desc";
		reportList =  hibernateTemplate.find(sql);
		
		return reportList;
	}

	@SuppressWarnings({ "unchecked" })
	public List<Object> getConfirmDetails(String status, String Datefrom, String DateTo, String clmn, String Criteria, String strItem) {
		
		List<Object> reportList = new ArrayList<Object>();			
		String sql="";
		
		if(Criteria.equals("like") || Criteria=="like")
		{
			if(!strItem.equals(""))
			{
				strItem=strItem+"%";
			}
		}
		if(strItem.equals("") || strItem == "")
		{
			sql="from Confirmpayments v where v.recordmatched IN ("+ status +")";
		}
		else
			sql="from Confirmpayments v where v.recordmatched IN ("+ status +") and  v."+clmn + " " + Criteria + " '"+strItem+"'";
		
		if (Datefrom !="" && DateTo !="")
		{
			if(strItem.equals(""))
			{
				sql ="from Confirmpayments v where v.recordmatched IN ("+ status +") and  v.trndt between  TO_DATE("+"'"+Datefrom+"','MM/DD/YYYY'"+") and TO_DATE("+"'"+DateTo+"','MM/DD/YYYY'"+")";
			}
			else
				sql += " and trndt between  TO_DATE("+"'"+Datefrom+"','MM/DD/YYYY'"+") and TO_DATE("+"'"+DateTo+"','MM/DD/YYYY'"+")";
		}
		else if (Datefrom !="" && DateTo =="")
		{
			if(strItem.equals(""))
			{
				sql ="from Confirmpayments v where v.recordmatched IN ("+ status +") and substr(v.trndt, 0 , 10)  =  TO_DATE("+"'"+Datefrom+"','MM/DD/YYYY'"+")";
			}
			else
				sql +=  " and substr(v.trndt, 0 , 10)  =  TO_DATE("+"'"+Datefrom+"','MM/DD/YYYY'"+")";
		}
		else if (Datefrom =="" && DateTo !="")
		{
			if(strItem.equals(""))
			{
				sql ="from Confirmpayments v where v.recordmatched IN ("+ status +") and  substr(v.trndt, 0 , 10)  =  TO_DATE("+"'"+DateTo+"','MM/DD/YYYY'"+")";
			}
			else
				sql = sql + " and substr(v.trndt, 0 , 10)  =  TO_DATE("+"'"+DateTo+"','MM/DD/YYYY'"+")";
		}
		
		sql = sql +" order by v.trndt desc";
		reportList =  hibernateTemplate.find(sql);
		
		return reportList;
	}
	
	@SuppressWarnings({ "unchecked" })
	public List<Object> getFromPaymentsRecon(String status, String Datefrom, String DateTo, String clmn, String Criteria, String strItem) {
		
		List<Object> reportList = new ArrayList<Object>();			
		String sql="";
		
		if(Criteria.equals("like") || Criteria=="like")
		{
			if(!strItem.equals(""))
			{
				strItem=strItem+"%";
			}
		}
		if(strItem.equals("") || strItem == "")
		{
			sql="from PaymentsRecon v where v.matched <> "+ status;
		}
		else
			sql="from PaymentsRecon v where v.matched <> "+ status +" and  v."+clmn + " " + Criteria + " '"+strItem+"'";
		
		if (Datefrom !="" && DateTo !="")
		{
			if(strItem.equals(""))
			{
				sql ="from PaymentsRecon v where v.matched <> "+ status +" and  v.txndate between  TO_DATE("+"'"+Datefrom+"','MM/DD/YYYY'"+") and TO_DATE("+"'"+DateTo+"','MM/DD/YYYY'"+")";
			}
			else
				sql += " and txndate between  TO_DATE("+"'"+Datefrom+"','MM/DD/YYYY'"+") and TO_DATE("+"'"+DateTo+"','MM/DD/YYYY'"+")";
		}
		else if (Datefrom !="" && DateTo =="")
		{
			if(strItem.equals(""))
			{
				sql ="from PaymentsRecon v where v.matched <> "+ status +" and substr(v.txndate, 0 , 10)  =  TO_DATE("+"'"+Datefrom+"','MM/DD/YYYY'"+")";
			}
			else
				sql +=  " and substr(v.txndate, 0 , 10)  =  TO_DATE("+"'"+Datefrom+"','MM/DD/YYYY'"+")";
		}
		else if (Datefrom =="" && DateTo !="")
		{
			if(strItem.equals(""))
			{
				sql ="from PaymentsRecon v where v.matched <> "+ status +" and  substr(v.txndate, 0 , 10)  =  TO_DATE("+"'"+DateTo+"','MM/DD/YYYY'"+")";
			}
			else
				sql = sql + " and substr(v.txndate, 0 , 10)  =  TO_DATE("+"'"+DateTo+"','MM/DD/YYYY'"+")";
		}
		
		sql = sql +" order by v.txndate desc";
		reportList =  hibernateTemplate.find(sql);
		
		return reportList;
	}
	
	@SuppressWarnings({ "unchecked" })
	public List<Object> getFromKplcPayments(String status, String Datefrom, String DateTo, String clmn, String Criteria, String strItem) {
		
		List<Object> reportList = new ArrayList<Object>();			
		String sql="";
		
		if(Criteria.equals("like") || Criteria=="like")
		{
			if(!strItem.equals(""))
			{
				strItem=strItem+"%";
			}
		}
		if(strItem.equals("") || strItem == "")
		{
			sql="from Kplcpayments v where v.recordmatched <> "+ status;
		}
		else
			sql="from Kplcpayments v where v.recordmatched <> "+ status +" and  v."+clmn + " " + Criteria + " '"+strItem+"'";
		
		if (Datefrom !="" && DateTo !="")
		{
			if(strItem.equals(""))
			{
				sql ="from Kplcpayments v where v.recordmatched <> "+ status +" and  v.trndt between  TO_DATE("+"'"+Datefrom+"','MM/DD/YYYY'"+") and TO_DATE("+"'"+DateTo+"','MM/DD/YYYY'"+")";
			}
			else
				sql += " and trndt between  TO_DATE("+"'"+Datefrom+"','MM/DD/YYYY'"+") and TO_DATE("+"'"+DateTo+"','MM/DD/YYYY'"+")";
		}
		else if (Datefrom !="" && DateTo =="")
		{
			if(strItem.equals(""))
			{
				sql ="from Kplcpayments v where v.recordmatched <> "+ status +" and substr(v.trndt, 0 , 10)  =  TO_DATE("+"'"+Datefrom+"','MM/DD/YYYY'"+")";
			}
			else
				sql +=  " and substr(v.trndt, 0 , 10)  =  TO_DATE("+"'"+Datefrom+"','MM/DD/YYYY'"+")";
		}
		else if (Datefrom =="" && DateTo !="")
		{
			if(strItem.equals(""))
			{
				sql ="from Kplcpayments v where v.recordmatched <> "+ status +" and  substr(v.trndt, 0 , 10)  =  TO_DATE("+"'"+DateTo+"','MM/DD/YYYY'"+")";
			}
			else
				sql = sql + " and substr(v.trndt, 0 , 10)  =  TO_DATE("+"'"+DateTo+"','MM/DD/YYYY'"+")";
		}
		
		sql = sql +" order by v.trndt desc";
		reportList =  hibernateTemplate.find(sql);
		
		return reportList;
	}

	@SuppressWarnings("unchecked")
	public List<Object> getCatalogDetails(String statusId, String Datefrom, String DateTo, String clmn, String Criteria, String strItem) {
		
		List<Object> reportList = new ArrayList<Object>();
		
		String sql="";
		if(Criteria.equals("like") || Criteria=="like")
		  {
			if(!strItem.equals(""))
			  {
				strItem=strItem+"%";
			  }
		  }
		if(strItem.equals("") || strItem == "")
		  {
			sql="from Eattacatalog v where v.teastatus IN ("+ statusId +")";
		  }
		else
			sql="from Eattacatalog v where v.teastatus IN ("+ statusId +") and  v."+clmn + " " + Criteria + " '"+strItem+"'";
		if (Datefrom !="" && DateTo !="")
		  {
			  if(strItem.equals(""))
			  {
				  sql ="from Eattacatalog v where v.teastatus IN ("+ statusId +") and  v.datecreate between  TO_DATE("+"'"+Datefrom+"','MM/DD/YYYY'"+") and TO_DATE("+"'"+DateTo+"','MM/DD/YYYY'"+")";
			  }
			  else
				  sql += " and datecreate between  TO_DATE("+"'"+Datefrom+"','MM/DD/YYYY'"+") and TO_DATE("+"'"+DateTo+"','MM/DD/YYYY'"+")";
		  }
		  else if (Datefrom !="" && DateTo =="")
		  {
			  if(strItem.equals(""))
			  {
				  sql ="from Eattacatalog v where v.teastatus IN ("+ statusId +") and substr(v.datecreate, 0 , 10)  =  TO_DATE("+"'"+Datefrom+"','MM/DD/YYYY'"+")";
			  }
			  else
				  sql +=  " and substr(v.datecreate, 0 , 10)  =  TO_DATE("+"'"+Datefrom+"','MM/DD/YYYY'"+")";
		  }
		  else if (Datefrom =="" && DateTo !="")
		  {
			  if(strItem.equals(""))
			  {
				  sql ="from Eattacatalog v where v.teastatus IN ("+ statusId +") and  substr(v.datecreate, 0 , 10)  =  TO_DATE("+"'"+DateTo+"','MM/DD/YYYY'"+")";
			  }
			  else
				  sql = sql + " and substr(v.datecreate, 0 , 10)  =  TO_DATE("+"'"+DateTo+"','MM/DD/YYYY'"+")";
		  }
		sql = sql +" order by v.datecreate desc";
		reportList =  hibernateTemplate.find(sql);
		
		return reportList;
	}
	
	@SuppressWarnings("unchecked")
	public List<Object> getCatalogDetails(String defaultCol, Long defaultStakeId, String stakeholderClmn, 
			Long Stakeholderid, String statusId, String Datefrom, String DateTo, String clmn, String Criteria, String strItem) {
		
		List<Object> reportList = new ArrayList<Object>();
		
		String sql="";
		if(Criteria.equals("like") || Criteria=="like")
		  {
			if(!strItem.equals(""))
			  {
				strItem=strItem+"%";
			  }
		  }
		if(strItem.equals("") || strItem == "")
		  {
			sql="from Eattacatalog v where v."+stakeholderClmn.toLowerCase() + "=" + Stakeholderid +" and v.teastatus IN ("+ statusId +") and v."+defaultCol.toLowerCase() + "=" + defaultStakeId;
		  }
		else
			sql="from Eattacatalog v where v."+stakeholderClmn.toLowerCase() + "=" + Stakeholderid +" and v.teastatus IN ("+ statusId +") and v."+defaultCol.toLowerCase() + "=" + defaultStakeId + " and  v."+clmn + " " + Criteria + " '"+strItem+"'";
		if (Datefrom !="" && DateTo !="")
		  {
			  if(strItem.equals(""))
			  {
				  sql ="from Eattacatalog v where v."+stakeholderClmn.toLowerCase() + "=" + Stakeholderid +" and v.teastatus IN ("+ statusId +") and v."+defaultCol.toLowerCase() + "=" + defaultStakeId + " and  v.datecreate between  TO_DATE("+"'"+Datefrom+"','MM/DD/YYYY'"+") and TO_DATE("+"'"+DateTo+"','MM/DD/YYYY'"+")";
			  }
			  else
				  sql += " and datecreate between  TO_DATE("+"'"+Datefrom+"','MM/DD/YYYY'"+") and TO_DATE("+"'"+DateTo+"','MM/DD/YYYY'"+")";
		  }
		  else if (Datefrom !="" && DateTo =="")
		  {
			  if(strItem.equals("") || strItem == "")
			  {
				  sql ="from Eattacatalog v where v."+stakeholderClmn.toLowerCase() + "=" + Stakeholderid +" and v.teastatus IN ("+ statusId +") and v."+defaultCol.toLowerCase() + "=" + defaultStakeId + " and substr(v.datecreate, 0 , 10)  =  TO_DATE("+"'"+Datefrom+"','MM/DD/YYYY'"+")";
			  }
			  else
				  sql +=  " and substr(v.datecreate, 0 , 10)  =  TO_DATE("+"'"+Datefrom+"','MM/DD/YYYY'"+")";
		  }
		  else if (Datefrom =="" && DateTo !="")
		  {
			  if(strItem.equals(""))
			  {
				  sql ="from Eattacatalog v where v."+stakeholderClmn.toLowerCase() + "=" + Stakeholderid +" and v.teastatus IN ("+ statusId +") and v."+defaultCol.toLowerCase() + "=" + defaultStakeId + " and  substr(v.datecreate, 0 , 10)  =  TO_DATE("+"'"+DateTo+"','MM/DD/YYYY'"+")";
			  }
			  else
				  sql = sql + " and substr(v.datecreate, 0 , 10)  =  TO_DATE("+"'"+DateTo+"','MM/DD/YYYY'"+")";
		  }
		sql = sql +" order by v.datecreate desc";
		reportList =  hibernateTemplate.find(sql);
		
		return reportList;
	}
	
	@SuppressWarnings("unchecked")
	public List<Object> getCatalogDetails(String auctionnumber, Boolean outlot, String Datefrom, String DateTo, String clmn, String Criteria, String strItem) {
		
		List<Object> reportList = new ArrayList<Object>();
		
		String sql="";
		if(Criteria.equals("like") || Criteria=="like")
		  {
			if(!strItem.equals(""))
			  {
				strItem=strItem+"%";
			  }
		  }
		if(strItem.equals("") || strItem == "")
		  {
			sql="from Eattacatalog v where v.auctionnumber = '"+ auctionnumber + "' and v.outlot = 1";
		  }
		else
			sql="from Eattacatalog v where v.auctionnumber = '"+ auctionnumber + "' and v.outlot = 1 and  v."+clmn + " " + Criteria + " '"+strItem+"'";
		if (Datefrom !="" && DateTo !="")
		  {
			  if(strItem.equals(""))
			  {
				  sql ="from Eattacatalog v where v.auctionnumber = '"+ auctionnumber + "' and v.outlot = 1 and  v.datecreate between  TO_DATE("+"'"+Datefrom+"','MM/DD/YYYY'"+") and TO_DATE("+"'"+DateTo+"','MM/DD/YYYY'"+")";
			  }
			  else
				  sql += " and datecreate between  TO_DATE("+"'"+Datefrom+"','MM/DD/YYYY'"+") and TO_DATE("+"'"+DateTo+"','MM/DD/YYYY'"+")";
		  }
		  else if (Datefrom !="" && DateTo =="")
		  {
			  if(strItem.equals("") || strItem == "")
			  {
				  sql ="from Eattacatalog v where v.auctionnumber = '"+ auctionnumber + "' and v.outlot = 1 and substr(v.datecreate, 0 , 10)  =  TO_DATE("+"'"+Datefrom+"','MM/DD/YYYY'"+")";
			  }
			  else
				  sql +=  " and substr(v.datecreate, 0 , 10)  =  TO_DATE("+"'"+Datefrom+"','MM/DD/YYYY'"+")";
		  }
		  else if (Datefrom =="" && DateTo !="")
		  {
			  if(strItem.equals(""))
			  {
				  sql ="from Eattacatalog v where v.auctionnumber = '"+ auctionnumber + "' and v.outlot = 1 and  substr(v.datecreate, 0 , 10)  =  TO_DATE("+"'"+DateTo+"','MM/DD/YYYY'"+")";
			  }
			  else
				  sql = sql + " and substr(v.datecreate, 0 , 10)  =  TO_DATE("+"'"+DateTo+"','MM/DD/YYYY'"+")";
		  }
		sql = sql +" order by v.datecreate desc";
		reportList =  hibernateTemplate.find(sql);
		
		return reportList;
	}
	
	@SuppressWarnings("unchecked")
	public List<Object> getBuyerPenalties(String stakeholderClmn, Long Stakeholderid, String Datefrom, String DateTo, String clmn, String Criteria, String strItem) {
		
		List<Object> reportList = new ArrayList<Object>();
		
		String sql="";
		if(Criteria.equals("like") || Criteria=="like")
		  {
			if(!strItem.equals(""))
			  {
				strItem=strItem+"%";
			  }
		  }
		if(strItem.equals("") || strItem == "")
		  {
			sql="from Buyerpenalties v where v."+stakeholderClmn.toLowerCase() + "=" + Stakeholderid;
		  }
		else
			sql="from Buyerpenalties v where v."+stakeholderClmn.toLowerCase() + "=" + Stakeholderid +" and  v."+clmn + " " + Criteria + " '"+strItem+"'";
		if (Datefrom !="" && DateTo !="")
		  {
			  if(strItem.equals(""))
			  {
				  sql ="from Buyerpenalties v where v."+stakeholderClmn.toLowerCase() + "=" + Stakeholderid +" and  v.datecreate between  TO_DATE("+"'"+Datefrom+"','MM/DD/YYYY'"+") and TO_DATE("+"'"+DateTo+"','MM/DD/YYYY'"+")";
			  }
			  else
				  sql += " and datecreate between  TO_DATE("+"'"+Datefrom+"','MM/DD/YYYY'"+") and TO_DATE("+"'"+DateTo+"','MM/DD/YYYY'"+")";
		  }
		  else if (Datefrom !="" && DateTo =="")
		  {
			  if(strItem.equals("") || strItem == "")
			  {
				  sql ="from Buyerpenalties v where v."+stakeholderClmn.toLowerCase() + "=" + Stakeholderid +" and substr(v.datecreate, 0 , 10)  =  TO_DATE("+"'"+Datefrom+"','MM/DD/YYYY'"+")";
			  }
			  else
				  sql +=  " and substr(v.datecreate, 0 , 10)  =  TO_DATE("+"'"+Datefrom+"','MM/DD/YYYY'"+")";
		  }
		  else if (Datefrom =="" && DateTo !="")
		  {
			  if(strItem.equals(""))
			  {
				  sql ="from Buyerpenalties v where v."+stakeholderClmn.toLowerCase() + "=" + Stakeholderid +" and  substr(v.datecreate, 0 , 10)  =  TO_DATE("+"'"+DateTo+"','MM/DD/YYYY'"+")";
			  }
			  else
				  sql = sql + " and substr(v.datecreate, 0 , 10)  =  TO_DATE("+"'"+DateTo+"','MM/DD/YYYY'"+")";
		  }
		sql = sql +" order by v.datecreate desc";
		reportList =  hibernateTemplate.find(sql);
		
		return reportList;
	}
	
	@SuppressWarnings("unchecked")
	public List<Object> getCatalogDetails(String defaultCol, Long defaultStakeId, String stakeholderClmn, 
			Long Stakeholderid, String boolCol, Long boolValue, String Datefrom, String DateTo, String clmn, String Criteria, String strItem) {
		
		List<Object> reportList = new ArrayList<Object>();
		
		String sql="";
		if(Criteria.equals("like") || Criteria=="like")
		  {
			if(!strItem.equals(""))
			  {
				strItem=strItem+"%";
			  }
		  }
		if(strItem.equals("") || strItem == "")
		  {
			sql="from Eattacatalog v where v."+stakeholderClmn.toLowerCase() + "=" + Stakeholderid +" and v."+boolCol.toLowerCase() + "=1 and v."+defaultCol.toLowerCase() + "=" + defaultStakeId;
		  }
		else
			sql="from Eattacatalog v where v."+stakeholderClmn.toLowerCase() + "=" + Stakeholderid +" and v."+boolCol.toLowerCase() + "=1 and v."+defaultCol.toLowerCase() + "=" + defaultStakeId + " and  v."+clmn + " " + Criteria + " '"+strItem+"'";
		if (Datefrom !="" && DateTo !="")
		  {
			  if(strItem.equals(""))
			  {
				  sql ="from Eattacatalog v where v."+stakeholderClmn.toLowerCase() + "=" + Stakeholderid +" and v."+boolCol.toLowerCase() + "=1 and v."+defaultCol.toLowerCase() + "=" + defaultStakeId + " and  v.datecreate between  TO_DATE("+"'"+Datefrom+"','MM/DD/YYYY'"+") and TO_DATE("+"'"+DateTo+"','MM/DD/YYYY'"+")";
			  }
			  else
				  sql += " and datecreate between  TO_DATE("+"'"+Datefrom+"','MM/DD/YYYY'"+") and TO_DATE("+"'"+DateTo+"','MM/DD/YYYY'"+")";
		  }
		  else if (Datefrom !="" && DateTo =="")
		  {
			  if(strItem.equals("") || strItem == "")
			  {
				  sql ="from Eattacatalog v where v."+stakeholderClmn.toLowerCase() + "=" + Stakeholderid +" and v."+boolCol.toLowerCase() + "=1 and v."+defaultCol.toLowerCase() + "=" + defaultStakeId + " and substr(v.datecreate, 0 , 10)  =  TO_DATE("+"'"+Datefrom+"','MM/DD/YYYY'"+")";
			  }
			  else
				  sql +=  " and substr(v.datecreate, 0 , 10)  =  TO_DATE("+"'"+Datefrom+"','MM/DD/YYYY'"+")";
		  }
		  else if (Datefrom =="" && DateTo !="")
		  {
			  if(strItem.equals(""))
			  {
				  sql ="from Eattacatalog v where v."+stakeholderClmn.toLowerCase() + "=" + Stakeholderid +" and v."+boolCol.toLowerCase() + "=1 and v."+defaultCol.toLowerCase() + "=" + defaultStakeId + " and  substr(v.datecreate, 0 , 10)  =  TO_DATE("+"'"+DateTo+"','MM/DD/YYYY'"+")";
			  }
			  else
				  sql = sql + " and substr(v.datecreate, 0 , 10)  =  TO_DATE("+"'"+DateTo+"','MM/DD/YYYY'"+")";
		  }
		sql = sql +" order by v.datecreate desc";
		reportList =  hibernateTemplate.find(sql);
		
		return reportList;
	}
	
	@SuppressWarnings("unchecked")
	public List<Eattacatalog> getCatalogDetails(String defaultCol, Long defaultStakeId, String stakeholderClmn, 
			Long Stakeholderid, String statusId, String stringCol, String stringValue, String Datefrom, String DateTo, String clmn, String Criteria, String strItem) {
		
		List<Eattacatalog> reportList = new ArrayList<Eattacatalog>();
		
		String sql="";
		if(Criteria.equals("like") || Criteria=="like")
		  {
			if(!strItem.equals(""))
			  {
				strItem=strItem+"%";
			  }
		  }
		if(strItem.equals("") || strItem == "")
		  {
			sql="from Eattacatalog v where v."+stakeholderClmn.toLowerCase() + "=" + Stakeholderid +" and v.teastatus IN ("+ statusId +") and v."+defaultCol.toLowerCase() + "=" + defaultStakeId + " and v."+stringCol + "='" + stringValue +"'";
		  }
		else
			sql="from Eattacatalog v where v."+stakeholderClmn.toLowerCase() + "=" + Stakeholderid +" and v.teastatus IN ("+ statusId +") and v."+defaultCol.toLowerCase() + "=" + defaultStakeId + " and v."+stringCol + "='" + stringValue + "' and  v."+clmn + " " + Criteria + " '"+strItem+"'";
		if (Datefrom !="" && DateTo !="")
		  {
			  if(strItem.equals(""))
			  {
				  sql ="from Eattacatalog v where v."+stakeholderClmn.toLowerCase() + "=" + Stakeholderid +" and v.teastatus IN ("+ statusId +") and v."+defaultCol.toLowerCase() + "=" + defaultStakeId + " and v."+stringCol + "='" + stringValue + "' and  v.datecreate between  TO_DATE("+"'"+Datefrom+"','MM/DD/YYYY'"+") and TO_DATE("+"'"+DateTo+"','MM/DD/YYYY'"+")";
			  }
			  else
				  sql += " and datecreate between  TO_DATE("+"'"+Datefrom+"','MM/DD/YYYY'"+") and TO_DATE("+"'"+DateTo+"','MM/DD/YYYY'"+")";
		  }
		  else if (Datefrom !="" && DateTo =="")
		  {
			  if(strItem.equals("") || strItem == "")
			  {
				  sql ="from Eattacatalog v where v."+stakeholderClmn.toLowerCase() + "=" + Stakeholderid +" and v.teastatus IN ("+ statusId +") and v."+defaultCol.toLowerCase() + "=" + defaultStakeId + " and v."+stringCol + "='" + stringValue + "' and substr(v.datecreate, 0 , 10)  =  TO_DATE("+"'"+Datefrom+"','MM/DD/YYYY'"+")";
			  }
			  else
				  sql +=  " and substr(v.datecreate, 0 , 10)  =  TO_DATE("+"'"+Datefrom+"','MM/DD/YYYY'"+")";
		  }
		  else if (Datefrom =="" && DateTo !="")
		  {
			  if(strItem.equals(""))
			  {
				  sql ="from Eattacatalog v where v."+stakeholderClmn.toLowerCase() + "=" + Stakeholderid +" and v.teastatus IN ("+ statusId +") and v."+defaultCol.toLowerCase() + "=" + defaultStakeId + " and v."+stringCol + "='" + stringValue + "' and  substr(v.datecreate, 0 , 10)  =  TO_DATE("+"'"+DateTo+"','MM/DD/YYYY'"+")";
			  }
			  else
				  sql = sql + " and substr(v.datecreate, 0 , 10)  =  TO_DATE("+"'"+DateTo+"','MM/DD/YYYY'"+")";
		  }
		sql = sql +" order by v.datecreate desc";
		reportList =  hibernateTemplate.find(sql);
		
		return reportList;
	}
	
	@SuppressWarnings("unchecked")
	public List<Eattacatalog> getInvoiceSummary(String defaultCol, Long defaultStakeId, String stakeholderClmn, 
			Long Stakeholderid, String statusId, String Datefrom, String DateTo, String clmn, String Criteria, String strItem) {
		
		List<Eattacatalog> reportList = new ArrayList<Eattacatalog>();
		
		String sql="";
		if(Criteria.equals("like") || Criteria=="like")
		  {
			if(!strItem.equals(""))
			  {
				strItem=strItem+"%";
			  }
		  }
		if(strItem.equals("") || strItem == "")
		  {
			sql="from Eattacatalog v where v."+stakeholderClmn.toLowerCase() + "=" + Stakeholderid +" and v.teastatus IN ("+ statusId +") and v."+defaultCol.toLowerCase() + "=" + defaultStakeId;
		  }
		else
			sql="from Eattacatalog v where v."+stakeholderClmn.toLowerCase() + "=" + Stakeholderid +" and v.teastatus IN ("+ statusId +") and v."+defaultCol.toLowerCase() + "=" + defaultStakeId + " and  v."+clmn + " " + Criteria + " '"+strItem+"'";
		if (Datefrom !="" && DateTo !="")
		  {
			  if(strItem.equals(""))
			  {
				  sql ="from Eattacatalog v where v."+stakeholderClmn.toLowerCase() + "=" + Stakeholderid +" and v.teastatus IN ("+ statusId +") and v."+defaultCol.toLowerCase() + "=" + defaultStakeId + " and  v.datecreate between  TO_DATE("+"'"+Datefrom+"','MM/DD/YYYY'"+") and TO_DATE("+"'"+DateTo+"','MM/DD/YYYY'"+")";
			  }
			  else
				  sql += " and datecreate between  TO_DATE("+"'"+Datefrom+"','MM/DD/YYYY'"+") and TO_DATE("+"'"+DateTo+"','MM/DD/YYYY'"+")";
		  }
		  else if (Datefrom !="" && DateTo =="")
		  {
			  if(strItem.equals("") || strItem == "")
			  {
				  sql ="from Eattacatalog v where v."+stakeholderClmn.toLowerCase() + "=" + Stakeholderid +" and v.teastatus IN ("+ statusId +") and v."+defaultCol.toLowerCase() + "=" + defaultStakeId + " and substr(v.datecreate, 0 , 10)  =  TO_DATE("+"'"+Datefrom+"','MM/DD/YYYY'"+")";
			  }
			  else
				  sql +=  " and substr(v.datecreate, 0 , 10)  =  TO_DATE("+"'"+Datefrom+"','MM/DD/YYYY'"+")";
		  }
		  else if (Datefrom =="" && DateTo !="")
		  {
			  if(strItem.equals(""))
			  {
				  sql ="from Eattacatalog v where v."+stakeholderClmn.toLowerCase() + "=" + Stakeholderid +" and v.teastatus IN ("+ statusId +") and v."+defaultCol.toLowerCase() + "=" + defaultStakeId + " and  substr(v.datecreate, 0 , 10)  =  TO_DATE("+"'"+DateTo+"','MM/DD/YYYY'"+")";
			  }
			  else
				  sql = sql + " and substr(v.datecreate, 0 , 10)  =  TO_DATE("+"'"+DateTo+"','MM/DD/YYYY'"+")";
		  }
		sql = sql +" order by v.datecreate desc";
		reportList =  hibernateTemplate.find(sql);
		
		return reportList;
	}

	@SuppressWarnings("unchecked")
	public List<Object> getLotDetails(String stakeholderClmn, Long Stakeholderid, String statusId, 
			String Datefrom, String DateTo, String clmn, String Criteria, String strItem) {
		
		List<Object> reportList = new ArrayList<Object>();
		
		String sql="";
		if(Criteria.equals("like") || Criteria=="like")
		  {
			if(!strItem.equals(""))
			  {
				strItem=strItem+"%";
			  }
		  }
		if(strItem.equals("") || strItem == "")
		  {
			sql="from Splitlots v where v."+stakeholderClmn.toLowerCase() + "=" + Stakeholderid +" and v.teastatus IN ("+ statusId +")";
		  }
		else
			sql="from Splitlots v where v."+stakeholderClmn.toLowerCase() + "=" + Stakeholderid +" and v.teastatus IN ("+ statusId +") and  v."+clmn + " " + Criteria + " '"+strItem+"'";
		if (Datefrom !="" && DateTo !="")
		  {
			  if(strItem.equals(""))
			  {
				  sql ="from Splitlots v where v."+stakeholderClmn.toLowerCase() + "=" + Stakeholderid +" and v.teastatus IN ("+ statusId +") and  v.datecreate between  TO_DATE("+"'"+Datefrom+"','MM/DD/YYYY'"+") and TO_DATE("+"'"+DateTo+"','MM/DD/YYYY'"+")";
			  }
			  else
				  sql += " and datecreate between  TO_DATE("+"'"+Datefrom+"','MM/DD/YYYY'"+") and TO_DATE("+"'"+DateTo+"','MM/DD/YYYY'"+")";
		  }
		  else if (Datefrom !="" && DateTo =="")
		  {
			  if(strItem.equals("") || strItem == "")
			  {
				  sql ="from Splitlots v where v."+stakeholderClmn.toLowerCase() + "=" + Stakeholderid +" and v.teastatus IN ("+ statusId +") and substr(v.datecreate, 0 , 10)  =  TO_DATE("+"'"+Datefrom+"','MM/DD/YYYY'"+")";
			  }
			  else
				  sql +=  " and substr(v.datecreate, 0 , 10)  =  TO_DATE("+"'"+Datefrom+"','MM/DD/YYYY'"+")";
		  }
		  else if (Datefrom =="" && DateTo !="")
		  {
			  if(strItem.equals(""))
			  {
				  sql ="from Splitlots v where v."+stakeholderClmn.toLowerCase() + "=" + Stakeholderid +" and v.teastatus IN ("+ statusId +") and  substr(v.datecreate, 0 , 10)  =  TO_DATE("+"'"+DateTo+"','MM/DD/YYYY'"+")";
			  }
			  else
				  sql = sql + " and substr(v.datecreate, 0 , 10)  =  TO_DATE("+"'"+DateTo+"','MM/DD/YYYY'"+")";
		  }
		sql = sql +" order by v.datecreate desc";
		reportList =  hibernateTemplate.find(sql);
		
		return reportList;		
	}

	@SuppressWarnings("unchecked")
	public List<Object> getLotDetails(String statusId, String Datefrom, String DateTo, String clmn, String Criteria, String strItem) {
		
		List<Object> reportList = new ArrayList<Object>();		
		String sql="";
		if(Criteria.equals("like") || Criteria=="like")
		  {
			if(!strItem.equals(""))
			  {
				strItem=strItem+"%";
			  }
		  }
		if(strItem.equals("") || strItem == "")
		  {
			sql="from Splitlots v where v.teastatus IN ("+ statusId +")";
		  }
		else
			sql="from Splitlots v where v.teastatus IN ("+ statusId +") and  v."+clmn + " " + Criteria + " '"+strItem+"'";
		if (Datefrom !="" && DateTo !="")
		  {
			  if(strItem.equals(""))
			  {
				  sql ="from Splitlots v where v.teastatus IN ("+ statusId +") and  v.datecreate between  TO_DATE("+"'"+Datefrom+"','MM/DD/YYYY'"+") and TO_DATE("+"'"+DateTo+"','MM/DD/YYYY'"+")";
			  }
			  else
				  sql += " and datecreate between  TO_DATE("+"'"+Datefrom+"','MM/DD/YYYY'"+") and TO_DATE("+"'"+DateTo+"','MM/DD/YYYY'"+")";
		  }
		  else if (Datefrom !="" && DateTo =="")
		  {
			  if(strItem.equals(""))
			  {
				  sql ="from Splitlots v where v.teastatus IN ("+ statusId +") and substr(v.datecreate, 0 , 10)  =  TO_DATE("+"'"+Datefrom+"','MM/DD/YYYY'"+")";
			  }
			  else
				  sql +=  " and substr(v.datecreate, 0 , 10)  =  TO_DATE("+"'"+Datefrom+"','MM/DD/YYYY'"+")";
		  }
		  else if (Datefrom =="" && DateTo !="")
		  {
			  if(strItem.equals(""))
			  {
				  sql ="from Splitlots v where v.teastatus IN ("+ statusId +") and  substr(v.datecreate, 0 , 10)  =  TO_DATE("+"'"+DateTo+"','MM/DD/YYYY'"+")";
			  }
			  else
				  sql = sql + " and substr(v.datecreate, 0 , 10)  =  TO_DATE("+"'"+DateTo+"','MM/DD/YYYY'"+")";
		  }
		sql = sql +" order by v.datecreate desc";
		reportList =  hibernateTemplate.find(sql);
		
		return reportList;
	}


	@SuppressWarnings("unchecked")
	public List<Splitlots> getDistinctBuyers(String teastatusid, String stakeholderClmn, Long stakeholderid) {		
		List<Splitlots> catalog = new ArrayList<Splitlots>();
		
		List<Long> records = getHibernateTemplate().find("select distinct ls.buyerid from Splitlots ls where ls.teastatus IN ("+teastatusid+") and ls." + stakeholderClmn.toLowerCase() + "=? and ls.buyerid is not null", stakeholderid);
		if (records.isEmpty())
		{			
			return catalog;
		} else {
			for(Long b:records)
			{							
				List<Splitlots> cat = getHibernateTemplate().find("from Splitlots ls where ls.teastatus IN ("+teastatusid+") and ls.buyerid=?", b);
				Splitlots splitlots=cat.get(0);
				splitlots.setNoRecords(cat.size());
//				catalog.add(cat.get(0));
				catalog.add(splitlots);
			}
			return catalog;
		}
	}

	@SuppressWarnings("unchecked")
	public List<Splitlots> getDistinctInvoices(String teastatus, String stakeholderClmn, Long stakeholderid) {		
		List<Splitlots> catalog = new ArrayList<Splitlots>();
		
		List<String> records = getHibernateTemplate().find("select distinct ls.invoice from Splitlots ls where ls.teastatus IN ("+teastatus+") and ls." + stakeholderClmn.toLowerCase() + "=? and ls.buyerid is not null", stakeholderid);
		if (records.isEmpty())
		{			
			return catalog;
		} else {
			for(String b:records)
			{			
				Object[] params = new Object[]{b, stakeholderid};
				List<Splitlots> cat = getHibernateTemplate().find("from Splitlots ls where ls.teastatus IN ("+teastatus+") and ls.invoice=? and ls." + stakeholderClmn.toLowerCase() + "=?", params);
				catalog.add(cat.get(0));
			}
			return catalog;
		}
	}

	@SuppressWarnings("unchecked")
	public List<Splitlots> getByInvoice(String teastatus, String stakeholderClmn, Long stakeholderid, String invoicenumber) {		
		Object[] params = new Object[]{stakeholderid, invoicenumber};
		List<Splitlots> records = getHibernateTemplate().find("from Splitlots ls where ls."+stakeholderClmn+"=? and ls.invoice=? and ls.teastatus IN ("+teastatus+")", params);
		if (records.isEmpty())
		{
			List<Splitlots> noRecords = new ArrayList<Splitlots>();
			return noRecords;
		} else {
			return records;
		}
	}

	@SuppressWarnings("unchecked")
	public List<Splitlots> getSplitLots(String defaultstakecol,Long defaultstakeid, String searchstakecol, Long searchstakeid,String teastatuslise) {		
		Object[] params = new Object[]{defaultstakeid, searchstakeid};
		List<Splitlots> records = getHibernateTemplate().find("from Splitlots ls where ls."+defaultstakecol+"=? and ls."+searchstakecol+"=? and ls.teastatus IN ("+teastatuslise+")", params);
		if (records.isEmpty())
		{
			List<Splitlots> noRecords = new ArrayList<Splitlots>();
			return noRecords;
		} else {
			return records;
		}
	}	

	@SuppressWarnings("unchecked")
	public List<Splitlots> getDistinctDeliveryOrders(String teastatus, String stakeholderClmn, Long stakeholderid) {
		List<Splitlots> catalog = new ArrayList<Splitlots>();
		
		List<String> records = getHibernateTemplate().find("select distinct ls.deliveryordernumber from Splitlots ls where ls.teastatus IN ("+teastatus+") and ls." + stakeholderClmn.toLowerCase() + "=? and ls.buyerid is not null", stakeholderid);
		if (records.isEmpty())
		{			
			return catalog;
		} else {
			for(String b:records)
			{			
				Object[] params = new Object[]{b, stakeholderid};
				List<Splitlots> cat = getHibernateTemplate().find("from Splitlots ls where ls.teastatus IN ("+teastatus+") and ls.deliveryordernumber=? and ls." + stakeholderClmn.toLowerCase() + "=?", params);
				catalog.add(cat.get(0));
			}
			return catalog;
		}
	}	

	@SuppressWarnings("unchecked")
	public List<Splitlots> getByDeliveryOrderNumber(String teastatus, String stakeholderClmn, Long stakeholderid, String deliveryordernumber) {
		Object[] params = new Object[]{stakeholderid, deliveryordernumber};
		List<Splitlots> records = getHibernateTemplate().find("from Splitlots ls where ls."+stakeholderClmn+"=? and ls.deliveryordernumber=? and ls.teastatus IN ("+teastatus+")", params);
		if (records.isEmpty())
		{
			List<Splitlots> noRecords = new ArrayList<Splitlots>();
			return noRecords;
		} else {
			return records;
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<Splitlots> getLotsDetails(String stakeholdercol, Long stakeholderid, String teastatuslist) {		
		List<Splitlots> records = getHibernateTemplate().find("from Splitlots sl where sl."+stakeholdercol+"=? and sl.teastatus IN ("+teastatuslist+")", stakeholderid);
		if (records.isEmpty())
		{
			List<Splitlots> noRecords = new ArrayList<Splitlots>();
			return noRecords;
		} else {
			return records;
		}
	}

	@SuppressWarnings("unchecked")
	public List<Splitlots> getByBankAndStatus(String teastatuslist, String bank) {
		List<Splitlots> records = getHibernateTemplate().find("from Splitlots sl where sl.teastatus IN ("+teastatuslist+") and sl.bank=?", bank);
		if (records.isEmpty())
		{
			List<Splitlots> noRecords = new ArrayList<Splitlots>();
			return noRecords;
		} else {
			return records;
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<Splitlots> getByBankAndStatus(String teastatuslist, String bank, 
			String Datefrom, String DateTo, String clmn, String Criteria, String strItem) {
		
		List<Splitlots> reportList = new ArrayList<Splitlots>();
		
		String sql="";
		if(Criteria.equals("like") || Criteria=="like")
		  {
			if(!strItem.equals(""))
			  {
				strItem=strItem+"%";
			  }
		  }
		if(strItem.equals("") || strItem == "")
		  {
			sql="from Splitlots v where v.teastatus IN ("+ teastatuslist +") and v.bank='" + bank +"'";
		  }
		else
			sql="from Splitlots v where v.teastatus IN ("+ teastatuslist +") and v.bank='" + bank +"' and  v."+clmn + " " + Criteria + " '"+strItem+"'";
		if (Datefrom !="" && DateTo !="")
		  {
			  if(strItem.equals(""))
			  {
				  sql ="from Splitlots v where v.teastatus IN ("+ teastatuslist +") and v.bank='" + bank +"' and  v.datecreate between  TO_DATE("+"'"+Datefrom+"','MM/DD/YYYY'"+") and TO_DATE("+"'"+DateTo+"','MM/DD/YYYY'"+")";
			  }
			  else
				  sql += " and datecreate between  TO_DATE("+"'"+Datefrom+"','MM/DD/YYYY'"+") and TO_DATE("+"'"+DateTo+"','MM/DD/YYYY'"+")";
		  }
		  else if (Datefrom !="" && DateTo =="")
		  {
			  if(strItem.equals("") || strItem == "")
			  {
				  sql ="from Splitlots v where v.teastatus IN ("+ teastatuslist +") and v.bank='" + bank +"' and substr(v.datecreate, 0 , 10)  =  TO_DATE("+"'"+Datefrom+"','MM/DD/YYYY'"+")";
			  }
			  else
				  sql +=  " and substr(v.datecreate, 0 , 10)  =  TO_DATE("+"'"+Datefrom+"','MM/DD/YYYY'"+")";
		  }
		  else if (Datefrom =="" && DateTo !="")
		  {
			  if(strItem.equals(""))
			  {
				  sql ="from Splitlots v where v.teastatus IN ("+ teastatuslist +") and v.bank='" + bank +"' and  substr(v.datecreate, 0 , 10)  =  TO_DATE("+"'"+DateTo+"','MM/DD/YYYY'"+")";
			  }
			  else
				  sql = sql + " and substr(v.datecreate, 0 , 10)  =  TO_DATE("+"'"+DateTo+"','MM/DD/YYYY'"+")";
		  }
		sql = sql +" order by v.datecreate desc";
		reportList =  hibernateTemplate.find(sql);
		
		return reportList;		
	}

	@SuppressWarnings("unchecked")
	public List<Splitlots> getOtherBanksCatalogs(String teastatuslist, String bank) {
		List<Splitlots> records = getHibernateTemplate().find("from Splitlots sl where sl.teastatus IN ("+teastatuslist+") and sl.bank<>?", bank);
		if (records.isEmpty())
		{
			List<Splitlots> noRecords = new ArrayList<Splitlots>();
			return noRecords;
		} else {
			return records;
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<Splitlots> getOtherBanksCatalogs(String teastatuslist, String bank, String Datefrom, String DateTo, String clmn,
			String Criteria, String strItem) {
		
		List<Splitlots> reportList = new ArrayList<Splitlots>();
		
		String sql="";
		if(Criteria.equals("like") || Criteria=="like")
		  {
			if(!strItem.equals(""))
			  {
				strItem=strItem+"%";
			  }
		  }
		if(strItem.equals("") || strItem == "")
		  {
			sql="from Splitlots v where v.teastatus IN ("+ teastatuslist +") and v.bank<>'" + bank +"'";
		  }
		else
			sql="from Splitlots v where v.teastatus IN ("+ teastatuslist +") and v.bank<>'" + bank +"' and  v."+clmn + " " + Criteria + " '"+strItem+"'";
		if (Datefrom !="" && DateTo !="")
		  {
			  if(strItem.equals(""))
			  {
				  sql ="from Splitlots v where v.teastatus IN ("+ teastatuslist +") and v.bank<>'" + bank +"' and  v.datecreate between  TO_DATE("+"'"+Datefrom+"','MM/DD/YYYY'"+") and TO_DATE("+"'"+DateTo+"','MM/DD/YYYY'"+")";
			  }
			  else
				  sql += " and datecreate between  TO_DATE("+"'"+Datefrom+"','MM/DD/YYYY'"+") and TO_DATE("+"'"+DateTo+"','MM/DD/YYYY'"+")";
		  }
		  else if (Datefrom !="" && DateTo =="")
		  {
			  if(strItem.equals("") || strItem == "")
			  {
				  sql ="from Splitlots v where v.teastatus IN ("+ teastatuslist +") and v.bank<>'" + bank +"' and substr(v.datecreate, 0 , 10)  =  TO_DATE("+"'"+Datefrom+"','MM/DD/YYYY'"+")";
			  }
			  else
				  sql +=  " and substr(v.datecreate, 0 , 10)  =  TO_DATE("+"'"+Datefrom+"','MM/DD/YYYY'"+")";
		  }
		  else if (Datefrom =="" && DateTo !="")
		  {
			  if(strItem.equals(""))
			  {
				  sql ="from Splitlots v where v.teastatus IN ("+ teastatuslist +") and v.bank<>'" + bank +"' and  substr(v.datecreate, 0 , 10)  =  TO_DATE("+"'"+DateTo+"','MM/DD/YYYY'"+")";
			  }
			  else
				  sql = sql + " and substr(v.datecreate, 0 , 10)  =  TO_DATE("+"'"+DateTo+"','MM/DD/YYYY'"+")";
		  }
		sql = sql +" order by v.datecreate desc";
		reportList =  hibernateTemplate.find(sql);
		
		return reportList;
	}

	@SuppressWarnings("unchecked")
	public List<Eattacatalog> getCatalogByBank(String teastatuslist, String bank) {
		List<Eattacatalog> records = new ArrayList<Eattacatalog>();
		log.debug("\n ----is here?");
		if(bank.toLowerCase().contains("all")){
	//	List<Eattacatalog> records = getHibernateTemplate().find("from Eattacatalog ec where ec.teastatus IN ("+teastatuslist+") and ec.bank=?", bank);
		records = getHibernateTemplate().find("from Eattacatalog ec where ec.teastatus IN ("+teastatuslist+") order by ec.bank");
		log.debug("\n ----records ? "+records.size());
		}else{
			records = getHibernateTemplate().find("from Eattacatalog ec where ec.teastatus IN ("+teastatuslist+") and ec.bank=?", bank);
		}
		if (records.isEmpty())
		{
			List<Eattacatalog> noRecords = new ArrayList<Eattacatalog>();
			return noRecords;
		} else {
			return records;
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<Eattacatalog> getCatalogByBank(String teastatuslist, String bank, 
			String Datefrom, String DateTo, String clmn, String Criteria, String strItem) {
		
		List<Eattacatalog> reportList = new ArrayList<Eattacatalog>();
		
		String sql="";
		if(bank.toLowerCase().contains("all")){
			
			
			if(Criteria.equals("like") || Criteria=="like")
			  {
				if(!strItem.equals(""))
				  {
					strItem=strItem+"%";
				  }
			  }
			if(strItem.equals("") || strItem == "")
			  {
				//sql="from Eattacatalog v where v.teastatus IN ("+ teastatuslist +") and v.bank='" + bank +"'";
				sql="from Eattacatalog v where v.teastatus IN ("+teastatuslist+")";
			  }
			else
				sql="from Eattacatalog v where v.teastatus IN ("+ teastatuslist +") and  v."+clmn + " " + Criteria + " '"+strItem+"'";
			if (Datefrom !="" && DateTo !="")
			  {
				  if(strItem.equals(""))
				  {
					  sql ="from Eattacatalog v where v.teastatus IN ("+ teastatuslist +")  and  v.datecreate between  TO_DATE("+"'"+Datefrom+"','MM/DD/YYYY'"+") and TO_DATE("+"'"+DateTo+"','MM/DD/YYYY'"+")";
				  }
				  else
					  sql += " and datecreate between  TO_DATE("+"'"+Datefrom+"','MM/DD/YYYY'"+") and TO_DATE("+"'"+DateTo+"','MM/DD/YYYY'"+")";
			  }
			  else if (Datefrom !="" && DateTo =="")
			  {
				  if(strItem.equals("") || strItem == "")
				  {
					  sql ="from Eattacatalog v where v.teastatus IN ("+ teastatuslist +") and substr(v.datecreate, 0 , 10)  =  TO_DATE("+"'"+Datefrom+"','MM/DD/YYYY'"+")";
				  }
				  else
					  sql +=  " and substr(v.datecreate, 0 , 10)  =  TO_DATE("+"'"+Datefrom+"','MM/DD/YYYY'"+")";
			  }
			  else if (Datefrom =="" && DateTo !="")
			  {
				  if(strItem.equals(""))
				  {
					  sql ="from Eattacatalog v where v.teastatus IN ("+ teastatuslist +") and  substr(v.datecreate, 0 , 10)  =  TO_DATE("+"'"+DateTo+"','MM/DD/YYYY'"+")";
				  }
				  else
					  sql = sql + " and substr(v.datecreate, 0 , 10)  =  TO_DATE("+"'"+DateTo+"','MM/DD/YYYY'"+")";
			  }
			sql = sql +" order by v.bank, v.datecreate desc  ";
			reportList =  hibernateTemplate.find(sql);
			
			
			
			
			
			
		}else{
			
		
		
		
		
		
		
		
		
		
		
		
		
		
		if(Criteria.equals("like") || Criteria=="like")
		  {
			if(!strItem.equals(""))
			  {
				strItem=strItem+"%";
			  }
		  }
		if(strItem.equals("") || strItem == "")
		  {
			sql="from Eattacatalog v where v.teastatus IN ("+ teastatuslist +") and v.bank='" + bank +"'";
		  }
		else
			sql="from Eattacatalog v where v.teastatus IN ("+ teastatuslist +") and v.bank='" + bank +"' and  v."+clmn + " " + Criteria + " '"+strItem+"'";
		if (Datefrom !="" && DateTo !="")
		  {
			  if(strItem.equals(""))
			  {
				  sql ="from Eattacatalog v where v.teastatus IN ("+ teastatuslist +") and v.bank='" + bank +"' and  v.datecreate between  TO_DATE("+"'"+Datefrom+"','MM/DD/YYYY'"+") and TO_DATE("+"'"+DateTo+"','MM/DD/YYYY'"+")";
			  }
			  else
				  sql += " and datecreate between  TO_DATE("+"'"+Datefrom+"','MM/DD/YYYY'"+") and TO_DATE("+"'"+DateTo+"','MM/DD/YYYY'"+")";
		  }
		  else if (Datefrom !="" && DateTo =="")
		  {
			  if(strItem.equals("") || strItem == "")
			  {
				  sql ="from Eattacatalog v where v.teastatus IN ("+ teastatuslist +") and v.bank='" + bank +"' and substr(v.datecreate, 0 , 10)  =  TO_DATE("+"'"+Datefrom+"','MM/DD/YYYY'"+")";
			  }
			  else
				  sql +=  " and substr(v.datecreate, 0 , 10)  =  TO_DATE("+"'"+Datefrom+"','MM/DD/YYYY'"+")";
		  }
		  else if (Datefrom =="" && DateTo !="")
		  {
			  if(strItem.equals(""))
			  {
				  sql ="from Eattacatalog v where v.teastatus IN ("+ teastatuslist +") and v.bank='" + bank +"' and  substr(v.datecreate, 0 , 10)  =  TO_DATE("+"'"+DateTo+"','MM/DD/YYYY'"+")";
			  }
			  else
				  sql = sql + " and substr(v.datecreate, 0 , 10)  =  TO_DATE("+"'"+DateTo+"','MM/DD/YYYY'"+")";
		  }
		sql = sql +" order by v.datecreate desc";
		reportList =  hibernateTemplate.find(sql);
		}
		return reportList;		
	}

	@SuppressWarnings("unchecked")
	public List<Eattacatalog> getCatalogOtherBanks(String teastatuslist, String bank) {
		List<Eattacatalog> records = getHibernateTemplate().find("from Eattacatalog ec where ec.teastatus IN ("+teastatuslist+") and ec.bank<>?", bank);
		if (records.isEmpty())
		{
			List<Eattacatalog> noRecords = new ArrayList<Eattacatalog>();
			return noRecords;
		} else {
			return records;
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<Eattacatalog> getCatalogOtherBanks(String teastatuslist, String bank, String Datefrom, String DateTo, String clmn,
			String Criteria, String strItem) {
		
		List<Eattacatalog> reportList = new ArrayList<Eattacatalog>();
		
		String sql="";
		if(Criteria.equals("like") || Criteria=="like")
		  {
			if(!strItem.equals(""))
			  {
				strItem=strItem+"%";
			  }
		  }
		if(strItem.equals("") || strItem == "")
		  {
			sql="from Eattacatalog v where v.teastatus IN ("+ teastatuslist +") and v.bank<>'" + bank +"'";
		  }
		else
			sql="from Eattacatalog v where v.teastatus IN ("+ teastatuslist +") and v.bank<>'" + bank +"' and  v."+clmn + " " + Criteria + " '"+strItem+"'";
		if (Datefrom !="" && DateTo !="")
		  {
			  if(strItem.equals(""))
			  {
				  sql ="from Eattacatalog v where v.teastatus IN ("+ teastatuslist +") and v.bank<>'" + bank +"' and  v.datecreate between  TO_DATE("+"'"+Datefrom+"','MM/DD/YYYY'"+") and TO_DATE("+"'"+DateTo+"','MM/DD/YYYY'"+")";
			  }
			  else
				  sql += " and datecreate between  TO_DATE("+"'"+Datefrom+"','MM/DD/YYYY'"+") and TO_DATE("+"'"+DateTo+"','MM/DD/YYYY'"+")";
		  }
		  else if (Datefrom !="" && DateTo =="")
		  {
			  if(strItem.equals("") || strItem == "")
			  {
				  sql ="from Eattacatalog v where v.teastatus IN ("+ teastatuslist +") and v.bank<>'" + bank +"' and substr(v.datecreate, 0 , 10)  =  TO_DATE("+"'"+Datefrom+"','MM/DD/YYYY'"+")";
			  }
			  else
				  sql +=  " and substr(v.datecreate, 0 , 10)  =  TO_DATE("+"'"+Datefrom+"','MM/DD/YYYY'"+")";
		  }
		  else if (Datefrom =="" && DateTo !="")
		  {
			  if(strItem.equals(""))
			  {
				  sql ="from Eattacatalog v where v.teastatus IN ("+ teastatuslist +") and v.bank<>'" + bank +"' and  substr(v.datecreate, 0 , 10)  =  TO_DATE("+"'"+DateTo+"','MM/DD/YYYY'"+")";
			  }
			  else
				  sql = sql + " and substr(v.datecreate, 0 , 10)  =  TO_DATE("+"'"+DateTo+"','MM/DD/YYYY'"+")";
		  }
		sql = sql +" order by v.datecreate desc";
		reportList =  hibernateTemplate.find(sql);
		
		return reportList;
	}

	@SuppressWarnings("unchecked")
	public List<String> getDistinctAuctions(String teastatuslist, String stakeholdercol, Long stakeholderid) {
		List<String> records = getHibernateTemplate().find("select distinct sl.auctionnumber from Splitlots sl where sl.teastatus IN ("+teastatuslist+") and sl."+stakeholdercol+"=?", stakeholderid);
		if (records.isEmpty())
		{
			List<String> noRecords = new ArrayList<String>();
			return noRecords;
		} else {
			return records;
		}
	}

	@SuppressWarnings("unchecked")
	public List<Splitlots> getAuctionResults(String teastatuslist, String stakeholdercol, Long stakeholderid, String auctionnumber) {
		Object[] params = new Object[]{auctionnumber, stakeholderid};
		List<Splitlots> records = getHibernateTemplate().find("from Splitlots sl where sl.auctionnumber=? and sl.teastatus IN ("+teastatuslist+") and sl."+stakeholdercol+"=?", params);
		if (records.isEmpty())
		{
			List<Splitlots> noRecords = new ArrayList<Splitlots>();
			return noRecords;
		} else {
			return records;
		}
	}

	@SuppressWarnings("unchecked")
	public List<Splitlots> getDistinctStakeholders(String teastatuslist, String stakeholdercol, Long stakeholderid, String coltoget) {
		List<Splitlots> catalog = new ArrayList<Splitlots>();		
		List<Long> records = getHibernateTemplate().find("select distinct sl." + coltoget.toLowerCase() + " from Splitlots sl where sl.teastatus IN ("+teastatuslist+") and sl." + stakeholdercol.toLowerCase() + "=? and sl." + coltoget.toLowerCase() + " is not null", stakeholderid);		
		if (records.isEmpty())
		{			
			return catalog;
		} else {
			for(Long b:records)
			{			
				Object[] params = new Object[]{b, stakeholderid};
				List<Splitlots> cat = getHibernateTemplate().find("from Splitlots sl where sl.teastatus IN ("+teastatuslist+") and sl." + coltoget.toLowerCase() + "=? and sl." + stakeholdercol.toLowerCase() + "=?", params);				
				catalog.add(cat.get(0));
			}			
			return catalog;
		}
	}

	@SuppressWarnings("unchecked")
	public List<Object> getLotsDetails(String defaultCol, Long defaultStakeId, String stakeholderClmn, Long Stakeholderid, 
			String statuslist, String Datefrom, String DateTo, String clmn, String Criteria, String strItem) {
		
		List<Object> reportList = new ArrayList<Object>();
		
		String sql="";
		if(Criteria.equals("like") || Criteria=="like")
		  {
			if(!strItem.equals(""))
			  {
				strItem=strItem+"%";
			  }
		  }
		if(strItem.equals("") || strItem == "")
		  {
			sql="from Splitlots v where v."+stakeholderClmn.toLowerCase() + "=" + Stakeholderid +" and v.teastatus IN ("+ statuslist +") and v."+defaultCol.toLowerCase() + "=" + defaultStakeId;
		  }
		else
			sql="from Splitlots v where v."+stakeholderClmn.toLowerCase() + "=" + Stakeholderid +" and v.teastatus IN ("+ statuslist +") and v."+defaultCol.toLowerCase() + "=" + defaultStakeId + " and  v."+clmn + " " + Criteria + " '"+strItem+"'";
		if (Datefrom !="" && DateTo !="")
		  {
			  if(strItem.equals(""))
			  {
				  sql ="from Splitlots v where v."+stakeholderClmn.toLowerCase() + "=" + Stakeholderid +" and v.teastatus IN ("+ statuslist +") and v."+defaultCol.toLowerCase() + "=" + defaultStakeId + " and  v.datecreate between  TO_DATE("+"'"+Datefrom+"','MM/DD/YYYY'"+") and TO_DATE("+"'"+DateTo+"','MM/DD/YYYY'"+")";
			  }
			  else
				  sql += " and datecreate between  TO_DATE("+"'"+Datefrom+"','MM/DD/YYYY'"+") and TO_DATE("+"'"+DateTo+"','MM/DD/YYYY'"+")";
		  }
		  else if (Datefrom !="" && DateTo =="")
		  {
			  if(strItem.equals("") || strItem == "")
			  {
				  sql ="from Splitlots v where v."+stakeholderClmn.toLowerCase() + "=" + Stakeholderid +" and v.teastatus IN ("+ statuslist +") and v."+defaultCol.toLowerCase() + "=" + defaultStakeId + " and substr(v.datecreate, 0 , 10)  =  TO_DATE("+"'"+Datefrom+"','MM/DD/YYYY'"+")";
			  }
			  else
				  sql +=  " and substr(v.datecreate, 0 , 10)  =  TO_DATE("+"'"+Datefrom+"','MM/DD/YYYY'"+")";
		  }
		  else if (Datefrom =="" && DateTo !="")
		  {
			  if(strItem.equals(""))
			  {
				  sql ="from Splitlots v where v."+stakeholderClmn.toLowerCase() + "=" + Stakeholderid +" and v.teastatus IN ("+ statuslist +") and v."+defaultCol.toLowerCase() + "=" + defaultStakeId + " and  substr(v.datecreate, 0 , 10)  =  TO_DATE("+"'"+DateTo+"','MM/DD/YYYY'"+")";
			  }
			  else
				  sql = sql + " and substr(v.datecreate, 0 , 10)  =  TO_DATE("+"'"+DateTo+"','MM/DD/YYYY'"+")";
		  }
		sql = sql +" order by v.datecreate desc";
		reportList =  hibernateTemplate.find(sql);
		
		return reportList;
	}

	@SuppressWarnings("unchecked")
	public List<Splitlots> getDistinctStakeholders(String boolCol, Integer boolValue, Long stakeholderid, String stakeholderClmn, String colToGet) {
		List<Splitlots> catalog = new ArrayList<Splitlots>();		
		try {			
			List<Long> records = getHibernateTemplate().find("select distinct sl." + colToGet.toLowerCase() + " from Splitlots sl where sl." + boolCol.toLowerCase() + "=1 and sl." + stakeholderClmn.toLowerCase() + "=? and sl." + colToGet.toLowerCase() + " is not null", stakeholderid);			
			if (!records.isEmpty())
			{			
				for(Long b:records)
				{		
					Object[] params2 = new Object[]{b, stakeholderid};
					List<Splitlots> cat = getHibernateTemplate().find("from Splitlots sl where sl." + boolCol.toLowerCase() + "=1 and sl." + colToGet.toLowerCase() + "=? and sl." + stakeholderClmn.toLowerCase() + "=?", params2);				
					catalog.add(cat.get(0));
				}
			} 
		} catch (Exception e) {
			LogFile.LogError(e);
		}
		return catalog;
	}
	
	@SuppressWarnings("unchecked")
	public List<Object> getLotsDetails(String defaultCol, Long defaultStakeId, String stakeholderClmn, 
			Long Stakeholderid, String boolCol, Boolean boolValue, String Datefrom, String DateTo, String clmn, String Criteria, String strItem) {
		
		List<Object> reportList = new ArrayList<Object>();
		
		String sql="";
		if(Criteria.equals("like") || Criteria=="like")
		  {
			if(!strItem.equals(""))
			  {
				strItem=strItem+"%";
			  }
		  }
		if(strItem.equals("") || strItem == "")
		  {
			sql="from Splitlots v where v."+stakeholderClmn.toLowerCase() + "=" + Stakeholderid +" and v."+boolCol.toLowerCase() + "=1 and v."+defaultCol.toLowerCase() + "=" + defaultStakeId;
		  }
		else
			sql="from Splitlots v where v."+stakeholderClmn.toLowerCase() + "=" + Stakeholderid +" and v."+boolCol.toLowerCase() + "=1 and v."+defaultCol.toLowerCase() + "=" + defaultStakeId + " and  v."+clmn + " " + Criteria + " '"+strItem+"'";
		if (Datefrom !="" && DateTo !="")
		  {
			  if(strItem.equals(""))
			  {
				  sql ="from Splitlots v where v."+stakeholderClmn.toLowerCase() + "=" + Stakeholderid +" and v."+boolCol.toLowerCase() + "=1 and v."+defaultCol.toLowerCase() + "=" + defaultStakeId + " and  v.datecreate between  TO_DATE("+"'"+Datefrom+"','MM/DD/YYYY'"+") and TO_DATE("+"'"+DateTo+"','MM/DD/YYYY'"+")";
			  }
			  else
				  sql += " and datecreate between  TO_DATE("+"'"+Datefrom+"','MM/DD/YYYY'"+") and TO_DATE("+"'"+DateTo+"','MM/DD/YYYY'"+")";
		  }
		  else if (Datefrom !="" && DateTo =="")
		  {
			  if(strItem.equals("") || strItem == "")
			  {
				  sql ="from Splitlots v where v."+stakeholderClmn.toLowerCase() + "=" + Stakeholderid +" and v."+boolCol.toLowerCase() + "=1 and v."+defaultCol.toLowerCase() + "=" + defaultStakeId + " and substr(v.datecreate, 0 , 10)  =  TO_DATE("+"'"+Datefrom+"','MM/DD/YYYY'"+")";
			  }
			  else
				  sql +=  " and substr(v.datecreate, 0 , 10)  =  TO_DATE("+"'"+Datefrom+"','MM/DD/YYYY'"+")";
		  }
		  else if (Datefrom =="" && DateTo !="")
		  {
			  if(strItem.equals(""))
			  {
				  sql ="from Splitlots v where v."+stakeholderClmn.toLowerCase() + "=" + Stakeholderid +" and v."+boolCol.toLowerCase() + "=1 and v."+defaultCol.toLowerCase() + "=" + defaultStakeId + " and  substr(v.datecreate, 0 , 10)  =  TO_DATE("+"'"+DateTo+"','MM/DD/YYYY'"+")";
			  }
			  else
				  sql = sql + " and substr(v.datecreate, 0 , 10)  =  TO_DATE("+"'"+DateTo+"','MM/DD/YYYY'"+")";
		  }
		sql = sql +" order by v.datecreate desc";
		reportList =  hibernateTemplate.find(sql);
		
		return reportList;
	}

	@SuppressWarnings("unchecked")
	public List<Splitlots> getLotsDetails(String stakeholdercol, Long stakeholderid, String teastatuslist, String searchcol,
			String searchvalue, String Datefrom, String DateTo, String clmn, String Criteria, String strItem) {
		
		List<Splitlots> reportList = new ArrayList<Splitlots>();		
		String sql="";
		if(Criteria.equals("like") || Criteria=="like")
		  {
			if(!strItem.equals(""))
			  {
				strItem=strItem+"%";
			  }
		  }
		if(strItem.equals("") || strItem == "")
		  {
			sql="from Splitlots v where v."+stakeholdercol.toLowerCase() + "=" + stakeholderid +" and v."+searchcol.toLowerCase() + "='" + searchvalue +"' and v.teastatus IN ("+teastatuslist + ")";
		  }
		else
			sql="from Splitlots v where v."+stakeholdercol.toLowerCase() + "=" + stakeholderid +" and v."+searchcol.toLowerCase() + "='" + searchvalue +"' and v.teastatus IN ("+teastatuslist + ") and  v."+clmn + " " + Criteria + " '"+strItem+"'";
		if (Datefrom !="" && DateTo !="")
		  {
			  if(strItem.equals(""))
			  {
				  sql ="from Splitlots v where v."+stakeholdercol.toLowerCase() + "=" + stakeholderid +" and v."+searchcol.toLowerCase() + "='" + searchvalue +"' and v.teastatus IN ("+teastatuslist + ") and  v.datecreate between  TO_DATE("+"'"+Datefrom+"','MM/DD/YYYY'"+") and TO_DATE("+"'"+DateTo+"','MM/DD/YYYY'"+")";
			  }
			  else
				  sql += " and datecreate between  TO_DATE("+"'"+Datefrom+"','MM/DD/YYYY'"+") and TO_DATE("+"'"+DateTo+"','MM/DD/YYYY'"+")";
		  }
		  else if (Datefrom !="" && DateTo =="")
		  {
			  if(strItem.equals("") || strItem == "")
			  {
				  sql ="from Splitlots v where v."+stakeholdercol.toLowerCase() + "=" + stakeholderid +" and v."+searchcol.toLowerCase() + "='" + searchvalue +"' and v.teastatus IN ("+teastatuslist + ") and substr(v.datecreate, 0 , 10)  =  TO_DATE("+"'"+Datefrom+"','MM/DD/YYYY'"+")";
			  }
			  else
				  sql +=  " and substr(v.datecreate, 0 , 10)  =  TO_DATE("+"'"+Datefrom+"','MM/DD/YYYY'"+")";
		  }
		  else if (Datefrom =="" && DateTo !="")
		  {
			  if(strItem.equals(""))
			  {
				  sql ="from Splitlots v where v."+stakeholdercol.toLowerCase() + "=" + stakeholderid +" and v."+searchcol.toLowerCase() + "='" + searchvalue +"' and v.teastatus IN ("+teastatuslist + ") and  substr(v.datecreate, 0 , 10)  =  TO_DATE("+"'"+DateTo+"','MM/DD/YYYY'"+")";
			  }
			  else
				  sql = sql + " and substr(v.datecreate, 0 , 10)  =  TO_DATE("+"'"+DateTo+"','MM/DD/YYYY'"+")";
		  }
		sql = sql +" order by v.datecreate desc";
		reportList =  hibernateTemplate.find(sql);
		
		return reportList;
	}

	@SuppressWarnings("unchecked")
	public List<Splitlots> getDistinctGardens(String teastatuslist, String stakeholdercol, Long stakeholderid, String coltoget) {
		List<Splitlots> catalog = new ArrayList<Splitlots>();		
		List<String> records = getHibernateTemplate().find("select distinct sl." + coltoget.toLowerCase() + " from Splitlots sl where sl.teastatus IN ("+teastatuslist+") and sl." + stakeholdercol.toLowerCase() + "=? and sl." + coltoget.toLowerCase() + " is not null", stakeholderid);		
		if (records.isEmpty())
		{			
			return catalog;
		} else {
			for(String b:records)
			{			
				Object[] params = new Object[]{b, stakeholderid};
				List<Splitlots> cat = getHibernateTemplate().find("from Splitlots sl where sl.teastatus IN ("+teastatuslist+") and sl." + coltoget.toLowerCase() + "=? and sl." + stakeholdercol.toLowerCase() + "=?", params);				
				catalog.add(cat.get(0));
			}			
			return catalog;
		}
	}

	
	@SuppressWarnings("unchecked")
	public List<Splitlots> getLotsDetails(String stakeholdercol, Long stakeholderid, String teastatuslist, String searchstakeholdercol, 
			Long searchstakeholderid, String Datefrom, String DateTo, String clmn, String Criteria, String strItem) {
		
		List<Splitlots> reportList = new ArrayList<Splitlots>();		
		String sql="";
		if(Criteria.equals("like") || Criteria=="like")
		  {
			if(!strItem.equals(""))
			  {
				strItem=strItem+"%";
			  }
		  }
		if(strItem.equals("") || strItem == "")
		  {
			sql="from Splitlots v where v."+searchstakeholdercol.toLowerCase() + "=" + searchstakeholderid +" and v.teastatus IN ("+ teastatuslist +") and v."+stakeholdercol.toLowerCase() + "=" + stakeholderid;
		  }
		else
			sql="from Splitlots v where v."+searchstakeholdercol.toLowerCase() + "=" + searchstakeholderid +" and v.teastatus IN ("+ teastatuslist +") and v."+stakeholdercol.toLowerCase() + "=" + stakeholderid + " and  v."+clmn + " " + Criteria + " '"+strItem+"'";
		if (Datefrom !="" && DateTo !="")
		  {
			  if(strItem.equals(""))
			  {
				  sql ="from Splitlots v where v."+searchstakeholdercol.toLowerCase() + "=" + searchstakeholderid +" and v.teastatus IN ("+ teastatuslist +") and v."+stakeholdercol.toLowerCase() + "=" + stakeholderid + " and  v.datecreate between  TO_DATE("+"'"+Datefrom+"','MM/DD/YYYY'"+") and TO_DATE("+"'"+DateTo+"','MM/DD/YYYY'"+")";
			  }
			  else
				  sql += " and datecreate between  TO_DATE("+"'"+Datefrom+"','MM/DD/YYYY'"+") and TO_DATE("+"'"+DateTo+"','MM/DD/YYYY'"+")";
		  }
		  else if (Datefrom !="" && DateTo =="")
		  {
			  if(strItem.equals("") || strItem == "")
			  {
				  sql ="from Splitlots v where v."+searchstakeholdercol.toLowerCase() + "=" + searchstakeholderid +" and v.teastatus IN ("+ teastatuslist +") and v."+stakeholdercol.toLowerCase() + "=" + stakeholderid + " and substr(v.datecreate, 0 , 10)  =  TO_DATE("+"'"+Datefrom+"','MM/DD/YYYY'"+")";
			  }
			  else
				  sql +=  " and substr(v.datecreate, 0 , 10)  =  TO_DATE("+"'"+Datefrom+"','MM/DD/YYYY'"+")";
		  }
		  else if (Datefrom =="" && DateTo !="")
		  {
			  if(strItem.equals(""))
			  {
				  sql ="from Splitlots v where v."+searchstakeholdercol.toLowerCase() + "=" + searchstakeholderid +" and v.teastatus IN ("+ teastatuslist +") and v."+stakeholdercol.toLowerCase() + "=" + stakeholderid + " and  substr(v.datecreate, 0 , 10)  =  TO_DATE("+"'"+DateTo+"','MM/DD/YYYY'"+")";
			  }
			  else
				  sql = sql + " and substr(v.datecreate, 0 , 10)  =  TO_DATE("+"'"+DateTo+"','MM/DD/YYYY'"+")";
		  }
		sql = sql +" order by v.datecreate desc";
		reportList =  hibernateTemplate.find(sql);
		
		return reportList;
	}

	@SuppressWarnings("unchecked")
	public List<Object> getTeaDashBoard(String Datefrom, String DateTo, String clmn, String Criteria, String strItem) {
		List<Object> reportList = new ArrayList<Object>();		
		String sql="";
		if(Criteria.equals("like") || Criteria=="like")
		  {
			if(!strItem.equals(""))
			  {
				strItem=strItem+"%";
			  }
		  }
		if(strItem.equals("") || strItem == "")
		  {
			sql="from Eattateadashboard v";
		  }
		else
			sql="from Eattateadashboard v where  v."+clmn + " " + Criteria + " '"+strItem+"'";
		if (Datefrom !="" && DateTo !="")
		  {
			  if(strItem.equals("") || strItem == "")
			  {
				  sql ="from Eattateadashboard v where v.datecreate between  TO_DATE("+"'"+Datefrom+"','MM/DD/YYYY'"+") and TO_DATE("+"'"+DateTo+"','MM/DD/YYYY'"+")";
			  }
			  else
				  sql += " and datecreate between  TO_DATE("+"'"+Datefrom+"','MM/DD/YYYY'"+") and TO_DATE("+"'"+DateTo+"','MM/DD/YYYY'"+")";
		  }
		  else if (Datefrom !="" && DateTo =="")
		  {
			  if(strItem.equals("") || strItem == "")
			  {
				  sql ="from Eattateadashboard v where substr(v.datecreate, 0 , 10)  =  TO_DATE("+"'"+Datefrom+"','MM/DD/YYYY'"+")";
			  }
			  else
				  sql +=  " and substr(v.datecreate, 0 , 10)  =  TO_DATE("+"'"+Datefrom+"','MM/DD/YYYY'"+")";
		  }
		  else if (Datefrom =="" && DateTo !="")
		  {
			  if(strItem.equals("") || strItem == "")
			  {
				  sql ="from Eattateadashboard v where  substr(v.datecreate, 0 , 10)  =  TO_DATE("+"'"+DateTo+"','MM/DD/YYYY'"+")";
			  }
			  else
				  sql = sql + " and substr(v.datecreate, 0 , 10)  =  TO_DATE("+"'"+DateTo+"','MM/DD/YYYY'"+")";
		  }
		sql = sql +" order by v.datecreate desc";
		reportList =  hibernateTemplate.find(sql);
		
		return reportList;
	}

	@SuppressWarnings("unchecked")
	public List<Splitlots> getDistinctLots(String teastatuslist) {
		List<Splitlots> catalog = new ArrayList<Splitlots>();
		
		List<String> records = getHibernateTemplate().find("select distinct ls.lot from Splitlots ls");		
		if (records.isEmpty())
		{			
			return catalog;
		} else {
			for(String b:records)
			{							
				List<Splitlots> cat = getHibernateTemplate().find("from Splitlots ls where ls.lot=?", b);
				catalog.add(cat.get(0));
			}			
			return catalog;
		}
	}

	@SuppressWarnings("unchecked")
	public List<Paidlots> getPaidLots() {
		List<Paidlots> lots = new ArrayList<Paidlots>();
		lots = getHibernateTemplate().find("from Paidlots pl");
		return lots;
	}
	
	@SuppressWarnings("unchecked")
	public List<Object> getSalesBoardOtherBanks(String bank, String teastatuslist, String Datefrom, String DateTo, String clmn, String Criteria, String strItem) {
		List<Object> reportList = new ArrayList<Object>();		
		String sql="";
		if(Criteria.equals("like") || Criteria=="like")
		  {
			if(!strItem.equals(""))
			  {
				strItem=strItem+"%";
			  }
		  }
		if(strItem.equals("") || strItem == "")
		  {
			sql="from Eattateadashboard v where v.teastatusid IN ("+ teastatuslist +") and v.bank <> '"+bank +"'";
		  }
		else
			sql="from Eattateadashboard v where v.teastatusid IN ("+ teastatuslist +") and v.bank <> '"+bank +"' and v."+clmn + " " + Criteria + " '"+strItem+"'";
		if (Datefrom !="" && DateTo !="")
		  {
			  if(strItem.equals("") || strItem == "")
			  {
				  sql ="from Eattateadashboard v where v.teastatusid IN ("+ teastatuslist +") and v.bank <> '"+bank +"' and v.datecreate between  TO_DATE("+"'"+Datefrom+"','MM/DD/YYYY'"+") and TO_DATE("+"'"+DateTo+"','MM/DD/YYYY'"+")";
			  }
			  else
				  sql += " and datecreate between  TO_DATE("+"'"+Datefrom+"','MM/DD/YYYY'"+") and TO_DATE("+"'"+DateTo+"','MM/DD/YYYY'"+")";
		  }
		  else if (Datefrom !="" && DateTo =="")
		  {
			  if(strItem.equals("") || strItem == "")
			  {
				  sql ="from Eattateadashboard v where v.teastatusid IN ("+ teastatuslist +") and v.bank <> '"+bank +"' and substr(v.datecreate, 0 , 10)  =  TO_DATE("+"'"+Datefrom+"','MM/DD/YYYY'"+")";
			  }
			  else
				  sql +=  " and substr(v.datecreate, 0 , 10)  =  TO_DATE("+"'"+Datefrom+"','MM/DD/YYYY'"+")";
		  }
		  else if (Datefrom =="" && DateTo !="")
		  {
			  if(strItem.equals("") || strItem == "")
			  {
				  sql ="from Eattateadashboard v where v.teastatusid IN ("+ teastatuslist +") and v.bank <> '"+bank +"' and substr(v.datecreate, 0 , 10)  =  TO_DATE("+"'"+DateTo+"','MM/DD/YYYY'"+")";
			  }
			  else
				  sql = sql + " and substr(v.datecreate, 0 , 10)  =  TO_DATE("+"'"+DateTo+"','MM/DD/YYYY'"+")";
		  }
		sql = sql +" order by v.datecreate desc";
		reportList =  hibernateTemplate.find(sql);
		
		return reportList;
	}
		    
}
