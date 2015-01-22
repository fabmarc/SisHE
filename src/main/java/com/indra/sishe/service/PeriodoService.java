package com.indra.sishe.service;

import java.util.List;

import javax.ejb.Local;

import com.indra.infra.service.BaseService;
import com.indra.sishe.entity.Periodo;
import com.indra.sishe.entity.Regra;

@Local
public interface PeriodoService extends BaseService<Periodo> {

	public List<Periodo> findByFilter(Periodo entity);

	public List<Periodo> findByRegra(Regra entity);
}
