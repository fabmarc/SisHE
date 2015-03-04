package com.indra.sishe.service.impl;

import java.util.List;

import javax.ejb.Stateless;

import org.springframework.beans.factory.annotation.Autowired;

import com.indra.infra.dao.exception.RegistroDuplicadoException;
import com.indra.infra.service.exception.ApplicationException;
import com.indra.sishe.dao.BancoHorasDAO;
import com.indra.sishe.entity.BancoHoras;
import com.indra.sishe.entity.HistoricoDetalhes;
import com.indra.sishe.entity.Usuario;
import com.indra.sishe.service.BancoHorasService;
import com.indra.sishe.service.StatelessServiceAb;

@Stateless
public class BancoHorasServiceImpl extends StatelessServiceAb implements BancoHorasService {

	private static final long serialVersionUID = 1877572808403223993L;

	@Autowired
	private BancoHorasDAO bancoHorasDao;

	public BancoHorasServiceImpl() {

	}

	@Override
	public BancoHoras save(BancoHoras entity) throws ApplicationException {
		try {
			return bancoHorasDao.save(entity);
		} catch (RegistroDuplicadoException e) {
			throw new ApplicationException(e, "msg.error.registro.duplicado", "Banco de horas");
		}
	}

	@Override
	public BancoHoras update(BancoHoras entity) throws ApplicationException {
		return null;
	}

	@Override
	public List<BancoHoras> findAll() {
		return null;
	}

	@Override
	public BancoHoras findById(Long id) throws ApplicationException {
		return null;
	}

	@Override
	public void remove(Long id) throws ApplicationException {
	}

	@Override
	public void remove(List<Long> ids) throws ApplicationException {
	}

	@Override
	public List<HistoricoDetalhes> contabilizarHorasBanco(List<Long> idsSolicitacoes) {
		return bancoHorasDao.contabilizarHorasBanco(idsSolicitacoes);
	}

	@Override
	public BancoHoras findByUsuario(Usuario entity) {
		return bancoHorasDao.findByUsuario(entity);
	}

	@Override
	public void alterarHoras(Long idUsuario, Integer minutos) {
		bancoHorasDao.alterarHoras(idUsuario, minutos);
	}

	@Override
	public String findSaldoFormatadoByUsuario(Usuario entity) {
		String saldoFormatado;
		Long saldo = bancoHorasDao.findByUsuario(entity).getSaldo();
		Long min, horas;
		horas = saldo / 60;
		min = saldo % 60;
		if (saldo < 0) {
			if (horas == 0 && min < 0) {
				saldoFormatado = "- " + Math.abs(min) + "min";
			} else {
				saldoFormatado = "- " + Math.abs(horas) + "h e " + Math.abs(min) + "min";
			}
		} else {
			if (horas == 0) {
				saldoFormatado = Math.abs(min) + "min";
			} else {
				saldoFormatado = horas + "h e " + min + "min";
			}
		}
		return saldoFormatado;
	}

}
