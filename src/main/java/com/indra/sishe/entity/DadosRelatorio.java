package com.indra.sishe.entity;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class DadosRelatorio implements Serializable, Comparable<DadosRelatorio> {

	private static final long serialVersionUID = -2945377505162563295L;

	private int idSolicitacao;
	private String dataSolicitacao;
	private String dataHistorico;
	private String horaInicioSolicitacao;
	private String horaFimSolicitacao;
	private String minutos;
	private String porcentagem;
	private String valor;
	private String total;

	public DadosRelatorio(String dataSolicitacao, String dataHistorico, String horaInicioSolicitacao,
			String horaFimSolicitacao, String minutos, String porcentagem, String valor) {
		this.dataSolicitacao = dataSolicitacao;
		this.dataHistorico = dataHistorico;
		this.horaInicioSolicitacao = horaInicioSolicitacao;
		this.horaFimSolicitacao = horaFimSolicitacao;
		this.minutos = minutos;
		this.porcentagem = porcentagem;
		this.valor = valor;
	}

	public DadosRelatorio() {
	}

	public String getDataSolicitacao() {
		return dataSolicitacao;
	}

	public void setDataSolicitacao(String dataSolicitacao) {
		this.dataSolicitacao = dataSolicitacao;
	}

	public String getDataHistorico() {
		return dataHistorico;
	}

	public void setDataHistorico(String dataHistorico) {
		this.dataHistorico = dataHistorico;
	}

	public String getHoraInicioSolicitacao() {
		return horaInicioSolicitacao;
	}

	public void setHoraInicioSolicitacao(String horaInicioSolicitacao) {
		this.horaInicioSolicitacao = horaInicioSolicitacao;
	}

	public String getHoraFimSolicitacao() {
		return horaFimSolicitacao;
	}

	public void setHoraFimSolicitacao(String horaFimSolicitacao) {
		this.horaFimSolicitacao = horaFimSolicitacao;
	}

	public String getMinutos() {
		return minutos;
	}

	public void setMinutos(String minutos) {
		this.minutos = minutos;
	}

	public String getPorcentagem() {
		return porcentagem;
	}

	public void setPorcentagem(String porcentagem) {
		this.porcentagem = porcentagem;
	}

	public String getValor() {
		return valor;
	}

	public void setValor(String valor) {
		this.valor = valor;
	}

	public int getIdSolicitacao() {
		return idSolicitacao;
	}

	public void setIdSolicitacao(int idSolicitacao) {
		this.idSolicitacao = idSolicitacao;
	}

	public String getTotal() {
		return total;
	}

	public void setTotal(String total) {
		this.total = total;
	}

	public String obterMinutosSoliciacao() {
		Integer minInicial = minutosSolicitacao();
		return minInicial.toString() + "min (" + formatarHora(minInicial) + " horas)";
	}

	public Integer minutosSolicitacao() {

		SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
		Date d = null;
		try {
			d = df.parse(this.horaInicioSolicitacao);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		Calendar inicio = new GregorianCalendar();
		inicio.setTime(d);
		Date d2 = null;
		try {
			d2 = df.parse(this.horaFimSolicitacao);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		Calendar fim = new GregorianCalendar();
		fim.setTime(d2);
		Integer minInicial = (inicio.get(Calendar.HOUR_OF_DAY) * 60) + (inicio.get(Calendar.MINUTE));
		Integer minFinal = (fim.get(Calendar.HOUR_OF_DAY) * 60) + (fim.get(Calendar.MINUTE));
		return minFinal - minInicial;
	}

	public String porcentagemGeral() {
		return Integer.toString(((Integer.parseInt(total) - minutosSolicitacao()) * 100) / minutosSolicitacao())
				+ "%";
	}

	public String obterMinutosGerado() {
		return total + "min" + " (" + formatarHora(Integer.parseInt(total)) + " horas)";
	}

	public static String formatarHora(Integer minutos) {
		Integer hora = minutos / 60;
		Integer minuto = minutos % 60;
		String h = Integer.toString(hora);
		String m = Integer.toString(minuto);
		if (hora < 10) {
			h = "0" + Integer.toString(hora);
		}
		if (minuto < 10) {
			m = "0" + Integer.toString(minuto);
		}
		return h + ":" + m;
	}

	@Override
	public int compareTo(DadosRelatorio o) {
		// TODO Auto-generated method stub
		return 0;
	}

}
