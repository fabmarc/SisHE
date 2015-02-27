package com.indra.sishe.controller.sindicato;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import com.indra.infra.resource.MessageProvider;
import com.indra.infra.service.exception.ApplicationException;
import com.indra.sishe.entity.Estado;
import com.indra.sishe.entity.Sindicato;
import com.indra.sishe.enums.EstadoEnum;

@ViewScoped
@ManagedBean(name = "sindicatoCad")
public class SindicatoCadController extends SindicatoController {

	
	private static final long serialVersionUID = -7110793257142962826L;
	
	public SindicatoCadController() {
		
	}

	@PostConstruct
	private void init() {

		MessageProvider.setInstance(messageProvider);

		searched = (Boolean) getFlashAttr("searched");

		sindicatoSelecionado = (Sindicato) getFlashAttr("sindicatoSelecionado");
		if (sindicatoSelecionado == null) {
			sindicatoSelecionado = new Sindicato();				
		}
		sindicatoFiltro = (Sindicato) getFlashAttr("sindicatoFiltro");
		setListaEstado(EstadoEnum.listaEstados());
	}

	public String cadastrarSindicato() {
		try {
			this.sindicatoSelecionado = sindicatoService.save(sindicatoSelecionado);
			putFlashAttr("sindicatoFiltro", sindicatoFiltro);
			returnInfoMessage(messageProvider.getMessage("msg.success.registro.cadastrado", "Sindicato"));
			putFlashAttr("searched", searched);
			return irParaConsultar();
		} catch (ApplicationException e) {
			returnErrorMessage(e.getMessage());
		}
		return null;
	}
	
	

	public String alterarSindicato() {

		if (validarSindicato(sindicatoSelecionado)) {
			try {
				sindicatoService.update(sindicatoSelecionado);
				returnInfoMessage(messageProvider.getMessage(
						"msg.success.registro.alterado", "Sindicato"));
				putFlashAttr("sindicatoFiltro", sindicatoFiltro);
				putFlashAttr("searched", searched);
				return irParaConsultar();
			} catch (ApplicationException e) {
				returnErrorMessage(e.getMessage());
				return irParaAlterar(sindicatoSelecionado);
			}
		}
		return null;
	}

	public String cancelar() {
		putFlashAttr("searched", searched);
		putFlashAttr("sindicatoFiltro", sindicatoFiltro);
		putFlashAttr("sindicatoSelecionado", null);
		return "/paginas/sindicato/consultarSindicato.xhtml?faces-redirect=true";
	}

	public Sindicato getSindicatoFiltro() {
		return sindicatoFiltro;
	}

	public void setSindicatoFiltro(Sindicato sindicatoFiltro) {
		this.sindicatoFiltro = sindicatoFiltro;
	}

	public Sindicato getSindicatoSelecionado() {
		return sindicatoSelecionado;
	}

	public void setSindicatoSelecionado(Sindicato sindicatoSelecionado) {
		this.sindicatoSelecionado = sindicatoSelecionado;
	}

	public boolean wasSearched() {
		return searched;
	}

	public void setPesquisar(boolean pesquisar) {
		this.searched = pesquisar;
	}	
	
	public String confirmar() {
		if (modoCadastrar()) {
			return cadastrarSindicato();
		} else {
			return alterarSindicato();
		}
	}

	public boolean modoCadastrar() {
		if (sindicatoSelecionado == null || sindicatoSelecionado.getId() == null) {			
			return true;
		} else {						
			return false;
		}
	}

}
