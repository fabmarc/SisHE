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
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcDaoSupport;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import com.indra.infra.dao.exception.DeletarRegistroViolacaoFK;
import com.indra.infra.dao.exception.RegistroDuplicadoException;
import com.indra.infra.dao.exception.RegistroInexistenteException;
import com.indra.sishe.dao.DatasFolgaDAO;
import com.indra.sishe.entity.DatasFolga;
import com.indra.sishe.entity.Folga;

@Repository
public class DatasFolgaDaoImpl extends NamedParameterJdbcDaoSupport implements DatasFolgaDAO {

	@Autowired
	private DataSource dataSource;

	private SimpleJdbcInsert insertDatasFolga;

	@PostConstruct
	private void init() {
		setDataSource(dataSource);
		insertDatasFolga = new SimpleJdbcInsert(getJdbcTemplate()).withTableName("datas_folga").usingGeneratedKeyColumns("id");
	}
	
	@Override
	public DatasFolga save(DatasFolga entity) throws RegistroDuplicadoException {
		
		try {
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue("id_folga", entity.getFolga().getId());
			params.addValue("data", entity.getData());

			Number key = insertDatasFolga.executeAndReturnKey(params);
			entity.setId(key.longValue());
		} catch (DuplicateKeyException e) {
			throw new RegistroDuplicadoException(e.toString());
		}

		return entity;
	}

	@Override
	public DatasFolga update(DatasFolga entity) throws RegistroInexistenteException, RegistroDuplicadoException {
		return null;
	}

	@Override
	public List<DatasFolga> findAll() {
		return null;
	}

	@Override
	public DatasFolga findById(Object id) throws RegistroInexistenteException {
		return null;
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
		try {
			int[] affectedRows = getJdbcTemplate().batchUpdate("DELETE FROM datas_folga WHERE id_folga = ?", params);
			for (int rows : affectedRows)
				if (rows == 0) throw new RegistroInexistenteException();
		} catch (DataIntegrityViolationException d) {
			throw new DeletarRegistroViolacaoFK();
		}
	}

	@Override
	public EntityManager getEntityManager() {
		return null;
	}

	@Override
	public List<DatasFolga> findDatasBySolicitacaoFolga(Long idFolga) {
		StringBuilder sql = new StringBuilder();
		MapSqlParameterSource params = new MapSqlParameterSource();
		sql.append("SELECT id, id_folga, data FROM datas_folga WHERE id_folga = :idFolga ORDER BY data ASC");
		params.addValue("idFolga", idFolga);
	
		List<DatasFolga> lista = getNamedParameterJdbcTemplate().query(sql.toString(), params,

		new RowMapper<DatasFolga>() {
			@Override
			public DatasFolga mapRow(ResultSet rs, int idx) throws SQLException {
				
				DatasFolga datasFolga = new DatasFolga();
				Folga folga = new Folga();
				
				folga.setId(rs.getLong("id_folga"));
				
				datasFolga.setId(rs.getLong("id"));
				datasFolga.setFolga(folga);
				datasFolga.setData(rs.getDate("data"));
				return datasFolga;
			}
		});
		return lista;
	}

	@Override
	public void removeTodasDatasPorFolga(Long idFolga) throws DeletarRegistroViolacaoFK  {
		
		try {
			getJdbcTemplate().update("DELETE FROM datas_folga WHERE id_folga = ?", idFolga);
		} catch (DataIntegrityViolationException d) {
			throw new DeletarRegistroViolacaoFK();
		}
	}

	@Override
	public void insereTodasDatasPorFolga(List<DatasFolga> listaDatas) {
		for (DatasFolga data : listaDatas) {
				MapSqlParameterSource params = new MapSqlParameterSource();
				params.addValue("id_folga", data.getFolga().getId());
				params.addValue("data", data.getData());
				insertDatasFolga.execute(params);
		}
	}

}
