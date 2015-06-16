package com.indra.sishe.dao;

import java.util.Date;
import java.util.List;

import com.indra.infra.dao.BaseDAO;
import com.indra.sishe.entity.Feriado;
import com.indra.sishe.entity.Folga;

public interface FeriadoDAO extends BaseDAO<Feriado> {

	public List<Feriado> findByFilter(Feriado entity);
	
	public Boolean verificaFeriadoPorData(Folga folga);
	
}
