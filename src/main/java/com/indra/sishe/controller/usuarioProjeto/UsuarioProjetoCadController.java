package com.indra.sishe.controller.usuarioProjeto;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import com.indra.infra.resource.MessageProvider;
import com.indra.infra.service.exception.ApplicationException;
import com.indra.sishe.entity.Projeto;
import com.indra.sishe.entity.Usuario;
import com.indra.sishe.entity.UsuarioProjeto;

@ViewScoped
@ManagedBean(name = "usuarioProjetoCad")
public class UsuarioProjetoCadController extends UsuarioProjetoController {

	private static final long serialVersionUID = 5412878556555830463L;
	private List<UsuarioProjeto> usuariosProjetos = new ArrayList<UsuarioProjeto>();

	@PostConstruct
	private void init() {
		MessageProvider.setInstance(messageProvider);
		searched = (Boolean) getFlashAttr("searched");
		usuarioProjetoSelecionado = (UsuarioProjeto) getSessionAttr("usuarioProjetoSelecionado");

		if (usuarioProjetoSelecionado == null) {
			usuarioProjetoSelecionado = new UsuarioProjeto();
		}

		usuarioProjetoFiltro = (UsuarioProjeto) getFlashAttr("usuarioProjetoFiltro");		
		listarUsuarios(usuarioProjetoSelecionado);
		
	}

	public void UsuarioProjetoMntController(){
		
	}
	
	public List<UsuarioProjeto> listarUsuarios(UsuarioProjeto usuarioProjeto) {
		listaUsuarioProjeto = usuarioProjetoService.findUserNotInProjeto(usuarioProjeto);
		return listaUsuarioProjeto;
	}

	

	public boolean modoCadastrar() {
		if (usuarioProjetoSelecionado == null || usuarioProjetoSelecionado.getId() == null) {			
			cadastrarUsuarioProjeto(usuariosProjetos);
			return true;
		} else {
			return false;
		}
	}

/*	public String confirmar() {
		if (modoCadastrar()) {
			return cadastrarUsuarioProjeto();
		} else {
			return alterarUsuarioProjeto();
		}
	}*/

	public String cadastrarUsuarioProjeto(List<UsuarioProjeto> usuariosProjetos) {
		usuarioProjetoService.salvar(usuariosProjetos);
		putFlashAttr("usuarioProjetoFiltro", usuarioProjetoFiltro);
		returnInfoMessage(messageProvider.getMessage("msg.success.registro.cadastrado", "Equipe"));
		putFlashAttr("searched", searched);
		return irParaConsultar();
		
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
	
	public List<UsuarioProjeto> getListaUsuarioProjeto() {
		return  listaUsuarioProjeto;
	}

	public List<UsuarioProjeto> getUsuariosProjetos() {
		return usuariosProjetos;
	}

	public void setUsuariosProjetos(List<UsuarioProjeto> usuariosProjetos) {
		this.usuariosProjetos = usuariosProjetos;
	}
	
	
}
