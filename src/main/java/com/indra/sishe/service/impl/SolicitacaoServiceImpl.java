package com.indra.sishe.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;

import org.springframework.beans.factory.annotation.Autowired;

import com.indra.infra.dao.exception.DeletarRegistroViolacaoFK;
import com.indra.infra.dao.exception.RegistroInexistenteException;
import com.indra.infra.service.exception.ApplicationException;
import com.indra.sishe.controller.usuario.UsuarioLogado;
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
	}

	public boolean validarRemove(Solicitacao solicitacao) throws ApplicationException {
		if (UsuarioLogado.getPermissoes().contains("ROLE_GERENTE")) {
			if (solicitacao.getStatusGerente().getId() > 0) { 
				throw new ApplicationException("msg.error.excluir.solicitacao.avaliada", "Gerente"); 
			}else{
				return true;
			}
		}else if (UsuarioLogado.getPermissoes().contains("ROLE_LIDER")) {
			if (solicitacao.getStatusGerente().getId() > 0) { 
				throw new ApplicationException("msg.error.excluir.solicitacao.avaliada", "Gerente"); 
			}else{
				return true;
			}
		}else if (UsuarioLogado.getPermissoes().contains("ROLE_FUNCIONARIO")) {
			if (solicitacao.getStatusGerente().getId() > 0) {
				throw new ApplicationException("msg.error.excluir.solicitacao.avaliada", "Gerente");
			}else if (solicitacao.getStatusLider().getId() > 0) { 
				throw new ApplicationException("msg.error.excluir.solicitacao.avaliada", "Líder"); 
			}else{
				return true;
			}
		}else{
			return false;
		}
	}

	@Override
	public void removeSolicitacoes(List<Solicitacao> solicitacoesParaRemover) throws ApplicationException {

		try {
			int size = solicitacoesParaRemover.size();
			ArrayList<Long> ids = new ArrayList<Long>(size);
			for (Solicitacao solicitacao : solicitacoesParaRemover) {
				if (validarRemove(solicitacao)) {
					ids.add(solicitacao.getId());
				}
			}

			List<Object> pks = new ArrayList<Object>(ids);
			solicitacaoDao.remove(pks);
		} catch (RegistroInexistenteException e) {
			e.printStackTrace();
		} catch (DeletarRegistroViolacaoFK e) {
			e.printStackTrace();
		}
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

	@Override
	public List<Solicitacao> findByFilterByUsuario(Solicitacao solicitacaoFiltro) {
		return solicitacaoDao.findByFilterByUsuario(solicitacaoFiltro);
	}

	@Override
	public void remove(List<Long> ids) throws ApplicationException {
	}

}