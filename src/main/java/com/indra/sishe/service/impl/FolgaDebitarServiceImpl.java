package com.indra.sishe.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.indra.infra.dao.exception.DeletarRegistroViolacaoFK;
import com.indra.infra.dao.exception.RegistroDuplicadoException;
import com.indra.infra.dao.exception.RegistroInexistenteException;
import com.indra.infra.service.exception.ApplicationException;
import com.indra.sishe.dao.FolgaDebitarDAO;
import com.indra.sishe.entity.FolgaDebitar;
import com.indra.sishe.service.FolgaDebitarService;
import com.indra.sishe.service.StatelessServiceAb;

public class FolgaDebitarServiceImpl extends StatelessServiceAb implements FolgaDebitarService{

	private static final long serialVersionUID = -7799735793755724125L;
	
	@Autowired
	private FolgaDebitarDAO folgaDebitarDAO;

	@Override
	public FolgaDebitar save(FolgaDebitar entity) throws ApplicationException {
		try {
			return folgaDebitarDAO.save(entity);
		} catch (RegistroDuplicadoException e) {
			throw new ApplicationException(e, "msg.error.registro.duplicado", "Folga Debitar");
		}
	}

	@Override
	public FolgaDebitar update(FolgaDebitar entity) throws ApplicationException {
		return null;
	}

	@Override
	public List<FolgaDebitar> findAll() {
		return null;
	}

	@Override
	public FolgaDebitar findById(Long id) throws ApplicationException {
		try {
			return folgaDebitarDAO.findById(id);
		} catch (RegistroInexistenteException e) {
			throw new ApplicationException(e, "msg.error.registro.inexistente", "Folga Debitar");
		}
	}

	@Override
	public void remove(Long id) throws ApplicationException {
	}

	@Override
	public void remove(List<Long> ids) throws ApplicationException {
		try {
			folgaDebitarDAO.remove(ids);
		} catch (RegistroInexistenteException e) {
			throw new ApplicationException(e, "msg.error.registro.inexistente", "Folga Debitar");
		} catch (DeletarRegistroViolacaoFK e) {
			throw new ApplicationException(e, "msg.error.excluir.registro.relacionado", "Folga Debitar");
		}
	}

}
