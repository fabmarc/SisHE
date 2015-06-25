package com.indra.sishe.controller.folga;

import java.util.Date;

import org.primefaces.model.DefaultScheduleEvent;

import com.indra.sishe.entity.Folga;

public class EventFolga extends DefaultScheduleEvent {

	private static final long serialVersionUID = -4856820165438240881L;
	
	private Folga folga;
	
	public EventFolga(String title, Date start, Date end){
		super(title, start, end);
		folga = new Folga();
	}
	
	public EventFolga(String title, Date start, Date end, Folga f){
		super(title, start, end);
		this.folga = f;
	}
	
	public EventFolga(String title, Date start, Date end, String style, Folga f){
		super(title, start, end, style);
		this.folga = f;
	}
	
	public EventFolga(){
		super();
		folga = new Folga();
	}

	public Folga getFolga() {
		return folga;
	}

	public void setFolga(Folga folga) {
		this.folga = folga;
	}
	
}
