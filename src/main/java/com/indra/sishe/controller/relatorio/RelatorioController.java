package com.indra.sishe.controller.relatorio;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;

import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;

import com.indra.infra.controller.BaseController;
import com.indra.sishe.entity.DadosRelatorio;
import com.indra.sishe.enums.Mes;
import com.indra.sishe.service.HistoricoService;

@ManagedBean(name = "relatorioController")
@ViewScoped
public class RelatorioController extends BaseController implements Serializable {

	private static final long serialVersionUID = -7452585750795635252L;

	private DadosRelatorio selectedDocument;

	private TreeNode table;
	
	private String ano;
	
	private String mes;

	@Inject
	protected transient HistoricoService historicoService;

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
		this.table = gerarHistorico();
		if(!mostrarTable()){
			returnInfoMessage(messageProvider.getMessage("msg.info.relatorio.vazio"));
		}
	}

	public String getAno() {
		return ano;
	}

	public void setAno(String ano) {
		this.ano = ano;
	}

	public String getMes() {
		return mes;
	}

	public void setMes(String mes) {
		this.mes = mes;
	}

	public TreeNode gerarHistorico() {
		TreeNode table = new DefaultTreeNode(new DadosRelatorio("-", "-", "-", "-", "-", "-", "-"), null);
		List<DadosRelatorio> dados = historicoService.gerarRelatorio(this.mes, this.ano);
		Integer idMarcador = -1;
		Integer total = 0;
		TreeNode work = null;
		for (DadosRelatorio dadoTemp : dados) {
			if (dadoTemp.getIdSolicitacao() != idMarcador) {
				work = new DefaultTreeNode(new DadosRelatorio(dadoTemp.getDataSolicitacao(), "-", dadoTemp.getHoraInicioSolicitacao(), dadoTemp.getHoraFimSolicitacao(), dadoTemp.obterMinutosSoliciacao(), dadoTemp.porcentagemGeral(), dadoTemp.obterMinutosGerado()), table);
				idMarcador = dadoTemp.getIdSolicitacao();
			}
			new DefaultTreeNode(new DadosRelatorio("-", "-", "-", "-", dadoTemp.getMinutos() + " min", dadoTemp.getPorcentagem() + " %", dadoTemp.getValor() + " min"), work);
			total = total + Integer.parseInt(dadoTemp.getValor());
		}
		return table;
	}

	public boolean mostrarTable() {
		if (table == null || table.getChildren().size()<1) {
			return false;
		} else {
			return true;
		}
	}
	
	public List<Mes> obterListaMes() {
		List<Mes> listaDias = new ArrayList<Mes>(Arrays.asList(Mes.values()));
		return listaDias;
	}
	
	public String irParaRelatorio(){
		return "/paginas/solicitacao/relatorio.xhtml?faces-redirect=true";
	}

}
