package com.indra.sishe.dao;

import java.util.List;

import com.indra.infra.dao.BaseDAO;
import com.indra.sishe.entity.Estado;

public interface EstadoDAO extends BaseDAO<Estado> {

	public List<Estado> pesquisarPorSigla(String sigla);
}
