/******************************************************************************
 * NuvemWare                                                                  *
 * Copyright 2010, Nuvemware , LTDA, and individual                           *
 * contributors as indicated by the @authors tag. See the                     *
 * copyright.txt in the distribution for a full listing of                    *
 * individual contributors.                                                   *
 *                                                                            *
 * This is free software; you can redistribute it and/or modify it            *
 * under the terms of the GNU Lesser General Public License as                *
 * published by the Free Software Foundation; either version 2.1 of           *
 * the License, or (at your option) any later version.                        *
 *                                                                            *
 * This software is distributed in the hope that it will be useful,           *
 * but WITHOUT ANY WARRANTY; without even the implied warranty of             *
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU           *
 * Lesser General Public License for more details.                            *
 *                                                                            *
 * You should have received a copy of the GNU Lesser General Public           *
 * License along with this software; if not, write to the Free                *
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA         *
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.                   *
 ******************************************************************************/
package com.nuvemware.corerest.framework;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import com.google.appengine.api.datastore.EntityNotFoundException;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.helper.DAOBase;

/**
 * This is the base class for all BackingBeans for JSF that needs to persist data
 * @author Jefferson Ankiewsky 
 * */

public abstract class AbstractPersistentObject<T> extends DAOBase



{
	private Class<T> clazz;

	/**
	 * We've got to get the associated domain class somehow
	 *
	 * @param clazz
	 */
	protected AbstractPersistentObject(Class<T> clazz)
	{
		this.clazz = clazz;
	}


	public Key<T> add(T entity)

	{
		Key<T> key = ofy().put(entity);
		return key;
	}
	
	public List<T> findAll(Integer maxResults)

	{
		
		return ofy().query(clazz).limit(maxResults).list();
	}


	public void delete(T entity)
	{
		ofy().delete(entity);
	}

	public void delete(Key<T> entityKey)
	{
		ofy().delete(entityKey);
	}

	public T get(Long id) throws EntityNotFoundException
	{
		T obj = ofy().get(this.clazz, id);
		return obj;
	}

	public T get(String id) throws EntityNotFoundException
	{
		T obj = ofy().get(this.clazz, id);
		return obj;
	}
	
	public Object get(String col,String value) throws EntityNotFoundException
	{
		Object obj = ofy().query(this.clazz).filter(col, value).get();
		
		return obj;
	}
	


	public Object getPropertyValue(Object obj, String propertyName)
	{
		BeanInfo beanInfo;
		try
		{
			beanInfo = Introspector.getBeanInfo(obj.getClass());
		}
		catch (IntrospectionException e)
		{
			throw new RuntimeException(e);
		}
		PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
		for (PropertyDescriptor propertyDescriptor : propertyDescriptors)
		{
			String propName = propertyDescriptor.getName();
			if (propName.equals(propertyName))
			{
				Method readMethod = propertyDescriptor.getReadMethod();
				try
				{
					Object value = readMethod.invoke(obj, new Object[] {});
					return value;
				}
				catch (IllegalArgumentException e)
				{
					throw new RuntimeException(e);
				}
				catch (IllegalAccessException e)
				{
					throw new RuntimeException(e);
				}
				catch (InvocationTargetException e)
				{
					throw new RuntimeException(e);
				}
			}
		}
		return null;
	}

}
