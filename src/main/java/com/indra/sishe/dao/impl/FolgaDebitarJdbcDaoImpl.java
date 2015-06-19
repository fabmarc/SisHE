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
import com.indra.sishe.dao.FolgaDebitarDAO;
import com.indra.sishe.entity.BancoHoras;
import com.indra.sishe.entity.Cargo;
import com.indra.sishe.entity.DatasFolga;
import com.indra.sishe.entity.Folga;
import com.indra.sishe.entity.FolgaDebitar;
import com.indra.sishe.entity.Usuario;
import com.indra.sishe.enums.StatusEnum;

@Repository
public class FolgaDebitarJdbcDaoImpl extends NamedParameterJdbcDaoSupport implements FolgaDebitarDAO {
	
	@Autowired
	private DataSource dataSource;

	private SimpleJdbcInsert insertFolgaDebitar;
	
	@PostConstruct
	private void init(){
		 insertFolgaDebitar = new SimpleJdbcInsert(getJdbcTemplate()).withTableName("folga_debitar").usingGeneratedKeyColumns("id");
	}

	@Override
	public FolgaDebitar save(FolgaDebitar entity) throws RegistroDuplicadoException {
		try {
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue("id_datas_folga", entity.getDatasFolga().getId());
			params.addValue("id_banco_horas", entity.getBancoHoras().getId());

			Number key = insertFolgaDebitar.executeAndReturnKey(params);
			entity.setId(key.longValue());
		} catch (DuplicateKeyException e) {
			throw new RegistroDuplicadoException(e.toString());
		}

		return entity;
	}

	@Override
	public FolgaDebitar update(FolgaDebitar entity) throws RegistroInexistenteException, RegistroDuplicadoException {
		return null;
	}

	@Override
	public List<FolgaDebitar> findAll() {
		return null;
	}

	@Override
	public FolgaDebitar findById(Object id) throws RegistroInexistenteException {
		try {
			StringBuilder sql = new StringBuilder();
			MapSqlParameterSource params = new MapSqlParameterSource();
			sql.append("SELECT df.id AS datasFolgaId , df.id_folga AS datasFolgaIdFolga , df.data AS datasFolgaData , " +
					"bh.id AS bancoHorasId , fd.id AS folgaDebitarId , fd.flg_contabilizado AS contabilizado  " +
					"FROM teste_folga_debitar fd " +
					"INNER JOIN datas_folga df ON (fd.id_datas_folga = df.id) " +
					"INNER JOIN banco_horas bh ON (fd.id_banco_horas = bh.id) ");
			sql.append("WHERE f.id = :idFolgaDebitar ");
			params.addValue("idFolgaDebitar", id);

			return getNamedParameterJdbcTemplate().queryForObject(sql.toString(), params, new RowMapper<FolgaDebitar>() {
				@Override
				public FolgaDebitar mapRow(ResultSet rs, int idx) throws SQLException {
					BancoHoras bancoHoras = new BancoHoras();
					bancoHoras.setId(rs.getLong("bancoHorasId"));
					
					DatasFolga datasFolga = new DatasFolga();
					datasFolga.setId(rs.getLong("datasFolgaId"));
					datasFolga.setData(rs.getDate("datasFolgaData"));
					Folga folga = new Folga();
					folga.setId(rs.getLong("datasFolgaIdFolga"));
					datasFolga.setFolga(folga);
					
					FolgaDebitar folgaDebitar = new FolgaDebitar();
					folgaDebitar.setId(rs.getLong("folgaDebitarId"));
					folgaDebitar.setDatasFolga(datasFolga);
					folgaDebitar.setBancoHoras(bancoHoras);
					folgaDebitar.setFlgContabilizado(rs.getBoolean("contabilizado"));
					return folgaDebitar;
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
			int[] affectedRows = getJdbcTemplate().batchUpdate("DELETE FROM folga_debitar WHERE id = ? AND flg_contabilizado = FALSE ", params);
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
