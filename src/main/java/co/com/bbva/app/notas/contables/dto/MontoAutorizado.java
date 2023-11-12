package co.com.bbva.app.notas.contables.dto;

import co.com.bbva.app.notas.contables.anotacion.Columna;
import co.com.bbva.app.notas.contables.anotacion.ColumnaId;
import co.com.bbva.app.notas.contables.anotacion.Tabla;

@Tabla(nombreTabla = "NC_MONTO_AUTORIZADO")
public class MontoAutorizado extends CommonVO<MontoAutorizado> implements java.io.Serializable {

	private static final long serialVersionUID = 4074840711140878603L;
	@ColumnaId
	@Columna(nombreDB = "CODIGO", nombreApp = "codigo")
	private Number codigo = 0;
	@Columna(nombreDB = "CODIGO_TIPO_EVENTO", nombreApp = "codigoTipoAutorizacion")
	private Number codigoTipoAutorizacion = 0;
	@Columna(nombreDB = "CODIGO_ENTE_AUTORIZADOR", nombreApp = "codigoEnteAutorizador")
	private Number codigoEnteAutorizador = 0;
	@Columna(nombreDB = "CODIGO_TEMA_AUTORIZACION", nombreApp = "codigoTemaAutorizacion")
	private Number codigoTemaAutorizacion = 0;
	@Columna(nombreDB = "MONTO", nombreApp = "monto")
	private Number monto = 0;
	@Columna(nombreDB = "ESTADO", nombreApp = "estado", esEstado = true)
	private String estado = "A";

	private String nombreTipoEvento = "";
	private String codigoSucursal = "";
	private String nombreSucursal = "";
	private String codigoEmpleado = "";
	private String nombreTeamAut = "";

	public MontoAutorizado() {
		codigo = 0;
		codigoTipoAutorizacion = 0;
		codigoEnteAutorizador = 0;
		codigoTemaAutorizacion = 0;
		monto = 0;
		estado = "A";
		nombreTipoEvento = "";
		codigoSucursal = "";
		nombreSucursal = "";
		codigoEmpleado = "";
		nombreTeamAut = "";
	}

	public Number getCodigo() {
		return codigo;
	}

	public void setCodigo(Number codigo) {
		this.codigo = codigo;
	}

	public Number getCodigoTipoAutorizacion() {
		return codigoTipoAutorizacion;
	}

	public void setCodigoTipoAutorizacion(Number codigoTipoAutorizacion) {
		this.codigoTipoAutorizacion = codigoTipoAutorizacion;
	}

	public Number getCodigoEnteAutorizador() {
		return codigoEnteAutorizador;
	}

	public void setCodigoEnteAutorizador(Number codigoEnteAutorizador) {
		this.codigoEnteAutorizador = codigoEnteAutorizador;
	}

	public Number getCodigoTemaAutorizacion() {
		return codigoTemaAutorizacion;
	}

	public void setCodigoTemaAutorizacion(Number codigoTemaAutorizacion) {
		this.codigoTemaAutorizacion = codigoTemaAutorizacion;
	}

	public Number getMonto() {
		return monto;
	}

	public void setMonto(Number monto) {
		this.monto = monto;
	}

	@Override
	public String getEstado() {
		return estado;
	}

	@Override
	public void setEstado(String estado) {
		this.estado = estado;
	}

	public String getNombreTipoEvento() {
		return nombreTipoEvento;
	}

	public void setNombreTipoEvento(String nombreTipoEvento) {
		this.nombreTipoEvento = nombreTipoEvento;
	}

	public String getCodigoSucursal() {
		return codigoSucursal;
	}

	public void setCodigoSucursal(String codigoSucursal) {
		this.codigoSucursal = codigoSucursal;
	}

	public String getNombreSucursal() {
		return nombreSucursal;
	}

	public void setNombreSucursal(String nombreSucursal) {
		this.nombreSucursal = nombreSucursal;
	}

	public String getCodigoEmpleado() {
		return codigoEmpleado;
	}

	public void setCodigoEmpleado(String codigoEmpleado) {
		this.codigoEmpleado = codigoEmpleado;
	}

	public String getNombreTeamAut() {
		return nombreTeamAut;
	}

	public void setNombreTeamAut(String nombreTeamAut) {
		this.nombreTeamAut = nombreTeamAut;
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
