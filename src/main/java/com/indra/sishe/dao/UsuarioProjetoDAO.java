package com.indra.sishe.dao;

import java.util.List;

import com.indra.infra.dao.BaseDAO;
import com.indra.sishe.entity.Projeto;
import com.indra.sishe.entity.Usuario;
import com.indra.sishe.entity.UsuarioProjeto;

public interface UsuarioProjetoDAO extends BaseDAO<UsuarioProjeto> {

	public List<UsuarioProjeto> findByFilter(UsuarioProjeto usuarioProjeto);
	
	public List<UsuarioProjeto> findByProjeto(UsuarioProjeto usuarioProjeto);
	
	public List<Usuario> findUserNotInProjeto(UsuarioProjeto usuarioProjeto);
	
	/*Método para cadastrar um usuário em uma equipe*/
	public void salvar(List<Usuario> usuarios, Projeto projeto);
}
