package com.indra.sishe.service.impl;

import java.util.List;

import javax.ejb.Stateless;

import org.springframework.beans.factory.annotation.Autowired;

import com.indra.infra.service.exception.ApplicationException;
import com.indra.sishe.dao.EstadoDAO;
import com.indra.sishe.entity.Estado;
import com.indra.sishe.service.EstadoService;
import com.indra.sishe.service.StatelessServiceAb;

@Stateless
public class EstadoServiceImp extends StatelessServiceAb implements EstadoService {

	
	private static final long serialVersionUID = -2868291146381026972L;
	
	@Autowired
	private EstadoDAO estadoDAO;
	
	@Override
	public Estado save(Estado entity) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Estado update(Estado entity) throws ApplicationException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Estado> findAll() {
		return estadoDAO.findAll();
	}

	@Override
	public Estado findById(Long id) throws ApplicationException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void remove(Long id) throws ApplicationException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void remove(List<Long> ids) throws ApplicationException {
		// TODO Auto-generated method stub
		
	}

}
