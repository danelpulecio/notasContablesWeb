package co.com.bbva.app.notas.contables.dto;

import oracle.sql.TIMESTAMP;

import java.sql.Timestamp;
import java.util.Date;

public class Auditoria extends CommonVO<Auditoria> implements java.io.Serializable {

	private static final long serialVersionUID = -8290857215435614456L;
	private Number codigo = 0;
	private TIMESTAMP fechaHora = null;
	private Number codigoUsuario = 0;
	private String operacion = "";
	private String tipoRegistro = "";
	private String codigoRegistro = "";

	private String nombreUsuario;

	private AuditoriaDetalle detalle;
	private UsuarioModulo usuario;

	public Number getCodigo() {
		return codigo;
	}

	public void setCodigo(Number codigo) {
		this.codigo = codigo;
	}

	public TIMESTAMP getFechaHora() {
		return fechaHora;
	}

	public void setFechaHora(TIMESTAMP fechaHora) {
		this.fechaHora = fechaHora;
	}

	public Number getCodigoUsuario() {
		return codigoUsuario;
	}

	public void setCodigoUsuario(Number codigoUsuario) {
		this.codigoUsuario = codigoUsuario;
	}

	public String getOperacion() {
		return operacion;
	}

	public void setOperacion(String operacion) {
		this.operacion = operacion;
	}

	public String getTipoRegistro() {
		return tipoRegistro;
	}

	public void setTipoRegistro(String tipoRegistro) {
		this.tipoRegistro = tipoRegistro;
	}

	public String getCodigoRegistro() {
		return codigoRegistro;
	}

	public void setCodigoRegistro(String codigoRegistro) {
		this.codigoRegistro = codigoRegistro;
	}

	public String getNombreUsuario() {
		return nombreUsuario;
	}

	public void setNombreUsuario(String nombreUsuario) {
		this.nombreUsuario = nombreUsuario;
	}

	public AuditoriaDetalle getDetalle() {
		return detalle;
	}

	public void setDetalle(AuditoriaDetalle detalle) {
		this.detalle = detalle;
	}

	public UsuarioModulo getUsuario() {
		return usuario;
	}

	public void setUsuario(UsuarioModulo usuario) {
		this.usuario = usuario;
	}

	public Timestamp getFechaHoraTs() {
		try {
			return fechaHora.timestampValue();
		} catch (Exception e) {
			return new Timestamp(new Date().getTime());
		}
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
