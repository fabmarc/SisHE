package com.indra.sishe.service;

import java.util.List;

import javax.ejb.Local;

import com.indra.infra.service.BaseService;
import com.indra.infra.service.exception.ApplicationException;
import com.indra.sishe.entity.Sindicato;

@Local
public interface SindicatoService extends BaseService<Sindicato> {

	// FUNÇÃO QUE PESQUISA UM SINDICATO PELO FILTRO INFORMADO
	public List<Sindicato> findByFilter(Sindicato sindicato);

}
