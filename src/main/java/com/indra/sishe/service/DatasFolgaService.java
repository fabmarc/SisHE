package com.indra.sishe.service;

import java.util.List;

import com.indra.infra.service.BaseService;
import com.indra.sishe.entity.DatasFolga;

public interface DatasFolgaService extends BaseService<DatasFolga>{

	public List<DatasFolga> findDatasBySolicitacaoFolga(Long idFolga);
	
}
