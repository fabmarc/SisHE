package com.indra.sishe.service;

import java.util.List;

import javax.ejb.Local;


import com.indra.infra.service.BaseService;
import com.indra.sishe.entity.Feriado;
import com.indra.sishe.entity.Folga;

@Local
public interface FeriadoService extends BaseService<Feriado>{

	public List<Feriado> findByFilter(Feriado entity);
	
	public Boolean verificaFeriadoPorData(Folga folga);
}
