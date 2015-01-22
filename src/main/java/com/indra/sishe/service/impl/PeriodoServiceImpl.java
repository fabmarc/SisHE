package com.indra.sishe.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;

import org.springframework.beans.factory.annotation.Autowired;

import com.indra.infra.dao.exception.DeletarRegistroViolacaoFK;
import com.indra.infra.dao.exception.RegistroDuplicadoException;
import com.indra.infra.dao.exception.RegistroInexistenteException;
import com.indra.infra.service.exception.ApplicationException;
import com.indra.sishe.dao.PeriodoDAO;
import com.indra.sishe.entity.Periodo;
import com.indra.sishe.entity.Regra;
import com.indra.sishe.service.PeriodoService;
import com.indra.sishe.service.StatelessServiceAb;

@Stateless
public class PeriodoServiceImpl extends StatelessServiceAb implements PeriodoService {

	@Autowired
	private PeriodoDAO periodoDao;

	private static final long serialVersionUID = -29269946960860925L;

	@Override
	public Periodo save(Periodo entity) throws ApplicationException {
		try {
			return periodoDao.save(entity);
		} catch (RegistroDuplicadoException e) {
			throw new ApplicationException(e, "msg.error.registro.duplicado", "Periodo");
		}
	}

	@Override
	public Periodo update(Periodo entity) throws ApplicationException {
		try {
			return periodoDao.update(entity);
		} catch (RegistroInexistenteException e) {
			throw new ApplicationException(e, "msg.error.registro.inexistente", "Periodo");
		} catch (RegistroDuplicadoException d) {
			throw new ApplicationException(d, "msg.error.campo.existente", "periodo", "");
		}
	}

	@Override
	public List<Periodo> findAll() {
		return periodoDao.findAll();
	}

	@Override
	public Periodo findById(Long id) throws ApplicationException {
		try {
			return periodoDao.findById(id);
		} catch (RegistroInexistenteException e) {
			throw new ApplicationException(e, "msg.error.registro.inexistente", "Periodo");
		}
	}

	@Override
	public void remove(Long id) throws ApplicationException {
		try {
			periodoDao.remove(id);
		} catch (RegistroInexistenteException e) {
			throw new ApplicationException(e, "msg.error.registro.inexistente", "Periodo");
		} catch (DeletarRegistroViolacaoFK d) {
			throw new ApplicationException(d, "msg.error.excluir.registro.relacionado", "Periodo");
		}
	}

	@Override
	public void remove(List<Long> ids) throws ApplicationException {
		try {
			List<Object> pks = new ArrayList<Object>(ids);
			periodoDao.remove(pks);
		} catch (RegistroInexistenteException e) {
			throw new ApplicationException(e, "msg.error.registro.inexistente", "Periodo");
		} catch (DeletarRegistroViolacaoFK d) {
			throw new ApplicationException(d, "msg.error.excluir.registro.relacionado", "Periodo");
		}
	}

	@Override
	public List<Periodo> findByFilter(Periodo entity) {
		return periodoDao.findByFilter(entity);
	}

	@Override
	public List<Periodo> findByRegra(Regra entity) {
		return periodoDao.findByRegra(entity);
	}

}
