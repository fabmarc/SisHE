package com.indra.sishe.service.impl;

import java.util.List;

import javax.ejb.Stateless;

import org.springframework.beans.factory.annotation.Autowired;

import com.indra.infra.dao.exception.RegistroInexistenteException;
import com.indra.infra.service.exception.ApplicationException;
import com.indra.sishe.dao.SolicitacaoDAO;
import com.indra.sishe.entity.Solicitacao;
import com.indra.sishe.entity.Usuario;
import com.indra.sishe.service.SolicitacaoService;
import com.indra.sishe.service.StatelessServiceAb;

@Stateless
public class SolicitacaoServiceImpl extends StatelessServiceAb implements SolicitacaoService {

	private static final long serialVersionUID = -5541504816519972712L;

	@Autowired
	private SolicitacaoDAO solicitacaoDao;

	@Override
	public Solicitacao save(Solicitacao entity) throws ApplicationException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Solicitacao update(Solicitacao entity) throws ApplicationException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Solicitacao> findAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Solicitacao findById(Long id) throws ApplicationException {
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
	public List<Solicitacao> findByLider(Usuario lider) {
		return solicitacaoDao.findByLider(lider);
	}

	@Override
	public List<Solicitacao> findByGerente(Usuario gerente) {
		return solicitacaoDao.findByGerente(gerente);
	}

	@Override
	public List<Solicitacao> findByFilter(Solicitacao solicitacaoFiltro) {
		return solicitacaoDao.findByFilter(solicitacaoFiltro);
	}

	@Override
	public void liderAcaoSolicitacao(List<Long> ids, int status) throws ApplicationException {
		try {
			solicitacaoDao.liderAcaoSolicitacao(ids, status);
		} catch (RegistroInexistenteException e) {
			throw new ApplicationException(e, "msg.error.registro.inexistente", "Solicitação");
		}
	}

	@Override
	public void gerenteAcaoSolicitacao(List<Long> ids, int status) throws ApplicationException {
		try {
			solicitacaoDao.gerenteAcaoSolicitacao(ids, status);
		} catch (RegistroInexistenteException e) {
			throw new ApplicationException(e, "msg.error.registro.inexistente", "Solicitação");
		}
	}

}