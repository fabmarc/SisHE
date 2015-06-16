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
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcDaoSupport;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import com.indra.infra.dao.exception.DeletarRegistroViolacaoFK;
import com.indra.infra.dao.exception.RegistroDuplicadoException;
import com.indra.infra.dao.exception.RegistroInexistenteException;
import com.indra.sishe.dao.FeriadoDAO;
import com.indra.sishe.entity.Cidade;
import com.indra.sishe.entity.Feriado;
import com.indra.sishe.entity.Folga;
import com.indra.sishe.enums.EstadoEnum;

@Repository
public class FeriadoJdbcDaoImp extends NamedParameterJdbcDaoSupport implements FeriadoDAO {

	@Autowired
	private DataSource dataSource;

	private SimpleJdbcInsert insertFeriado;

	@PostConstruct
	private void init() {
		setDataSource(dataSource);
		insertFeriado = new SimpleJdbcInsert(getJdbcTemplate()).withTableName("feriado").usingGeneratedKeyColumns("id");
	}

	@Override
	public List<Feriado> findByFilter(Feriado feriado) {

		StringBuilder sql = new StringBuilder();
		MapSqlParameterSource params = new MapSqlParameterSource();
		sql.append("SELECT feriado.id AS id ,feriado.data AS data ,INITCAP(feriado.nome) AS nome ,feriado.tipo AS tipo ," +
				"feriado.id_estado as idEstado ,cidade.nome AS cidade_nome ,id_cidade AS cidade_id " +
				"FROM feriado LEFT OUTER JOIN cidade ON (cidade.id = feriado.id_cidade) " +
				"WHERE 1 = 1 ");

		if (feriado != null && feriado.getNome() != null && !"".equals(feriado.getNome())) {
			sql.append("AND LOWER(feriado.nome) LIKE '%'|| :nomeFeriado || '%' ");
			params.addValue("nomeFeriado", feriado.getNome().toLowerCase());
		}

		if (feriado != null && feriado.getData() != null) {
			sql.append("AND feriado.data = :dataFeriado ");
			params.addValue("dataFeriado", feriado.getData());
		}

		if (feriado.getEstado() != null && feriado.getEstado().getId() != null && feriado.getEstado().getId() > 0) {
			sql.append("AND feriado.id_estado = :estadoId ");
			params.addValue("estadoId", feriado.getEstado().getId());
		}

		if (feriado.getEstado() != null && feriado.getEstado().getId() == 0) {
			sql.append("AND feriado.id_estado IS NULL ");
		}

		sql.append("ORDER BY data desc ");

		List<Feriado> lista = getNamedParameterJdbcTemplate().query(sql.toString(), params,

		new RowMapper<Feriado>() {
			@Override
			public Feriado mapRow(ResultSet rs, int idx) throws SQLException {
				Feriado feriado = new Feriado();
				Cidade cidade = new Cidade();

				cidade.setId(rs.getLong("cidade_id"));
				cidade.setNome(rs.getString("cidade_nome"));

				feriado.setCidade(cidade);
				feriado.setEstado(EstadoEnum.obterEstado(rs.getLong("idEstado")));
				feriado.setId(rs.getLong("id"));
				feriado.setNome(rs.getString("nome"));
				feriado.setData(rs.getDate("data"));
				feriado.setTipo(rs.getString("tipo").charAt(0));

				return feriado;
			}
		});
		return lista;
	}

	@Override
	public Feriado save(Feriado feriado) throws RegistroDuplicadoException {

		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("tipo", feriado.getTipo());
		params.addValue("data", feriado.getData());
		params.addValue("nome", feriado.getNome());
		if (feriado.getEstado() != null && feriado.getEstado().getId() > 0) {
			params.addValue("id_estado", feriado.getEstado().getId());
		}
		if (feriado.getCidade() != null) {
			params.addValue("id_cidade", feriado.getCidade().getId());
		}
		Number key = insertFeriado.executeAndReturnKey(params);
		feriado.setId(key.longValue());
		return feriado;
	}

	@Override
	public Feriado update(Feriado feriado) throws RegistroInexistenteException {

		Long idEstado = null;
		Long idCidade = null;
		if (feriado.getEstado() != null && feriado.getEstado().getId() > 0) {
			idEstado = feriado.getEstado().getId();
		}
		if (feriado.getCidade() != null) {
			idCidade = feriado.getCidade().getId();
		}
		int rows = getJdbcTemplate()
				.update("UPDATE feriado SET nome = INITCAP(?), data = ?, tipo = ?, id_estado = ?, id_cidade = ? WHERE id = ?",
						feriado.getNome(), feriado.getData(), feriado.getTipo(), idEstado, idCidade,
						feriado.getId());

		if (rows == 0) throw new RegistroInexistenteException();
		return feriado;
	}

	@Override
	public List<Feriado> findAll() {

		StringBuilder sql = new StringBuilder();
		MapSqlParameterSource params = new MapSqlParameterSource();
		sql.append("SELECT feriado.id AS id ,feriado.data AS data ,INITCAP(feriado.nome) AS nome ,feriado.tipo AS tipo ," +
				"feriado.id_estado as idEstado ,cidade.nome AS cidade_nome ,id_cidade AS cidade_id " +
				"FROM feriado LEFT OUTER JOIN cidade ON (cidade.id = feriado.id_cidade) ORDER BY data ");

		List<Feriado> lista = getNamedParameterJdbcTemplate().query(sql.toString(), params,

		new RowMapper<Feriado>() {
			@Override
			public Feriado mapRow(ResultSet rs, int idx) throws SQLException {
				Feriado feriado = new Feriado();
				Cidade cidade = new Cidade();

				cidade.setId(rs.getLong("cidade_id"));
				cidade.setNome(rs.getString("cidade_nome"));

				feriado.setCidade(cidade);
				feriado.setEstado(EstadoEnum.obterEstado(rs.getLong("idEstado")));
				feriado.setId(rs.getLong("id"));
				feriado.setNome(rs.getString("nome"));
				feriado.setData(rs.getDate("data"));
				feriado.setTipo(rs.getString("tipo").charAt(0));

				return feriado;
			}
		});
		return lista;
	}

	@Override
	public Feriado findById(Object id) throws RegistroInexistenteException {

		try {
			StringBuilder sql = new StringBuilder();
			MapSqlParameterSource params = new MapSqlParameterSource();
			sql.append("SELECT feriado.id AS id ,feriado.data AS data ,INITCAP(feriado.nome) AS nome ,feriado.tipo AS tipo ," +
					"feriado.id_estado as idEstado ,cidade.nome AS cidade_nome ,id_cidade AS cidade_id " +
					"FROM feriado LEFT OUTER JOIN cidade ON (cidade.id = feriado.id_cidade) " +
					"WHERE feriado.id = :idFeriado ");
			params.addValue("idFeriado", id);

			return getNamedParameterJdbcTemplate().queryForObject(sql.toString(), params,
					new RowMapper<Feriado>() {
						@Override
						public Feriado mapRow(ResultSet rs, int idx) throws SQLException {
							Feriado feriado = new Feriado();
							Cidade cidade = new Cidade();

							cidade.setId(rs.getLong("cidade_id"));
							cidade.setNome(rs.getString("cidade_nome"));

							feriado.setCidade(cidade);
							feriado.setEstado(EstadoEnum.obterEstado(rs.getLong("idEstado")));
							feriado.setId(rs.getLong("id"));
							feriado.setNome(rs.getString("nome"));
							feriado.setData(rs.getDate("data"));
							feriado.setTipo(rs.getString("tipo").charAt(0));
							return feriado;
						}
					});
		} catch (EmptyResultDataAccessException e) {
			throw new RegistroInexistenteException();
		}
	}

	@Override
	public void remove(Object id) throws RegistroInexistenteException {
		int rows = getJdbcTemplate().update("DELETE FROM feriado WHERE id = ?", id);
		if (rows == 0) throw new RegistroInexistenteException();
	}

	@Override
	public EntityManager getEntityManager() {
		return null;
	}

	@Override
	public void remove(List<Object> ids) throws RegistroInexistenteException, DeletarRegistroViolacaoFK  {

		ArrayList<Object[]> params = new ArrayList<Object[]>(ids.size());
		for (Object id : ids) {
			Object[] param = new Object[] { id };
			params.add(param);
		}
		try {
			int[] affectedRows = getJdbcTemplate().batchUpdate("DELETE FROM feriado WHERE id = ?", params);
			for (int rows : affectedRows)
				if (rows == 0) throw new RegistroInexistenteException();
		} catch (DataIntegrityViolationException d) {
			throw new DeletarRegistroViolacaoFK();
		}
	}

	@Override
	public Boolean verificaFeriadoPorData(Folga folga) {
		StringBuilder sql = new StringBuilder();
		MapSqlParameterSource params = new MapSqlParameterSource();
		sql.append("SELECT feriado.data AS data " + "FROM FOLGA LEFT JOIN USUARIO ON (USUARIO.ID = FOLGA.ID_SOLICITANTE) " + "LEFT JOIN CIDADE ON (CIDADE.ID = USUARIO.ID_CIDADE) "
				+ "LEFT JOIN SINDICATO ON (SINDICATO.ID = USUARIO.ID_SINDICATO) " + "LEFT JOIN FERIADO ON ( ( ( FERIADO.ID_ESTADO = SINDICATO.ID_ESTADO AND FERIADO.ID_CIDADE = CIDADE.ID ) "
				+ "OR ( FERIADO.ID_ESTADO = SINDICATO.ID_ESTADO AND FERIADO.ID_CIDADE IS NULL ) OR (FERIADO.ID_ESTADO IS NULL) ) "
				+ "AND ( (folga.data_folga = feriado.data) OR ( TO_CHAR(folga.data_folga, 'MM') = TO_CHAR(feriado.data, 'MM') "
				+ "AND TO_CHAR(folga.data_folga, 'DD') = TO_CHAR(feriado.data, 'DD') AND feriado.tipo = 'F' ) ) ) ");

		sql.append(" WHERE folga.id = :idFolga AND feriado.data is not null ");
		params.addValue("idFolga", folga.getId());

		List<Feriado> lista = getNamedParameterJdbcTemplate().query(sql.toString(), params,

		new RowMapper<Feriado>() {
			@Override
			public Feriado mapRow(ResultSet rs, int idx) throws SQLException {
				Feriado feriado = new Feriado();
				feriado.setData(rs.getDate("data"));
				return feriado;
			}
		});
		if (lista.size() > 0) {
			return true;
		} else {
			return false;
		}

	}

}
