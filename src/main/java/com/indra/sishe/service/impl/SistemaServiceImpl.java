package com.indra.sishe.service.impl;

import java.util.List;

import javax.ejb.Stateless;

import org.springframework.beans.factory.annotation.Autowired;

import com.indra.infra.service.exception.ApplicationException;
import com.indra.sishe.entity.Sistema;
import com.indra.sishe.service.SistemaService;
import com.indra.sishe.service.StatelessServiceAb;

@Stateless
public class SistemaServiceImpl extends StatelessServiceAb implements SistemaService{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public SistemaServiceImpl() {
		// TODO Auto-generated constructor stub
	}

	@Autowired
	private SistemaService sistemaService;

	@Override
	public Sistema save(Sistema entity) throws ApplicationException {
		// TODO Auto-generated method stub
		return sistemaService.save(entity);
	}

	@Override
	public Sistema update(Sistema entity) throws ApplicationException {
		// TODO Auto-generated method stub
		return sistemaService.update(entity);
	}

	@Override
	public List<Sistema> findAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Sistema findById(Long id) throws ApplicationException {
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
		sistemaService.remove(ids);
	}
	
	
}
