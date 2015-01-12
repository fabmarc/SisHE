package com.indra.sishe.controller.cliente;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.primefaces.context.RequestContext;

import com.indra.infra.resource.MessageProvider;
import com.indra.infra.service.exception.ApplicationException;
import com.indra.sishe.entity.Cliente;

@ViewScoped
@ManagedBean(name = "clienteMnt")
public class ClienteMntController extends ClienteController {

	private static final long serialVersionUID = -6166298037225326023L;

	private List<Cliente> listaClientes;
	
	private List<Cliente> clientesSelecionados;
	
	public ClienteMntController() {
	}

	@PostConstruct
	private void init() {
		
		MessageProvider.setInstance(messageProvider);

		searched = (Boolean) getFlashAttr("searched");
		if (searched == null) searched = false;
		
		clienteFiltro = (Cliente) getFlashAttr("clienteFiltro");
		if (clienteFiltro == null) clienteFiltro = new Cliente();
		
		if (!searched) listaClientes = new ArrayList<Cliente>();
		else pesquisar();
	}
	
	public void beforeRemoveCliente() {
		
		if (clientesSelecionados.size() == 0) {
			RequestContext.getCurrentInstance().execute("selectAtleastOne.show()");
		} else {
			RequestContext.getCurrentInstance().execute("confirmExclusao.show()");
		}
	}

	public String removerCliente() {

		int size = clientesSelecionados.size();
		ArrayList<Long> ids = new ArrayList<Long>(size);
		for (Cliente cliente : clientesSelecionados) ids.add(cliente.getIdCliente());
		try {
			clienteService.remove(ids);
			messager.info(messageProvider.getMessage("msg.success.registro.excluido", "Cliente"));
		} catch (ApplicationException e) {
			messager.error(e.getMessage());
		}
		pesquisar();
		return irParaConsultar();
	}

	public void pesquisar() {
		listaClientes = clienteService.findByFilter(clienteFiltro);
		Collections.sort(listaClientes);
		searched = true;
	}

	public String irParaAlterar(Cliente clienteSelecionado) {
		putFlashAttr("searched", searched);
		putFlashAttr("clienteFiltro", clienteFiltro);
		try {
			clienteSelecionado = clienteService.findById(clienteSelecionado.getIdCliente());
			putFlashAttr("clienteSelecionado", clienteSelecionado);
			return "/paginas/cliente/alterarCliente.xhtml?faces-redirect=true";
		} catch (ApplicationException e) {
			returnErrorMessage(e.getMessage());
			return irParaConsultar();
		}
	}

	public List<Cliente> getListaClientes() {
		return listaClientes;
	}

	public void setListaClientes(List<Cliente> listaClientes) {
		this.listaClientes = listaClientes;
	}
	
	public Cliente getClienteFiltro() {
		return clienteFiltro;
	}

	public void setClienteFiltro(Cliente clienteFiltro) {
		this.clienteFiltro = clienteFiltro;
	}

	public List<Cliente> getClientesSelecionados() {
		return clientesSelecionados;
	}

	public void setClientesSelecionados(List<Cliente> clientesSelecionados) {
		this.clientesSelecionados = clientesSelecionados;
	}

}
