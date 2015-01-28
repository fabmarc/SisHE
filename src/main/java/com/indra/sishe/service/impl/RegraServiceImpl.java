package com.indra.sishe.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;

import org.springframework.beans.factory.annotation.Autowired;

import com.indra.infra.dao.exception.DeletarRegistroViolacaoFK;
import com.indra.infra.dao.exception.RegistroDuplicadoException;
import com.indra.infra.dao.exception.RegistroInexistenteException;
import com.indra.infra.service.exception.ApplicationException;
import com.indra.sishe.dao.RegraDAO;
import com.indra.sishe.entity.Regra;
import com.indra.sishe.service.RegraService;
import com.indra.sishe.service.StatelessServiceAb;

@Stateless
public class RegraServiceImpl extends StatelessServiceAb implements RegraService {

	private static final long serialVersionUID = -4481366216215891259L;

	@Autowired
	private RegraDAO regraDAO;

	public boolean validarRegra(Regra regra) throws ApplicationException {

		if (regra.getDescricao().isEmpty()) {
			throw new ApplicationException("msg.error.campo.obrigatorio", "Nome");
		} else if (regra.getDescricao().length() > 100) {
			throw new ApplicationException("msg.error.campo.maior.esperado", "Nome", "100");
		} else if (regra.getSindicato() == null) {
			throw new ApplicationException("msg.error.campo.obrigatorio", "Sindicato");
		} else if (regra.getDataInicio() == null) {
			throw new ApplicationException("msg.error.campo.obrigatorio", "Data Inicial");
		} else if (regra.getDataFim() == null) {
			throw new ApplicationException("msg.error.campo.obrigatorio", "Data Final");
		} else if (regra.getDataInicio().after(regra.getDataFim())) {
			throw new ApplicationException("msg.error.intervalo.incorreto", "Data Inicial", "Data Final");
		} else if (regra.getPorcentagem() == null) {
			throw new ApplicationException("msg.error.campo.obrigatorio", "Porcentagem");
		} else {
			return true;
		}
	}

	@Override
	public Regra save(Regra regra) throws ApplicationException {

		try {
			if (validarRegra(regra)) {
				return regraDAO.save(regra);
			} else {
				return null;
			}
		} catch (RegistroDuplicadoException e) {
			throw new ApplicationException(e, "msg.error.registro.duplicado", "Regra");
		}
	}

	public RegraServiceImpl() {
	}

	@Override
	public Regra update(Regra regra) throws ApplicationException {

		try {
			if (validarRegra(regra)) {
				return regraDAO.update(regra);
			} else {
				return null;
			}
		} catch (RegistroInexistenteException e) {
			throw new ApplicationException(e, "msg.error.registro.inexistente", "Regra");
		} catch (RegistroDuplicadoException e) {
			throw new ApplicationException(e, "msg.error.registro.duplicado", "Regra");
		}
	}

	@Override
	public List<Regra> findAll() {
		return regraDAO.findAll();
	}

	@Override
	public Regra findById(Long id) throws ApplicationException {
		try {
			return regraDAO.findById(id);
		} catch (RegistroInexistenteException e) {
			throw new ApplicationException(e, "msg.error.registro.inexistente", "Regra");
		}
	}

	@Override
	public void remove(Long id) throws ApplicationException {
	}

	@Override
	public void remove(List<Long> ids) throws ApplicationException {
		try {
			List<Object> pks = new ArrayList<Object>(ids);
			regraDAO.remove(pks);
		} catch (RegistroInexistenteException e) {
			throw new ApplicationException(e, "msg.error.registro.inexistente", "Projeto");
		} catch (DeletarRegistroViolacaoFK e) {
			throw new ApplicationException(e, "msg.error.excluir.registro.relacionado", "Regra");
		}
	}

	@Override
	public List<Regra> findByFilter(Regra regra) {
		return regraDAO.findByFilter(regra);
	}

}
