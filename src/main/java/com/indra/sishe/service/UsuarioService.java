package com.indra.sishe.service;

import java.util.List;

import javax.ejb.Local;

import com.indra.infra.service.BaseService;
import com.indra.infra.service.exception.ApplicationException;
import com.indra.sishe.entity.Projeto;
import com.indra.sishe.entity.Usuario;

@Local
public interface UsuarioService extends BaseService<Usuario> {

	public List<Usuario> findByFilter(Usuario usuarioFiltro);

	public List<Usuario> findByCargo(String role);

	public void alterarSenha(Usuario usuario) throws ApplicationException;

	public Usuario findByLogin(String login);
	
	public List<Usuario> findGerentesDisponiveis();
	
	public List<Usuario> findLideresDisponiveis(Projeto projeto);

	public List<Usuario> findByProjetos(List<Long> ids);
	
}
