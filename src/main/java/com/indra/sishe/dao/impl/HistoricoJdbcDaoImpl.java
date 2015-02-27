package com.indra.sishe.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
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
import com.indra.sishe.dao.HistoricoDAO;
import com.indra.sishe.entity.DadosRelatorio;
import com.indra.sishe.entity.Historico;
import com.indra.sishe.entity.HistoricoDetalhes;
import com.indra.sishe.entity.Usuario;

@Repository
public class HistoricoJdbcDaoImpl extends NamedParameterJdbcDaoSupport implements HistoricoDAO {

	@Autowired
	private DataSource dataSource;

	private SimpleJdbcInsert insertHistorico;

	@PostConstruct
	private void init() {
		setDataSource(dataSource);
		insertHistorico = new SimpleJdbcInsert(getJdbcTemplate()).withTableName("historico")
				.usingGeneratedKeyColumns("id");
	}

	@Override
	public Historico save(Historico entity) throws RegistroDuplicadoException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Historico update(Historico entity) throws RegistroInexistenteException, RegistroDuplicadoException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Historico> findAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Historico findById(Object id) throws RegistroInexistenteException {
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
	public void gerarHistorico(List<Long> ids, String descricao, List<HistoricoDetalhes> detalhes) {
		Number key;
		MapSqlParameterSource params = new MapSqlParameterSource();
		for (Long id : ids) {
			if (UsuarioLogado.getPermissoes().contains("ROLE_GERENTE")) {
				params.addValue("id_gerente", (Long) UsuarioLogado.getId());
			}
			params.addValue("id_solicitacao", id);
			params.addValue("id_banco_hora", obterIdBanco(id));
			params.addValue("data", new Date());
			params.addValue("descricao", descricao);
			key = insertHistorico.executeAndReturnKey(params);
			for (HistoricoDetalhes d : detalhes) {
				if (d.getHistorico().getSolicitacao().getId() == id) {
					gerarDetalhes(key.longValue(), d.getMinutos(), d.getPorcentagem(), d.getValor());
				}
			}
		}
	}

	private void gerarDetalhes(Long idHistorico, Integer minutos, Integer porcentagem, Integer valor) {
		SimpleJdbcInsert insertHistoricoDetalhes = new SimpleJdbcInsert(getJdbcTemplate()).withTableName(
				"historico_detalhes").usingGeneratedKeyColumns("id");
		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("id_historico", idHistorico);
		params.addValue("minutos", minutos);
		params.addValue("porcentagem", porcentagem);
		params.addValue("valor", valor);
		insertHistoricoDetalhes.executeAndReturnKey(params);
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

	public List<DadosRelatorio> gerarRelatorio(String mes, String ano, Usuario entity) {
		StringBuilder sql = new StringBuilder();
		MapSqlParameterSource params = new MapSqlParameterSource();
		sql.append("select historico_detalhes.id, solicitacao.id as idSolicitacao, TO_CHAR(solicitacao.data, 'DD/MM/YYYY') as dataSolicitacao, historico.data, id_historico as idHistorico, TO_CHAR(solicitacao.data, 'MM') , solicitacao.hora_inicio, solicitacao.hora_final, minutos, porcentagem, valor, (SELECT SUM(VALOR) FROM historico_detalhes WHERE HISTORICO.ID = historico_detalhes.ID_HISTORICO AND historico.id_solicitacao = solicitacao.id LIMIT 1) as total from  historico_detalhes inner join historico on (historico.id = historico_detalhes.id_historico) inner join solicitacao on (historico.id_solicitacao = solicitacao.id) inner join usuario on (usuario.id = solicitacao.id_usuario) where usuario.id = :idUsuario ");
		params.addValue("idUsuario", entity.getId());
		if (mes != null && !mes.isEmpty()) {
			sql.append(" and TO_CHAR(solicitacao.data, 'MM') = :mes ");
			params.addValue("mes", mes);
		}
		if (ano != null && !ano.isEmpty()) {
			sql.append(" and TO_CHAR(solicitacao.data, 'YYYY') = :ano ");
			params.addValue("ano", ano);
		}
		sql.append(" order by solicitacao.id, historico_detalhes.id ");

		List<DadosRelatorio> dados = getNamedParameterJdbcTemplate().query(sql.toString(), params,
				new RowMapper<DadosRelatorio>() {
					@Override
					public DadosRelatorio mapRow(ResultSet rs, int idx) throws SQLException {
						DadosRelatorio dado = new DadosRelatorio();

						dado.setIdSolicitacao(rs.getInt("idSolicitacao"));
						dado.setDataSolicitacao(rs.getString("dataSolicitacao"));
						dado.setHoraInicioSolicitacao(rs.getString("hora_inicio"));
						dado.setHoraFimSolicitacao(rs.getString("hora_final"));
						dado.setMinutos(rs.getString("minutos"));
						dado.setPorcentagem(rs.getString("porcentagem"));
						dado.setValor(rs.getString("valor"));
						dado.setTotal(rs.getString("total"));

						return dado;
					}
				});
		return dados;
	}

}
