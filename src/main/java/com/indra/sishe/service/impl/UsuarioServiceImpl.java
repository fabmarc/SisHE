package com.indra.sishe.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;

import org.springframework.beans.factory.annotation.Autowired;

import com.indra.infra.dao.exception.DeletarRegistroViolacaoFK;
import com.indra.infra.dao.exception.RegistroDuplicadoException;
import com.indra.infra.dao.exception.RegistroInexistenteException;
import com.indra.infra.service.exception.ApplicationException;
import com.indra.sishe.dao.UsuarioDAO;
import com.indra.sishe.entity.Cargo;
import com.indra.sishe.entity.Usuario;
import com.indra.sishe.service.StatelessServiceAb;
import com.indra.sishe.service.UsuarioService;

@Stateless
public class UsuarioServiceImpl extends StatelessServiceAb implements UsuarioService {

	private static final long serialVersionUID = 3763809097797645890L;

	@Autowired
	private UsuarioDAO usuarioDao;

	@Override
	public Usuario save(Usuario entity) throws ApplicationException {
		try {
			return usuarioDao.save(entity);
		} catch (RegistroDuplicadoException e) {
			if (e.getMessageCode().contains("ERRO: duplicar valor da chave viola a restrição de unicidade \"uq_matricula\"")) {
				throw new ApplicationException(e, "msg.error.campo.existente", "usuário", "matrícula");
			}
			if (e.getMessageCode().contains("ERRO: duplicar valor da chave viola a restrição de unicidade \"uq_login\"")) {
				throw new ApplicationException(e, "msg.error.campo.existente", "usuário", "login");
			}
			throw new ApplicationException(e, "msg.error.registro.duplicado", "Usuário");
		}
	}

	@Override
	public Usuario update(Usuario entity) throws ApplicationException {
		try {
			return usuarioDao.update(entity);
		} catch (RegistroInexistenteException e) {
			throw new ApplicationException(e, "msg.error.registro.inexistente", "Usuário");
		} catch (RegistroDuplicadoException d) {
			if (d.toString().contains("ERRO: duplicar valor da chave viola a restrição de unicidade \"uq_matricula\"")) {
				throw new ApplicationException(d, "msg.error.campo.existente", "usuário", "matrícula");
			}
			if (d.toString().contains("ERRO: duplicar valor da chave viola a restrição de unicidade \"uq_login\"")) {
				throw new ApplicationException(d, "msg.error.campo.existente", "usuário", "login");
			}
			throw new ApplicationException(d, "Erro");
		}
	}

	@Override
	public List<Usuario> findAll() {
		return usuarioDao.findAll();
	}

	@Override
	public Usuario findById(Long id) throws ApplicationException {
		try {
			return usuarioDao.findById(id);
		} catch (RegistroInexistenteException e) {
			throw new ApplicationException(e, "msg.error.registro.inexistente", "Usuário");
		}
	}

	@Override
	public List<Usuario> findByFilter(Usuario usuarioFiltro) {
		return usuarioDao.findByFilter(usuarioFiltro);
	}

	@Override
	public void remove(Long id) throws ApplicationException {
		try {
			usuarioDao.remove(id);
		} catch (RegistroInexistenteException e) {
			throw new ApplicationException(e, "msg.error.registro.inexistente", "Usuário");
		} catch (DeletarRegistroViolacaoFK d) {
			throw new ApplicationException(d, "msg.error.excluir.registro.relacionado", "Usuário");
		}
	}

	@Override
	public void remove(List<Long> ids) throws ApplicationException {
		try {
			List<Object> pks = new ArrayList<Object>(ids);
			usuarioDao.remove(pks);
		} catch (RegistroInexistenteException e) {
			throw new ApplicationException(e, "msg.error.registro.inexistente", "Cargo");
		} catch (DeletarRegistroViolacaoFK d) {
			throw new ApplicationException(d, "msg.error.excluir.registro.relacionado", "Cargo");
		}
	}

	@Override
	public List<Usuario> findByCargo(Cargo cargo) {
		return usuarioDao.findByCargo(cargo);
	}

}
