package com.indra.sishe.service;

import java.util.List;

import javax.ejb.Local;

import com.indra.infra.service.BaseService;
import com.indra.sishe.entity.Historico;

@Local
public interface HistoricoService extends BaseService<Historico>{
	
	public void gerarHistorico(List<Long> ids, String descricao);

}
