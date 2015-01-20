package com.indra.sishe.controller.cargo;

import java.io.Serializable;

import javax.inject.Inject;

import com.indra.infra.controller.BaseController;
import com.indra.sishe.entity.Cargo;
import com.indra.sishe.service.CargoService;

public abstract class CargoController extends BaseController implements Serializable {

	private static final long serialVersionUID = 5748750306305246554L;

	@Inject
	protected transient CargoService cargoService;

	public Cargo cargoFiltro;

	protected Boolean searched;

	public String irParaConsultar() {
		return "/paginas/cargo/consultarCargo.xhtml?faces-redirect=true";
	}

	public String irParaCadastrar() {
		putFlashAttr("cargoSelecionado", null);
		putFlashAttr("searched", this.searched);
		putFlashAttr("cargoFiltro", this.cargoFiltro);
		return "/paginas/cargo/cadastrarCargo.xhtml";
	}

	public String irParaAlterar(Cargo cargoSelecionado) {
		putFlashAttr("searched", searched);
		putFlashAttr("CargoFiltro", cargoFiltro);
		putFlashAttr("cargoSelecionado", cargoSelecionado);
		return irParaAlterar();
	}

	public String irParaAlterar() {
		return "/paginas/cargo/cadastrarCargo.xhtml";
	}

	public boolean validarCargo(Cargo cargoSelecionado) {
		if (cargoSelecionado.getNome().isEmpty()) {
			messager.error(messageProvider.getMessage("msg.error.campo.obrigatorio", "Nome"));
		} else if (cargoSelecionado.getNome().length() > 40) {
			messager.error(messageProvider.getMessage("msg.error.campo.maior.esperado", "Nome", "40"));
		} else {
			return true;
		}
		return false;
	}

	public Cargo getCargoFiltro() {
		return cargoFiltro;
	}

	public void setCargoFiltro(Cargo cargoFiltro) {
		this.cargoFiltro = cargoFiltro;
	}

	public boolean wasSearched() {
		return searched;
	}

	public void setPesquisar(boolean pesquisar) {
		this.searched = pesquisar;
	}

}
