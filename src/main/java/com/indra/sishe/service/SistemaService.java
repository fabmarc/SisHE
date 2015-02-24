package com.indra.sishe.service;

import java.util.List;

import javax.ejb.Local;

import com.indra.infra.service.BaseService;
import com.indra.sishe.entity.Sistema;

@Local
public interface SistemaService extends BaseService<Sistema> {

	// FUNÇÃO QUE PESQUISA UM SINDICATO PELO FILTRO INFORMADO
	public List<Sistema> findByFilter(Sistema sistema);
	
	public List<Sistema> findByProjetoByUsuarioLogado(Long idUsuarioLogado);
}
