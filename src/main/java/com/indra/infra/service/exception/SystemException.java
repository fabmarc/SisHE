package com.indra.infra.service.exception;

import com.indra.infra.resource.MessageProvider;


public class SystemException extends RuntimeException {

	private static final long serialVersionUID = 1985799425424657598L;
	
	private String messageCode;
	private String[] messageParams;
	
	public SystemException(Exception cause, String messageCode, String... messageParams) {
		super(cause);
		this.setMessageCode(messageCode);
		this.setMessageParams(messageParams);
	}

	public SystemException(String messageCode, String... messageParams) {
		this.setMessageCode(messageCode);
		this.setMessageParams(messageParams);
	}
	
	@Override
	public String getMessage() {
		return MessageProvider.getInstance().getMessage(messageCode, messageParams);
	}

	public String getMessageCode() {
		return messageCode;
	}

	public void setMessageCode(String messageCode) {
		this.messageCode = messageCode;
	}

	public String[] getMessageParams() {
		return messageParams;
	}

	public void setMessageParams(String[] messageParams) {
		this.messageParams = messageParams;
	}
}
