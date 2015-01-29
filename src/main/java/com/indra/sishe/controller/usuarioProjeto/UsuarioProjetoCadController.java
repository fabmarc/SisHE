package com.indra.sishe.controller.usuarioProjeto;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import com.indra.infra.resource.MessageProvider;
import com.indra.infra.service.exception.ApplicationException;
import com.indra.sishe.entity.Cargo;
import com.indra.sishe.entity.Projeto;
import com.indra.sishe.entity.Usuario;
import com.indra.sishe.entity.UsuarioProjeto;

@ViewScoped
@ManagedBean(name = "usuarioProjetoCad")
public class UsuarioProjetoCadController extends usuarioProjetoController {

	private static final long serialVersionUID = 5412878556555830463L;

	@PostConstruct
	private void init() {
		MessageProvider.setInstance(messageProvider);
		searched = (Boolean) getFlashAttr("searched");
		usuarioProjetoSelecionado = (UsuarioProjeto) getFlashAttr("usuarioProjetoSelecionado");

		if (usuarioProjetoSelecionado == null) {
			usuarioProjetoSelecionado = new UsuarioProjeto();
		}

		usuarioProjetoFiltro = (UsuarioProjeto) getFlashAttr("usuarioProjetoFiltro");
		listarUsuarios();
		listarProjetos();
	}

	private List<Usuario> listarUsuarios() {
		listaUsuarios = usuarioService.findAll();
		return listaUsuarios;
	}

	public List<Projeto> listarProjetos() {
		listaProjetos = projetoService.findAll();
		return listaProjetos;
	}

	public List<Usuario> listarUsuariosPorCargo(String nomeCargo) {
		Cargo cargo = new Cargo();
		cargo.setNome(nomeCargo);
		cargo = cargoService.findByFilter(cargo).get(0);
		listaUsuarios = usuarioService.findByCargo(cargo);
		return listaUsuarios;
	}

	private boolean modoCadastrar() {
		if (usuarioProjetoSelecionado == null || usuarioProjetoSelecionado.getId() == null) {
			return true;
		} else {
			return false;
		}
	}

	public String confirmar() {
		if (modoCadastrar()) {
			return cadastrarUsuarioProjeto();
		} else {
			return alterarUsuarioProjeto();
		}
	}

	public String cadastrarUsuarioProjeto() {
		try {
			this.usuarioProjetoSelecionado = usuarioProjetoService.save(usuarioProjetoSelecionado);
			putFlashAttr("usuarioProjetoFiltro", usuarioProjetoFiltro);
			returnInfoMessage(messageProvider.getMessage("msg.success.registro.cadastrado", "Equipe"));
			putFlashAttr("searched", searched);
			return irParaConsultar();
		} catch (ApplicationException e) {
			returnErrorMessage(e.getMessage());
		}
		return null;
	}

	public String alterarUsuarioProjeto() {
		try {
			usuarioProjetoService.update(usuarioProjetoSelecionado);
			returnInfoMessage(messageProvider.getMessage("msg.success.registro.alterado", "Equipe"));
			putFlashAttr("usuarioProjetoFiltro", usuarioProjetoFiltro);
			putFlashAttr("searched", searched);
			return irParaConsultar();
		} catch (ApplicationException e) {
			returnErrorMessage(e.getMessage());
			return irParaAlterar(usuarioProjetoSelecionado);
		}
	}

	public List<Usuario> getListaUsuarios() {
		return listaUsuarios;
	}

	public void setListaUsuarios(List<Usuario> listaUsuarios) {
		this.listaUsuarios = listaUsuarios;
	}

	public List<Projeto> getListaProjetos() {
		return listaProjetos;
	}

	public void setListaProjetos(List<Projeto> listaProjetos) {
		this.listaProjetos = listaProjetos;
	}

	public UsuarioProjeto getUsuarioProjetoFiltro() {
		return usuarioProjetoFiltro;
	}

	public void setUsuarioProjetoFiltro(UsuarioProjeto usuarioProjetoFiltro) {
		this.usuarioProjetoFiltro = usuarioProjetoFiltro;
	}

	public UsuarioProjeto getUsuarioProjetoSelecionado() {
		return usuarioProjetoSelecionado;
	}

	public void setUsuarioProjetoSelecionado(UsuarioProjeto usuarioProjetoSelecionado) {
		this.usuarioProjetoSelecionado = usuarioProjetoSelecionado;
	}

}
