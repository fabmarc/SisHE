package com.indra.sishe.dao;

import java.util.List;

import com.indra.infra.dao.BaseDAO;
import com.indra.sishe.entity.Regra;

public interface RegraDAO extends BaseDAO<Regra> {

	public List<Regra> findByFilter(Regra regra);
	
}
