package com.indra.sishe.controller.cargo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import com.indra.infra.resource.MessageProvider;
import com.indra.infra.service.exception.ApplicationException;
import com.indra.sishe.entity.Cargo;
import com.indra.sishe.enums.PermissaoEnum;

@ViewScoped
@ManagedBean(name = "cargoCad")
public class CargoCadController extends CargoController {

	private static final long serialVersionUID = -2996122688507056006L;

	protected Cargo cargoSelecionado;

	public CargoCadController() {
	}

	@PostConstruct
	private void init() {

		MessageProvider.setInstance(messageProvider);

		searched = (Boolean) getFlashAttr("searched");

		cargoSelecionado = (Cargo) getFlashAttr("cargoSelecionado");
		if (cargoSelecionado == null) cargoSelecionado = new Cargo();

		cargoFiltro = (Cargo) getFlashAttr("cargoFiltro");
	}

	public String cadastrarCargo() {

		try {
			this.cargoSelecionado = cargoService.save(cargoSelecionado);
			putFlashAttr("cargoFiltro", cargoFiltro);
			returnInfoMessage(messageProvider.getMessage("msg.success.registro.cadastrado", "Cargo"));
			putFlashAttr("searched", searched);
			return irParaConsultar();
		} catch (ApplicationException e) {
			returnErrorMessage(e.getMessage());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public String alterarCargo() {

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

	public String cancelar() {
		putFlashAttr("searched", searched);
		putFlashAttr("cargoFiltro", cargoFiltro);
		putFlashAttr("cargoSelecionado", null);
		return irParaConsultar();
	}

	public boolean modoCadastrar() {
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

	public String msgValidacao(String key, String params) {
		return messageProvider.getMessage(key, params);
	}

	public List<PermissaoEnum> obterListaPermissoes() {
		List<PermissaoEnum> listapermissoes = new ArrayList<PermissaoEnum>(Arrays.asList(PermissaoEnum.values()));
		return listapermissoes;
	}

}
