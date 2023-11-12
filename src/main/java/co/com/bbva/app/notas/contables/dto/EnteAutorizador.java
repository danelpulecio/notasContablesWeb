package co.com.bbva.app.notas.contables.dto;

import co.com.bbva.app.notas.contables.anotacion.Columna;
import co.com.bbva.app.notas.contables.anotacion.ColumnaId;
import co.com.bbva.app.notas.contables.anotacion.Tabla;

@Tabla(nombreTabla = "NC_ENTE_AUTORIZADOR")
public class EnteAutorizador extends CommonVO<EnteAutorizador> implements java.io.Serializable {

	private static final long serialVersionUID = 9121985668307463842L;
	@ColumnaId
	@Columna(nombreDB = "CODIGO", nombreApp = "codigo")
	private Number codigo = 0;
	@Columna(nombreDB = "CODIGO_SUCURSAL", nombreApp = "codigoSucursal")
	private String codigoSucursal = "";
	@Columna(nombreDB = "CODIGO_USUARIO", nombreApp = "codigoUsuarioModulo")
	private Number codigoUsuarioModulo = 0;
	@Columna(nombreDB = "ESTADO", nombreApp = "estado", esEstado = true)
	private String estado = "A";

	private String nombreSucursal = "";
	private String codigoEmpleado = "";
	private String nombreUsuario = "";

	public EnteAutorizador() {
		codigo = 0;
		codigoSucursal = "";
		codigoUsuarioModulo = 0;
		estado = "A";
		nombreSucursal = "";
		nombreUsuario = "";
		codigoEmpleado = "";
	}

	public Number getCodigo() {
		return codigo;
	}

	public void setCodigo(Number codigo) {
		this.codigo = codigo;
	}

	public String getCodigoSucursal() {
		return codigoSucursal;
	}

	public void setCodigoSucursal(String codigoSucursal) {
		this.codigoSucursal = codigoSucursal;
	}

	public Number getCodigoUsuarioModulo() {
		return codigoUsuarioModulo;
	}

	public void setCodigoUsuarioModulo(Number codigoUsuarioModulo) {
		this.codigoUsuarioModulo = codigoUsuarioModulo;
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

	public String getNombreUsuario() {
		return nombreUsuario;
	}

	public void setNombreUsuario(String nombreUsuario) {
		this.nombreUsuario = nombreUsuario;
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
