package com.indra.sishe.dao;

import java.util.List;

import com.indra.infra.dao.BaseDAO;
import com.indra.infra.dao.exception.RegistroInexistenteException;
import com.indra.infra.service.exception.ApplicationException;
import com.indra.sishe.entity.Sindicato;

public interface SindicatoDAO extends BaseDAO<Sindicato> {

	// FUNÇÃO QUE PESQUISA UM SINDICATO PELO ESTADO DE ORIGEM E RETORNA UMA
	// LISTA
	public List<Sindicato> pesquisarPorEstado(Sindicato sindicato);	
	
	// FUNÇÃO QUE PESQUISA UM SINDICATO PELO FILTRO INFORMADO
	public List<Sindicato> findByFilter(Sindicato sindicato);

	void remove(List<Object> ids) throws RegistroInexistenteException;
}
