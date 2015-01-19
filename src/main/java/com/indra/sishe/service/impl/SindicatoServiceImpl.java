package com.indra.sishe.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;

import org.springframework.beans.factory.annotation.Autowired;

import com.indra.infra.dao.exception.DeletarRegistroViolacaoFK;
import com.indra.infra.dao.exception.RegistroDuplicadoException;
import com.indra.infra.dao.exception.RegistroInexistenteException;
import com.indra.infra.service.exception.ApplicationException;
import com.indra.sishe.dao.SindicatoDAO;
import com.indra.sishe.entity.Sindicato;
import com.indra.sishe.service.SindicatoService;
import com.indra.sishe.service.StatelessServiceAb;

@Stateless
public class SindicatoServiceImpl extends StatelessServiceAb implements
		SindicatoService {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8670069751964068611L;

	@Autowired
	private SindicatoDAO sindicatoDao;

	@Override
	public Sindicato save(Sindicato entity) {
		// TODO Auto-generated method stub
		try {
			return sindicatoDao.save(entity);
		} catch (RegistroDuplicadoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
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
		} catch (RegistroDuplicadoException e) {
			throw new ApplicationException(e, "msg.error.campo.existente", "sindicato", "nome");
		}

	}

	@Override
	public List<Sindicato> findAll() {
		// TODO Auto-generated method stub
		return sindicatoDao.findAll();
	}

	@Override
	public Sindicato findById(Long id) throws ApplicationException {
		try {
			return sindicatoDao.findById(id);
		} catch (RegistroInexistenteException e) {
			// TODO: handle exception
			throw new ApplicationException(e, "msg.error.registro.inexistente",
					"Sindicato");
		}

	}

	@Override
	public List<Sindicato> findByFilter(Sindicato sindicato) {
		// TODO Auto-generated method stub
		return sindicatoDao.findByFilter(sindicato);
	}

	@Override
	public void remove(Long id) throws ApplicationException {
		// TODO Auto-generated method stub
		try {
			sindicatoDao.remove(id);
		} catch (RegistroInexistenteException e) {
			// TODO: handle exception
			throw new ApplicationException(e, "msg.error.registro.inexistente",
					"Sindicato");
		} catch (DeletarRegistroViolacaoFK e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void remove(List<Long> ids) throws ApplicationException {
		// TODO Auto-generated method stub
		try {
			List<Object> pks = new ArrayList<Object>(ids);
			sindicatoDao.remove(pks);
		} catch (RegistroInexistenteException e) {
			throw new ApplicationException(e, "msg.error.registro.inexistente",
					"Sindicato");
		} catch (DeletarRegistroViolacaoFK e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
