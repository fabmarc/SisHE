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
import com.indra.sishe.entity.Estado;
import com.indra.sishe.entity.Sindicato;

@ViewScoped
@ManagedBean(name = "sindicatoMnt")
public class SindicatoMntController extends SindicatoController {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private List<Sindicato> listaSindicato;
	
	private List<Sindicato> sindicatosSelecionados;
	
	public SindicatoMntController() {
		// TODO Auto-generated constructor stub
	}

	// INCIACIALIZA O MB APÓS O @INJECT MAS ANTES É INSERIDO NA CLASSE SERVICE
	@PostConstruct
	private void init() {

		MessageProvider.setInstance(messageProvider);

		searched = (Boolean) getFlashAttr("searched");
		if (searched == null)
			searched = false;

		sindicatoFiltro = (Sindicato) getFlashAttr("sindicatoFiltro");
		if (sindicatoFiltro == null)
			sindicatoFiltro = new Sindicato();
		if (!searched)
			listaSindicato = new ArrayList<Sindicato>();
		else
			pesquisar();
		
		setListaEstado(estadoService.findAll());
		
		
	}

	public String removerSindicato() {
		int size = sindicatosSelecionados.size();
		ArrayList<Long> ids = new ArrayList<Long>(size);
		for (Sindicato sindicato : sindicatosSelecionados) ids.add(sindicato.getId());
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
			sindicatoSelecionado = sindicatoService
					.findById(sindicatoSelecionado.getId());
			putFlashAttr("sindicatoSelecionado", sindicatoSelecionado);
			return "/paginas/sindicato/cadastrarSindicato.xhtml?faces-redirect=true";
		} catch (ApplicationException e) {
			returnErrorMessage(e.getMessage());
			return irParaConsultar();
		}
	}

	

	public void beforeRemoveCliente() {
		
		if (sindicatosSelecionados.size() == 0) {
			RequestContext.getCurrentInstance().execute(
					"selectAtleastOne.show()");
		} else {
			RequestContext.getCurrentInstance().execute(
					"confirmExclusao.show()");
		}
	}

	public void pesquisar() {
		listaSindicato = sindicatoService.findByFilter(sindicatoFiltro);
		Collections.sort(listaSindicato);
		searched = true;
	}

	/**
	 * @return the listaSindicato
	 */
	public List<Sindicato> getListaSindicato() {
		return listaSindicato;
	}

	/**
	 * @param listaSindicato
	 *            the listaSindicato to set
	 */
	public void setListaSindicato(List<Sindicato> listaSindicato) {
		this.listaSindicato = listaSindicato;
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

	/**
	 * @return the sindicatosSelecionados
	 */
	public List<Sindicato> getSindicatosSelecionados() {
		return sindicatosSelecionados;
	}

	/**
	 * @param sindicatosSelecionados the sindicatosSelecionados to set
	 */
	public void setSindicatosSelecionados(List<Sindicato> sindicatosSelecionados) {
		this.sindicatosSelecionados = sindicatosSelecionados;
	}
 
	

}
