package com.indra.sishe.service;

import java.util.List;

import javax.ejb.Local;

import com.indra.infra.service.BaseService;
import com.indra.sishe.entity.Regra;

@Local
public interface RegraService extends BaseService<Regra> {
	
	public List<Regra> findByFilter(Regra regra);

}
