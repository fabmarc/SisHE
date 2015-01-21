package com.indra.sishe.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
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

	private SimpleJdbcInsert insertRegra;

	@PostConstruct
	private void init() {
		setDataSource(dataSource);
		insertRegra = new SimpleJdbcInsert(getJdbcTemplate()).withTableName("regra").usingGeneratedKeyColumns("id");
	}

	@Override
	public List<Regra> findByFilter(Regra regra) {
		StringBuilder sql = new StringBuilder();
		MapSqlParameterSource params = new MapSqlParameterSource();
		sql.append("SELECT regra.id AS id, regra.id_sindicato AS idSindicato, sindicato.descricao AS nomeSindicato, regra.descricao AS descricao, "
				+ "regra.data_inicio AS dataInicio, regra.data_fim AS dataFim, regra.porcentagem_feriado AS porcentagem ");
		sql.append("FROM regra INNER JOIN sindicato ON (regra.id_sindicato = sindicato.id) WHERE 1=1 ");

		if (regra != null && regra.getDescricao() != null) {
			sql.append("AND LOWER(regra.descricao) LIKE '%'|| :nomeRegra || '%' ");
			params.addValue("nomeRegra", regra.getDescricao().toLowerCase());
		}
		
		if (regra != null && regra.getSindicato() != null && regra.getId() != null) {
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
				regra.setDataInicio(rs.getDate("dataInicio"));
				regra.setDataFim(rs.getDate("dataFim"));
				regra.setPorcentagem(rs.getDouble("porcentagem"));

				return regra;
			}
		});
		return lista;
	}

	@Override
	public Regra save(Regra regra) throws RegistroDuplicadoException {
		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("id_sindicato", regra.getSindicato().getId());
		params.addValue("descricao", regra.getDescricao());
		params.addValue("data_inicio", regra.getDataInicio());
		params.addValue("data_fim", regra.getDataFim());
		params.addValue("porcentagem", regra.getPorcentagem());
		Number key = insertRegra.executeAndReturnKey(params);
		regra.setId(key.longValue());
		return regra;
	}

	@Override
	public Regra update(Regra regra) throws RegistroInexistenteException, RegistroDuplicadoException {
		
		int rows = getJdbcTemplate().update("UPDATE regra SET id_sindicato = ?, data_inicio = ?, data_fim = ?, descricao = ?, porcentagem_feriado = ?",
			regra.getSindicato().getId(), regra.getDataInicio(), regra.getDataFim(), regra.getDescricao(), regra.getPorcentagem());
		if (rows == 0) throw new RegistroInexistenteException();
		return regra;
	}

	@Override
	public List<Regra> findAll() {
		StringBuilder sql = new StringBuilder();
		MapSqlParameterSource params = new MapSqlParameterSource();
		sql.append("SELECT regra.id AS id, regra.id_sindicato AS idSindicato, sindicato.descricao AS nomeSindicato, regra.descricao AS descricao, "
				+ "regra.data_inicio AS dataInicio, regra.data_fim AS dataFim, regra.porcentagem_feriado AS porcentagem ");
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
				regra.setPorcentagem(rs.getDouble("porcentagem"));

				return regra;
			}
		});
		return lista;
	}

	@Override
	public Regra findById(Object id) throws RegistroInexistenteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void remove(Object id) throws RegistroInexistenteException, DeletarRegistroViolacaoFK {
		// TODO Auto-generated method stub

	}

	@Override
	public void remove(List<Object> ids) throws RegistroInexistenteException, DeletarRegistroViolacaoFK {
		// TODO Auto-generated method stub

	}

	@Override
	public EntityManager getEntityManager() {
		// TODO Auto-generated method stub
		return null;
	}

}
