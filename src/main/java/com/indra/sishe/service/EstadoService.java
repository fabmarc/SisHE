package com.indra.sishe.service;

import javax.ejb.Local;

import com.indra.infra.service.BaseService;
import com.indra.sishe.entity.Cidade;
import com.indra.sishe.entity.Estado;
import com.indra.sishe.enums.EstadoEnum;

@Local
public interface EstadoService extends BaseService<Estado>{
	
	public EstadoEnum findByCidade(Cidade cidade);

}
