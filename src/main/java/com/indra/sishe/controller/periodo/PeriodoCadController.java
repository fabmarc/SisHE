package com.indra.sishe.controller.periodo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import com.indra.infra.resource.MessageProvider;
import com.indra.infra.service.exception.ApplicationException;
import com.indra.sishe.entity.Periodo;
import com.indra.sishe.entity.Regra;
import com.indra.sishe.enums.DiaSemanaEnum;

@ViewScoped
@ManagedBean(name = "periodoCad")
public class PeriodoCadController extends PeriodoController {

	private static final long serialVersionUID = 230592613883854267L;

	protected Periodo periodoSelecionado;

	@PostConstruct
	private void init() {

		MessageProvider.setInstance(messageProvider);

		searched = (Boolean) getFlashAttr("searched");

		periodoSelecionado = (Periodo) getFlashAttr("periodoSelecionado");
		if (periodoSelecionado == null) periodoSelecionado = new Periodo();

		periodoFiltro = (Periodo) getFlashAttr("periodoFiltro");
		
		regraSelecionada = (Regra) getFlashAttr("regraSelecionadaFiltro");
	}

	public String cadastrarPeriodo() {
		try {
			periodoSelecionado.setRegra(regraSelecionada);
			this.periodoSelecionado = periodoService.save(periodoSelecionado);
			putFlashAttr("regraSelecionadaFiltro",regraSelecionada);
			putFlashAttr("periodoFiltro", periodoFiltro);
			returnInfoMessage(messageProvider.getMessage("msg.success.registro.cadastrado", "Periodo"));
			putFlashAttr("searched", searched);
			return irParaConsultar();
		} catch (ApplicationException e) {
			returnErrorMessage(e.getMessage());
		}
		return null;
	}

	public String alterarPeriodo() {
		try {
			periodoService.update(periodoSelecionado);
			returnInfoMessage(messageProvider.getMessage("msg.success.registro.alterado", "Periodo"));
			putFlashAttr("regraSelecionadaFiltro",regraSelecionada);
			putFlashAttr("periodoFiltro", periodoFiltro);
			putFlashAttr("searched", searched);
			return irParaConsultar();
		} catch (ApplicationException e) {
			returnErrorMessage(e.getMessage());
			return irParaAlterar(periodoSelecionado);
		}
	}

	public String cancelar() {
		putFlashAttr("searched", searched);
		putFlashAttr("periodoFiltro", periodoFiltro);
		putFlashAttr("regraSelecionadaFiltro",regraSelecionada);
		putFlashAttr("periodoSelecionado", null);
		return irParaConsultar();
	}

	private boolean modoCadastrar() {
		if (periodoSelecionado == null || periodoSelecionado.getId() == null) {
			return true;
		} else {
			return false;
		}
	}

	public String confirmar() {
		if (modoCadastrar()) {
			return cadastrarPeriodo();
		} else {
			return alterarPeriodo();
		}
	}

	public Periodo getPeriodoSelecionado() {
		return periodoSelecionado;
	}

	public void setPeriodoSelecionado(Periodo periodoSelecionado) {
		this.periodoSelecionado = periodoSelecionado;
	}

	public List<DiaSemanaEnum> obterListaDias() {
		List<DiaSemanaEnum> listaDias = new ArrayList<DiaSemanaEnum>(Arrays.asList(DiaSemanaEnum.values()));
		return listaDias;
	}

}
