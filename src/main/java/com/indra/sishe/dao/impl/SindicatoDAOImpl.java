package com.indra.sishe.dao.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.indra.infra.dao.BaseDAOImpl;
import com.indra.sishe.dao.SindicatoDAO;
import com.indra.sishe.entity.Sindicato;

public class SindicatoDAOImpl extends BaseDAOImpl<Sindicato> implements SindicatoDAO{

	@SuppressWarnings("unchecked")
	@Override
	public List<Sindicato> pesquisarPorEstado(String estado) {
		// TODO Auto-generated method stub
		Criteria c = getSession().createCriteria(Sindicato.class);
		// c.add(Restrictions.like("nomeCliente", "%" + nome + "%"));//sem
		// ignore case
		c.add(Restrictions.eq("titulo",estado));// consultar com ignore case
		c.addOrder(Order.asc("titulo"));
		return c.list();
	}

	@Override
	public List<Sindicato> pesquisarPorNome(String nome) {
		// TODO Auto-generated method stub
		return null;
	}

	
}
