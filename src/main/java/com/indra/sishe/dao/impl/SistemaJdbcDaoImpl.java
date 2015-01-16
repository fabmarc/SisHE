package com.indra.sishe.dao.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcDaoSupport;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import com.indra.infra.dao.exception.DeletarRegistroViolacaoFK;
import com.indra.infra.dao.exception.RegistroDuplicadoException;
import com.indra.infra.dao.exception.RegistroInexistenteException;
import com.indra.sishe.dao.SistemaDAO;
import com.indra.sishe.entity.Sistema;

@Repository
public class SistemaJdbcDaoImpl extends NamedParameterJdbcDaoSupport implements
		SistemaDAO {

	@Autowired
	private DataSource dataSource;

	private SimpleJdbcInsert insertSistema;

	@PostConstruct
	private void init() {
		setDataSource(dataSource);
		insertSistema = new SimpleJdbcInsert(getJdbcTemplate()).withTableName(
				"sistema").usingGeneratedKeyColumns("id");

	}

	public SistemaJdbcDaoImpl() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public Sistema save(Sistema entity) throws RegistroDuplicadoException {
		try {
			MapSqlParameterSource parms = new MapSqlParameterSource();

			parms.addValue("id_lider", entity.getUsuario());
			// parms.addValue("id_projeto", entity.getIdProjeto()));
			parms.addValue("nome", entity.getNome());
			parms.addValue("descricao", entity.getDescricao());

			Number key = insertSistema.executeAndReturnKey(parms);
			entity.setId(key.longValue());
						
		} catch (DuplicateKeyException e) {
			throw new RegistroDuplicadoException();
		}

		return entity;
	}

	@Override
	// ADICIONAR O ATRIBUTO ID_PROJETO AO UPDATE E USAR O GET COMO PARAMETRO
	public Sistema update(Sistema entity) throws RegistroInexistenteException {
		int rows = getJdbcTemplate().update(
				"UPDATE sistema SET id_lider=?, " + " descricao=?, nome=?"
						+ "WHERE id = ?", entity.getUsuario(),
				entity.getDescricao(), entity.getNome());

		if (rows == 0)
			throw new RegistroInexistenteException();
		return entity;
	}

	// NÃO IMPLEMENTADO
	@Override
	public List<Sistema> findAll() {
		return null;
	}

	// NÃO IMPLEMENTADO
	@Override
	public Sistema findById(Object id) throws RegistroInexistenteException {
		// TODO Auto-generated method stub
		return null;
	}

	// NÃO IMPLEMENTADO
	@Override
	public void remove(Object id) throws RegistroInexistenteException,
			DeletarRegistroViolacaoFK {
		// TODO Auto-generated method stub

	}

	@Override
	public void remove(List<Object> ids) throws RegistroInexistenteException,
			DeletarRegistroViolacaoFK {
		// TODO Auto-generated method stub
		ArrayList<Object[]> params = new ArrayList<Object[]>(ids.size());
		for (Object id : ids) {
			Object[] param = new Object[] { id };
			params.add(param);
		}
		try {
			int[] affectedRows = getJdbcTemplate().batchUpdate(
					"DELETE FROM sistema WHERE id = ?", params);
			for (int rows : affectedRows)
				if (rows == 0)
					throw new RegistroInexistenteException();

		} catch (DataIntegrityViolationException d) {

			throw new DeletarRegistroViolacaoFK();

		}
	}

	@Override
	public EntityManager getEntityManager() {
		// TODO Auto-generated method stub
		return null;
	}

}
