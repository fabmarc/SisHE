package com.indra.sishe.controller.relatorio;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;

import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;

import com.indra.infra.controller.BaseController;
import com.indra.sishe.controller.usuario.UsuarioLogado;
import com.indra.sishe.entity.DadosRelatorio;
import com.indra.sishe.entity.HistoricoDebito;
import com.indra.sishe.entity.Projeto;
import com.indra.sishe.entity.Usuario;
import com.indra.sishe.enums.MesEnum;
import com.indra.sishe.service.BancoHorasService;
import com.indra.sishe.service.HistoricoDebitoService;
import com.indra.sishe.service.HistoricoService;
import com.indra.sishe.service.ProjetoService;
import com.indra.sishe.service.UsuarioService;

@ManagedBean(name = "relatorioController")
@ViewScoped
public class RelatorioController extends BaseController implements Serializable {

	private static final long serialVersionUID = -7452585750795635252L;

	private DadosRelatorio selectedDocument;

	private TreeNode table;

	private String ano;

	private MesEnum mes;

	@Inject
	protected transient HistoricoService historicoService;

	@Inject
	protected transient BancoHorasService bancoHorasService;

	@Inject
	protected transient HistoricoDebitoService historicoDebitoService;

	@Inject
	protected transient ProjetoService projetoService;

	@Inject
	protected transient UsuarioService usuarioService;

	private Usuario usuarioFiltro;

	public Usuario getUsuarioFiltro() {
		return usuarioFiltro;
	}

	public void setUsuarioFiltro(Usuario usuarioFiltro) {
		this.usuarioFiltro = usuarioFiltro;
	}

	public TreeNode getTable() {
		return table;
	}

	public void setTable(TreeNode table) {
		this.table = table;
	}

	public DadosRelatorio getSelectedDocument() {
		return selectedDocument;
	}

	public void setSelectedDocument(DadosRelatorio selectedDocument) {
		this.selectedDocument = selectedDocument;
	}

	public void pesquisar() {

		table = null;
		this.table = gerarHistorico();
		if (!mostrarTable()) {
			returnInfoMessage(messageProvider.getMessage("msg.info.relatorio.vazio"));
		}
	}

	public String getAno() {
		return ano;
	}

	public void setAno(String ano) {
		this.ano = ano;
	}

	public MesEnum getMes() {
		return mes;
	}

	public void setMes(MesEnum mes) {
		this.mes = mes;
	}

	public TreeNode gerarHistorico() {

		TreeNode table = new DefaultTreeNode(new DadosRelatorio("-", "-", "-", "-", "-", "-", "-"), null);
		TreeNode credito = new DefaultTreeNode(new DadosRelatorio("Créditos", "-", "-", "-", "-", "-", "-"), table);
		TreeNode debito = new DefaultTreeNode(new DadosRelatorio("Débitos", "-", "-", "-", "-", "-", "-"), table);
		List<DadosRelatorio> dados;
		List<HistoricoDebito> debitos;
		if (usuarioFiltro == null) {
			dados = historicoService.gerarRelatorio(this.mes.getNumero(), Integer.toString(this.mes.getAno()),
					new Usuario(UsuarioLogado.getId()));
			debitos = historicoDebitoService.findByUsuarioEMes(new Usuario(UsuarioLogado.getId()),
					this.mes.getNumero(), Integer.toString(this.mes.getAno()));
		} else {
			dados = historicoService.gerarRelatorio(this.mes.getNumero(), Integer.toString(this.mes.getAno()),
					usuarioFiltro);
			debitos = historicoDebitoService.findByUsuarioEMes(usuarioFiltro, this.mes.getNumero(),
					Integer.toString(this.mes.getAno()));
		}
		Integer idMarcador = -1;
		Integer total = 0;
		TreeNode work = null;
		Integer saldo = 0;
		Integer saldoMinu = 0;
		for (DadosRelatorio dadoTemp : dados) {
			if (dadoTemp.getIdSolicitacao() != idMarcador) {
				work = new DefaultTreeNode(new DadosRelatorio(dadoTemp.getDataSolicitacao(), "-", dadoTemp
						.getHoraInicioSolicitacao().substring(0, 5), dadoTemp.getHoraFimSolicitacao().substring(0,
						5), dadoTemp.obterMinutosSoliciacao(), dadoTemp.porcentagemGeral(),
						dadoTemp.obterMinutosGerado()), credito);
				idMarcador = dadoTemp.getIdSolicitacao();
				saldo = saldo + Integer.parseInt(dadoTemp.getTotal());
				saldoMinu = saldoMinu + dadoTemp.minutosSolicitacao();
			}
			new DefaultTreeNode(new DadosRelatorio("-", "-", "-", "-", dadoTemp.getMinutos() + "min",
					dadoTemp.getPorcentagem() + "%", dadoTemp.getValor() + "min"), work);
			total = total + Integer.parseInt(dadoTemp.getValor());
			new DefaultTreeNode(new DadosRelatorio("Total", "-", "-", "-", saldoMinu.toString() + "min ("
					+ DadosRelatorio.formatarHora(saldoMinu) + " horas)", "-", saldo.toString() + "min ("
					+ DadosRelatorio.formatarHora(saldo) + " horas)"), credito);
		}

		int totalNegativo = 0;
		for (HistoricoDebito histoTemp : debitos) {
			SimpleDateFormat formatarData = new SimpleDateFormat("dd/MM/yyyy");
			work = new DefaultTreeNode(new DadosRelatorio(formatarData.format(histoTemp.getData()).toString(),
					"-", "-", "-", "-", "-", "- " + histoTemp.getMinutos().toString() + "min ("
							+ DadosRelatorio.formatarHora(histoTemp.getMinutos()) + " horas)"), debito);
			saldo = saldo - histoTemp.getMinutos();
			saldoMinu = saldoMinu - histoTemp.getMinutos();
			totalNegativo = totalNegativo + histoTemp.getMinutos();

			total = total + histoTemp.getMinutos();
		}
		if (debitos.size() > 0) {
			new DefaultTreeNode(
					new DadosRelatorio("Total", "-", "-", "-", "-", "-", "- " + totalNegativo + "min"), debito);
		}

		// exibir o saldo
		if (!(table == null || (table.getChildren().get(0).getChildren().size() < 1 && table.getChildren().get(1)
				.getChildren().size() < 1))) {
			new DefaultTreeNode(new DadosRelatorio("Total Geral", "-", "-", "-", saldoMinu.toString() + "min ("
					+ DadosRelatorio.formatarHora(saldoMinu) + " horas)", "-", saldo.toString() + "min ("
					+ DadosRelatorio.formatarHora(saldo) + " horas)"), table);
		} else {
			table = null;
		}
		carregarSaldo();
		return table;
	}

	public boolean mostrarTable() {

		if (table == null || table.getChildren().size() < 1) {
			return false;
		} else {
			return true;
		}
	}

	public List<MesEnum> obterListaMes() {
		List<MesEnum> listaDias = MesEnum.listaMeses();
		return listaDias;
	}

	public List<Integer> obterListaAnos() {
		return MesEnum.anosAtuais();
	}

	public String irParaRelatorio() {
		return "/paginas/solicitacao/relatorio.xhtml?faces-redirect=true";
	}

	public List<Usuario> obterUsuariosProjeto() {

		List<Usuario> usuarios = new ArrayList<Usuario>();
		ArrayList<Long> idsProjeto = new ArrayList<Long>();
		if (UsuarioLogado.verificarPermissao("ROLE_GERENTE")) {
			List<Projeto> projetos = projetoService.findByGerente(new Usuario(UsuarioLogado.getId()));
			for (Projeto p : projetos) {
				idsProjeto.add(p.getId());
			}
			usuarios = usuarioService.findByProjetos(idsProjeto);
			usuarios.add(new Usuario(UsuarioLogado.getId(), (String) getSessionAttr("usuario_nome")));
			return usuarios;
		} else if (UsuarioLogado.verificarPermissao("ROLE_LIDER")) {
			Projeto p = projetoService.findByUsuario(new Usuario(UsuarioLogado.getId(),
					(String) getSessionAttr("usuario_nome")));
			idsProjeto.add(p.getId());
			usuarios = usuarioService.findByProjetos(idsProjeto);
			return usuarios;
		} else {
			return null;
		}
	}

	public boolean verificarFuncionario() {

		if (UsuarioLogado.verificarPermissao("ROLE_FUNCIONARIO")) {
			return true;
		} else {
			return false;
		}
	}

	private void carregarSaldo() {
		putSessionAttr("dataAtualizacao", Calendar.getInstance());// Guardar a data/hora atual.
		putSessionAttr("saldo", bancoHorasService.findByUsuario(new Usuario(UsuarioLogado.getId())).getSaldo());// Atualizar saldo do usuário logado.
	}

	public void atualizarSaldo() {
		Calendar dataAtual = Calendar.getInstance();
		Calendar dataAntiga = (Calendar) getSessionAttr("dataAtualizacao");
		int tempoEspera = 5;// Tempo de espera igual a 5 min.
		if (dataAntiga == null
				|| (dataAntiga.get(Calendar.MINUTE) + tempoEspera <= dataAtual.get(Calendar.MINUTE))
				|| (dataAntiga.get(Calendar.HOUR) < dataAtual.get(Calendar.HOUR))) {// Se passar 5 min depois da ultima atualização ou a hora foi alterada será atualizado o saldo (de 5 em 5 min pode se atualizar)
			carregarSaldo();
		}
	}

}
