package com.indra.sishe.controller.sistema;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;

import com.indra.infra.resource.MessageProvider;
import com.indra.infra.service.exception.ApplicationException;
import com.indra.sishe.entity.Cargo;
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

	private List<Usuario> listaLider = new ArrayList<Usuario>();
	private List<Projeto> listaProjeto = new ArrayList<Projeto>();

	@Inject
	private UsuarioService usuarioService;

	@Inject
	private CargoService cargoService;

	@Inject
	private ProjetoService projetoService;

	public SistemaCadController() {
		// TODO Auto-generated constructor stub
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

	public String modoCadastrar() {

		if (sistemaSelecionado.getId() == null) {
			cadastrarSistema();
			return irParaConsultar();
		} else {
			alterarSindicato();
			return irParaConsultar();
		}
	}

	public String cadastrarSistema() {

		if (validarSistema(sistemaSelecionado)) {
			try {
				sistemaService.save(sistemaSelecionado);
			} catch (ApplicationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			putFlashAttr("sistemaFiltro", sistemaFiltro);
			returnInfoMessage(messageProvider.getMessage(
					"msg.success.registro.cadastrado", "Sistema"));
			putFlashAttr("searched", searched);
			return irParaConsultar();
		} else {
			returnInfoMessage(messageProvider.getMessage(
					"msg.error.registro.nao.cadastrado", "Sistema"));
			return null;
		}
	}

	public String alterarSistema() {

		if (validarSistema(sistemaSelecionado)) {
			try {
				sistemaService.update(sistemaSelecionado);
				returnInfoMessage(messageProvider.getMessage(
						"msg.success.registro.alterado", "Cliente"));
				putFlashAttr("sistemaFiltro", sistemaFiltro);
				putFlashAttr("searched", searched);
				return irParaConsultar();
			} catch (ApplicationException e) {
				returnErrorMessage(e.getMessage());
				return irParaAlterar(sistemaSelecionado);
			}
		}
		return null;
	}

	public String cadastrarSindicato() {

		if (validarSistema(sistemaSelecionado)) {
			try {
				sistemaService.save(sistemaSelecionado);
			} catch (ApplicationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			putFlashAttr("sistemaFiltro", sistemaFiltro);
			returnInfoMessage(messageProvider.getMessage(
					"msg.success.registro.cadastrado", "Sindicato"));
			putFlashAttr("searched", searched);
			return irParaConsultar();
		} else {
			returnInfoMessage(messageProvider.getMessage(
					"msg.error.registro.nao.cadastrado", "Sindicato"));
			return null;
		}
	}

	public String alterarSindicato() {

		if (validarSistema(sistemaSelecionado)) {
			try {
				sistemaService.update(sistemaSelecionado);
				returnInfoMessage(messageProvider.getMessage(
						"msg.success.registro.alterado", "Cliente"));
				putFlashAttr("clienteFiltro", sistemaFiltro);
				putFlashAttr("searched", searched);
				return irParaConsultar();
			} catch (ApplicationException e) {
				returnErrorMessage(e.getMessage());
				return irParaAlterar(sistemaSelecionado);
			}
		}
		return null;
	}

	public String cancelar() {
		putFlashAttr("searched", searched);
		putFlashAttr("sistemaFiltro", sistemaFiltro);
		return "/paginas/sistema/consultarSistema.xhtml?faces-redirect=true";
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
}
