package com.indra.sishe.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;

import org.springframework.beans.factory.annotation.Autowired;

import com.indra.infra.dao.exception.DeletarRegistroViolacaoFK;
import com.indra.infra.dao.exception.RegistroDuplicadoException;
import com.indra.infra.dao.exception.RegistroInexistenteException;
import com.indra.infra.service.exception.ApplicationException;
import com.indra.sishe.dao.SistemaDAO;
import com.indra.sishe.entity.Sistema;
import com.indra.sishe.service.SistemaService;
import com.indra.sishe.service.StatelessServiceAb;

@Stateless
public class SistemaServiceImpl extends StatelessServiceAb implements
		SistemaService {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public SistemaServiceImpl() {
		// TODO Auto-generated constructor stub
	}

	@Autowired
	private SistemaDAO sistemaDao;

	@Override
	public Sistema save(Sistema entity) throws ApplicationException {

		try {
			return sistemaDao.save(entity);
		} catch (RegistroDuplicadoException e) {
			// TODO: handle exception
			throw new ApplicationException(e, "msg.error.registro.duplicado",
					"Sistema");
		}

	}

	@Override
	public Sistema update(Sistema entity) throws ApplicationException {
		// TODO Auto-generated method stub
		try {
			return sistemaDao.update(entity);
		} catch (RegistroDuplicadoException e) {
			// TODO: handle exception
			throw new ApplicationException(e, "msg.error.registro.duplicado");
		} catch (RegistroInexistenteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new ApplicationException(e, "msg.error.registro.inexistente");
		}

	}

	@Override
	public List<Sistema> findAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Sistema> findByFilter(Sistema sistema) {
		return sistemaDao.findByFilter(sistema);
	}

	@Override
	public Sistema findById(Long id) throws ApplicationException {
		// TODO Auto-generated method stub
		try {
			return sistemaDao.findById(id);
		} catch (RegistroInexistenteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new ApplicationException(e, "msg.error.registro.inexistente",
					"Sistema");
		}

	}

	@Override
	public void remove(Long id) throws ApplicationException {
		// TODO Auto-generated method stub

	}

	@Override
	public void remove(List<Long> ids) throws ApplicationException {
		// TODO Auto-generated method stub
		try {
			List<Object> pks = new ArrayList<Object>(ids);
			sistemaDao.remove(pks);
		} catch (RegistroInexistenteException e) {
			// TODO Auto-generated catch block
			throw new ApplicationException(e, "msg.error.registro.inexistente",
					"Sistema");
		} catch (DeletarRegistroViolacaoFK e) {
			// TODO Auto-generated catch block
			throw new ApplicationException(e,
					"msg.error.excluir.registro.relacionado", "Sistema");
		}
	}

}
