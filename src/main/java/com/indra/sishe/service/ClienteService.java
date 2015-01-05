package com.indra.sishe.service;

import java.util.List;

import javax.ejb.Local;

import com.indra.infra.service.BaseService;
import com.indra.sishe.entity.Cliente;

@Local
public interface ClienteService extends BaseService<Cliente> {

	public List<Cliente> findByFilter(Cliente cliente);

}
