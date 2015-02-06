package com.indra.sishe.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
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
import com.indra.sishe.entity.Periodo;
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

	private int porcentagemFeriado;

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
		StringBuilder sql = new StringBuilder();
		MapSqlParameterSource params = new MapSqlParameterSource();
		sql.append("SELECT solicitacao.id AS idSolicitacao, hora_inicio, hora_final, solicitacao.descricao AS descricao, data_aprovacao_lider, data_aprovacao_gerente, data, id_status_lider, id_status_gerente, id_usuario, usuario.nome AS nomeUsuario, id_sistema, sistema.nome AS nomeSistema, id_aprovador_lider, lider.nome AS nomeLider, projeto.nome AS nomeProjeto, projeto.id AS idprojeto, id_aprovador_gerente, gerente.nome AS nomeGerente ");
		sql.append("FROM solicitacao INNER JOIN usuario ON (usuario.id = solicitacao.id_usuario) INNER JOIN sistema ON (sistema.id = solicitacao.id_sistema) LEFT JOIN usuario lider ON (lider.id = solicitacao.id_aprovador_lider) LEFT JOIN usuario gerente ON (gerente.id = solicitacao.id_aprovador_gerente) INNER JOIN projeto ON (projeto.id = sistema.id_projeto) ");

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
	public Solicitacao findById(Object id) throws RegistroInexistenteException {
		// TODO Auto-generated method stub
		return null;
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
			int[] affectedRows = getJdbcTemplate().batchUpdate("DELETE FROM solicitacao WHERE id = ?", params);
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

		return null;

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

		if (status == 1) {// se for para aprovar a solicita��o, deve-se gerar o
							// hist�rico e contabilizar os minutos.
			contabilizarHorasBanco(ids);
			gerarHistorico(ids);
		}

		for (int rows : affectedRows)
			if (rows == 0) throw new RegistroInexistenteException();
	}

	private void gerarHistorico(List<Long> ids) {

		SimpleJdbcInsert insertHistorico;
		insertHistorico = new SimpleJdbcInsert(getJdbcTemplate()).withTableName("historico")
				.usingGeneratedKeyColumns("id");
		MapSqlParameterSource params = new MapSqlParameterSource();
		for (Long id : ids) {
			params.addValue("id_gerente", (Long) UsuarioLogado.getId());
			params.addValue("id_solicitacao", id);
			params.addValue("id_banco_hora", obterIdBanco(id));
			params.addValue("data", new Date());
			insertHistorico.executeAndReturnKey(params);
		}
	}

	private Long obterIdBanco(Long idSolicitacao) {

		StringBuilder sql = new StringBuilder();
		MapSqlParameterSource params = new MapSqlParameterSource();
		sql.append("SELECT banco_horas.id as idBanco FROM banco_horas left join solicitacao on (solicitacao.id_usuario = banco_horas.id_usuario) WHERE 1=1 ");
		sql.append("AND solicitacao.id = :id");
		params.addValue("id", idSolicitacao);

		return getNamedParameterJdbcTemplate().queryForObject(sql.toString(), params, new RowMapper<Long>() {
			@Override
			public Long mapRow(ResultSet rs, int idx) throws SQLException {
				return rs.getLong("idBanco");
			}
		});
	}

	private void contabilizarHorasBanco(List<Long> idsSolicitacoes) {

		StringBuilder sql;
		MapSqlParameterSource params;
		int minutos;
		int minutoTotal;			
		int sobra;
		int diferenca;
		int diaSolicitado;
		int minutoSolicitacaoInicial, minutoSolicitacaoFinal;
		int horaInicioSolicitacao, horaFimSolicitacao;
		int minutoInicioSolicitacao, minutoFimSolicitacao;
		int horaInicioPeriodo, horaFimPeriodo;
		int minutoInicioPeriodo, minutoFimPeriodo;	
		
		Calendar calSolicitacao = Calendar.getInstance();
		Calendar calPeriodo = Calendar.getInstance();

		//percorrer todas as solicita��es selecionadas.
		for (Long id : idsSolicitacoes) {
			porcentagemFeriado = 0;
			minutos = 0;
			minutoSolicitacaoFinal = 0;
			minutoSolicitacaoInicial = 0;
			minutoFimPeriodo = 0;
			minutoInicioPeriodo = 0;
			sobra = 0;
			diferenca = 0;
			minutoTotal = 0;
			sql = new StringBuilder();
			params = new MapSqlParameterSource();
			// Consultar data, hora inicio e hora fim da solicita��o atual.
			sql.append("SELECT	solicitacao.data, hora_inicio, hora_final, id_usuario, feriado.data as data_feriado, CASE WHEN FERIADO.DATA IS NOT NULL THEN (SELECT regra.porcentagem_feriado FROM REGRA WHERE REGRA.ID_SINDICATO = SINDICATO.ID AND REGRA.DATA_FIM > SOLICITACAO.DATA AND REGRA.DATA_INICIO <SOLICITACAO.DATA ORDER BY REGRA.DATA_FIM LIMIT 1) END AS porcentagem FROM SOLICITACAO  LEFT JOIN USUARIO ON (USUARIO.ID = SOLICITACAO.ID_USUARIO) LEFT JOIN CIDADE ON (CIDADE.ID = USUARIO.ID_CIDADE) LEFT JOIN ESTADO ON (ESTADO.ID = CIDADE.ID_ESTADO) LEFT JOIN FERIADO ON (((FERIADO.ID_ESTADO = ESTADO.ID AND FERIADO.ID_CIDADE=CIDADE.ID) OR (FERIADO.ID_ESTADO = ESTADO.ID AND FERIADO.ID_CIDADE IS NULL))AND solicitacao.data = feriado.data) LEFT JOIN SINDICATO ON (SINDICATO.ID = USUARIO.ID_SINDICATO) WHERE solicitacao.id = :id ");
			params.addValue("id", id);
			
			//obter uma solicita��o.
			Solicitacao solicitacao = getNamedParameterJdbcTemplate().queryForObject(sql.toString(), params,
					new RowMapper<Solicitacao>() {
						@Override
						public Solicitacao mapRow(ResultSet rs, int idx) throws SQLException {
							Solicitacao s = new Solicitacao();
							s.setData(rs.getDate("data"));
							s.setHoraInicio(rs.getTime("hora_inicio"));
							s.setHoraFinal(rs.getTime("hora_final"));
							Usuario u = new Usuario(rs.getLong("id_usuario"));
							s.setUsuario(u);
							if (rs.getDate("data_feriado") != null) {
								porcentagemFeriado = rs.getInt("porcentagem");
							} else {
								porcentagemFeriado = -1;
							}
							return s;
						}
					});

			// obter o dia da semana da solicita��o.
			calSolicitacao.setTime(solicitacao.getData());
			diaSolicitado = calSolicitacao.get(Calendar.DAY_OF_WEEK);

			// obt�m minutos da solicita��o.
			calSolicitacao.setTime(solicitacao.getHoraInicio());
			horaInicioSolicitacao = calSolicitacao.get(Calendar.HOUR_OF_DAY);
			minutoInicioSolicitacao = calSolicitacao.get(Calendar.MINUTE);
			calSolicitacao.setTime(solicitacao.getHoraFinal());
			horaFimSolicitacao = calSolicitacao.get(Calendar.HOUR_OF_DAY);
			minutoFimSolicitacao = calSolicitacao.get(Calendar.MINUTE);
			minutoSolicitacaoInicial = (horaInicioSolicitacao * 60) + minutoInicioSolicitacao;
			minutoSolicitacaoFinal = (horaFimSolicitacao * 60) + minutoFimSolicitacao;

			// se porcentagem do feriado para a regra for menor que zero, � porque n�o existe feriado
			// para a data da solicita��o.
			if (porcentagemFeriado < 0) {
				// consultar os periodos que correspondem a data e hora da
				// solicita��o.
				sql = new StringBuilder();
				params = new MapSqlParameterSource();
				//comando SQL para obter dados do periodo.
				sql.append("SELECT dia_semana, hora_inicio, hora_fim, porcentagem from periodo inner join regra on (regra.id = periodo.id_regra) inner join sindicato on (regra.id_sindicato = sindicato.id) inner join usuario on (usuario.id_sindicato = sindicato.id) where usuario.id = :idUsuario and dia_semana = :diaSemana and (hora_inicio >= :horaInicio and hora_fim <=:horaFim) and :dataSolicitada between regra.data_inicio and regra.data_fim order by hora_inicio asc");
				params.addValue("idUsuario", solicitacao.getUsuario().getId());
				params.addValue("diaSemana", diaSolicitado);
				params.addValue("horaInicio", solicitacao.getHoraInicio());
				params.addValue("horaFim", solicitacao.getHoraFinal());
				params.addValue("dataSolicitada", solicitacao.getData());

				//Obter todos os periodos correspondentes.
				List<Periodo> periodos = getNamedParameterJdbcTemplate().query(sql.toString(), params,
						new RowMapper<Periodo>() {
							@Override
							public Periodo mapRow(ResultSet rs, int idx) throws SQLException {

								Periodo p = new Periodo();
								p.setHoraInicio(rs.getTime("hora_inicio"));
								p.setHoraFim(rs.getTime("hora_fim"));
								p.setPorcentagem(rs.getInt("porcentagem"));
								return p;
							}
						});
				
				//Obter minutos da solicita��o a serem adicionados.
				minutoTotal = (int) (horaFimSolicitacao * 60) + minutoFimSolicitacao
						- (horaInicioSolicitacao * 60) + minutoInicioSolicitacao;

				// verificar se existe periodos que correspondem a solicita��o.
				if (periodos.size() < 1) {
					//caso n�o exista nenhum periodo, ser� adicionado todos os minutos sem c�lculo.
					minutos = minutoTotal;
				} else {// Caso existam periodos.
					//percorrer todos os periodos.
					for (Periodo p : periodos) {//Obter dados de um periodo.
						calPeriodo.setTime(p.getHoraInicio());
						horaInicioPeriodo = calPeriodo.get(Calendar.HOUR_OF_DAY);
						minutoInicioPeriodo = calPeriodo.get(Calendar.MINUTE);
						calPeriodo.setTime(p.getHoraFim());
						horaFimPeriodo = calPeriodo.get(Calendar.HOUR_OF_DAY);
						minutoFimPeriodo = calPeriodo.get(Calendar.MINUTE);
						minutoInicioPeriodo = (horaInicioPeriodo * 60) + minutoInicioPeriodo;
						minutoFimPeriodo = (horaFimPeriodo * 60) + minutoFimPeriodo;
						
						//Obter sobra entre o fim do periodo e o fim da solicita��o.
						sobra = minutoSolicitacaoFinal - minutoFimPeriodo;
						
						if (sobra > 0) {//se o periodo n�o atender a todos os minutos da solicita��o.
							if (minutoSolicitacaoInicial >= minutoInicioPeriodo) {//se o inicio da solicita��o estiver entre o inicio do periodo.  
								//obter os minutos que representam o periodo atual.
								diferenca = minutoFimPeriodo - minutoSolicitacaoInicial;
							} else {
								//obter os minutos que representam o periodo atual.
								diferenca = minutoFimPeriodo - minutoInicioPeriodo;
							}
							//obter minutos que ainda n�o foram utilizados no calculo.
							minutoTotal = minutoTotal - diferenca;
							//C�lcular os minutos que correspondem ao periodo de acordo com a porcentagem do mesmo.
							minutos = (int) (minutos + (diferenca + (diferenca * ((float) p.getPorcentagem() / 100))));
						} else {//caso o periodo atenda a todos os minutos da solicita��o.
							//obter minutos que representam o periodo atual.
							diferenca = minutoSolicitacaoFinal - minutoInicioPeriodo;
							//obter minutos que ainda n�o foram utilizados no calculo.
							minutoTotal = minutoTotal - diferenca;
							//C�lcular os minutos que correspondem ao periodo de acordo com a porcentagem do mesmo.
							minutos = (int) (minutos + (diferenca + (diferenca * ((float) p.getPorcentagem() / 100))));
						}
					}
				}
				// caso exista algum registro sem porcentagem ser� adicionado os
				// minutos sem c�lculo da porcentagem.
				if (minutoTotal > 0) {
					minutos = minutos + minutoTotal;
				}
			} else {
				// se for feriado calcular de acordo com a porcentagem definida
				// para o feriado na regra.
				minutoTotal = (int) (horaFimSolicitacao * 60) + minutoFimSolicitacao
						- (horaInicioSolicitacao * 60) + minutoInicioSolicitacao;
				minutos = (int) (minutoTotal + (minutoTotal * ((float) porcentagemFeriado / 100)));
			}
			//Adicionar novo saldo ao banco de horas.
			getJdbcTemplate()
					.update("UPDATE banco_horas SET saldo = (select saldo from banco_horas where id_usuario= ?) + ? WHERE id_usuario = ?;",
							solicitacao.getUsuario().getId(), minutos, solicitacao.getUsuario().getId());
		}
	}

	@Override
	public List<Solicitacao> findByFilterByUsuario(Solicitacao solicitacaoFiltro) {
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
}
