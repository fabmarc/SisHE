package com.indra.sishe.dao.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcDaoSupport;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import com.indra.infra.dao.exception.DeletarRegistroViolacaoFK;
import com.indra.infra.dao.exception.RegistroDuplicadoException;
import com.indra.infra.dao.exception.RegistroInexistenteException;
import com.indra.sishe.dao.FolgaDebitarDAO;
import com.indra.sishe.entity.FolgaDebitar;

@Repository
public class FolgaDebitarJdbcDaoImpl extends NamedParameterJdbcDaoSupport implements FolgaDebitarDAO {
	
	@Autowired
	private DataSource dataSource;

	private SimpleJdbcInsert insertFolgaDebitar;

	@Override
	public FolgaDebitar save(FolgaDebitar entity) throws RegistroDuplicadoException {
		return null;
	}

	@Override
	public FolgaDebitar update(FolgaDebitar entity) throws RegistroInexistenteException, RegistroDuplicadoException {
		return null;
	}

	@Override
	public List<FolgaDebitar> findAll() {
		return null;
	}

	@Override
	public FolgaDebitar findById(Object id) throws RegistroInexistenteException {
		return null;
	}

	@Override
	public void remove(Object id) throws RegistroInexistenteException, DeletarRegistroViolacaoFK {
	}

	@Override
	public void remove(List<Object> ids) throws RegistroInexistenteException, DeletarRegistroViolacaoFK {
	}

	@Override
	public EntityManager getEntityManager() {
		return null;
	}

}
