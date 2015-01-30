package com.indra.sishe.dao;

import java.util.List;

import com.indra.infra.dao.BaseDAO;
import com.indra.infra.dao.exception.RegistroInexistenteException;
import com.indra.sishe.entity.Cargo;
import com.indra.sishe.entity.Usuario;

public interface UsuarioDAO extends BaseDAO<Usuario> {

	public List<Usuario> findByFilter(Usuario usuarioFiltro);

	public List<Usuario> findByCargo(Cargo cargo);

	public Usuario updatePassword(Usuario entity) throws RegistroInexistenteException;
}
