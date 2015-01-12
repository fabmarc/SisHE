package com.indra.sishe.dao;

import java.util.List;

import com.indra.infra.dao.BaseDAO;
import com.indra.sishe.entity.Projeto;

public interface ProjetoDAO extends BaseDAO<Projeto> {

	public List<Projeto> findClienteByDescricao(String descricao);

}
