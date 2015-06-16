package com.indra.sishe.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;

import org.springframework.beans.factory.annotation.Autowired;

import com.indra.infra.dao.exception.DeletarRegistroViolacaoFK;
import com.indra.infra.dao.exception.RegistroDuplicadoException;
import com.indra.infra.dao.exception.RegistroInexistenteException;
import com.indra.infra.service.exception.ApplicationException;
import com.indra.sishe.dao.FeriadoDAO;
import com.indra.sishe.entity.Feriado;
import com.indra.sishe.entity.Folga;
import com.indra.sishe.service.FeriadoService;
import com.indra.sishe.service.StatelessServiceAb;

@Stateless
public class FeriadoServiceImpl extends StatelessServiceAb implements FeriadoService {

	private static final long serialVersionUID = -8168464255161850517L;

	@Autowired
	private FeriadoDAO feriadoDAO;

	public boolean validarFeriado(Feriado feriadoSelecionado) throws ApplicationException {
		if (feriadoSelecionado.getNome().isEmpty()) {
			throw new ApplicationException("msg.error.campo.obrigatorio", "Nome");
		} else if (feriadoSelecionado.getNome().length() > 30) {
			throw new ApplicationException("msg.error.campo.maior.esperado", "Nome", "30");
		} else if ((feriadoSelecionado.getTipo() != 'F') && (feriadoSelecionado.getTipo() != 'M')) {
			throw new ApplicationException("msg.error.campo.obrigatorio", "Tipo");
		} else if (feriadoSelecionado.getData() == null) {
			throw new ApplicationException("msg.error.campo.obrigatorio", "Data");
		} else {
			return true;
		}
	}

	@Override
	public Feriado save(Feriado entity) throws ApplicationException {
		try {
			if (validarFeriado(entity)) {
				return feriadoDAO.save(entity);
			} else {
				return null;
			}
		} catch (RegistroDuplicadoException e) {
			throw new ApplicationException(e, "msg.error.registro.duplicado", "Feriado");
		}
	}

	@Override
	public Feriado update(Feriado entity) throws ApplicationException {
		try {
			if (validarFeriado(entity)) {
				return feriadoDAO.update(entity);
			} else {
				return null;
			}
		} catch (RegistroInexistenteException e) {
			throw new ApplicationException(e, "msg.error.registro.inexistente", "Feriado");
		} catch (RegistroDuplicadoException d) {
			throw new ApplicationException(d, "msg.error.registro.duplicado", "Feriado", "nome");
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
		} catch (DeletarRegistroViolacaoFK d) {
			throw new ApplicationException(d, "msg.error.excluir.registro.relacionado", "Feriado");
		}
	}

	@Override
	public void remove(List<Long> ids) throws ApplicationException {
		try {
			List<Object> pks = new ArrayList<Object>(ids);
			feriadoDAO.remove(pks);
		} catch (RegistroInexistenteException e) {
			throw new ApplicationException(e, "msg.error.registro.inexistente", "Feriado");
		} catch (DeletarRegistroViolacaoFK e) {
			throw new ApplicationException(e, "msg.error.registro.relacionado", "Feriado");
		}

	}

	@Override
	public List<Feriado> findByFilter(Feriado entity) {
		return feriadoDAO.findByFilter(entity);
	}

	@Override
	public Boolean verificaFeriadoPorData(Folga folga) {
		return feriadoDAO.verificaFeriadoPorData(folga);
	}

}
