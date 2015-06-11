package com.indra.sishe.dao;

import java.util.List;

import com.indra.infra.dao.BaseDAO;
import com.indra.sishe.entity.Folga;
import com.indra.sishe.entity.Usuario;

public interface FolgaDAO extends BaseDAO<Folga> {

	public List<Folga> findByFilter(Folga folga);
	
	public List<Folga> findByFilterByUsuario(Folga folga);
	
	public List<Folga> findFolgasByGerente (Usuario usuario, Integer tipoFiltro);
	
}
