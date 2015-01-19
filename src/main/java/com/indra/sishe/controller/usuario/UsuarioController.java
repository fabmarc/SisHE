package com.indra.sishe.controller.usuario;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;

import com.indra.infra.controller.BaseController;
import com.indra.sishe.entity.Cargo;
import com.indra.sishe.entity.Usuario;
import com.indra.sishe.service.CargoService;
import com.indra.sishe.service.UsuarioService;

public abstract class UsuarioController extends BaseController implements Serializable {

	private static final long serialVersionUID = -6070319180097929260L;

	@Inject
	protected transient UsuarioService usuarioService;

	@Inject
	protected transient CargoService cargoService;

	protected String senhaConfirm;

	protected Usuario usuarioFiltro;

	protected Boolean searched;

	public String irParaConsultar() {
		return "/paginas/usuario/consultarUsuario.xhtml?faces-redirect=true";
	}

	public String irParaCadastrar() {
		putFlashAttr("usuarioSelecionado", null);
		putFlashAttr("searched", this.searched);
		putFlashAttr("usuarioFiltro", this.usuarioFiltro);
		return "/paginas/usuario/cadastrarUsuario.xhtml?faces-redirect=true";
	}

	public String irParaAlterar() {
		return "/paginas/usuario/cadastrarUsuario.xhtml?faces-redirect=true";
	}

	public String irParaAlterar(Usuario usuarioSelecionado) {
		putFlashAttr("searched", this.searched);
		putFlashAttr("usuarioFiltro", this.usuarioFiltro);
		putFlashAttr("usuarioSelecionado", usuarioSelecionado);
		return irParaAlterar();
	}

	public boolean validarUsuario(Usuario usuarioSelecionado) {
		if (usuarioSelecionado.getNome().isEmpty()) {
			messager.error(messageProvider.getMessage("msg.error.campo.obrigatorio", "Nome"));
		} else {
			if (usuarioSelecionado.getNome().length() > 40) {
				messager.error(messageProvider.getMessage("msg.error.campo.maior.esperado", "Nome", "40"));
			} else {
				if (usuarioSelecionado.getMatricula() == null) {
					messager.error(messageProvider.getMessage("msg.error.campo.obrigatorio", "Matrícula"));
				} else {
					if (usuarioSelecionado.getCargo() == null || usuarioSelecionado.getCargo().getId() == null) {
						messager.error(messageProvider.getMessage("msg.error.campo.obrigatorio", "Cargo"));
					} else {
						if (usuarioSelecionado.getSindicato() == null || usuarioSelecionado.getSindicato().getId() == null) {
							messager.error(messageProvider.getMessage("msg.error.campo.obrigatorio", "Sindicato"));
						} else {
							if (usuarioSelecionado.getCidade() == null || usuarioSelecionado.getCidade().getId() == null) {
								messager.error(messageProvider.getMessage("msg.error.campo.obrigatorio", "Cidade"));
							} else {
								if (usuarioSelecionado.getLogin() == null || "".equals(usuarioSelecionado.getLogin())) {
									messager.error(messageProvider.getMessage("msg.error.campo.obrigatorio", "Login"));
								} else {
									if (usuarioSelecionado.getEmail() == null || "".equals(usuarioSelecionado.getEmail())) {
										messager.error(messageProvider.getMessage("msg.error.campo.obrigatorio", "Email"));
									} else {
										if ((usuarioSelecionado.getSenha() == null || "".equals(usuarioSelecionado.getSenha())) && (senhaConfirm == null || "".equals(senhaConfirm))) {
											messager.error(messageProvider.getMessage("msg.error.campo.obrigatorio", "Senha"));
										} else {
											if (!usuarioSelecionado.getSenha().equals(senhaConfirm)) {
												messager.error("Senha divergente.");
											} else {
												return true;
											}
										}
									}
								}
							}
						}
					}
				}
			}
		}
		return false;
	}

	public Usuario getUsuarioFiltro() {
		return usuarioFiltro;
	}

	public void setUsuarioFiltro(Usuario usuarioFiltro) {
		this.usuarioFiltro = usuarioFiltro;
	}

	public Boolean wasSearched() {
		return searched;
	}

	public void setSearched(Boolean searched) {
		this.searched = searched;
	}

	public List<Cargo> obterCargos() {
		return cargoService.findAll();
	}

	public String getSenhaConfirm() {
		return senhaConfirm;
	}

	public void setSenhaConfirm(String senhaConfirm) {
		this.senhaConfirm = senhaConfirm;
	}

}
