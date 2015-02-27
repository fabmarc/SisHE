package com.indra.sishe.dao;

import java.util.List;

import com.indra.infra.dao.BaseDAO;
import com.indra.sishe.entity.DadosRelatorio;
import com.indra.sishe.entity.Historico;
import com.indra.sishe.entity.HistoricoDetalhes;
import com.indra.sishe.entity.Usuario;

public interface HistoricoDAO extends BaseDAO<Historico> {

	public void gerarHistorico(List<Long> ids, String descricao, List<HistoricoDetalhes> detalhes);

	public List<DadosRelatorio> gerarRelatorio(String mes, String ano, Usuario entity);
}
