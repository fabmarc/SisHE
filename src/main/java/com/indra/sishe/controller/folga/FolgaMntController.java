package com.indra.sishe.controller.folga;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import com.indra.sishe.controller.usuario.UsuarioLogado;
import com.indra.sishe.entity.Folga;

@ViewScoped
@ManagedBean(name = "folgaMnt")
public class FolgaMntController extends FolgaController implements Serializable{

	private static final long serialVersionUID = 7932650096539006690L;
	
	private Folga folgaFiltro;
	
	private List<Folga> listaFolgas;
	
	private Boolean todasSolicitacoes;
	
	@PostConstruct
	private void init(){
		folgaFiltro = (Folga) getFlashAttr("folgaFiltro");
		if (folgaFiltro == null) {
			folgaFiltro = new Folga();
		}
	}
	
	@Override
	public String pesquisar() {
		putFlashAttr("folgaFiltro", folgaFiltro);
		if (UsuarioLogado.getPermissoes().contains("ROLE_GERENTE")) { // Gerente consulta folga de recursos de todos do Projeto dele
			pesquisarGerente();
		} else if (UsuarioLogado.getPermissoes().contains("ROLE_LIDER")) {
			pesquisarLider();
		} else { 
			pesquisarPorUsuario();
		}
		return "";
	}
	
	private void pesquisarGerente(){
		listaFolgas = folgaService.findFolgasByGerente(folgaFiltro);
	}
	                               
	
	private void pesquisarLider() {
		if (todasSolicitacoes) {
			folgaService.findFolgasBylider(folgaFiltro);
		}else {
			pesquisarPorUsuario();
		}
	}
	
	private void pesquisarPorUsuario(){
		listaFolgas = folgaService.findFolgaByUsuario(folgaFiltro);
	}
	

	public Folga getFolgaFiltro() {
		return folgaFiltro;
	}

	public void setFolgaFiltro(Folga folgaFiltro) {
		this.folgaFiltro = folgaFiltro;
	}

	public List<Folga> getListaFolgas() {
		return listaFolgas;
	}

	public void setListaFolgas(List<Folga> listaFolgas) {
		this.listaFolgas = listaFolgas;
	}

	public Boolean getTodasSolicitacoes() {
		return todasSolicitacoes;
	}

	public void setTodasSolicitacoes(Boolean todasSolicitacoes) {
		this.todasSolicitacoes = todasSolicitacoes;
	}
	

}
