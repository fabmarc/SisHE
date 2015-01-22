package com.indra.sishe.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
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
import com.indra.sishe.dao.PeriodoDAO;
import com.indra.sishe.entity.Periodo;
import com.indra.sishe.entity.Regra;
import com.indra.sishe.entity.Sindicato;

@Repository
public class PeriodoJdbcDaoImpl extends NamedParameterJdbcDaoSupport implements PeriodoDAO {

	@Autowired
	private DataSource dataSource;

	private SimpleJdbcInsert insertPeriodo;

	@PostConstruct
	private void init() {
		setDataSource(dataSource);
		insertPeriodo = new SimpleJdbcInsert(getJdbcTemplate()).withTableName("periodo").usingGeneratedKeyColumns("id");
	}

	@Override
	public Periodo save(Periodo entity) throws RegistroDuplicadoException {
		try {
			MapSqlParameterSource params = new MapSqlParameterSource();

			params.addValue("dia_semana", entity.getDiaSemana());
			params.addValue("hora_inicio", entity.getHoraInicio());
			params.addValue("hora_fim", entity.getHoraFim());
			params.addValue("porcentagem", entity.getPorcentagem());
			if (entity.getRegra() != null) {
				params.addValue("id_regra", entity.getRegra().getId());
			}

			Number key = insertPeriodo.executeAndReturnKey(params);
			entity.setId(key.longValue());
		} catch (DuplicateKeyException e) {
			throw new RegistroDuplicadoException(e.toString());
		}
		return entity;
	}

	@Override
	public Periodo update(Periodo entity) throws RegistroInexistenteException, RegistroDuplicadoException {
		try {
			int rows = getJdbcTemplate()
					.update("UPDATE periodo SET id_regra = ?, dia_semana = ?, hora_inicio = ?, hora_fim = ?, porcentagem = ? " + "WHERE id = ?", entity.getRegra().getId(), entity.getDiaSemana(), entity.getHoraInicio(), entity.getHoraFim(), entity.getPorcentagem(), entity.getId());
			if (rows == 0)
				throw new RegistroInexistenteException();
		} catch (DuplicateKeyException e) {
			throw new RegistroDuplicadoException(e.toString());
		}
		return entity;
	}

	@Override
	public List<Periodo> findAll() {
		StringBuilder sql = new StringBuilder();
		MapSqlParameterSource params = new MapSqlParameterSource();
		sql.append("Select sindicato.id as id_sindicato, sindicato.descricao as descricao_sindicato, periodo.id, periodo.id_regra as id_regra, regra.descricao as descricao_regra, periodo.dia_semana, periodo.hora_inicio, periodo.hora_fim, periodo.porcentagem ");
		sql.append("from periodo left join regra on (periodo.id_regra = regra.id) left join sindicato on (regra.id_sindicato = sindicato.id) WHERE 1=1 ");
		return consultar(sql, params);
	}

	@Override
	public Periodo findById(Object id) throws RegistroInexistenteException {
		try {
			StringBuilder sql = new StringBuilder();
			MapSqlParameterSource params = new MapSqlParameterSource();
			sql.append("Select sindicato.id as id_sindicato, sindicato.descricao as descricao_sindicato, periodo.id, periodo.id_regra as id_regra, regra.descricao as descricao_regra, periodo.dia_semana, periodo.hora_inicio, periodo.hora_fim, periodo.porcentagem ");
			sql.append("from periodo left join regra on (periodo.id_regra = regra.id) left join sindicato on (regra.id_sindicato = sindicato.id) WHERE 1=1 ");
			sql.append("AND periodo.id = :idPeriodo");
			params.addValue("idPeriodo", id);
			List<Periodo> lista = consultar(sql, params);

			if (!lista.isEmpty()) {
				return lista.get(0);
			} else {
				return null;
			}
		} catch (EmptyResultDataAccessException e) {
			throw new RegistroInexistenteException();
		}
	}

	@Override
	public void remove(Object id) throws RegistroInexistenteException, DeletarRegistroViolacaoFK {
		try {
			int rows = getJdbcTemplate().update("DELETE FROM periodo WHERE id = ?", id);
			if (rows == 0)
				throw new RegistroInexistenteException();
		} catch (DataIntegrityViolationException d) {
			throw new DeletarRegistroViolacaoFK();
		}
	}

	@Override
	public void remove(List<Object> ids) throws RegistroInexistenteException, DeletarRegistroViolacaoFK {
		ArrayList<Object[]> params = new ArrayList<Object[]>(ids.size());
		for (Object id : ids) {
			Object[] param = new Object[] { id };
			params.add(param);
		}
		try {
			int[] affectedRows = getJdbcTemplate().batchUpdate("DELETE FROM periodo WHERE id = ?", params);
			for (int rows : affectedRows)
				if (rows == 0)
					throw new RegistroInexistenteException();
		} catch (DataIntegrityViolationException d) {
			throw new DeletarRegistroViolacaoFK();
		}

	}

	@Override
	public EntityManager getEntityManager() {
		return null;
	}

	@Override
	public List<Periodo> findByFilter(Periodo entity) {
		StringBuilder sql = new StringBuilder();
		MapSqlParameterSource params = new MapSqlParameterSource();

		sql.append("Select sindicato.id as id_sindicato, sindicato.descricao as descricao_sindicato, periodo.id, periodo.id_regra as id_regra, regra.descricao as descricao_regra, periodo.dia_semana, periodo.hora_inicio, periodo.hora_fim, periodo.porcentagem ");
		sql.append("from periodo left join regra on (periodo.id_regra = regra.id) left join sindicato on (regra.id_sindicato = sindicato.id) WHERE 1=1 ");

		if (entity != null && entity.getId() != null) {
			sql.append("AND periodo.id = :idPeriodo");
			params.addValue("idPeriodo", entity.getId());
		}
		if (entity != null && entity.getDiaSemana() != null) {
			sql.append("AND periodo.dia_semana = :diaSemana");
			params.addValue("diaSemana", entity.getDiaSemana());
		}
		if (entity != null && entity.getPorcentagem() != null) {
			sql.append("AND periodo.porcentagem = :porcentagem");
			params.addValue("porcentagem", entity.getPorcentagem());
		}
		return consultar(sql, params);
	}

	private List<Periodo> consultar(StringBuilder sql, MapSqlParameterSource params) {
		List<Periodo> lista = getNamedParameterJdbcTemplate().query(sql.toString(), params, new RowMapper<Periodo>() {
			@Override
			public Periodo mapRow(ResultSet rs, int idx) throws SQLException {

				Periodo periodo = new Periodo();

				Sindicato sindicato = new Sindicato();
				sindicato.setId(rs.getLong("id_sindicato"));
				sindicato.setDescricao(rs.getString("descricao_sindicato"));

				Regra regra = new Regra();
				regra.setId(rs.getLong("id_regra"));
				regra.setDescricao(rs.getString("descricao_regra"));
				regra.setSindicato(sindicato);

				periodo.setRegra(regra);
				periodo.setId(rs.getLong("id"));
				periodo.setDiaSemana(rs.getLong("dia_semana"));
				periodo.setHoraInicio(rs.getTime("hora_inicio"));
				periodo.setHoraFim(rs.getTime("hora_fim"));
				periodo.setPorcentagem(rs.getDouble("porcentagem"));

				return periodo;
			}
		});
		return lista;
	}

	@Override
	public List<Periodo> findByRegra(Regra entity) {
		StringBuilder sql = new StringBuilder();
		MapSqlParameterSource params = new MapSqlParameterSource();

		sql.append("Select sindicato.id as id_sindicato, sindicato.descricao as descricao_sindicato, periodo.id, periodo.id_regra as id_regra, regra.descricao as descricao_regra, periodo.dia_semana, periodo.hora_inicio, periodo.hora_fim, periodo.porcentagem ");
		sql.append("from periodo left join regra on (periodo.id_regra = regra.id) left join sindicato on (regra.id_sindicato = sindicato.id) WHERE 1=1 ");

		if (entity != null && entity.getId() != null) {
			sql.append("AND periodo.id_regra= :regra");
			params.addValue("regra", entity.getId());
		}

		return consultar(sql, params);
	}

}
