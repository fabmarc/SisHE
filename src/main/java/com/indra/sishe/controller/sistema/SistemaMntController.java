package com.indra.sishe.controller.sistema;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;

import org.primefaces.context.RequestContext;

import com.indra.infra.resource.MessageProvider;
import com.indra.infra.service.exception.ApplicationException;
import com.indra.sishe.entity.Cargo;
import com.indra.sishe.entity.Projeto;
import com.indra.sishe.entity.Sistema;
import com.indra.sishe.entity.Usuario;
import com.indra.sishe.service.CargoService;
import com.indra.sishe.service.ProjetoService;
import com.indra.sishe.service.UsuarioService;

@SuppressWarnings("serial")
@ViewScoped
@ManagedBean(name = "sistemaMnt")
public class SistemaMntController extends SistemaController {

	private List<Sistema> listaSistema;
	private List<Usuario> listaLider = new ArrayList<Usuario>();
	private List<Projeto> listaProjeto = new ArrayList<Projeto>();

	@Inject
	private UsuarioService usuarioService;

	@Inject
	private CargoService cargoService;

	@Inject
	private ProjetoService projetoService;

	@SuppressWarnings("unused")
	private List<Sistema> sistemasSelecionados;

	public SistemaMntController() {
		
	}

	@PostConstruct
	private void init() {

		MessageProvider.setInstance(messageProvider);

		searched = (Boolean) getFlashAttr("searched");
		if (searched == null)
			searched = false;

		sistemaFiltro = (Sistema) getFlashAttr("sistemaFiltro");
		if (sistemaFiltro == null)
			sistemaFiltro = new Sistema();
		if (!searched)
			listaSistema = new ArrayList<Sistema>();
		else
			pesquisar();

		listarLideres();
		listarProjeto();
	}

	public void pesquisar() {
		listaSistema = sistemaService.findByFilter(sistemaFiltro);
		Collections.sort(listaSistema);
		searched = true;
	}

	public void beforeRemoveSistema() {

		if (sistemasSelecionados.size() == 0) {
			RequestContext.getCurrentInstance().execute(
					"selectAtleastOne.show()");
		} else {
			RequestContext.getCurrentInstance().execute(
					"confirmExclusao.show()");
		}
	}

	public String irParaAlterar(Sistema sistemaSelecionado) {
		putFlashAttr("searched", searched);
		putFlashAttr("sistemaFiltro", sistemaFiltro);
		try {
			sistemaSelecionado = sistemaService.findById(sistemaSelecionado
					.getId());
			putFlashAttr("sistemaSelecionado", sistemaSelecionado);
			return "/paginas/sistema/cadastrarSistema.xhtml?faces-redirect=true";
		} catch (ApplicationException e) {
			returnErrorMessage(e.getMessage());
			return irParaConsultar();
		}
	}

	public String removerSistema() {
		int size = sistemasSelecionados.size();
		ArrayList<Long> ids = new ArrayList<Long>(size);
		for (Sistema sistema : sistemasSelecionados)
			ids.add(sistema.getId());
		try {
			sistemaService.remove(ids);
			messager.info(messageProvider.getMessage(
					"msg.success.registro.excluido", "sistema"));
		} catch (ApplicationException e) {
			messager.error(e.getMessage());
		}
		pesquisar();
		return irParaConsultar();
	}

	public List<Usuario> listarLideres() {

		Cargo cargo = new Cargo();
		cargo.setNome("Lider");
		cargo = cargoService.findByFilter(cargo).get(0);
		listaLider = usuarioService.findByCargo(cargo);

		return listaLider;
	}

	public List<Projeto> listarProjeto() {
		listaProjeto = projetoService.findAll();
		return listaProjeto;
	}

	public List<Sistema> getListaSistema() {
		return listaSistema;
	}

	public void setListaSistema(List<Sistema> listaSistema) {
		this.listaSistema = listaSistema;
	}

	public List<Usuario> getListaLider() {
		return listaLider;
	}

	public void setListaLider(List<Usuario> listaLider) {
		this.listaLider = listaLider;
	}

	public List<Projeto> getListaProjeto() {
		return listaProjeto;
	}

	public void setListaProjeto(List<Projeto> listaProjeto) {
		this.listaProjeto = listaProjeto;
	}

	public Sistema getSistemaFiltro() {
		return sistemaFiltro;
	}

	public void setSistemaFiltro(Sistema sistemaFiltro) {
		this.sistemaFiltro = sistemaFiltro;
	}

	public Sistema getSistemaSelecionado() {
		return sistemaSelecionado;
	}

	public void setSistemaSelecionado(Sistema sistemaSelecionado) {
		this.sistemaSelecionado = sistemaSelecionado;
	}

	public List<Sistema> getSistemasSelecionados() {
		return sistemasSelecionados;
	}

	public void setSistemasSelecionados(List<Sistema> sistemasSelecionados) {
		this.sistemasSelecionados = sistemasSelecionados;
	}
}
