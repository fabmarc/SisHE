package com.indra.sishe.service;

import java.util.List;

import javax.ejb.Local;

import com.indra.infra.service.BaseService;
import com.indra.sishe.entity.Sindicato;

@Local
public interface SindicatoService extends BaseService<Sindicato> {

	// FUNÇÃO QUE PESQUISA UM SINDICATO PELO ESTADO DE ORIGEM E RETORNAR UMA
	// LISTA
	public List<Sindicato> pesquisarPorEstado(String estado);

	// FUNÇÃO QUE PESQUISA UM SINDICATO POR NOME E RETORNAR UMA LISTA
	public List<Sindicato> pesquisarPorNome(String nome);
}
