package com.indra.sishe.dao;

import java.util.List;

import com.indra.infra.dao.BaseDAO;
import com.indra.sishe.entity.Historico;

public interface HistoricoDAO extends BaseDAO<Historico> {

	public void gerarHistorico(List<Long> ids);

}
