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
import com.indra.sishe.dao.UsuarioDAO;
import com.indra.sishe.entity.Cargo;
import com.indra.sishe.entity.Cidade;
import com.indra.sishe.entity.Estado;
import com.indra.sishe.entity.Sindicato;
import com.indra.sishe.entity.Usuario;

@Repository
public class UsuarioJdbcDaoImpl extends NamedParameterJdbcDaoSupport implements UsuarioDAO {

	@Autowired
	private DataSource dataSource;

	private SimpleJdbcInsert insertUsuario;

	public UsuarioJdbcDaoImpl() {
	}

	@PostConstruct
	private void init() {
		setDataSource(dataSource);
		insertUsuario = new SimpleJdbcInsert(getJdbcTemplate()).withTableName("usuario").usingGeneratedKeyColumns(
				"id");
	}

	@Override
	public Usuario save(Usuario entity) throws RegistroDuplicadoException {

		try {
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue("nome", entity.getNome());
			params.addValue("login", entity.getLogin());
			params.addValue("senha", entity.getSenha());
			params.addValue("email", entity.getEmail());
			params.addValue("matricula", entity.getMatricula());
			if (entity.getCargo() != null) {
				params.addValue("id_cargo", entity.getCargo().getId());
			}
			if (entity.getCidade() != null) {
				params.addValue("id_cidade", entity.getCidade().getId());
			}
			if (entity.getSindicato() != null) {
				params.addValue("id_sindicato", entity.getSindicato().getId());
			}
			Number key = insertUsuario.executeAndReturnKey(params);
			entity.setId(key.longValue());
		} catch (DuplicateKeyException e) {
			throw new RegistroDuplicadoException(e.toString());
		}
		return entity;
	}

	@Override
	public Usuario update(Usuario entity) throws RegistroInexistenteException, RegistroDuplicadoException {

		int rows = 0;
		try {
			rows = getJdbcTemplate().update(
					"UPDATE usuario SET id_cargo = ?, id_sindicato = ?, id_cidade = ?, nome = ?, "
							+ "login = ?, senha = ?, matricula = ?, email = ? " + "WHERE id = ?",
					entity.getCargo().getId(), entity.getSindicato().getId(), entity.getCidade().getId(),
					entity.getNome(), entity.getLogin(), entity.getSenha(), entity.getMatricula(),
					entity.getEmail(), entity.getId());
			if (rows == 0) throw new RegistroInexistenteException();
		} catch (DuplicateKeyException e) {
			throw new RegistroDuplicadoException(e.toString());
		}
		return entity;
	}

	@Override
	public void remove(Object id) throws RegistroInexistenteException, DeletarRegistroViolacaoFK {
		try {
			int rows = getJdbcTemplate().update("DELETE FROM usuario WHERE id = ?", id);
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
	public List<Usuario> findAll() {
		StringBuilder sql = new StringBuilder();
		MapSqlParameterSource params = new MapSqlParameterSource();
		sql.append("SELECT usuario.id, usuario.id_cargo as id_cargo, cargo.nome as nome_cargo, usuario.nome as nome, usuario.matricula, usuario.email as email, usuario.login as login, usuario.senha as senha, usuario.id_sindicato as id_sindicato, sindicato.descricao as sindicato_descricao, cidade.id as id_cidade, cidade.id_estado as id_cidade_estado, cidade.nome as cidade_nome ");
		sql.append("FROM usuario LEFT JOIN CARGO ON (CARGO.ID = USUARIO.ID_CARGO) LEFT JOIN SINDICATO ON (SINDICATO.ID = USUARIO.ID_SINDICATO) LEFT JOIN CIDADE ON (CIDADE.ID = USUARIO.ID_CIDADE) WHERE 1=1 ");
		return consultar(sql, params);
	}

	@Override
	public List<Usuario> findByCargo(Cargo cargo) {
		StringBuilder sql = new StringBuilder();
		MapSqlParameterSource params = new MapSqlParameterSource();
		sql.append("SELECT usuario.id, usuario.id_cargo as id_cargo, cargo.nome as nome_cargo, usuario.nome as nome, usuario.matricula, usuario.email as email, usuario.login as login, usuario.senha as senha, usuario.id_sindicato as id_sindicato, sindicato.descricao as sindicato_descricao, cidade.id as id_cidade, cidade.id_estado as id_cidade_estado, cidade.nome as cidade_nome ");
		sql.append("FROM usuario LEFT JOIN CARGO ON (CARGO.ID = USUARIO.ID_CARGO) LEFT JOIN SINDICATO ON (SINDICATO.ID = USUARIO.ID_SINDICATO) LEFT JOIN CIDADE ON (CIDADE.ID = USUARIO.ID_CIDADE) WHERE 1=1 ");
		sql.append("AND usuario.id_cargo = :idCargo");
		params.addValue("idCargo", cargo.getId());
		return consultar(sql, params);
	}

	@Override
	public Usuario findById(Object id) throws RegistroInexistenteException {

		try {
			StringBuilder sql = new StringBuilder();
			MapSqlParameterSource params = new MapSqlParameterSource();
			sql.append("SELECT usuario.id, usuario.id_cargo as id_cargo, cargo.nome as nome_cargo, usuario.nome as nome, usuario.matricula, usuario.email as email, usuario.login as login, usuario.senha as senha, usuario.id_sindicato as id_sindicato, sindicato.descricao as sindicato_descricao, cidade.id as id_cidade, cidade.id_estado as id_cidade_estado, cidade.nome as cidade_nome ");
			sql.append("FROM usuario LEFT JOIN CARGO ON (CARGO.ID = USUARIO.ID_CARGO) LEFT JOIN SINDICATO ON (SINDICATO.ID = USUARIO.ID_SINDICATO) LEFT JOIN CIDADE ON (CIDADE.ID = USUARIO.ID_CIDADE) WHERE 1=1 ");
			sql.append("AND usuario.id = :idUsuario");
			params.addValue("idUsuario", id);
			return consultarUmUsuario(sql, params);
		} catch (EmptyResultDataAccessException e) {
			throw new RegistroInexistenteException();
		}
	}
	
	@Override
	public Usuario findByLogin(String login) {

			StringBuilder sql = new StringBuilder();
			MapSqlParameterSource params = new MapSqlParameterSource();
			sql.append("SELECT usuario.id, usuario.id_cargo as id_cargo, cargo.nome as nome_cargo, usuario.nome as nome, usuario.matricula, usuario.email as email, usuario.login as login, usuario.senha as senha, usuario.id_sindicato as id_sindicato, sindicato.descricao as sindicato_descricao, cidade.id as id_cidade, cidade.id_estado as id_cidade_estado, cidade.nome as cidade_nome ");
			sql.append("FROM usuario LEFT JOIN CARGO ON (CARGO.ID = USUARIO.ID_CARGO) LEFT JOIN SINDICATO ON (SINDICATO.ID = USUARIO.ID_SINDICATO) LEFT JOIN CIDADE ON (CIDADE.ID = USUARIO.ID_CIDADE) WHERE 1=1 ");
			sql.append("AND usuario.login = :login");
			params.addValue("login", login);
			return consultarUmUsuario(sql, params);
		
	}

	@Override
	public List<Usuario> findByFilter(Usuario usuarioFiltro) {

		StringBuilder sql = new StringBuilder();
		MapSqlParameterSource params = new MapSqlParameterSource();

		sql.append("SELECT usuario.id, usuario.id_cargo as id_cargo, cargo.nome as nome_cargo, usuario.nome as nome, usuario.matricula, usuario.email as email, usuario.login as login, usuario.senha as senha, usuario.id_sindicato as id_sindicato, sindicato.descricao as sindicato_descricao, cidade.id as id_cidade, cidade.id_estado as id_cidade_estado, cidade.nome as cidade_nome ");
		sql.append("FROM usuario LEFT JOIN CARGO ON (CARGO.ID = USUARIO.ID_CARGO) LEFT JOIN SINDICATO ON (SINDICATO.ID = USUARIO.ID_SINDICATO) LEFT JOIN CIDADE ON (CIDADE.ID = USUARIO.ID_CIDADE) WHERE 1=1 ");

		if (usuarioFiltro != null && usuarioFiltro.getId() != null) {
			sql.append("AND usuario.id = :idUsuario");
			params.addValue("idUsuario", usuarioFiltro.getId());
		}
		if (usuarioFiltro != null && usuarioFiltro.getNome() != null && !usuarioFiltro.getNome().isEmpty()) {
			sql.append("AND LOWER(usuario.nome) LIKE '%' || :nomeUsuario || '%'");
			params.addValue("nomeUsuario", usuarioFiltro.getNome().toLowerCase());
		}
		if (usuarioFiltro != null && usuarioFiltro.getLogin() != null && !usuarioFiltro.getLogin().isEmpty()) {
			sql.append("AND LOWER(usuario.login) LIKE '%' || :loginUsuario || '%'");
			params.addValue("loginUsuario", usuarioFiltro.getLogin().toLowerCase());
		}
		if (usuarioFiltro != null && usuarioFiltro.getMatricula() != null
				&& !usuarioFiltro.getMatricula().toString().isEmpty()) {
			sql.append("AND usuario.matricula = :matriculaUsuario");
			params.addValue("matriculaUsuario", usuarioFiltro.getMatricula());
		}
		if (usuarioFiltro != null && usuarioFiltro.getCargo() != null
				&& !usuarioFiltro.getCargo().getNome().isEmpty()) {
			sql.append("AND usuario.id_cargo = :idCargo");
			params.addValue("idCargo", usuarioFiltro.getCargo().getId());
		}
		if (usuarioFiltro != null && usuarioFiltro.getSindicato() != null
				&& !usuarioFiltro.getSindicato().getDescricao().isEmpty()) {
			sql.append("AND usuario.id_sindicato = :idSindicato");
			params.addValue("idSindicato", usuarioFiltro.getSindicato().getId());
		}
		if (usuarioFiltro != null && usuarioFiltro.getSindicato() != null
				&& !usuarioFiltro.getSindicato().getDescricao().isEmpty()) {
			sql.append("AND usuario.id_sindicato = :idSindicato");
			params.addValue("idSindicato", usuarioFiltro.getSindicato().getId());
		}

		return consultar(sql, params);
	}

	@Override
	public void remove(List<Object> ids) throws RegistroInexistenteException, DeletarRegistroViolacaoFK {

		ArrayList<Object[]> params = new ArrayList<Object[]>(ids.size());
		for (Object id : ids) {
			Object[] param = new Object[] { id };
			params.add(param);
		}
		try {
			int[] affectedRows = getJdbcTemplate().batchUpdate("DELETE FROM usuario WHERE id = ?", params);
			for (int rows : affectedRows)
				if (rows == 0) throw new RegistroInexistenteException();
		} catch (DataIntegrityViolationException d) {
			throw new DeletarRegistroViolacaoFK();
		}
	}

	private List<Usuario> consultar(StringBuilder sql, MapSqlParameterSource params) {

		List<Usuario> lista = getNamedParameterJdbcTemplate().query(sql.toString(), params,
				new RowMapper<Usuario>() {
					@Override
					public Usuario mapRow(ResultSet rs, int idx) throws SQLException {

						Usuario usuario = new Usuario();

						Cargo cargo = new Cargo();
						cargo.setId(rs.getLong("id_cargo"));
						cargo.setNome(rs.getString("nome_cargo"));

						Cidade cidade = new Cidade();
						cidade.setId(rs.getLong("id_cidade"));
						Estado estado = new Estado();
						estado.setId(rs.getLong("id_cidade_estado"));
						cidade.setEstado(estado);
						cidade.setNome(rs.getString("cidade_nome"));

						Sindicato sind = new Sindicato();
						sind.setId(rs.getLong("id_sindicato"));
						sind.setDescricao(rs.getString("sindicato_descricao"));

						usuario.setCargo(cargo);
						usuario.setSindicato(sind);
						usuario.setCidade(cidade);
						usuario.setId(rs.getLong("id"));
						usuario.setNome(rs.getString("nome"));
						usuario.setMatricula(rs.getInt("matricula"));
						usuario.setEmail(rs.getString("email"));
						usuario.setLogin(rs.getString("login"));
						usuario.setSenha(rs.getString("senha"));

						return usuario;
					}
				});
		return lista;
	}

	public Usuario consultarUmUsuario(StringBuilder sql, MapSqlParameterSource params) {

		return getNamedParameterJdbcTemplate().queryForObject(sql.toString(), params, new RowMapper<Usuario>() {
			@Override
			public Usuario mapRow(ResultSet rs, int idx) throws SQLException {

				Usuario usuario = new Usuario();

				Cargo cargo = new Cargo();
				cargo.setId(rs.getLong("id_cargo"));
				cargo.setNome(rs.getString("nome_cargo"));

				Cidade cidade = new Cidade();
				cidade.setId(rs.getLong("id_cidade"));
				Estado estado = new Estado();
				estado.setId(rs.getLong("id_cidade_estado"));
				cidade.setEstado(estado);
				cidade.setNome(rs.getString("cidade_nome"));

				Sindicato sind = new Sindicato();
				sind.setId(rs.getLong("id_sindicato"));
				sind.setDescricao(rs.getString("sindicato_descricao"));

				usuario.setCargo(cargo);
				usuario.setSindicato(sind);
				usuario.setCidade(cidade);
				usuario.setId(rs.getLong("id"));
				usuario.setNome(rs.getString("nome"));
				usuario.setMatricula(rs.getInt("matricula"));
				usuario.setEmail(rs.getString("email"));
				usuario.setLogin(rs.getString("login"));
				usuario.setSenha(rs.getString("senha"));

				return usuario;
			}
		});
	}
	
	@Override
	public Usuario updatePassword(Usuario entity) throws RegistroInexistenteException{
		int rows = getJdbcTemplate().update("UPDATE usuario SET senha = ? WHERE id = ?", entity.getSenha(),  entity.getId());
			if (rows == 0) throw new RegistroInexistenteException();
		return entity;
	}

}
