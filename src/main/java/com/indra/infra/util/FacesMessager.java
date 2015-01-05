package com.indra.infra.util;

import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

/**
 * Classe utilitaria para gerencimento de mensagens <br>
 * do JSF.
 * 
 */
@RequestScoped
public class FacesMessager {

	@Inject
	protected FacesContext facesContext;

	// ************************************************************************************************
	// Metodos de Mensagem
	// ************************************************************************************************

	/**
	 * Gera Mensagem de informacao para o usuario.
	 * 
	 * @param String
	 *            - Mensagem a ser exibida.
	 */
	public void info(String message) {
		facesContext.addMessage(null, new FacesMessage(
				FacesMessage.SEVERITY_INFO, null, message));
	}

	/**
	 * Gera Mensagem de informacao para o usuario associando esta mensagem a um
	 * componente.
	 * 
	 * @param UIComponent
	 *            - Componente a ser associado a mensagem. String - Mensagem a
	 *            ser exibida.
	 */
	public void info(UIComponent component, String message) {
		facesContext.addMessage(component.getClientId(facesContext),
				new FacesMessage(FacesMessage.SEVERITY_INFO, null, message));
	}

	/**
	 * Gera Mensagem de erro para o usuario.
	 * 
	 * @param String
	 *            - Mensagem a ser exibida.
	 */
	public void error(String message) {
		facesContext.addMessage(null, new FacesMessage(
				FacesMessage.SEVERITY_ERROR, null, message));
	}

	/**
	 * Gera Mensagem de erro para o usuario associando esta mensagem a um
	 * componente.
	 * 
	 * @param UIComponent
	 *            - Componente a ser associado a mensagem. String - Mensagem a
	 *            ser exibida.
	 */
	public void error(UIComponent component, String message) {
		facesContext.addMessage(component.getClientId(facesContext),
				new FacesMessage(FacesMessage.SEVERITY_ERROR, null, message));
	}

	/**
	 * Gera Mensagem de erro fatal para o usuario.
	 * 
	 * @param String
	 *            - Mensagem a ser exibida.
	 */
	public void fatal(String message) {
		facesContext.addMessage(null, new FacesMessage(
				FacesMessage.SEVERITY_FATAL, null, message));
	}

	/**
	 * Gera Mensagem de erro fatal para o usuario associando esta mensagem a um
	 * componente.
	 * 
	 * @param UIComponent
	 *            - Componente a ser associado a mensagem. String - Mensagem a
	 *            ser exibida.
	 */
	public void fatal(UIComponent component, String message) {
		facesContext.addMessage(component.getClientId(facesContext),
				new FacesMessage(FacesMessage.SEVERITY_FATAL, null, message));
	}

	/**
	 * Gera Mensagem de aviso para o usuario.
	 * 
	 * @param String
	 *            - Mensagem a ser exibida.
	 */
	public void warn(String message) {
		facesContext.addMessage(null, new FacesMessage(
				FacesMessage.SEVERITY_WARN, null, message));
	}

	/**
	 * Gera Mensagem de aviso para o usuario associando esta mensagem a um
	 * componente.
	 * 
	 * @param UIComponent
	 *            - Componente a ser associado a mensagem. String - Mensagem a
	 *            ser exibida.
	 */
	public void warn(UIComponent component, String message) {
		facesContext.addMessage(component.getClientId(facesContext),
				new FacesMessage(FacesMessage.SEVERITY_WARN, null, message));
	}
}
