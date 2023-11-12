package co.com.bbva.app.notas.contables.dto;

import co.com.bbva.app.notas.contables.anotacion.Columna;
import co.com.bbva.app.notas.contables.anotacion.ColumnaId;
import co.com.bbva.app.notas.contables.anotacion.Tabla;

@Tabla(nombreTabla = "NC_MONTO_AUT_GENERAL")
public class MontoAutorizadoGeneral extends CommonVO<MontoAutorizadoGeneral> implements java.io.Serializable {

	private static final long serialVersionUID = -4966407204176936599L;
	@ColumnaId
	@Columna(nombreDB = "CODIGO", nombreApp = "codigo")
	private Number codigo = 0;
	@Columna(nombreDB = "CODIGO_TIPO_EVENTO", nombreApp = "codigoTipoAutorizacion")
	private Number codigoTipoAutorizacion = 0;
	@Columna(nombreDB = "CODIGO_ROL", nombreApp = "codigoRol")
	private Number codigoRol = 0;
	@Columna(nombreDB = "CODIGO_TEMA_AUTORIZACION", nombreApp = "codigoTemaAutorizacion")
	private Number codigoTemaAutorizacion = 0;
	@Columna(nombreDB = "MONTO", nombreApp = "monto")
	private Number monto = 0;
	@Columna(nombreDB = "ESTADO", nombreApp = "estado", esEstado = true)
	private String estado = "A";

	private String nombreTipoEvento = "";
	private String nombreRol = "";
	private String nombreTeamAut = "";

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

	public Number getCodigoRol() {
		return codigoRol;
	}

	public void setCodigoRol(Number codigoRol) {
		this.codigoRol = codigoRol;
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

	public String getNombreRol() {
		return nombreRol;
	}

	public void setNombreRol(String nombreRol) {
		this.nombreRol = nombreRol;
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
