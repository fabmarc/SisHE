package com.indra.sishe.entity;

import java.io.Serializable;
import java.util.Date;

public class DatasFolga implements Serializable{

	private static final long serialVersionUID = 7895805468164309356L;

	private Long id;
	
	private Folga folga;
	
	private Date data;
	
	public DatasFolga(){
	}
	
	public DatasFolga(Date data){
		this.data = data;
	}

	public DatasFolga(Date data, Folga f){
		this.data = data;
		this.folga = f;
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Folga getFolga() {
		return folga;
	}

	public void setFolga(Folga folga) {
		this.folga = folga;
	}

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	
	
}
