package com.indra.sishe.dao;

import java.util.List;

import com.indra.infra.dao.BaseDAO;
import com.indra.infra.dao.exception.DeletarRegistroViolacaoFK;
import com.indra.infra.dao.exception.RegistroInexistenteException;
import com.indra.sishe.entity.DatasFolga;

public interface DatasFolgaDAO extends BaseDAO<DatasFolga>{
	
	public List<DatasFolga> findDatasBySolicitacaoFolga(Long idFolga);
	
	public void removeTodasDatasPorFolga(Long idFolga) throws DeletarRegistroViolacaoFK ;
	
	public void insereTodasDatasPorFolga(List<DatasFolga> listaDatas);
	
}
