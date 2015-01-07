package com.indra.sishe.controller.cargo;

import java.io.Serializable;

import javax.inject.Inject;

import com.indra.infra.controller.BaseController;
import com.indra.sishe.entity.Cargo;
import com.indra.sishe.service.CargoService;

public class CargoController extends BaseController implements Serializable{

	private static final long serialVersionUID = 5748750306305246554L;
	
	@Inject
	protected transient CargoService cargoService;
	
	public Cargo cargoFiltro;
	
	public Cargo cargoSelecionado;
	
	protected Boolean searched;
	
	
	public String irParaConsultar() {
		return "/paginas/cargo/consultarCargo.xhtml";
	}

	public String irParaCadastrar() {
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
	
	public String irParaAlterar(){
		return "/paginas/cargo/cadastrarCargo.xhtml";
	}
	
	public boolean validarCargo(Cargo cargoSelecionado) {

		if (cargoSelecionado.getNome().isEmpty()) {
			// nome não foi preenchido.
			messager.error(messageProvider.getMessage("msg.error.campo.obrigatorio", "Nome"));

		} else if (cargoSelecionado.getNome().length() > 40) {
			// o nome ultrapassa 40 caracteres.
			messager.error(messageProvider.getMessage("msg.error.campo.maior.esperado", "Nome", "40"));
		} else {
			Cargo temp = cargoService.pesquisarNome(cargoSelecionado.getNome());
				if(temp == null || temp.getId().equals(cargoSelecionado.getId())){
					return true;
				}else{
					messager.error(messageProvider.getMessage("msg.error.registro.existente"));					
				}
		}
		return false;
	}
	
	public Cargo getCargoFiltro() {
		return cargoFiltro;
	}

	public void setCargoFiltro(Cargo cargoFiltro) {
		this.cargoFiltro = cargoFiltro;
	}

	public Cargo getCargoSelecionado() {
		return cargoSelecionado;
	}

	public void setCargoSelecionado(Cargo cargoSelecionado) {
		this.cargoSelecionado = cargoSelecionado;
	}

	public boolean wasSearched() {
		return searched;
	}

	public void setPesquisar(boolean pesquisar) {
		this.searched = pesquisar;
	}
	
}
