package com.indra.sishe.service.impl;

import java.util.List;

import javax.ejb.Stateless;
import javax.interceptor.Interceptors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ejb.interceptor.SpringBeanAutowiringInterceptor;

import com.indra.infra.dao.exception.RegistroInexistenteException;
import com.indra.infra.service.exception.ApplicationException;
import com.indra.sishe.dao.ClienteDAO;
import com.indra.sishe.entity.Cliente;
import com.indra.sishe.service.ClienteService;

@Stateless
@Interceptors(SpringBeanAutowiringInterceptor.class)
public class ClienteServiceImp implements ClienteService {

	private static final long serialVersionUID = 7812505232483080092L;

	@Autowired
	private ClienteDAO clienteDao;

	@Override
	public Cliente save(Cliente entity) {
		return clienteDao.save(entity);
	}

	@Override
	public Cliente update(Cliente entity) throws ApplicationException {
		try {
			return clienteDao.update(entity);
		} catch (RegistroInexistenteException e) {
			throw new ApplicationException(e, "msg.error.registro.inexistente", "Cliente");
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
		}
	}

	@Override
	public List<Cliente> findByFilter(Cliente cliente) {
		return clienteDao.findByFilter(cliente);
	}

}
