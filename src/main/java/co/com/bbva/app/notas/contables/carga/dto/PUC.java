package co.com.bbva.app.notas.contables.carga.dto;

import co.com.bbva.app.notas.contables.anotacion.Columna;
import co.com.bbva.app.notas.contables.anotacion.Tabla;
import co.com.bbva.app.notas.contables.dto.CommonVO;

import java.sql.Timestamp;

@Tabla(nombreTabla = "NC_PUC")
public class PUC extends CommonVO<PUC> implements java.io.Serializable {

	private static final long serialVersionUID = 6338584974318830827L;
	@Columna(nombreDB = "NUMERO_CUENTA", nombreApp = "numeroCuenta", esClave = true)
	private String numeroCuenta = "";
	private String digitoVerificacion = "";
	@Columna(nombreDB = "DESCRIPCION", nombreApp = "descripcion", esValor = true)
	private String descripcion = "";
	private String estadoCuenta = "";
	private String centroResponsable = "";
	private String indicadorCuenta = "";
	private String naturaleza = "";
	private String tipoCuenta = "";
	private String formaActualizacion = "";
	private String contrapartidaDebe = "";
	private String contrapartidaHaber = "";
	private String tipoApunte = "";
	private String tipoCentroOrigenAutorizado = "";
	private String indicadorCentroOrigen = "";
	private String centrosOrigenAutorizados = "";
	private String tipoCentroDestinoAutorizado = "";
	private String indicadorCentroDestino = "";
	private String centrosDestinoAutorizados = "";
	private String tipoMoneda = "";
	private String indicadorSIRO = "";
	private String indicadorTercero = "";
	private String estadoCarga = "";
	private Timestamp fechaUltimaCarga = null;

	// indicador de seleccion en capa de vista
	private Boolean selected = false;

	public String getNumeroCuenta() {
		return numeroCuenta;
	}

	public void setNumeroCuenta(String numeroCuenta) {
		this.numeroCuenta = numeroCuenta;
	}

	public String getDigitoVerificacion() {
		return digitoVerificacion;
	}

	public void setDigitoVerificacion(String digitoVerificacion) {
		this.digitoVerificacion = digitoVerificacion;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getEstadoCuenta() {
		return estadoCuenta;
	}

	public void setEstadoCuenta(String estadoCuenta) {
		this.estadoCuenta = estadoCuenta;
	}

	public String getCentroResponsable() {
		return centroResponsable;
	}

	public void setCentroResponsable(String centroResponsable) {
		this.centroResponsable = centroResponsable;
	}

	public String getIndicadorCuenta() {
		return indicadorCuenta;
	}

	public void setIndicadorCuenta(String indicadorCuenta) {
		this.indicadorCuenta = indicadorCuenta;
	}

	public String getNaturaleza() {
		return naturaleza;
	}

	public void setNaturaleza(String naturaleza) {
		this.naturaleza = naturaleza;
	}

	public String getTipoCuenta() {
		return tipoCuenta;
	}

	public void setTipoCuenta(String tipoCuenta) {
		this.tipoCuenta = tipoCuenta;
	}

	public String getFormaActualizacion() {
		return formaActualizacion;
	}

	public void setFormaActualizacion(String formaActualizacion) {
		this.formaActualizacion = formaActualizacion;
	}

	public String getContrapartidaDebe() {
		return contrapartidaDebe;
	}

	public void setContrapartidaDebe(String contrapartidaDebe) {
		this.contrapartidaDebe = contrapartidaDebe;
	}

	public String getContrapartidaHaber() {
		return contrapartidaHaber;
	}

	public void setContrapartidaHaber(String contrapartidaHaber) {
		this.contrapartidaHaber = contrapartidaHaber;
	}

	public String getTipoApunte() {
		return tipoApunte;
	}

	public void setTipoApunte(String tipoApunte) {
		this.tipoApunte = tipoApunte;
	}

	public String getTipoCentroOrigenAutorizado() {
		return tipoCentroOrigenAutorizado;
	}

	public void setTipoCentroOrigenAutorizado(String tipoCentroOrigenAutorizado) {
		this.tipoCentroOrigenAutorizado = tipoCentroOrigenAutorizado;
	}

	public String getIndicadorCentroOrigen() {
		return indicadorCentroOrigen;
	}

	public void setIndicadorCentroOrigen(String indicadorCentroOrigen) {
		this.indicadorCentroOrigen = indicadorCentroOrigen;
	}

	public String getCentrosOrigenAutorizados() {
		return centrosOrigenAutorizados;
	}

	public void setCentrosOrigenAutorizados(String centrosOrigenAutorizados) {
		this.centrosOrigenAutorizados = centrosOrigenAutorizados;
	}

	public String getTipoCentroDestinoAutorizado() {
		return tipoCentroDestinoAutorizado;
	}

	public void setTipoCentroDestinoAutorizado(String tipoCentroDestinoAutorizado) {
		this.tipoCentroDestinoAutorizado = tipoCentroDestinoAutorizado;
	}

	public String getIndicadorCentroDestino() {
		return indicadorCentroDestino;
	}

	public void setIndicadorCentroDestino(String indicadorCentroDestino) {
		this.indicadorCentroDestino = indicadorCentroDestino;
	}

	public String getCentrosDestinoAutorizados() {
		return centrosDestinoAutorizados;
	}

	public void setCentrosDestinoAutorizados(String centrosDestinoAutorizados) {
		this.centrosDestinoAutorizados = centrosDestinoAutorizados;
	}

	public String getTipoMoneda() {
		return tipoMoneda;
	}

	public void setTipoMoneda(String tipoMoneda) {
		this.tipoMoneda = tipoMoneda;
	}

	public String getIndicadorSIRO() {
		return indicadorSIRO;
	}

	public void setIndicadorSIRO(String indicadorSIRO) {
		this.indicadorSIRO = indicadorSIRO;
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
		return numeroCuenta;
	}

	public Boolean getSelected() {
		return selected;
	}

	public void setSelected(Boolean selected) {
		this.selected = selected;
	}

	public String getIndicadorTercero() {
		return indicadorTercero;
	}

	public void setIndicadorTercero(String indicadorTercero) {
		this.indicadorTercero = indicadorTercero;
	}

}
