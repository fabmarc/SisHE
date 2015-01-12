package com.indra.sishe.dao;

import java.util.List;

import com.indra.infra.dao.BaseDAO;
import com.indra.sishe.entity.Sindicato;

public interface SindicatoDAO extends BaseDAO<Sindicato> {

	// FUNÇÃO QUE PESQUISA UM SINDICATO PELO ESTADO DE ORIGEM E RETORNAR UMA
	// LISTA
	public List<Sindicato> pesquisarPorEstado(String estado);

	// FUNÇÃO QUE PESQUISA UM SINDICATO POR NOME E RETORNAR UMA LISTA
	public List<Sindicato> pesquisarPorNome(String nome);
}
