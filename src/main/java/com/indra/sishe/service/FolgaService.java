package com.indra.sishe.service;

import java.util.List;

import javax.ejb.Local;

import com.indra.infra.service.BaseService;
import com.indra.infra.service.exception.ApplicationException;
import com.indra.sishe.entity.Folga;

@Local
public interface FolgaService extends BaseService<Folga>{
	
public List<Folga> findByFilter(Folga folga);
	
	public List<Folga> findByFilterByUsuario(Folga folga);
	
	public List<Folga> findFolgasByGerente (Folga folgaFiltro, Long idGerenteLogado);
	
	public Boolean validarFolga(Folga folga) throws ApplicationException;
	
	public void folgasParaRemocao(List<Folga> listaFolgasParaRemocao) throws ApplicationException;
	
	public void avaliarFolga (List<Long> ids, Integer acao) throws ApplicationException;

}
