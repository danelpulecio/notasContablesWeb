package co.com.bbva.app.notas.contables.dto;

import co.com.bbva.app.notas.contables.carga.dto.Sucursal;
import oracle.sql.TIMESTAMP;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Instancia extends CommonVO<Instancia> implements java.io.Serializable {

	private static final long serialVersionUID = 9047363510651851969L;

	enum ESTADOS {
		PEND_AP_SUB_GER(0), PEND_REV(1), PEND_AP_PAD(2), PEND_AP_ENT_AUT(3), PRE_CIERRE(4), CIERRE(5), ANULADA(9);
		private final Integer state;

		private ESTADOS(Integer state) {
			this.state = state;
		}

		public Integer getState() {
			return state;
		}

		public String getStateName() {
			return getStateName(state);
		}

		public static String getStateName(String state) {
			return getStateName(Integer.valueOf(state.trim()));
		}

		public static String getStateName(Integer state) {
			switch (state) {
			case 0:
				return "Pendiente de aprobación por Subgerente o Responsable Área Central";
			case 1:
				return "Pendiente de revisión";
			case 2:
				return "Pendiente de aprobación por Padrino - Unidad de Análisis";
			case 3:
				return "Pendiente de aprobación ente Autorizador";
			case 4:
				return "Precierre";
			case 5:
				return "Cierre";
			case 6:
				return "En firme";
			case 9:
				return "Anulada";
			}
			return "";
		}
	}

	private Number codigo = 0;
	private TIMESTAMP fechaHoraInicio = null;
	private Number codigoNotaContable = 0;
	private String codigoSucursalOrigen = "";
	private Number codigoUsuarioActual = 0;
	private TIMESTAMP ultimaActualizacion = null;
	private String estado = "A";

	private NotaContable nc = new NotaContable();
	private Sucursal sucursal = new Sucursal();
	private UsuarioModulo usuMod = new UsuarioModulo();
	private Concepto concepto = new Concepto();
	private Rol rol = new Rol();
	private List<NotaContableTotal> totales = new ArrayList<NotaContableTotal>();

	public TIMESTAMP getFechaHoraInicio() {
		return fechaHoraInicio;
	}

	public void setFechaHoraInicio(TIMESTAMP fechaHoraInicio) {
		this.fechaHoraInicio = fechaHoraInicio;
	}

	public String getCodigoSucursalOrigen() {
		return codigoSucursalOrigen;
	}

	public void setCodigoSucursalOrigen(String codigoSucursalOrigen) {
		this.codigoSucursalOrigen = codigoSucursalOrigen;
	}

	public TIMESTAMP getUltimaActualizacion() {
		return ultimaActualizacion;
	}

	public void setUltimaActualizacion(TIMESTAMP ultimaActualizacion) {
		this.ultimaActualizacion = ultimaActualizacion;
	}

	public NotaContable getNC() {
		return nc;
	}

	public void setNC(NotaContable notaContable) {
		this.nc = notaContable;
	}

	public Sucursal getSucursal() {
		return sucursal;
	}

	public void setSucursal(Sucursal sucursal) {
		this.sucursal = sucursal;
	}

	public UsuarioModulo getUsuMod() {
		return usuMod;
	}

	public void setUsuMod(UsuarioModulo usuMod) {
		this.usuMod = usuMod;
	}

	public Concepto getConcepto() {
		return concepto;
	}

	public void setConcepto(Concepto concepto) {
		this.concepto = concepto;
	}

	public Number getCodigo() {
		return codigo;
	}

	public void setCodigo(Number codigo) {
		this.codigo = codigo;
	}

	public Number getCodigoNotaContable() {
		return codigoNotaContable;
	}

	public void setCodigoNotaContable(Number codigoNotaContable) {
		this.codigoNotaContable = codigoNotaContable;
	}

	public Number getCodigoUsuarioActual() {
		return codigoUsuarioActual;
	}

	public void setCodigoUsuarioActual(Number codigoUsuarioActual) {
		this.codigoUsuarioActual = codigoUsuarioActual;
	}

	public Rol getRol() {
		return rol;
	}

	public void setRol(Rol rol) {
		this.rol = rol;
	}

	@Override
	public String getEstado() {
		return estado;
	}

	@Override
	public void setEstado(String estado) {
		this.estado = estado;
	}

	public String getNombreEstado() {
		return ESTADOS.getStateName(estado);
	}

	public Timestamp getFechaHoraInicioTs() {
		try {

			return fechaHoraInicio.timestampValue();
		} catch (Exception e) {
			return new Timestamp(new Date().getTime());
		}
	}

	public Timestamp getUltimaActualizacionTs() {
		try {
			return ultimaActualizacion.timestampValue();
		} catch (Exception e) {
			return new Timestamp(new Date().getTime());
		}
	}

	public void setUltimaActualizacionTs(Timestamp time) {
		setUltimaActualizacion(new TIMESTAMP(time));
	}

	public void setFechaHoraInicioTs(Timestamp time) {
		setFechaHoraInicio(new TIMESTAMP(time));
	}

	public List<NotaContableTotal> getTotales() {
		return totales;
	}

	public void setTotales(List<NotaContableTotal> totales) {
		this.totales = totales;
	}

	@Override
	public Object getPK() {
		return codigo;
	}

	@Override
	public void restartPK(Object pk) {
		codigo = Integer.valueOf(pk.toString());
	}


	@Override
	public String toString() {
		return "Instancia{" +
				"codigo=" + codigo +
				", fechaHoraInicio=" + fechaHoraInicio +
				", codigoNotaContable=" + codigoNotaContable +
				", codigoSucursalOrigen='" + codigoSucursalOrigen + '\'' +
				", codigoUsuarioActual=" + codigoUsuarioActual +
				", ultimaActualizacion=" + ultimaActualizacion +
				", estado='" + estado + '\'' +
				", nc=" + nc +
				", sucursal=" + sucursal +
				", usuMod=" + usuMod +
				", concepto=" + concepto +
				", rol=" + rol +
				", totales=" + totales +
				'}';
	}
}
