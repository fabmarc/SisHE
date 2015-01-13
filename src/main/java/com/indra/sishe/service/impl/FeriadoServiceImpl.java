package com.indra.sishe.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;

import org.springframework.beans.factory.annotation.Autowired;

import com.indra.infra.dao.exception.DeletarRegistroViolacaoFK;
import com.indra.infra.dao.exception.RegistroInexistenteException;
import com.indra.infra.service.exception.ApplicationException;
import com.indra.sishe.dao.FeriadoDAO;
import com.indra.sishe.entity.Feriado;
import com.indra.sishe.service.FeriadoService;
import com.indra.sishe.service.StatelessServiceAb;


@Stateless
public class FeriadoServiceImpl extends StatelessServiceAb implements FeriadoService{

	private static final long serialVersionUID = -8168464255161850517L;
	
	@Autowired
	private FeriadoDAO feriadoDAO;
	
	@Override
	public Feriado save(Feriado entity) {
//		if(entity.getEstado() == null || entity.getEstado().getId() == 0){
//			entity.getEstado().setId(null);
//		}
//		if(entity.getCidade() == null || entity.getCidade().getId() == 0){
//			entity.getCidade().setId(null);
//		}
		return feriadoDAO.save(entity);
	}

	@Override
	public Feriado update(Feriado entity) throws ApplicationException {
		try {
			return feriadoDAO.update(entity);
		} catch (RegistroInexistenteException e) {
			throw new ApplicationException(e, "msg.error.registro.inexistente", "Feriado");
		}
	}

	@Override
	public List<Feriado> findAll() {
		return feriadoDAO.findAll();
	}

	@Override
	public Feriado findById(Long id) throws ApplicationException {
		try {
			return feriadoDAO.findById(id);
		} catch (RegistroInexistenteException e) {
			throw new ApplicationException(e, "msg.error.registro.inexistente", "Feriado");
		}
	}

	@Override
	public void remove(Long id) throws ApplicationException {
		try {
			feriadoDAO.remove(id);
		} catch (RegistroInexistenteException e) {
			throw new ApplicationException(e, "msg.error.registro.inexistente", "Feriado");
		}
	}
	
	@Override
	public void remove(List<Long> ids) throws ApplicationException{
		try {
			List<Object> pks = new ArrayList<Object>(ids);
			feriadoDAO.remove(pks);
		} catch (RegistroInexistenteException e) {
			throw new ApplicationException(e, "msg.error.registro.inexistente", "Feriado");
		} catch (DeletarRegistroViolacaoFK e) {
			throw new ApplicationException(e, "msg.error.registro.", "Feriado");
		}
		
	}

	@Override
	public List<Feriado> findByFilter(Feriado entity) {
		return feriadoDAO.findByFilter(entity);
	}

}
