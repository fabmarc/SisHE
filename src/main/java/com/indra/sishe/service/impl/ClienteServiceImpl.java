package com.indra.sishe.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;

import org.springframework.beans.factory.annotation.Autowired;

import com.indra.infra.dao.exception.DeletarRegistroViolacaoFK;
import com.indra.infra.dao.exception.RegistroDuplicadoException;
import com.indra.infra.dao.exception.RegistroInexistenteException;
import com.indra.infra.service.exception.ApplicationException;
import com.indra.sishe.dao.ClienteDAO;
import com.indra.sishe.entity.Cliente;
import com.indra.sishe.service.ClienteService;
import com.indra.sishe.service.StatelessServiceAb;

@Stateless
public class ClienteServiceImpl extends StatelessServiceAb implements ClienteService {

	private static final long serialVersionUID = 7812505232483080092L;

	@Autowired
	private ClienteDAO clienteDao;

	public ClienteServiceImpl() {
	}

	@Override
	public Cliente save(Cliente entity) {
		try {
			return clienteDao.save(entity);
		} catch (RegistroDuplicadoException e) {
			e.printStackTrace();
		}
		return entity;
	}

	@Override
	public Cliente update(Cliente entity) throws ApplicationException {
		try {
			return clienteDao.update(entity);
		} catch (RegistroInexistenteException e) {
			throw new ApplicationException(e, "msg.error.registro.inexistente", "Cliente");
		} catch (RegistroDuplicadoException e) {
			throw new ApplicationException(e, "msg.error.campo.existente", "cliente", "nome");
		}
	}

	@Override
	public List<Cliente> findAll() {
		return clienteDao.findAll();
	}

	@Override
	public Cliente findById(Long id) throws ApplicationException {
		try {
			return clienteDao.findById(id);
		} catch (RegistroInexistenteException e) {
			throw new ApplicationException(e, "msg.error.registro.inexistente", "Cliente");
		}
	}

	@Override
	public void remove(Long id) throws ApplicationException {
		try {
			clienteDao.remove(id);
		} catch (RegistroInexistenteException e) {
			throw new ApplicationException(e, "msg.error.registro.inexistente", "Cliente");
		} catch (DeletarRegistroViolacaoFK e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public List<Cliente> findByFilter(Cliente cliente) {
		return clienteDao.findByFilter(cliente);
	}

	@Override
	public void remove(List<Long> ids) throws ApplicationException {
		try {
			List<Object> pks = new ArrayList<Object>(ids);
			clienteDao.remove(pks);
		} catch (RegistroInexistenteException e) {
			throw new ApplicationException(e, "msg.error.registro.inexistente", "Cliente");
		} catch (DeletarRegistroViolacaoFK e) {
			e.printStackTrace();
		}
	}

}
