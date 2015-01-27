package com.indra.sishe.controller.regra;

import java.io.Serializable;

import javax.inject.Inject;

import com.indra.infra.controller.BaseController;
import com.indra.sishe.entity.Regra;
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

	public boolean validarRegra(Regra regra) {
		if (regra.getDescricao().isEmpty()) {
			messager.error(messageProvider.getMessage("msg.error.campo.obrigatorio", "Nome"));
		} else if (regra.getDescricao().length() > 100) {
			messager.error(messageProvider.getMessage("msg.error.campo.maior.esperado", "Nome", "100"));
		} else if (regra.getSindicato() == null) {
			messager.error(messageProvider.getMessage("msg.error.campo.obrigatorio", "Sindicato"));
		} else if (regra.getDataInicio() == null) {
			messager.error(messageProvider.getMessage("msg.error.campo.obrigatorio", "Data Inicial"));
		} else if (regra.getDataFim() == null) {
			messager.error(messageProvider.getMessage("msg.error.campo.obrigatorio", "Data Final"));
		} else if (regra.getDataInicio().after(regra.getDataFim())) {
			messager.error(messageProvider.getMessage("msg.error.intervalo.incorreto", "Data Inicial",
					"Data Final"));
		} else if (regra.getPorcentagem() == null) {
			messager.error(messageProvider.getMessage("msg.error.campo.obrigatorio", "Porcentagem"));
		} else {
			return true;
		}
		return false;
	}

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

}
