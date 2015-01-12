package com.indra.sishe.controller.usuario;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import com.indra.infra.resource.MessageProvider;
import com.indra.infra.service.exception.ApplicationException;
import com.indra.sishe.entity.Usuario;

@ViewScoped
@ManagedBean(name="usuarioMnt")
public class UsuarioMntController extends UsuarioController {

	private static final long serialVersionUID = 4228136065932205095L;
	
	private List<Usuario> listaUsuarios;

	public UsuarioMntController() {
	}
	
	@PostConstruct
	public void init(){
		MessageProvider.setInstance(messageProvider);

		searched = (Boolean) getFlashAttr("searched");
		if (searched == null) searched = false;
		
		usuarioFiltro = (Usuario) getFlashAttr("usuarioFiltro");
		if (usuarioFiltro == null) usuarioFiltro = new Usuario();
		
		if (!searched) listaUsuarios = new ArrayList<Usuario>();
		else pesquisar();	
	}
	
	public void pesquisar() {
		listaUsuarios = usuarioService.findByFilter(usuarioFiltro);
		Collections.sort(listaUsuarios);
		searched = true;
	}
	
	public String removerUsuario() {
		String nome = usuarioSelecionado.getNome();
		try {
			usuarioService.remove(usuarioSelecionado.getId());
			messager.info(messageProvider.getMessage("msg.success.registro.excluido", "Usuário", nome));
		} catch (ApplicationException e) {
			messager.error(e.getMessage());
		}
		pesquisar();
		return irParaConsultar();
	}
	
	public String irParaAlterar(Usuario usuarioSelecionado) {
		putFlashAttr("searched", this.searched);
		putFlashAttr("usuarioFiltro", this.usuarioFiltro);
		try {
			usuarioSelecionado = usuarioService.findById(usuarioSelecionado.getId());
			putFlashAttr("usuarioSelecionado", usuarioSelecionado);
			return irParaAlterar();
		} catch (ApplicationException e) {
			returnErrorMessage(e.getMessage());
			return irParaConsultar();
		}
	}

	public List<Usuario> getListaUsuarios() {
		return listaUsuarios;
	}

	public void setListaUsuarios(List<Usuario> listaUsuarios) {
		this.listaUsuarios = listaUsuarios;
	}
}
