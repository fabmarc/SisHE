package com.indra.sishe.controller.projeto;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.primefaces.context.RequestContext;

import com.indra.infra.resource.MessageProvider;
import com.indra.infra.service.exception.ApplicationException;
import com.indra.sishe.entity.Projeto;

@ViewScoped
@ManagedBean(name = "projetoMnt")
public class ProjetoMntController extends ProjetoController {

	private static final long serialVersionUID = -8555563300052713170L;

	private List<Projeto> listaProjetos;
	private List<Projeto> projetosSelecionados;

	public ProjetoMntController() {
	}

	@PostConstruct
	private void init() {

		MessageProvider.setInstance(messageProvider);

		searched = (Boolean) getFlashAttr("searched");
		if (searched == null) searched = false;

		projetoFiltro = (Projeto) getFlashAttr("projetoFiltro");
		if (projetoFiltro == null) projetoFiltro = new Projeto();

		if (!searched) listaProjetos = new ArrayList<Projeto>();
		else pesquisar();
	}

	public void pesquisar() {
		listaProjetos = projetoService.findByFilter(projetoFiltro);
		searched = true;
	}

	public void beforeRemoveProjeto() {
		if (projetosSelecionados.size() == 0) {
			RequestContext.getCurrentInstance().execute("selectAtleastOne.show()");
		} else {
			RequestContext.getCurrentInstance().execute("confirmExclusao.show()");
		}
	}

	public String remove() {

		int size = projetosSelecionados.size();
		ArrayList<Long> ids = new ArrayList<Long>(size);
		for (Projeto projeto : projetosSelecionados) {
			ids.add(projeto.getId());
		}
		try {
			projetoService.remove(ids);
			messager.info(messageProvider.getMessage("msg.success.registro.excluido", "Projeto"));
		} catch (Exception e) {
			messager.error(e.getMessage());
		}
		pesquisar();
		return irParaConsultar();
	}

	public String irParaAlterar(Projeto projeto) {

		putFlashAttr("searched", searched);
		putFlashAttr("projetoFiltro", projetoFiltro);
		try {
			projeto = projetoService.findById(projeto.getId());
			putFlashAttr("projetoSelecionado", projeto);
			return irParaAlterar();
		} catch (ApplicationException e) {
			returnErrorMessage(e.getMessage());
			return irParaConsultar();
		}
	}

	public String irParaEquipe() {
		if (projetosSelecionados.size() != 1) {
			RequestContext.getCurrentInstance().execute("selectOne.show()");
		} else {
			putFlashAttr("searched", true);
			putFlashAttr("projetoFiltro", projetoFiltro);
			putSessionAttr("projetoSelecionadoFiltro", projetosSelecionados.get(0));
			return "/paginas/equipe/cadastrarEquipe.xhtml?faces-redirect=true";
		}
		return null;
	}
	
	public List<Projeto> getListaProjetos() {
		return listaProjetos;
	}

	public void setListaProjetos(List<Projeto> listaProjetos) {
		this.listaProjetos = listaProjetos;
	}

	public List<Projeto> getProjetosSelecionados() {
		return projetosSelecionados;
	}

	public void setProjetosSelecionados(List<Projeto> projetosSelecionados) {
		this.projetosSelecionados = projetosSelecionados;
	}

}
