package com.indra.sishe.service;

import javax.ejb.Local;

import com.indra.infra.service.BaseService;
import com.indra.sishe.entity.Folga;

@Local
public interface FolgaService extends BaseService<Folga>{
	
	public Boolean validarFolga(Folga folga);

}
