package com.indra.sishe.controller.cargo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.primefaces.context.RequestContext;

import com.indra.infra.resource.MessageProvider;
import com.indra.infra.service.exception.ApplicationException;
import com.indra.sishe.entity.Cargo;

@ViewScoped
@ManagedBean(name = "cargoMnt")
public class CargoMntController extends CargoController {

	private static final long serialVersionUID = 8119803534282941375L;

	private List<Cargo> listaCargos;

	private List<Cargo> cargosSelecionados;

	public CargoMntController() {
	}

	@PostConstruct
	public void init() {
		MessageProvider.setInstance(messageProvider);

		searched = (Boolean) getFlashAttr("searched");
		if (searched == null) searched = false;

		cargoFiltro = (Cargo) getFlashAttr("cargoFiltro");
		if (cargoFiltro == null) cargoFiltro = new Cargo();

		if (!searched) listaCargos = new ArrayList<Cargo>();
		else pesquisar();
	}

	public void beforeRemoveCargos() {
		if (cargosSelecionados.size() == 0) {
			RequestContext.getCurrentInstance().execute("selectAtleastOne.show()");
		} else {
			RequestContext.getCurrentInstance().execute("confirmExclusao.show()");
		}
	}

	public String removerCargo() {
		int size = cargosSelecionados.size();
		ArrayList<Long> ids = new ArrayList<Long>(size);
		for (Cargo cargo : cargosSelecionados)
			ids.add(cargo.getId());
		try {
			cargoService.remove(ids);
			messager.info(messageProvider.getMessage("msg.success.registro.excluido", "Cargo"));
		} catch (ApplicationException e) {
			messager.error(e.getMessage());
		}
		pesquisar();
		return irParaConsultar();
	}

	public void pesquisar() {
		listaCargos = cargoService.findByFilter(cargoFiltro);
		Collections.sort(listaCargos);
		searched = true;
	}

	public String irParaAlterar(Cargo cargoSelecionado) {
		putFlashAttr("searched", this.searched);
		putFlashAttr("cargoFiltro", this.cargoFiltro);
		try {
			cargoSelecionado = cargoService.findById(cargoSelecionado.getId());
			putFlashAttr("cargoSelecionado", cargoSelecionado);
			return irParaAlterar();
		} catch (ApplicationException e) {
			returnErrorMessage(e.getMessage());
			return irParaConsultar();
		}
	}

	public List<Cargo> getListaCargos() {
		return listaCargos;
	}

	public void setListaCargos(List<Cargo> listaCargos) {
		this.listaCargos = listaCargos;
	}

	public List<Cargo> getCargosSelecionados() {
		return cargosSelecionados;
	}

	public void setCargosSelecionados(List<Cargo> cargosSelecionados) {
		this.cargosSelecionados = cargosSelecionados;
	}

}
