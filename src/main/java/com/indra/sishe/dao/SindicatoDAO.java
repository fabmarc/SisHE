package com.indra.sishe.dao;

import java.util.List;

import com.indra.infra.dao.BaseDAO;
import com.indra.infra.dao.exception.DeletarRegistroViolacaoFK;
import com.indra.infra.dao.exception.RegistroInexistenteException;
import com.indra.infra.service.exception.ApplicationException;
import com.indra.sishe.entity.Sindicato;

public interface SindicatoDAO extends BaseDAO<Sindicato> {

	// FUNÇÃO QUE PESQUISA UM SINDICATO PELO FILTRO INFORMADO
	public List<Sindicato> findByFilter(Sindicato sindicato);

}
