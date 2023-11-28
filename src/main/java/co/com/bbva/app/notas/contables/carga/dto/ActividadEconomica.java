package co.com.bbva.app.notas.contables.carga.dto;

import co.com.bbva.app.notas.contables.dto.CommonVO;

import java.sql.Timestamp;
import java.util.Objects;

public class ActividadEconomica extends CommonVO<ActividadEconomica> implements java.io.Serializable {

	private static final long serialVersionUID = -5586021096552870871L;
	private String codigo = "";
	private String nombre = "";
	private String estadoCarga = "";
	private Timestamp fechaUltimaCarga = null;

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
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
		return codigo;
	}


	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof ActividadEconomica that)) return false;
		return Objects.equals(getCodigo(), that.getCodigo()) && Objects.equals(getNombre(), that.getNombre()) && Objects.equals(getEstadoCarga(), that.getEstadoCarga()) && Objects.equals(getFechaUltimaCarga(), that.getFechaUltimaCarga());
	}

	@Override
	public int hashCode() {
		return Objects.hash(getCodigo(), getNombre(), getEstadoCarga(), getFechaUltimaCarga());
	}
}
