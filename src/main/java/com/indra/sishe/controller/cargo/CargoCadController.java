package com.indra.sishe.controller.cargo;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import com.indra.infra.resource.MessageProvider;
import com.indra.infra.service.exception.ApplicationException;
import com.indra.sishe.entity.Cargo;

@ViewScoped
@ManagedBean(name = "cargoCad")
public class CargoCadController extends CargoController {

	private static final long serialVersionUID = -2996122688507056006L;

	protected Cargo cargoSelecionado;

	public CargoCadController() {
		System.out.println("Controler CargoCad criado.");
	}

	@PostConstruct
	private void init() {

		MessageProvider.setInstance(messageProvider);

		searched = (Boolean) getFlashAttr("searched");

		cargoSelecionado = (Cargo) getFlashAttr("cargoSelecionado");
		if (cargoSelecionado == null)
			cargoSelecionado = new Cargo();

		cargoFiltro = (Cargo) getFlashAttr("cargoFiltro");
	}

	public String cadastrarCargo() {
		if (validarCargo(cargoSelecionado)) {
			try {
				this.cargoSelecionado = cargoService.save(cargoSelecionado);
				putFlashAttr("cargoFiltro", cargoFiltro);
				returnInfoMessage(messageProvider.getMessage("msg.success.registro.cadastrado", "Cargo"));
				putFlashAttr("searched", searched);
				return irParaConsultar();
			} catch (ApplicationException e) {
				returnErrorMessage(e.getMessage());
			}
		}
		return null;
	}

	public String alterarCargo() {
		if (validarCargo(cargoSelecionado)) {
			try {
				cargoService.update(cargoSelecionado);
				returnInfoMessage(messageProvider.getMessage("msg.success.registro.alterado", "Cargo"));
				putFlashAttr("cargoFiltro", cargoFiltro);
				putFlashAttr("searched", searched);
				return irParaConsultar();
			} catch (ApplicationException e) {
				returnErrorMessage(e.getMessage());
				return irParaAlterar(cargoSelecionado);
			}
		}
		return null;
	}

	public String cancelar() {
		putFlashAttr("searched", searched);
		putFlashAttr("cargoFiltro", cargoFiltro);
		putFlashAttr("cargoSelecionado", null);
		return irParaConsultar();
	}

	private boolean modoCadastrar() {
		if (cargoSelecionado == null || cargoSelecionado.getId() == null) {
			return true;
		} else {
			return false;
		}
	}

	public String confirmar() {
		if (modoCadastrar()) {
			return cadastrarCargo();
		} else {
			return alterarCargo();
		}
	}

	public Cargo getCargoSelecionado() {
		return cargoSelecionado;
	}

	public void setCargoSelecionado(Cargo cargoSelecionado) {
		this.cargoSelecionado = cargoSelecionado;
	}

}
