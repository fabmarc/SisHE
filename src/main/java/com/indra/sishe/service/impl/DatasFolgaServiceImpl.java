package com.indra.sishe.service.impl;

import java.util.List;

import javax.ejb.Stateless;

import org.springframework.beans.factory.annotation.Autowired;

import com.indra.infra.dao.exception.DeletarRegistroViolacaoFK;
import com.indra.infra.dao.exception.RegistroDuplicadoException;
import com.indra.infra.dao.exception.RegistroInexistenteException;
import com.indra.infra.service.exception.ApplicationException;
import com.indra.sishe.dao.DatasFolgaDAO;
import com.indra.sishe.entity.DatasFolga;
import com.indra.sishe.service.DatasFolgaService;
import com.indra.sishe.service.StatelessServiceAb;

@Stateless
public class DatasFolgaServiceImpl extends StatelessServiceAb implements DatasFolgaService{

	private static final long serialVersionUID = -4179206047838774489L;
	
	
	@Autowired
	private DatasFolgaDAO datasFolgaDAO;
	
	@Override
	public DatasFolga save(DatasFolga entity) throws ApplicationException {
		try {
			return datasFolgaDAO.save(entity);
		} catch (RegistroDuplicadoException e) {
			throw new ApplicationException(e, "msg.error.registro.duplicado", "Data Folga");
		}
	}

	@Override
	public DatasFolga update(DatasFolga entity) throws ApplicationException {
		return null;
	}

	@Override
	public List<DatasFolga> findAll() {
		return null;
	}

	@Override
	public DatasFolga findById(Long id) throws ApplicationException {
		return null;
	}

	@Override
	public void remove(Long id) throws ApplicationException {
	}

	@Override
	public void remove(List<Long> ids) throws ApplicationException {
		try {
			datasFolgaDAO.remove(ids);
		} catch (RegistroInexistenteException e) {
			throw new ApplicationException(e, "msg.error.registro.inexistente", "Data Folga");
		} catch (DeletarRegistroViolacaoFK d) {
			throw new ApplicationException(d, "msg.error.excluir.registro.relacionado", "Data Folga");
		}
	}

	@Override
	public List<DatasFolga> findDatasBySolicitacaoFolga(Long idFolga) {
		return datasFolgaDAO.findDatasBySolicitacaoFolga(idFolga);
	}

}
