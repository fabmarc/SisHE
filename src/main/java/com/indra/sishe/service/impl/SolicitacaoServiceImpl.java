package com.indra.sishe.service.impl;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;

import org.springframework.beans.factory.annotation.Autowired;

import com.indra.infra.dao.exception.DeletarRegistroViolacaoFK;
import com.indra.infra.dao.exception.RegistroDuplicadoException;
import com.indra.infra.dao.exception.RegistroInexistenteException;
import com.indra.infra.service.exception.ApplicationException;
import com.indra.sishe.controller.usuario.UsuarioLogado;
import com.indra.sishe.dao.SolicitacaoDAO;
import com.indra.sishe.entity.Solicitacao;
import com.indra.sishe.service.SolicitacaoService;
import com.indra.sishe.service.StatelessServiceAb;

@Stateless
public class SolicitacaoServiceImpl extends StatelessServiceAb implements SolicitacaoService {

	private static final long serialVersionUID = -5541504816519972712L;

	@Autowired
	private SolicitacaoDAO solicitacaoDao;

	@Override
	public Solicitacao save(Solicitacao entity) throws ApplicationException {		
		try {
			return solicitacaoDao.save(entity);
		} catch (RegistroDuplicadoException e) {			
			throw new ApplicationException(e, "msg.error.registro.duplicado", "Solicitação");
		}
	}
	
	@Override
	public Solicitacao update(Solicitacao entity) throws ApplicationException {
		return null;
	}

	@Override
	public List<Solicitacao> findAll() {
		return null;
	}

	@Override
	public Solicitacao findById(Long id) throws ApplicationException {
		return null;
	}

	@Override
	public void remove(Long id) throws ApplicationException { }

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
	public List<Solicitacao> findByLider(Solicitacao solicitacaoFiltro) {
		return solicitacaoDao.findByLider(solicitacaoFiltro);
	}

	@Override
	public List<Solicitacao> findByGerente(Solicitacao solicitacaoFiltro) {
		return solicitacaoDao.findByGerente(solicitacaoFiltro);
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
	
	@Override
	public boolean validarSolicitacao(Solicitacao solicitacao) throws ApplicationException, ParseException {
		
		DateFormat dateFormatter;
		dateFormatter = DateFormat.getDateInstance(DateFormat.SHORT);
		
		String hoje = dateFormatter.format(new Date());
		String dtEscolhida= dateFormatter.format(solicitacao.getData());
		
		if (solicitacao.getHoraInicio() == null ) {
			throw new ApplicationException("msg.error.campo.obrigatorio", "Hora Inicial");
		}		
		else if(solicitacao.getHoraFinal() == null){
			throw new ApplicationException("msg.error.campo.obrigatorio", "Hora Final");
		}		
		else if ((solicitacao.getHoraInicio() != null || solicitacao.getHoraFinal() != null) && solicitacao.getHoraInicio().after(solicitacao.getHoraFinal())) {
			throw new ApplicationException("msg.error.intervalo.incorreto","Hora Inicial", "Hora Final");
		}		
		else if (solicitacao.getDescricao() != null && solicitacao.getDescricao().length() > 500) {
			throw new ApplicationException("msg.error.campo.maior.esperado","Descricao","500");			
		}	
		else if(solicitacao.getDescricao() == null){
			throw new ApplicationException("msg.error.campo.obrigatorio", "Descricao");
		}else if(solicitacao.getDescricao() != null && (solicitacao.getDescricao().length() > 500)){
			throw new ApplicationException("msg.error.campo.maior.esperado", "Descricao", "500");
		}else if(solicitacao.getSistema() ==  null || solicitacao.getSistema().getId() == 0){
			throw new ApplicationException("msg.error.campo.obrigatorio", "Sistema");
		}
		else if (!hoje.equalsIgnoreCase(dtEscolhida) && dateFormatter.parse(dtEscolhida).before(dateFormatter.parse(hoje))){
			
			throw new ApplicationException("msg.error.data");
		}
		else{
			return true;
		}		
	}
}
