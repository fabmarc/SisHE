package com.indra.sishe.controller.periodo;

import java.io.Serializable;

import javax.inject.Inject;

import com.indra.infra.controller.BaseController;
import com.indra.sishe.entity.Periodo;
import com.indra.sishe.entity.Regra;
import com.indra.sishe.service.PeriodoService;

public class PeriodoController extends BaseController implements Serializable {

	private static final long serialVersionUID = 7108766087430922340L;

	@Inject
	protected transient PeriodoService periodoService;

	protected Periodo periodoFiltro;

	public Regra regraSelecionada;

	protected Boolean searched;

	public String irParaConsultar() {
		putFlashAttr("regraSelecionadaFiltro", regraSelecionada);
		return "/paginas/periodo/consultarPeriodo.xhtml?faces-redirect=true";
	}

	public String irParaCadastrar() {
		putFlashAttr("regraSelecionadaFiltro", regraSelecionada);
		putFlashAttr("periodoSelecionado", null);
		putFlashAttr("searched", this.searched);
		putFlashAttr("periodoFiltro", this.periodoFiltro);
		return "/paginas/periodo/cadastrarPeriodo.xhtml?faces-redirect=true";
	}

	public String irParaAlterar(Periodo periodoSelecionado) {
		putFlashAttr("regraSelecionadaFiltro", regraSelecionada);
		putFlashAttr("searched", searched);
		putFlashAttr("periodoFiltro", periodoFiltro);
		putFlashAttr("periodoSelecionado", periodoSelecionado);
		return irParaAlterar();
	}

	public String irParaAlterar() {
		return "/paginas/periodo/cadastrarPeriodo.xhtml?faces-redirect=true";
	}

	public PeriodoService getPeriodoService() {
		return periodoService;
	}

	public void setPeriodoService(PeriodoService periodoService) {
		this.periodoService = periodoService;
	}

	public Periodo getPeriodoFiltro() {
		return periodoFiltro;
	}

	public void setPeriodoFiltro(Periodo periodoFiltro) {
		this.periodoFiltro = periodoFiltro;
	}

	public Boolean getSearched() {
		return searched;
	}

	public void setSearched(Boolean searched) {
		this.searched = searched;
	}

	public Regra getRegraSelecionada() {
		return regraSelecionada;
	}

	public void setRegraSelecionada(Regra regraSelecionada) {
		this.regraSelecionada = regraSelecionada;
	}

}
