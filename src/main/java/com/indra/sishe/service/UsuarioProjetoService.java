package com.indra.sishe.service;

import java.util.List;

import javax.ejb.Local;

import com.indra.infra.service.BaseService;
import com.indra.sishe.entity.UsuarioProjeto;

@Local
public interface UsuarioProjetoService extends BaseService<UsuarioProjeto> {

	public List<UsuarioProjeto> findByFilter(UsuarioProjeto usuarioProjeto);
}
