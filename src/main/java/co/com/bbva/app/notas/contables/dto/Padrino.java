package co.com.bbva.app.notas.contables.dto;

import co.com.bbva.app.notas.contables.anotacion.Columna;
import co.com.bbva.app.notas.contables.anotacion.ColumnaId;
import co.com.bbva.app.notas.contables.anotacion.Tabla;

@Tabla(nombreTabla = "NC_PADRINO")
public class Padrino extends CommonVO<Padrino> implements java.io.Serializable {

	private static final long serialVersionUID = -5959583390658738672L;
	@ColumnaId
	@Columna(nombreDB = "CODIGO", nombreApp = "codigo")
	private Number codigo = 0;
	@Columna(nombreDB = "CODIGO_USUARIO", nombreApp = "codigoUsuario")
	private Number codigoUsuario = 0;
	@Columna(nombreDB = "CODIGO_UNIDAD_ANALISIS", nombreApp = "codigoUnidadAnalisis")
	private Number codigoUnidadAnalisis = 0;
	@Columna(nombreDB = "ESTADO", nombreApp = "estado", esEstado = true)
	private String estado = "A";

	private String codigoSucursal;
	private String nombreSucursal;
	private String nombreEmpleado;
	private String codigoEmpleado;

	public Number getCodigo() {
		return codigo;
	}

	public void setCodigo(Number codigo) {
		this.codigo = codigo;
	}

	public Number getCodigoUsuario() {
		return codigoUsuario;
	}

	public void setCodigoUsuario(Number codigoUsuario) {
		this.codigoUsuario = codigoUsuario;
	}

	public Number getCodigoUnidadAnalisis() {
		return codigoUnidadAnalisis;
	}

	public void setCodigoUnidadAnalisis(Number codigoUnidadAnalisis) {
		this.codigoUnidadAnalisis = codigoUnidadAnalisis;
	}

	@Override
	public String getEstado() {
		return estado;
	}

	@Override
	public void setEstado(String estado) {
		this.estado = estado;
	}

	public String getNombreSucursal() {
		return nombreSucursal;
	}

	public void setNombreSucursal(String nombreSucursal) {
		this.nombreSucursal = nombreSucursal;
	}

	public String getNombreEmpleado() {
		return nombreEmpleado;
	}

	public void setNombreEmpleado(String nombreEmpleado) {
		this.nombreEmpleado = nombreEmpleado;
	}

	public String getCodigoSucursal() {
		return codigoSucursal;
	}

	public void setCodigoSucursal(String codigoSucursal) {
		this.codigoSucursal = codigoSucursal;
	}

	public String getCodigoEmpleado() {
		return codigoEmpleado;
	}

	public void setCodigoEmpleado(String codigoEmpleado) {
		this.codigoEmpleado = codigoEmpleado;
	}

	@Override
	public Object getPK() {
		return codigo;
	}

	@Override
	public void restartPK(Object pk) {
		codigo = Integer.valueOf(pk.toString());
	}
}
