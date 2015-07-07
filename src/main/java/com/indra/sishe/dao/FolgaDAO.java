package com.indra.sishe.dao;

import java.util.List;

import com.indra.infra.dao.BaseDAO;
import com.indra.infra.dao.exception.RegistroInexistenteException;
import com.indra.sishe.entity.Folga;
import com.indra.sishe.entity.Usuario;

public interface FolgaDAO extends BaseDAO<Folga> {

	public List<Folga> findByFilter(Folga folga);
	
	public List<Folga> findFolgaByUsuario(Usuario usuario);
	
	public List<Folga> findFolgaByUsuario(Folga folga);
	
	public List<Folga> findFolgasByGerente (Folga folgaFiltro);
	
	public List<Folga> findFolgasBylider (Folga folga);
	
	public void avaliarFolga (List<Long> ids, Integer acao) throws RegistroInexistenteException;
	
}
