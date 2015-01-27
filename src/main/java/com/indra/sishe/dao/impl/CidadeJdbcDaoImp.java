package com.indra.sishe.dao.impl;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcDaoSupport;
import org.springframework.stereotype.Repository;

import com.indra.infra.dao.exception.DeletarRegistroViolacaoFK;
import com.indra.infra.dao.exception.RegistroInexistenteException;
import com.indra.sishe.dao.CidadeDAO;
import com.indra.sishe.entity.Cidade;
import com.indra.sishe.enums.EstadoEnum;

@Repository
public class CidadeJdbcDaoImp extends NamedParameterJdbcDaoSupport implements CidadeDAO{

	@Autowired
	private DataSource dataSource;
	
	@PostConstruct
	private void init(){
		setDataSource(dataSource);
	}

	@Override
	public Cidade save(Cidade entity) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Cidade update(Cidade entity) throws RegistroInexistenteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Cidade> findAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Cidade findById(Object id) throws RegistroInexistenteException {
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
	public List<Cidade> findByEstado(EstadoEnum estado) {
		StringBuilder sql = new StringBuilder();
		MapSqlParameterSource params = new MapSqlParameterSource();
		sql.append("SELECT id AS id, nome AS nome ");
		sql.append("FROM cidade WHERE id_estado = :idEstado");
		params.addValue("idEstado", estado.getId());
		
		return getNamedParameterJdbcTemplate().query(sql.toString(), params, new BeanPropertyRowMapper<Cidade>(Cidade.class));
	}

	@Override
	public void remove(List<Object> ids) throws RegistroInexistenteException, DeletarRegistroViolacaoFK {
		
	}
	
}
