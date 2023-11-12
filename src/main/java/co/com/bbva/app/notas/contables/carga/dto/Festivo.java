package co.com.bbva.app.notas.contables.carga.dto;

import co.com.bbva.app.notas.contables.dto.CommonVO;

import java.sql.Date;
import java.sql.Timestamp;

public class Festivo extends CommonVO<Festivo> implements java.io.Serializable {

	private static final long serialVersionUID = 2118288422636272717L;
	private Date fecha = null;
	private String estadoCarga = "";
	private Timestamp fechaUltimaCarga = null;

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
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
