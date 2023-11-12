package co.com.bbva.app.notas.contables.carga.dto;

import co.com.bbva.app.notas.contables.dto.CommonVO;
import co.com.bbva.app.notas.contables.dto.Rol;

import java.sql.Timestamp;

public class UsuarioAltamira extends CommonVO<UsuarioAltamira> implements java.io.Serializable {

	private static final long serialVersionUID = -6172959578133861095L;
	private String codigoEmpleado = "";
	private String nombreEmpleado = "";
	private Number codigoArea = 0;
	private String nombreArea = "";
	private String codigoPerfil = "";
	private String nombrePerfil = "";
	private String correoElectronico = "";
	private String estadoCarga = "";
	private Timestamp fechaUltimaCarga = null;

	private Rol rol = new Rol();

	public UsuarioAltamira() {
		codigoEmpleado = "";
		nombreEmpleado = "";
		codigoArea = 0;
		nombreArea = "";
		codigoPerfil = "";
		nombrePerfil = "";
		correoElectronico = "";
		estadoCarga = "";
		fechaUltimaCarga = null;
	}

	public String getCodigoEmpleado() {
		return codigoEmpleado;
	}

	public void setCodigoEmpleado(String codigoEmpleado) {
		this.codigoEmpleado = codigoEmpleado;
	}

	public String getNombreEmpleado() {
		return nombreEmpleado;
	}

	public void setNombreEmpleado(String nombreEmpleado) {
		this.nombreEmpleado = nombreEmpleado;
	}

	public Number getCodigoArea() {
		return codigoArea;
	}

	public void setCodigoArea(Number codigoArea) {
		this.codigoArea = codigoArea;
	}

	public String getNombreArea() {
		return nombreArea;
	}

	public void setNombreArea(String nombreArea) {
		this.nombreArea = nombreArea;
	}

	public String getCodigoPerfil() {
		return codigoPerfil;
	}

	public void setCodigoPerfil(String codigoPerfil) {
		this.codigoPerfil = codigoPerfil;
	}

	public String getNombrePerfil() {
		return nombrePerfil;
	}

	public void setNombrePerfil(String nombrePerfil) {
		this.nombrePerfil = nombrePerfil;
	}

	public String getCorreoElectronico() {
		return correoElectronico;
	}

	public void setCorreoElectronico(String correoElectronico) {
		this.correoElectronico = correoElectronico;
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

	public Rol getRol() {
		return rol;
	}

	public void setRol(Rol rol) {
		this.rol = rol;
	}

	@Override
	public Object getPK() {
		return codigoEmpleado;
	}

}
