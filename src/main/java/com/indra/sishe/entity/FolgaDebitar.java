package com.indra.sishe.entity;

import java.io.Serializable;

public class FolgaDebitar implements Serializable {

	private static final long serialVersionUID = -7410617300374465301L;

	private Long id;
	
	private BancoHoras bancoHoras;
	
	private DatasFolga datasFolga;
	
	private Boolean flgContabilizado;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
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

	public Boolean getFlgContabilizado() {
		return flgContabilizado;
	}

	public void setFlgContabilizado(Boolean flgContabilizado) {
		this.flgContabilizado = flgContabilizado;
	}
	
}
