package com.indra.sishe.dao;

import java.util.List;

import com.indra.infra.dao.BaseDAO;
import com.indra.infra.dao.exception.RegistroInexistenteException;
import com.indra.sishe.entity.Folga;
import com.indra.sishe.entity.Usuario;

public interface FolgaDAO extends BaseDAO<Folga> {

	public List<Folga> findByFilter(Folga folga);
	
	public List<Folga> findFolgaByUsuario(Usuario usuario);
	
	public List<Folga> findFolgasByGerente (Folga folgaFiltro, Long idGerenteLogado);
	
	public void avaliarFolga (List<Long> ids, Integer acao) throws RegistroInexistenteException;
	
}
