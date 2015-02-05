package com.indra.sishe.dao;

import java.util.List;

import com.indra.infra.dao.BaseDAO;
import com.indra.sishe.entity.UsuarioProjeto;

public interface UsuarioProjetoDAO extends BaseDAO<UsuarioProjeto> {

	public List<UsuarioProjeto> findByFilter(UsuarioProjeto usuarioProjeto);
	
	public List<UsuarioProjeto> findByProjeto(UsuarioProjeto usuarioProjeto);
}
