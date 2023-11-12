package co.com.bbva.app.notas.contables.carga.dto;

import co.com.bbva.app.notas.contables.anotacion.Columna;
import co.com.bbva.app.notas.contables.anotacion.Tabla;
import co.com.bbva.app.notas.contables.dto.CommonVO;

import java.sql.Timestamp;

@Tabla(nombreTabla = "NC_RIES_OPER_PROC")
public class RiesgoOperacionalProceso extends CommonVO<RiesgoOperacionalProceso> implements java.io.Serializable {

	private static final long serialVersionUID = -7001764654854024967L;

	@Columna(nombreDB = "CODIGO", nombreApp = "codigo", esClave = true)
	private String codigo = "";

	@Columna(nombreDB = "nombre", nombreApp = "nombre", esValor = true)
	private String nombre = "";

	private String estadoCarga = "";

	private Timestamp fechaUltimaCarga = null;
	
	private String codigoProcesoAris = null;

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
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

	/**
	 * @return the codigoProcesoAris
	 */
	public String getCodigoProcesoAris() {
		return codigoProcesoAris;
	}

	/**
	 * @param codigoProcesoAris the codigoProcesoAris to set
	 */
	public void setCodigoProcesoAris(String codigoProcesoAris) {
		this.codigoProcesoAris = codigoProcesoAris;
	}

	@Override
	public Object getPK() {
		return codigo;
	}

}
