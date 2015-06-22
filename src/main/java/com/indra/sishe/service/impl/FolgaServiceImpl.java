package com.indra.sishe.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;

import org.springframework.beans.factory.annotation.Autowired;

import com.indra.infra.dao.exception.DeletarRegistroViolacaoFK;
import com.indra.infra.dao.exception.RegistroDuplicadoException;
import com.indra.infra.dao.exception.RegistroInexistenteException;
import com.indra.infra.service.exception.ApplicationException;
import com.indra.sishe.dao.DatasFolgaDAO;
import com.indra.sishe.dao.FeriadoDAO;
import com.indra.sishe.dao.FolgaDAO;
import com.indra.sishe.entity.DatasFolga;
import com.indra.sishe.entity.Folga;
import com.indra.sishe.service.FolgaService;
import com.indra.sishe.service.StatelessServiceAb;

@Stateless
public class FolgaServiceImpl extends StatelessServiceAb implements FolgaService {

	private static final long serialVersionUID = -6545774307270513446L;
	
	@Autowired
	private FolgaDAO folgaDAO;
	
	@Autowired
	private DatasFolgaDAO datasFolgaDAO;
	
	@Autowired
	private FeriadoDAO feriadoDAO;

	@Override
	public Folga save(Folga entity) throws ApplicationException {
		try {
			Calendar dataIndex = Calendar.getInstance();
			Calendar dataFim = Calendar.getInstance();
			
			dataIndex.setTime(entity.getEvent().getStartDate());
			dataIndex.add(Calendar.DATE, 1);
			dataFim.setTime(entity.getEvent().getEndDate());
			List<DatasFolga> datas = new ArrayList<DatasFolga>();
			
			for(;dataIndex.getTime().before(dataFim.getTime()) || dataIndex.getTime().equals(dataFim.getTime()); dataIndex.add(Calendar.DATE, 1)){
				datas.add(new DatasFolga(dataIndex.getTime()));
			}
			entity.setDatasFolga(datas);
			
			Folga folga = folgaDAO.save(entity);
			
			datasFolgaDAO.insereTodasDatasPorFolga(entity.getDatasFolga());
			return folga;
		} catch (RegistroDuplicadoException e) {
			throw new ApplicationException(e, "msg.error.registro.duplicado", "Folga");
		}
	}

	@Override
	public Folga update(Folga entity) throws ApplicationException {
		try {
			Folga folga = folgaDAO.update(entity);
			datasFolgaDAO.removeTodasDatasPorFolga(entity.getId());
			datasFolgaDAO.insereTodasDatasPorFolga(entity.getDatasFolga());
			return folga;
		} catch (RegistroInexistenteException e) {
			throw new ApplicationException(e, "msg.error.registro.inexistente", "Folga");
		} catch (RegistroDuplicadoException d) {
			throw new ApplicationException(d, "msg.error.registro.duplicado", "Folga", "nome");
		}
	}

	@Override
	public List<Folga> findAll() {
		return folgaDAO.findAll();
	}

	@Override
	public Folga findById(Long id) throws ApplicationException {
		try {
			return folgaDAO.findById(id);
		} catch (RegistroInexistenteException e) {
			throw new ApplicationException(e, "msg.error.registro.inexistente", "Folga");
		}
	}

	@Override
	public void remove(Long id) throws ApplicationException {
	}

	@Override
	public void remove(List<Long> ids) throws ApplicationException {
		try {
			folgaDAO.remove(ids);
			for(Long id : ids){
				datasFolgaDAO.removeTodasDatasPorFolga(id);
			}
		} catch (RegistroInexistenteException e) {
			throw new ApplicationException(e, "msg.error.registro.inexistente", "Folga");
		} catch (DeletarRegistroViolacaoFK d) {
			throw new ApplicationException(d, "msg.error.excluir.registro.relacionado", "Folga");
		}
	}

	@Override
	public Boolean validarFolga(Folga folga) throws ApplicationException {
		SimpleDateFormat formatarData = new SimpleDateFormat("dd/MM/yyyy");
		Date hoje = new Date();
		
		for (DatasFolga data : folga.getDatasFolga()){
			if (data.getData().before(hoje)) {
				throw new ApplicationException("msg.error.data.superior", formatarData.format(data.getData()).toString());
			}
		}
//		if (feriadoDAO.verificaFeriadoPorData(folga)) {
//			throw new ApplicationException("msg.error.data.superior");
//		}
		
		return true;
	}

	@Override
	public List<Folga> findByFilter(Folga folga) {
		return folgaDAO.findByFilter(folga);
	}

	@Override
	public List<Folga> findByFilterByUsuario(Folga folga) {
		return folgaDAO.findByFilterByUsuario(folga);
	}

	@Override
	public List<Folga> findFolgasByGerente(Folga folgaFiltro, Long idGerenteLogado) {
		return folgaDAO.findFolgasByGerente(folgaFiltro, idGerenteLogado);
	}

	@Override
	public void folgasParaRemocao(List<Folga> listaFolgasParaRemocao) throws ApplicationException {
		try {
			int size = listaFolgasParaRemocao.size();
			ArrayList<Long> ids = new ArrayList<Long>(size);
			for (Folga folga : listaFolgasParaRemocao) {
				if (validaRemocao(folga)) {
					ids.add(folga.getId());
				}
			}
			List<Object> pks = new ArrayList<Object>(ids);
			folgaDAO.remove(pks);
		} catch (RegistroInexistenteException e) {
			throw new ApplicationException(e, "msg.error.registro.inexistente", "Solicitação de Folga");
		} catch (DeletarRegistroViolacaoFK e) {
			throw new ApplicationException(e, "msg.error.excluir.registro.relacionado", "Solicitação de Folga");
		}

	}
	
	private Boolean validaRemocao(Folga folga) {
		if (folga.getStatus().getId() == 3) {
			return true;
		}else {
			return false;
		}
	}

	@Override
	public void avaliarFolga(List<Long> ids, Integer acao) throws ApplicationException {
		try {
			folgaDAO.avaliarFolga(ids, acao);
		} catch (RegistroInexistenteException e) {
			throw new ApplicationException(e, "msg.error.registro.inexistente", "Solicitação de Folga");
		}
	}

}
