package com.indra.sishe.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;

import org.springframework.beans.factory.annotation.Autowired;

import com.indra.infra.dao.exception.DeletarRegistroViolacaoFK;
import com.indra.infra.dao.exception.RegistroDuplicadoException;
import com.indra.infra.dao.exception.RegistroInexistenteException;
import com.indra.infra.service.exception.ApplicationException;
import com.indra.sishe.dao.CargoDAO;
import com.indra.sishe.entity.Cargo;
import com.indra.sishe.service.CargoService;
import com.indra.sishe.service.StatelessServiceAb;

@Stateless
public class CargoServiceImpl extends StatelessServiceAb implements CargoService {

	private static final long serialVersionUID = 6547321167422397681L;

	@Autowired
	private CargoDAO cargoDao;

	public CargoServiceImpl() {

		System.out.println("Criou o CargoServiceImpl");
	}

	@Override
	public Cargo save(Cargo entity) throws ApplicationException {
		try {
			if (validarCargo(entity)) {
				return cargoDao.save(entity);
			} else {
				return null;
			}
		} catch (RegistroDuplicadoException e) {
			throw new ApplicationException(e, "msg.error.registro.duplicado", "Cargo");
		}
	}

	@Override
	public Cargo update(Cargo entity) throws ApplicationException {

		try {
			if (validarCargo(entity)) {
				return cargoDao.update(entity);
			} else {
				return null;
			}
		} catch (RegistroInexistenteException e) {
			throw new ApplicationException(e, "msg.error.registro.inexistente", "Cargo");
		} catch (RegistroDuplicadoException d) {
			throw new ApplicationException(d, "msg.error.campo.existente", "cargo", "nome");
		}
	}

	@Override
	public List<Cargo> findAll() {
		return cargoDao.findAll();
	}

	@Override
	public Cargo findById(Long id) throws ApplicationException {
		try {
			return cargoDao.findById(id);
		} catch (RegistroInexistenteException e) {
			throw new ApplicationException(e, "msg.error.registro.inexistente", "Cargo");
		}
	}

	@Override
	public List<Cargo> findByFilter(Cargo cargoFiltro) {
		return cargoDao.findByFilter(cargoFiltro);
	}

	@Override
	public void remove(Long id) throws ApplicationException {
		try {
			cargoDao.remove(id);
		} catch (RegistroInexistenteException e) {
			throw new ApplicationException(e, "msg.error.registro.inexistente", "Cargo");
		} catch (DeletarRegistroViolacaoFK d) {
			throw new ApplicationException(d, "msg.error.excluir.registro.relacionado", "Cargo");
		}
	}

	@Override
	public void remove(List<Long> ids) throws ApplicationException {
		try {
			List<Object> pks = new ArrayList<Object>(ids);
			cargoDao.remove(pks);
		} catch (RegistroInexistenteException e) {
			throw new ApplicationException(e, "msg.error.registro.inexistente", "Cargo");
		} catch (DeletarRegistroViolacaoFK d) {
			throw new ApplicationException(d, "msg.error.excluir.registro.relacionado", "Cargo");
		}
	}

	public boolean validarCargo(Cargo entity) throws ApplicationException {
		if (entity.getNome().isEmpty()) {
			throw new ApplicationException("msg.error.campo.obrigatorio", "Nome");
		} else if (entity.getNome().length() > 40) {
			throw new ApplicationException("msg.error.campo.maior.esperado", "Nome", "40");
		} else {
			return true;
		}
	}

}
