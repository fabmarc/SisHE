package com.indra.sishe.controller.usuario;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;

import com.indra.infra.resource.MessageProvider;
import com.indra.infra.service.exception.ApplicationException;
import com.indra.sishe.entity.BancoHoras;
import com.indra.sishe.entity.Cidade;
import com.indra.sishe.entity.Sindicato;
import com.indra.sishe.entity.Usuario;
import com.indra.sishe.enums.EstadoEnum;
import com.indra.sishe.service.BancoHorasService;
import com.indra.sishe.service.CidadeService;
import com.indra.sishe.service.EstadoService;
import com.indra.sishe.service.SindicatoService;

@ViewScoped
@ManagedBean(name = "usuarioCad")
public class UsuarioCadController extends UsuarioController {

	private static final long serialVersionUID = -7129370270911504298L;

	protected Usuario usuarioSelecionado;

	protected EstadoEnum estadoSelecionado;

	@Inject
	protected transient SindicatoService sindicatoService;

	@Inject
	protected transient EstadoService estadoService;

	@Inject
	protected transient CidadeService cidadeService;

	@Inject
	protected transient BancoHorasService bancoHorasService;

	public UsuarioCadController() {
	}

	@PostConstruct
	private void init() {
		MessageProvider.setInstance(messageProvider);

		searched = (Boolean) getFlashAttr("searched");

		usuarioSelecionado = (Usuario) getFlashAttr("usuarioSelecionado");
		if (usuarioSelecionado == null) {
			usuarioSelecionado = new Usuario();
		}

		usuarioFiltro = (Usuario) getFlashAttr("usuarioFiltro");

		if (!modoCadastrar() && usuarioSelecionado.getCidade() != null) {
			estadoSelecionado = estadoService.findByCidade(usuarioSelecionado.getCidade());
		}
	}

	public String cadastrarUsuario() {
		try {
			Usuario userTemp = usuarioService.save(usuarioSelecionado);
			if (userTemp != null) {
				bancoHorasService.save(new BancoHoras(userTemp, new Long(0)));
			}
			putFlashAttr("usuarioFiltro", usuarioFiltro);
			returnInfoMessage(messageProvider.getMessage("msg.success.registro.cadastrado", "Usuario"));
			putFlashAttr("searched", searched);
			return irParaConsultar();
		} catch (ApplicationException e) {
			returnErrorMessage(e.getMessage());
		}
		return null;
	}

	public String alterarUsuario() {

		try {
			usuarioService.update(usuarioSelecionado);
			returnInfoMessage(messageProvider.getMessage("msg.success.registro.alterado", "Usuario"));
			putFlashAttr("usuarioFiltro", usuarioFiltro);
			putFlashAttr("searched", searched);
			return irParaConsultar();
		} catch (ApplicationException e) {
			returnErrorMessage(e.getMessage());
			return "";
		}
	}

	public String cancelar() {

		putFlashAttr("searched", searched);
		putFlashAttr("usuarioFiltro", usuarioFiltro);
		return irParaConsultar();
	}

	public boolean modoCadastrar() {
		if (usuarioSelecionado == null || usuarioSelecionado.getId() == null) {
			return true;
		} else {
			return false;
		}
	}

	public String confirmar() {
		if (modoCadastrar()) {
			return cadastrarUsuario();
		} else {
			return alterarUsuario();
		}
	}

	public Usuario getUsuarioSelecionado() {
		return usuarioSelecionado;
	}

	public void setUsuarioSelecionado(Usuario usuarioSelecionado) {
		this.usuarioSelecionado = usuarioSelecionado;
	}

	public List<Sindicato> obterSindicatos() {
		return sindicatoService.findAll();
	}

	public List<EstadoEnum> obterEstados() {
		return EstadoEnum.listaEstados();
	}

	public EstadoEnum getEstadoSelecionado() {
		return estadoSelecionado;
	}

	public void setEstadoSelecionado(EstadoEnum estadoSelecionado) {
		this.estadoSelecionado = estadoSelecionado;
	}

	public List<Cidade> obterCidadePorEstado() {
		if (estadoSelecionado != null) {
			return cidadeService.findByEstado(this.estadoSelecionado);
		} else {
			return null;
		}
	}

}
