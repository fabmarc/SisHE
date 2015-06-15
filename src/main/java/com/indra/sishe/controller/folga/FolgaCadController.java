package com.indra.sishe.controller.folga;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import com.indra.infra.resource.MessageProvider;
import com.indra.sishe.entity.Folga;
import com.indra.sishe.entity.Projeto;

@ViewScoped
@ManagedBean(name = "folgaCad")
public class FolgaCadController extends FolgaController{
	
	private static final long serialVersionUID = -3504568175362667869L;
	
	public Folga folgaSelecionada;

	@PostConstruct
	private void init() {

		MessageProvider.setInstance(messageProvider);

		searched = (Boolean) getFlashAttr("searched");
		folgaSelecionada = (Folga) getFlashAttr("folgaSelecionada");

		if (folgaSelecionada == null) folgaSelecionada = new Folga();

		folgaFiltro = (Folga) getFlashAttr("folgaFiltro");
	}

	public String cadastrarFolga(){
		try {
			if (folgaService.validarFolga(folgaSelecionada)) {
				
			}
		} catch (Exception e) {

		}
		return null;
	}
	
	
}
