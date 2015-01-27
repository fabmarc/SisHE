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
import com.indra.sishe.dao.SistemaDAO;
import com.indra.sishe.entity.Projeto;
import com.indra.sishe.entity.Sistema;
import com.indra.sishe.entity.Usuario;

@Repository
public class SistemaJdbcDaoImpl extends NamedParameterJdbcDaoSupport implements SistemaDAO {

	@Autowired
	private DataSource dataSource;

	private SimpleJdbcInsert insertSistema;

	@PostConstruct
	private void init() {
		setDataSource(dataSource);
		insertSistema = new SimpleJdbcInsert(getJdbcTemplate()).withTableName("sistema").usingGeneratedKeyColumns(
				"id");
	}

	public SistemaJdbcDaoImpl() {
	}

	@Override
	public Sistema save(Sistema entity) throws RegistroDuplicadoException {

		try {
			MapSqlParameterSource parms = new MapSqlParameterSource();

			parms.addValue("id_lider", entity.getLider().getId());
			parms.addValue("id_projeto", entity.getProjeto().getId());
			parms.addValue("nome", entity.getNome());
			parms.addValue("descricao", entity.getDescricao());

			Number key = insertSistema.executeAndReturnKey(parms);
			entity.setId(key.longValue());

		} catch (DuplicateKeyException e) {
			throw new RegistroDuplicadoException();
		}
		return entity;
	}

	@Override
	public Sistema update(Sistema entity) throws RegistroInexistenteException {

		int rows = getJdbcTemplate().update(
				"UPDATE sistema SET id_lider=?, id_projeto = ?, descricao = ?, nome = ?" + "WHERE id = ?",
				entity.getLider().getId(), entity.getProjeto().getId(), entity.getDescricao(), entity.getNome(),
				entity.getId());

		if (rows == 0) throw new RegistroInexistenteException();
		return entity;
	}

	@Override
	public List<Sistema> findAll() {
		StringBuilder sql = new StringBuilder();
		MapSqlParameterSource params = new MapSqlParameterSource();

		sql.append("SELECT s.id as idSistema, s.id_lider as idLider, s.id_projeto as idProjeto, s.descricao as sistemaDescricao,"
				+ " s.nome as nomeSistema , u.nome as nomeUsuario, u.id as idUsuario,"
				+ " p.id as idProjeto, p.nome as nomeProjeto ");
		sql.append(" FROM sistema s");
		sql.append(" INNER JOIN usuario u ON s.id_lider = u.id");
		sql.append(" INNER JOIN projeto p ON s.id_projeto = p.id ");
		sql.append(" WHERE 1 = 1 ");

		List<Sistema> lista = getNamedParameterJdbcTemplate().query(sql.toString(), params,

		new RowMapper<Sistema>() {
			@Override
			public Sistema mapRow(ResultSet rs, int idx) throws SQLException {

				Sistema sis = new Sistema();
				Projeto projeto = new Projeto();
				Usuario usuario = new Usuario();

				projeto.setId(rs.getLong("idProjeto"));
				projeto.setNome(rs.getString("nomeProjeto"));

				usuario.setId(rs.getLong("idUsuario"));
				usuario.setNome(rs.getString("nomeUsuario"));

				sis.setProjeto(projeto);
				sis.setLider(usuario);
				sis.setId(rs.getLong("idSistema"));
				sis.setNome(rs.getString("nomeSistema"));
				sis.setDescricao(rs.getString("sistemaDescricao"));

				return sis;
			}
		});

		return lista;
	}

	@Override
	public Sistema findById(Object id) throws RegistroInexistenteException {

		StringBuilder sql = new StringBuilder();
		sql.append("SELECT s.id as idSistema, s.id_lider as idLider, s.id_projeto as idProjeto, s.descricao as sistemaDescricao,"
				+ " s.nome as nomeSistema , u.nome as nomeUsuario, u.id as idUsuario,"
				+ " p.id as idProjeto, p.nome as nomeProjeto ");
		sql.append(" FROM sistema s");
		sql.append(" INNER JOIN usuario u ON s.id_lider = u.id");
		sql.append(" INNER JOIN projeto p ON s.id_projeto = p.id ");
		sql.append(" WHERE s.id = ?");

		try {
			return getJdbcTemplate().queryForObject(sql.toString(), new Object[] { id }, new RowMapper<Sistema>() {
				@Override
				public Sistema mapRow(ResultSet rs, int idx) throws SQLException {

					Sistema sis = new Sistema();
					Projeto projeto = new Projeto();
					Usuario usuario = new Usuario();

					projeto.setId(rs.getLong("idProjeto"));
					projeto.setNome(rs.getString("nomeProjeto"));

					usuario.setId(rs.getLong("idUsuario"));
					usuario.setNome(rs.getString("nomeUsuario"));

					sis.setProjeto(projeto);
					sis.setLider(usuario);
					sis.setId(rs.getLong("idSistema"));
					sis.setNome(rs.getString("nomeSistema"));
					sis.setDescricao(rs.getString("sistemaDescricao"));
					return sis;
				}
			});
		} catch (EmptyResultDataAccessException e) {
			throw new RegistroInexistenteException();
		}
	}

	public List<Sistema> findByFilter(Sistema sistema) {

		StringBuilder sql = new StringBuilder();
		MapSqlParameterSource params = new MapSqlParameterSource();

		sql.append("SELECT s.id as idSistema, s.id_lider as idLider, s.id_projeto as idProjeto, s.descricao as sistemaDescricao,"
				+ " s.nome as nomeSistema , u.nome as nomeUsuario, u.id as idUsuario,"
				+ " p.id as idProjeto, p.nome as nomeProjeto ");
		sql.append(" FROM sistema s");
		sql.append(" INNER JOIN usuario u ON s.id_lider = u.id");
		sql.append(" INNER JOIN projeto p ON s.id_projeto = p.id ");
		sql.append(" WHERE 1 = 1 ");

		if (!(sistema.getNome() == null) && !sistema.getNome().isEmpty()) {
			sql.append(" AND UPPER(s.nome) LIKE '%' || :nome || '%'");
			params.addValue("nome", sistema.getNome().toUpperCase());
		}

		if (!(sistema.getLider() == null) && !sistema.getLider().getNome().isEmpty()) {
			sql.append(" AND u.nome = :nomeUsu ");
			params.addValue("nomeUsu", sistema.getLider().getNome());
		}

		if (!(sistema.getProjeto() == null) && !sistema.getProjeto().getNome().isEmpty()) {
			sql.append(" AND p.nome = :nomeProjeto ");
			params.addValue("nomeProjeto", sistema.getProjeto().getNome());
		}

		List<Sistema> lista = getNamedParameterJdbcTemplate().query(sql.toString(), params,

		new RowMapper<Sistema>() {
			@Override
			public Sistema mapRow(ResultSet rs, int idx) throws SQLException {

				Sistema sis = new Sistema();
				Projeto projeto = new Projeto();
				Usuario usuario = new Usuario();

				projeto.setId(rs.getLong("idProjeto"));
				projeto.setNome(rs.getString("nomeProjeto"));

				usuario.setId(rs.getLong("idUsuario"));
				usuario.setNome(rs.getString("nomeUsuario"));

				sis.setProjeto(projeto);
				sis.setLider(usuario);
				sis.setId(rs.getLong("idSistema"));
				sis.setNome(rs.getString("nomeSistema"));
				sis.setDescricao(rs.getString("sistemaDescricao"));

				return sis;
			}
		});

		return lista;
	}

	@Override
	public void remove(Object id) throws RegistroInexistenteException, DeletarRegistroViolacaoFK {

		try {
			int rows = getJdbcTemplate().update("DELETE FROM sistema WHERE id = ?", id);
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
			int[] affectedRows = getJdbcTemplate().batchUpdate("DELETE FROM sistema WHERE id = ?", params);
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

}
