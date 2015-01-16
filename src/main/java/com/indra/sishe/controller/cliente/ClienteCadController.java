package com.indra.sishe.controller.cliente;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import com.indra.infra.resource.MessageProvider;
import com.indra.infra.service.exception.ApplicationException;
import com.indra.sishe.entity.Cliente;

@ViewScoped
@ManagedBean(name = "clienteCad")
public class ClienteCadController extends ClienteController {

	private static final long serialVersionUID = -7787160113773729958L;

	public ClienteCadController() {
	}

	@PostConstruct
	private void init() {

		MessageProvider.setInstance(messageProvider);

		searched = (Boolean) getFlashAttr("searched");
		
		clienteSelecionado = (Cliente) getFlashAttr("clienteSelecionado");
		if (clienteSelecionado == null) clienteSelecionado = new Cliente();
		
		clienteFiltro = (Cliente) getFlashAttr("clienteFiltro");
	}

	public String cadastrarCliente() {
		
		if (validarCliente(clienteSelecionado)) {
			clienteService.save(clienteSelecionado);
			putFlashAttr("clienteFiltro", clienteFiltro);
			returnInfoMessage(messageProvider.getMessage("msg.success.registro.cadastrado", "Cliente"));			
			putFlashAttr("searched", searched);			
			return irParaConsultar();
		}
		return null;
	}
	
	public String alterarCliente() {
		
		if (validarCliente(clienteSelecionado)) {
			try {
				clienteService.update(clienteSelecionado);
				returnInfoMessage(messageProvider.getMessage("msg.success.registro.alterado", "Cliente"));
				putFlashAttr("clienteFiltro", clienteFiltro);			
				putFlashAttr("searched", searched);			
				return irParaConsultar();
			} catch (ApplicationException e) {
				returnErrorMessage(e.getMessage());
				return irParaAlterar(clienteSelecionado);
			}
		}
		return null;
	}

	public String cancelar() {
		putFlashAttr("searched", searched);			
		putFlashAttr("clienteFiltro", clienteFiltro);			
		return "/paginas/cliente/consultarCliente.xhtml?faces-redirect=true";
	}

	public Cliente getClienteFiltro() {
		return clienteFiltro;
	}

	public void setClienteFiltro(Cliente clienteFiltro) {
		this.clienteFiltro = clienteFiltro;
	}

	public Cliente getClienteSelecionado() {
		return clienteSelecionado;
	}

	public void setClienteSelecionado(Cliente clienteSelecionado) {
		this.clienteSelecionado = clienteSelecionado;
	}

	public boolean wasSearched() {
		return searched;
	}

	public void setPesquisar(boolean pesquisar) {
		this.searched = pesquisar;
	}
	
}
