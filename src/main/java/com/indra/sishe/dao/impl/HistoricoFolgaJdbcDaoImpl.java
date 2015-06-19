package com.indra.sishe.dao.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcDaoSupport;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import com.indra.infra.dao.exception.DeletarRegistroViolacaoFK;
import com.indra.infra.dao.exception.RegistroDuplicadoException;
import com.indra.infra.dao.exception.RegistroInexistenteException;
import com.indra.sishe.controller.usuario.UsuarioLogado;
import com.indra.sishe.dao.HistoricoFolgaDAO;
import com.indra.sishe.entity.HistoricoFolga;

@Repository
public class HistoricoFolgaJdbcDaoImpl extends NamedParameterJdbcDaoSupport implements HistoricoFolgaDAO {

	@Autowired
	private DataSource dataSource;

	private SimpleJdbcInsert insertFolgaDebitar;
	
	@Override
	public HistoricoFolga save(HistoricoFolga entity) throws RegistroDuplicadoException {
		try {
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue("id_folga", entity.getFolga().getId());
			params.addValue("id_gerente", entity.getGerente().getId());
			params.addValue("data", entity.getData());
			params.addValue("observacao", entity.getObservacao());

			Number key = insertFolgaDebitar.executeAndReturnKey(params);
			entity.setId(key.longValue());
		} catch (DuplicateKeyException e) {
			throw new RegistroDuplicadoException(e.toString());
		}

		return entity;
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
