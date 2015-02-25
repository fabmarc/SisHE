package com.indra.sishe.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;

import org.springframework.beans.factory.annotation.Autowired;

import com.indra.infra.dao.exception.DeletarRegistroViolacaoFK;
import com.indra.infra.dao.exception.RegistroDuplicadoException;
import com.indra.infra.dao.exception.RegistroInexistenteException;
import com.indra.infra.service.exception.ApplicationException;
import com.indra.sishe.dao.PeriodoDAO;
import com.indra.sishe.entity.Periodo;
import com.indra.sishe.entity.Regra;
import com.indra.sishe.service.PeriodoService;
import com.indra.sishe.service.StatelessServiceAb;

@Stateless
public class PeriodoServiceImpl extends StatelessServiceAb implements PeriodoService {

	@Autowired
	private PeriodoDAO periodoDao;

	private static final long serialVersionUID = -29269946960860925L;

	@Override
	public Periodo save(Periodo entity) throws ApplicationException {
		try {
			if (validarPeriodo(entity)) {
				return periodoDao.save(entity);
			} else {
				return null;
			}
		} catch (RegistroDuplicadoException e) {
			throw new ApplicationException(e, "msg.error.faixa.tempo.existente");
		}
	}

	@Override
	public Periodo update(Periodo entity) throws ApplicationException {
		try {
			if (validarPeriodo(entity)) {
				return periodoDao.update(entity);
			} else {
				return null;
			}
		} catch (RegistroInexistenteException e) {
			throw new ApplicationException(e, "msg.error.registro.inexistente", "Periodo");
		} catch (RegistroDuplicadoException d) {
			if (d.getMessageCode().equalsIgnoreCase("msg.error.faixa.tempo.existente")) {
				throw new ApplicationException(d, "msg.error.faixa.tempo.existente");
			} else {
				throw new ApplicationException(d, "msg.error.campo.existente", "periodo", "horário");
			}
		}
	}

	@Override
	public List<Periodo> findAll() {
		return periodoDao.findAll();
	}

	@Override
	public Periodo findById(Long id) throws ApplicationException {
		try {
			return periodoDao.findById(id);
		} catch (RegistroInexistenteException e) {
			throw new ApplicationException(e, "msg.error.registro.inexistente", "Periodo");
		}
	}

	@Override
	public void remove(Long id) throws ApplicationException {
		try {
			periodoDao.remove(id);
		} catch (RegistroInexistenteException e) {
			throw new ApplicationException(e, "msg.error.registro.inexistente", "Periodo");
		} catch (DeletarRegistroViolacaoFK d) {
			throw new ApplicationException(d, "msg.error.excluir.registro.relacionado", "Periodo");
		}
	}

	@Override
	public void remove(List<Long> ids) throws ApplicationException {
		try {
			List<Object> pks = new ArrayList<Object>(ids);
			periodoDao.remove(pks);
		} catch (RegistroInexistenteException e) {
			throw new ApplicationException(e, "msg.error.registro.inexistente", "Periodo");
		} catch (DeletarRegistroViolacaoFK d) {
			throw new ApplicationException(d, "msg.error.excluir.registro.relacionado", "Periodo");
		}
	}

	@Override
	public List<Periodo> findByFilter(Periodo entity) {
		return periodoDao.findByFilter(entity);
	}

	@Override
	public List<Periodo> findByRegra(Regra entity) {
		return periodoDao.findByRegra(entity);
	}

	public boolean validarPeriodo(Periodo entity) throws ApplicationException {

		if (entity.getDiaSemana() == null || entity.getDiaSemana().numeroDia() < 1
				|| entity.getDiaSemana().numeroDia() > 7) {
			throw new ApplicationException("msg.error.campo.obrigatorio", "Dia da semana");
		} else if (entity.getHoraInicio() == null) {
			throw new ApplicationException("msg.error.campo.obrigatorio", "Hora Inicio");
		} else if (entity.getHoraFim() == null) {
			throw new ApplicationException("msg.error.campo.obrigatorio", "Hora Fim");
		} else if (entity.getHoraInicio().getTime() >= entity.getHoraFim().getTime()) {
			throw new ApplicationException("msg.error.intervalo.incorreto", "Hora Inicio", "Hora Fim");
		} else if (entity.getPorcentagem() == null) {
			throw new ApplicationException("msg.error.campo.obrigatorio", "Porcentagem");
		} else if (entity.getPorcentagem() > 100 || entity.getPorcentagem() < 0) {
			throw new ApplicationException("msg.error.valor.entre", "Porcentagem", "0%", "100%");
		} else {
			return true;
		}
	}

}
