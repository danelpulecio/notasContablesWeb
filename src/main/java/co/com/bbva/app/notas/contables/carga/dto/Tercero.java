package co.com.bbva.app.notas.contables.carga.dto;

import co.com.bbva.app.notas.contables.dto.CommonVO;

import java.sql.Timestamp;

public class Tercero extends CommonVO<Tercero> implements java.io.Serializable {

	private static final long serialVersionUID = 4949795663382871864L;
	private String tipoIdentificacion = "";
	private String numeroIdentificacion = "";
	private String digitoVerificacion = "";
	private String primerApellido = "";
	private String segundoApellido = "";
	private String primerNombre = "";
	private String segundoNombre = "";
	private String PEATVIA = "";
	private String direccion = "";
	private String departamento = "";
	private String municipio = "";
	private String actividadEconomica = "";
	private String tipoTelefono = "";
	private String indicativo = "";
	private String telefono = "";
	private String aplicativo = "";
	private String usuario = "";
	private String oficinaModificacion = "";
	private String fecha = "";
	private String estadoCarga = "";
	private String pais = "";
	private Number extension = 0;
	private String tipoTelefono2 = "";
	private String indicativo2 = "";
	private String telefono2 = "";
	private Number extension2 = 0;
	private String moneda = "";
	private String indicadorNotasContables = "";
	private String EMail = "";
	private String regimenTributario = "";
	private String sexo = "";
	private Number ingresoApp = 0;

	private Timestamp fechaUltimaCarga = null;

	// Campo Transient
	private String tipoIdentificacionStr = "";

	public String getTipoIdentificacionStr() {
		return tipoIdentificacionStr;
	}

	public void setTipoIdentificacionStr(String tipoIdentificacionStr) {
		this.tipoIdentificacionStr = tipoIdentificacionStr;
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

	public String getSegundoNombre() {
		return segundoNombre;
	}

	public void setSegundoNombre(String segundoNombre) {
		this.segundoNombre = segundoNombre;
	}

	public String getPEATVIA() {
		return PEATVIA;
	}

	public void setPEATVIA(String pEATVIA) {
		PEATVIA = pEATVIA;
	}

	public String getDireccion() {
		return direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	public String getDepartamento() {
		return departamento;
	}

	public void setDepartamento(String departamento) {
		this.departamento = departamento;
	}

	public String getMunicipio() {
		return municipio;
	}

	public void setMunicipio(String municipio) {
		this.municipio = municipio;
	}

	public String getActividadEconomica() {
		return actividadEconomica;
	}

	public void setActividadEconomica(String actividadEconomica) {
		this.actividadEconomica = actividadEconomica;
	}

	public String getTipoTelefono() {
		return tipoTelefono;
	}

	public void setTipoTelefono(String tipoTelefono) {
		this.tipoTelefono = tipoTelefono;
	}

	public String getIndicativo() {
		return indicativo;
	}

	public void setIndicativo(String indicativo) {
		this.indicativo = indicativo;
	}

	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public String getAplicativo() {
		return aplicativo;
	}

	public void setAplicativo(String aplicativo) {
		this.aplicativo = aplicativo;
	}

	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	public String getOficinaModificacion() {
		return oficinaModificacion;
	}

	public void setOficinaModificacion(String oficinaModificacion) {
		this.oficinaModificacion = oficinaModificacion;
	}

	public String getFecha() {
		return fecha;
	}

	public void setFecha(String fecha) {
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

	public String getPais() {
		return pais;
	}

	public void setPais(String pais) {
		this.pais = pais;
	}

	public Number getExtension() {
		return extension;
	}

	public void setExtension(Number extension) {
		this.extension = extension;
	}

	public String getTipoTelefono2() {
		return tipoTelefono2;
	}

	public void setTipoTelefono2(String tipoTelefono2) {
		this.tipoTelefono2 = tipoTelefono2;
	}

	public String getIndicativo2() {
		return indicativo2;
	}

	public void setIndicativo2(String indicativo2) {
		this.indicativo2 = indicativo2;
	}

	public String getTelefono2() {
		return telefono2;
	}

	public void setTelefono2(String telefono2) {
		this.telefono2 = telefono2;
	}

	public Number getExtension2() {
		return extension2;
	}

	public void setExtension2(Number extension2) {
		this.extension2 = extension2;
	}

	public String getMoneda() {
		return moneda;
	}

	public void setMoneda(String moneda) {
		this.moneda = moneda;
	}

	public String getIndicadorNotasContables() {
		return indicadorNotasContables;
	}

	public void setIndicadorNotasContables(String indicadorNotasContables) {
		this.indicadorNotasContables = indicadorNotasContables;
	}

	public String getEMail() {
		return EMail;
	}

	public void setEMail(String mail) {
		EMail = mail;
	}

	public String getRegimenTributario() {
		return regimenTributario;
	}

	public void setRegimenTributario(String regimenTributario) {
		this.regimenTributario = regimenTributario;
	}

	public String getSexo() {
		return sexo;
	}

	public void setSexo(String sexo) {
		this.sexo = sexo;
	}

	public Number getIngresoApp() {
		return ingresoApp;
	}

	public void setIngresoApp(Number ingresoApp) {
		this.ingresoApp = ingresoApp;
	}

	@Override
	public Object getPK() {
		return null;
	}

}
