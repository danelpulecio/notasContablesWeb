package co.com.bbva.app.notas.contables.dto;

import co.com.bbva.app.notas.contables.anotacion.Columna;
import co.com.bbva.app.notas.contables.anotacion.ColumnaId;
import co.com.bbva.app.notas.contables.anotacion.Tabla;

import java.sql.Timestamp;

@Tabla(nombreTabla = "NC_OPER_N3_SFC")
public class JerarquiaOperacionSFC_N3 extends CommonVO<JerarquiaOperacionSFC_N3> implements java.io.Serializable {

	private static final long serialVersionUID = 3404991765929116451L;
	@ColumnaId
	@Columna(nombreDB = "CONSECUTIVO", nombreApp = "consecutivo", esClave = true)
	private String consecutivo;

	@Columna(nombreDB = "NOMBRE", nombreApp = "nombre", esValor = true)
	private String nombre;
	
	@Columna(nombreDB = "CODIGO", nombreApp = "codigo")
	private String codigo;

	@Columna(nombreDB = "CODIGO_N1_SFC", nombreApp = "codigoNcN1ClaseRiesgo")
	private String codigoNcN1ClaseRiesgo;

	@Columna(nombreDB = "CODIGO_N2_SFC", nombreApp = "codigoNcClaseRiesgo")
	private String codigoNcClaseRiesgo;
	
	@Columna(nombreDB = "ESTADO_CARGA", nombreApp = "estadoCarga")
	private String estadoCarga;

	@Columna(nombreDB = "FECHA_ESTADO_CARGA", nombreApp = "fechaEstadoCarga")
	private Timestamp fechaEstadoCarga;


	/**
	 * @return the consecutivo
	 */
	public String getConsecutivo() {
		return consecutivo;
	}

	/**
	 * @param consecutivo the consecutivo to set
	 */
	public void setConsecutivo(String consecutivo) {
		this.consecutivo = consecutivo;
	}

	/**
	 * @return the codigo
	 */
	public String getCodigo() {
		return codigo;
	}

	/**
	 * @param codigo the codigo to set
	 */
	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	/**
	 * @return the nombre
	 */
	public String getNombre() {
		return nombre;
	}

	/**
	 * @param nombre the nombre to set
	 */
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	/**
	 * @return the codigoNcClaseRiesgo
	 */
	public String getCodigoNcClaseRiesgo() {
		return codigoNcClaseRiesgo;
	}

	/**
	 * @param codigoNcClaseRiesgo the codigoNcClaseRiesgo to set
	 */
	public void setCodigoNcClaseRiesgo(String codigoNcClaseRiesgo) {
		this.codigoNcClaseRiesgo = codigoNcClaseRiesgo;
	}


	/**
	 * @return the codigoNcN1ClaseRiesgo
	 */
	public String getCodigoNcN1ClaseRiesgo() {
		return codigoNcN1ClaseRiesgo;
	}

	/**
	 * @param codigoNcN1ClaseRiesgo the codigoNcN1ClaseRiesgo to set
	 */
	public void setCodigoNcN1ClaseRiesgo(String codigoNcN1ClaseRiesgo) {
		this.codigoNcN1ClaseRiesgo = codigoNcN1ClaseRiesgo;
	}

	/**
	 * @return the fechaEstadoCarga
	 */
	public Timestamp getFechaEstadoCarga() {
		return fechaEstadoCarga;
	}

	/**
	 * @param fechaEstadoCarga the fechaEstadoCarga to set
	 */
	public void setFechaEstadoCarga(Timestamp fechaEstadoCarga) {
		this.fechaEstadoCarga = fechaEstadoCarga;
	}

	
	/**
	 * @return the estadoCarga
	 */
	public String getEstadoCarga() {
		return estadoCarga;
	}

	/**
	 * @param estadoCarga the estadoCarga to set
	 */
	public void setEstadoCarga(String estadoCarga) {
		this.estadoCarga = estadoCarga;
	}

	@Override
	public Object getPK() {
		return consecutivo;
	}

}
