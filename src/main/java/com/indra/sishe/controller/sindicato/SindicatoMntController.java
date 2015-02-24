package com.indra.sishe.controller.sindicato;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.primefaces.context.RequestContext;

import com.indra.infra.resource.MessageProvider;
import com.indra.infra.service.exception.ApplicationException;
import com.indra.sishe.entity.Sindicato;
import com.indra.sishe.enums.EstadoEnum;

@ViewScoped
@ManagedBean(name = "sindicatoMnt")
public class SindicatoMntController extends SindicatoController {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private List<Sindicato> listaSindicatos;

	private List<Sindicato> sindicatosSelecionados;

	public SindicatoMntController() {
	}

	// INCIACIALIZA O MB APÓS O @INJECT MAS ANTES É INSERIDO NA CLASSE SERVICE
	@PostConstruct
	private void init() {

		MessageProvider.setInstance(messageProvider);

		searched = (Boolean) getFlashAttr("searched");
		if (searched == null) searched = false;

		sindicatoFiltro = (Sindicato) getFlashAttr("sindicatoFiltro");
		if (sindicatoFiltro == null) sindicatoFiltro = new Sindicato();
		if (!searched) listaSindicatos = new ArrayList<Sindicato>();
		else pesquisar();

		setListaEstado(EstadoEnum.listaEstados());

	}

	public String removerSindicato() {
		int size = sindicatosSelecionados.size();
		ArrayList<Long> ids = new ArrayList<Long>(size);
		for (Sindicato sindicato : sindicatosSelecionados)
			ids.add(sindicato.getId());
		try {
			sindicatoService.remove(ids);
			messager.info(messageProvider.getMessage("msg.success.registro.excluido", "sindicato"));
		} catch (ApplicationException e) {
			messager.error(e.getMessage());
		}
		pesquisar();
		return irParaConsultar();
	}

	public String irParaAlterar(Sindicato sindicatoSelecionado) {
		putFlashAttr("searched", searched);
		putFlashAttr("sindicatoFiltro", sindicatoFiltro);
		try {
			sindicatoSelecionado = sindicatoService.findById(sindicatoSelecionado.getId());
			putFlashAttr("sindicatoSelecionado", sindicatoSelecionado);
			return "/paginas/sindicato/cadastrarSindicato.xhtml?faces-redirect=true";
		} catch (ApplicationException e) {
			returnErrorMessage(e.getMessage());
			return irParaConsultar();
		}
	}

	public void beforeRemoveSindicato() {

		if (sindicatosSelecionados.size() == 0) {
			RequestContext.getCurrentInstance().execute("selectAtleastOne.show()");
		} else {
			RequestContext.getCurrentInstance().execute("confirmExclusao.show()");
		}
	}

	public void pesquisar() {
		listaSindicatos = sindicatoService.findByFilter(sindicatoFiltro);
		Collections.sort(listaSindicatos);
		searched = true;
	}

	public String irParaRegra() {
		if (sindicatosSelecionados.size() != 1) {
			RequestContext.getCurrentInstance().execute("selectOne.show()");
		} else {
			putFlashAttr("searched", true);
			putFlashAttr("sindicatoFiltro", sindicatoFiltro);
			putSessionAttr("sindicadoSelecionadoFiltro", sindicatosSelecionados.get(0));
			return "/paginas/regra/consultarRegra.xhtml?faces-redirect=true";
		}
		return null;
	}

	public List<Sindicato> getListaSindicato() {
		return listaSindicatos;
	}

	public void setListaSindicato(List<Sindicato> listaSindicato) {
		this.listaSindicatos = listaSindicato;
	}

	public Sindicato getSindicatoFiltro() {
		return sindicatoFiltro;
	}

	public void setSindicatoFiltro(Sindicato sindicatoFiltro) {
		this.sindicatoFiltro = sindicatoFiltro;
	}

	public Sindicato getSindicatoSelecionado() {
		return sindicatoSelecionado;
	}

	public void setSindicatoSelecionado(Sindicato sindicatoSelecionado) {
		this.sindicatoSelecionado = sindicatoSelecionado;
	}

	public List<Sindicato> getSindicatosSelecionados() {
		return sindicatosSelecionados;
	}

	public void setSindicatosSelecionados(List<Sindicato> sindicatosSelecionados) {
		this.sindicatosSelecionados = sindicatosSelecionados;
	}

}
