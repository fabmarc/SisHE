package com.indra.sishe.controller.feriado;

import java.io.Serializable;

import javax.inject.Inject;

import com.indra.infra.controller.BaseController;
import com.indra.sishe.entity.Feriado;
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

	protected Boolean searched;

	public boolean validarFeriado(Feriado feriadoSelecionado) {
		if (feriadoSelecionado.getNome().isEmpty()) {
			messager.error(messageProvider.getMessage("msg.error.campo.obrigatorio", "Nome"));
		} else if (feriadoSelecionado.getNome().length() > 30) {
			messager.error(messageProvider.getMessage("msg.error.campo.maior.esperado", "Nome", "30"));
		} else if ((feriadoSelecionado.getTipo() != 'F') && (feriadoSelecionado.getTipo() != 'M')) {
			messager.error(messageProvider.getMessage("msg.error.campo.obrigatorio", "Tipo"));
		} else if(feriadoSelecionado.getData() == null){
			messager.error(messageProvider.getMessage("msg.error.campo.obrigatorio", "Data"));
		}else{
			return true;
		}
		return false;
	}

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
		return "/paginas/feriado/cadastrarFeriado.xhtml?faces-redirect=true";
	}
	
	public String irParaCadastrar() {
		putFlashAttr("searched", searched);
		putFlashAttr("feriadoFiltro", feriadoFiltro);
		putFlashAttr("feriadoSelecionado", null);
		return "/paginas/feriado/cadastrarFeriado.xhtml?faces-redirect=true";
	}

	public Feriado getFeriadoFiltro() {
		return feriadoFiltro;
	}

	public void setFeriadoFiltro(Feriado feriadoFiltro) {
		this.feriadoFiltro = feriadoFiltro;
	}

}