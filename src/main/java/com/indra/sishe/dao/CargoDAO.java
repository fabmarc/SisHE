package com.indra.sishe.dao;

import java.util.List;

import com.indra.infra.dao.BaseDAO;
import com.indra.sishe.entity.Cargo;

public interface CargoDAO extends BaseDAO<Cargo>{
	
	public List<Cargo> findByFilter(Cargo cargoFiltro);
	
	public Cargo pesquinarNome(String nome);
	
}
