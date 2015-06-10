package com.indra.sishe.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcDaoSupport;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import com.indra.infra.dao.exception.DeletarRegistroViolacaoFK;
import com.indra.infra.dao.exception.RegistroDuplicadoException;
import com.indra.infra.dao.exception.RegistroInexistenteException;
import com.indra.sishe.controller.usuario.UsuarioLogado;
import com.indra.sishe.dao.FolgaDAO;
import com.indra.sishe.entity.Cargo;
import com.indra.sishe.entity.Cidade;
import com.indra.sishe.entity.Feriado;
import com.indra.sishe.entity.Folga;
import com.indra.sishe.entity.Usuario;
import com.indra.sishe.enums.EstadoEnum;

@Repository
public class FolgaJdbcDaoImpl extends NamedParameterJdbcDaoSupport implements FolgaDAO {

	@Autowired
	private DataSource dataSource;

	private SimpleJdbcInsert insertFolga;

	@PostConstruct
	private void init() {
		setDataSource(dataSource);
		insertFolga = new SimpleJdbcInsert(getJdbcTemplate()).withTableName("folga").usingGeneratedKeyColumns("id");
	}

	@Override
	public Folga save(Folga entity) throws RegistroDuplicadoException {

		try {
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue("id_solicitante", UsuarioLogado.getId());
			params.addValue("id_aprovador", entity.getAprovador().getId());
			params.addValue("data_solicitacao", entity.getDataSolicitacao());
			params.addValue("data_aprovacao", entity.getDataAprovacao());
			params.addValue("data_folga", entity.getDataFolga());
			params.addValue("observacao", entity.getObservacao());

			Number key = insertFolga.executeAndReturnKey(params);
			entity.setId(key.longValue());
		} catch (DuplicateKeyException e) {
			throw new RegistroDuplicadoException(e.toString());
		}

		return entity;
	}

	@Override
	public Folga update(Folga entity) throws RegistroInexistenteException, RegistroDuplicadoException {
		int rows = 0;
		try {
			rows = getJdbcTemplate().update("UPDATE folga SET data_folga = ?, observacao = ? WHERE id = ?",
					entity.getDataFolga(), entity.getObservacao());
			if (rows == 0) throw new RegistroInexistenteException();
		} catch (DuplicateKeyException e) {
			throw new RegistroDuplicadoException(e.toString());
		}
		return entity;
	}

	@Override
	public List<Folga> findAll() {
		return null;
	}

	@Override
	public Folga findById(Object id) throws RegistroInexistenteException {
		try {
			StringBuilder sql = new StringBuilder();
			MapSqlParameterSource params = new MapSqlParameterSource();
			sql.append("SELECT f.id as idFolga, f.id_solicitante as idSolicitante, solic.nome as nomeSolicitante, cs.nome as cargoSolicitante, " +
					"f.id_aprovador as idAprovador, aprov.nome as nomeAprovador, ca.nome as cargoAprovador, f.data_solicitacao as dataSolicitacao, " +
					"f.data_aprovacao as dataAprovacao, f.data_folga as dataFolga, f.observacao as observacao, cs.id as idCargoSolicitante, ca.id as idCargoAprovador,  " +
					"FROM folga f INNER JOIN usuario solic ON (f.id_solicitante = solic.id) " +
					"			  INNER JOIN cargo cs ON (solic.id_cargo = cs.id)" +
					"			  LEFT JOIN usuario aprov ON (f.id_aprovador = aprov.id)" +
					"			  LEFT JOIN cargo ca ON (aprov.id_cargo = ca.id) " +
					"WHERE f.id = :idFolga ");
			params.addValue("idFolga", id);

			return getNamedParameterJdbcTemplate().queryForObject(sql.toString(), params,
					new RowMapper<Folga>() {
						@Override
						public Folga mapRow(ResultSet rs, int idx) throws SQLException {
							Cargo cargoSolicitante = new Cargo();
							Cargo cargoAprovador = new Cargo();
							Usuario solicitante = new Usuario();
							Usuario aprovador = new Usuario();
							Folga folga = new Folga();

							cargoSolicitante.setId(rs.getLong("idCargoSolicitante"));
							cargoSolicitante.setNome(rs.getString("cargoSolicitante"));
							cargoAprovador.setId(rs.getLong("idCargoAprovador"));
							cargoAprovador.setNome(rs.getString("cargoSolicitante"));
							
							solicitante.setId(rs.getLong("idSolicitante"));
							solicitante.setNome(rs.getString("nomeSolicitante"));
							aprovador.setId(rs.getLong("idAprovador"));
							aprovador.setNome(rs.getString("nomeAprovador"));
							
							folga.setId(rs.getLong("idFolga"));
							folga.setDataAprovacao(rs.getDate("dataAprovacao"));
							folga.setDataFolga(rs.getDate("dataFolga"));
							folga.setDataSolicitacao(rs.getDate("dataSolicitacao"));
							folga.setAprovador(aprovador);
							folga.setSolicitante(solicitante);
							folga.setObservacao(rs.getString("observacao"));
							return folga;
						}
					});
		} catch (EmptyResultDataAccessException e) {
			throw new RegistroInexistenteException();
		}
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
