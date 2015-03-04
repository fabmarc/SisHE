package com.indra.sishe.controller.historico;

import java.io.Serializable;
import java.util.Calendar;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;

import com.indra.infra.controller.BaseController;
import com.indra.infra.resource.MessageProvider;
import com.indra.infra.service.exception.ApplicationException;
import com.indra.sishe.controller.usuario.UsuarioLogado;
import com.indra.sishe.entity.HistoricoDebito;
import com.indra.sishe.entity.Usuario;
import com.indra.sishe.service.BancoHorasService;
import com.indra.sishe.service.HistoricoDebitoService;
import com.indra.sishe.service.UsuarioService;

@ManagedBean(name = "debitarHorasCont")
@ViewScoped
public class HistoricoDebitoController extends BaseController implements Serializable {

	private static final long serialVersionUID = -3370870932725818489L;

	@Inject
	protected transient HistoricoDebitoService historicoDebitoService;

	@Inject
	protected transient UsuarioService usuarioService;

	@Inject
	protected transient BancoHorasService bancoHorasService;

	private HistoricoDebito historicoDebito = new HistoricoDebito();

	private Usuario usuarioFiltro = new Usuario();

	private List<Usuario> listaUsuarios;

	@PostConstruct
	private void init() {
		MessageProvider.setInstance(messageProvider);
		usuarioFiltro = new Usuario();
	}

	public String irParaDebitarHoras() {
		return "/paginas/solicitacao/debitarHoras.xhtml?faces-redirect=true";
	}

	public void pesquisar() {
		usuarioFiltro.setId(UsuarioLogado.getId());
		listaUsuarios = usuarioService.findUsuarioByGerente(usuarioFiltro);
		if (usuarioFiltro.getNome() != null && "".equals(usuarioFiltro.getNome())) {
			try {
				listaUsuarios.add(0, usuarioService.findById(UsuarioLogado.getId()));
			} catch (ApplicationException e) {
				e.printStackTrace();
			}
		}
	}

	public void debitarHoras() {
		boolean debitoRealizado = false;
		try {
			for (Usuario u : listaUsuarios) {
				if (u.getDebito() != null && u.getDebito() > 0) {
					historicoDebito.setData(Calendar.getInstance().getTime());
					historicoDebito.setBanco(bancoHorasService.findByUsuario(u));
					historicoDebito.setGerente(new Usuario(UsuarioLogado.getId()));
					historicoDebito.setMinutos(u.getDebito());
					historicoDebitoService.save(historicoDebito);
					bancoHorasService.alterarHoras(u.getId(), u.getDebito() * -1);
					historicoDebito = new HistoricoDebito();
					debitoRealizado = true;
				}
				u.setDebito(null);
			}
			if (!debitoRealizado) {

			} else {
				messager.info(messageProvider.getMessage("msg.success.saldo.debitado"));
			}
		} catch (ApplicationException e) {
			e.printStackTrace();
		}
	}

	public Long findByUser(Usuario user) {
		return bancoHorasService.findByUsuario(user).getSaldo();
	}

	public String findSaldoByUser(Usuario user) {
		return bancoHorasService.findSaldoFormatadoByUsuario(user);
	}

	public List<Usuario> getListaUsuarios() {
		return listaUsuarios;
	}

	public void setListaUsuarios(List<Usuario> listaUsuarios) {
		this.listaUsuarios = listaUsuarios;
	}

	public HistoricoDebito getHistoricoDebito() {
		return historicoDebito;
	}

	public void setHistoricoDebito(HistoricoDebito historicoDebito) {
		this.historicoDebito = historicoDebito;
	}

	public Usuario getUsuarioFiltro() {
		return usuarioFiltro;
	}

	public void setUsuarioFiltro(Usuario usuarioFiltro) {
		this.usuarioFiltro = usuarioFiltro;
	}

}
