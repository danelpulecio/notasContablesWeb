package co.com.bbva.app.notas.contables.dto;

import oracle.sql.TIMESTAMP;

import java.sql.Timestamp;
import java.util.Date;

public class ActividadRealizada extends CommonVO<ActividadRealizada> {

	private static final long serialVersionUID = 1231333378808984858L;

	private Number codigo = 0;
	private TIMESTAMP fechaHora = null;
	private TIMESTAMP fechaHoraCierre = null;
	private Number codigoInstancia = 0;
	private String estado = "";
	private Number codigoUsuario = 0;
	private String valor1 = "";
	private String valor2 = "";
	private String valor3 = "";
	private String valor4 = "";
	private String valor5 = "";

	private Number duracionActividad = 0;

	private Double horasEtapa = 0D;
	private UsuarioModulo usuMod = new UsuarioModulo();
	private Rol rol = new Rol();
	private CausalDevolucion cauDev = new CausalDevolucion();

	private String numeroRadicacionNC = "";

	public TIMESTAMP getFechaHora() {
		return fechaHora;
	}

	public void setFechaHora(TIMESTAMP fechaHora) {
		this.fechaHora = fechaHora;
	}

	public TIMESTAMP getFechaHoraCierre() {
		return fechaHoraCierre;
	}

	public void setFechaHoraCierre(TIMESTAMP fechaHoraCierre) {
		this.fechaHoraCierre = fechaHoraCierre;
	}

	public Timestamp getFechaHoraTs() {
		try {
			return fechaHora.timestampValue();
		} catch (Exception e) {
			return new Timestamp(new Date().getTime());
		}
	}

	public void setFechaHoraTs(Timestamp fechaHora) {
		this.fechaHora = new TIMESTAMP(fechaHora);
	}

	public Timestamp getFechaHoraCierreTs() {
		try {
			return fechaHoraCierre.timestampValue();
		} catch (Exception e) {
			return new Timestamp(new Date().getTime());
		}
	}

	public void setFechaHoraCierreTs(Timestamp fechaHoraCierre) {
		this.fechaHoraCierre = new TIMESTAMP(fechaHoraCierre);
	}

	@Override
	public String getEstado() {
		return estado;
	}

	@Override
	public void setEstado(String estado) {
		this.estado = estado;
	}

	public Number getCodigo() {
		return codigo;
	}

	public void setCodigo(Number codigo) {
		this.codigo = codigo;
	}

	public Number getCodigoInstancia() {
		return codigoInstancia;
	}

	public void setCodigoInstancia(Number codigoInstancia) {
		this.codigoInstancia = codigoInstancia;
	}

	public Number getCodigoUsuario() {
		return codigoUsuario;
	}

	public void setCodigoUsuario(Number codigoUsuario) {
		this.codigoUsuario = codigoUsuario;
	}

	public String getValor1() {
		return valor1;
	}

	public void setValor1(String valor1) {
		this.valor1 = valor1;
	}

	public String getValor2() {
		return valor2;
	}

	public void setValor2(String valor2) {
		this.valor2 = valor2;
	}

	public String getValor3() {
		return valor3;
	}

	public void setValor3(String valor3) {
		this.valor3 = valor3;
	}

	public String getValor4() {
		return valor4;
	}

	public void setValor4(String valor4) {
		this.valor4 = valor4;
	}

	public String getValor5() {
		return valor5;
	}

	public void setValor5(String valor5) {
		this.valor5 = valor5;
	}

	public UsuarioModulo getUsuMod() {
		return usuMod;
	}

	public void setUsuMod(UsuarioModulo usuMod) {
		this.usuMod = usuMod;
	}

	public CausalDevolucion getCauDev() {
		return cauDev;
	}

	public void setCauDev(CausalDevolucion cauDev) {
		this.cauDev = cauDev;
	}

	public Rol getRol() {
		return rol;
	}

	public void setRol(Rol rol) {
		this.rol = rol;
	}

	public Double getHorasEtapa() {
		return horasEtapa;
	}

	public void setHorasEtapa(Double horasEtapa) {
		this.horasEtapa = horasEtapa;
	}

	public Number getDuracionActividad() {
		return duracionActividad;
	}

	public void setDuracionActividad(Number duracionActividad) {
		this.duracionActividad = duracionActividad;
	}

	public String getNumeroRadicacionNC() {
		return numeroRadicacionNC;
	}

	public void setNumeroRadicacionNC(String numeroRadicacionNC) {
		this.numeroRadicacionNC = numeroRadicacionNC;
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
