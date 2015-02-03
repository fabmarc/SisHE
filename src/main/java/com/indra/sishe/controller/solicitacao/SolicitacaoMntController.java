package com.indra.sishe.controller.solicitacao;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.primefaces.context.RequestContext;

import com.indra.infra.resource.MessageProvider;
import com.indra.infra.service.exception.ApplicationException;
import com.indra.sishe.entity.Solicitacao;
import com.indra.sishe.entity.Usuario;

@ViewScoped
@ManagedBean(name = "solicitacaoMnt")
public class SolicitacaoMntController extends SolicitacaoController {

	private static final long serialVersionUID = 3699487265700133440L;

	private List<Solicitacao> listaSolicitacoes;

	private List<Solicitacao> solicitacoesSelecionadas;

	@PostConstruct
	public void init() {

		MessageProvider.setInstance(messageProvider);

		searched = (Boolean) getFlashAttr("searched");
		if (searched == null) searched = false;

		solicitacaoFiltro = (Solicitacao) getFlashAttr("solicitacaoFiltro");
		if (solicitacaoFiltro == null) solicitacaoFiltro = new Solicitacao();

		if (!searched) listaSolicitacoes = new ArrayList<Solicitacao>();
		else pesquisar();
	}

	public void pesquisar() {
		listaSolicitacoes = solicitacaoService.findByFilter(solicitacaoFiltro);
		searched = true;
	}

	public void pesquisarPendentes() {
		if (((String) getSessionAttr("usuario_permissoes")).contains("ROLE_GERENTE")) {
			listaSolicitacoes = solicitacaoService.findByGerente(new Usuario((Long) getSessionAttr("usuario_id")));
		} else {
			listaSolicitacoes = solicitacaoService.findByLider(new Usuario((Long) getSessionAttr("usuario_id")));
		}
		searched = true;
	}

	public void beforeAprovarSolicitacao() {
		if (solicitacoesSelecionadas.size() == 0) {
			RequestContext.getCurrentInstance().execute("selectAtleastOne.show()");
		} else {
			RequestContext.getCurrentInstance().execute("confirmAprovacao.show()");
		}
	}
	
	public void beforeReprovarSolicitacao() {
		if (solicitacoesSelecionadas.size() == 0) {
			RequestContext.getCurrentInstance().execute("selectAtleastOne.show()");
		} else {
			RequestContext.getCurrentInstance().execute("confirmReprovacao.show()");
		}
	}

	public void aprovar() {
		int size = solicitacoesSelecionadas.size();
		ArrayList<Long> ids = new ArrayList<Long>(size);
		for (Solicitacao solicitacao : solicitacoesSelecionadas)
			ids.add(solicitacao.getId());
		try {
			solicitacaoService.aprovarSolicitacoes(ids);
			messager.info(messageProvider.getMessage("msg.success.solicitacao.aprovada"));
		} catch (ApplicationException e) {
			messager.error(e.getMessage());
		}
		pesquisar();
	}

	public void reprovar() {
		System.out.println("Reprovar");
		// Implementar;
	}

	public void visualizar() {
		System.out.println("Visualizar");
		// Implementar;
	}

	public List<Solicitacao> getListaSolicitacoes() {
		return listaSolicitacoes;
	}

	public void setListaSolicitacoes(List<Solicitacao> listaSolicitacoes) {
		this.listaSolicitacoes = listaSolicitacoes;
	}

	public List<Solicitacao> getSolicitacoesSelecionadas() {
		return solicitacoesSelecionadas;
	}

	public void setSolicitacoesSelecionadas(List<Solicitacao> solicitacoesSelecionadas) {
		this.solicitacoesSelecionadas = solicitacoesSelecionadas;
	}

}
