package com.indra.sishe.dao;

import java.util.List;

import com.indra.infra.dao.BaseDAO;
import com.indra.sishe.entity.Cidade;
import com.indra.sishe.entity.Estado;
import com.indra.sishe.enums.EstadoEnum;

public interface EstadoDAO extends BaseDAO<Estado> {

	List<Estado> pesquisarPorSigla(String sigla);
	
	EstadoEnum findByCidade(Cidade cidade);
	
}
