package com.indra.sishe.service.impl;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.indra.infra.dao.exception.DeletarRegistroViolacaoFK;
import com.indra.infra.dao.exception.RegistroDuplicadoException;
import com.indra.infra.dao.exception.RegistroInexistenteException;
import com.indra.infra.service.exception.ApplicationException;
import com.indra.sishe.dao.FolgaDAO;
import com.indra.sishe.entity.Folga;
import com.indra.sishe.service.FolgaService;

public class FolgaServiceImpl implements FolgaService {

	private static final long serialVersionUID = -6545774307270513446L;
	
	@Autowired
	private FolgaDAO folgaDAO;

	@Override
	public Folga save(Folga entity) throws ApplicationException {
		try {
			return folgaDAO.save(entity);
		} catch (RegistroDuplicadoException e) {
			throw new ApplicationException(e, "msg.error.registro.duplicado", "Folga");
		}
	}

	@Override
	public Folga update(Folga entity) throws ApplicationException {
		try {
			return folgaDAO.update(entity);
		} catch (RegistroInexistenteException e) {
			throw new ApplicationException(e, "msg.error.registro.inexistente", "Folga");
		} catch (RegistroDuplicadoException d) {
			throw new ApplicationException(d, "msg.error.registro.duplicado", "Folga", "nome");
		}
	}

	@Override
	public List<Folga> findAll() {
		return folgaDAO.findAll();
	}

	@Override
	public Folga findById(Long id) throws ApplicationException {
		try {
			return folgaDAO.findById(id);
		} catch (RegistroInexistenteException e) {
			throw new ApplicationException(e, "msg.error.registro.inexistente", "Folga");
		}
	}

	@Override
	public void remove(Long id) throws ApplicationException {
	}

	@Override
	public void remove(List<Long> ids) throws ApplicationException {
		try {
			folgaDAO.remove(ids);
		} catch (RegistroInexistenteException e) {
			throw new ApplicationException(e, "msg.error.registro.inexistente", "Folga");
		} catch (DeletarRegistroViolacaoFK d) {
			throw new ApplicationException(d, "msg.error.excluir.registro.relacionado", "Folga");
		}
	}

	@Override
	public Boolean validarFolga(Folga folga) {
		
		Date hoje = new Date();
		
//		if (folga.getDataFolga().before(hoje)) {
//			throw new ApplicationException("msg.error.data.superior");
//		}else if (folga.get) {
//			
//		}
		
		return true;
	}

}
