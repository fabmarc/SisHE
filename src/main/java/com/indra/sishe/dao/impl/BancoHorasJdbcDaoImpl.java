package com.indra.sishe.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcDaoSupport;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import com.indra.infra.dao.exception.DeletarRegistroViolacaoFK;
import com.indra.infra.dao.exception.RegistroDuplicadoException;
import com.indra.infra.dao.exception.RegistroInexistenteException;
import com.indra.sishe.dao.BancoHorasDAO;
import com.indra.sishe.entity.BancoHoras;
import com.indra.sishe.entity.Historico;
import com.indra.sishe.entity.HistoricoDetalhes;
import com.indra.sishe.entity.Periodo;
import com.indra.sishe.entity.Solicitacao;
import com.indra.sishe.entity.Usuario;

@Repository
public class BancoHorasJdbcDaoImpl extends NamedParameterJdbcDaoSupport implements BancoHorasDAO{

	@Autowired
	private DataSource dataSource;

	private SimpleJdbcInsert insertBancoHoras;
	
	private int porcentagemFeriado;
	
	@PostConstruct
	private void init() {
		setDataSource(dataSource);
		insertBancoHoras = new SimpleJdbcInsert(getJdbcTemplate()).withTableName("banco_horas")
				.usingGeneratedKeyColumns("id");
	}
	
	@Override
	public BancoHoras save(BancoHoras entity) throws RegistroDuplicadoException {

		try {
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue("id_usuario", entity.getUsuario().getId());
			params.addValue("saldo", entity.getSaldo());
			Number key = insertBancoHoras.executeAndReturnKey(params);
			entity.setId(key.longValue());
		} catch (DuplicateKeyException e) {
			throw new RegistroDuplicadoException(e.toString());
		}
		return entity;
	}

	@Override
	public BancoHoras update(BancoHoras entity) throws RegistroInexistenteException, RegistroDuplicadoException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<BancoHoras> findAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BancoHoras findById(Object id) throws RegistroInexistenteException {
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
	public List<HistoricoDetalhes> contabilizarHorasBanco(List<Long> idsSolicitacoes) {
		
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
		List<HistoricoDetalhes> historicoDetalhes = new ArrayList<HistoricoDetalhes>();
		
		Calendar calSolicitacao = Calendar.getInstance();
		Calendar calPeriodo = Calendar.getInstance();

		//percorrer todas as solicitações selecionadas.
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
			// Consultar data, hora inicio e hora fim da solicitação atual.
			sql.append("SELECT	solicitacao.data, hora_inicio, hora_final, id_usuario, feriado.data as data_feriado, CASE WHEN FERIADO.DATA IS NOT NULL THEN (SELECT regra.porcentagem_feriado FROM REGRA WHERE REGRA.ID_SINDICATO = SINDICATO.ID AND REGRA.DATA_FIM > SOLICITACAO.DATA AND REGRA.DATA_INICIO <SOLICITACAO.DATA ORDER BY REGRA.DATA_FIM LIMIT 1) END AS porcentagem FROM SOLICITACAO  LEFT JOIN USUARIO ON (USUARIO.ID = SOLICITACAO.ID_USUARIO) LEFT JOIN CIDADE ON (CIDADE.ID = USUARIO.ID_CIDADE) LEFT JOIN ESTADO ON (ESTADO.ID = CIDADE.ID_ESTADO) LEFT JOIN FERIADO ON (((FERIADO.ID_ESTADO = ESTADO.ID AND FERIADO.ID_CIDADE=CIDADE.ID) OR (FERIADO.ID_ESTADO = ESTADO.ID AND FERIADO.ID_CIDADE IS NULL))AND solicitacao.data = feriado.data) LEFT JOIN SINDICATO ON (SINDICATO.ID = USUARIO.ID_SINDICATO) WHERE solicitacao.id = :id ");
			params.addValue("id", id);
			
			//obter uma solicitação.
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

			// obter o dia da semana da solicitação.
			calSolicitacao.setTime(solicitacao.getData());
			diaSolicitado = calSolicitacao.get(Calendar.DAY_OF_WEEK);

			// obtém minutos da solicitação.
			calSolicitacao.setTime(solicitacao.getHoraInicio());
			horaInicioSolicitacao = calSolicitacao.get(Calendar.HOUR_OF_DAY);
			minutoInicioSolicitacao = calSolicitacao.get(Calendar.MINUTE);
			calSolicitacao.setTime(solicitacao.getHoraFinal());
			horaFimSolicitacao = calSolicitacao.get(Calendar.HOUR_OF_DAY);
			minutoFimSolicitacao = calSolicitacao.get(Calendar.MINUTE);
			minutoSolicitacaoInicial = (horaInicioSolicitacao * 60) + minutoInicioSolicitacao;
			minutoSolicitacaoFinal = (horaFimSolicitacao * 60) + minutoFimSolicitacao;

			// se porcentagem do feriado para a regra for menor que zero, é porque não existe feriado
			// para a data da solicitação.
			if (porcentagemFeriado < 0) {
				// consultar os periodos que correspondem a data e hora da
				// solicitação.
				sql = new StringBuilder();
				params = new MapSqlParameterSource();
				//comando SQL para obter dados do periodo.
				sql.append("SELECT dia_semana, hora_inicio, hora_fim, porcentagem from periodo inner join regra on (regra.id = periodo.id_regra) inner join sindicato on (regra.id_sindicato = sindicato.id) inner join usuario on (usuario.id_sindicato = sindicato.id) where usuario.id = :idUsuario and dia_semana = :diaSemana and ((hora_inicio >= :horaInicio and hora_fim <=:horaFim) or (:horaInicio between hora_inicio and hora_fim and hora_fim <=:horaFim)) and :dataSolicitada between regra.data_inicio and regra.data_fim order by hora_inicio asc");
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
				
				//Obter minutos da solicitação a serem adicionados.
				minutoTotal = ((horaFimSolicitacao * 60) + minutoFimSolicitacao) - ((horaInicioSolicitacao * 60) + minutoInicioSolicitacao);
				
				// verificar se existe periodos que correspondem a solicitação.
				if (periodos.size() < 1) {
					//caso não exista nenhum periodo, será adicionado todos os minutos sem cálculo.
					historicoDetalhes.add(new HistoricoDetalhes(minutoTotal, minutoTotal, new Historico(new Solicitacao(id))));
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
						
						//Obter sobra entre o fim do periodo e o fim da solicitação.
						sobra = minutoSolicitacaoFinal - minutoFimPeriodo;
						
						if (sobra > 0) {//se o periodo não atender a todos os minutos da solicitação.
							if (minutoSolicitacaoInicial >= minutoInicioPeriodo) {//se o inicio da solicitação estiver entre o inicio do periodo.  
								//obter os minutos que representam o periodo atual.
								diferenca = minutoFimPeriodo - minutoSolicitacaoInicial;
							} else {
								//obter os minutos que representam o periodo atual.
								diferenca = minutoFimPeriodo - minutoInicioPeriodo;
							}
							//obter minutos que ainda não foram utilizados no calculo.
							minutoTotal = minutoTotal - diferenca;
							//Cálcular os minutos que correspondem ao periodo de acordo com a porcentagem do mesmo.
							historicoDetalhes.add(new HistoricoDetalhes(diferenca, p.getPorcentagem(), (int) (diferenca + (diferenca * (float) p.getPorcentagem() / 100)), new Historico(new Solicitacao(id))));
							minutos = (int) (minutos + (diferenca + (diferenca * ((float) p.getPorcentagem() / 100))));
						} else {//caso o periodo atenda a todos os minutos da solicitação.
							//obter minutos que representam o periodo atual.
							diferenca = minutoSolicitacaoFinal - minutoInicioPeriodo;
							//obter minutos que ainda não foram utilizados no calculo.
							minutoTotal = minutoTotal - diferenca;
							historicoDetalhes.add(new HistoricoDetalhes(diferenca, p.getPorcentagem(), (int) (diferenca + (diferenca * (float) p.getPorcentagem() / 100)), new Historico(new Solicitacao(id))));
							//Cálcular os minutos que correspondem ao periodo de acordo com a porcentagem do mesmo.
							minutos = (int) (minutos + (diferenca + (diferenca * ((float) p.getPorcentagem() / 100))));
						}
					}
				}
				// caso exista algum registro sem porcentagem será adicionado os
				// minutos sem cálculo da porcentagem.
				if (minutoTotal > 0) {
					minutos = minutos + minutoTotal;
					historicoDetalhes.add(new HistoricoDetalhes(minutoTotal, 0, minutoTotal, new Historico(new Solicitacao(id))));
				}
			} else {
				// se for feriado calcular de acordo com a porcentagem definida
				// para o feriado na regra.
				minutoTotal = (int) (horaFimSolicitacao * 60) + minutoFimSolicitacao
						- (horaInicioSolicitacao * 60) + minutoInicioSolicitacao;
				historicoDetalhes.add(new HistoricoDetalhes(diferenca, porcentagemFeriado, (int) (minutoTotal * (float) porcentagemFeriado / 100), new Historico(new Solicitacao(id))));
				
				minutos = (int) (minutoTotal + (minutoTotal * ((float) porcentagemFeriado / 100)));
			}
			//Adicionar novo saldo ao banco de horas.
			getJdbcTemplate()
					.update("UPDATE banco_horas SET saldo = (select saldo from banco_horas where id_usuario= ?) + ? WHERE id_usuario = ?;",
							solicitacao.getUsuario().getId(), minutos, solicitacao.getUsuario().getId());
		}	
		return historicoDetalhes;
	}

}
