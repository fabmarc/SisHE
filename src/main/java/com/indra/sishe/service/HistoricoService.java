package com.indra.sishe.service;

import java.util.List;

import javax.ejb.Local;

import com.indra.infra.service.BaseService;
import com.indra.sishe.entity.DadosRelatorio;
import com.indra.sishe.entity.Historico;
import com.indra.sishe.entity.HistoricoDetalhes;

@Local
public interface HistoricoService extends BaseService<Historico> {

	public void gerarHistorico(List<Long> ids, String descricao, List<HistoricoDetalhes> detalhes);

	public List<DadosRelatorio> gerarRelatorio(String mes, String ano);

}
