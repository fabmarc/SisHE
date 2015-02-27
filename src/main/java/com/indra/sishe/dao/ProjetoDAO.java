package com.indra.sishe.dao;

import java.util.List;

import com.indra.infra.dao.BaseDAO;
import com.indra.sishe.entity.Projeto;
import com.indra.sishe.entity.Usuario;

public interface ProjetoDAO extends BaseDAO<Projeto> {

	public List<Projeto> findByFilter(Projeto projeto);
	
	public List<Projeto> findByGerente(Usuario usuario);
}
