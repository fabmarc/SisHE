package com.indra.infra.resource;

import java.text.MessageFormat;
import java.util.ResourceBundle;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

@Named
@Singleton
public class MessageProvider {
	
	@Inject
	@MessageBundle
	private ResourceBundle bundle;
	
	private static MessageProvider instance;
	
	private MessageProvider() {
	}
	
	public static MessageProvider getInstance() {
		if (instance == null) instance = new MessageProvider();
		return instance;
	}
	
	public static void setInstance(MessageProvider instance) {
		MessageProvider.instance = instance;
	}
	
	public String getMessage(String key) {
		return bundle.getString(key);
	}

	public String getMessage(String key, String... params) {
		return MessageFormat.format(bundle.getString(key), (Object[]) params);
	}


}
