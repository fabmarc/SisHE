package com.indra.sishe.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
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
import com.indra.sishe.dao.RegraDAO;
import com.indra.sishe.entity.Regra;
import com.indra.sishe.entity.Sindicato;

@Repository
public class RegraJdbcDaoImpl extends NamedParameterJdbcDaoSupport implements RegraDAO {

	@Autowired
	private DataSource dataSource;

	public RegraJdbcDaoImpl() {
	}

	private SimpleJdbcInsert insertRegra;

	@PostConstruct
	private void init() {
		setDataSource(dataSource);
		insertRegra = new SimpleJdbcInsert(getJdbcTemplate()).withTableName("regra")
				.usingGeneratedKeyColumns("id");
	}

	@Override
	public List<Regra> findByFilter(Regra regra) {

		StringBuilder sql = new StringBuilder();
		MapSqlParameterSource params = new MapSqlParameterSource();
		sql.append("SELECT regra.id AS id, regra.id_sindicato AS idSindicato, sindicato.descricao AS nomeSindicato, regra.descricao "
				+ "AS descricao, regra.data_inicio AS dataInicio, regra.data_fim AS dataFim, regra.porcentagem_feriado AS porcentagem ");
		sql.append("FROM regra INNER JOIN sindicato ON (regra.id_sindicato = sindicato.id) WHERE 1=1 ");

		if (regra != null && regra.getDescricao() != null && !"".equals(regra.getDescricao())) {
			sql.append("AND LOWER(regra.descricao) LIKE '%'|| :nomeRegra || '%' ");
			params.addValue("nomeRegra", regra.getDescricao().toLowerCase());
		}

		if (regra != null && regra.getSindicato() != null && regra.getSindicato().getId() != null) {
			sql.append("AND regra.id_sindicato = :idSidicato");
			params.addValue("idSidicato", regra.getSindicato().getId());
		}

		List<Regra> lista = getNamedParameterJdbcTemplate().query(sql.toString(), params, new RowMapper<Regra>() {
			@Override
			public Regra mapRow(ResultSet rs, int idx) throws SQLException {
				Sindicato sindicato = new Sindicato();
				Regra regra = new Regra();

				sindicato.setId(rs.getLong("idSindicato"));
				sindicato.setDescricao(rs.getString("nomeSindicato"));

				regra.setId(rs.getLong("id"));
				regra.setSindicato(sindicato);
				regra.setDescricao(rs.getString("descricao"));
				regra.setDataInicio(rs.getDate("dataInicio"));
				regra.setDataFim(rs.getDate("dataFim"));
				regra.setPorcentagem(rs.getInt("porcentagem"));

				return regra;
			}
		});
		return lista;
	}

	private boolean validarRegra(Regra regra, String modo) {
		StringBuilder sql = new StringBuilder();
		MapSqlParameterSource params = new MapSqlParameterSource();
		sql.append("SELECT id, id_sindicato, data_inicio, data_fim FROM regra ");
		sql.append("WHERE id_sindicato = :idSidicato AND (((:dataInicio BETWEEN data_inicio AND data_fim) OR (:dataFinal BETWEEN data_inicio AND data_fim)) ");
		sql.append("OR ((data_inicio BETWEEN :dataInicio AND :dataFinal) OR (data_fim BETWEEN :dataInicio AND :dataFinal))) ");
		params.addValue("idSidicato", regra.getSindicato().getId());
		params.addValue("dataInicio", regra.getDataInicio());
		params.addValue("dataFinal", regra.getDataFim());
		List<Regra> lista = getNamedParameterJdbcTemplate().query(sql.toString(), params, new RowMapper<Regra>() {
			@Override
			public Regra mapRow(ResultSet rs, int idx) throws SQLException {
				Sindicato sindicato = new Sindicato();
				Regra regra = new Regra();
				sindicato.setId(rs.getLong("id_sindicato"));
				regra.setId(rs.getLong("id"));
				regra.setSindicato(sindicato);
				regra.setDataInicio(rs.getDate("data_inicio"));
				regra.setDataFim(rs.getDate("data_fim"));
				return regra;
			}
		});
		if (modo.equalsIgnoreCase("cadastrar")) {
			if (lista.size() > 0) {
				return false;
			} else {
				return true;
			}
		} else {
			for (Regra r : lista) {
				if (r.getId() != regra.getId()) { return false; }
			}
			return true;
		}

	}

	@Override
	public Regra save(Regra regra) throws RegistroDuplicadoException {

		try {
			if (validarRegra(regra, "cadastrar")) {
				MapSqlParameterSource params = new MapSqlParameterSource();
				params.addValue("id_sindicato", regra.getSindicato().getId());
				params.addValue("descricao", regra.getDescricao());
				params.addValue("data_inicio", regra.getDataInicio());
				params.addValue("data_fim", regra.getDataFim());
				params.addValue("porcentagem_feriado", regra.getPorcentagem());
				Number key = insertRegra.executeAndReturnKey(params);
				regra.setId(key.longValue());
				return regra;
			} else {
				throw new RegistroDuplicadoException("Intervalo existente");
			}
		} catch (DuplicateKeyException e) {
			throw new RegistroDuplicadoException(e.toString());
		}
	}

	@Override
	public Regra update(Regra regra) throws RegistroInexistenteException, RegistroDuplicadoException {

		int rows = getJdbcTemplate().update(
				"UPDATE regra SET id_sindicato = ?, data_inicio = ?, data_fim = ?, "
						+ "descricao = ?, porcentagem_feriado = ? WHERE id = ?", regra.getSindicato().getId(),
				regra.getDataInicio(), regra.getDataFim(), regra.getDescricao(), regra.getPorcentagem(),
				regra.getId());
		if (rows == 0) throw new RegistroInexistenteException();
		return regra;
	}

	@Override
	public List<Regra> findAll() {

		StringBuilder sql = new StringBuilder();
		MapSqlParameterSource params = new MapSqlParameterSource();
		sql.append("SELECT regra.id AS id, regra.id_sindicato AS idSindicato, sindicato.descricao AS nomeSindicato, "
				+ "regra.descricao AS descricao, regra.data_inicio AS dataInicio, regra.data_fim AS dataFim, "
				+ "regra.porcentagem_feriado AS porcentagem ");
		sql.append("FROM regra INNER JOIN sindicato ON (regra.id_sindicato = sindicato.id) WHERE 1=1 ");

		List<Regra> lista = getNamedParameterJdbcTemplate().query(sql.toString(), params, new RowMapper<Regra>() {
			@Override
			public Regra mapRow(ResultSet rs, int idx) throws SQLException {
				Sindicato sindicato = new Sindicato();
				Regra regra = new Regra();

				sindicato.setId(rs.getLong("idSindicato"));
				sindicato.setDescricao(rs.getString("nomeSindicato"));

				regra.setId(rs.getLong("id"));
				regra.setSindicato(sindicato);
				regra.setDataInicio(rs.getDate("dataInicio"));
				regra.setDataFim(rs.getDate("dataFim"));
				regra.setPorcentagem(rs.getInt("porcentagem"));

				return regra;
			}
		});
		return lista;
	}

	@Override
	public Regra findById(Object id) throws RegistroInexistenteException {

		try {
			StringBuilder sql = new StringBuilder();
			MapSqlParameterSource params = new MapSqlParameterSource();
			sql.append("SELECT regra.id AS id, regra.id_sindicato AS idSindicato, sindicato.descricao AS nomeSindicato, "
					+ "regra.descricao AS descricao, regra.data_inicio AS dataInicio, regra.data_fim AS dataFim, "
					+ "regra.porcentagem_feriado AS porcentagem ");
			sql.append("FROM regra INNER JOIN sindicato ON (regra.id_sindicato = sindicato.id) "
					+ "WHERE regra.id = :id");
			params.addValue("id", id);

			return getNamedParameterJdbcTemplate().queryForObject(sql.toString(), params, new RowMapper<Regra>() {
				@Override
				public Regra mapRow(ResultSet rs, int idx) throws SQLException {
					Sindicato sindicato = new Sindicato();
					Regra regra = new Regra();

					sindicato.setId(rs.getLong("idSindicato"));
					sindicato.setDescricao(rs.getString("nomeSindicato"));

					regra.setId(rs.getLong("id"));
					regra.setSindicato(sindicato);
					regra.setDescricao(rs.getString("descricao"));
					regra.setDataInicio(rs.getDate("dataInicio"));
					regra.setDataFim(rs.getDate("dataFim"));
					regra.setPorcentagem(rs.getInt("porcentagem"));

					return regra;
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

		ArrayList<Object[]> params = new ArrayList<Object[]>(ids.size());
		for (Object id : ids) {
			Object[] param = new Object[] { id };
			params.add(param);
		}
		int[] affectedRows = getJdbcTemplate().batchUpdate("DELETE FROM regra WHERE id = ?", params);
		for (int rows : affectedRows)
			if (rows == 0) throw new RegistroInexistenteException();
	}

	@Override
	public EntityManager getEntityManager() {
		return null;
	}

	public SimpleJdbcInsert getInsertRegra() {
		return insertRegra;
	}

	public void setInsertRegra(SimpleJdbcInsert insertRegra) {
		this.insertRegra = insertRegra;
	}

}
