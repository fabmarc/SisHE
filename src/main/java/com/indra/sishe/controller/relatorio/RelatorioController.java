package com.indra.sishe.controller.relatorio;

import java.io.Serializable;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;

import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;

import com.indra.infra.controller.BaseController;
import com.indra.sishe.entity.DadosRelatorio;
import com.indra.sishe.enums.MesEnum;
import com.indra.sishe.service.BancoHorasService;
import com.indra.sishe.service.HistoricoService;

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
		List<DadosRelatorio> dados = historicoService.gerarRelatorio(this.mes.getNumero(), Integer.toString(this.mes.getAno()));
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
						dadoTemp.obterMinutosGerado()), table);
				idMarcador = dadoTemp.getIdSolicitacao();
				saldo = saldo + Integer.parseInt(dadoTemp.getTotal());
				saldoMinu = saldoMinu + dadoTemp.minutosSolicitacao();
			}
			new DefaultTreeNode(new DadosRelatorio("-", "-", "-", "-", dadoTemp.getMinutos() + "min",
					dadoTemp.getPorcentagem() + "%", dadoTemp.getValor() + "min"), work);
			total = total + Integer.parseInt(dadoTemp.getValor());
		}
		
		// exibir o saldo
		if (!(table == null || table.getChildren().size() < 1)) {
			new DefaultTreeNode(new DadosRelatorio("Total", "-", "-", "-", saldoMinu.toString() + "min ("
					+ DadosRelatorio.formatarHora(saldoMinu) + " horas)", "-", saldo.toString() + "min ("
					+ DadosRelatorio.formatarHora(saldo) + " horas)"), table);
		}
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

}
