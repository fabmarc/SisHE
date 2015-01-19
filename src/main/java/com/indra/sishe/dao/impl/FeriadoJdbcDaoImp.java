package com.indra.sishe.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
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

import com.indra.infra.dao.exception.RegistroDuplicadoException;
import com.indra.infra.dao.exception.RegistroInexistenteException;
import com.indra.sishe.dao.FeriadoDAO;
import com.indra.sishe.entity.Cidade;
import com.indra.sishe.entity.Estado;
import com.indra.sishe.entity.Feriado;

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
		sql.append("SELECT feriado.id AS id, feriado.data AS data, feriado.nome AS nome,  feriado.tipo AS tipo, estado.id AS estado_id, estado.nome AS estado_nome, estado.sigla AS estado_sigla, cidade.nome AS cidade_nome, id_cidade AS cidade_id, case 	when feriado.id_estado is null and feriado.id_cidade is null then 'Nacional' else case when feriado.id_estado is not null and feriado.id_cidade is null then (select nome from estado where id = feriado.id_estado) else (select nome from cidade where id = feriado.id_cidade)||' ('||(select sigla from estado where id = feriado.id_estado)||')' end end as abrangencia ");
		sql.append("FROM feriado left outer JOIN estado on (estado.id = feriado.id_estado) left outer JOIN cidade on (cidade.id  = feriado.id_cidade) WHERE 1=1 ");

		if (feriado != null && feriado.getNome() != null && !"".equals(feriado.getNome())) {
			sql.append("AND LOWER(feriado.nome) LIKE '%'|| :nomeFeriado || '%' ");
			params.addValue("nomeFeriado", feriado.getNome());
		}

		if (feriado != null && feriado.getData() != null) {
			sql.append("AND feriado.data = :dataFeriado ");
			params.addValue("dataFeriado", feriado.getData());
		}

		if (feriado.getEstado() != null && feriado.getEstado().getId() != null && feriado.getEstado().getId() > 0) {
			sql.append("AND estado.id = :estadoId ");
			params.addValue("estadoId", feriado.getEstado().getId());
		}

		if (feriado.getEstado() != null && feriado.getEstado().getId() == 0) {
			sql.append("AND estado.id IS NULL ");
		}

		sql.append("order by data ");

		List<Feriado> lista = getNamedParameterJdbcTemplate().query(sql.toString(), params,

		new RowMapper<Feriado>() {
			@Override
			public Feriado mapRow(ResultSet rs, int idx) throws SQLException {
				Feriado feriado = new Feriado();
				Estado estado = new Estado();
				Cidade cidade = new Cidade();

				estado.setId(rs.getLong("estado_id"));
				estado.setNome(rs.getString("estado_nome"));
				estado.setSigla(rs.getString("estado_sigla"));

				cidade.setId(rs.getLong("cidade_id"));
				cidade.setNome(rs.getString("cidade_nome"));
				cidade.setEstado(estado);

				feriado.setCidade(cidade);
				feriado.setEstado(estado);
				feriado.setId(rs.getLong("id"));
				feriado.setNome(rs.getString("nome"));
				feriado.setData(rs.getDate("data"));
				feriado.setTipo(rs.getString("tipo").charAt(0));
				feriado.setAbrangencia(rs.getString("abrangencia"));

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
		if (feriado.getCidade() != null) {
			params.addValue("id_cidade", feriado.getCidade().getId());
			params.addValue("id_estado", feriado.getEstado().getId());
		} else if (feriado.getEstado() != null) {
			params.addValue("id_estado", feriado.getEstado().getId());
		}

		Number key = insertFeriado.executeAndReturnKey(params);
		feriado.setId(key.longValue());
		return feriado;
	}

	@Override
	public Feriado update(Feriado feriado) throws RegistroInexistenteException {
		int rows;
		if (feriado.getEstado() == null) { // Feriado Nacional, define Estado e
											// Cidade como NULL
			rows = getJdbcTemplate().update("UPDATE feriado SET nome = ?, data = ?, tipo = ?, id_estado = default, id_cidade = default WHERE id = ?", feriado.getNome(), feriado.getData(),
					feriado.getTipo(), feriado.getId());
		} else if (feriado.getCidade() == null) { // Feriado Estadual, define
													// Cidade como NULL
			rows = getJdbcTemplate().update("UPDATE feriado SET nome = ?, data = ?, tipo = ?, id_estado = ? WHERE id = ?", feriado.getNome(), feriado.getData(), feriado.getTipo(),
					feriado.getEstado().getId(), feriado.getId());
		} else { // Feriado Municipal
			rows = getJdbcTemplate().update("UPDATE feriado SET nome = ?, data = ?, tipo = ?, id_estado = ?, id_cidade = ? WHERE id = ?", feriado.getNome(), feriado.getData(), feriado.getTipo(),
					feriado.getEstado().getId(), feriado.getCidade().getId(), feriado.getId());
		}
		if (rows == 0)
			throw new RegistroInexistenteException();
		return feriado;
	}

	@Override
	public List<Feriado> findAll() {
		StringBuilder sql = new StringBuilder();
		MapSqlParameterSource params = new MapSqlParameterSource();
		sql.append("SELECT feriado.id AS id, feriado.data AS data, feriado.nome AS nome,  feriado.tipo AS tipo, estado.id AS estado_id, estado.nome AS estado_nome, estado.sigla AS estado_sigla, cidade.nome AS cidade_nome, id_cidade AS cidade_id, case 	when feriado.id_estado is null and feriado.id_cidade is null then 'Nacional' else case when feriado.id_estado is not null and feriado.id_cidade is null then (select nome from estado where id = feriado.id_estado) else (select nome from cidade where id = feriado.id_cidade)||' ('||(select sigla from estado where id = feriado.id_estado)||')' end end as abrangencia ");
		sql.append("FROM feriado left outer JOIN estado on (estado.id = feriado.id_estado) left outer JOIN cidade on (cidade.id  = feriado.id_cidade) WHERE 1=1 ");

		sql.append("order by data ");

		List<Feriado> lista = getNamedParameterJdbcTemplate().query(sql.toString(), params,

		new RowMapper<Feriado>() {
			@Override
			public Feriado mapRow(ResultSet rs, int idx) throws SQLException {
				Feriado feriado = new Feriado();
				Estado estado = new Estado();
				Cidade cidade = new Cidade();

				estado.setId(rs.getLong("estado_id"));
				estado.setNome(rs.getString("estado_nome"));
				estado.setSigla(rs.getString("estado_sigla"));

				cidade.setId(rs.getLong("cidade_id"));
				cidade.setNome(rs.getString("cidade_nome"));
				cidade.setEstado(estado);

				feriado.setCidade(cidade);
				feriado.setEstado(estado);
				feriado.setId(rs.getLong("id"));
				feriado.setNome(rs.getString("nome"));
				feriado.setData(rs.getDate("data"));
				feriado.setTipo(rs.getString("tipo").charAt(0));
				feriado.setAbrangencia(rs.getString("abrangencia"));

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
			sql.append("SELECT feriado.id AS id, feriado.data AS data, feriado.nome AS nome,  feriado.tipo AS tipo, estado.id AS estado_id, estado.nome AS estado_nome, estado.sigla AS estado_sigla, cidade.nome AS cidade_nome, id_cidade AS cidade_id, case 	when feriado.id_estado is null and feriado.id_cidade is null then 'Nacional' else case when feriado.id_estado is not null and feriado.id_cidade is null then (select nome from estado where id = feriado.id_estado) else (select nome from cidade where id = feriado.id_cidade)||' ('||(select sigla from estado where id = feriado.id_estado)||')' end end as abrangencia ");
			sql.append("FROM feriado left outer JOIN estado on (estado.id = feriado.id_estado) left outer JOIN cidade on (cidade.id  = feriado.id_cidade) WHERE 1=1 ");
			sql.append("AND feriado.id = :idFeriado ");
			params.addValue("idFeriado", id);

			List<Feriado> lista = getNamedParameterJdbcTemplate().query(sql.toString(), params,
			new RowMapper<Feriado>() {
				@Override
				public Feriado mapRow(ResultSet rs, int idx) throws SQLException {
					Feriado feriado = new Feriado();
					Estado estado = new Estado();
					Cidade cidade = new Cidade();

					estado.setId(rs.getLong("estado_id"));
					estado.setNome(rs.getString("estado_nome"));
					estado.setSigla(rs.getString("estado_sigla"));

					cidade.setId(rs.getLong("cidade_id"));
					cidade.setNome(rs.getString("cidade_nome"));
					cidade.setEstado(estado);

					feriado.setCidade(cidade);
					feriado.setEstado(estado);
					feriado.setId(rs.getLong("id"));
					feriado.setNome(rs.getString("nome"));
					feriado.setData(rs.getDate("data"));
					feriado.setTipo(rs.getString("tipo").charAt(0));
					feriado.setAbrangencia(rs.getString("abrangencia"));

					return feriado;
				}
			});
			if (lista.size() > 0) {
				return lista.get(0);
			} else {
				return null;
			}
		} catch (Exception e) {
			throw new RegistroInexistenteException();
		}
	}

	@Override
	public void remove(Object id) throws RegistroInexistenteException {
		int rows = getJdbcTemplate().update("DELETE FROM feriado WHERE id = ?", id);
		if (rows == 0)
			throw new RegistroInexistenteException();
	}

	@Override
	public EntityManager getEntityManager() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void remove(List<Object> ids) throws RegistroInexistenteException {
		ArrayList<Object[]> params = new ArrayList<Object[]>(ids.size());
		for (Object id : ids) {
			Object[] param = new Object[] { id };
			params.add(param);
		}
		int[] affectedRows = getJdbcTemplate().batchUpdate("DELETE FROM feriado WHERE id = ?", params);
		for (int rows : affectedRows)
			if (rows == 0)
				throw new RegistroInexistenteException();

	}

}
