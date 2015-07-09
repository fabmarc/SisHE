package com.indra.sishe.controller.folga;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;
import javax.inject.Inject;

import org.primefaces.context.RequestContext;
import org.primefaces.event.ScheduleEntryMoveEvent;
import org.primefaces.event.ScheduleEntryResizeEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.DefaultScheduleModel;
import org.primefaces.model.ScheduleModel;

import com.indra.infra.controller.BaseController;
import com.indra.infra.resource.MessageProvider;
import com.indra.infra.service.exception.ApplicationException;
import com.indra.sishe.controller.usuario.UsuarioLogado;
import com.indra.sishe.entity.DatasFolga;
import com.indra.sishe.entity.Folga;
import com.indra.sishe.entity.Usuario;
import com.indra.sishe.enums.StatusEnum;
import com.indra.sishe.service.DatasFolgaService;
import com.indra.sishe.service.FolgaService;

@ViewScoped
@ManagedBean(name = "folgaCont")
public class FolgaController extends BaseController implements Serializable{

	private static final long serialVersionUID = -4667670116817828607L;
	
	@Inject
	protected transient FolgaService folgaService;

	@Inject
	protected transient DatasFolgaService datasFolgaService;
	
	private Folga folgaFiltro;
	
	private List<Folga> listaFolgas;

	protected Boolean searched;
	
	private boolean exibirCalendario;
	
	private List<Folga> folgasSelecionadas;
	
	private Folga folgaDetalhe;
	
	private Date dataFiltro;
	
	private Boolean gerenteLogado = false;
	
	private Boolean liderLogado = false;
	
	private Boolean todasSolicitacoes;
	
	
	/******************** Atributos para o componente calendário *********************/
	private ScheduleModel eventModel;
    
    private EventFolga eventFolgaSelecionada = new EventFolga();
    
    @PostConstruct
	private void init() {
		
		/************************** Carregar dados do componente calendário *******************************/
		eventModel = new DefaultScheduleModel();
		atualizarListaFolga();
		/**************************************************************/
		dataFiltro = new Date();
	         
		MessageProvider.setInstance(messageProvider);
		searched = (Boolean) getFlashAttr("searched");
		folgaFiltro = (Folga) getFlashAttr("folgaFiltro");
		if (folgaFiltro == null) {
			folgaFiltro = new Folga();
		}
		if(getSessionAttr("exibirCalendario") != null && !"".equals(getSessionAttr("exibirCalendario"))){
			exibirCalendario = (Boolean) getSessionAttr("exibirCalendario");
		}
		exibirCalendario = true;
		verificarCargoLogado();
	}
    

    // Ao salvar
    public void confirmar(ActionEvent actionEvent) {
        if(eventFolgaSelecionada.getId() == null){
            eventFolgaSelecionada.getFolga().setSolicitante(new Usuario(UsuarioLogado.getId()));
           
            // Corrigir a data, pois o componente obtém uma data a menos.
            Calendar dataCerta = Calendar.getInstance();
            dataCerta.setTime(eventFolgaSelecionada.getStartDate());
            dataCerta.add(Calendar.DATE, 1);
            eventFolgaSelecionada.getFolga().setDataInicio(dataCerta.getTime());
            dataCerta.setTime(eventFolgaSelecionada.getEndDate());
            dataCerta.add(Calendar.DATE, 1);
            eventFolgaSelecionada.getFolga().setDataFim(dataCerta.getTime());
            
            save(eventFolgaSelecionada.getFolga());
            eventFolgaSelecionada.setTitle(eventFolgaSelecionada.getFolga().getTitulo());
        } else {
        	eventFolgaSelecionada.getFolga().setSolicitante(new Usuario(UsuarioLogado.getId()));
//        	Corrigir a data, pois o componente obtém uma data a menos.
//        	Calendar dataCerta = Calendar.getInstance();
//            dataCerta.setTime(eventFolgaSelecionada.getFolga().getDataInicio());
//            dataCerta.add(Calendar.DATE, 1);
//            eventFolgaSelecionada.getFolga().setDataInicio(dataCerta.getTime());
//            dataCerta.setTime(eventFolgaSelecionada.getFolga().getDataFim());
//            dataCerta.add(Calendar.DATE, 1);
//            eventFolgaSelecionada.getFolga().setDataFim(dataCerta.getTime());
        	update(eventFolgaSelecionada.getFolga());
        	eventFolgaSelecionada.setTitle(eventFolgaSelecionada.getFolga().getTitulo());
        }

        atualizarListaFolga();

        eventFolgaSelecionada = new EventFolga();
        
    }
    
    public void removeEvent(ActionEvent actionEvent) {
    	
        if(eventFolgaSelecionada.getId() != null) {
        	remove(eventFolgaSelecionada.getFolga());
        }
        atualizarListaFolga();
        eventFolgaSelecionada = new EventFolga();
    }
     
    // Ao selecionar um evento existente na data
    public void onEventSelect(SelectEvent selectEvent) {
    	eventFolgaSelecionada = (EventFolga) selectEvent.getObject();
    }
     
    // Ao selecionar uma data vazia
    public void onDateSelect(SelectEvent selectEvent) {
    	
    	eventFolgaSelecionada = new EventFolga("", (Date) selectEvent.getObject(), (Date) selectEvent.getObject());
    	SimpleDateFormat formatarData = new SimpleDateFormat("dd/MM/yyyy");
    	Calendar dtIni = Calendar.getInstance();
    	Calendar hoje = Calendar.getInstance();
    	
    	try {
			dtIni.setTime(formatarData.parse(formatarData.format(eventFolgaSelecionada.getStartDate())));
			dtIni.add(Calendar.DATE, 1);
			hoje.setTime(formatarData.parse(formatarData.format(new Date())));
		} catch (ParseException e) {
			e.printStackTrace();
		}
    	
    	if(!dtIni.getTime().before(hoje.getTime())){
    		eventFolgaSelecionada.getFolga().setDataInicio(dtIni.getTime());
    		eventFolgaSelecionada.getFolga().setDataFim(dtIni.getTime());
    		
    		RequestContext.getCurrentInstance().execute("eventDialog.show();");
    	}
    	
    }
     
    public void onEventMove(ScheduleEntryMoveEvent event) {
    	
    	this.eventFolgaSelecionada = (EventFolga) event.getScheduleEvent();
        this.eventFolgaSelecionada.getFolga().setSolicitante(new Usuario(UsuarioLogado.getId()));
        
        if(event.getDayDelta() != 0 && this.eventFolgaSelecionada.getStartDate().equals(this.eventFolgaSelecionada.getEndDate())){
        	Calendar c = Calendar.getInstance();
        	c.setTime(this.eventFolgaSelecionada.getStartDate());
        	c.add(Calendar.DATE, -event.getDayDelta());
        	this.eventFolgaSelecionada.getFolga().setDataInicio(c.getTime());
        	this.eventFolgaSelecionada.getFolga().setDataFim(c.getTime());
        }  else {
        	this.eventFolgaSelecionada.getFolga().setDataInicio(this.eventFolgaSelecionada.getStartDate());
        	this.eventFolgaSelecionada.getFolga().setDataFim(this.eventFolgaSelecionada.getEndDate());
        }
    	
    	update(this.eventFolgaSelecionada.getFolga());
    	this.eventFolgaSelecionada.setTitle(this.eventFolgaSelecionada.getFolga().getTitulo());
    	
    	atualizarListaFolga();
        
        this.eventFolgaSelecionada = new EventFolga();

    }
     
    public void onEventResize(ScheduleEntryResizeEvent event) {

    	this.eventFolgaSelecionada = (EventFolga) event.getScheduleEvent();
        this.eventFolgaSelecionada.getFolga().setSolicitante(new Usuario(UsuarioLogado.getId()));
        if(event.getDayDelta()>0 && this.eventFolgaSelecionada.getStartDate().equals(this.eventFolgaSelecionada.getEndDate())){
        	Calendar c = Calendar.getInstance();
        	c.setTime(this.eventFolgaSelecionada.getStartDate());
        	c.add(Calendar.DATE, -event.getDayDelta());
        	this.eventFolgaSelecionada.getFolga().setDataInicio(c.getTime());
        	this.eventFolgaSelecionada.getFolga().setDataFim(this.eventFolgaSelecionada.getEndDate());
        }  else {
        	this.eventFolgaSelecionada.getFolga().setDataInicio(this.eventFolgaSelecionada.getStartDate());
        	this.eventFolgaSelecionada.getFolga().setDataFim(this.eventFolgaSelecionada.getEndDate());
        }
        
        
    	update(this.eventFolgaSelecionada.getFolga());
    	this.eventFolgaSelecionada.setTitle(this.eventFolgaSelecionada.getFolga().getTitulo());
    	
    	atualizarListaFolga();
        
        this.eventFolgaSelecionada = new EventFolga();

    }
    
    
    /************************** Métodos de folga *******************************/
    
    private void atualizarListaFolga(){
    	
    	eventModel = new DefaultScheduleModel();
    	Folga folga = new Folga();
    	folga.setSolicitante(new Usuario(UsuarioLogado.getId()));
    	listaFolgas = folgaService.findFolgaByUsuario(folga);
		
        
		for(Folga f : listaFolgas) {
			if(f.getDatasFolga()!= null && !f.getDatasFolga().isEmpty()){
				EventFolga eventTemp = new EventFolga(f.getTitulo(), f.getDatasFolga().get(0).getData(), f.getDatasFolga().get(f.getDatasFolga().size()-1).getData(), f.getStatusGeral().getNome(), f);
//				if(!(eventTemp.getFolga().getStatusGeral().equals(StatusEnum.Pendente))) {
//					eventTemp.setEditable(false);
//				}
				eventTemp.setAllDay(true);
				eventModel.addEvent(eventTemp);
			}
		}
		RequestContext.getCurrentInstance().execute("myschedule.update();");
    }
    
	public String save(Folga entity) {
		try {
			folgaService.save(entity);
			putFlashAttr("folgaFiltro", folgaFiltro);
			returnInfoMessage(messageProvider.getMessage("msg.success.registro.cadastrado", "Solicitação de Folga"));
			putFlashAttr("searched", searched);
			return irParaConsultar();
		} catch (ApplicationException e) {
			returnErrorMessage(e.getMessage());
		}
		return null;
	}
	
	public String update(Folga entity){
		
		try {
			folgaService.update(entity);
			putFlashAttr("folgaFiltro", folgaFiltro);
			returnInfoMessage(messageProvider.getMessage("msg.success.registro.cadastrado", "Solicitação de Folga"));
			putFlashAttr("searched", searched);
			return irParaConsultar();
		} catch (ApplicationException e) {
			returnErrorMessage(e.getMessage());
		}
		return null;
	}
	
	public void remove(Folga entity){
		
		try {
			folgaService.remove(entity);
		} catch (ApplicationException e) {
			returnErrorMessage(e.getMessage());
		}
	}
	/*
	public String alterarFolga(){
		try {
			folgaService.update(folgaSelecionada);
			returnInfoMessage(messageProvider.getMessage("msg.success.registro.alterado", "Solicitação de Folga"));
			putFlashAttr("searched", searched);
			putFlashAttr("folgaFiltro", folgaFiltro);
			return irParaConsultar();
		} catch (ApplicationException e) {
			returnErrorMessage(e.getMessage());
			return irParaAlterar(folgaSelecionada);
		}
	}

	public Boolean modoCadastrar(){
		if (folgaSelecionada.equals(new Folga())) {
			return true;
		} else {
			return false;
		}
	}
	 */
	
	public List<StatusEnum> listaStatus() {
		return StatusEnum.status();
	}
	
	public void pesquisar() {
//		if (dataFiltro != null) {
//			List<DatasFolga> dataFolgaFiltro = new ArrayList<DatasFolga>(1);
//			DatasFolga dataFolga = new DatasFolga(dataFiltro);
//			dataFolgaFiltro.add(dataFolga);
//			folgaFiltro.setDatasFolga(dataFolgaFiltro);
//		}
		if (UsuarioLogado.getPermissoes().contains("ROLE_GERENTE")) { // Gerente consulta folga de recursos de todos do Projeto dele
			pesquisarGerente();
		} else if (UsuarioLogado.getPermissoes().contains("ROLE_LIDER")) {
			pesquisarLider();
		} else { 
			pesquisarPorUsuario();
		}
	}
	
	private void pesquisarGerente(){
		listaFolgas = folgaService.findFolgasByGerente(folgaFiltro);
	}
	                               
	
	private void pesquisarLider() {
		if (todasSolicitacoes) {
			folgaService.findFolgasBylider(folgaFiltro);
		}else {
			pesquisarPorUsuario();
		}
	}
	
	private void pesquisarPorUsuario(){
		listaFolgas = folgaService.findFolgaByUsuario(folgaFiltro);
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
		messager.info(messageProvider.getMessage("msg.success.registro.excluido", "Solicitação(ões) de Folga"));
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
				if (folga.getStatusGerente().getId() == 3) { //retira as solicitações que já foram aprovadas/reprovadas
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
	
	public Boolean permiteAlteracao(Folga folga){
		Boolean retorno = false;
		if (UsuarioLogado.verificarPermissao("ROLE_LIDER")) {
			if (folga.getSolicitante().getId() == UsuarioLogado.getId() && folga.getStatusGerente() == StatusEnum.Pendente) {
				retorno = true;
			}
		}else {
			if (folga.getSolicitante().getId() == UsuarioLogado.getId() && folga.getStatusLider() == StatusEnum.Pendente) {
				retorno = true;
			}
		}
		return retorno;
	}
	
	public void verificarCargoLogado() {
		if (UsuarioLogado.verificarPermissao("ROLE_GERENTE")) {
			gerenteLogado = true;
		} else if (UsuarioLogado.verificarPermissao("ROLE_LIDER")) {
			setLiderLogado(true);
		}
	}

	public String cancelar(){
		putFlashAttr("searched", searched);
		putFlashAttr("folgaFiltro", folgaFiltro);
		return irParaConsultar();
	}

	
	
	public void mostrarCalendario(){
		this.exibirCalendario = true;
		putSessionAttr("exibirCalendario", true);
	}
	
	public void mostrarLista(){
		this.exibirCalendario = false;
		putSessionAttr("exibirCalendario", false);
	}
    
	
	public String irParaConsultar() {
		return "/paginas/folga/folga.xhtml?faces-redirect=true";
	}
	
	public String irParaCadastrar(){
		putFlashAttr("searched", this.searched);
		putFlashAttr("folgaFiltro", this.folgaFiltro);
		putFlashAttr("folgaSelecionada", null);
		return irParaConsultar();
	}
	

	public String irParaAlterar(Folga folgaSelecionada){
		putFlashAttr("searched", this.searched);
		putFlashAttr("folgaFiltro", this.folgaFiltro);
		putFlashAttr("folgaSelecionada", folgaSelecionada);
		return irParaConsultar();
	}


	public Folga getFolgaFiltro() {
		return folgaFiltro;
	}


	public void setFolgaFiltro(Folga folgaFiltro) {
		this.folgaFiltro = folgaFiltro;
	}


	public List<Folga> getListaFolgas() {
		return listaFolgas;
	}


	public void setListaFolgas(List<Folga> listaFolgas) {
		this.listaFolgas = listaFolgas;
	}


	public Boolean getSearched() {
		return searched;
	}


	public void setSearched(Boolean searched) {
		this.searched = searched;
	}


	public boolean isExibirCalendario() {
		return exibirCalendario;
	}


	public void setExibirCalendario(boolean exibirCalendario) {
		this.exibirCalendario = exibirCalendario;
	}


	public List<Folga> getFolgasSelecionadas() {
		return folgasSelecionadas;
	}


	public void setFolgasSelecionadas(List<Folga> folgasSelecionadas) {
		this.folgasSelecionadas = folgasSelecionadas;
	}


	public Folga getFolgaDetalhe() {
		return folgaDetalhe;
	}


	public void setFolgaDetalhe(Folga folgaDetalhe) {
		this.folgaDetalhe = folgaDetalhe;
	}


	public Date getDataFiltro() {
		return dataFiltro;
	}


	public void setDataFiltro(Date dataFiltro) {
		this.dataFiltro = dataFiltro;
	}


	public Boolean getGerenteLogado() {
		return gerenteLogado;
	}


	public void setGerenteLogado(Boolean gerenteLogado) {
		this.gerenteLogado = gerenteLogado;
	}


	public Boolean getLiderLogado() {
		return liderLogado;
	}


	public void setLiderLogado(Boolean liderLogado) {
		this.liderLogado = liderLogado;
	}


	public Boolean getTodasSolicitacoes() {
		return todasSolicitacoes;
	}


	public void setTodasSolicitacoes(Boolean todasSolicitacoes) {
		this.todasSolicitacoes = todasSolicitacoes;
	}


	public ScheduleModel getEventModel() {
		return eventModel;
	}


	public void setEventModel(ScheduleModel eventModel) {
		this.eventModel = eventModel;
	}


	public EventFolga getEventFolgaSelecionada() {
		return eventFolgaSelecionada;
	}


	public void setEventFolgaSelecionada(EventFolga eventFolgaSelecionada) {
		this.eventFolgaSelecionada = eventFolgaSelecionada;
	}

}
