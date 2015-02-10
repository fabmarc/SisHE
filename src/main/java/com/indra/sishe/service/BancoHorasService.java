package com.indra.sishe.service;

import java.util.List;

import javax.ejb.Local;

import com.indra.infra.service.BaseService;
import com.indra.sishe.entity.BancoHoras;
import com.indra.sishe.entity.HistoricoDetalhes;

@Local
public interface BancoHorasService extends BaseService<BancoHoras>{

	public List<HistoricoDetalhes> contabilizarHorasBanco(List<Long> idsSolicitacoes);
	
}
