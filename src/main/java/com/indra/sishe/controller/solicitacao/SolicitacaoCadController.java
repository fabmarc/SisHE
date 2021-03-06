package com.indra.sishe.controller.solicitacao;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;

import com.indra.infra.resource.MessageProvider;
import com.indra.infra.service.exception.ApplicationException;
import com.indra.sishe.controller.usuario.UsuarioLogado;
import com.indra.sishe.entity.HistoricoDetalhes;
import com.indra.sishe.entity.Sistema;
import com.indra.sishe.entity.Solicitacao;
import com.indra.sishe.service.BancoHorasService;
import com.indra.sishe.service.HistoricoService;

@ViewScoped
@ManagedBean(name = "solicitacaoCad")
public class SolicitacaoCadController extends SolicitacaoController {

	private static final long serialVersionUID = -631053948832685230L;

	private Sistema sistemaselecionado = new Sistema();
	
	private Solicitacao solicitacaoCadastrar = new Solicitacao();
	
	private Solicitacao solicitacaoParaHistorico = new Solicitacao();
	
	@Inject
	private BancoHorasService bancoHorasService;
	
	@Inject
	private HistoricoService historicoService;
	
	public SolicitacaoCadController() {
	}

	@PostConstruct
	public void init() {
		MessageProvider.setInstance(messageProvider);
		searched = (Boolean) getFlashAttr("searched");

		if (searched == null) searched = false;

		solicitacaoFiltro = (Solicitacao) getFlashAttr("solicitacaoFiltro");

		if (solicitacaoFiltro == null) solicitacaoFiltro = new Solicitacao();

		setListaSistemas(obterSistemas());
	}

	public String cadastrarSolicitacao() throws ApplicationException, ParseException {
		
		try {
			if (solicitacaoService.validarSolicitacao(solicitacaoCadastrar)) {
				this.solicitacaoParaHistorico = solicitacaoService.save(this.solicitacaoCadastrar);
				if (UsuarioLogado.verificarPermissao("ROLE_GERENTE")) {
					List<Long> id = new ArrayList<Long>();
					id.add(solicitacaoParaHistorico.getId());
					solicitacaoService.gerenteAcaoSolicitacao(id, 1);
					List<HistoricoDetalhes> relatorio = new ArrayList<HistoricoDetalhes>();
					relatorio =  bancoHorasService.contabilizarHorasBanco(id);
					historicoService.gerarHistorico(id, "", relatorio);					
				} 
				
				if (UsuarioLogado.verificarPermissao("ROLE_LIDER")) {
					List<Long> id = new ArrayList<Long>();
					id.add(solicitacaoParaHistorico.getId());
					solicitacaoService.liderAcaoSolicitacao(id, 1);
					List<HistoricoDetalhes> relatorio = new ArrayList<HistoricoDetalhes>();					
					historicoService.gerarHistorico(id, "", relatorio);
				}
				
				putFlashAttr("solicitacaoFiltro", solicitacaoFiltro);
				returnInfoMessage(messageProvider.getMessage("msg.success.registro.cadastrado", "Solicitação"));
				putFlashAttr("searched", searched);
			}
			return irParaConsultar();
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
