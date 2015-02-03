package com.indra.sishe.controller.regra;

import java.io.Serializable;

import javax.inject.Inject;

import com.indra.infra.controller.BaseController;
import com.indra.sishe.entity.Regra;
import com.indra.sishe.entity.Sindicato;
import com.indra.sishe.service.RegraService;
import com.indra.sishe.service.SindicatoService;

public class RegraController extends BaseController implements Serializable {

	private static final long serialVersionUID = 8214192137526431359L;

	@Inject
	protected transient RegraService regraService;

	@Inject
	protected transient SindicatoService sindicatoService;

	protected Regra regraFiltro;

	protected Boolean searched;
	
	protected Sindicato sindicatoSelecionado;

	public String irParaConsultar() {
		return "/paginas/regra/consultarRegra.xhtml?faces-redirect=true";
	}

	public String irParaAlterar() {
		return "/paginas/regra/cadastrarRegra.xhtml?faces-redirect=true";
	}

	public String irParaAlterar(Regra regraSelecionada) {
		putFlashAttr("searched", searched);
		putFlashAttr("regraFiltro", regraFiltro);
		putFlashAttr("regraSelecionada", regraSelecionada);
		return irParaAlterar();
	}

	public String irParaCadastrar() {
		putFlashAttr("searched", searched);
		putFlashAttr("regraFiltro", regraFiltro);
		putFlashAttr("regraSelecionada", null);
		return irParaAlterar();
	}

	public Regra getRegraFiltro() {
		return regraFiltro;
	}

	public void setRegraFiltro(Regra regraFiltro) {
		this.regraFiltro = regraFiltro;
	}

	public Sindicato getSindicatoSelecionado() {
		return sindicatoSelecionado;
	}

	public void setSindicatoSelecionado(Sindicato sindicatoSelecionado) {
		this.sindicatoSelecionado = sindicatoSelecionado;
	}
	
}
