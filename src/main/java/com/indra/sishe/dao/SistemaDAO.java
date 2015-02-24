package com.indra.sishe.dao;

import java.util.List;

import com.indra.infra.dao.BaseDAO;
import com.indra.sishe.entity.Sistema;

public interface SistemaDAO extends BaseDAO<Sistema> {

	// FUNÇÃO QUE PESQUISA UM SINDICATO PELO FILTRO INFORMADO
		public List<Sistema> findByFilter(Sistema sistema);
		
		public List<Sistema> findByProjetoByUsuarioLogado(Long idUsuarioLogado);
		
}
