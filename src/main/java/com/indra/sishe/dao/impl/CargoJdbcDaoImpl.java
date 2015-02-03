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
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcDaoSupport;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import com.indra.infra.dao.exception.DeletarRegistroViolacaoFK;
import com.indra.infra.dao.exception.RegistroDuplicadoException;
import com.indra.infra.dao.exception.RegistroInexistenteException;
import com.indra.sishe.dao.CargoDAO;
import com.indra.sishe.entity.Cargo;
import com.indra.sishe.enums.PermissaoEnum;

@Repository
public class CargoJdbcDaoImpl extends NamedParameterJdbcDaoSupport implements CargoDAO {

	@Autowired
	private DataSource dataSource;

	private SimpleJdbcInsert insertCargo;

	public CargoJdbcDaoImpl() {
		System.out.println("Criou CargoDaoImpl");
	}

	@PostConstruct
	private void init() {
		setDataSource(dataSource);
		insertCargo = new SimpleJdbcInsert(getJdbcTemplate()).withTableName("cargo")
				.usingGeneratedKeyColumns("id");
	}

	@Override
	public Cargo save(Cargo entity) throws RegistroDuplicadoException {

		try {
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue("nome", entity.getNome());
			params.addValue("role", entity.getRole().getPermissao());
			Number key = insertCargo.executeAndReturnKey(params);
			entity.setId(key.longValue());
		} catch (DuplicateKeyException e) {
			throw new RegistroDuplicadoException(e.toString());
		}

		try {
			Number key = insertCargo.executeAndReturnKey(new BeanPropertySqlParameterSource(entity));
			entity.setId(key.longValue());
		} catch (DuplicateKeyException e) {
			throw new RegistroDuplicadoException(e.toString());
		}
		return entity;
	}

	@Override
	public Cargo update(Cargo entity) throws RegistroInexistenteException, RegistroDuplicadoException {

		try {
			int rows = getJdbcTemplate().update("UPDATE cargo SET nome = ?, role = ? " + "WHERE id = ?",
					entity.getNome(), entity.getRole().getRole(), entity.getId());
			if (rows == 0) throw new RegistroInexistenteException();
		} catch (DuplicateKeyException e) {
			throw new RegistroDuplicadoException(e.toString());
		}
		return entity;
	}

	@Override
	public List<Cargo> findAll() {
		StringBuilder sql = new StringBuilder();
		MapSqlParameterSource params = new MapSqlParameterSource();
		sql.append("SELECT id, nome, role " + "FROM cargo WHERE 1=1 ");
		
		return consultar(sql, params);
	}

	@Override
	public Cargo findById(Object id) throws RegistroInexistenteException {
		try {
			StringBuilder sql = new StringBuilder();
			MapSqlParameterSource params = new MapSqlParameterSource();
			sql.append("SELECT id, nome, role " + "FROM cargo WHERE 1=1 ");
			sql.append("AND id = :id");
			params.addValue("id", id);
			return consultarUmPeriodo(sql, params);
		} catch (EmptyResultDataAccessException e) {
			throw new RegistroInexistenteException();
		}
	}

	@Override
	public EntityManager getEntityManager() {
		return null;
	}

	@Override
	public List<Cargo> findByFilter(Cargo cargoFiltro) {

		StringBuilder sql = new StringBuilder();
		MapSqlParameterSource params = new MapSqlParameterSource();

		sql.append("SELECT id, nome, role ");
		sql.append("FROM cargo WHERE 1 = 1");

		if (cargoFiltro != null && cargoFiltro.getId() != null) {
			sql.append("AND id = :idCargo ");
			params.addValue("idCargo", cargoFiltro.getId());
		}
		if (cargoFiltro != null && cargoFiltro.getNome() != null && !cargoFiltro.getNome().isEmpty()) {
			sql.append("AND LOWER(nome) LIKE '%' || :nomeCargo || '%' ");
			params.addValue("nomeCargo", cargoFiltro.getNome().toLowerCase());
		}
		if (cargoFiltro != null && cargoFiltro.getRole() != null) {
			sql.append("AND LOWER(nome) LIKE :roleCargo ");
			params.addValue("roleCargo", cargoFiltro.getRole().getRole().toLowerCase());
		}
		return consultar(sql, params);
	}

	@Override
	public void remove(Object id) throws RegistroInexistenteException, DeletarRegistroViolacaoFK {
		try {
			int rows = getJdbcTemplate().update("DELETE FROM cargo WHERE id = ?", id);
			if (rows == 0) throw new RegistroInexistenteException();
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
			int[] affectedRows = getJdbcTemplate().batchUpdate("DELETE FROM cargo WHERE id = ?", params);
			for (int rows : affectedRows)
				if (rows == 0) throw new RegistroInexistenteException();
		} catch (DataIntegrityViolationException d) {
			throw new DeletarRegistroViolacaoFK();
		}
	}

	private List<Cargo> consultar(StringBuilder sql, MapSqlParameterSource params) {
		List<Cargo> lista = getNamedParameterJdbcTemplate().query(sql.toString(), params, new RowMapper<Cargo>() {
			@Override
			public Cargo mapRow(ResultSet rs, int idx) throws SQLException {

				Cargo cargo = new Cargo();
				cargo.setId(rs.getLong("id"));
				cargo.setNome(rs.getString("nome"));
				cargo.setRole(PermissaoEnum.obterPermissao(rs.getString("role")));

				return cargo;
			}
		});
		return lista;
	}

	private Cargo consultarUmPeriodo(StringBuilder sql, MapSqlParameterSource params) {

		Cargo cargo = getNamedParameterJdbcTemplate().queryForObject(sql.toString(), params,
				new RowMapper<Cargo>() {
					@Override
					public Cargo mapRow(ResultSet rs, int idx) throws SQLException {

						Cargo cargo = new Cargo();
						cargo.setId(rs.getLong("id"));
						cargo.setNome(rs.getString("nome"));
						cargo.setRole(PermissaoEnum.obterPermissao(rs.getString("role")));

						return cargo;
					}
				});
		return cargo;
	}

}
