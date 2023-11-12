package co.com.bbva.app.notas.contables.carga.dto;

import co.com.bbva.app.notas.contables.dto.CommonVO;

import java.sql.Timestamp;

public class CierreMensual extends CommonVO<CierreMensual> implements java.io.Serializable {

	private static final long serialVersionUID = 3678980388903498599L;

	private int year = 0;
	private int mes = 0;
	private Timestamp fechaUltimaCarga = null;

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public int getMes() {
		return mes;
	}

	public void setMes(int mes) {
		this.mes = mes;
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
