package com.indra.sishe.controller.solicitacao;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;

import org.primefaces.context.RequestContext;

import com.indra.infra.resource.MessageProvider;
import com.indra.infra.service.exception.ApplicationException;
import com.indra.sishe.controller.usuario.UsuarioLogado;
import com.indra.sishe.entity.HistoricoDetalhes;
import com.indra.sishe.entity.Solicitacao;
import com.indra.sishe.entity.Usuario;
import com.indra.sishe.service.BancoHorasService;
import com.indra.sishe.service.HistoricoService;

@ViewScoped
@ManagedBean(name = "solicitacaoPend")
public class SolicitacaoPendController extends SolicitacaoController {

	private static final long serialVersionUID = 6411818531460354566L;

	private List<Solicitacao> listaSolicitacoes;

	private List<Solicitacao> solicitacoesSelecionadas;

	private Solicitacao solicitacaoDetalhe;

	private String observacao;

	@Inject
	protected transient BancoHorasService bancoHorasService;

	@Inject
	protected transient HistoricoService historicoService;

	@PostConstruct
	public void init() {

		MessageProvider.setInstance(messageProvider);

		searched = (Boolean) getFlashAttr("searched");
		if (searched == null) searched = false;

		solicitacaoFiltro = (Solicitacao) getFlashAttr("solicitacaoFiltro");
		if (solicitacaoFiltro == null) solicitacaoFiltro = new Solicitacao();

		pesquisarPendentes();
	}

	public void pesquisarPendentes() {
		if ((UsuarioLogado.getPermissoes()).contains("ROLE_GERENTE")) {
			setListaSolicitacoes(solicitacaoService.findByGerente(new Usuario(UsuarioLogado.getId())));
		} else {
			setListaSolicitacoes(solicitacaoService.findByLider(new Usuario(UsuarioLogado.getId())));
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
		List<HistoricoDetalhes> detalhes = new ArrayList<HistoricoDetalhes>();
		for (Solicitacao solicitacao : solicitacoesSelecionadas)
			ids.add(solicitacao.getId());
		try {
			if ((UsuarioLogado.getPermissoes()).contains("ROLE_GERENTE")) {
				solicitacaoService.gerenteAcaoSolicitacao(ids, status);
				if (status == 1) {
					detalhes = bancoHorasService.contabilizarHorasBanco(ids);
				}
			} else {
				solicitacaoService.liderAcaoSolicitacao(ids, status);
			}
			historicoService.gerarHistorico(ids, observacao, detalhes);
			observacao = "";
			messager.info(messageProvider.getMessage("msg.success.solicitacao.aprovada"));
		} catch (ApplicationException e) {
			messager.error(e.getMessage());
		}
		pesquisarPendentes();
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

	public String getObservacao() {
		return observacao;
	}

	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}

	public Solicitacao getSolicitacaoDetalhe() {
		return solicitacaoDetalhe;
	}

	public void setSolicitacaoDetalhe(Solicitacao solicitacaoDetalhe) {
		this.solicitacaoDetalhe = solicitacaoDetalhe;
	}

}
