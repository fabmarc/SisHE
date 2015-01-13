package com.indra.sishe.service;

import java.util.List;

import javax.ejb.Local;

import com.indra.infra.service.BaseService;
import com.indra.sishe.entity.Estado;

@Local
public interface EstadoService extends BaseService<Estado> {

	public List<Estado> pesquisarPorSigla(String sigla);
}
