package com.indra.sishe.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;

import org.springframework.beans.factory.annotation.Autowired;

import com.indra.infra.dao.exception.DeletarRegistroViolacaoFK;
import com.indra.infra.dao.exception.RegistroDuplicadoException;
import com.indra.infra.dao.exception.RegistroInexistenteException;
import com.indra.infra.service.exception.ApplicationException;
import com.indra.sishe.dao.ProjetoDAO;
import com.indra.sishe.entity.Projeto;
import com.indra.sishe.entity.Usuario;
import com.indra.sishe.service.ProjetoService;
import com.indra.sishe.service.StatelessServiceAb;

@Stateless
public class ProjetoServiceImpl extends StatelessServiceAb implements ProjetoService {

	private static final long serialVersionUID = 5704865963322848308L;

	@Autowired
	private ProjetoDAO projetoDAO;

	private boolean validarProjeto(Projeto projetoSelecionado) throws ApplicationException {
		
		if (projetoSelecionado.getNome().isEmpty()) {
			throw new ApplicationException("msg.error.campo.obrigatorio", "Nome");
		} else if (projetoSelecionado.getNome().length() > 50) {
			throw new ApplicationException("msg.error.campo.maior.esperado", "Nome", "50");
		} else if (projetoSelecionado.getGerente() == null) {
			throw new ApplicationException("msg.error.campo.obrigatorio", "Gerente");
		} else if (projetoSelecionado.getDescricao().length() > 500) {
			throw new ApplicationException("msg.error.campo.maior.esperado", "Descrição", "500");
		} else {
			return true;
		}
	}

	@Override
	public Projeto save(Projeto projeto) throws ApplicationException {
		
		try {
			if (validarProjeto(projeto)) {
				return projetoDAO.save(projeto);
			} else {
				return null;
			}
		} catch (RegistroDuplicadoException e) {
			throw new ApplicationException(e, "msg.error.registro.duplicado", "Projeto");
		}
	}

	@Override
	public Projeto update(Projeto projeto) throws ApplicationException {
		
		try {
			if (validarProjeto(projeto)) {
				return projetoDAO.update(projeto);
			} else {
				return null;
			}
		} catch (RegistroInexistenteException e) {
			throw new ApplicationException(e, "msg.error.registro.inexistente", "Projeto");
		} catch (RegistroDuplicadoException e) {
			throw new ApplicationException(e, "msg.error.registro.duplicado", "Projeto");
		}
	}

	@Override
	public List<Projeto> findAll() {
		return projetoDAO.findAll();
	}

	@Override
	public Projeto findById(Long id) throws ApplicationException {
		try {
			return projetoDAO.findById(id);
		} catch (RegistroInexistenteException e) {
			throw new ApplicationException(e, "msg.error.registro.inexistente", "Projeto");
		}
	}

	@Override
	public void remove(Long id) throws ApplicationException {
	}

	@Override
	public void remove(List<Long> ids) throws ApplicationException {
		try {
			List<Object> pks = new ArrayList<Object>(ids);
			projetoDAO.remove(pks);
		} catch (RegistroInexistenteException e) {
			throw new ApplicationException(e, "msg.error.registro.inexistente", "Projeto");
		} catch (DeletarRegistroViolacaoFK e) {
			throw new ApplicationException(e, "msg.error.excluir.registro.relacionado", "Projeto");
		}

	}

	@Override
	public List<Projeto> findByFilter(Projeto projeto) {
		return projetoDAO.findByFilter(projeto);
	}

	@Override
	public List<Projeto> findByGerente(Usuario usuario) {
		return projetoDAO.findByGerente(usuario);
	}

	@Override
	public Projeto findByUsuario(Usuario usuario) {
		return projetoDAO.findByUsuario(usuario);
	}

}
