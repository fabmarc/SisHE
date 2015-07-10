package com.indra.sishe.dao;

import java.util.List;


import com.indra.infra.dao.BaseDAO;
import com.indra.sishe.entity.Feriado;

public interface FeriadoDAO extends BaseDAO<Feriado> {

	public List<Feriado> findByFilter(Feriado entity);
	
}
