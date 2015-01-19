package com.indra.sishe.controller.sistema;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;

import com.indra.infra.controller.BaseController;
import com.indra.sishe.entity.Sindicato;
import com.indra.sishe.entity.Sistema;
import com.indra.sishe.service.SistemaService;

public class SistemaController extends BaseController implements Serializable {


	/**
	 * 
	 */
	private static final long serialVersionUID = -2097043422349464567L;
	@Inject
	protected transient SistemaService sistemaService;
	protected List<Sistema> listaSistema;
	
	// VARIÁVEL UTILIZADA PARA O FILTRO DA PESQUISA
	public Sindicato sistemaFiltro ;

	// VARIÁVEL UTILIZADA PARA EXCLUIR OU ALTERAR
	public Sindicato sitemaSelecionado;

	// TRUE QUANDO O BOTÃO PESQUISAR FOR PRESSIONADO
	protected Boolean searched;
	
	public SistemaController() {
		// TODO Auto-generated constructor stub
	}
	
	// FALTA ADICIONAR A VERIFICAÇÃO PARA PROJETO
	public boolean validarSistema(Sistema sistemaFiltro) {
		
		if( sistemaFiltro != null) {
			
			if (sistemaFiltro.getDescricao().length() > 200) {
				messager.error(messageProvider.getMessage(
						"msg.error.campo.maior.esperado", "Descrição"));
				return false;
			}else if (sistemaFiltro.getDescricao() == null) {
				messager.error(messageProvider.getMessage(
						"msg.error.campo.obrigatorio", "Descrição"));
				return false;
			} else if (sistemaFiltro.getNome().length() > 50) {
				messager.error(messageProvider.getMessage(
						"msg.error.campo.maior.esperado", "Nome"));
				return false;
			}else if (sistemaFiltro.getNome() == null) {
				messager.error(messageProvider.getMessage(
						"msg.error.campo.obrigatorio", "Nome"));
				return false;
			}else if (sistemaFiltro.getUsuario() == null) {
				messager.error(messageProvider.getMessage(
						"msg.error.campo.maior.esperado", "Lider"));
				return false;
			}			
			else {
				return true;
			}			
			
			
		}else{
			return true;
		}
				
				//|| validarNome(sistemaFiltro) 
				//|| sistemaFiltro.getUsuario() != null || sistemaFiltro.getUsuario().getId() != null  ){
		
			
		}
		
	
	
	
	
	
	public String irParaConsultar() {
		return "/paginas/sistema/consultarSistema.xhtml?faces-redirect=true";
	}

	public String irParaCadastrar() {
		putFlashAttr("searched", searched);
		putFlashAttr("sindicatoFiltro", sistemaFiltro);
		return "/paginas/sistema/cadastrarSistema.xhtml?faces-redirect=true";
	}

	public String irParaAlterar(Sindicato sindicatoSelecionado) {
		putFlashAttr("searched", searched);
		putFlashAttr("sindicatoFiltro", sistemaFiltro);
		putFlashAttr("sindicatoSelecionado", sindicatoSelecionado);
		return "/paginas/sistema/alterarSistema.xhtml";
	}
	
	

}
