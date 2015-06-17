package com.indra.sishe.entity;

import java.io.Serializable;

public class FolgaDebitar implements Serializable {

	private static final long serialVersionUID = -7410617300374465301L;

	private Integer id;
	
	private BancoHoras bancoHoras;
	
	private DatasFolga datasFolga;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public BancoHoras getBancoHoras() {
		return bancoHoras;
	}

	public void setBancoHoras(BancoHoras bancoHoras) {
		this.bancoHoras = bancoHoras;
	}

	public DatasFolga getDatasFolga() {
		return datasFolga;
	}

	public void setDatasFolga(DatasFolga datasFolga) {
		this.datasFolga = datasFolga;
	}
	
}
