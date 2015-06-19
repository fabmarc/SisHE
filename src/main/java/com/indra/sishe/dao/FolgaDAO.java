package com.indra.sishe.dao;

import java.util.List;

import com.indra.infra.dao.BaseDAO;
import com.indra.infra.dao.exception.RegistroInexistenteException;
import com.indra.sishe.entity.Folga;

public interface FolgaDAO extends BaseDAO<Folga> {

	public List<Folga> findByFilter(Folga folga);
	
	public List<Folga> findByFilterByUsuario(Folga folga);
	
	public List<Folga> findFolgasByLider (Folga folgaFiltro, Long idGerenteLogado);
	
	public List<Folga> findFolgasByGerente (Folga folgaFiltro, Long idGerenteLogado);
	
	public void avaliarFolgaGerente (List<Long> ids, Integer acao) throws RegistroInexistenteException;
	
	public void avaliarFolgaLider (List<Long> ids, Integer acao) throws RegistroInexistenteException;
	
}
