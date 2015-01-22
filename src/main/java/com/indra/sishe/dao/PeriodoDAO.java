package com.indra.sishe.dao;

import java.util.List;

import com.indra.infra.dao.BaseDAO;
import com.indra.sishe.entity.Periodo;
import com.indra.sishe.entity.Regra;

public interface PeriodoDAO extends BaseDAO<Periodo> {

	public List<Periodo> findByFilter(Periodo entity);
	
	public List<Periodo> findByRegra(Regra entity);
}
