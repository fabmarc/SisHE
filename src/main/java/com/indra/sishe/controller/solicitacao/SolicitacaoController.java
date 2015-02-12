package com.indra.sishe.controller.solicitacao;

import java.io.Serializable;

import javax.inject.Inject;

import com.indra.infra.controller.BaseController;
import com.indra.sishe.entity.Solicitacao;
import com.indra.sishe.service.SolicitacaoService;

public abstract class SolicitacaoController extends BaseController implements Serializable{

	private static final long serialVersionUID = 1100985347243628344L;
	
	@Inject
	protected transient SolicitacaoService solicitacaoService;
	
	protected Solicitacao solicitacaoFiltro = new Solicitacao();
	
	protected Boolean searched;
	
	public String irParaConsultarPendentes() {
		return "/paginas/solicitacao/solicitacoesPendentes.xhtml?faces-redirect=true";
	}
	
	public String irParaConsultar() {
		return "/paginas/solicitacao/consultarSolicitacao.xhtml?faces-redirect=true";
	}
	
	public String irParaConsultarPorUsuario() {
		return "/paginas/solicitacao/consultarSolicitacoes.xhtml?faces-redirect=true";
	}

	public String irParaCadastrar() {
		putFlashAttr("solicitacaoSelecionada", null);
		putFlashAttr("searched", this.searched);
		putFlashAttr("solicitacaoFiltro", this.solicitacaoFiltro);
		return "/paginas/solicitacao/cadastrarSolicitacao.xhtml?faces-redirect=true";
	}

	public String irParaAlterar() {
		return "";
	}

	public String irParaAlterar(Solicitacao solicitacaoSelecionada) {
		putFlashAttr("searched", this.searched);
		putFlashAttr("solicitacaoFiltro", this.solicitacaoFiltro);
		putFlashAttr("solicitacaoSelecionada", solicitacaoSelecionada);
		return irParaAlterar();
	}

	public Solicitacao getSolicitacaoFiltro() {
		return solicitacaoFiltro;
	}

	public void setSolicitacaoFiltro(Solicitacao solicitacaoFiltro) {
		this.solicitacaoFiltro = solicitacaoFiltro;
	}

	public Boolean wasSearched() {
		return searched;
	}

	public void setSearched(Boolean searched) {
		this.searched = searched;
	}
	

}
