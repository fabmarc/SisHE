package com.indra.sishe.dao;

import java.util.List;

import com.indra.infra.dao.BaseDAO;
import com.indra.sishe.entity.HistoricoDebito;
import com.indra.sishe.entity.Usuario;

public interface HistoricoDebitoDao extends BaseDAO<HistoricoDebito> {

	public List<HistoricoDebito> findByUsuarioEMes(Usuario user, String mes, String ano);
	
}
