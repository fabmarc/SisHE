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
import com.indra.sishe.controller.usuario.UsuarioLogado;
import com.indra.sishe.dao.FolgaDAO;
import com.indra.sishe.entity.Cargo;
import com.indra.sishe.entity.Folga;
import com.indra.sishe.entity.Usuario;
import com.indra.sishe.enums.StatusEnum;

@Repository
public class FolgaJdbcDaoImpl extends NamedParameterJdbcDaoSupport implements FolgaDAO {

	@Autowired
	private DataSource dataSource;

	private SimpleJdbcInsert insertFolga;

	private String selectPadrao;
	
	@PostConstruct
	private void init() {
		setDataSource(dataSource);
		insertFolga = new SimpleJdbcInsert(getJdbcTemplate()).withTableName("folga").usingGeneratedKeyColumns("id");
		selectPadrao = "SELECT DISTINCT ON (f.id) f.id AS idFolga ,f.id_solicitante AS idSolicitante ,solic.nome AS nomeSolicitante ," +
				"cs.id as idCargoSolicitante ,cs.nome AS cargoSolicitante ,f.id_lider AS idLider ,lider.nome AS nomeLider ," +
				"f.id_gerente AS idGerente ,gerente.nome AS nomeGerente ,f.data_solicitacao AS dataSolicitacao ," +
				"f.data_aprovacao_lider AS dataAprovacaoLider ,f.data_aprovacao_gerente AS dataAprovacaoGerente ," +
				"f.observacao AS observacao ,f.id_status_lider AS statusLider, f.id_status_gerente AS statusGerente " +
				"FROM folga f " +
				"INNER JOIN usuario solic ON (f.id_solicitante = solic.id) " +
				"INNER JOIN cargo cs ON (solic.id_cargo = cs.id) " +
				"LEFT JOIN usuario lider ON (f.id_lider = lider.id) " +
				"LEFT JOIN usuario gerente ON (f.id_gerente = gerente.id) " +
				"LEFT JOIN cargo ca ON (gerente.id_cargo = ca.id) " +
				"LEFT JOIN datas_folga df ON (f.id = df.id_folga) ";
	}

	@Override
	public Folga save(Folga entity) throws RegistroDuplicadoException {

		try {
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue("id_status", entity.getObservacao());
			params.addValue("id_solicitante", UsuarioLogado.getId());
			params.addValue("id_lider", entity.getLider().getId());
			params.addValue("id_gerente", entity.getGerente().getId());
			params.addValue("data_solicitacao", entity.getDataSolicitacao());
			params.addValue("data_aprovacao_lider", entity.getDataAprovacaoLider());
			params.addValue("data_aprovacao_gerente", entity.getDataAprovacaoGerente());
			params.addValue("observacao", entity.getObservacao());

			Number key = insertFolga.executeAndReturnKey(params);
			entity.setId(key.longValue());
		} catch (DuplicateKeyException e) {
			throw new RegistroDuplicadoException(e.toString());
		}

		return entity;
	}

	@Override
	public Folga update(Folga entity) throws RegistroInexistenteException, RegistroDuplicadoException {
		int rows = 0;
		try {
			rows = getJdbcTemplate().update("UPDATE folga SET observacao = ? WHERE id = ?", entity.getObservacao());
			if (rows == 0) throw new RegistroInexistenteException();
		} catch (DuplicateKeyException e) {
			throw new RegistroDuplicadoException(e.toString());
		}
		return entity;
	}

	@Override
	public List<Folga> findAll() {
		StringBuilder sql = new StringBuilder();
		MapSqlParameterSource params = new MapSqlParameterSource();
		sql.append(selectPadrao);
		sql.append("ORDER BY f.id DESC ");

		List<Folga> lista = getNamedParameterJdbcTemplate().query(sql.toString(), params,

		new RowMapper<Folga>() {
			@Override
			public Folga mapRow(ResultSet rs, int idx) throws SQLException {
				Cargo cargoSolicitante = new Cargo();
				Usuario solicitante = new Usuario();
				Usuario lider = new Usuario();
				Usuario gerente = new Usuario();
				Folga folga = new Folga();

				cargoSolicitante.setId(rs.getLong("idCargoSolicitante"));
				cargoSolicitante.setNome(rs.getString("cargoSolicitante"));

				solicitante.setId(rs.getLong("idSolicitante"));
				solicitante.setNome(rs.getString("nomeSolicitante"));
				lider.setId(rs.getLong("idLider"));
				lider.setNome(rs.getString("nomeLider"));
				gerente.setId(rs.getLong("idGerente"));
				gerente.setNome(rs.getString("nomeGerente"));
				

				folga.setId(rs.getLong("idFolga"));
				folga.setDataAprovacaoLider(rs.getDate("dataAprovacaoLider"));
				folga.setDataAprovacaoGerente(rs.getDate("dataAprovacaoGerente"));
				folga.setDataSolicitacao(rs.getDate("dataSolicitacao"));
				folga.setLider(lider);
				folga.setGerente(gerente);
				folga.setSolicitante(solicitante);
				folga.setObservacao(rs.getString("observacao"));
				folga.setStatusLider(StatusEnum.obterStatus(rs.getLong("statusLider")));
				folga.setStatusGerente(StatusEnum.obterStatus(rs.getLong("statusGerente")));
				return folga;
			}
		});
		return lista;
	}

	@Override
	public Folga findById(Object id) throws RegistroInexistenteException {
		try {
			StringBuilder sql = new StringBuilder();
			MapSqlParameterSource params = new MapSqlParameterSource();
			sql.append(selectPadrao);
			sql.append("WHERE f.id = :idFolga ");
			params.addValue("idFolga", id);

			return getNamedParameterJdbcTemplate().queryForObject(sql.toString(), params, new RowMapper<Folga>() {
				@Override
				public Folga mapRow(ResultSet rs, int idx) throws SQLException {
					Cargo cargoSolicitante = new Cargo();
					Usuario solicitante = new Usuario();
					Usuario lider = new Usuario();
					Usuario gerente = new Usuario();
					Folga folga = new Folga();

					cargoSolicitante.setId(rs.getLong("idCargoSolicitante"));
					cargoSolicitante.setNome(rs.getString("cargoSolicitante"));

					solicitante.setId(rs.getLong("idSolicitante"));
					solicitante.setNome(rs.getString("nomeSolicitante"));
					lider.setId(rs.getLong("idLider"));
					lider.setNome(rs.getString("nomeLider"));
					gerente.setId(rs.getLong("idGerente"));
					gerente.setNome(rs.getString("nomeGerente"));
					

					folga.setId(rs.getLong("idFolga"));
					folga.setDataAprovacaoLider(rs.getDate("dataAprovacaoLider"));
					folga.setDataAprovacaoGerente(rs.getDate("dataAprovacaoGerente"));
					folga.setDataSolicitacao(rs.getDate("dataSolicitacao"));
					folga.setLider(lider);
					folga.setGerente(gerente);
					folga.setSolicitante(solicitante);
					folga.setObservacao(rs.getString("observacao"));
					folga.setStatusLider(StatusEnum.obterStatus(rs.getLong("statusLider")));
					folga.setStatusGerente(StatusEnum.obterStatus(rs.getLong("statusGerente")));
					return folga;
				}
			});
		} catch (EmptyResultDataAccessException e) {
			throw new RegistroInexistenteException();
		}
	}

	@Override
	public void remove(Object id) throws RegistroInexistenteException, DeletarRegistroViolacaoFK {
	}

	@Override
	public void remove(List<Object> ids) throws RegistroInexistenteException, DeletarRegistroViolacaoFK {

		ArrayList<Object[]> params = new ArrayList<Object[]>(ids.size());
		for (Object id : ids) {
			Object[] param = new Object[] { id };
			params.add(param);
		}
		try {
			int[] affectedRows = getJdbcTemplate().batchUpdate("DELETE FROM folga WHERE id = ?", params);
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
	public List<Folga> findByFilter(Folga folga) {
		StringBuilder sql = new StringBuilder();
		MapSqlParameterSource params = new MapSqlParameterSource();
		sql.append("SELECT DISTINCT ON (f.id) f.id AS idFolga ,f.id_solicitante AS idSolicitante ,solic.nome AS nomeSolicitante ," +
				"cs.nome AS cargoSolicitante ,f.id_avaliador AS idAprovador ,aprov.nome AS nomeAprovador ,ca.nome AS cargoAprovador ," +
				"f.data_solicitacao AS dataSolicitacao ,f.data_aprovacao AS dataAprovacao ,f.data_folga AS dataFolga ," +
				"f.observacao AS observacao ,f.id_status AS statusFolga ,cs.id AS idCargoSolicitante ,ca.id AS idCargoAprovador " +
				"FROM folga f INNER JOIN usuario solic ON (f.id_solicitante = solic.id) " +
				"INNER JOIN cargo cs ON (solic.id_cargo = cs.id) " +
				"LEFT JOIN usuario aprov ON (f.id_avaliador = aprov.id) " +
				"LEFT JOIN cargo ca ON (aprov.id_cargo = ca.id) " +
				"LEFT JOIN datas_folga df ON (f.id = df.id_folga) ");
		sql.append("WHERE 1=1 ");

		if (folga != null) {

			if (folga.getStatusGerente() != null) {
				if (folga.getStatusGerente().getId() == 2) {
					sql.append("AND (f.id_status_lider = :status || f.id_status_gerente = :status )");
				}else {
					sql.append("AND f.id_status_gerente = :status ");
				}
				sql.append("AND f.id_status_lider = :status ");
				params.addValue("status", folga.getStatusGerente());
			}

			if (folga.getSolicitante() != null && folga.getSolicitante().getId() != null) {
				sql.append("AND f.id_solicitante = :solicitante ");
				params.addValue("solicitante", folga.getSolicitante().getId());
			}
			if (folga.getGerente() != null && folga.getGerente().getNome() != null) {
				sql.append("AND (lider.nome LIKE '%' || :nomeAprovador || '%' || gerente.nome LIKE '%' || :nomeAprovador || '%' ) ");
				params.addValue("nomeAprovador", folga.getGerente().getNome());
			}
			if (folga.getDataAprovacaoGerente() != null) {
				sql.append("AND f.data_aprovacao = :dataAprovacao ");
				params.addValue("dataAprovacao", folga.getDataAprovacaoGerente());
			}
			if (folga.getDatasFolga() != null && folga.getDatasFolga().get(0) != null && folga.getDatasFolga().get(0).getData() != null) {
				sql.append("AND f.data_folga = :dataFolga ");
				params.addValue("dataFolga", folga.getDatasFolga().get(0).getData());
			}
			if (folga.getDataSolicitacao() != null) {
				sql.append("AND f.data_solicitacao = :dataSolicitacao ");
				params.addValue("dataSolicitacao", folga.getDataSolicitacao());
			}

		}

		sql.append("ORDER BY f.id DESC ");

		List<Folga> lista = getNamedParameterJdbcTemplate().query(sql.toString(), params,

		new RowMapper<Folga>() {
			@Override
			public Folga mapRow(ResultSet rs, int idx) throws SQLException {
				Cargo cargoSolicitante = new Cargo();
				Usuario solicitante = new Usuario();
				Usuario lider = new Usuario();
				Usuario gerente = new Usuario();
				Folga folga = new Folga();

				cargoSolicitante.setId(rs.getLong("idCargoSolicitante"));
				cargoSolicitante.setNome(rs.getString("cargoSolicitante"));

				solicitante.setId(rs.getLong("idSolicitante"));
				solicitante.setNome(rs.getString("nomeSolicitante"));
				lider.setId(rs.getLong("idLider"));
				lider.setNome(rs.getString("nomeLider"));
				gerente.setId(rs.getLong("idGerente"));
				gerente.setNome(rs.getString("nomeGerente"));
				

				folga.setId(rs.getLong("idFolga"));
				folga.setDataAprovacaoLider(rs.getDate("dataAprovacaoLider"));
				folga.setDataAprovacaoGerente(rs.getDate("dataAprovacaoGerente"));
				folga.setDataSolicitacao(rs.getDate("dataSolicitacao"));
				folga.setLider(lider);
				folga.setGerente(gerente);
				folga.setSolicitante(solicitante);
				folga.setObservacao(rs.getString("observacao"));
				folga.setStatusLider(StatusEnum.obterStatus(rs.getLong("statusLider")));
				folga.setStatusGerente(StatusEnum.obterStatus(rs.getLong("statusGerente")));
				return folga;
			}
		});
		return lista;
	}

	@Override
	public List<Folga> findFolgasByLider(Folga folgaFiltro, Long idLiderLogado) {

		StringBuilder sql = new StringBuilder();
		MapSqlParameterSource params = new MapSqlParameterSource();
		sql.append("SELECT DISTINCT ON (f.id) f.id AS idFolga ,f.id_solicitante AS idSolicitante ,solic.nome AS nomeSolicitante ," +
				"cs.nome AS cargoSolicitante ,f.id_avaliador AS idAprovador ,aprov.nome AS nomeAprovador ,ca.nome AS cargoAprovador ," +
				"f.data_solicitacao AS dataSolicitacao ,f.data_aprovacao AS dataAprovacao ,f.data_folga AS dataFolga ," +
				"f.observacao AS observacao ,f.id_status AS statusFolga ,cs.id AS idCargoSolicitante ,ca.id AS idCargoAprovador " +
				"FROM folga f INNER JOIN usuario solic ON (f.id_solicitante = solic.id) " +
				"INNER JOIN cargo cs ON (solic.id_cargo = cs.id) " +
				"LEFT JOIN usuario aprov ON (f.id_avaliador = aprov.id) " +
				"LEFT JOIN cargo ca ON (aprov.id_cargo = ca.id) " +
				"LEFT JOIN datas_folga df ON (f.id = df.id_folga) ");
		
		sql.append("WHERE proj.id_gerente = :idGerente ");
		params.addValue("idGerente", idLiderLogado);

		if (folgaFiltro != null) {

			if (folgaFiltro.getStatusGerente() != null) {
				if (folgaFiltro.getStatusGerente().getId() == 2) {
					sql.append("AND (f.id_status_lider = :status || f.id_status_gerente = :status )");
				}else {
					sql.append("AND f.id_status_gerente = :status ");
				}
				sql.append("AND f.id_status_lider = :status ");
				params.addValue("status", folgaFiltro.getStatusGerente());
			}

			if (folgaFiltro.getSolicitante() != null && folgaFiltro.getSolicitante().getId() != null) {
				sql.append("AND f.id_solicitante = :solicitante ");
				params.addValue("solicitante", folgaFiltro.getSolicitante().getId());
			}
			if (folgaFiltro.getGerente() != null && folgaFiltro.getGerente().getNome() != null) { // Campo "Aprovado por:" na tela de consulta, pode ser gerente ou Lider
				sql.append("AND (lider.nome LIKE '%' || :nomeAprovador || '%' || gerente.nome LIKE '%' || :nomeAprovador || '%' ) ");
				params.addValue("nomeAprovador", folgaFiltro.getGerente().getNome());
			}
			if (folgaFiltro.getDataAprovacaoGerente() != null) {
				sql.append("AND f.data_aprovacao = :dataAprovacao ");
				params.addValue("dataAprovacao", folgaFiltro.getDataAprovacaoGerente());
			}
			if (folgaFiltro.getDatasFolga() != null && folgaFiltro.getDatasFolga().get(0) != null && folgaFiltro.getDatasFolga().get(0).getData() != null) {
				sql.append("AND f.data_folga = :dataFolga ");
				params.addValue("dataFolga", folgaFiltro.getDatasFolga().get(0).getData());
			}
			if (folgaFiltro.getDataSolicitacao() != null) {
				sql.append("AND f.data_solicitacao = :dataSolicitacao ");
				params.addValue("dataSolicitacao", folgaFiltro.getDataSolicitacao());
			}
		}
		sql.append("ORDER BY f.id DESC ");

		List<Folga> lista = getNamedParameterJdbcTemplate().query(sql.toString(), params,

		new RowMapper<Folga>() {
			@Override
			public Folga mapRow(ResultSet rs, int idx) throws SQLException {
				Cargo cargoSolicitante = new Cargo();
				Usuario solicitante = new Usuario();
				Usuario lider = new Usuario();
				Usuario gerente = new Usuario();
				Folga folga = new Folga();

				cargoSolicitante.setId(rs.getLong("idCargoSolicitante"));
				cargoSolicitante.setNome(rs.getString("cargoSolicitante"));

				solicitante.setId(rs.getLong("idSolicitante"));
				solicitante.setNome(rs.getString("nomeSolicitante"));
				lider.setId(rs.getLong("idLider"));
				lider.setNome(rs.getString("nomeLider"));
				gerente.setId(rs.getLong("idGerente"));
				gerente.setNome(rs.getString("nomeGerente"));
				

				folga.setId(rs.getLong("idFolga"));
				folga.setDataAprovacaoLider(rs.getDate("dataAprovacaoLider"));
				folga.setDataAprovacaoGerente(rs.getDate("dataAprovacaoGerente"));
				folga.setDataSolicitacao(rs.getDate("dataSolicitacao"));
				folga.setLider(lider);
				folga.setGerente(gerente);
				folga.setSolicitante(solicitante);
				folga.setObservacao(rs.getString("observacao"));
				folga.setStatusLider(StatusEnum.obterStatus(rs.getLong("statusLider")));
				folga.setStatusGerente(StatusEnum.obterStatus(rs.getLong("statusGerente")));
				return folga;
			}
		});
		return lista;
	}
	
	@Override
	public List<Folga> findFolgasByGerente(Folga folgaFiltro, Long idGerenteLogado) {

		StringBuilder sql = new StringBuilder();
		MapSqlParameterSource params = new MapSqlParameterSource();
		sql.append("SELECT DISTINCT ON (f.id) f.id AS idFolga ,f.id_solicitante AS idSolicitante ,solic.nome AS nomeSolicitante ," +
				"cs.nome AS cargoSolicitante ,f.id_avaliador AS idAprovador ,aprov.nome AS nomeAprovador ,ca.nome AS cargoAprovador ," +
				"f.data_solicitacao AS dataSolicitacao ,f.data_aprovacao AS dataAprovacao ,f.data_folga AS dataFolga ," +
				"f.observacao AS observacao ,f.id_status AS statusFolga ,cs.id AS idCargoSolicitante ,ca.id AS idCargoAprovador " +
				"FROM folga f INNER JOIN usuario solic ON (f.id_solicitante = solic.id) " +
				"INNER JOIN cargo cs ON (solic.id_cargo = cs.id) " +
				"LEFT JOIN usuario aprov ON (f.id_avaliador = aprov.id) " +
				"LEFT JOIN cargo ca ON (aprov.id_cargo = ca.id) " +
				"LEFT JOIN datas_folga df ON (f.id = df.id_folga) ");
		
		sql.append("WHERE proj.id_gerente = :idGerente ");
		params.addValue("idGerente", idGerenteLogado);

		if (folgaFiltro != null) {

			if (folgaFiltro.getStatusGerente() != null) {
				if (folgaFiltro.getStatusGerente().getId() == 2) {
					sql.append("AND (f.id_status_lider = :status || f.id_status_gerente = :status )");
				}else {
					sql.append("AND f.id_status_gerente = :status ");
				}
				sql.append("AND f.id_status_lider = :status ");
				params.addValue("status", folgaFiltro.getStatusGerente());
			}

			if (folgaFiltro.getSolicitante() != null && folgaFiltro.getSolicitante().getId() != null) {
				sql.append("AND f.id_solicitante = :solicitante ");
				params.addValue("solicitante", folgaFiltro.getSolicitante().getId());
			}
			if (folgaFiltro.getGerente() != null && folgaFiltro.getGerente().getNome() != null) {
				sql.append("AND (lider.nome LIKE '%' || :nomeAprovador || '%' || gerente.nome LIKE '%' || :nomeAprovador || '%' ) ");
				params.addValue("nomeAprovador", folgaFiltro.getGerente().getNome());
			}
			if (folgaFiltro.getDataAprovacaoGerente() != null) {
				sql.append("AND f.data_aprovacao = :dataAprovacao ");
				params.addValue("dataAprovacao", folgaFiltro.getDataAprovacaoGerente());
			}
			if (folgaFiltro.getDatasFolga() != null && folgaFiltro.getDatasFolga().get(0) != null && folgaFiltro.getDatasFolga().get(0).getData() != null) {
				sql.append("AND f.data_folga = :dataFolga ");
				params.addValue("dataFolga", folgaFiltro.getDatasFolga().get(0).getData());
			}
			if (folgaFiltro.getDataSolicitacao() != null) {
				sql.append("AND f.data_solicitacao = :dataSolicitacao ");
				params.addValue("dataSolicitacao", folgaFiltro.getDataSolicitacao());
			}
		}
		sql.append("ORDER BY f.id DESC ");

		List<Folga> lista = getNamedParameterJdbcTemplate().query(sql.toString(), params,

		new RowMapper<Folga>() {
			@Override
			public Folga mapRow(ResultSet rs, int idx) throws SQLException {
				Cargo cargoSolicitante = new Cargo();
				Usuario solicitante = new Usuario();
				Usuario lider = new Usuario();
				Usuario gerente = new Usuario();
				Folga folga = new Folga();

				cargoSolicitante.setId(rs.getLong("idCargoSolicitante"));
				cargoSolicitante.setNome(rs.getString("cargoSolicitante"));

				solicitante.setId(rs.getLong("idSolicitante"));
				solicitante.setNome(rs.getString("nomeSolicitante"));
				lider.setId(rs.getLong("idLider"));
				lider.setNome(rs.getString("nomeLider"));
				gerente.setId(rs.getLong("idGerente"));
				gerente.setNome(rs.getString("nomeGerente"));
				

				folga.setId(rs.getLong("idFolga"));
				folga.setDataAprovacaoLider(rs.getDate("dataAprovacaoLider"));
				folga.setDataAprovacaoGerente(rs.getDate("dataAprovacaoGerente"));
				folga.setDataSolicitacao(rs.getDate("dataSolicitacao"));
				folga.setLider(lider);
				folga.setGerente(gerente);
				folga.setSolicitante(solicitante);
				folga.setObservacao(rs.getString("observacao"));
				folga.setStatusLider(StatusEnum.obterStatus(rs.getLong("statusLider")));
				folga.setStatusGerente(StatusEnum.obterStatus(rs.getLong("statusGerente")));
				return folga;
			}
		});
		return lista;
	}

	@Override
	public List<Folga> findByFilterByUsuario(Folga folgaFiltro) {
		StringBuilder sql = new StringBuilder();
		MapSqlParameterSource params = new MapSqlParameterSource();
		sql.append(selectPadrao);
		sql.append("WHERE f.id_solicitante = :idsolicitante ");
		params.addValue("idsolicitante", folgaFiltro.getSolicitante().getId());

		if (folgaFiltro.getStatusGerente() != null) {
			if (folgaFiltro.getStatusGerente().getId() == 2) {
				sql.append("AND (f.id_status_lider = :status || f.id_status_gerente = :status )");
			}else {
				sql.append("AND f.id_status_gerente = :status ");
			}
			sql.append("AND f.id_status_lider = :status ");
			params.addValue("status", folgaFiltro.getStatusGerente());
		}
		if (folgaFiltro.getGerente() != null && folgaFiltro.getGerente().getNome() != null) {
			sql.append("AND (lider.nome LIKE '%' || :nomeAprovador || '%' || gerente.nome LIKE '%' || :nomeAprovador || '%' ) ");
			params.addValue("nomeAprovador", folgaFiltro.getGerente().getNome());
		}
		if (folgaFiltro.getDataAprovacaoGerente() != null) {
			sql.append("AND f.data_aprovacao = :dataAprovacao ");
			params.addValue("dataAprovacao", folgaFiltro.getDataAprovacaoGerente());
		}
		if (folgaFiltro.getDatasFolga() != null && folgaFiltro.getDatasFolga().get(0) != null && folgaFiltro.getDatasFolga().get(0).getData() != null) {
			sql.append("AND f.data_folga = :dataFolga ");
			params.addValue("dataFolga", folgaFiltro.getDatasFolga().get(0).getData());
		}
		if (folgaFiltro.getDataSolicitacao() != null) {
			sql.append("AND f.data_solicitacao = :dataSolicitacao ");
			params.addValue("dataSolicitacao", folgaFiltro.getDataSolicitacao());
		}

		sql.append("ORDER BY f.id DESC ");

		List<Folga> lista = getNamedParameterJdbcTemplate().query(sql.toString(), params,

		new RowMapper<Folga>() {
			@Override
			public Folga mapRow(ResultSet rs, int idx) throws SQLException {
				Cargo cargoSolicitante = new Cargo();
				Usuario solicitante = new Usuario();
				Usuario lider = new Usuario();
				Usuario gerente = new Usuario();
				Folga folga = new Folga();

				cargoSolicitante.setId(rs.getLong("idCargoSolicitante"));
				cargoSolicitante.setNome(rs.getString("cargoSolicitante"));

				solicitante.setId(rs.getLong("idSolicitante"));
				solicitante.setNome(rs.getString("nomeSolicitante"));
				lider.setId(rs.getLong("idLider"));
				lider.setNome(rs.getString("nomeLider"));
				gerente.setId(rs.getLong("idGerente"));
				gerente.setNome(rs.getString("nomeGerente"));
				

				folga.setId(rs.getLong("idFolga"));
				folga.setDataAprovacaoLider(rs.getDate("dataAprovacaoLider"));
				folga.setDataAprovacaoGerente(rs.getDate("dataAprovacaoGerente"));
				folga.setDataSolicitacao(rs.getDate("dataSolicitacao"));
				folga.setLider(lider);
				folga.setGerente(gerente);
				folga.setSolicitante(solicitante);
				folga.setObservacao(rs.getString("observacao"));
				folga.setStatusLider(StatusEnum.obterStatus(rs.getLong("statusLider")));
				folga.setStatusGerente(StatusEnum.obterStatus(rs.getLong("statusGerente")));
				return folga;
			}
		});
		return lista;
	}

	@Override
	public void avaliarFolgaGerente(List<Long> ids, Integer acao) throws RegistroInexistenteException {
		
		// Ação: 1 - Aprovado | 2 - Reprovado | 3 - P endente
		ArrayList<Object[]> params = new ArrayList<Object[]>(ids.size());
		for (Object id : ids) {
			Object[] param = new Object[] { acao, id };
			params.add(param);
		}
		int[] affectedRows = getJdbcTemplate().batchUpdate("UPDATE folga SET id_status_gerente = ?  WHERE id = ?", params);
		for (int rows : affectedRows)
			if (rows == 0) throw new RegistroInexistenteException();
	}
	
	@Override
	public void avaliarFolgaLider(List<Long> ids, Integer acao) throws RegistroInexistenteException {
		
		// Ação: 1 - Aprovado | 2 - Reprovado | 3 - P endente
		ArrayList<Object[]> params = new ArrayList<Object[]>(ids.size());
		for (Object id : ids) {
			Object[] param = new Object[] { acao, id };
			params.add(param);
		}
		int[] affectedRows = getJdbcTemplate().batchUpdate("UPDATE folga SET id_status_lider = ?  WHERE id = ?", params);
		for (int rows : affectedRows)
			if (rows == 0) throw new RegistroInexistenteException();
	}

}
