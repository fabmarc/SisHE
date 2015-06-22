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

@ViewScoped
@ManagedBean(name = "folgaCad")
public class FolgaCadController extends FolgaController{
	
	private static final long serialVersionUID = -3504568175362667869L;
	
	private Folga folgaSelecionada;
	private Folga folga;
	
	private List<Folga> listaFolgas;
	
	/*****************************************/
	private ScheduleModel eventModel;
    
    private ScheduleEvent event = new DefaultScheduleEvent();
    /******************************************/
	
	@PostConstruct
	private void init() {
		 	/*********************************************************/
		listaFolgas = new ArrayList<Folga>();
		//listaFolgas = folgaService.findByFilterByUsuario(folga)
		eventModel = new DefaultScheduleModel();
        
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
	
	public Date getRandomDate(Date base) {
        Calendar date = Calendar.getInstance();
        date.setTime(base);
        date.add(Calendar.DATE, ((int) (Math.random()*30)) + 1);    //set random day of month
         
        return date.getTime();
    }
     
    public Date getInitialDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(calendar.get(Calendar.YEAR), Calendar.FEBRUARY, calendar.get(Calendar.DATE), 0, 0, 0);
         
        return calendar.getTime();
    }
     
    public ScheduleModel getEventModel() {
        return eventModel;
    }
     
    private Calendar today() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE), 0, 0, 0);
 
        return calendar;
    }
     
    private Date previousDay8Pm() {
        Calendar t = (Calendar) today().clone();
        t.set(Calendar.AM_PM, Calendar.PM);
        t.set(Calendar.DATE, t.get(Calendar.DATE) - 1);
        t.set(Calendar.HOUR, 8);
         
        return t.getTime();
    }
     
    private Date previousDay11Pm() {
        Calendar t = (Calendar) today().clone();
        t.set(Calendar.AM_PM, Calendar.PM);
        t.set(Calendar.DATE, t.get(Calendar.DATE) - 1);
        t.set(Calendar.HOUR, 11);
         
        return t.getTime();
    }
     
    private Date today1Pm() {
        Calendar t = (Calendar) today().clone();
        t.set(Calendar.AM_PM, Calendar.PM);
        t.set(Calendar.HOUR, 1);
         
        return t.getTime();
    }
     
    private Date theDayAfter3Pm() {
        Calendar t = (Calendar) today().clone();
        t.set(Calendar.DATE, t.get(Calendar.DATE) + 2);     
        t.set(Calendar.AM_PM, Calendar.PM);
        t.set(Calendar.HOUR, 3);
         
        return t.getTime();
    }
 
    private Date today6Pm() {
        Calendar t = (Calendar) today().clone(); 
        t.set(Calendar.AM_PM, Calendar.PM);
        t.set(Calendar.HOUR, 6);
         
        return t.getTime();
    }
     
    private Date nextDay9Am() {
        Calendar t = (Calendar) today().clone();
        t.set(Calendar.AM_PM, Calendar.AM);
        t.set(Calendar.DATE, t.get(Calendar.DATE) + 1);
        t.set(Calendar.HOUR, 9);
         
        return t.getTime();
    }
     
    private Date nextDay11Am() {
        Calendar t = (Calendar) today().clone();
        t.set(Calendar.AM_PM, Calendar.AM);
        t.set(Calendar.DATE, t.get(Calendar.DATE) + 1);
        t.set(Calendar.HOUR, 11);
         
        return t.getTime();
    }
     
    private Date fourDaysLater3pm() {
        Calendar t = (Calendar) today().clone(); 
        t.set(Calendar.AM_PM, Calendar.PM);
        t.set(Calendar.DATE, t.get(Calendar.DATE) + 4);
        t.set(Calendar.HOUR, 3);
         
        return t.getTime();
    }
     
   /************************************************************/
    
    public ScheduleEvent getEvent() {
        return event;
    }
 
    public void setEvent(ScheduleEvent event) {
        this.event = event;
    }
     
    
    // Ao salvar
    public void addEvent(ActionEvent actionEvent) {
        if(event.getId() == null){
            folgaSelecionada.setSolicitante(new Usuario(UsuarioLogado.getId()));
            folgaSelecionada.setEvent(event);
            //folgaService.findAll();
            cadastrarFolga();
            event.setId(folgaSelecionada.getId().toString());
        	eventModel.addEvent(event);
        	folgaSelecionada = new Folga();
        } else {
        	folgaSelecionada.setTitulo(event.getTitle());
        	folgaSelecionada.setSolicitante(new Usuario(UsuarioLogado.getId()));
            eventModel.updateEvent(event);
        }
         
        event = new DefaultScheduleEvent();
    }
    
    public void removeEvent(ActionEvent actionEvent) {
        if(event.getId() != null)
        	eventModel.deleteEvent(event);
         
        event = new DefaultScheduleEvent();
    }
     
    // Ao selecionar um evento existente na data
    public void onEventSelect(SelectEvent selectEvent) {
    	event = (ScheduleEvent) selectEvent.getObject();
    	folgaSelecionada = new Folga();
    	try {
			folgaSelecionada = folgaService.findById(Long.parseLong(event.getId()));
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (ApplicationException e) {
			e.printStackTrace();
		}
    	event.setId(folgaSelecionada.getId().toString());
    	folgaSelecionada.setEvent(event);
    }
     
    // Ao selecionar uma data vazia
    public void onDateSelect(SelectEvent selectEvent) {
    	folgaSelecionada = new Folga();
        event = new DefaultScheduleEvent("", (Date) selectEvent.getObject(), (Date) selectEvent.getObject());
        folgaSelecionada.setEvent(event);
    }
     
    public void onEventMove(ScheduleEntryMoveEvent event) {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Event moved", "Day delta:" + event.getDayDelta() + ", Minute delta:" + event.getMinuteDelta());
         
        addMessage(message);
    }
     
    public void onEventResize(ScheduleEntryResizeEvent event) {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Event resized", "Day delta:" + event.getDayDelta() + ", Minute delta:" + event.getMinuteDelta());
         
        addMessage(message);
    }
     
    private void addMessage(FacesMessage message) {
        FacesContext.getCurrentInstance().addMessage(null, message);
    }
    
/*********************************************************/
	public String cadastrarFolga() {
		try {
			if (folgaService.validarFolga(folgaSelecionada)) {
				folgaSelecionada = folgaService.save(folgaSelecionada);
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
	
	public String confirmar() {
		if (modoCadastrar()) {
			return cadastrarFolga();
		} else {
			return alterarFolga();
		}
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
	
	
}
