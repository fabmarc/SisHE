package com.indra.sishe.controller.folga;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;

import org.primefaces.context.RequestContext;

import com.indra.infra.resource.MessageProvider;
import com.indra.infra.service.exception.ApplicationException;
import com.indra.sishe.controller.usuario.UsuarioLogado;
import com.indra.sishe.entity.DatasFolga;
import com.indra.sishe.entity.Folga;
import com.indra.sishe.entity.Usuario;
import com.indra.sishe.enums.StatusEnum;
import com.indra.sishe.service.DatasFolgaService;

@ViewScoped
@ManagedBean(name = "folgaMnt")
public class FolgaMntController extends FolgaController {

	private static final long serialVersionUID = -6427814442444323592L;
	
	private List<Folga> listaFolgas;
	
	private List<Folga> folgasSelecionadas;
	
	private Folga folgaDetalhe;
	
	@Inject
	protected transient DatasFolgaService datasFolgaService;
	
	@PostConstruct
	public void init() {
		MessageProvider.setInstance(messageProvider);
		searched = (Boolean) getFlashAttr("searched");
		if (searched == null) searched = false;
		folgaFiltro = (Folga) getFlashAttr("folgaFiltro");
		if (folgaFiltro == null) folgaFiltro = new Folga();
	}
	
	public List<StatusEnum> listaStatus() {
		return StatusEnum.status();
	}
	
	public void pesquisar() {
		if (UsuarioLogado.getPermissoes().contains("ROLE_GERENTE")) { // Gerente consulta folga de todos do Projeto dele
			listaFolgas = folgaService.findFolgasByGerente(folgaFiltro, UsuarioLogado.getId());
		} else { // Usu�rios n�o gerentes consultam apenas suas pr�prias solicita��es de folga
			Usuario usuarioLogado = new Usuario();
			usuarioLogado.setId(UsuarioLogado.getId());
			folgaFiltro.setSolicitante(usuarioLogado);
			listaFolgas = folgaService.findByFilterByUsuario(folgaFiltro);
		}
	}
	
	public void beforeRemoverSolicitacao(){
		if (folgasSelecionadas.size() == 0) {
			RequestContext.getCurrentInstance().execute("selectAtleastOne.show()");
		}else {
			RequestContext.getCurrentInstance().execute("confirmExclusao.show()");
		}
	}
	
	public String remove(){
		try {
			folgaService.folgasParaRemocao(folgasSelecionadas);
		messager.info(messageProvider.getMessage("msg.success.registro.excluido", "Solicita��o(�es) de Folga"));
		} catch (ApplicationException e) {
			messager.error(e.getMessage());
		}
		
		pesquisar();
		return irParaConsultar();
	}
	
	public void beforeAprovarFolga() {
		if (folgasSelecionadas.size() == 0) {
			RequestContext.getCurrentInstance().execute("selectAtleastOne.show()");
		} else {
			RequestContext.getCurrentInstance().execute("confirmAprovacao.show()");
		}
	}
	
	public void beforeReprovarSolicitacao() {
		if (folgasSelecionadas.size() == 0) {
			RequestContext.getCurrentInstance().execute("selectAtleastOne.show()");
		} else {
			RequestContext.getCurrentInstance().execute("confirmReprovacao.show()");
		}
	}

	public void aprovar(){
		avaliarFolga(1);
	}
	
	public void reprovar(){
		avaliarFolga(2);
	}
	
	private void avaliarFolga(int acao) {

		try {
			int size = folgasSelecionadas.size();
			ArrayList<Long> ids = new ArrayList<Long>(size);
			for (Folga folga : folgasSelecionadas) {
				if (folga.getStatus().getId() == 3) { //retira as solicita��es que j� foram aprovadas/reprovadas
					ids.add(folga.getId());
				}
			}
			folgaService.avaliarFolga(ids, acao);
			
			if (size != ids.size()) {
				messager.info(messageProvider.getMessage("msg.success.solicitacao.aprovada.excecao"));
			} else {
				messager.info(messageProvider.getMessage("msg.success.solicitacao.aprovada"));
			}

		} catch (ApplicationException e) {
			messager.error(e.getMessage());
		}

	}

}
