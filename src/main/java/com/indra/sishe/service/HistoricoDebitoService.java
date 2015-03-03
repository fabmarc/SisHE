package com.indra.sishe.service;

import java.util.List;

import javax.ejb.Local;

import com.indra.infra.service.BaseService;
import com.indra.sishe.entity.HistoricoDebito;
import com.indra.sishe.entity.Usuario;

@Local
public interface HistoricoDebitoService extends BaseService<HistoricoDebito>{
	
	public List<HistoricoDebito> findByUsuarioEMes(Usuario user, String mes, String ano);
	
}
