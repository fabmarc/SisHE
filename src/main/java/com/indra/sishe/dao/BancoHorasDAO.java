package com.indra.sishe.dao;

import java.util.List;

import com.indra.infra.dao.BaseDAO;
import com.indra.sishe.entity.BancoHoras;
import com.indra.sishe.entity.HistoricoDetalhes;

public interface BancoHorasDAO extends BaseDAO<BancoHoras>{
	
	public List<HistoricoDetalhes> contabilizarHorasBanco(List<Long> idsSolicitacoes);

}
