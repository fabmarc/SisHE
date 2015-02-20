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
import com.indra.sishe.dao.UsuarioProjetoDAO;
import com.indra.sishe.entity.Cargo;
import com.indra.sishe.entity.Projeto;
import com.indra.sishe.entity.Usuario;
import com.indra.sishe.entity.UsuarioProjeto;

@Repository
public class UsuarioProjetoJdbcDaoImpl extends NamedParameterJdbcDaoSupport implements UsuarioProjetoDAO {

	@Autowired
	private DataSource dataSource;
	private SimpleJdbcInsert insertUsuarioProjeto;

	@PostConstruct
	private void init() {
		setDataSource(dataSource);
		insertUsuarioProjeto = new SimpleJdbcInsert(getJdbcTemplate()).withTableName("usuario_projeto")
				.usingGeneratedKeyColumns("id");
	}

	@Override
	public UsuarioProjeto save(UsuarioProjeto entity) throws RegistroDuplicadoException {

		try {
			Number key = insertUsuarioProjeto.executeAndReturnKey(new BeanPropertySqlParameterSource(entity));
			entity.setId(key.longValue());
		} catch (DuplicateKeyException e) {
			throw new RegistroDuplicadoException(e.toString());
		}
		return entity;
	}

	@Override
	public void salvar(List<Usuario> usuarios, Projeto projeto) {
		ArrayList<Object[]> params = new ArrayList<Object[]>(usuarios.size());
		for (Usuario user : usuarios) {
			Object[] param = new Object[] { user.getId(), projeto.getId() };
			params.add(param);
		}

		int[] affectedRows = getJdbcTemplate().batchUpdate(
				"INSERT INTO usuario_projeto(id_usuario, id_projeto) values (?,?)", params);
	}

	@Override
	public UsuarioProjeto update(UsuarioProjeto entity) throws RegistroInexistenteException,
			RegistroDuplicadoException {

		try {
			int rows = getJdbcTemplate().update(
					"UPDATE usuario_projeto SET id_usuario = ?, id_projeto = ? " + "WHERE id = ?",
					entity.getUsuario().getId(), entity.getProjeto().getId(), entity.getId());
			if (rows == 0) throw new RegistroInexistenteException();
		} catch (DuplicateKeyException e) {
			throw new RegistroDuplicadoException(e.toString());
		}
		return entity;
	}

	@Override
	public List<UsuarioProjeto> findAll() {
		StringBuilder sql = new StringBuilder();
		MapSqlParameterSource params = new MapSqlParameterSource();

		sql.append("SELECT up.id as idUP,up.id_usuario AS idUsuario,up.id_projeto AS idProjeto,"
				+ " u.nome AS nomeUsuario, p.nome AS nomeProjeto ");
		sql.append(" FROM usuario_projeto up");
		sql.append("INNER JOIN usuario u on u.id = up.id");
		sql.append("INNER JOIN projeto p on p.id = up.id ");
		sql.append(" WHERE 1 = 1 ");

		List<UsuarioProjeto> lista = getNamedParameterJdbcTemplate().query(sql.toString(), params,

		new RowMapper<UsuarioProjeto>() {
			@Override
			public UsuarioProjeto mapRow(ResultSet rs, int idx) throws SQLException {

				UsuarioProjeto up = new UsuarioProjeto();
				Projeto projeto = new Projeto();
				Usuario usuario = new Usuario();

				projeto.setId(rs.getLong("idProjeto"));
				projeto.setNome(rs.getString("nomeProjeto"));

				usuario.setId(rs.getLong("idUsuario"));
				usuario.setNome(rs.getString("nomeUsuario"));

				up.setProjeto(projeto);
				up.setUsuario(usuario);
				up.setId(rs.getLong("idUP"));

				return up;
			}
		});

		return lista;
	}

	@Override
	public UsuarioProjeto findById(Object id) throws RegistroInexistenteException {
		StringBuilder sql = new StringBuilder();

		sql.append("SELECT up.id as idUP,up.id_usuario AS idUsuario,up.id_projeto AS idProjeto,"
				+ " u.nome AS nomeUsuario, p.nome AS nomeProjeto ");
		sql.append(" FROM usuario_projeto up");
		sql.append("INNER JOIN usuario u on u.id = up.id");
		sql.append("INNER JOIN projeto p on p.id = up.id ");
		sql.append(" WHERE id = ? ");

		try {
			return getJdbcTemplate().queryForObject(sql.toString(), new Object[] { id },
					new RowMapper<UsuarioProjeto>() {
						@Override
						public UsuarioProjeto mapRow(ResultSet rs, int idx) throws SQLException {

							UsuarioProjeto up = new UsuarioProjeto();
							Projeto projeto = new Projeto();
							Usuario usuario = new Usuario();

							projeto.setId(rs.getLong("idProjeto"));
							projeto.setNome(rs.getString("nomeProjeto"));

							usuario.setId(rs.getLong("idUsuario"));
							usuario.setNome(rs.getString("nomeUsuario"));

							up.setProjeto(projeto);
							up.setUsuario(usuario);
							up.setId(rs.getLong("idUP"));

							return up;
						}
					});
		} catch (EmptyResultDataAccessException e) {
			throw new RegistroInexistenteException();
		}
	}

	@Override
	public void remove(Object id) throws RegistroInexistenteException, DeletarRegistroViolacaoFK {

		try {
			int rows = getJdbcTemplate().update("DELETE FROM usuario_projeto WHERE id = ?", id);
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
			int[] affectedRows = getJdbcTemplate().batchUpdate("DELETE FROM usuario_projeto WHERE id = ?", params);
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
	public List<UsuarioProjeto> findByFilter(UsuarioProjeto usuarioProjeto) {

		StringBuilder sql = new StringBuilder();
		MapSqlParameterSource params = new MapSqlParameterSource();

		sql.append("SELECT up.id as idUP,up.id_usuario AS idUsuario,up.id_projeto AS idProjeto,"
				+ " u.nome AS nomeUsuario, c.id AS idCargo,c.nome AS cargoUsuario, p.nome AS nomeProjeto ");
		sql.append(" FROM usuario_projeto up ");
		sql.append(" INNER JOIN usuario u on u.id = up.id ");
		sql.append(" INNER JOIN projeto p on p.id = up.id ");
		sql.append(" INNER JOIN cargo c on c.id = u.id ");
		sql.append(" WHERE 1 = 1 ");

		if (!(usuarioProjeto.getUsuario() == null) && !usuarioProjeto.getUsuario().getNome().isEmpty()) {
			sql.append(" AND UPPER(u.nome) LIKE '%' || :nome || '%'");
			params.addValue("nome", usuarioProjeto.getUsuario().getNome().toUpperCase());
		}

		if (!(usuarioProjeto.getProjeto() == null) && !usuarioProjeto.getProjeto().getNome().isEmpty()) {
			sql.append(" AND p.nome = :nomeProjeto ");
			params.addValue("nomeProjeto", usuarioProjeto.getProjeto().getNome());
		}

		List<UsuarioProjeto> lista = getNamedParameterJdbcTemplate().query(sql.toString(), params,
				new RowMapper<UsuarioProjeto>() {
					@Override
					public UsuarioProjeto mapRow(ResultSet rs, int idx) throws SQLException {
						UsuarioProjeto up = new UsuarioProjeto();
						Projeto projeto = new Projeto();
						Usuario usuario = new Usuario();
						Cargo cargo = new Cargo();

						cargo.setId(rs.getLong("idCargo"));
						cargo.setNome(rs.getString("cargoUsuario"));

						projeto.setId(rs.getLong("idProjeto"));
						projeto.setNome(rs.getString("nomeProjeto"));

						usuario.setId(rs.getLong("idUsuario"));
						usuario.setNome(rs.getString("nomeUsuario"));
						usuario.setCargo(cargo);

						up.setProjeto(projeto);
						up.setUsuario(usuario);
						up.setId(rs.getLong("idUP"));

						return up;
					}
				});
		return lista;
	}

	@Override
	public List<UsuarioProjeto> findByProjeto(UsuarioProjeto usuarioProjeto) {
		StringBuilder sql = new StringBuilder();
		MapSqlParameterSource params = new MapSqlParameterSource();

		sql.append("SELECT u.nome AS nomeUsuario, up.id as idUP ,u.id as usuarioId, c.nome AS nomeCargo, up.id_projeto AS idProjeto,p.nome as nomeProjeto");
		sql.append(" FROM usuario_projeto up");
		sql.append(" INNER JOIN usuario u ON up.id_usuario = u.id");
		sql.append(" INNER JOIN cargo c ON c.id = u.id_cargo ");
		sql.append(" INNER JOIN projeto p ON p.id = up.id_projeto ");
		sql.append(" WHERE 1 = 1");

		if (usuarioProjeto.getProjeto() != null && usuarioProjeto.getProjeto().getId() != null) {
			sql.append(" AND up.id_projeto = :idProjeto");
			params.addValue("idProjeto", usuarioProjeto.getProjeto().getId());
		}

		List<UsuarioProjeto> lista = getNamedParameterJdbcTemplate().query(sql.toString(), params,
				new RowMapper<UsuarioProjeto>() {
					@Override
					public UsuarioProjeto mapRow(ResultSet rs, int idx) throws SQLException {

						Usuario usuario = new Usuario();
						Projeto projeto = new Projeto();
						Cargo cargo = new Cargo();
						UsuarioProjeto up = new UsuarioProjeto();

						usuario.setNome(rs.getString("nomeUsuario"));
						usuario.setId(rs.getLong("usuarioId"));
						cargo.setNome(rs.getString("nomeCargo"));
						usuario.setCargo(cargo);

						projeto.setId(rs.getLong("idProjeto"));

						up.setUsuario(usuario);
						up.setProjeto(projeto);
						up.setId(rs.getLong("idUP"));

						return up;
					}
				});
		return lista;
	}

	@Override
	public List<Usuario> findUserNotInProjeto(UsuarioProjeto usuarioProjeto) {
		StringBuilder sql = new StringBuilder();
		MapSqlParameterSource params = new MapSqlParameterSource();

		sql.append("SELECT u.nome AS nomeUsuario, u.id as usuarioId, c.nome AS nomeCargo, c.id as idCargo ");
		sql.append(" FROM usuario u");
		sql.append(" INNER JOIN cargo c ON c.id = u.id_cargo ");
		sql.append(" LEFT JOIN usuario_projeto up ON (up.id_usuario = u.id) ");
		sql.append(" WHERE 1 = 1");

		if (usuarioProjeto.getProjeto() != null && usuarioProjeto.getProjeto().getId() != 0) {
			sql.append(" AND up.id_projeto IS NULL");
			params.addValue("idProjeto", usuarioProjeto.getProjeto().getId());
		}
		
		if (usuarioProjeto.getProjeto() != null && usuarioProjeto.getProjeto().getGerente().getId() != 0) {
			sql.append(" AND c.id <> 2");
		}

		List<Usuario> lista = getNamedParameterJdbcTemplate().query(sql.toString(), params,
				new RowMapper<Usuario>() {
					@Override
					public Usuario mapRow(ResultSet rs, int idx) throws SQLException {

						Usuario usuario = new Usuario();
						Cargo cargo = new Cargo();

						usuario.setNome(rs.getString("nomeUsuario"));
						usuario.setId(rs.getLong("usuarioId"));

						cargo.setId(rs.getLong("idCargo"));
						cargo.setNome(rs.getString("nomeCargo"));
						usuario.setCargo(cargo);

						return usuario;
					}
				});
		return lista;
	}
}
