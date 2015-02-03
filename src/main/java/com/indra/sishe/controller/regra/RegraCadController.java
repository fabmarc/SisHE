package com.indra.sishe.controller.regra;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import com.indra.infra.resource.MessageProvider;
import com.indra.sishe.entity.Regra;
import com.indra.sishe.entity.Sindicato;

@ViewScoped
@ManagedBean(name = "regraCad")
public class RegraCadController extends RegraController {

	private static final long serialVersionUID = -5964546538405452858L;

	public Regra regraSelecionada;

	public List<Sindicato> listaSindicatos;

	public RegraCadController() {
	}

	@PostConstruct
	private void init() {

		MessageProvider.setInstance(messageProvider);
		setListaSindicatos(obterSindicatos());

		searched = (Boolean) getFlashAttr("searched");
		regraSelecionada = (Regra) getFlashAttr("regraSelecionada");
		if (regraSelecionada == null) {
			regraSelecionada = new Regra();
		}
		sindicatoSelecionado = (Sindicato) getSessionAttr("sindicadoSelecionadoFiltro");
		regraFiltro = (Regra) getFlashAttr("regraFiltro");
	}

	public String cadastrarRegra() {

		try {
			regraSelecionada.setSindicato(sindicatoSelecionado);
			regraService.save(regraSelecionada);
			putFlashAttr("regraFiltro", regraFiltro);
			returnInfoMessage(messageProvider.getMessage("msg.success.registro.cadastrado", "Regra"));
			putFlashAttr("searched", searched);
			return irParaConsultar();
		} catch (Exception e) {
			returnErrorMessage(e.getMessage());
		}
		return null;
	}

	public boolean modoCadastrar() {
		if (regraSelecionada == null || regraSelecionada.getId() == null) {
			return true;
		} else {
			return false;
		}
	}

	public String confirmar() {
		if (modoCadastrar()) {
			return cadastrarRegra();
		} else {
			return alterarRegra();
		}
	}

	public String alterarRegra() {

		try {
			regraService.update(regraSelecionada);
			returnInfoMessage(messageProvider.getMessage("msg.success.registro.alterado", "Regra"));
			putFlashAttr("regraFiltro", regraFiltro);
			putFlashAttr("searched", searched);
			return irParaConsultar();
		} catch (Exception e) {
			returnErrorMessage(e.getMessage());
			return irParaAlterar(regraSelecionada);
		}
	}

	public List<Sindicato> obterSindicatos() {
		Sindicato sindicato = new Sindicato();
		return sindicatoService.findByFilter(sindicato);
	}

	public String cancelar() {
		putFlashAttr("searched", searched);
		putFlashAttr("regraFiltro", regraFiltro);
		return irParaConsultar();
	}

	public Regra getRegraSelecionada() {
		return regraSelecionada;
	}

	public void setRegraSelecionada(Regra regraSelecionada) {
		this.regraSelecionada = regraSelecionada;
	}

	public List<Sindicato> getListaSindicatos() {
		return listaSindicatos;
	}

	public void setListaSindicatos(List<Sindicato> listaSindicatos) {
		this.listaSindicatos = listaSindicatos;
	}

}
