package com.indra.sishe.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcDaoSupport;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import com.indra.infra.dao.exception.DeletarRegistroViolacaoFK;
import com.indra.infra.dao.exception.RegistroDuplicadoException;
import com.indra.infra.dao.exception.RegistroInexistenteException;
import com.indra.sishe.dao.UsuarioDAO;
import com.indra.sishe.entity.Cargo;
import com.indra.sishe.entity.Usuario;

@Repository
public class UsuarioJdbcDAOImp extends NamedParameterJdbcDaoSupport implements UsuarioDAO {

	@Autowired
	@Resource(mappedName="java:jboss/datasources/SisHE")
	private DataSource dataSource;
	
	private SimpleJdbcInsert insertUsuario;
	
	public UsuarioJdbcDAOImp(){
	}
	
	@PostConstruct
	private void init() {
		setDataSource(dataSource);
		insertUsuario = new SimpleJdbcInsert(getJdbcTemplate()).withTableName("usuario").usingGeneratedKeyColumns("id");
	}
	
	@Override
	public Usuario save(Usuario entity) throws RegistroDuplicadoException {
		try{
			Number key = insertUsuario.executeAndReturnKey(new BeanPropertySqlParameterSource(entity));
			entity.setId(key.longValue());
		}catch(DuplicateKeyException e){
			throw new RegistroDuplicadoException();
		}
		return entity;
	}

	@Override
	public Usuario update(Usuario entity) throws RegistroInexistenteException {
		int rows = getJdbcTemplate().update("UPDATE usuario SET id_cargo = ?, id_sindicato = ?, id_cidade = ?, nome = ?, login = ?, senha = ?, matricula = ?, email = ? "
				+ "WHERE id = ?", entity.getCargo().getId(), entity.getSindicato().getId(), entity.getCidade().getId(), entity.getNome(), entity.getLogin(), entity.getSenha(), entity.getMatricula(), entity.getEmail(), entity.getId());
		if (rows == 0) throw new RegistroInexistenteException();
		return entity;
	}

	@Override
	public List<Usuario> findAll() {
		return getJdbcTemplate().query("SELECT id, id_cargo as cargo, id_sindicato as sindicato, id_cidade as cidade, nome, login, senha, matricula, email "
				+ "FROM usuario", new BeanPropertyRowMapper<Usuario>(Usuario.class));
	}

	@Override
	public Usuario findById(Object id) throws RegistroInexistenteException {
		try {
			return getJdbcTemplate().queryForObject("SELECT id, id_cargo as cargo, id_sindicato as sindicato, id_cidade as cidade, nome, login, senha, matricula, email "
					+ "FROM usuario WHERE id = ?", new Object[] { id }, new BeanPropertyRowMapper<Usuario>(Usuario.class));
		} catch (EmptyResultDataAccessException e) {
			throw new RegistroInexistenteException();
		}
	}

	@Override
	public void remove(Object id) throws RegistroInexistenteException, DeletarRegistroViolacaoFK {
		try{
			int rows = getJdbcTemplate().update("DELETE FROM usuario WHERE id = ?", id);
			if (rows == 0) throw new RegistroInexistenteException();
		}catch(DataIntegrityViolationException d){
			throw new DeletarRegistroViolacaoFK();
		}	
		
	}

	@Override
	public EntityManager getEntityManager() {
		return null;
	}

	@Override
	public List<Usuario> findByFilter(Usuario usuarioFiltro) {
		StringBuilder sql = new StringBuilder();
		MapSqlParameterSource params = new MapSqlParameterSource();
		
		sql.append("SELECT usuario.id, usuario.id_cargo as id_cargo, cargo.nome as nome_cargo, usuario.nome as nome, usuario.matricula ");
		sql.append("FROM usuario LEFT JOIN CARGO ON (CARGO.ID = USUARIO.ID_CARGO) WHERE 1 = 1");
		
		if (usuarioFiltro != null && usuarioFiltro.getId() != null) {
			sql.append("AND id = :idUsuario");
			params.addValue("idUsuario", usuarioFiltro.getId());
		}
		if (usuarioFiltro != null && usuarioFiltro.getNome() != null && !usuarioFiltro.getNome().isEmpty()) {
			sql.append("AND LOWER(nome) LIKE '%' || :nomeUsuario || '%'");
			params.addValue("nomeUsuario", usuarioFiltro.getNome().toLowerCase());
		}
		if (usuarioFiltro != null && usuarioFiltro.getLogin() != null && !usuarioFiltro.getLogin().isEmpty()) {
			sql.append("AND LOWER(login) LIKE '%' || :loginUsuario || '%'");
			params.addValue("loginUsuario", usuarioFiltro.getLogin().toLowerCase());
		}
		if (usuarioFiltro != null && usuarioFiltro.getMatricula() != null && !usuarioFiltro.getMatricula().toString().isEmpty()) {
			sql.append("AND matricula = :matriculaUsuario");
			params.addValue("matriculaUsuario", usuarioFiltro.getMatricula());
		}
		
List<Usuario> lista = getNamedParameterJdbcTemplate().query(sql.toString(), params, 
				
				new RowMapper<Usuario>() {
					@Override
					public Usuario mapRow(ResultSet rs, int idx) throws SQLException {
						
						Usuario usuario = new Usuario();
						Cargo cargo= new Cargo();
						
						cargo.setId(rs.getLong("id_cargo"));
						cargo.setNome(rs.getString("nome_cargo"));
						
						usuario.setCargo(cargo);
						
						usuario.setId(rs.getLong("id"));
						usuario.setNome(rs.getString("nome"));
						usuario.setMatricula(rs.getInt("matricula"));
						
						
						return usuario;
					}
				});
		return lista;//getNamedParameterJdbcTemplate().query(sql.toString(), params, new BeanPropertyRowMapper<Usuario>(Usuario.class));
	
	}

}
