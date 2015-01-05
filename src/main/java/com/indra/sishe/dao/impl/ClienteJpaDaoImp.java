package com.indra.sishe.dao.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.indra.infra.dao.BaseDAOImpl;
import com.indra.sishe.dao.ClienteDAO;
import com.indra.sishe.entity.Cliente;

public class ClienteJpaDaoImp extends BaseDAOImpl<Cliente> implements ClienteDAO {
	
	@Override
	@SuppressWarnings("unchecked")
	public List<Cliente> findByFilter(Cliente cliente) {
		
		// consultar com ignore case
		Criteria c = getSession().createCriteria(Cliente.class);
		String nomeCliente = cliente.getNomeCliente();
		c.add(Restrictions.ilike("nomeCliente", "%" + (nomeCliente == null ? "" : nomeCliente) + "%", MatchMode.ANYWHERE));
		c.addOrder(Order.asc("nomeCliente"));
		return c.list();
	}

}
