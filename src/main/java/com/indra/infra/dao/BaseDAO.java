package com.indra.infra.dao;

import java.util.List;

import javax.persistence.EntityManager;

import com.indra.infra.dao.exception.DeletarRegistroViolacaoFK;
import com.indra.infra.dao.exception.RegistroDuplicadoException;
import com.indra.infra.dao.exception.RegistroInexistenteException;

public interface BaseDAO<T> {

	public T save(T entity) throws RegistroDuplicadoException;

	public T update(T entity) throws RegistroInexistenteException;
	
	public List<T> findAll();

	public T findById(Object id) throws RegistroInexistenteException;

	public void remove(Object id) throws RegistroInexistenteException, DeletarRegistroViolacaoFK;

	public void remove(List<Object> ids) throws RegistroInexistenteException, DeletarRegistroViolacaoFK;

	EntityManager getEntityManager();
}