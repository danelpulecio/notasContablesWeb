package co.com.bbva.app.notas.contables.carga.dto;

import co.com.bbva.app.notas.contables.dto.CommonVO;

import java.sql.Timestamp;

public class PerdidaOperacionalClaseRiesgo extends CommonVO<PerdidaOperacionalClaseRiesgo> implements java.io.Serializable {

	private static final long serialVersionUID = -6447443899497280032L;
	private String cuenta = "";
	private String codigoClaseRiesgo = "";
	private String codigoTipoPerdida = "";
	private String estadoCarga = "";
	private Timestamp fechaUltimaCarga = null;

	public String getCuenta() {
		return cuenta;
	}

	public void setCuenta(String cuenta) {
		this.cuenta = cuenta;
	}

	public String getCodigoClaseRiesgo() {
		return codigoClaseRiesgo;
	}

	public void setCodigoClaseRiesgo(String codigoClaseRiesgo) {
		this.codigoClaseRiesgo = codigoClaseRiesgo;
	}

	public String getCodigoTipoPerdida() {
		return codigoTipoPerdida;
	}

	public void setCodigoTipoPerdida(String codigoTipoPerdida) {
		this.codigoTipoPerdida = codigoTipoPerdida;
	}

	public String getEstadoCarga() {
		return estadoCarga;
	}

	public void setEstadoCarga(String estadoCarga) {
		this.estadoCarga = estadoCarga;
	}

	public Timestamp getFechaUltimaCarga() {
		return fechaUltimaCarga;
	}

	public void setFechaUltimaCarga(Timestamp fechaUltimaCarga) {
		this.fechaUltimaCarga = fechaUltimaCarga;
	}

	@Override
	public Object getPK() {
		return null;
	}

}
