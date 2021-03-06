package com.indra.sishe.controller.regra;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.primefaces.context.RequestContext;

import com.indra.infra.resource.MessageProvider;
import com.indra.infra.service.exception.ApplicationException;
import com.indra.sishe.entity.Regra;
import com.indra.sishe.entity.Sindicato;

@ViewScoped
@ManagedBean(name = "regraMnt")
public class RegraMntController extends RegraController {

	private static final long serialVersionUID = -6428570700082431997L;

	private List<Regra> regrasSelecionadas;

	protected List<Regra> listaRegras;
	
	public List<Sindicato> listaSindicatos;

	public RegraMntController() {
	}

	@PostConstruct
	private void init() {

		MessageProvider.setInstance(messageProvider);
		setListaSindicatos(obterSindicatos());
		
		searched = (Boolean) getFlashAttr("searched");
		if (searched == null) searched = false;

		regraFiltro = (Regra) getFlashAttr("regraFiltro");
		if (regraFiltro == null) regraFiltro = new Regra();
		
		sindicatoSelecionado = (Sindicato) getSessionAttr("sindicadoSelecionadoFiltro");
		pesquisar();
	}

	public void pesquisar() {
		regraFiltro.setSindicato(sindicatoSelecionado);
		listaRegras = regraService.findByFilter(regraFiltro);
		searched = true;
	}


	
	public String irParaPeriodo() {		
		if (regrasSelecionadas.size() != 1) {
			RequestContext.getCurrentInstance().execute("selectOne.show()");
		} else {
			putFlashAttr("searched", true);
			putFlashAttr("regraFiltro", regraFiltro);
			putSessionAttr("regraSelecionadaFiltro", regrasSelecionadas.get(0));
			return "/paginas/periodo/consultarPeriodo.xhtml?faces-redirect=true";
		}
		return null;
	}
	
	public List<Sindicato> obterSindicatos() {
		Sindicato sindicato = new Sindicato();
		return sindicatoService.findByFilter(sindicato);
	}

	public void beforeRemoveRegra() {
		if (regrasSelecionadas.size() == 0) {
			RequestContext.getCurrentInstance().execute("selectAtleastOne.show()");
		} else {
			RequestContext.getCurrentInstance().execute("confirmExclusao.show()");
		}
	}


	public String remove() {

		int size = regrasSelecionadas.size();
		ArrayList<Long> ids = new ArrayList<Long>(size);
		for (Regra regra : regrasSelecionadas) {
			ids.add(regra.getId());
		}
		try {
			regraService.remove(ids);
			messager.info(messageProvider.getMessage("msg.success.registro.excluido", "Regra"));
		} catch (Exception e) {
			messager.error(e.getMessage());
		}
		pesquisar();
		return irParaConsultar();
	}

	public String irParaAlterar(Regra regra) {

		putFlashAttr("searched", searched);
		putFlashAttr("regraFiltro", regraFiltro);
		try {
			regra = regraService.findById(regra.getId());
			putFlashAttr("regraSelecionada", regra);
			return irParaAlterar();
		} catch (ApplicationException e) {
			returnErrorMessage(e.getMessage());
			return irParaConsultar();
		}
	}

	public String voltarParaSindicato() {
		removeSessionAttr("sindicadoSelecionadoFiltro");
		putFlashAttr("regraFiltro", null);
		return "/paginas/sindicato/consultarSindicato.xhtml?faces-redirect=true";
	}
	
	public List<Regra> getRegrasSelecionadas() {
		return regrasSelecionadas;
	}

	public void setRegrasSelecionadas(List<Regra> regrasSelecionadas) {
		this.regrasSelecionadas = regrasSelecionadas;
	}

	public List<Regra> getListaRegras() {
		return listaRegras;
	}

	public void setListaRegras(List<Regra> listaRegras) {
		this.listaRegras = listaRegras;
	}

	public List<Sindicato> getListaSindicatos() {
		return listaSindicatos;
	}

	public void setListaSindicatos(List<Sindicato> listaSindicatos) {
		this.listaSindicatos = listaSindicatos;
	}

}
