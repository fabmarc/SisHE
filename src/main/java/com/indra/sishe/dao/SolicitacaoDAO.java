package com.indra.sishe.dao;

import java.util.List;

import com.indra.infra.dao.BaseDAO;
import com.indra.sishe.entity.Solicitacao;
import com.indra.sishe.entity.Usuario;

public interface SolicitacaoDAO extends BaseDAO<Solicitacao> {
	
	public List<Solicitacao> findByLider(Usuario lider);
	
	public List<Solicitacao> findByGerente(Usuario gerente);
	
	public List<Solicitacao> findByFilter(Solicitacao solicitacaoFiltro);

}
