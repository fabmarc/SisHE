package com.indra.sishe.controller.feriado;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import com.indra.infra.resource.MessageProvider;
import com.indra.infra.service.exception.ApplicationException;
import com.indra.sishe.entity.Cidade;
import com.indra.sishe.entity.Estado;
import com.indra.sishe.entity.Feriado;

@ViewScoped
@ManagedBean(name = "feriadoCad")
public class FeriadoCadController extends FeriadoController{

	private static final long serialVersionUID = 3068532582581639566L;

	private List<Estado> listaEstado;
	
	public Feriado feriadoSelecionado;
	
	
	public FeriadoCadController() {
	}
	
	@PostConstruct
	private void init() {
		MessageProvider.setInstance(messageProvider);
		
		searched = (Boolean) getFlashAttr("searched");
		feriadoSelecionado = (Feriado) getFlashAttr("feriadoSelecionado");
		if (feriadoSelecionado == null) {
			feriadoSelecionado = new Feriado(new Estado(), new Cidade());
		}
		setListaEstado(obterEstados());
		feriadoFiltro = (Feriado) getFlashAttr("feriadoFiltro");
	}
	
	public String cadastrarFeriado() {
		if (validarFeriado(feriadoSelecionado)){
		feriadoService.save(feriadoSelecionado);
		putFlashAttr("feriadoFiltro", feriadoFiltro);
		returnInfoMessage(messageProvider.getMessage("msg.success.registro.cadastrado", "Feriado"));
		putFlashAttr("searched", searched);
		return irParaConsultar();
		}
		return null;
	}
	
	public String confirmar() {
		if (feriadoSelecionado.getId() == null || "".equals(feriadoSelecionado.getId().toString()) ) {
			cadastrarFeriado();
		}else{
			alterarCliente();
		} 
		return irParaConsultar();
	}
	
	public String alterarCliente(){
		if (validarFeriado(feriadoSelecionado)){
			try {
				feriadoService.update(feriadoSelecionado);
				returnInfoMessage(messageProvider.getMessage("msg.success.registro.alterado", "Feriado"));
				putFlashAttr("feriadoFiltro", feriadoFiltro);
				putFlashAttr("searched", searched);
				return irParaConsultar();
			} catch (ApplicationException e) {
				returnErrorMessage(e.getMessage());
				//return irParaAlterar(feriadoSelecionado.getId());
			}
		}
		return null;		
	}
	
	public String cancelar() {
		putFlashAttr("searched", searched);			
		putFlashAttr("clienteFiltro", feriadoFiltro);			
		return "/paginas/feriado/consultarFeriado.xhtml?faces-redirect=true";
	}
	
	public List<Estado> obterEstados(){
		return estadoService.findAll();
	}

	public List<Cidade> obterCidades(){
		return cidadeService.findByEstado(feriadoSelecionado.getEstado());
	}
	
	public boolean modoCadastrar() {
		if (feriadoSelecionado.equals(new Feriado())) {
			return true;
		} else {
			return false;
		}
	}

	public Feriado getFeriadoSelecionado() {
		return feriadoSelecionado;
	}

	public void setFeriadoSelecionado(Feriado feriadoSelecionado) {
		this.feriadoSelecionado = feriadoSelecionado;
	}
	
	public boolean wasSearched() {
		return searched;
	}

	public void setPesquisar(boolean pesquisar) {
		this.searched = pesquisar;
	}
	
	public List<Estado> getListaEstado() {
		return listaEstado;
	}

	public void setListaEstado(List<Estado> listaEstado) {
		this.listaEstado = listaEstado;
	}
	
}
