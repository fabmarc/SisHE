package com.indra.sishe.service;

import java.util.List;

import javax.ejb.Local;

import com.indra.infra.service.BaseService;
import com.indra.sishe.entity.Cargo;
import com.indra.sishe.entity.Usuario;

@Local
public interface UsuarioService extends BaseService<Usuario> {

	public List<Usuario> findByFilter(Usuario usuarioFiltro);

	public List<Usuario> findByCargo(Cargo cargo);

}
