package com.indra.sishe.service;

import java.util.List;

import javax.ejb.Local;

import com.indra.infra.service.BaseService;
import com.indra.infra.service.exception.ApplicationException;
import com.indra.sishe.entity.Solicitacao;
import com.indra.sishe.entity.Usuario;

@Local
public interface SolicitacaoService extends BaseService<Solicitacao> {

	public List<Solicitacao> findByLider(Usuario lider);

	public List<Solicitacao> findByGerente(Usuario gerente);

	public List<Solicitacao> findByFilter(Solicitacao solicitacaoFiltro);
	
	public List<Solicitacao> findByFilterByUsuario(Solicitacao solicitacaoFiltro);

	public void liderAcaoSolicitacao(List<Long> ids, int status) throws ApplicationException;

	public void gerenteAcaoSolicitacao(List<Long> ids, int status) throws ApplicationException;
	
	void removeSolicitacoes(List<Solicitacao> solicitacoesParaRemover) throws ApplicationException;
	
	public boolean validarSolicitacao(Solicitacao solicitacao)throws ApplicationException;
}