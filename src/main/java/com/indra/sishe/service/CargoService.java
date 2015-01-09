package com.indra.sishe.service;

import java.util.List;

import javax.ejb.Local;

import com.indra.infra.service.BaseService;
import com.indra.sishe.entity.Cargo;

@Local
public interface CargoService extends BaseService<Cargo>{

	public List<Cargo> findByFilter (Cargo cargoFiltro);
		
}
