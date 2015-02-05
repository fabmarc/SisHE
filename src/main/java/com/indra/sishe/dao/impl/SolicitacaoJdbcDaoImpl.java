package com.indra.sishe.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcDaoSupport;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import com.indra.infra.dao.exception.DeletarRegistroViolacaoFK;
import com.indra.infra.dao.exception.RegistroDuplicadoException;
import com.indra.infra.dao.exception.RegistroInexistenteException;
import com.indra.sishe.controller.usuario.UsuarioLogado;
import com.indra.sishe.dao.SolicitacaoDAO;
import com.indra.sishe.entity.Projeto;
import com.indra.sishe.entity.Sistema;
import com.indra.sishe.entity.Solicitacao;
import com.indra.sishe.entity.Status;
import com.indra.sishe.entity.Usuario;

@Repository
public class SolicitacaoJdbcDaoImpl extends NamedParameterJdbcDaoSupport implements SolicitacaoDAO {

	@Autowired
	private DataSource dataSource;

	private SimpleJdbcInsert insertSolicitacao;

	@PostConstruct
	private void init() {
		setDataSource(dataSource);
		insertSolicitacao = new SimpleJdbcInsert(getJdbcTemplate()).withTableName("solicitacao")
				.usingGeneratedKeyColumns("id");
	}

	@Override
	public Solicitacao save(Solicitacao entity) throws RegistroDuplicadoException {
		/*
		 * try { MapSqlParameterSource params = new MapSqlParameterSource();
		 * 
		 * params.addValue("data", entity.getData());
		 * params.addValue("hora_inicio", entity.getHoraInicio());
		 * params.addValue("hora_final", entity.getHoraFinal());
		 * params.addValue("descricao", entity.getDescricao()); if
		 * (entity.getSistema() != null) { params.addValue("id_sistema",
		 * entity.getSistema().getId()); } if (entity.getUsuario() != null) {
		 * params.addValue("id_usuario", entity.getUsuario().getId()); } Number
		 * key = insertSolicitacao.executeAndReturnKey(params);
		 * entity.setId(key.longValue()); } catch (DuplicateKeyException e) {
		 * throw new RegistroDuplicadoException(e.toString()); }
		 */
		return null;// entity;
	}

	@Override
	public Solicitacao update(Solicitacao entity) throws RegistroInexistenteException, RegistroDuplicadoException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Solicitacao> findAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Solicitacao findById(Object id) throws RegistroInexistenteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void remove(Object id) throws RegistroInexistenteException, DeletarRegistroViolacaoFK {
		// TODO Auto-generated method stub

	}

	@Override
	public void remove(List<Object> ids) throws RegistroInexistenteException, DeletarRegistroViolacaoFK {
		// TODO Auto-generated method stub

	}

	@Override
	public EntityManager getEntityManager() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Solicitacao> findByLider(Usuario lider) {
		StringBuilder sql = new StringBuilder();
		MapSqlParameterSource params = new MapSqlParameterSource();
		sql.append("Select solicitacao.id as idSolicitacao, id_usuario, usuario.nome as nomeUsuario, id_sistema, sistema.nome as nomeSistema, sistema.id_lider, l.nome as nomeLider, data  ");
		sql.append("from solicitacao Inner join usuario on (usuario.id = solicitacao.id_usuario) Inner join sistema on (sistema.id = solicitacao.id_sistema) Inner join usuario l on (l.id = sistema.id_lider) where solicitacao.id_status_lider is null ");
		sql.append("AND sistema.id_lider = :idLider");
		params.addValue("idLider", lider.getId());

		List<Solicitacao> lista = getNamedParameterJdbcTemplate().query(sql.toString(), params,
				new RowMapper<Solicitacao>() {
					@Override
					public Solicitacao mapRow(ResultSet rs, int idx) throws SQLException {

						Solicitacao solicitacao = new Solicitacao();

						solicitacao.setData(rs.getDate("data"));
						solicitacao.setId(rs.getLong("idSolicitacao"));

						Sistema sistema = new Sistema();
						sistema.setId(rs.getLong("id_sistema"));
						sistema.setNome(rs.getString("nomeSistema"));

						solicitacao.setSistema(sistema);

						Usuario usuario = new Usuario();
						usuario.setId(rs.getLong("id_usuario"));
						usuario.setNome(rs.getString("nomeUsuario"));

						solicitacao.setUsuario(usuario);

						return solicitacao;
					}
				});
		return lista;
	}

	@Override
	public List<Solicitacao> findByGerente(Usuario gerente) {
		StringBuilder sql = new StringBuilder();
		MapSqlParameterSource params = new MapSqlParameterSource();
		sql.append("Select solicitacao.id as idSolicitacao, id_usuario, usuario.nome as nomeUsuario, id_sistema, sistema.nome as nomeSistema, sistema.id_lider, l.nome as nomeLider, projeto.nome as nomeProjeto, projeto.id as idprojeto, data ");
		sql.append("from solicitacao Inner join usuario on (usuario.id = solicitacao.id_usuario) inner join sistema on (sistema.id = solicitacao.id_sistema) inner join usuario l on (l.id = sistema.id_lider) inner join projeto on (projeto.id = sistema.id_projeto) where (solicitacao.id_status_lider = 1 AND solicitacao.id_status_gerente is null) ");
		sql.append("AND projeto.id_gerente = :idGerente");
		params.addValue("idGerente", gerente.getId());

		List<Solicitacao> lista = getNamedParameterJdbcTemplate().query(sql.toString(), params,
				new RowMapper<Solicitacao>() {
					@Override
					public Solicitacao mapRow(ResultSet rs, int idx) throws SQLException {

						Solicitacao solicitacao = new Solicitacao();

						solicitacao.setData(rs.getDate("data"));
						solicitacao.setId(rs.getLong("idSolicitacao"));

						Projeto projeto = new Projeto();
						projeto.setNome(rs.getString("nomeProjeto"));
						projeto.setId(rs.getLong("idprojeto"));

						Sistema sistema = new Sistema();
						sistema.setId(rs.getLong("id_sistema"));
						sistema.setNome(rs.getString("nomeSistema"));
						sistema.setProjeto(projeto);

						solicitacao.setSistema(sistema);

						Usuario usuario = new Usuario();
						usuario.setId(rs.getLong("id_usuario"));
						usuario.setNome(rs.getString("nomeUsuario"));

						solicitacao.setUsuario(usuario);

						return solicitacao;
					}
				});
		return lista;
	}

	@Override
	public List<Solicitacao> findByFilter(Solicitacao solicitacaoFiltro) {
		StringBuilder sql = new StringBuilder();
		MapSqlParameterSource params = new MapSqlParameterSource();
		sql.append("SELECT solicitacao.id AS idSolicitacao, hora_inicio, hora_final, solicitacao.descricao AS descricao, data_aprovacao_lider, data_aprovacao_gerente, data, id_status_lider, id_status_gerente, id_usuario, usuario.nome AS nomeUsuario, id_sistema, sistema.nome AS nomeSistema, id_aprovador_lider, lider.nome AS nomeLider, projeto.nome AS nomeProjeto, projeto.id AS idprojeto, id_aprovador_gerente, gerente.nome AS nomeGerente ");
		sql.append("FROM solicitacao INNER JOIN usuario ON (usuario.id = solicitacao.id_usuario) INNER JOIN sistema ON (sistema.id = solicitacao.id_sistema) LEFT JOIN usuario lider ON (lider.id = solicitacao.id_aprovador_lider) LEFT JOIN usuario gerente ON (gerente.id = solicitacao.id_aprovador_gerente) INNER JOIN projeto ON (projeto.id = sistema.id_projeto) ");
		sql.append("WHERE solicitacao.id_usuario = :idUsuario");
		params.addValue("idUsuario", solicitacaoFiltro.getUsuario().getId());

		List<Solicitacao> lista = getNamedParameterJdbcTemplate().query(sql.toString(), params,
				new RowMapper<Solicitacao>() {
					@Override
					public Solicitacao mapRow(ResultSet rs, int idx) throws SQLException {

						Projeto projeto = new Projeto();
						projeto.setNome(rs.getString("nomeProjeto"));
						projeto.setId(rs.getLong("idprojeto"));

						Sistema sistema = new Sistema();
						sistema.setId(rs.getLong("id_sistema"));
						sistema.setNome(rs.getString("nomeSistema"));
						sistema.setProjeto(projeto);

						Usuario usuario = new Usuario();
						usuario.setId(rs.getLong("id_usuario"));
						usuario.setNome(rs.getString("nomeUsuario"));

						Usuario lider = new Usuario();
						lider.setId(rs.getLong("id_aprovador_lider"));
						lider.setNome(rs.getString("nomeLider"));
						
						Usuario gerente = new Usuario();
						gerente.setId(rs.getLong("id_aprovador_gerente"));
						gerente.setNome(rs.getString("nomeGerente"));
						
						Status statusLider = new Status();
						statusLider.setId(rs.getLong("id_status_lider"));
						Status statusGerente = new Status();
						statusGerente.setId(rs.getLong("id_status_gerente"));
						
						Solicitacao solicitacao = new Solicitacao();
						solicitacao.setStatusLider(statusLider);
						solicitacao.setStatusGerente(statusGerente);
						solicitacao.setUsuario(usuario);
						solicitacao.setSistema(sistema);
						solicitacao.setLider(lider);
						solicitacao.setGerente(gerente);
						solicitacao.setId(rs.getLong("idSolicitacao"));
						solicitacao.setHoraInicio(rs.getTime("hora_inicio"));
						solicitacao.setHoraFinal(rs.getTime("hora_final"));
						solicitacao.setDescricao(rs.getString("descricao"));
						solicitacao.setDataAprovacaoLider(rs.getDate("data_aprovacao_lider"));
						solicitacao.setDataAprovacaoGerente(rs.getDate("data_aprovacao_gerente"));
						solicitacao.setData(rs.getDate("data"));

						return solicitacao;
					}
				});
		return lista;
	}

	@Override
	public void liderAcaoSolicitacao(final List<Long> ids, final int status) throws RegistroInexistenteException {

		ArrayList<Object[]> params = new ArrayList<Object[]>(ids.size());
		for (Object id : ids) {
			Object[] param = new Object[] { (int) status, (Long) UsuarioLogado.getId(), (Date) new Date(),
					(Long) id };
			params.add(param);
		}
		int[] affectedRows = getJdbcTemplate().batchUpdate(
				"UPDATE solicitacao SET id_status_lider = ?,  id_aprovador_lider = ?,"
						+ " data_aprovacao_lider = ? WHERE id = ?", params);
		for (int rows : affectedRows)
			if (rows == 0) throw new RegistroInexistenteException();
	}

	@Override
	public void gerenteAcaoSolicitacao(List<Long> ids, int status) throws RegistroInexistenteException {
		ArrayList<Object[]> params = new ArrayList<Object[]>(ids.size());
		for (Object id : ids) {
			Object[] param = new Object[] { (int) status, (Long) UsuarioLogado.getId(), (Date) new Date(),
					(Long) id };
			params.add(param);
		}
		int[] affectedRows = getJdbcTemplate()
				.batchUpdate(
						"UPDATE solicitacao SET id_status_gerente = ?,  id_aprovador_gerente = ?, data_aprovacao_gerente = ? WHERE id = ?",
						params);
		for (int rows : affectedRows)
			if (rows == 0) throw new RegistroInexistenteException();
	}

}