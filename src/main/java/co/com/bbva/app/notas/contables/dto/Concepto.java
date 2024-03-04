package co.com.bbva.app.notas.contables.dto;

import co.com.bbva.app.notas.contables.anotacion.Columna;
import co.com.bbva.app.notas.contables.anotacion.ColumnaId;
import co.com.bbva.app.notas.contables.anotacion.Tabla;
import co.com.bbva.app.notas.contables.jsf.parametros.ConceptoPage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

@Tabla(nombreTabla = "NC_CONCEPTO")
public class Concepto extends CommonVO<Concepto> implements Comparable<Concepto> {

	private static final Logger LOGGER = LoggerFactory.getLogger(Concepto.class);

	private static final long serialVersionUID = -8970002437078802545L;

	@ColumnaId
	@Columna(nombreDB = "CODIGO", nombreApp = "codigo", esClave = true)
	private Number codigo = 0;
	@Columna(nombreDB = "NOMBRE", nombreApp = "nombre", esValor = true)
	private String nombre = "";
	@Columna(nombreDB = "CODIGO_UNIDAD_ANALISIS", nombreApp = "codigoUnidadAnalisis")
	private Number codigoUnidadAnalisis = 0;
	@Columna(nombreDB = "CODIGO_TEMA_AUTORIZACION", nombreApp = "codigoTemaAutorizacion")
	private Number codigoTemaAutorizacion = 0;
	@Columna(nombreDB = "CEN_AUT_SUC", nombreApp = "centrosAutorizadosSucursales")
	private String centrosAutSucursales = "N";
	@Columna(nombreDB = "CEN_AUT_ARE", nombreApp = "centrosAutorizadosAreasCentrales")
	private String centrosAutAreasCentrales = "N";
	@Columna(nombreDB = "CEN_AUT_CEN", nombreApp = "centrosAutorizadosCentroEspecial")
	private String centrosAutCentroEspecial = "N";
	@Columna(nombreDB = "VISTO_BUENO_ANALISIS", nombreApp = "vistoBuenoAnalisis")
	private String vistoBuenoAnalisis = "S";
	@Columna(nombreDB = "AUTORIZACION_TERCERO", nombreApp = "autorizacionTercero")
	private String autorizacionTercero = "S";
	@Columna(nombreDB = "SOPORTE", nombreApp = "soportes")
	private String soportes = "S";
	@Columna(nombreDB = "ORIGEN_IGUAL_DESTINO", nombreApp = "origenDestino")
	private String origenDestino = "S";
	@Columna(nombreDB = "ESTADO", nombreApp = "estado", esEstado = true)
	private String estado = "A";

	// estos campos son para soportar las chambonadas del desarrollador anterior
	private String codSucursal;
	private String nombreSucursal;

	private Boolean boolCentrosAutSucursales = false;
	private Boolean boolCentrosAutAreasCentrales = false;
	private Boolean boolCentrosAutCentroEspecial = false;
	private Boolean boolVistoBuenoAnalisis = true;
	private Boolean boolAutorizacionTercero = true;
	private Boolean boolSoportes = true;
	private Boolean boolOrigenDestino = true;

	private Boolean mostrarTemas = false;

	private List<Tema> temas = new ArrayList<Tema>();

	public Number getCodigo() {
		return codigo;
	}

	public void setCodigo(Number codigo) {
		this.codigo = codigo;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public Number getCodigoUnidadAnalisis() {

		LOGGER.info("CODIGO UNIDAD ANALISIS {}", codigoUnidadAnalisis);
		return codigoUnidadAnalisis;
	}

	public void setCodigoUnidadAnalisis(Number codigoUnidadAnalisis) {
		this.codigoUnidadAnalisis = codigoUnidadAnalisis;
	}

	public Number getCodigoTemaAutorizacion() {
		return codigoTemaAutorizacion;
	}

	public void setCodigoTemaAutorizacion(Number codigoTemaAutorizacion) {
		this.codigoTemaAutorizacion = codigoTemaAutorizacion;
	}

	public String getCentrosAutSucursales() {
		return centrosAutSucursales;
	}

	public void setCentrosAutSucursales(String centrosAutSucursales) {
		this.centrosAutSucursales = centrosAutSucursales;
	}

	public String getCentrosAutAreasCentrales() {
		return centrosAutAreasCentrales;
	}

	public void setCentrosAutAreasCentrales(String centrosAutAreasCentrales) {
		this.centrosAutAreasCentrales = centrosAutAreasCentrales;
	}

	public String getCentrosAutCentroEspecial() {
		return centrosAutCentroEspecial;
	}

	public void setCentrosAutCentroEspecial(String centrosAutCentroEspecial) {
		this.centrosAutCentroEspecial = centrosAutCentroEspecial;
	}

	public String getCodSucursal() {
		return codSucursal;
	}

	public void setCodSucursal(String codSucursal) {
		this.codSucursal = codSucursal;
	}

	public String getNombreSucursal() {
		return nombreSucursal;
	}

	public void setNombreSucursal(String nombreSucursal) {
		this.nombreSucursal = nombreSucursal;
	}

	public String getVistoBuenoAnalisis() {
		return vistoBuenoAnalisis;
	}

	public void setVistoBuenoAnalisis(String vistoBuenoAnalisis) {
		this.vistoBuenoAnalisis = vistoBuenoAnalisis;
	}

	public String getAutorizacionTercero() {
		return autorizacionTercero;
	}

	public void setAutorizacionTercero(String autorizacionTercero) {
		this.autorizacionTercero = autorizacionTercero;
	}

	public String getSoportes() {
		return soportes;
	}

	public void setSoportes(String soportes) {
		this.soportes = soportes;
	}

	public String getOrigenDestino() {
		return origenDestino;
	}

	public void setOrigenDestino(String origenDestino) {
		this.origenDestino = origenDestino;
	}

	@Override
	public String getEstado() {
		return estado;
	}

	@Override
	public void setEstado(String estado) {
		this.estado = estado;
	}

	public Boolean getBoolCentrosAutSucursales() {
		return boolCentrosAutSucursales;
	}

	public void setBoolCentrosAutSucursales(Boolean boolCentrosAutSucursales) {
		this.boolCentrosAutSucursales = boolCentrosAutSucursales;
	}

	public Boolean getBoolCentrosAutAreasCentrales() {
		return boolCentrosAutAreasCentrales;
	}

	public void setBoolCentrosAutAreasCentrales(Boolean boolCentrosAutAreasCentrales) {
		this.boolCentrosAutAreasCentrales = boolCentrosAutAreasCentrales;
	}

	public Boolean getBoolCentrosAutCentroEspecial() {
		return boolCentrosAutCentroEspecial;
	}

	public void setBoolCentrosAutCentroEspecial(Boolean boolCentrosAutCentroEspecial) {
		this.boolCentrosAutCentroEspecial = boolCentrosAutCentroEspecial;
	}

	public Boolean getBoolVistoBuenoAnalisis() {
		return boolVistoBuenoAnalisis;
	}

	public void setBoolVistoBuenoAnalisis(Boolean boolVistoBuenoAnalisis) {
		this.boolVistoBuenoAnalisis = boolVistoBuenoAnalisis;
	}

	public Boolean getBoolAutorizacionTercero() {
		return boolAutorizacionTercero;
	}

	public void setBoolAutorizacionTercero(Boolean boolAutorizacionTercero) {
		this.boolAutorizacionTercero = boolAutorizacionTercero;
	}

	public Boolean getBoolSoportes() {
		return boolSoportes;
	}

	public void setBoolSoportes(Boolean boolSoportes) {
		this.boolSoportes = boolSoportes;
	}

	public Boolean getBoolOrigenDestino() {
		return boolOrigenDestino;
	}

	public void setBoolOrigenDestino(Boolean boolOrigenDestino) {
		this.boolOrigenDestino = boolOrigenDestino;
	}

	public List<Tema> getTemas() {
		return temas;
	}

	public void setTemas(List<Tema> temas) {
		this.temas = temas;
	}

	public Boolean getMostrarTemas() {
		return mostrarTemas;
	}

	public void setMostrarTemas(Boolean mostrarTemas) {
		this.mostrarTemas = mostrarTemas;
	}

	@Override
	public int compareTo(Concepto o) {
		return getNombre().compareTo(o.getNombre());
	}

	@Override
	public Object getPK() {
		return codigo;
	}

	@Override
	public void restartPK(Object pk) {
		codigo = Integer.valueOf(pk.toString());
	}

	@Override
	public String toString() {
		return "Concepto{" +
				"codigo=" + codigo +
				", nombre='" + nombre + '\'' +
				", codigoUnidadAnalisis=" + codigoUnidadAnalisis +
				", codigoTemaAutorizacion=" + codigoTemaAutorizacion +
				", centrosAutSucursales='" + centrosAutSucursales + '\'' +
				", centrosAutAreasCentrales='" + centrosAutAreasCentrales + '\'' +
				", centrosAutCentroEspecial='" + centrosAutCentroEspecial + '\'' +
				", vistoBuenoAnalisis='" + vistoBuenoAnalisis + '\'' +
				", autorizacionTercero='" + autorizacionTercero + '\'' +
				", soportes='" + soportes + '\'' +
				", origenDestino='" + origenDestino + '\'' +
				", estado='" + estado + '\'' +
				", codSucursal='" + codSucursal + '\'' +
				", nombreSucursal='" + nombreSucursal + '\'' +
				", boolCentrosAutSucursales=" + boolCentrosAutSucursales +
				", boolCentrosAutAreasCentrales=" + boolCentrosAutAreasCentrales +
				", boolCentrosAutCentroEspecial=" + boolCentrosAutCentroEspecial +
				", boolVistoBuenoAnalisis=" + boolVistoBuenoAnalisis +
				", boolAutorizacionTercero=" + boolAutorizacionTercero +
				", boolSoportes=" + boolSoportes +
				", boolOrigenDestino=" + boolOrigenDestino +
				", mostrarTemas=" + mostrarTemas +
				", temas=" + temas +
				'}';
	}
}
