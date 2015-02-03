package com.indra.sishe.controller.sistema;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;

import com.indra.infra.resource.MessageProvider;
import com.indra.infra.service.exception.ApplicationException;
import com.indra.sishe.entity.Projeto;
import com.indra.sishe.entity.Sistema;
import com.indra.sishe.entity.Usuario;
import com.indra.sishe.service.CargoService;
import com.indra.sishe.service.ProjetoService;
import com.indra.sishe.service.UsuarioService;

@ViewScoped
@ManagedBean(name = "sistemaCad")
public class SistemaCadController extends SistemaController {

	private static final long serialVersionUID = -3214994834298229437L;
	
	@Inject
	private UsuarioService usuarioService;

	@Inject
	private CargoService cargoService;

	@Inject
	private ProjetoService projetoService;

	public SistemaCadController() {

	}

	@PostConstruct
	private void init() {

		MessageProvider.setInstance(messageProvider);

		searched = (Boolean) getFlashAttr("searched");

		sistemaSelecionado = (Sistema) getFlashAttr("sistemaSelecionado");
		if (sistemaSelecionado == null) {
			sistemaSelecionado = new Sistema();

		}
		sistemaFiltro = (Sistema) getFlashAttr("sistemaFiltro");
		listarLideres();
		listarProjeto();
	}

	public List<Usuario> listarLideres() {

		listaLideres = usuarioService.findByCargo("ROLE_LIDER, ROLE_FUNCIONARIO");
		return listaLideres;
	}

	public List<Projeto> listarProjeto() {
		listaProjetos = projetoService.findAll();
		return listaProjetos;
	}

	private boolean modoCadastrar() {
		if (sistemaSelecionado == null || sistemaSelecionado.getId() == null) {
			return true;
		} else {
			return false;
		}
	}

	public String confirmar() {
		if (modoCadastrar()) {
			return cadastrarSistema();
		} else {
			return alterarSistema();
		}
	}

	public String cadastrarSistema() {

		try {
			this.sistemaSelecionado = sistemaService.save(sistemaSelecionado);
			putFlashAttr("sistemaFiltro", sistemaFiltro);
			returnInfoMessage(messageProvider.getMessage("msg.success.registro.cadastrado", "Sistema"));
			putFlashAttr("searched", searched);
			return irParaConsultar();
		} catch (ApplicationException e) {
			returnErrorMessage(e.getMessage());
		}

		return null;
	}

	public String alterarSistema() {

		try {
			sistemaService.update(sistemaSelecionado);
			returnInfoMessage(messageProvider.getMessage("msg.success.registro.alterado", "Sistema"));
			putFlashAttr("sistemaFiltro", sistemaFiltro);
			putFlashAttr("searched", searched);
			return irParaConsultar();
		} catch (ApplicationException e) {
			returnErrorMessage(e.getMessage());
			return irParaAlterar(sistemaSelecionado);
		}
	}

	public String cancelar() {
		putFlashAttr("searched", searched);
		putFlashAttr("sistemaFiltro", sistemaFiltro);
		return "/paginas/sistema/consultarSistema.xhtml?faces-redirect=true";
	}

	public List<Usuario> getListaLider() {
		return listaLideres;
	}

	public void setListaLideres(List<Usuario> listaLideres) {
		this.listaLideres = listaLideres;
	}

	public List<Projeto> getListaProjetos() {
		return listaProjetos;
	}

	public void setListaProjetos(List<Projeto> listaProjetos) {
		this.listaProjetos = listaProjetos;
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
}
