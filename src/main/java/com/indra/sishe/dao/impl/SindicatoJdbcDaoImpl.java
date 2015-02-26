package com.indra.sishe.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcDaoSupport;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import com.indra.infra.dao.exception.DeletarRegistroViolacaoFK;
import com.indra.infra.dao.exception.RegistroDuplicadoException;
import com.indra.infra.dao.exception.RegistroInexistenteException;
import com.indra.sishe.dao.SindicatoDAO;
import com.indra.sishe.entity.Estado;
import com.indra.sishe.entity.Sindicato;
import com.indra.sishe.enums.EstadoEnum;

@Repository
public class SindicatoJdbcDaoImpl extends NamedParameterJdbcDaoSupport
		implements SindicatoDAO {

	@Autowired
	private DataSource dataSource;

	private SimpleJdbcInsert insertSindicato;

	@PostConstruct
	private void init() {
		setDataSource(dataSource);
		insertSindicato = new SimpleJdbcInsert(getJdbcTemplate())
				.withTableName("sindicato").usingGeneratedKeyColumns("id");

	}

	@Override
	public List<Sindicato> findByFilter(Sindicato sindicato) {

		StringBuilder sql = new StringBuilder();
		MapSqlParameterSource params = new MapSqlParameterSource();

		sql.append("SELECT s.id AS idSindicato , s.descricao, e.sigla, e.id AS idEstado, e.nome as nome, s.periodo_acerto, s.dias_antecedencia ,"
							+" limite_positivo, limite_negativo ");
		sql.append("FROM estado e INNER JOIN sindicato s ON e.id = s.id_estado ");

		if (sindicato != null && sindicato.getDescricao() != null
				&& !sindicato.getDescricao().isEmpty()) {
			sql.append("AND UPPER(s.descricao) LIKE '%' || :descricao || '%'");
			params.addValue("descricao", sindicato.getDescricao().toUpperCase());
		}

		if (sindicato != null && sindicato.getEstado() != null
				&& !sindicato.getEstado().getNome().isEmpty()) {
			sql.append("AND e.nome = :nome ");
			params.addValue("nome", sindicato.getEstado().getNome());
		}

		List<Sindicato> lista = getNamedParameterJdbcTemplate().query(
				sql.toString(), params,

				new RowMapper<Sindicato>() {
					@Override
					public Sindicato mapRow(ResultSet rs, int idx)
							throws SQLException {

						Sindicato sind = new Sindicato();
						Estado estado = new Estado();
						estado.setSigla(rs.getString("sigla"));
						estado.setNome(rs.getString("nome"));
						estado.setId(rs.getLong("idEstado"));
						sind.setEstado(EstadoEnum.obterEstado(rs.getLong("idEstado")));

						sind.setId(rs.getLong("idSindicato"));
						sind.setDescricao(rs.getString("descricao"));
						sind.setPeriodoAcerto(rs.getInt("periodo_acerto"));
						sind.setLimPositivo((rs.getInt("limite_positivo") / 60));
						sind.setLimNegativo((rs.getInt("limite_negativo") / 60));
						sind.setDiasAntecedencia(rs.getInt("dias_antecedencia"));
						return sind;
					}
				});

		return lista;
	}

	@Override
	public Sindicato save(Sindicato entity) throws RegistroDuplicadoException {

		try {		
			
			Calendar hj = Calendar.getInstance();
			entity.setUltimoAcerto(hj);
			
			MapSqlParameterSource parms = new MapSqlParameterSource();
			parms.addValue("id_estado", entity.getEstado().getId());
			parms.addValue("descricao", entity.getDescricao());			
		
			parms.addValue("limite_positivo", (entity.getLimPositivo() * 60)); // transforma de hora para minuto 	
			parms.addValue("limite_negativo", (entity.getLimNegativo() * 60)); // transforma de hora para minuto 	
			parms.addValue("periodo_acerto", (entity.getPeriodoAcerto() ));	
			parms.addValue("dias_antecedencia", entity.getDiasAntecedencia());
			parms.addValue("ultimo_acerto", entity.getUltimoAcerto()); // no cadastro do sindicato, insere a data do dia como a data do último acerto 
																	  // para realizar o calculo para o envio da notificação
			
			Number key = insertSindicato.executeAndReturnKey(parms);
			entity.setId(key.longValue());
		} catch (DuplicateKeyException e) {
			throw new RegistroDuplicadoException();
		}

		return entity;
	}

	@Override
	public Sindicato update(Sindicato entity)
			throws RegistroInexistenteException {
		int rows = getJdbcTemplate().update(
				"UPDATE sindicato SET descricao = ?, id_estado = ? , limite_positivo = (? * 60), limite_negativo = (? * 60), periodo_acerto = ?"
						+ "WHERE id = ?", entity.getDescricao(),
				entity.getEstado().getId(),entity.getLimPositivo(),entity.getLimNegativo(),
				entity.getPeriodoAcerto(), entity.getId() );
		if (rows == 0)
			throw new RegistroInexistenteException();
		return entity;
	}

	@Override
	public List<Sindicato> findAll() {
		return getJdbcTemplate().query(
				"SELECT id,id_estado,descricao, periodo_acerto , " 
				 + "dias_antecedencia,(limite_positivo / 60) as limite_positivo,"
				 + "(limite_negativo / 60)limite_negativo " + " FROM sindicato",
				new BeanPropertyRowMapper<Sindicato>(Sindicato.class));
	}

	@Override
	public Sindicato findById(Object id) throws RegistroInexistenteException {
		try {
			return getJdbcTemplate()
					.queryForObject(
							"SELECT s.id AS idSindicato, s.descricao as descricao,"
							+"s.periodo_acerto, s.dias_antecedencia ,"
							+" limite_positivo, limite_negativo,"
							+ "e.id AS idEstado, e.nome as nome FROM estado e INNER JOIN sindicato s ON e.id = s.id_estado "
							+ "WHERE s.id = ?  ", new Object[] { id },
							new RowMapper<Sindicato>() {

								@Override
								public Sindicato mapRow(ResultSet rs, int idx)
										throws SQLException {									
									Sindicato sind = new Sindicato();				
									
									sind.setId(rs.getLong("idSindicato"));
									sind.setDescricao(rs.getString("descricao"));
									sind.setEstado(EstadoEnum.obterEstado(rs.getLong("idEstado")));	
									sind.setPeriodoAcerto(rs.getInt("periodo_acerto"));
									sind.setLimPositivo((rs.getInt("limite_positivo") / 60));
									sind.setLimNegativo((rs.getInt("limite_negativo") / 60));
									sind.setDiasAntecedencia(rs.getInt("dias_antecedencia"));
									return sind;
								}

							});

		} catch (EmptyResultDataAccessException e) {
			throw new RegistroInexistenteException();
		}
	}

	@Override
	public void remove(Object id) throws RegistroInexistenteException,
			DeletarRegistroViolacaoFK {
		// TODO Auto-generated method stub
		try {
			int rows = getJdbcTemplate().update(
					"DELETE FROM sindicato WHERE id = ?", id);
			if (rows == 0)
				throw new RegistroInexistenteException();
		} catch (DataIntegrityViolationException d) {
			throw new DeletarRegistroViolacaoFK();
		}

	}

	@Override
	public EntityManager getEntityManager() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void remove(List<Object> ids) throws RegistroInexistenteException,
			DeletarRegistroViolacaoFK {
		// TODO Auto-generated method stub
		ArrayList<Object[]> params = new ArrayList<Object[]>(ids.size());
		for (Object id : ids) {
			Object[] param = new Object[] { id };
			params.add(param);
		}
		try {
			int[] affectedRows = getJdbcTemplate().batchUpdate(
					"DELETE FROM sindicato WHERE id = ?", params);
			for (int rows : affectedRows)
				if (rows == 0)
					throw new RegistroInexistenteException();

		} catch (DataIntegrityViolationException d) {

			throw new DeletarRegistroViolacaoFK();

		}
	}

}
