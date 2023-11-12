/**
 * 
 */
package co.com.bbva.app.notas.contables.dto;

import co.com.bbva.app.notas.contables.anotacion.Columna;
import co.com.bbva.app.notas.contables.anotacion.ColumnaId;
import co.com.bbva.app.notas.contables.anotacion.Tabla;

import java.sql.Timestamp;

/**
 *
 * @author pjimenez
* @version 1.0, 9/09/2021
* @since JDK1.8
 *
 */
@Tabla(nombreTabla = "NC_SUBCANALES")
public class SubCanal extends CommonVO<SubCanal> implements java.io.Serializable {


	private static final long serialVersionUID = 1L;
	@ColumnaId
	@Columna(nombreDB = "CODIGO", nombreApp = "codigo", esClave = true)
	private String codigo;
	
	@Columna(nombreDB = "DESCRIPCION", nombreApp = "descripcion", esValor = true)
	private String descripcion;
	
	@Columna(nombreDB = "CODIGO_CANAL", nombreApp = "codigoCanal")
	private String codigoCanal;
	
	@Columna(nombreDB = "FECHA_ESTADO_CARGA", nombreApp = "fechaEstadoCarga")
	private Timestamp fechaEstadoCarga;
	
	
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
	 * @return the descripcion
	 */
	public String getDescripcion() {
		return descripcion;
	}

	/**
	 * @param descripcion the descripcion to set
	 */
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	
	

	/**
	 * @return the codigoCanal
	 */
	public String getCodigoCanal() {
		return codigoCanal;
	}

	/**
	 * @param codigoCanal the codigoCanal to set
	 */
	public void setCodigoCanal(String codigoCanal) {
		this.codigoCanal = codigoCanal;
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
		return codigo;
	}
}
