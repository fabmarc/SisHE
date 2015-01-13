package com.indra.sishe.dao.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcDaoSupport;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import com.indra.infra.dao.exception.RegistroInexistenteException;
import com.indra.sishe.dao.ClienteDAO;
import com.indra.sishe.entity.Cliente;

@Repository
public class ClienteJdbcDaoImp extends NamedParameterJdbcDaoSupport implements ClienteDAO {
	
	@Autowired
	@Resource(mappedName="java:jboss/datasources/SislamDS") 
	private DataSource dataSource;
	
	private SimpleJdbcInsert insertCliente;

	@PostConstruct
	private void init() {
		setDataSource(dataSource);
		insertCliente = new SimpleJdbcInsert(getJdbcTemplate()).withTableName("cliente").usingGeneratedKeyColumns("id_cliente");
	}
	
	public ClienteJdbcDaoImp(){
		System.out.println("Criou ClienteDaoImpl");
	}
	
	@Override
	public List<Cliente> findByFilter(Cliente cliente) {
		
		StringBuilder sql = new StringBuilder();
		MapSqlParameterSource params = new MapSqlParameterSource();
		
		sql.append("SELECT id_cliente AS idCliente, nome_cliente AS nomeCliente ");
		sql.append("FROM cliente WHERE 1 = 1");
		
		if (cliente != null && cliente.getIdCliente() != null) {
			sql.append("AND id_cliente = :idCliente");
			params.addValue("idCliente", cliente.getIdCliente());
		}
		if (cliente != null && cliente.getNomeCliente() != null && !cliente.getNomeCliente().isEmpty()) {
			sql.append("AND LOWER(nome_cliente) LIKE '%' || :nomeCliente || '%'");
			params.addValue("nomeCliente", cliente.getNomeCliente().toLowerCase());
		}
		return getNamedParameterJdbcTemplate().query(sql.toString(), params, new BeanPropertyRowMapper<Cliente>(Cliente.class));
	}

	@Override
	public Cliente save(Cliente entity) {
		Number key = insertCliente.executeAndReturnKey(new BeanPropertySqlParameterSource(entity));
		entity.setIdCliente(key.longValue());
		return entity;
	}

	@Override
	public Cliente update(Cliente entity) throws RegistroInexistenteException {
		int rows = getJdbcTemplate().update("UPDATE cliente SET nome_cliente = ? "
				+ "WHERE id_cliente = ?", entity.getNomeCliente(), entity.getIdCliente());
		if (rows == 0) throw new RegistroInexistenteException();
		return entity;
	}

	@Override
	public List<Cliente> findAll() {
		return getJdbcTemplate().query("SELECT id_cliente AS idCliente, nome_cliente AS nomeCliente "
				+ "FROM cliente", new BeanPropertyRowMapper<Cliente>(Cliente.class));
	}

	@Override
	public Cliente findById(Object id) throws RegistroInexistenteException {
		try {
			return getJdbcTemplate().queryForObject("SELECT id_cliente AS idCliente, nome_cliente AS nomeCliente "
					+ "FROM cliente WHERE id_cliente = ?", new Object[] { id }, new BeanPropertyRowMapper<Cliente>(Cliente.class));
		} catch (EmptyResultDataAccessException e) {
			throw new RegistroInexistenteException();
		}
	}

	@Override
	public void remove(Object id) throws RegistroInexistenteException {
		int rows = getJdbcTemplate().update("DELETE FROM cliente WHERE id_cliente = ?", id);
		if (rows == 0) throw new RegistroInexistenteException();
	}
	
	@Override
	public void remove(List<Object> ids) throws RegistroInexistenteException {
		
		ArrayList<Object[]> params = new ArrayList<Object[]>(ids.size());
		for (Object id : ids) {
			Object[] param = new Object[] { id };
			params.add(param);
		}
		int[] affectedRows = getJdbcTemplate().batchUpdate("DELETE FROM cliente WHERE id_cliente = ?", params);
		for (int rows : affectedRows) if (rows == 0) throw new RegistroInexistenteException();
	}

	@Override
	public EntityManager getEntityManager() {
		return null;
	}

}
