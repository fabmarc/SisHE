package com.indra.sishe.service.impl;

import java.util.List;

import javax.ejb.Stateless;

import org.springframework.beans.factory.annotation.Autowired;

import com.indra.infra.dao.exception.RegistroDuplicadoException;
import com.indra.infra.dao.exception.RegistroInexistenteException;
import com.indra.infra.service.exception.ApplicationException;
import com.indra.sishe.dao.EstadoDAO;
import com.indra.sishe.entity.Cidade;
import com.indra.sishe.entity.Estado;
import com.indra.sishe.enums.EstadoEnum;
import com.indra.sishe.service.EstadoService;
import com.indra.sishe.service.StatelessServiceAb;

@Stateless
public class EstadoServiceImpl extends StatelessServiceAb implements EstadoService {

	private static final long serialVersionUID = 1L;

	@Autowired
	private EstadoDAO estadoDao;

	public EstadoServiceImpl() {
	}

	@Override
	public Estado save(Estado entity) {
		
		try {
			return estadoDao.save(entity);
		} catch (RegistroDuplicadoException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public Estado update(Estado entity) throws ApplicationException {
		try {
			return estadoDao.update(entity);
		} catch (RegistroInexistenteException e) {
			throw new ApplicationException(e, "msg.error.registro.inexistente", "Estado");
		} catch (RegistroDuplicadoException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public List<Estado> findAll() {
		return estadoDao.findAll();
	}

	@Override
	public Estado findById(Long id) throws ApplicationException {

		try {
			return estadoDao.findById(id);
		} catch (Exception e) {
			throw new ApplicationException(e, "msg.error.registro.inexistente", "Estado");
		}
	}

	@Override
	public void remove(Long id) throws ApplicationException {

		try {
			estadoDao.remove(id);
		} catch (Exception e) {
			throw new ApplicationException(e, "msg.error.registro.inexistente", "Estado");
		}
	}

	@Override
	public void remove(List<Long> ids) throws ApplicationException {
	}

	@Override
	public EstadoEnum findByCidade(Cidade cidade) {
		return estadoDao.findByCidade(cidade);
	}

}
