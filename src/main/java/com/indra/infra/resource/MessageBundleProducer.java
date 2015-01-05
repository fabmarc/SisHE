package com.indra.infra.resource;

import java.util.Locale;
import java.util.ResourceBundle;

import javax.enterprise.inject.Produces;
import javax.faces.context.FacesContext;
 
public class MessageBundleProducer {
 
	private ResourceBundle bundle;
 
	@Produces
	@MessageBundle
	public ResourceBundle getBundle() {
		
		if (this.bundle == null) {
			FacesContext context = FacesContext.getCurrentInstance();
			Locale locale = context.getViewRoot().getLocale();
			bundle = ResourceBundle.getBundle("messages", locale);
		}
		return this.bundle;
	}
}
 