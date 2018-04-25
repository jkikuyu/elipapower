package com.ipayafrica.elipapower.repository.hibernate;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import com.ipayafrica.elipapower.repository.IGenericDao;

http://www.baeldung.com/simplifying-the-data-access-layer-with-spring-and-java-generics


/**
 * This class serves as the Base class for all other DAOs - namely to hold
 * common CRUD methods that they might all use. You should only need to extend
 * this class when your require custom CRUD logic.
 * <p/>
 * <p>To register this class in your Spring context file, use the following XML.
 * <pre>
 *      &lt;bean id="fooDao" class="com.collections.citi.ebills.dao.hibernate.GenericDaoHibernate"&gt;
 *          &lt;constructor-arg value="com.ipayafrica.elipapowser.model.Foo"/&gt;
 *      &lt;/bean&gt;
 * </pre>
 *
 */
@Repository
@Scope( BeanDefinition.SCOPE_PROTOTYPE )
public class GenericDaoHibernate< T extends Serializable>
  extends AbstractHibernateDao<T > implements IGenericDao<T>{

	@Override
	public T findOne(long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<T> findAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void create(T entity) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void delete(T entity) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteById(long entityId) {
		// TODO Auto-generated method stub
		
	}

}
