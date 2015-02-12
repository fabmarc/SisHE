package com.indra.sishe.controller.solicitacao;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;

import com.indra.infra.resource.MessageProvider;
import com.indra.infra.service.exception.ApplicationException;
import com.indra.sishe.entity.Sistema;
import com.indra.sishe.entity.Solicitacao;
import com.indra.sishe.service.SistemaService;

@ViewScoped
@ManagedBean(name = "solicitacaoCad")
public class SolicitacaoCadController extends SolicitacaoController {

	private static final long serialVersionUID = -631053948832685230L;
	/* lista utilizada para preencher o componente select da view de cadastro */
	private List<Sistema> listaSistemas = new ArrayList<Sistema>();

	private Sistema sistemaselecionado = new Sistema();
	private Solicitacao solicitacaoCadastrar = new Solicitacao();
	@Inject
	private SistemaService sistemaService;

	public SolicitacaoCadController() {

	}

	@PostConstruct
	public void init() {
		MessageProvider.setInstance(messageProvider);
		searched = (Boolean) getFlashAttr("searched");

		if (searched == null) searched = false;

		solicitacaoFiltro = (Solicitacao) getFlashAttr("solicitacaoFiltro");

		if (solicitacaoFiltro == null) solicitacaoFiltro = new Solicitacao();

		if (listaSistemas.size() == 0) {
			obterSistemas();
		}
	}

	public String cadastrarSolicitacao() throws ApplicationException {
		try {
			if (solicitacaoService.validarSolicitacao(solicitacaoCadastrar)) {
				this.solicitacaoFiltro = solicitacaoService.save(this.solicitacaoCadastrar);
				putFlashAttr("solicitacaoFiltro", solicitacaoFiltro);
				returnInfoMessage(messageProvider.getMessage("msg.success.registro.cadastrado", "Solicitação"));
				putFlashAttr("searched", searched);
			}

			return irParaConsultarPendentes();
		} catch (ApplicationException e) {
			returnErrorMessage(e.getMessage());
		}
		return null;
	}

	public String cancelar() {
		putFlashAttr("searched", searched);
		putFlashAttr("solicitacaoFiltro", solicitacaoFiltro);
		return "/paginas/solicitacao/consultarSolicitacao.xhtml?faces-redirect=true";
	}
	
	public List<Sistema> obterSistemas() {
		listaSistemas = sistemaService.findAll();
		return listaSistemas;
	}

	public List<Sistema> getListaSistemas() {
		return listaSistemas;
	}

	public void setListaSistemas(List<Sistema> listaSistemas) {
		this.listaSistemas = listaSistemas;
	}

	public Sistema getSistemaSelecionado() {
		return sistemaselecionado;
	}

	public void setSistemaSelecionado(Sistema sistemaselecionado) {
		this.sistemaselecionado = sistemaselecionado;
	}

	public Solicitacao getSolicitacaoCadastrar() {
		return solicitacaoCadastrar;
	}

	public void setSolicitacaoCadastrar(Solicitacao solicitacaoCadastrar) {
		this.solicitacaoCadastrar = solicitacaoCadastrar;
	}

}
