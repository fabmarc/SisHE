package com.indra.sishe.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.indra.infra.service.exception.ApplicationException;
import com.indra.sishe.dao.FolgaDebitarDAO;
import com.indra.sishe.entity.FolgaDebitar;
import com.indra.sishe.service.FolgaDebitarService;
import com.indra.sishe.service.StatelessServiceAb;

public class FolgaDebitarServiceImpl extends StatelessServiceAb implements FolgaDebitarService{

	private static final long serialVersionUID = -7799735793755724125L;
	
	@Autowired
	private FolgaDebitarDAO folgaDebitarDAO;

	@Override
	public FolgaDebitar save(FolgaDebitar entity) throws ApplicationException {
		return null;
	}

	@Override
	public FolgaDebitar update(FolgaDebitar entity) throws ApplicationException {
		return null;
	}

	@Override
	public List<FolgaDebitar> findAll() {
		return null;
	}

	@Override
	public FolgaDebitar findById(Long id) throws ApplicationException {
		return null;
	}

	@Override
	public void remove(Long id) throws ApplicationException {
	}

	@Override
	public void remove(List<Long> ids) throws ApplicationException {
	}

}
