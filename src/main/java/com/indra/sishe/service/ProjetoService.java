package com.indra.sishe.service;

import java.util.List;

import javax.ejb.Local;

import com.indra.infra.service.BaseService;
import com.indra.sishe.entity.Projeto;

@Local
public interface ProjetoService extends BaseService<Projeto>{
	
	public List<Projeto> findByFilter(Projeto projeto);

}
