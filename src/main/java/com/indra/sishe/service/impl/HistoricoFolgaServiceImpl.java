package com.indra.sishe.service.impl;

import java.util.List;

import javax.ejb.Stateless;

import org.springframework.beans.factory.annotation.Autowired;

import com.indra.infra.service.exception.ApplicationException;
import com.indra.sishe.dao.HistoricoFolgaDAO;
import com.indra.sishe.entity.HistoricoFolga;
import com.indra.sishe.service.HistoricoFolgaService;
import com.indra.sishe.service.StatelessServiceAb;

@Stateless
public class HistoricoFolgaServiceImpl extends StatelessServiceAb implements HistoricoFolgaService{
	
	private static final long serialVersionUID = 4488126536563949411L;

	@Autowired
	private HistoricoFolgaDAO historicoFolgaDAO;
	
	@Override
	public HistoricoFolga save(HistoricoFolga entity) throws ApplicationException {
		return null;
	}

	@Override
	public HistoricoFolga update(HistoricoFolga entity) throws ApplicationException {
		return null;
	}

	@Override
	public List<HistoricoFolga> findAll() {
		return null;
	}

	@Override
	public HistoricoFolga findById(Long id) throws ApplicationException {
		return null;
	}

	@Override
	public void remove(Long id) throws ApplicationException {
	}

	@Override
	public void remove(List<Long> ids) throws ApplicationException {
	}

}
