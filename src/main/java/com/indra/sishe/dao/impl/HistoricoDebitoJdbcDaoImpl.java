package com.indra.sishe.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcDaoSupport;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import com.indra.infra.dao.exception.DeletarRegistroViolacaoFK;
import com.indra.infra.dao.exception.RegistroDuplicadoException;
import com.indra.infra.dao.exception.RegistroInexistenteException;
import com.indra.sishe.dao.HistoricoDebitoDao;
import com.indra.sishe.entity.HistoricoDebito;
import com.indra.sishe.entity.Usuario;

@Repository
public class HistoricoDebitoJdbcDaoImpl  extends NamedParameterJdbcDaoSupport implements HistoricoDebitoDao{

	@Autowired
	private DataSource dataSource;

	private SimpleJdbcInsert insertHistoricoDebito;

	@PostConstruct
	private void init() {
		setDataSource(dataSource);
		insertHistoricoDebito = new SimpleJdbcInsert(getJdbcTemplate()).withTableName("historico_debito")
				.usingGeneratedKeyColumns("id");
	}
	
	@Override
	public HistoricoDebito save(HistoricoDebito entity) throws RegistroDuplicadoException {
		try {
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue("data", entity.getData());
			params.addValue("minutos", entity.getMinutos());
			params.addValue("id_gerente", entity.getGerente().getId());
			params.addValue("id_banco", entity.getBanco().getId());
			Number key = insertHistoricoDebito.executeAndReturnKey(params);
			entity.setId(key.longValue());
		} catch (DuplicateKeyException e) {
			throw new RegistroDuplicadoException(e.toString());
		}
		return entity;
	}

	@Override
	public HistoricoDebito update(HistoricoDebito entity) throws RegistroInexistenteException,
			RegistroDuplicadoException {
		return null;
	}

	@Override
	public List<HistoricoDebito> findAll() {
		return null;
	}

	@Override
	public HistoricoDebito findById(Object id) throws RegistroInexistenteException {
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

	@Override
	public List<HistoricoDebito> findByUsuarioEMes(Usuario user, String mes, String ano) {
		StringBuilder sql = new StringBuilder();
		MapSqlParameterSource params = new MapSqlParameterSource();
		sql.append("SELECT historico_debito.id 	,historico_debito.data 	,historico_debito.minutos 	,historico_debito.id_gerente 	,historico_debito.id_banco 	,banco_horas.id_usuario ");
		sql.append("FROM historico_debito LEFT join banco_horas ON (historico_debito.id_banco = banco_horas.id) ");
		sql.append(" WHERE (TO_CHAR(historico_debito.data, 'MM') = :mes AND TO_CHAR(historico_debito.data, 'YYYY') = :ano) AND banco_horas.id_usuario = :idUsuario ");
		params.addValue("mes", mes);
		params.addValue("idUsuario", user.getId());
		params.addValue("ano", ano);
		
		return getNamedParameterJdbcTemplate().query(sql.toString(), params,
				new RowMapper<HistoricoDebito>() {
			@Override
			public HistoricoDebito mapRow(ResultSet rs, int idx) throws SQLException {

				HistoricoDebito historicoDebito = new HistoricoDebito();
				
				Usuario gerente = new Usuario();
				gerente.setId(rs.getLong("id_gerente"));
				
				historicoDebito.setGerente(gerente);

				historicoDebito.setData(rs.getDate("data"));
				historicoDebito.setMinutos(rs.getInt("minutos"));
				historicoDebito.setId(rs.getLong("id"));
				

				return historicoDebito;
			}
		});
	}
	
}
