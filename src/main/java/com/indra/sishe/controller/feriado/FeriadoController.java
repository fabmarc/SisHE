package com.indra.sishe.controller.feriado;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import com.indra.infra.controller.BaseController;
import com.indra.sishe.entity.Estado;
import com.indra.sishe.entity.Feriado;
import com.indra.sishe.enums.EstadoEnum;
import com.indra.sishe.service.CidadeService;
import com.indra.sishe.service.EstadoService;
import com.indra.sishe.service.FeriadoService;

public class FeriadoController extends BaseController implements Serializable {

	private static final long serialVersionUID = 6337707577928271493L;

	@Inject
	protected transient FeriadoService feriadoService;

	@Inject
	protected transient EstadoService estadoService;

	@Inject
	protected transient CidadeService cidadeService;

	public Feriado feriadoFiltro;
	
	protected List<Estado> listaEstado;

	protected Boolean searched;

	public String irParaConsultar() {
		return "/paginas/feriado/consultarFeriado.xhtml?faces-redirect=true";
	}

	public String irParaAlterar() {
		return "/paginas/feriado/cadastrarFeriado.xhtml?faces-redirect=true";
	}
	
	public String irParaAlterar(Feriado feriadoSelecionado) {
		putFlashAttr("searched", searched);
		putFlashAttr("feriadoFiltro", feriadoFiltro);
		putFlashAttr("feriadoSelecionado", feriadoSelecionado);
		return irParaAlterar();
	}

	public String irParaCadastrar() {
		putFlashAttr("searched", searched);
		putFlashAttr("feriadoFiltro", feriadoFiltro);
		putFlashAttr("feriadoSelecionado", null);
		return "/paginas/feriado/cadastrarFeriado.xhtml?faces-redirect=true";
	}
	
	public List<EstadoEnum> obterEstados() {
		List<EstadoEnum> listaEstados = new ArrayList<EstadoEnum>(Arrays.asList(EstadoEnum.values()));
		return listaEstados;
	}
	
	public Feriado getFeriadoFiltro() {
		return feriadoFiltro;
	}

	public void setFeriadoFiltro(Feriado feriadoFiltro) {
		this.feriadoFiltro = feriadoFiltro;
	}

	public List<Estado> getListaEstado() {
		return listaEstado;
	}

	public void setListaEstado(List<Estado> listaEstado) {
		this.listaEstado = listaEstado;
	}

}