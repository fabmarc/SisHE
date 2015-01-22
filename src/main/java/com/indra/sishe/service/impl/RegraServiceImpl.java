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
public class RegraServiceImpl extends StatelessServiceAb implements RegraService{

	private static final long serialVersionUID = -4481366216215891259L;
	
	@Autowired
	private RegraDAO regraDAO;

	@Override
	public Regra save(Regra regra) throws ApplicationException {
		System.out.print("break");
		try {
			return regraDAO.save(regra);
		} catch (RegistroDuplicadoException e) {
			throw new ApplicationException(e, "msg.error.registro.duplicado", "Regra");
		}
	}

	public RegraServiceImpl() {
		System.out.print("Criou RegraServiceImpl");
	}

	@Override
	public Regra update(Regra regra) throws ApplicationException {
		try {
			return regraDAO.update(regra);
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
	public void remove(Long id) throws ApplicationException {	}

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
