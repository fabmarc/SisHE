package com.indra.infra.dao.exception;

public class RegistroDuplicadoException extends Exception{

	private static final long serialVersionUID = 561060267531740787L;

	private String messageCode;

	public RegistroDuplicadoException() {
	}
	
	public String getMessageCode() {
		return messageCode;
	}

	public void setMessageCode(String messageCode) {
		this.messageCode = messageCode;
	}
	
	public RegistroDuplicadoException(String messageCode) {
		this.setMessageCode(messageCode);
	}
	
}
