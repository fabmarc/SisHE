package com.indra.infra.dao;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.List;

import javax.persistence.Id;

import com.indra.infra.dao.exception.RegistroInexistenteException;

public abstract class BaseDAOImpl<T> extends AbstractBaseDAOImpl implements BaseDAO<T> {

	private Class<T> persistentClass;

	@SuppressWarnings("unchecked")
	public BaseDAOImpl() {
		this.persistentClass = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
	}

	public T save(T entity) {
		getEntityManager().persist(entity);
		return entity;
	}

	public T update(T entity) throws RegistroInexistenteException {
		findById(getIdField(entity));
		return getEntityManager().merge(entity);
	}

	private Object getIdField(T entity) {
		Field[] declaredFields = entity.getClass().getDeclaredFields();
		for (Field field : declaredFields) {
			Id annotationId = field.getAnnotation(Id.class);
			if (annotationId != null) {
				try {
					String methodName = field.getName().substring(0, 1).toUpperCase();
					methodName = "get" + methodName + field.getName().substring(1);
					Method method = entity.getClass().getMethod(methodName, (Class[]) null);
					return method.invoke(entity, (Object[]) null);
				} catch (RuntimeException e) {
					throw e;
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			}
		}
		throw new RuntimeException("Id field not found.");
	}

	public void remove(Object id) throws RegistroInexistenteException {
		T entity = findById(id);
		getEntityManager().remove(entity);
	}

	@Override
	public void remove(List<Object> ids) throws RegistroInexistenteException {
		for (Object id : ids) remove(id);
	}

	@SuppressWarnings("unchecked")
	public List<T> findAll() {
		return getEntityManager().createQuery(
				"select a from " + persistentClass.getName() + " a").getResultList();
	}

	public T findById(Object id) throws RegistroInexistenteException {
		T entity = (T) getEntityManager().find(persistentClass, id);
		if (entity == null) throw new RegistroInexistenteException();
		return (T) getEntityManager().find(persistentClass, id);
	}
}
