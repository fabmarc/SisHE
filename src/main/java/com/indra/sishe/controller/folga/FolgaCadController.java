package com.indra.sishe.controller.folga;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Inject;

import org.primefaces.context.RequestContext;
import org.primefaces.event.ScheduleEntryMoveEvent;
import org.primefaces.event.ScheduleEntryResizeEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.DefaultScheduleEvent;
import org.primefaces.model.DefaultScheduleModel;
import org.primefaces.model.ScheduleEvent;
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
	
	private Folga folgaSelecionada;
	private Folga folga;
	
	private List<Folga> listaFolgas;
	
	@Inject
	protected transient DatasFolgaService datasFolgaService;
	
	/*****************************************/
	private ScheduleModel eventModel;
    
    private EventFolga event = new EventFolga();
    /******************************************/
	
	@PostConstruct
	private void init() {
		 	/*********************************************************/
		eventModel = new DefaultScheduleModel();

		atualizarListaFolga();
		
		/*eventModel.addEvent(new DefaultScheduleEvent("Pendente", previousDay8Pm(), previousDay11Pm(), "pendente"));
		eventModel.addEvent(new DefaultScheduleEvent("Cancelado", today1Pm(), today6Pm(), "cancelado"));
        eventModel.addEvent(new DefaultScheduleEvent("Aprovado", nextDay9Am(), nextDay11Am(), "aprovado"));
        eventModel.addEvent(new DefaultScheduleEvent("Plant the new garden stuff", theDayAfter3Pm(), fourDaysLater3pm()));*/
        
         
		/**************************************************************/
	         
		MessageProvider.setInstance(messageProvider);

		searched = (Boolean) getFlashAttr("searched");
		folgaSelecionada = (Folga) getFlashAttr("folgaSelecionada");

		if (folgaSelecionada == null) folgaSelecionada = new Folga();

		folgaFiltro = (Folga) getFlashAttr("folgaFiltro");
		
		folga = new Folga();
		
	}
/********************************************************************************************/
	
     
    public ScheduleModel getEventModel() {
        return eventModel;
    }
     
     
   /************************************************************/
    
    public EventFolga getEvent() {
        return event;
    }
 
    public void setEvent(EventFolga event) {
        this.event = event;
    }
     
    
    // Ao salvar
    public void confirmar(ActionEvent actionEvent) {
        if(event.getId() == null){
            event.getFolga().setSolicitante(new Usuario(UsuarioLogado.getId()));
           
            // Corrigir a data, pois o componente obtém uma data a menos.
            Calendar dataCerta = Calendar.getInstance();
            dataCerta.setTime(event.getStartDate());
            dataCerta.add(Calendar.DATE, 1);
            event.getFolga().setDataInicio(dataCerta.getTime());
            dataCerta.setTime(event.getEndDate());
            dataCerta.add(Calendar.DATE, 1);
            event.getFolga().setDataFim(dataCerta.getTime());
            
            save(event.getFolga());
            event.setTitle(event.getFolga().getTitulo());
        } else {
        	event.getFolga().setSolicitante(new Usuario(UsuarioLogado.getId()));
        	// Corrigir a data, pois o componente obtém uma data a menos.
        	Calendar dataCerta = Calendar.getInstance();
            dataCerta.setTime(event.getFolga().getDataInicio());
            dataCerta.add(Calendar.DATE, 1);
            event.getFolga().setDataInicio(dataCerta.getTime());
            dataCerta.setTime(event.getFolga().getDataFim());
            dataCerta.add(Calendar.DATE, 1);
            event.getFolga().setDataFim(dataCerta.getTime());
        	update(event.getFolga());
        	event.setTitle(event.getFolga().getTitulo());
        }
        eventModel.clear();
        atualizarListaFolga();
        RequestContext.getCurrentInstance().execute("myschedule.update();");
        event = new EventFolga();
    }
    
    public void removeEvent(ActionEvent actionEvent) {
        if(event.getId() != null)
        	eventModel.deleteEvent(event);
         
        event = new EventFolga();
    }
     
    // Ao selecionar um evento existente na data
    public void onEventSelect(SelectEvent selectEvent) {
    	event = (EventFolga) selectEvent.getObject();
    }
     
    // Ao selecionar uma data vazia
    public void onDateSelect(SelectEvent selectEvent) {
        event = new EventFolga("", (Date) selectEvent.getObject(), (Date) selectEvent.getObject());
        event.getFolga().setDataInicio(event.getStartDate());
        event.getFolga().setDataFim(event.getEndDate());
    }
     
    public void onEventMove(ScheduleEntryMoveEvent event) {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Event moved", "Day delta:" + event.getDayDelta() + ", Minute delta:" + event.getMinuteDelta());
         
        addMessage(message);
    }
     
    public void onEventResize(ScheduleEntryResizeEvent event) {
        //FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Event resized", "Day delta:" + event.getDayDelta() + ", Minute delta:" + event.getMinuteDelta());
        this.event = (EventFolga) event.getScheduleEvent();
        this.event.getFolga().setSolicitante(new Usuario(UsuarioLogado.getId()));
        if(event.getDayDelta()>0 && this.event.getStartDate().equals(this.event.getEndDate())){
        	Calendar c = Calendar.getInstance();
        	c.setTime(this.event.getStartDate());
        	c.add(Calendar.DATE, -event.getDayDelta());
        	this.event.getFolga().setDataInicio(c.getTime());
        	this.event.getFolga().setDataFim(this.event.getEndDate());
        }  else {
        	this.event.getFolga().setDataInicio(this.event.getStartDate());
        	this.event.getFolga().setDataFim(this.event.getEndDate());
        }
        
        
    	update(this.event.getFolga());
    	this.event.setTitle(this.event.getFolga().getTitulo());
    	
    	eventModel.clear();
    	atualizarListaFolga();
        RequestContext.getCurrentInstance().execute("myschedule.update();");
        
        this.event = new EventFolga();
       // addMessage(message);
    }
     
    private void addMessage(FacesMessage message) {
        FacesContext.getCurrentInstance().addMessage(null, message);
    }
    
/*********************************************************/
    
    private void atualizarListaFolga(){
    	
    	eventModel = new DefaultScheduleModel();
    	
    	listaFolgas = folgaService.findFolgaByUsuario(new Usuario(UsuarioLogado.getId()));
		
        
		for(Folga f : listaFolgas) {
			if(f.getDatasFolga()!= null && !f.getDatasFolga().isEmpty()){
				f.setDataInicio(f.getDatasFolga().get(0).getData());
				f.setDataFim(f.getDatasFolga().get(f.getDatasFolga().size()-1).getData());
				EventFolga eventTemp = new EventFolga(f.getTitulo(), f.getDatasFolga().get(0).getData(), f.getDatasFolga().get(f.getDatasFolga().size()-1).getData(), f);
				eventTemp.setId(f.getId().toString());
				eventModel.addEvent(eventTemp);
			}
		}
    }
    
	public String save(Folga entity) {
		try {
			if (folgaService.validarFolga(entity)) {
				folgaSelecionada = folgaService.save(entity);
				putFlashAttr("folgaFiltro", folgaFiltro);
				returnInfoMessage(messageProvider.getMessage("msg.success.registro.cadastrado", "Solicitação de Folga"));
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
			folgaSelecionada = folgaService.update(entity);
			putFlashAttr("folgaFiltro", folgaFiltro);
			returnInfoMessage(messageProvider.getMessage("msg.success.registro.cadastrado", "Solicitação de Folga"));
			putFlashAttr("searched", searched);
			return irParaConsultar();
		} catch (ApplicationException e) {
			returnErrorMessage(e.getMessage());
		}
		return null;
	}
	
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

	public String cancelar(){
		putFlashAttr("searched", searched);
		putFlashAttr("folgaFiltro", folgaFiltro);
		return irParaConsultar();
	}

	
	public Folga getFolgaSelecionada() {
		return folgaSelecionada;
	}
	
	public void setFolgaSelecionada(Folga folgaSelecionada) {
		this.folgaSelecionada = folgaSelecionada;
	}
	
	public Folga getFolga() {
		return folga;
	}
	
	public void setFolga(Folga folga) {
		this.folga = folga;
	}
	public List<Folga> getListaFolgas() {
		return listaFolgas;
	}
	public void setListaFolgas(List<Folga> listaFolgas) {
		this.listaFolgas = listaFolgas;
	}
	
}
