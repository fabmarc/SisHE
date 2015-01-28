package com.indra.sishe.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.indra.infra.dao.exception.DeletarRegistroViolacaoFK;
import com.indra.infra.dao.exception.RegistroDuplicadoException;
import com.indra.infra.dao.exception.RegistroInexistenteException;
import com.indra.infra.service.exception.ApplicationException;
import com.indra.sishe.dao.UsuarioProjetoDAO;
import com.indra.sishe.entity.UsuarioProjeto;
import com.indra.sishe.service.StatelessServiceAb;
import com.indra.sishe.service.UsuarioProjetoService;

public class UsuarioProjetoServiceImpl extends StatelessServiceAb implements UsuarioProjetoService {

	private static final long serialVersionUID = -2376148536768097124L;

	@Autowired
	private UsuarioProjetoDAO usuarioProjetoDAO;

	@Override
	public UsuarioProjeto save(UsuarioProjeto entity) throws ApplicationException {
		try {
			if (validarUsuarioProjeto(entity)) {
				return usuarioProjetoDAO.save(entity);
			} else {
				return null;
			}
		} catch (RegistroDuplicadoException e) {
			throw new ApplicationException(e, "msg.error.registro.duplicado", "Equipe");
		}
	}

	@Override
	public UsuarioProjeto update(UsuarioProjeto entity) throws ApplicationException {
		try {
			if (validarUsuarioProjeto(entity)) {
				return usuarioProjetoDAO.update(entity);
			} else {
				return null;
			}
		} catch (RegistroInexistenteException e) {
			throw new ApplicationException(e, "msg.error.registro.inexistente", "Equipe");
		} catch (RegistroDuplicadoException d) {
			throw new ApplicationException(d, "msg.error.campo.existente", "Equipe", "nome");
		}
	}

	@Override
	public List<UsuarioProjeto> findAll() {
		return usuarioProjetoDAO.findAll();
	}

	@Override
	public UsuarioProjeto findById(Long id) throws ApplicationException {
		try {
			return usuarioProjetoDAO.findById(id);
		} catch (RegistroInexistenteException e) {
			throw new ApplicationException(e, "msg.error.registro.inexistente", "Equipe");
		}
	}

	@Override
	public void remove(Long id) throws ApplicationException {
		try {
			usuarioProjetoDAO.remove(id);
		} catch (RegistroInexistenteException e) {
			throw new ApplicationException(e, "msg.error.registro.inexistente", "Equipe");
		} catch (DeletarRegistroViolacaoFK d) {
			throw new ApplicationException(d, "msg.error.excluir.registro.relacionado", "Equipe");
		}

	}

	@Override
	public void remove(List<Long> ids) throws ApplicationException {
		try {
			List<Object> pks = new ArrayList<Object>(ids);
			usuarioProjetoDAO.remove(pks);
		} catch (RegistroInexistenteException e) {
			throw new ApplicationException(e, "msg.error.registro.inexistente", "Equipe");
		} catch (DeletarRegistroViolacaoFK d) {
			throw new ApplicationException(d, "msg.error.excluir.registro.relacionado", "Equioe");
		}

	}

	@Override
	public List<UsuarioProjeto> findByFilter(UsuarioProjeto usuarioProjeto) {
		return usuarioProjetoDAO.findByFilter(usuarioProjeto);
	}

	public boolean validarUsuarioProjeto(UsuarioProjeto entity) throws ApplicationException {
		if (entity.getUsuario() == null || entity.getUsuario().getNome().isEmpty()) {
			throw new ApplicationException("msg.error.campo.obrigatorio", "Usuário");
		} else if (entity.getUsuario().getNome().length() > 60) {
			throw new ApplicationException("msg.error.campo.maior.esperado", "Nome", "60");
		} else if (entity.getProjeto() == null || entity.getProjeto().getNome().isEmpty()) {
			throw new ApplicationException("msg.error.campo.obrigatorio", "Projeto");
		} else if (entity.getProjeto().getNome().length() > 50) {
			throw new ApplicationException("msg.error.campo.maior.esperado", "Nome", "50");
		} else {
			return true;
		}
	}

}
