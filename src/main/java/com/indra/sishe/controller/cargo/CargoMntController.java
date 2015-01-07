package com.indra.sishe.controller.cargo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import com.indra.infra.resource.MessageProvider;
import com.indra.sishe.entity.Cargo;
import com.indra.infra.service.exception.ApplicationException;

@ViewScoped
@ManagedBean(name = "cargoMnt")
public class CargoMntController extends CargoController{

	private static final long serialVersionUID = 8119803534282941375L;
	
	private List<Cargo> listaCargos;
	
	public CargoMntController(){
	}
	
	@PostConstruct
	public void init(){
		MessageProvider.setInstance(messageProvider);

		searched = (Boolean) getFlashAttr("searched");
		if (searched == null) searched = false;
		
		cargoFiltro = (Cargo) getFlashAttr("cargoFiltro");
		if (cargoFiltro == null) cargoFiltro = new Cargo();
		
		if (!searched) listaCargos = new ArrayList<Cargo>();
		else pesquisar();	
	}
	
	public void pesquisar() {
		listaCargos = cargoService.findByFilter(cargoFiltro);
		Collections.sort(listaCargos);
		searched = true;
	}
	
	public String removerCargo() {
		String nome = cargoSelecionado.getNome();
		try {
			cargoService.remove((Long) cargoSelecionado.getId());
			messager.info(messageProvider.getMessage("msg.success.registro.excluido", "Cargo", nome));
		} catch (ApplicationException e) {
			messager.error(e.getMessage());
		}//verificar possibilidade de adicionar um catch para erro de SQL, caso o registro esteja em uso.
		pesquisar();
		return irParaConsultar();
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
	
}
