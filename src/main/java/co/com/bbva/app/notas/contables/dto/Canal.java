package co.com.bbva.app.notas.contables.dto;

import co.com.bbva.app.notas.contables.anotacion.Columna;
import co.com.bbva.app.notas.contables.anotacion.ColumnaId;
import co.com.bbva.app.notas.contables.anotacion.Tabla;

import java.sql.Timestamp;

/**
 *
 * @author pjimenez
 * @version 1.0, 8/09/2021
 * @since JDK1.8
 *
 */
@Tabla(nombreTabla = "NC_CANAL")
public class Canal extends CommonVO<Canal> implements java.io.Serializable {


	private static final long serialVersionUID = 1L;
	@ColumnaId
	@Columna(nombreDB = "CODIGO", nombreApp = "codigo", esClave = true)
	private String codigo;
	
	@Columna(nombreDB = "DESCRIPCION", nombreApp = "descripcion", esValor = true)
	private String descripcion;
	
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
