package com.indra.sishe.dao;

import java.util.List;

import com.indra.infra.dao.BaseDAO;
import com.indra.sishe.entity.BancoHoras;

public interface BancoHorasDAO extends BaseDAO<BancoHoras>{
	
	public void contabilizarHorasBanco(List<Long> idsSolicitacoes);

}
