package com.indra.sishe.controller.periodo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.primefaces.context.RequestContext;

import com.indra.infra.resource.MessageProvider;
import com.indra.infra.service.exception.ApplicationException;
import com.indra.sishe.entity.Periodo;
import com.indra.sishe.entity.Regra;
import com.indra.sishe.enums.DiaSemanaEnum;

@ViewScoped
@ManagedBean(name = "periodoMnt")
public class PeriodoMntController extends PeriodoController {

	private static final long serialVersionUID = -1579918403929309488L;

	private List<Periodo> listaPeriodos;

	private List<Periodo> periodosSelecionados;

	public PeriodoMntController() {

	}

	@PostConstruct
	public void init() {

		MessageProvider.setInstance(messageProvider);

		searched = (Boolean) getFlashAttr("searched");
		if (searched == null) searched = false;

		periodoFiltro = (Periodo) getFlashAttr("periodoFiltro");
		if (periodoFiltro == null) periodoFiltro = new Periodo();

		regraSelecionada = (Regra) getFlashAttr("regraSelecionadaFiltro");

		if (!searched) listaPeriodos = new ArrayList<Periodo>();
		else pesquisar();
	}

	public void pesquisar() {

		periodoFiltro.setRegra(regraSelecionada);
		listaPeriodos = periodoService.findByFilter(periodoFiltro);
		Collections.sort(listaPeriodos);
		searched = true;
	}

	public void beforeRemovePeriodos() {

		if (periodosSelecionados.size() == 0) {
			RequestContext.getCurrentInstance().execute("selectAtleastOne.show()");
		} else {
			RequestContext.getCurrentInstance().execute("confirmExclusao.show()");
		}
	}

	public String removerPeriodo() {

		int size = periodosSelecionados.size();
		ArrayList<Long> ids = new ArrayList<Long>(size);
		for (Periodo periodo : periodosSelecionados)
			ids.add(periodo.getId());
		try {
			periodoService.remove(ids);
			messager.info(messageProvider.getMessage("msg.success.registro.excluido", "Periodo"));
		} catch (ApplicationException e) {
			messager.error(e.getMessage());
		}
		pesquisar();
		return irParaConsultar();
	}

	public String irParaAlterar(Periodo periodoSelecionado) {

		putFlashAttr("searched", this.searched);
		putFlashAttr("periodoFiltro", this.periodoFiltro);
		try {
			periodoSelecionado = periodoService.findById(periodoSelecionado.getId());
			putFlashAttr("regraSelecionadaFiltro", regraSelecionada);
			putFlashAttr("periodoSelecionado", periodoSelecionado);
			return irParaAlterar();
		} catch (ApplicationException e) {
			returnErrorMessage(e.getMessage());
			return irParaConsultar();
		}
	}

	public List<Periodo> getListaPeriodos() {

		return listaPeriodos;
	}

	public void setListaPeriodos(List<Periodo> listaPeriodos) {

		this.listaPeriodos = listaPeriodos;
	}

	public List<Periodo> getPeriodosSelecionados() {

		return periodosSelecionados;
	}

	public void setPeriodosSelecionados(List<Periodo> periodosSelecionados) {

		this.periodosSelecionados = periodosSelecionados;
	}

	public DiaSemanaEnum obterDia(int diaSemana) {

		return DiaSemanaEnum.obterDiaSemana(diaSemana);
	}

	public String voltarRegra() {
		putFlashAttr("searched", true);
		return "/paginas/regra/consultarRegra.xhtml";
	}

}
