package com.indra.sishe.service.impl;

import java.util.List;

import javax.ejb.Stateless;

import org.springframework.beans.factory.annotation.Autowired;

import com.indra.infra.service.exception.ApplicationException;
import com.indra.sishe.dao.CidadeDAO;
import com.indra.sishe.entity.Cidade;
import com.indra.sishe.entity.Estado;
import com.indra.sishe.service.CidadeService;
import com.indra.sishe.service.StatelessServiceAb;

@Stateless
public class CidadeServiceImp extends StatelessServiceAb implements CidadeService{

	private static final long serialVersionUID = -2846409558284556285L;
	@Autowired
	private CidadeDAO cidadeDAO;

	@Override
	public Cidade save(Cidade entity) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Cidade update(Cidade entity) throws ApplicationException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Cidade> findAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Cidade findById(Long id) throws ApplicationException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void remove(Long id) throws ApplicationException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<Cidade> findByEstado(Estado estado) {
		return cidadeDAO.findByEstado(estado);
	}

	@Override
	public void remove(List<Long> ids) throws ApplicationException {
		// TODO Auto-generated method stub
		
	}
	
}
