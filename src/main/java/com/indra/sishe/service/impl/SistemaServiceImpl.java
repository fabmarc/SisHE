package com.indra.sishe.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;

import org.springframework.beans.factory.annotation.Autowired;

import com.indra.infra.dao.exception.DeletarRegistroViolacaoFK;
import com.indra.infra.dao.exception.RegistroDuplicadoException;
import com.indra.infra.dao.exception.RegistroInexistenteException;
import com.indra.infra.service.exception.ApplicationException;
import com.indra.sishe.dao.SistemaDAO;
import com.indra.sishe.entity.Sistema;
import com.indra.sishe.service.SistemaService;
import com.indra.sishe.service.StatelessServiceAb;

@Stateless
public class SistemaServiceImpl extends StatelessServiceAb implements SistemaService {

	private static final long serialVersionUID = 1L;

	public SistemaServiceImpl() {
	}

	@Autowired
	private SistemaDAO sistemaDao;

	@Override
	public Sistema save(Sistema entity) throws ApplicationException {

		try {
			if (validarSistema(entity)) {
				return sistemaDao.save(entity);
			} else {
				return null;
			}

		} catch (RegistroDuplicadoException e) {

			throw new ApplicationException(e, "msg.error.registro.duplicado", "Sistema");
		}
	}

	@Override
	public Sistema update(Sistema entity) throws ApplicationException {

		try {
			if (validarSistema(entity)) {
				return sistemaDao.update(entity);
			} else {
				return null;
			}

		} catch (RegistroInexistenteException e) {
			throw new ApplicationException(e, "msg.error.registro.inexistente", "Sistema");
		} catch (RegistroDuplicadoException d) {
			throw new ApplicationException(d, "msg.error.campo.existente", "Sistema", "nome");
		}
	}

	public boolean validarSistema(Sistema sistemaSelecionado) throws ApplicationException {

		if (sistemaSelecionado.getNome() == null || sistemaSelecionado.getNome().isEmpty()) {
			throw new ApplicationException("msg.error.campo.obrigatorio", "Nome");
		} else if (sistemaSelecionado.getNome() == null || sistemaSelecionado.getNome().length() > 50) {
			throw new ApplicationException("msg.error.campo.maior.esperado", "Nome", "50");

		} else if (sistemaSelecionado.getProjeto().getId() == null) {
			throw new ApplicationException("msg.error.campo.obrigatorio", "Projeto");
		} else if (sistemaSelecionado.getLider().getId() == null) {
			throw new ApplicationException("msg.error.campo.obrigatorio", "Lider");
		} else {
			return true;
		}
	}

	@Override
	public List<Sistema> findAll() {

		return sistemaDao.findAll();
	}

	@Override
	public List<Sistema> findByFilter(Sistema sistema) {
		return sistemaDao.findByFilter(sistema);
	}

	@Override
	public Sistema findById(Long id) throws ApplicationException {

		try {
			return sistemaDao.findById(id);
		} catch (RegistroInexistenteException e) {
			throw new ApplicationException(e, "msg.error.registro.inexistente", "Sistema");
		}
	}

	@Override
	public void remove(Long id) throws ApplicationException {
	}

	@Override
	public void remove(List<Long> ids) throws ApplicationException {

		try {
			List<Object> pks = new ArrayList<Object>(ids);
			sistemaDao.remove(pks);
		} catch (RegistroInexistenteException e) {
			throw new ApplicationException(e, "msg.error.registro.inexistente", "Sistema");
		} catch (DeletarRegistroViolacaoFK e) {

			throw new ApplicationException(e, "msg.error.excluir.registro.relacionado", "Sistema");
		}
	}

	@Override
	public List<Sistema> findByProjeto(Sistema sistema) {
		return null;
	}

}
