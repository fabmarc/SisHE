package com.indra.sishe.controller.solicitacao;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.primefaces.context.RequestContext;

import com.indra.infra.resource.MessageProvider;
import com.indra.sishe.controller.usuario.UsuarioLogado;
import com.indra.sishe.entity.Solicitacao;
import com.indra.sishe.entity.Usuario;

@ViewScoped
@ManagedBean(name = "solicitacaoMnt")
public class SolicitacaoMntController extends SolicitacaoController {

	private static final long serialVersionUID = 3699487265700133440L;

	private List<Solicitacao> listaSolicitacoes;

	private List<Solicitacao> solicitacoesSelecionadas;

	private Solicitacao solicitacaoDetalhe;

	@PostConstruct
	public void init() {

		MessageProvider.setInstance(messageProvider);

		searched = (Boolean) getFlashAttr("searched");
		if (searched == null) searched = false;

		solicitacaoFiltro = (Solicitacao) getFlashAttr("solicitacaoFiltro");
		if (solicitacaoFiltro == null) solicitacaoFiltro = new Solicitacao();

		// if (!searched) listaSolicitacoes = new ArrayList<Solicitacao>(); else
		pesquisarPorUsuarioLogado();
	}

	public void pesquisarPorUsuarioLogado() {
		Usuario usuario = new Usuario();
		usuario.setId(UsuarioLogado.getId());
		solicitacaoFiltro.setUsuario(usuario);
		listaSolicitacoes = solicitacaoService.findByFilterByUsuario(solicitacaoFiltro);
		searched = true;
	}

	public void beforeRemoverSolicitacao() {
		if (solicitacoesSelecionadas.size() == 0) {
			RequestContext.getCurrentInstance().execute("selectAtleastOne.show()");
		} else {
			RequestContext.getCurrentInstance().execute("confirmExclusao.show()");
		}
	}

	public String remove() {

		// for (Solicitacao solicitacao : solicitacoesSelecionadas){
		// if (solicitacao.getLider().getNome() != null) {
		// // throw new
		// ApplicationException("msg.error.excluir.solicitacao.avaliada");
		// messager.error(messageProvider.getMessage("msg.error.excluir.solicitacao.avaliada"));
		// }
		// }

		int size = solicitacoesSelecionadas.size();
		ArrayList<Solicitacao> solicitacoesParaRemover = new ArrayList<Solicitacao>(size);
		for (Solicitacao solicitacao : solicitacoesSelecionadas) {
			solicitacoesParaRemover.add(solicitacao);
		}
		try {
			solicitacaoService.removeSolicitacoes(solicitacoesParaRemover);
			messager.info(messageProvider.getMessage("msg.success.registro.excluido", "Solicitação"));
		} catch (Exception e) {
			messager.error(e.getMessage());
		}
		pesquisarPorUsuarioLogado();
		return irParaConsultarPorUsuario();
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

	public Solicitacao getSolicitacaoDetalhe() {
		return solicitacaoDetalhe;
	}

	public void setSolicitacaoDetalhe(Solicitacao solicitacaoDetalhe) {
		this.solicitacaoDetalhe = solicitacaoDetalhe;
	}

}
