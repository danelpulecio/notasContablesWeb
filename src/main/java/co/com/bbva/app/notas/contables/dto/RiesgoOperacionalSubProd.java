
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
@Tabla(nombreTabla = "NC_RIES_OPER_SUBPROD")
public class RiesgoOperacionalSubProd extends CommonVO<RiesgoOperacionalSubProd> implements java.io.Serializable {

	@ColumnaId
	@Columna(nombreDB = "CODIGO", nombreApp = "codigo", esClave = true)
	private String codigo;
	
	@Columna(nombreDB = "NOMBRE", nombreApp = "nombre", esValor = true)
	private String nombre;
	
	@Columna(nombreDB = "CODIGO_PRODUCTO", nombreApp = "codigoProducto")
	private String codigoProducto;
	
	@Columna(nombreDB = "FECHA_ESTADO_CARGA", nombreApp = "fechaEstadoCarga")
	private Timestamp fechaEstadoCarga;
	
	private static final long serialVersionUID = 1L;

	
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
	 * @return the codigoProducto
	 */
	public String getCodigoProducto() {
		return codigoProducto;
	}


	/**
	 * @param codigoProducto the codigoProducto to set
	 */
	public void setCodigoProducto(String codigoProducto) {
		this.codigoProducto = codigoProducto;
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
