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
import com.indra.sishe.entity.Projeto;
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
			if (validarUsuario(entity)) {
				return usuarioDao.save(entity);
			} else {
				return null;
			}
		} catch (RegistroDuplicadoException e) {
			if (e.getMessageCode().contains(
					"ERRO: duplicar valor da chave viola a restrição de unicidade \"uq_matricula\"")) { throw new ApplicationException(
					e, "msg.error.campo.existente", "usuário", "matrícula"); }
			if (e.getMessageCode().contains(
					"ERRO: duplicar valor da chave viola a restrição de unicidade \"uq_login\"")) { throw new ApplicationException(
					e, "msg.error.campo.existente", "usuário", "login"); }
			if (e.getMessageCode().contains(
					"ERRO: duplicar valor da chave viola a restrição de unicidade \"uq_email\"")) { throw new ApplicationException(
					e, "msg.error.campo.existente", "usuário", "email"); }
			throw new ApplicationException(e, "msg.error.registro.duplicado", "Usuário");
		}
	}

	@Override
	public Usuario update(Usuario entity) throws ApplicationException {

		try {
			if (validarUsuario(entity)) {
				return usuarioDao.update(entity);
			} else {
				return null;
			}
		} catch (RegistroInexistenteException e) {
			throw new ApplicationException(e, "msg.error.registro.inexistente", "Usuário");
		} catch (RegistroDuplicadoException d) {
			if (d.getMessageCode().contains(
					"ERRO: duplicar valor da chave viola a restrição de unicidade \"uq_matricula\"")) { throw new ApplicationException(
					d, "msg.error.campo.existente", "usuário", "matrícula"); }
			if (d.getMessageCode().contains(
					"ERRO: duplicar valor da chave viola a restrição de unicidade \"uq_login\"")) { throw new ApplicationException(
					d, "msg.error.campo.existente", "usuário", "login"); }
			if (d.getMessageCode().contains(
					"ERRO: duplicar valor da chave viola a restrição de unicidade \"uq_email\"")) { throw new ApplicationException(
					d, "msg.error.campo.existente", "usuário", "email"); }
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
			throw new ApplicationException(e, "msg.error.registro.inexistente", "Usuário");
		} catch (DeletarRegistroViolacaoFK d) {
			throw new ApplicationException(d, "msg.error.excluir.registro.relacionado", "Usuário");
		}
	}

	@Override
	public List<Usuario> findByCargo(String role) {
		return usuarioDao.findByCargo(role);
	}

	public boolean validarUsuario(Usuario entity) throws ApplicationException {

		if (entity.getNome().isEmpty()) {
			throw new ApplicationException("msg.error.campo.obrigatorio", "Nome");
		} else if (entity.getNome().length() > 60) {
			throw new ApplicationException("msg.error.campo.maior.esperado", "Nome", "40");
		} else if (entity.getNome().replace(" ", "").length() < 5) {
			throw new ApplicationException("msg.error.campo.menor.esperado", "Nome", "5");
		} else if (entity.getMatricula() != null && entity.getMatricula() == 0) {
			throw new ApplicationException("msg.error.campo.obrigatorio", "Matrícula");
		} else if (entity.getCargo() == null || entity.getCargo().getId() == null) {
			throw new ApplicationException("msg.error.campo.obrigatorio", "Cargo");
		} else if (entity.getSindicato() == null || entity.getSindicato().getId() == null) {
			throw new ApplicationException("msg.error.campo.obrigatorio", "Sindicato");
		} else if (entity.getCidade() == null || entity.getCidade().getId() == null) {
			throw new ApplicationException("msg.error.campo.obrigatorio", "Cidade");
		} else if (entity.getLogin() == null || entity.getLogin().isEmpty()) {
			throw new ApplicationException("msg.error.campo.obrigatorio", "Login");
		} else if (entity.getLogin().length() > 20) {
			throw new ApplicationException("msg.error.campo.maior.esperado", "Login", "20");
		} else if (entity.getLogin().length() < 5) {
			throw new ApplicationException("msg.error.campo.menor.esperado", "Login", "5");
		} else if (entity.getEmail() != null && entity.getEmail().length() > 30) {
			throw new ApplicationException("msg.error.campo.maior.esperado", "Email", "30");
		} else {
			return validarSenha(entity);
		}

	}

	private boolean validarSenha(Usuario entity) throws ApplicationException {
		if (entity.getSenha() == null || entity.getSenha().isEmpty()) {
			throw new ApplicationException("msg.error.campo.obrigatorio", "Senha");
		} else if (entity.getSenha().length() > 20) {
			throw new ApplicationException("msg.error.campo.maior.esperado", "Senha", "20");
		} else if (entity.getSenha().length() < 5) {
			throw new ApplicationException("msg.error.campo.menor.esperado", "Senha", "5");
		} else if (entity.getSenhaConfirm() == null || entity.getSenhaConfirm().isEmpty()) {
			throw new ApplicationException("msg.error.campo.obrigatorio", "Senha");
		} else if (!entity.getSenha().equals(entity.getSenhaConfirm())) {
			throw new ApplicationException("msg.error.senha.divergente");
		} else {
			return true;
		}
	}

	@Override
	public void alterarSenha(Usuario usuario) throws ApplicationException {
		try {
			if (validarSenha(usuario)) {
				Usuario usuarioBanco = usuarioDao.findById(usuario.getId());
				if (usuario.getSenhaAtual().equals(usuarioBanco.getSenha())) {
					usuarioDao.updatePassword(usuario);
				} else {
					throw new ApplicationException("msg.error.senha.incorreta");
				}
			} else {
				throw new ApplicationException("msg.error.senha.divergente");
			}
		} catch (RegistroInexistenteException e) {
			throw new ApplicationException("msg.error.registro.inexistente");
		} catch (ApplicationException e) {
			throw new ApplicationException(e.getMessageCode(), e.getMessageParams());
		}
	}

	@Override
	public Usuario findByLogin(String login) {
		return usuarioDao.findByLogin(login);
	}

	@Override
	public List<Usuario> findGerentesDisponiveis() {
		return usuarioDao.findGerentesDisponiveis();
	}

	@Override

	public List<Usuario> findByProjetos(List<Long> ids) {
		return usuarioDao.findByProjetos(ids);
	}

	public List<Usuario> findLideresDisponiveis(Projeto projeto) {
		return usuarioDao.findLideresDisponiveis(projeto);
	}

	@Override
	public List<Usuario> findUsuarioByGerente(Usuario usuarioFiltro) {
		return usuarioDao.findUsuarioByGerente(usuarioFiltro);
	}

}
