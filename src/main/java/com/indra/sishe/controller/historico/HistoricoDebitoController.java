package com.indra.sishe.controller.historico;

import java.io.Serializable;
import java.util.Calendar;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;

import com.indra.infra.controller.BaseController;
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
	protected transient BancoHorasService  bancoHorasService;
	
	private HistoricoDebito historicoDebito = new HistoricoDebito();

	public List<Usuario> listaUsuarios;
	
	public String irParaDebitarHoras() {
		return "/paginas/solicitacao/debitarHoras.xhtml?faces-redirect=true";
	}
	
	public void pesquisar() {
		listaUsuarios = usuarioService.findAll();
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
	
	public void debitarHoras() {
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
				}
				u.setDebito(null);
			}
		} catch (ApplicationException e) {
			e.printStackTrace();
		}
	}
	
	public Long findByUser(Usuario user){
		return bancoHorasService.findByUsuario(user).getSaldo();
	}
	
	
}
