package com.indra.sishe.controller.usuarioProjeto;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.primefaces.context.RequestContext;

import com.indra.infra.resource.MessageProvider;
import com.indra.infra.service.exception.ApplicationException;
import com.indra.sishe.entity.Projeto;
import com.indra.sishe.entity.UsuarioProjeto;

@ViewScoped
@ManagedBean(name = "usuarioProjetoMnt")
public class UsuarioProjetoMntController extends UsuarioProjetoController {

	private static final long serialVersionUID = -6925459169473948818L;	
	private List<UsuarioProjeto> usuariosProjetos = new ArrayList<UsuarioProjeto>();
		
	@PostConstruct
	private void init() {

		MessageProvider.setInstance(messageProvider);
		searched = (Boolean) getFlashAttr("searched");

		if (searched == null) searched = false;

		usuarioProjetoFiltro = (UsuarioProjeto) getFlashAttr("usuarioProjetoFiltro");

		if (usuarioProjetoFiltro == null) usuarioProjetoFiltro = new UsuarioProjeto();
		if (!searched) listaUsuarioProjeto = new ArrayList<UsuarioProjeto>();
		else pesquisar();

		projetoSelecionado = (Projeto) getSessionAttr("projetoSelecionadoFiltro");
		
		if(projetoSelecionado != null && projetoSelecionado.getId() != null){
			UsuarioProjeto up = new UsuarioProjeto();
			up.setProjeto(projetoSelecionado);
			listarUsuariosDoProjeto(up);
			putSessionAttr("usuarioProjetoSelecionado", up );
		}		
	}

	public void beforeRemoveUsuarioProjeto() {		
		if (usuariosProjetos.size() == 0) {
			RequestContext.getCurrentInstance().execute("selectAtleastOne.show()");
		} else {
			RequestContext.getCurrentInstance().execute("confirmExclusao.show()");
		}
	}	
	
	public void pesquisar() {
		usuarioProjetoFiltro.setProjeto(projetoSelecionado);
		listaUsuarioProjeto = usuarioProjetoService.findByProjeto(usuarioProjetoFiltro);
		Collections.sort(listaUsuarioProjeto);
		searched = true;
	}

	public String removerUsuarioDaEquipe() {
		int size = usuariosProjetos.size();
		ArrayList<Long> ids = new ArrayList<Long>(size);
		for (UsuarioProjeto usuarioProjeto : usuariosProjetos)
			ids.add(usuarioProjeto.getId());
		try {
			usuarioProjetoService.remove(ids);
			messager.info(messageProvider.getMessage("msg.success.registro.excluido", "Usuário"));
		} catch (ApplicationException e) {
			messager.error(e.getMessage());
		}
		pesquisar();
		return irParaConsultar();
	}
	
		
	public String voltarParaProjeto() {		
		return "/paginas/projeto/consultarProjeto.xhtml?faces-redirect=true";
	}
	
	public List<UsuarioProjeto> listarUsuariosDoProjeto(UsuarioProjeto usuarioProjeto){		
		listaUsuarioProjeto = usuarioProjetoService.findByProjeto(usuarioProjeto);
		return listaUsuarioProjeto;
	}
		
	public List<UsuarioProjeto> getListaUsuarioProjeto() {
		return listaUsuarioProjeto;
	}

	public void setListaUsuarioProjeto(List<UsuarioProjeto> listaUsuarioProjeto) {
		this.listaUsuarioProjeto = listaUsuarioProjeto;
	}

	public List<UsuarioProjeto> getUsuariosProjetos() {
		return usuariosProjetos;
	}

	public void setUsuariosProjetos(List<UsuarioProjeto> usuariosProjetos) {
		this.usuariosProjetos = usuariosProjetos;
	}

	public UsuarioProjeto getUsuarioProjetoFiltro() {
		return usuarioProjetoFiltro;
	}

	public void setUsuarioProjeto(List<UsuarioProjeto> usuarioProjetoFiltro) {
		this.usuarioProjetoFiltro = (UsuarioProjeto) usuarioProjetoFiltro;
	}

	
}
