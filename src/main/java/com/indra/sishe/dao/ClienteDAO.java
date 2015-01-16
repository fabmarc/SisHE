package com.indra.sishe.dao;

import java.util.List;

import com.indra.infra.dao.BaseDAO;
import com.indra.sishe.entity.Cliente;

public interface ClienteDAO extends BaseDAO<Cliente> {

	public List<Cliente> findByFilter(Cliente cliente);

}
