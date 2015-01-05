package com.indra.infra.dao;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import org.hibernate.Session;

public abstract class AbstractBaseDAOImpl {

	@Inject
	private EntityManager em;

	protected Session getSession() {
		return em.unwrap(Session.class);
	}

	public void setEntityManager(EntityManager em) {
		this.em = em;
	}

	/**
	 * Método responsável por retornar uma instância do entity manager associado ao banco de dados.
	 * 
	 * @return
	 */
	public EntityManager getEntityManager() {
		return this.em;
	}
}