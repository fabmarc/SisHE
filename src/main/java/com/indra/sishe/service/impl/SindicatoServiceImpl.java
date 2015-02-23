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
public class SindicatoServiceImpl extends StatelessServiceAb implements SindicatoService {

	private static final long serialVersionUID = -8670069751964068611L;

	@Autowired
	private SindicatoDAO sindicatoDao;

	@Override
	public Sindicato save(Sindicato entity) throws ApplicationException {
		try {
			if (validarSindicato(entity)) {
				return sindicatoDao.save(entity);
			} else {
				return null;
			}
		} catch (RegistroDuplicadoException e) {
			throw new ApplicationException(e, "msg.error.registro.duplicado", "Sindicato");
		}

	}

	@Override
	public Sindicato update(Sindicato entity) throws ApplicationException {
		try {
			return sindicatoDao.update(entity);
		} catch (RegistroInexistenteException e) {
			throw new ApplicationException(e, "msg.error.registro.inexistente", "Sindicato");
		} catch (RegistroDuplicadoException e) {
			throw new ApplicationException(e, "msg.error.campo.existente", "sindicato", "nome");
		}

	}

	@Override
	public List<Sindicato> findAll() {
		return sindicatoDao.findAll();
	}

	@Override
	public Sindicato findById(Long id) throws ApplicationException {
		try {
			return sindicatoDao.findById(id);
		} catch (RegistroInexistenteException e) {
			throw new ApplicationException(e, "msg.error.registro.inexistente", "Sindicato");
		}
	}

	@Override
	public List<Sindicato> findByFilter(Sindicato sindicato) {
		return sindicatoDao.findByFilter(sindicato);
	}

	@Override
	public void remove(Long id) throws ApplicationException {
		try {
			sindicatoDao.remove(id);
		} catch (RegistroInexistenteException e) {
			throw new ApplicationException(e, "msg.error.registro.inexistente", "Sindicato");
		} catch (DeletarRegistroViolacaoFK d) {
			throw new ApplicationException(d, "msg.error.excluir.registro.relacionado", "Sindicato");
		}
	}

	@Override
	public void remove(List<Long> ids) throws ApplicationException {
		try {
			List<Object> pks = new ArrayList<Object>(ids);
			sindicatoDao.remove(pks);
		} catch (RegistroInexistenteException e) {
			throw new ApplicationException(e, "msg.error.registro.inexistente", "Sindicato");
		} catch (DeletarRegistroViolacaoFK d) {
			throw new ApplicationException(d, "msg.error.excluir.registro.relacionado", "Sindicato");
		}
	}

	// VALIDA O SINDICATO
	public boolean validarSindicato(Sindicato sindicatoSelecionado) throws ApplicationException {

		// VALIDA A DESCRIÇÃO
		if (sindicatoSelecionado.getDescricao() == null || sindicatoSelecionado.getDescricao().isEmpty()
				|| sindicatoSelecionado.getDescricao().equals("")) {
			throw new ApplicationException("msg.error.campo.obrigatorio", "Nome do Sindicato");
		}
		// VALIDA O TAMANHO DA DESCRIÇÃO
		else if (sindicatoSelecionado.getDescricao() != null && sindicatoSelecionado.getDescricao().length() > 40) {
			throw new ApplicationException("msg.error.campo.maior.esperado", "Nome do Sindicato", "40");
		}
		// VALIDA O ESTADO
		else if (sindicatoSelecionado.getEstado() == null || sindicatoSelecionado.getEstado().getId() == null) {
			throw new ApplicationException("msg.error.campo.obrigatorio", "Estado");
		}
		// VALIDA O LIMITE POSITIVO
		else if (sindicatoSelecionado.getLimPositivo() == null) {
			throw new ApplicationException("msg.error.campo.obrigatorio", "Limite Positivo");
		}
		// VALIDA O LIMITE NEGATIVO
		else if (sindicatoSelecionado.getLimNegativo() == null) {
			throw new ApplicationException("msg.error.campo.obrigatorio", "Limite Negativo");
		} else {
			return true;
		}

	}
}
