package com.indra.sishe.dao.impl;

import java.util.List;

import javax.persistence.EntityManager;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcDaoSupport;

import com.indra.infra.dao.exception.DeletarRegistroViolacaoFK;
import com.indra.infra.dao.exception.RegistroDuplicadoException;
import com.indra.infra.dao.exception.RegistroInexistenteException;
import com.indra.sishe.dao.HistoricoFolgaDAO;
import com.indra.sishe.entity.HistoricoFolga;

public class HistoricoFolgaJdbcDaoImpl extends NamedParameterJdbcDaoSupport implements HistoricoFolgaDAO {

	@Override
	public HistoricoFolga save(HistoricoFolga entity) throws RegistroDuplicadoException {
		return null;
	}

	@Override
	public HistoricoFolga update(HistoricoFolga entity) throws RegistroInexistenteException, RegistroDuplicadoException {
		return null;
	}

	@Override
	public List<HistoricoFolga> findAll() {
		return null;
	}

	@Override
	public HistoricoFolga findById(Object id) throws RegistroInexistenteException {
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
