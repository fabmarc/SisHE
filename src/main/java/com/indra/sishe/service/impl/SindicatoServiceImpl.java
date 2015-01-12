package com.indra.sishe.service.impl;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import com.indra.infra.dao.exception.RegistroInexistenteException;
import com.indra.infra.service.exception.ApplicationException;
import com.indra.sishe.dao.SindicatoDAO;
import com.indra.sishe.entity.Sindicato;
import com.indra.sishe.service.SindicatoService;

@Stateless
public class SindicatoServiceImpl implements SindicatoService {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8670069751964068611L;

	@Inject
	private SindicatoDAO sindicatoDao;

	@Override
	public Sindicato save(Sindicato entity) {
		// TODO Auto-generated method stub
		return sindicatoDao.save(entity);
	}

	@Override
	public Sindicato update(Sindicato entity) throws ApplicationException {
		// TODO Auto-generated method stub
		try {
			return sindicatoDao.update(entity);
		} catch (RegistroInexistenteException e) {
			// TODO: handle exception
			throw new ApplicationException(e, "msg.error.registro.inexistente",
					"Sindicato");
		}

	}

	@Override
	public List<Sindicato> findAll() {
		// TODO Auto-generated method stub
		return sindicatoDao.findAll();
	}

	@Override
	public Sindicato findById(Long id) throws ApplicationException {
		// TODO Auto-generated method stub
		try {
			return sindicatoDao.findById(id);
		} catch (RegistroInexistenteException e) {
			// TODO: handle exception
			throw new ApplicationException(e, "msg.error.registro.inexistente",
					"Sindicato");
		}

	}


	// FUNÇÃO QUE PESQUISA UM SINDICATO PELO ESTADO DE ORIGEM E RETORNAR UMA
	// LISTA
	@Override
	public List<Sindicato> pesquisarPorEstado(String estado) {
		// TODO Auto-generated method stub
		return null;
	}

	// FUNÇÃO QUE PESQUISA UM SINDICATO POR NOME E RETORNAR UMA LISTA
	@Override
	public List<Sindicato> pesquisarPorNome(String nome) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void remove(Long id) throws ApplicationException {
		// TODO Auto-generated method stub
		
	}

}
