package com.indra.sishe.service;

import java.util.List;

import javax.ejb.Local;

import com.indra.infra.service.BaseService;
import com.indra.sishe.entity.Usuario;
import com.indra.sishe.entity.UsuarioProjeto;

@Local
public interface UsuarioProjetoService extends BaseService<UsuarioProjeto> {

	public List<UsuarioProjeto> findByFilter(UsuarioProjeto usuarioProjeto);
	
	public List<Usuario> findByProjeto(Long id);
}
