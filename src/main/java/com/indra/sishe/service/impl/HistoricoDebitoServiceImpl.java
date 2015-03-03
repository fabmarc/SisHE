package com.indra.sishe.service.impl;

import java.util.List;

import javax.ejb.Stateless;

import org.springframework.beans.factory.annotation.Autowired;

import com.indra.infra.dao.exception.RegistroDuplicadoException;
import com.indra.infra.service.exception.ApplicationException;
import com.indra.sishe.dao.HistoricoDebitoDao;
import com.indra.sishe.entity.HistoricoDebito;
import com.indra.sishe.entity.Usuario;
import com.indra.sishe.service.HistoricoDebitoService;
import com.indra.sishe.service.StatelessServiceAb;

@Stateless
public class HistoricoDebitoServiceImpl extends StatelessServiceAb implements HistoricoDebitoService{

	private static final long serialVersionUID = 5996539670806141733L;

	@Autowired
	private HistoricoDebitoDao historicoDebitoDao;
	
	@Override
	public HistoricoDebito save(HistoricoDebito entity) throws ApplicationException {
		try {
			return historicoDebitoDao.save(entity);
		} catch (RegistroDuplicadoException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public HistoricoDebito update(HistoricoDebito entity) throws ApplicationException {
		return null;
	}

	@Override
	public List<HistoricoDebito> findAll() {
		return null;
	}

	@Override
	public HistoricoDebito findById(Long id) throws ApplicationException {
		return null;
	}

	@Override
	public void remove(Long id) throws ApplicationException {
	}

	@Override
	public void remove(List<Long> ids) throws ApplicationException {
	}

	@Override
	public List<HistoricoDebito> findByUsuarioEMes(Usuario user, String mes, String ano) {
		return historicoDebitoDao.findByUsuarioEMes(user, mes, ano);
	}

}
