package com.indra.sishe.controller.solicitacao;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.primefaces.context.RequestContext;

import com.indra.infra.resource.MessageProvider;
import com.indra.infra.service.exception.ApplicationException;
import com.indra.sishe.controller.usuario.UsuarioLogado;
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

//		if (!searched) listaSolicitacoes = new ArrayList<Solicitacao>();
//		else 
			pesquisar();
	}

	public void pesquisar() {
		Usuario usuario = new Usuario();
		usuario.setId(UsuarioLogado.getId());
		solicitacaoFiltro.setUsuario(usuario);		
		listaSolicitacoes = solicitacaoService.findByFilter(solicitacaoFiltro);
		searched = true;
	}

	public void pesquisarPendentes() {
		if ((UsuarioLogado.getPermissoes()).contains("ROLE_GERENTE")) {
			listaSolicitacoes = solicitacaoService.findByGerente(new Usuario(UsuarioLogado.getId()));
		} else {
			listaSolicitacoes = solicitacaoService.findByLider(new Usuario(UsuarioLogado.getId()));
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
		acaoAprovarReprovar(1);
	}

	public void reprovar() {
		acaoAprovarReprovar(2);
	}

	private void acaoAprovarReprovar(int status) {

		int size = solicitacoesSelecionadas.size();
		ArrayList<Long> ids = new ArrayList<Long>(size);
		for (Solicitacao solicitacao : solicitacoesSelecionadas)
			ids.add(solicitacao.getId());
		try {
			if ((UsuarioLogado.getPermissoes()).contains("ROLE_GERENTE")) {
				solicitacaoService.gerenteAcaoSolicitacao(ids, status);
			} else {
				solicitacaoService.liderAcaoSolicitacao(ids, status);
			}
			messager.info(messageProvider.getMessage("msg.success.solicitacao.aprovada"));
		} catch (ApplicationException e) {
			messager.error(e.getMessage());
		}
		pesquisarPendentes();
	}

	public void visualizar() {
		System.out.println("Visualizar");
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