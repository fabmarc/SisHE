package com.indra.sishe.service.impl;

import java.io.Serializable;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import org.springframework.beans.factory.annotation.Autowired;

import com.indra.infra.dao.exception.RegistroDuplicadoException;
import com.indra.infra.dao.exception.RegistroInexistenteException;
import com.indra.infra.service.exception.ApplicationException;
import com.indra.sishe.dao.EstadoDAO;
import com.indra.sishe.entity.Estado;
import com.indra.sishe.service.EstadoService;
import com.indra.sishe.service.StatelessServiceAb;


@Stateless
public class EstadoServiceImpl extends StatelessServiceAb implements EstadoService {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Autowired
	private EstadoDAO estadoDao;

	public EstadoServiceImpl() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public Estado save(Estado entity) {
		// TODO Auto-generated method stub
		try {
			return estadoDao.save(entity);
		} catch (RegistroDuplicadoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public Estado update(Estado entity) throws ApplicationException {
		// TODO Auto-generated method stub
		try {
			return estadoDao.update(entity);
		} catch (RegistroInexistenteException e) {
			throw new ApplicationException(e, "msg.error.registro.inexistente",
					"Estado");
		}

	}

	@Override
	public List<Estado> findAll() {
		// TODO Auto-generated method stub
		return estadoDao.findAll();
	}

	@Override
	public Estado findById(Long id) throws ApplicationException {
		// TODO Auto-generated method stub
		try {
			return estadoDao.findById(id);
		} catch (Exception e) {
			throw new ApplicationException(e, "msg.error.registro.inexistente",
					"Estado");
			// TODO: handle exception
		}
	}

	

	@Override
	public List<Estado> pesquisarPorSigla(String sigla) {
		// TODO Auto-generated method stub
		return estadoDao.pesquisarPorSigla(sigla);

	}

	@Override
	public void remove(Long id) throws ApplicationException {
		// TODO Auto-generated method stub
		try {
			estadoDao.remove(id);
		} catch (Exception e) {
			throw new ApplicationException(e, "msg.error.registro.inexistente",
					"Estado");
			// TODO: handle exception
		}
		
	}

	@Override
	public void remove(List<Long> ids) throws ApplicationException {
		// TODO Auto-generated method stub
		
	}

}
