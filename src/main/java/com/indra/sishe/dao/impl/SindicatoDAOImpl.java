package com.indra.sishe.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcDaoSupport;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import com.indra.infra.dao.exception.RegistroInexistenteException;
import com.indra.sishe.dao.SindicatoDAO;
import com.indra.sishe.entity.Estado;
import com.indra.sishe.entity.Sindicato;

@Repository
public class SindicatoDAOImpl extends NamedParameterJdbcDaoSupport implements
		SindicatoDAO {

	@Autowired
	private DataSource dataSource;

	private SimpleJdbcInsert insertSindicato;

	@PostConstruct
	private void init() {
		setDataSource(dataSource);
		insertSindicato = new SimpleJdbcInsert(getJdbcTemplate())
				.withTableName("sindicato").usingGeneratedKeyColumns(
						"id");
		
		}

	StringBuilder sql = new StringBuilder();
	
	@Override
	public List<Sindicato> pesquisarPorEstado(Sindicato sindicato) {

		//StringBuilder sql = new StringBuilder();/
		MapSqlParameterSource params = new MapSqlParameterSource();

		sql.append("SELECT id AS idSindicato , descricao AS nomeSindicato ");
		sql.append("FROM from sindicato where id_estado in (select id from estado where nome like '%"
				+ sindicato.getDescricao() + "%')");

		if (sindicato != null && sindicato.getDescricao() != null
				&& !sindicato.getDescricao().isEmpty()) {
			sql.append("AND LOWER(descricao) LIKE '%' || :descricao || '%'");
			params.addValue("descricao", sindicato.getDescricao().toLowerCase());
		}

		return getNamedParameterJdbcTemplate().query(sql.toString(), params,
				new BeanPropertyRowMapper<Sindicato>(Sindicato.class));

	}

	
	@Override
	public List<Sindicato> findByFilter(Sindicato sindicato) {

		StringBuilder sql = new StringBuilder();
		MapSqlParameterSource params = new MapSqlParameterSource();


		sql.append("SELECT s.id AS idSindicato , s.descricao, e.sigla, e.id AS idEstado, e.nome as nome ");
		sql.append("FROM estado e INNER JOIN sindicato s ON e.id = s.id_estado "); 
		

		if (sindicato != null && sindicato.getDescricao() != null
				&& !sindicato.getDescricao().isEmpty()) {
			sql.append("AND UPPER(s.descricao) LIKE '%' || :descricao || '%'");
			params.addValue("descricao", sindicato.getDescricao().toUpperCase());
		}
		
		List<Sindicato> lista = getNamedParameterJdbcTemplate().query(sql.toString(), params, 
				
				new RowMapper<Sindicato>() {
					@Override
					public Sindicato mapRow(ResultSet rs, int idx) throws SQLException {
						
						Sindicato sind = new Sindicato();
						Estado estado = new Estado();
						estado.setSigla(rs.getString("sigla"));
						estado.setNome(rs.getString("nome"));
						estado.setId(rs.getLong("idEstado"));
						sind.setEstado(estado);
						
						
						sind.setId(rs.getLong("idSindicato"));
						sind.setDescricao(rs.getString("descricao"));
						
						return sind;
					}
				});
		
		return lista;
	}

	


	@Override
	public Sindicato save(Sindicato entity) {
		
		
		MapSqlParameterSource parms = new MapSqlParameterSource();
		parms.addValue("id_estado", entity.getEstado().getId());
		parms.addValue("descricao", entity.getDescricao());
		
		Number key = insertSindicato
				.executeAndReturnKey(parms);
		entity.setId(key.longValue()); 
		
		return entity;
	}

	@Override
	public Sindicato update(Sindicato entity) throws RegistroInexistenteException {
		int rows = getJdbcTemplate().update("UPDATE sindicato SET descricao = ?, id_estado = ? "
				+ "WHERE id = ?", entity.getDescricao(),entity.getEstado().getId(), entity.getId());
		if (rows == 0) throw new RegistroInexistenteException();
		return entity;
	}

	@Override
	public List<Sindicato> findAll() {
		return getJdbcTemplate().query("SELECT id, id_estado, descricao "
				+ "FROM sindicato", new BeanPropertyRowMapper<Sindicato>(Sindicato.class));
	}

	@Override
	public Sindicato findById(Object id) throws RegistroInexistenteException {
		try {
			return getJdbcTemplate().queryForObject("SELECT s.id AS idSindicato , s.descricao as descricao," 
					+ "e.id AS idEstado, e.nome as nome FROM estado e INNER JOIN sindicato s ON e.id = s.id_estado "
					+ "WHERE s.id = ?  ", new Object[] { id }, new RowMapper<Sindicato>(){

						@Override
						public Sindicato mapRow(ResultSet rs, int idx)
								throws SQLException {
							// TODO Auto-generated method stub
							Sindicato sind = new Sindicato();
							Estado estado = new Estado();
							estado.setId(rs.getLong("idEstado"));
							estado.setNome(rs.getString("nome"));
							sind.setId(rs.getLong("idSindicato"));
							sind.setDescricao(rs.getString("descricao"));							
							sind.setEstado(estado);
							
							return sind;
						}
						
					});
		
		} catch (EmptyResultDataAccessException e) {
			throw new RegistroInexistenteException();
		}
	}

	@Override
	public void remove(Object id) throws RegistroInexistenteException {
		// TODO Auto-generated method stub
		int rows = getJdbcTemplate().update("DELETE FROM sindicato WHERE id = ?", id);
		if (rows == 0) throw new RegistroInexistenteException();
	}

	@Override
	public EntityManager getEntityManager() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public void remove(List<Object> ids) throws RegistroInexistenteException {
		// TODO Auto-generated method stub
		ArrayList<Object[]> params = new ArrayList<Object[]>(ids.size());
		for (Object id : ids) {
			Object[] param = new Object[] { id };
			params.add(param);
		}
		int[] affectedRows = getJdbcTemplate().batchUpdate("DELETE FROM sindicato WHERE id = ?", params);
		for (int rows : affectedRows) if (rows == 0) throw new RegistroInexistenteException();

		
	}

	
}
