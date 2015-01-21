package com.indra.sishe.dao.impl;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcDaoSupport;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import com.indra.infra.dao.exception.DeletarRegistroViolacaoFK;
import com.indra.infra.dao.exception.RegistroInexistenteException;
import com.indra.sishe.dao.EstadoDAO;
import com.indra.sishe.entity.Cidade;
import com.indra.sishe.entity.Estado;

@Repository
public class EstadoJdbcDaoImpl extends NamedParameterJdbcDaoSupport implements
		EstadoDAO {

	@Autowired
	private DataSource dataSource;

	private SimpleJdbcInsert insertEstado;

	@PostConstruct
	private void init() {
		setDataSource(dataSource);
		insertEstado = new SimpleJdbcInsert(getJdbcTemplate()).withTableName(
				"estado").usingGeneratedKeyColumns("id");
	}

	@Override
	public List<Estado> pesquisarPorSigla(String sigla) {

		return null;
	}

	@Override
	public List<Estado> findAll() {
		return getJdbcTemplate().query(
				"SELECT id, nome , sigla " + "FROM estado",
				new BeanPropertyRowMapper<Estado>(Estado.class));
	}

	/**
	 * @return the insertEstado
	 */
	public SimpleJdbcInsert getInsertEstado() {
		return insertEstado;
	}

	/**
	 * @param insertEstado
	 *            the insertEstado to set
	 */
	public void setInsertEstado(SimpleJdbcInsert insertEstado) {
		this.insertEstado = insertEstado;
	}

	@Override
	public Estado save(Estado entity) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Estado update(Estado entity) throws RegistroInexistenteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Estado findById(Object id) throws RegistroInexistenteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void remove(Object id) throws RegistroInexistenteException {
		// TODO Auto-generated method stub

	}

	@Override
	public EntityManager getEntityManager() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void remove(List<Object> ids) throws RegistroInexistenteException,
			DeletarRegistroViolacaoFK {
		// TODO Auto-generated method stub

	}

	@Override
	public Estado findByCidade(Cidade cidade) {
		StringBuilder sql = new StringBuilder();
		MapSqlParameterSource params = new MapSqlParameterSource();
		sql.append("SELECT estado.id as id, estado.nome as nome, estado.sigla as sigla ");
		sql.append("FROM ESTADO INNER JOIN CIDADE ON (cidade.id_estado = estado.id) WHERE 1=1 ");
		sql.append("AND cidade.id  = :idCidade");
		params.addValue("idCidade", cidade.getId());
		List<Estado> estado = getNamedParameterJdbcTemplate().query(sql.toString(), params, new BeanPropertyRowMapper<Estado>(Estado.class));
		if(!estado.isEmpty()){
			return estado.get(0);
		}else{
			return null;
		}
	}
}
