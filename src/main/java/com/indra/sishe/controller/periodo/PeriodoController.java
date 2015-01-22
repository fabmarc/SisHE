package com.indra.sishe.controller.periodo;

import java.io.Serializable;

import javax.inject.Inject;

import com.indra.infra.controller.BaseController;
import com.indra.sishe.entity.Periodo;
import com.indra.sishe.service.PeriodoService;

public class PeriodoController extends BaseController implements Serializable {

	private static final long serialVersionUID = 7108766087430922340L;

	@Inject
	protected transient PeriodoService periodoService;

	public Periodo periodoFiltro;

	protected Boolean searched;

	public String irParaConsultar() {
		return "/paginas/periodo/consultarPeriodo.xhtml?faces-redirect=true";
	}

	public String irParaCadastrar() {
		putFlashAttr("periodoSelecionado", null);
		putFlashAttr("searched", this.searched);
		putFlashAttr("periodoFiltro", this.periodoFiltro);
		return "/paginas/periodo/cadastrarPeriodo.xhtml?faces-redirect=true";
	}

	public String irParaAlterar(Periodo periodoSelecionado) {
		putFlashAttr("searched", searched);
		putFlashAttr("periodoFiltro", periodoFiltro);
		putFlashAttr("periodoSelecionado", periodoSelecionado);
		return irParaAlterar();
	}

	public String irParaAlterar() {
		return "/paginas/periodo/cadastrarPeriodo.xhtml?faces-redirect=true";
	}

	public boolean validarPeriodo(Periodo periodoSelecionado) {
		if (periodoSelecionado.getDiaSemana() == null || periodoSelecionado.getDiaSemana() < 1 || periodoSelecionado.getDiaSemana() > 7) {
			messager.error(messageProvider.getMessage("msg.error.campo.obrigatorio", "Dia da semana"));
		} else if (periodoSelecionado.getHoraInicio() == null) {
			messager.error(messageProvider.getMessage("msg.error.campo.obrigatorio", "Hora Inicio"));
		} else if (periodoSelecionado.getHoraFim() == null) {
			messager.error(messageProvider.getMessage("msg.error.campo.obrigatorio", "Hora Fim"));
		} else if (periodoSelecionado.getHoraInicio().getTime() >= periodoSelecionado.getHoraFim().getTime()) {
			messager.error(messageProvider.getMessage("msg.error.intervalo.incorreto", "Hora Inicio", "Hora Fim"));
		} else if (periodoSelecionado.getPorcentagem() == null) {
			messager.error(messageProvider.getMessage("msg.error.campo.obrigatorio", "Porcentagem"));
		} else {
			return true;
		}

		return false;
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

}
