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
import com.indra.sishe.enums.StatusEnum;
import com.indra.sishe.service.BancoHorasService;
import com.indra.sishe.service.HistoricoService;
import com.indra.sishe.service.SistemaService;

@ViewScoped
@ManagedBean(name = "solicitacaoMnt")
public class SolicitacaoMntController extends SolicitacaoController {

	private static final long serialVersionUID = 3699487265700133440L;

	private List<Solicitacao> listaSolicitacoes;

	private List<Solicitacao> solicitacoesSelecionadas;

	private Solicitacao solicitacaoDetalhe;

	private String observacao;

	private boolean todasSolicitacoes;

	@Inject
	protected transient BancoHorasService bancoHorasService;

	@Inject
	protected transient HistoricoService historicoService;

	@Inject
	protected transient SistemaService sistemaService;

	@PostConstruct
	public void init() {

		MessageProvider.setInstance(messageProvider);
		setListaSistemas(obterSistemas());
		if (UsuarioLogado.getPermissoes().contains("ROLE_FUNCIONARIO")) {
			todasSolicitacoes = true; // Para habilitar campos do filtro
		}

		searched = (Boolean) getFlashAttr("searched");
		if (searched == null) searched = false;

		solicitacaoFiltro = (Solicitacao) getFlashAttr("solicitacaoFiltro");
		if (solicitacaoFiltro == null) solicitacaoFiltro = new Solicitacao();

		pesquisar();
	}

	public void pesquisar() {
		if (UsuarioLogado.getPermissoes().contains("ROLE_GERENTE")) {
			pesquisarGerente();
		} else if (UsuarioLogado.getPermissoes().contains("ROLE_LIDER")) {
			pesquisarLider();
		} else {
			pesquisarPorUsuarioLogado();
		}
	}

	private void pesquisarGerente() {
		if (todasSolicitacoes == false) {
			pesquisarPendentes();
		} else {
			pesquisarPorProjeto();
		}
	}

	private void pesquisarLider() {
		if (todasSolicitacoes == false) {
			pesquisarPendentes();
		} else {
			pesquisarPorUsuarioLogado();
		}
	}

	public void pesquisarTodos() {
		listaSolicitacoes = solicitacaoService.findByFilter(solicitacaoFiltro);
		searched = true;
	}

	public void pesquisarPorUsuarioLogado() {
		Usuario usuario = new Usuario();
		usuario.setId(UsuarioLogado.getId());
		solicitacaoFiltro.setUsuario(usuario);
		listaSolicitacoes = solicitacaoService.findByFilterByUsuario(solicitacaoFiltro);
		searched = true;
	}

	public void pesquisarPorProjeto() {
		Usuario usuario = new Usuario();
		usuario.setId(UsuarioLogado.getId());
		solicitacaoFiltro.setGerente(usuario);
		listaSolicitacoes = solicitacaoService.findByProjeto(solicitacaoFiltro);
		searched = true;
	}

	public void pesquisarPendentes() {
		if ((UsuarioLogado.getPermissoes()).contains("ROLE_GERENTE")) {
			Usuario gerenteLogado = new Usuario();
			gerenteLogado.setId(UsuarioLogado.getId());
			solicitacaoFiltro.setGerente(gerenteLogado);
			setListaSolicitacoes(solicitacaoService.findByGerente(solicitacaoFiltro));
		} else if ((UsuarioLogado.getPermissoes()).contains("ROLE_LIDER")) {
			Usuario liderLogado = new Usuario();
			liderLogado.setId(UsuarioLogado.getId());
			solicitacaoFiltro.setLider(liderLogado);
			setListaSolicitacoes(solicitacaoService.findByLider(solicitacaoFiltro));
		}
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
		pesquisar();
		return irParaConsultar();
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
		try {
			if ((UsuarioLogado.getPermissoes()).contains("ROLE_GERENTE")) {
				for (Solicitacao solicitacao : solicitacoesSelecionadas) {
					if (solicitacao.getStatusGerente().getId() == 3) {
						ids.add(solicitacao.getId());
					}
				}
				solicitacaoService.gerenteAcaoSolicitacao(ids, status);
				if (status == 1) {
					detalhes = bancoHorasService.contabilizarHorasBanco(ids);
				}
			} else {
				for (Solicitacao solicitacao : solicitacoesSelecionadas) {
					if (solicitacao.getStatusLider().getId() == 3) {
						ids.add(solicitacao.getId());
					}
				}
				solicitacaoService.liderAcaoSolicitacao(ids, status);
			}
			historicoService.gerarHistorico(ids, observacao, detalhes);
			observacao = "";
			if (solicitacoesSelecionadas.size() > ids.size()) {
				messager.info(messageProvider.getMessage("msg.success.solicitacao.aprovada.excecao"));
			} else {
				messager.info(messageProvider.getMessage("msg.success.solicitacao.aprovada"));
			}
		} catch (ApplicationException e) {
			messager.error(e.getMessage());
		}
		pesquisarPendentes();
	}

	public boolean verificarCargo() {
		if (UsuarioLogado.verificarPermissao("ROLE_GERENTE") || UsuarioLogado.verificarPermissao("ROLE_LIDER")) {
			return true;
		} else {
			return false;
		}
	}

	public boolean verificarLider() {
		if (UsuarioLogado.verificarPermissao("ROLE_LIDER")) {
			return true;
		} else {
			return false;
		}
	}

	public List<StatusEnum> listaStatus() {
		return StatusEnum.status();
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

	public String getObservacao() {
		return observacao;
	}

	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}

	public boolean isTodasSolicitacoes() {
		return todasSolicitacoes;
	}

	public void setTodasSolicitacoes(boolean todosUsuarios) {
		this.todasSolicitacoes = todosUsuarios;
	}

}
