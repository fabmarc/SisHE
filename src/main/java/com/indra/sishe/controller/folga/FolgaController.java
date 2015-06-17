package com.indra.sishe.controller.folga;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;

import com.indra.infra.controller.BaseController;
import com.indra.sishe.entity.Folga;
import com.indra.sishe.service.FolgaService;

public class FolgaController extends BaseController implements Serializable{

	private static final long serialVersionUID = -4667670116817828607L;
	
	@Inject
	protected transient FolgaService folgaService;
	
	public Folga folgaFiltro;
	
	protected Boolean searched;
	
	public String irParaConsultar() {
		return "/paginas/folga/consultarFolga.xhtml?faces-redirect=true";
	}
	
	public String irParaCadastrar(){
		putFlashAttr("searched", this.searched);
		putFlashAttr("folgaFiltro", this.folgaFiltro);
		putFlashAttr("folgaSelecionada", null);
		return "/paginas/folga/cadastrarFolga.xhtml?faces-redirect=true";
	}
	

	public String irParaAlterar(Folga folgaSelecionada){
		putFlashAttr("searched", this.searched);
		putFlashAttr("folgaFiltro", this.folgaFiltro);
		putFlashAttr("folgaSelecionada", folgaSelecionada);
		return "/paginas/folga/cadastrarFolga.xhtml?faces-redirect=true";
	}

	public Folga getFolgaFiltro() {
		return folgaFiltro;
	}
	
	public void setFolgaFiltro(Folga folgaFiltro) {
		this.folgaFiltro = folgaFiltro;
	}
}
