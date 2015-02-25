package com.indra.sishe.service;

import java.util.List;

import javax.ejb.Local;

import com.indra.infra.service.BaseService;
import com.indra.sishe.entity.BancoHoras;
import com.indra.sishe.entity.HistoricoDetalhes;
import com.indra.sishe.entity.Usuario;

@Local
public interface BancoHorasService extends BaseService<BancoHoras> {

	public List<HistoricoDetalhes> contabilizarHorasBanco(List<Long> idsSolicitacoes);

	public BancoHoras findByUsuario(Usuario entity);

}
