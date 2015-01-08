package com.indra.sishe.service.impl;

import java.util.List;

import javax.ejb.Stateless;

import org.springframework.beans.factory.annotation.Autowired;

import com.indra.infra.dao.exception.RegistroInexistenteException;
import com.indra.sishe.dao.CargoDAO;
import com.indra.sishe.entity.Cargo;
import com.indra.sishe.service.CargoService;
import com.indra.sishe.service.StatelessServiceAb;
import com.indra.infra.service.exception.ApplicationException;

@Stateless
public class CargoServiceImp extends StatelessServiceAb implements CargoService{

	private static final long serialVersionUID = 6547321167422397681L;

	@Autowired
	private CargoDAO cargoDao;
	
	public CargoServiceImp(){
		System.out.println("Criou o CargoServiceImpl");
	}
	
	@Override
	public Cargo save(Cargo entity) {
		return cargoDao.save(entity);		
	}

	@Override
	public Cargo update(Cargo entity) throws ApplicationException {
		try {
			return cargoDao.update(entity);
		} catch (RegistroInexistenteException e) {
			throw new ApplicationException(e, "msg.error.registro.inexistente", "Cargo");
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
	public Cargo pesquisarNome(String nome) {
		return cargoDao.pesquinarNome(nome);
	}

	@Override
	public void remove(Long id) throws ApplicationException {
		try {
			cargoDao.remove(id);
		} catch (RegistroInexistenteException e) {
			throw new ApplicationException(e, "msg.error.registro.inexistente", "Cargo");
		}
		
	}

	@Override
	public void remove(List<Long> ids) throws ApplicationException {
		// TODO Auto-generated method stub
	}

}
