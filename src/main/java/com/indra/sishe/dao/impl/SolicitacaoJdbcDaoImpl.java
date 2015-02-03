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
import com.indra.sishe.controller.usuario.UsuarioLogado;
import com.indra.sishe.dao.SolicitacaoDAO;
import com.indra.sishe.entity.Projeto;
import com.indra.sishe.entity.Sistema;
import com.indra.sishe.entity.Solicitacao;
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
		sql.append("from solicitacao Inner join usuario on (usuario.id = solicitacao.id_usuario) inner join sistema on (sistema.id = solicitacao.id_sistema) inner join usuario l on (l.id = sistema.id_lider) inner join projeto on (projeto.id = sistema.id_projeto) where (solicitacao.id_status_lider is not null AND solicitacao.id_status_gerente is null) ");
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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void aprovarSolicitacoes(List<Long> ids) throws RegistroInexistenteException {
		String temp = ids.toString();
		temp = (String) temp.subSequence(1, temp.length()-1);//remover os caracteres '[' e ']';
		temp = "(" + temp + ")";
		System.out.println(temp);
		
		getJdbcTemplate().update("UPDATE solicitacao SET id_status_lider = 1,  id_aprovador_lider = ? WHERE id in ?", UsuarioLogado.getId(), temp);
		/*for (Long id : ids) {
		}*/
		//int rows = 0;
		//rows = getJdbcTemplate().update("UPDATE solicitacao SET id_status_lider = 1,  id_aprovador_lider = ?, data_aprovacao_lider = ? WHERE id in ?", UsuarioLogado.getId(), new Date(),  params);
		
		//if (rows == 0) throw new RegistroInexistenteException();
		
		
		/*int[] affectedRows = getJdbcTemplate().batchUpdate("UPDATE solicitacao SET id_status_lider = 1,  id_aprovador_lider = ?, data_aprovacao_lider = ? WHERE id = ?", UsuarioLogado.getId(), new Date(),  params);
		for (int rows : affectedRows)
			if (rows == 0) throw new RegistroInexistenteException();	*/	
	}

}
