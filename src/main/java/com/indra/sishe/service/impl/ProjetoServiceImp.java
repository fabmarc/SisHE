package com.indra.sishe.service.impl;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import com.indra.infra.dao.exception.RegistroInexistenteException;
import com.indra.infra.service.exception.ApplicationException;
import com.indra.sishe.dao.ProjetoDAO;
import com.indra.sishe.entity.Projeto;
import com.indra.sishe.service.ProjetoService;

@Stateless
public class ProjetoServiceImp implements ProjetoService {

	private static final long serialVersionUID = -712532157790010620L;

	@Inject
	private ProjetoDAO projetoDao;

	@Override
	public Projeto save(Projeto entity) {
		projetoDao.save(entity);
		return null;
	}

	@Override
	public Projeto update(Projeto entity) {
		try {
			projetoDao.update(entity);
		} catch (RegistroInexistenteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public List<Projeto> findAll() {
		return projetoDao.findAll();
	}

	@Override
	public Projeto findById(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void remove(Long id) throws ApplicationException {
		// TODO Auto-generated method stub
		
	}


}
