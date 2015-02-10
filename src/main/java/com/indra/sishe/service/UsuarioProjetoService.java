package com.indra.sishe.service;

import java.util.List;

import javax.ejb.Local;

import com.indra.infra.service.BaseService;
import com.indra.sishe.entity.Projeto;
import com.indra.sishe.entity.Usuario;
import com.indra.sishe.entity.UsuarioProjeto;

@Local
public interface UsuarioProjetoService extends BaseService<UsuarioProjeto> {

	public List<UsuarioProjeto> findByFilter(UsuarioProjeto usuarioProjeto);

	public List<UsuarioProjeto> findByProjeto(UsuarioProjeto usuarioProjeto);

	public List<Usuario> findUserNotInProjeto(UsuarioProjeto usuarioProjeto);

	/* Método para cadastrar um usuário em uma equipe */
	public void salvar(List<Usuario> usuarios, Projeto projeto);
}
