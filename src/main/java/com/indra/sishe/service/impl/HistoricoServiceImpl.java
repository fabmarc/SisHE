package com.indra.sishe.service.impl;

import java.util.List;

import javax.ejb.Stateless;

import org.springframework.beans.factory.annotation.Autowired;

import com.indra.infra.service.exception.ApplicationException;
import com.indra.sishe.dao.HistoricoDAO;
import com.indra.sishe.entity.Historico;
import com.indra.sishe.service.HistoricoService;
import com.indra.sishe.service.StatelessServiceAb;

@Stateless
public class HistoricoServiceImpl extends StatelessServiceAb implements HistoricoService{

	private static final long serialVersionUID = 4285770176009473735L;
	
	@Autowired
	private HistoricoDAO historicoDao;
	
	public HistoricoServiceImpl(){
	}

	@Override
	public Historico save(Historico entity) throws ApplicationException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Historico update(Historico entity) throws ApplicationException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Historico> findAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Historico findById(Long id) throws ApplicationException {
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
		
	}

	@Override
	public void gerarHistorico(List<Long> ids, String descricao) {
		historicoDao.gerarHistorico(ids, descricao);
	}
	
	

}
