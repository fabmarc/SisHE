package com.indra.sishe.controller.sistema;

import javax.annotation.PostConstruct;

import com.indra.infra.resource.MessageProvider;
import com.indra.infra.service.exception.ApplicationException;
import com.indra.sishe.entity.Estado;
import com.indra.sishe.entity.Sindicato;
import com.indra.sishe.entity.Sistema;
import com.indra.sishe.entity.Usuario;

public class SistemaCadController extends SistemaController {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3214994834298229437L;

	public SistemaCadController() {
		// TODO Auto-generated constructor stub
	}

	@PostConstruct
	private void init() {

		MessageProvider.setInstance(messageProvider);

		searched = (Boolean) getFlashAttr("searched");

		sistemaSelecionado = (Sistema) getFlashAttr("sistemaSelecionado");
		if (sistemaSelecionado == null) {
			sistemaSelecionado = new Sistema();
			sistemaSelecionado.setUsuario(new Usuario());

		}
		sistemaFiltro = (Sistema) getFlashAttr("sistemaFiltro");
		
	}
	
	public String cadastrarSindicato() {

		if (validarSistema(sistemaSelecionado)) {
			try {
				sistemaService.save(sistemaSelecionado);
			} catch (ApplicationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			putFlashAttr("sistemaFiltro", sistemaFiltro);
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

		if (validarSistema(sistemaSelecionado)) {
			try {
				sistemaService.update(sistemaSelecionado);
				returnInfoMessage(messageProvider.getMessage(
						"msg.success.registro.alterado", "Cliente"));
				putFlashAttr("clienteFiltro", sistemaFiltro);
				putFlashAttr("searched", searched);
				return irParaConsultar();
			} catch (ApplicationException e) {
				returnErrorMessage(e.getMessage());
				return irParaAlterar(sistemaSelecionado);
			}
		}
		return null;
	}
	
	public String cancelar() {
		putFlashAttr("searched", searched);
		putFlashAttr("sistemaFiltro", sistemaFiltro);
		return "/paginas/sistema/consultarSistema.xhtml?faces-redirect=true";
	}

	public Sistema getSistemaFiltro() {
		return sistemaFiltro;
	}

	public void setSistemaFiltro(Sistema sistemaFiltro) {
		this.sistemaFiltro = sistemaFiltro;
	}

	public Sistema getSindicatoSelecionado() {
		return sistemaSelecionado;
	}

	public void setSindicatoSelecionado(Sistema sistemaSelecionado) {
		this.sistemaSelecionado = sistemaSelecionado;
	}
}
