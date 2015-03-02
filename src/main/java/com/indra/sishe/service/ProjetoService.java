package com.indra.sishe.service;

import java.util.List;

import javax.ejb.Local;

import com.indra.infra.service.BaseService;
import com.indra.sishe.entity.Projeto;
import com.indra.sishe.entity.Usuario;

@Local
public interface ProjetoService extends BaseService<Projeto>{
	
	public List<Projeto> findByFilter(Projeto projeto);
	
	public List<Projeto> findByGerente(Usuario usuario);

	public Projeto findByUsuario(Usuario usuario);
}
