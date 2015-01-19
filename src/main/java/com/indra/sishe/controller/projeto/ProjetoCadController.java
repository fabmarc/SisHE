package com.indra.sishe.controller.projeto;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import com.indra.infra.resource.MessageProvider;
import com.indra.sishe.entity.Projeto;

@ViewScoped
@ManagedBean(name = "projetoCad")
public class ProjetoCadController extends ProjetoController {

	private static final long serialVersionUID = 5528166823228155750L;

	public Projeto projetoSelecionado;

	public ProjetoCadController() {
	}

	@PostConstruct
	private void init() {
		MessageProvider.setInstance(messageProvider);

		searched = (Boolean) getFlashAttr("searched");
		projetoSelecionado = (Projeto) getFlashAttr("projetoSelecionado");
		if (projetoSelecionado == null) {
			projetoSelecionado = new Projeto(/*new Usuario()*/);
		}
		projetoFiltro = (Projeto) getFlashAttr("projetoFiltro");
	}

	public String cadastrarProjeto() {
		if (validarProjeto(projetoSelecionado)) {
			try {
				projetoService.save(projetoSelecionado);
				putFlashAttr("projetoFiltro", projetoFiltro);
				returnInfoMessage(messageProvider.getMessage("msg.success.registro.cadastrado", "Projeto"));
				putFlashAttr("searched", searched);
				return irParaConsultar();
			} catch (Exception e) {
				returnErrorMessage(e.getMessage());
			}
		}
		return null;
	}

	public String confirmar() {
		if (projetoSelecionado.getId() == null || "".equals(projetoSelecionado.getId().toString())) {
			cadastrarProjeto();
		}else{
			alterarProjeto();
		}
		return irParaConsultar();
	}

	public String alterarProjeto() {
		if (validarProjeto(projetoSelecionado)) {
			try {
				projetoService.update(projetoSelecionado);
				returnInfoMessage(messageProvider.getMessage("msg.success.registro.alterado", "Projeto"));
				putFlashAttr("projetoFiltro", projetoFiltro);
				putFlashAttr("searched", searched);
				return irParaConsultar();
			} catch (Exception e) {
				returnErrorMessage(e.getMessage());
				return irParaAlterar(projetoSelecionado);
			}
		}
		return irParaConsultar();
	}
	
	public String cancelar() {
		putFlashAttr("searched", searched);
		putFlashAttr("projetoFiltro", projetoFiltro);
		return irParaConsultar();
	}

	public Projeto getProjetoSelecionado() {
		return projetoSelecionado;
	}

	public void setProjetoSelecionado(Projeto projetoSelecionado) {
		this.projetoSelecionado = projetoSelecionado;
	}
	
	
}
