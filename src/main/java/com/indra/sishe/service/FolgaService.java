package com.indra.sishe.service;

import java.util.List;

import javax.ejb.Local;

import com.indra.infra.service.BaseService;
import com.indra.infra.service.exception.ApplicationException;
import com.indra.sishe.entity.Folga;
import com.indra.sishe.entity.Usuario;

@Local
public interface FolgaService extends BaseService<Folga>{
	
	public List<Folga> findByFilter(Folga folga);
	
	public List<Folga> findFolgaByUsuario(Usuario usuario);
	
	public List<Folga> findFolgasByGerente (Folga folgaFiltro, Long idGerenteLogado);
	
	public void folgasParaRemocao(List<Folga> listaFolgasParaRemocao) throws ApplicationException;
	
	public void avaliarFolga (List<Long> ids, Integer acao) throws ApplicationException;
	
	public void remove(Folga entity) throws ApplicationException;

}
