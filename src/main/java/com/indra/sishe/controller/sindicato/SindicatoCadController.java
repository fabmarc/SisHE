package com.indra.sishe.controller.sindicato;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import com.indra.infra.resource.MessageProvider;
import com.indra.infra.service.exception.ApplicationException;
import com.indra.sishe.entity.Estado;
import com.indra.sishe.entity.Sindicato;

@ViewScoped
@ManagedBean(name = "sindicatoCad")
public class SindicatoCadController extends SindicatoController {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7110793257142962826L;

	/**
	 * 
	 */

	public SindicatoCadController() {
		// TODO Auto-generated constructor stub

	}

	@PostConstruct
	private void init() {

		MessageProvider.setInstance(messageProvider);

		searched = (Boolean) getFlashAttr("searched");

		sindicatoSelecionado = (Sindicato) getFlashAttr("sindicatoSelecionado");
		if (sindicatoSelecionado == null) {
			sindicatoSelecionado = new Sindicato();
			sindicatoSelecionado.setEstado(new Estado());
			 
			
		}
		sindicatoFiltro = (Sindicato) getFlashAttr("sindicatoFiltro");
		setListaEstado(estadoService.findAll());
	}

	public String cadastrarSindicato() {

		if (validarSindicato(sindicatoSelecionado)) {
			try {
				sindicatoService.save(sindicatoSelecionado);
			} catch (ApplicationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			putFlashAttr("sindicatoFiltro", sindicatoFiltro);
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

		if (validarSindicato(sindicatoSelecionado)) {
			try {
				sindicatoService.update(sindicatoSelecionado);
				returnInfoMessage(messageProvider.getMessage(
						"msg.success.registro.alterado", "Cliente"));
				putFlashAttr("clienteFiltro", sindicatoFiltro);
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
		return "/paginas/sindicato/consultarSindicato.xhtml?faces-redirect=true";
	}

	public Sindicato getSindicatoFiltro() {
		return sindicatoFiltro;
	}

	public void setSindicatoFiltro(Sindicato clienteFiltro) {
		this.sindicatoFiltro = clienteFiltro;
	}

	public Sindicato getSindicatoSelecionado() {
		return sindicatoSelecionado;
	}

	public void setSindicatoSelecionado(Sindicato clienteSelecionado) {
		this.sindicatoSelecionado = clienteSelecionado;
	}

	public boolean wasSearched() {
		return searched;
	}

	public void setPesquisar(boolean pesquisar) {
		this.searched = pesquisar;
	}

	/*public List<Estado> obterEstados() {
		setListaEstado(estadoService.findAll());
		return estadoService.findAll();
		
	}*/

	public String modoCadastrar() {

		if (sindicatoSelecionado.getId() == null) {
			cadastrarSindicato();
			return irParaConsultar();
		} else {			
			alterarSindicato();
			return irParaConsultar();
		}
	}

}
