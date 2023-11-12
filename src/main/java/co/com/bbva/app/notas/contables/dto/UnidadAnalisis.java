package co.com.bbva.app.notas.contables.dto;

import co.com.bbva.app.notas.contables.anotacion.Columna;
import co.com.bbva.app.notas.contables.anotacion.ColumnaId;
import co.com.bbva.app.notas.contables.anotacion.Tabla;

@Tabla(nombreTabla = "NC_UNIDAD_ANALISIS")
public class UnidadAnalisis extends CommonVO<UnidadAnalisis> implements java.io.Serializable {

	private static final long serialVersionUID = -8821476109402189436L;
	@ColumnaId
	@Columna(nombreDB = "CODIGO", nombreApp = "codigo")
	private Number codigo = 0;
	@Columna(nombreDB = "CODIGO_SUCURSAL", nombreApp = "codigoSucursal")
	private String codigoSucursal = "";
	@Columna(nombreDB = "AUTORIZA_REF_CRUCE", nombreApp = "autorizaReferenciaCruce")
	private String autorizaReferenciaCruce = "N";
	@Columna(nombreDB = "ESTADO", nombreApp = "estado", esEstado = true)
	private String estado = "A";

	private String nombreSucursal;

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

	public String getAutorizaReferenciaCruce() {
		return autorizaReferenciaCruce;
	}

	public void setAutorizaReferenciaCruce(String autorizaReferenciaCruce) {
		this.autorizaReferenciaCruce = autorizaReferenciaCruce;
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

	@Override
	public Object getPK() {
		return codigo;
	}

	@Override
	public void restartPK(Object pk) {
		codigo = Integer.valueOf(pk.toString());
	}
}
