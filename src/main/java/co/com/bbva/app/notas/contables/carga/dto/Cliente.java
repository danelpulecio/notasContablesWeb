package co.com.bbva.app.notas.contables.carga.dto;

import co.com.bbva.app.notas.contables.dto.CommonVO;

import java.sql.Timestamp;

public class Cliente extends CommonVO<Cliente> implements java.io.Serializable {

	private static final long serialVersionUID = -4285055944387435904L;
	private String numeroCliente = "";
	private String tipoIdentificacion = "";
	private String numeroIdentificacion = "";
	private String digitoVerificacion = "";
	private String primerApellido = "";
	private String segundoApellido = "";
	private String primerNombre = "";
	private String estadoCarga = "";
	private Timestamp fechaUltimaCarga = null;

	public String getNumeroCliente() {
		return numeroCliente;
	}

	public void setNumeroCliente(String numeroCliente) {
		this.numeroCliente = numeroCliente;
	}

	public String getTipoIdentificacion() {
		return tipoIdentificacion;
	}

	public void setTipoIdentificacion(String tipoIdentificacion) {
		this.tipoIdentificacion = tipoIdentificacion;
	}

	public String getNumeroIdentificacion() {
		return numeroIdentificacion;
	}

	public void setNumeroIdentificacion(String numeroIdentificacion) {
		this.numeroIdentificacion = numeroIdentificacion;
	}

	public String getDigitoVerificacion() {
		return digitoVerificacion;
	}

	public void setDigitoVerificacion(String digitoVerificacion) {
		this.digitoVerificacion = digitoVerificacion;
	}

	public String getPrimerApellido() {
		return primerApellido;
	}

	public void setPrimerApellido(String primerApellido) {
		this.primerApellido = primerApellido;
	}

	public String getSegundoApellido() {
		return segundoApellido;
	}

	public void setSegundoApellido(String segundoApellido) {
		this.segundoApellido = segundoApellido;
	}

	public String getPrimerNombre() {
		return primerNombre;
	}

	public void setPrimerNombre(String primerNombre) {
		this.primerNombre = primerNombre;
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
		return numeroCliente;
	}

	@Override
	public String toString() {
		return "Cliente{" +
				"numeroCliente='" + numeroCliente + '\'' +
				", tipoIdentificacion='" + tipoIdentificacion + '\'' +
				", numeroIdentificacion='" + numeroIdentificacion + '\'' +
				", digitoVerificacion='" + digitoVerificacion + '\'' +
				", primerApellido='" + primerApellido + '\'' +
				", segundoApellido='" + segundoApellido + '\'' +
				", primerNombre='" + primerNombre + '\'' +
				", estadoCarga='" + estadoCarga + '\'' +
				", fechaUltimaCarga=" + fechaUltimaCarga +
				'}';
	}
}
