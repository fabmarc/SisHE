package com.indra.sishe.dao;

import java.util.List;

import com.indra.infra.dao.BaseDAO;
import com.indra.infra.dao.exception.RegistroInexistenteException;
import com.indra.sishe.entity.Solicitacao;

public interface SolicitacaoDAO extends BaseDAO<Solicitacao> {

	public List<Solicitacao> findByLider(Solicitacao solicitacaoFiltro);

	public List<Solicitacao> findByGerente(Solicitacao solicitacaoFiltro);

	public List<Solicitacao> findByFilter(Solicitacao solicitacaoFiltro);
	
	public List<Solicitacao> findByFilterByUsuario(Solicitacao solicitacaoFiltro);
	
	public List<Solicitacao> findByProjeto(Solicitacao solicitacaoFiltro);

	public void liderAcaoSolicitacao(List<Long> ids, int status) throws RegistroInexistenteException;

	public void gerenteAcaoSolicitacao(List<Long> ids, int status) throws RegistroInexistenteException;

}