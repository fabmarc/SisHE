package com.indra.sishe.service.impl;

import java.text.ParseException;
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
import com.indra.sishe.controller.usuario.UsuarioLogado;
import com.indra.sishe.dao.DatasFolgaDAO;
import com.indra.sishe.dao.FeriadoDAO;
import com.indra.sishe.dao.FolgaDAO;
import com.indra.sishe.entity.DatasFolga;
import com.indra.sishe.entity.Folga;
import com.indra.sishe.entity.Usuario;
import com.indra.sishe.enums.StatusEnum;
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
			//validarFolgaCadastro(entity);
			if (UsuarioLogado.verificarPermissao("ROLE_GERENTE")) {
				entity.setStatusGerente(StatusEnum.Aprovada);
				entity.setStatusLider(StatusEnum.Aprovada);
			}else if (UsuarioLogado.verificarPermissao("ROLE_LIDER")) {
				entity.setStatusLider(StatusEnum.Aprovada);
				entity.setStatusGerente(StatusEnum.Pendente);
			} else {
				entity.setStatusGerente(StatusEnum.Pendente);
				entity.setStatusLider(StatusEnum.Pendente);
			}
			Folga folga = folgaDAO.save(entity);
			
			associarDatas(folga);
			
			return folga;
		} catch (RegistroDuplicadoException e) {
			throw new ApplicationException(e, "msg.error.registro.duplicado", "Folga");
		}
	}
	
	private void associarDatas(Folga entity) {
		
		Calendar dataIndex = Calendar.getInstance();
		Calendar dataFim = Calendar.getInstance();
		
		dataIndex.setTime(entity.getDataInicio());
		dataFim.setTime(entity.getDataFim());
		List<DatasFolga> datas = new ArrayList<DatasFolga>();
		
		for(;dataIndex.getTime().before(dataFim.getTime()) || dataIndex.getTime().equals(dataFim.getTime()); dataIndex.add(Calendar.DATE, 1)){
			datas.add(new DatasFolga(dataIndex.getTime(), entity));
		}
		entity.setDatasFolga(datas);
		
		datasFolgaDAO.insereTodasDatasPorFolga(entity.getDatasFolga());
	}
	
	private void desassociarDatas(Folga entity) throws DeletarRegistroViolacaoFK{
		datasFolgaDAO.removeTodasDatasPorFolga(entity.getId());
	}

	@Override
	public Folga update(Folga entity) throws ApplicationException {
		try {
			validarFolgaAlteracao(entity);
			Folga folga = folgaDAO.update(entity);
			
			desassociarDatas(folga);
			associarDatas(folga);
			
			return folga;
		} catch (RegistroInexistenteException e) {
			throw new ApplicationException(e, "msg.error.registro.inexistente", "Folga");
		} catch (RegistroDuplicadoException d) {
			throw new ApplicationException(d, "msg.error.registro.duplicado", "Folga", "nome");
		} catch (DeletarRegistroViolacaoFK e) {
			throw new ApplicationException(e, "msg.error.excluir.registro.relacionado", "Folga");
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
		
		try {
			datasFolgaDAO.removeTodasDatasPorFolga(id);
			folgaDAO.remove(id);
		} catch (RegistroInexistenteException e) {
			throw new ApplicationException(e, "msg.error.registro.inexistente", "Folga");
		} catch (DeletarRegistroViolacaoFK e) {
			throw new ApplicationException(e, "msg.error.excluir.registro.relacionado", "Folga");
		}
		
	}
	
	public void remove(Folga entity) throws ApplicationException {
		
		try {
			//validarStatus(entity);
			datasFolgaDAO.removeTodasDatasPorFolga(entity.getId());
			folgaDAO.remove(entity.getId());
		} catch (RegistroInexistenteException e) {
			throw new ApplicationException(e, "msg.error.registro.inexistente", "Folga");
		} catch (DeletarRegistroViolacaoFK e) {
			throw new ApplicationException(e, "msg.error.excluir.registro.relacionado", "Folga");
		}
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

	private boolean validarCamposObrigatorios(Folga folga) throws ApplicationException { 
		
		if (folga.getDataInicio() == null || "".equals(folga.getDataInicio())) {
			throw new ApplicationException("msg.error.campo.obrigatorio", "De");
		} else if (folga.getDataFim() == null || "".equals(folga.getDataFim())) {
			throw new ApplicationException("msg.error.campo.obrigatorio", "Até");
		}

		return true;
	}
	
	private Boolean validarFolgaCadastro(Folga folga) throws ApplicationException {
		
		if(folga.getTitulo() == null || "".equals(folga.getTitulo())){
			folga.setTitulo("Folga");
		}
		
		validarCamposObrigatorios(folga);
		validarData(folga);
		return true;
	}
	
	private Boolean validarFolgaAlteracao(Folga folga) throws ApplicationException {
		if(folga.getTitulo() == null || "".equals(folga.getTitulo())){
			folga.setTitulo("Folga");
		}
		validarCamposObrigatorios(folga);
		validarStatus(folga);
		validarData(folga);
		return true;
	}
	
	private boolean validarStatus(Folga folga) throws ApplicationException {
		
		if((!(folga.getStatusGerente().getId() == StatusEnum.Pendente.getId())) || (!(folga.getStatusLider().getId() == StatusEnum.Pendente.getId())) ){
			throw new ApplicationException("msg.error.alterar.bloqueado.status", StatusEnum.Pendente.getNome());
		}
		return true;
	}
	
	private boolean validarData(Folga folga) throws ApplicationException {
		
		SimpleDateFormat formatarData = new SimpleDateFormat("dd/MM/yyyy");
		Date hoje = new Date();
		Date dataInicial = new Date();
		try {
			hoje = formatarData.parse(formatarData.format(hoje));
			dataInicial = formatarData.parse(formatarData.format(folga.getDataInicio()));
		} catch (ParseException e) {
			e.printStackTrace();
		}

		if(dataInicial.before(hoje)){
			throw new ApplicationException("msg.error.data.superior", formatarData.format(folga.getDataInicio()).toString());
		}
		return true;
	}

	@Override
	public List<Folga> findByFilter(Folga folga) {
		return folgaDAO.findByFilter(folga);
	}

	@Override
	public List<Folga> findFolgaByUsuario(Usuario usuario) {
		List<Folga> folgas = new ArrayList<Folga>();
		List<Folga> folgasRetorno = new ArrayList<Folga>();
		folgas = folgaDAO.findFolgaByUsuario(usuario);
		
		for(Folga folga : folgas){
			folga.setDatasFolga(datasFolgaDAO.findDatasBySolicitacaoFolga(folga.getId()));
			folgasRetorno.add(folga);
		}
		
		return folgasRetorno;
	}
	
	@Override
	public List<Folga> findFolgaByUsuario(Folga folga) {
		List<Folga> folgas = new ArrayList<Folga>();
		List<Folga> folgasRetorno = new ArrayList<Folga>();
		folgas = folgaDAO.findFolgaByUsuario(folga);
		
		for(Folga f : folgas){
			f.setDatasFolga(datasFolgaDAO.findDatasBySolicitacaoFolga(f.getId()));
			folgasRetorno.add(f);
		}
		
		return folgasRetorno;
	}

	@Override
	public List<Folga> findFolgasByGerente(Folga folgaFiltro) {
		return folgaDAO.findFolgasByGerente(folgaFiltro);
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
		return null;
//		if (folga.getStatus().getId() == 3) {
//			return true;
//		}else {
//			return false;
//		}
	}

	@Override
	public void avaliarFolga(List<Long> ids, Integer acao) throws ApplicationException {
		try {
			folgaDAO.avaliarFolga(ids, acao);
		} catch (RegistroInexistenteException e) {
			throw new ApplicationException(e, "msg.error.registro.inexistente", "Solicitação de Folga");
		}
	}

	@Override
	public List<Folga> findFolgasBylider(Folga folga) {
		return folgaDAO.findFolgasBylider(folga);
	}



}
