package com.indra.sishe.controller.folga;

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

import com.indra.infra.resource.MessageProvider;
import com.indra.infra.service.exception.ApplicationException;
import com.indra.sishe.controller.usuario.UsuarioLogado;
import com.indra.sishe.entity.Folga;
import com.indra.sishe.entity.Usuario;
import com.indra.sishe.service.DatasFolgaService;

@ViewScoped
@ManagedBean(name = "folgaCad")
public class FolgaCadController extends FolgaController{
	
	private static final long serialVersionUID = -3504568175362667869L;
	
	private List<Folga> listaFolgas;
	
	@Inject
	protected transient DatasFolgaService datasFolgaService;
	
	/******************** Atributos para o componente calend�rio *********************/
	
	private ScheduleModel eventModel;
    
    private EventFolga eventFolgaSelecionada = new EventFolga();
    
    /*******************************************************************************/
	
	@PostConstruct
	private void init() {
		
		/************************** Carregar dados do componente calend�rio *******************************/
		
		eventModel = new DefaultScheduleModel();

		atualizarListaFolga();
		
		/**************************************************************/
	         
		MessageProvider.setInstance(messageProvider);

		searched = (Boolean) getFlashAttr("searched");

		folgaFiltro = (Folga) getFlashAttr("folgaFiltro");
		
	}
	
    /********************** A��es do componente calend�rio ********************************/ 
	
    public ScheduleModel getEventModel() {
        return eventModel;
    }
     
    public EventFolga getEventFolgaSelecionada() {
        return eventFolgaSelecionada;
    }
 
    public void setEventFolgaSelecionada(EventFolga eventFolgaSelecionada) {
        this.eventFolgaSelecionada = eventFolgaSelecionada;
    }
     
    // Ao salvar
    public void confirmar(ActionEvent actionEvent) {
        if(eventFolgaSelecionada.getId() == null){
            eventFolgaSelecionada.getFolga().setSolicitante(new Usuario(UsuarioLogado.getId()));
           
            // Corrigir a data, pois o componente obt�m uma data a menos.
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
        	// Corrigir a data, pois o componente obt�m uma data a menos.
        	Calendar dataCerta = Calendar.getInstance();
            dataCerta.setTime(eventFolgaSelecionada.getFolga().getDataInicio());
            dataCerta.add(Calendar.DATE, 1);
            eventFolgaSelecionada.getFolga().setDataInicio(dataCerta.getTime());
            dataCerta.setTime(eventFolgaSelecionada.getFolga().getDataFim());
            dataCerta.add(Calendar.DATE, 1);
            eventFolgaSelecionada.getFolga().setDataFim(dataCerta.getTime());
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
        eventFolgaSelecionada.getFolga().setDataInicio(eventFolgaSelecionada.getStartDate());
        eventFolgaSelecionada.getFolga().setDataFim(eventFolgaSelecionada.getEndDate());
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
    
    
    /************************** M�todos de folga *******************************/
    
    private void atualizarListaFolga(){
    	
    	eventModel = new DefaultScheduleModel();
    	
    	listaFolgas = folgaService.findFolgaByUsuario(new Usuario(UsuarioLogado.getId()));
		
        
		for(Folga f : listaFolgas) {
			if(f.getDatasFolga()!= null && !f.getDatasFolga().isEmpty()){
				f.setDataInicio(f.getDatasFolga().get(0).getData());
				f.setDataFim(f.getDatasFolga().get(f.getDatasFolga().size()-1).getData());
				EventFolga eventTemp = new EventFolga(f.getTitulo(), f.getDatasFolga().get(0).getData(), f.getDatasFolga().get(f.getDatasFolga().size()-1).getData(), f.getStatus().getNome(), f);
				eventTemp.setAllDay(true);
				eventModel.addEvent(eventTemp);
			}
		}
		
		RequestContext.getCurrentInstance().execute("myschedule.update();");
    }
    
	public String save(Folga entity) {
		try {
			if (folgaService.validarFolga(entity)) {
				folgaService.save(entity);
				putFlashAttr("folgaFiltro", folgaFiltro);
				returnInfoMessage(messageProvider.getMessage("msg.success.registro.cadastrado", "Solicita��o de Folga"));
				putFlashAttr("searched", searched);
				return irParaConsultar();
			}
		} catch (ApplicationException e) {
			returnErrorMessage(e.getMessage());
		}
		return null;
	}
	
	public String update(Folga entity){
		
		try {
			folgaService.update(entity);
			putFlashAttr("folgaFiltro", folgaFiltro);
			returnInfoMessage(messageProvider.getMessage("msg.success.registro.cadastrado", "Solicita��o de Folga"));
			putFlashAttr("searched", searched);
			return irParaConsultar();
		} catch (ApplicationException e) {
			returnErrorMessage(e.getMessage());
		}
		return null;
	}
	
	public void remove(Folga entity){
		
		try {
			folgaService.remove(entity.getId());
		} catch (ApplicationException e) {
			e.printStackTrace();
		}
	}
	/*
	public String alterarFolga(){
		try {
			folgaService.update(folgaSelecionada);
			returnInfoMessage(messageProvider.getMessage("msg.success.registro.alterado", "Solicita��o de Folga"));
			putFlashAttr("searched", searched);
			putFlashAttr("folgaFiltro", folgaFiltro);
			return irParaConsultar();
		} catch (ApplicationException e) {
			returnErrorMessage(e.getMessage());
			return irParaAlterar(folgaSelecionada);
		}
	}
	*/
	
	/*
	public Boolean modoCadastrar(){
		if (folgaSelecionada.equals(new Folga())) {
			return true;
		} else {
			return false;
		}
	}
*/
	public String cancelar(){
		putFlashAttr("searched", searched);
		putFlashAttr("folgaFiltro", folgaFiltro);
		return irParaConsultar();
	}

	public List<Folga> getListaFolgas() {
		return listaFolgas;
	}
	
	public void setListaFolgas(List<Folga> listaFolgas) {
		this.listaFolgas = listaFolgas;
	}
	
}
