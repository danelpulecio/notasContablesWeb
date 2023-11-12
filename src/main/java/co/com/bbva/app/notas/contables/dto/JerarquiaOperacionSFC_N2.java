package co.com.bbva.app.notas.contables.dto;

import co.com.bbva.app.notas.contables.anotacion.Columna;
import co.com.bbva.app.notas.contables.anotacion.ColumnaId;
import co.com.bbva.app.notas.contables.anotacion.Tabla;

import java.sql.Timestamp;

@Tabla(nombreTabla = "NC_OPER_N2_SFC")
public class JerarquiaOperacionSFC_N2 extends CommonVO<JerarquiaOperacionSFC_N2> implements java.io.Serializable {

	private static final long serialVersionUID = 3404991765929116451L;
	@ColumnaId
	@Columna(nombreDB = "CONSECUTIVO", nombreApp = "consecutivo", esClave = true)
	private String consecutivo;
	
	@Columna(nombreDB = "NOMBRE", nombreApp = "nombre", esValor = true)
	private String nombre;
	
	@Columna(nombreDB = "CODIGO", nombreApp = "codigo")
	private String codigo;
	
	@Columna(nombreDB = "CODIGO_NC_CLASE_RIESGO", nombreApp = "codigoNcClaseRiesgo")
	private String codigoNcClaseRiesgo;

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

	
	@Override
	public Object getPK() {
		return consecutivo;
	}

}
