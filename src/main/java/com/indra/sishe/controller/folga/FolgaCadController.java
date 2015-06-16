package com.indra.sishe.controller.folga;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import com.indra.infra.resource.MessageProvider;
import com.indra.infra.service.exception.ApplicationException;
import com.indra.sishe.entity.Folga;

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

	public String cadastrarFolga() {
		try {
			if (folgaService.validarFolga(folgaSelecionada)) {
				folgaService.save(folgaSelecionada);
				putFlashAttr("folgaFiltro", folgaFiltro);
				returnInfoMessage(messageProvider.getMessage("msg.success.registro.cadastrado", "Solicitação de Folga"));
				putFlashAttr("searched", searched);
				return irParaConsultar();
			}
		} catch (ApplicationException e) {
			returnErrorMessage(e.getMessage());
		}
		return null;
	}
	
	public String confirmar() {
		if (modoCadastrar()) {
			return cadastrarFolga();
		} else {
			return alterarFolga();
		}
	}
	
	public String alterarFolga(){
		try {
			folgaService.update(folgaSelecionada);
			returnInfoMessage(messageProvider.getMessage("msg.success.registro.alterado", "Solicitação de Folga"));
			putFlashAttr("searched", searched);
			putFlashAttr("folgaFiltro", folgaFiltro);
			return irParaConsultar();
		} catch (ApplicationException e) {
			returnErrorMessage(e.getMessage());
			return irParaAlterar(folgaSelecionada);
		}
	}
	
	public Boolean modoCadastrar(){
		if (folgaSelecionada.equals(new Folga())) {
			return true;
		} else {
			return false;
		}
	}
	
	public String cancelar(){
		putFlashAttr("searched", searched);
		putFlashAttr("folgaFiltro", folgaFiltro);
		return irParaConsultar();
	}
	
}
