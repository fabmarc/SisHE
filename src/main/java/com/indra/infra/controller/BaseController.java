package com.indra.infra.controller;

import javax.faces.context.FacesContext;
import javax.faces.context.Flash;
import javax.inject.Inject;

import com.indra.infra.resource.MessageProvider;
import com.indra.infra.util.FacesMessager;

public class BaseController {
	
	@Inject
	protected transient FacesMessager messager;

	@Inject
	protected transient MessageProvider messageProvider;
	
	protected void returnInfoMessage(String message) {
		messager.info(message);
		FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
	}

	protected void returnErrorMessage(String message) {
		messager.error(message);
		FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
	}

	protected void putFlashAttr(String key, Object value) {
		Flash flash = FacesContext.getCurrentInstance().getExternalContext().getFlash();
		flash.put(key, value);
	}

	protected Object getFlashAttr(String key) {
		Flash flash = FacesContext.getCurrentInstance().getExternalContext().getFlash();
		return flash.get(key);
	}
	

}
