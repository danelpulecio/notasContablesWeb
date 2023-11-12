package co.com.bbva.app.notas.contables.dto;

import oracle.sql.TIMESTAMP;

import java.sql.Timestamp;
import java.util.Date;

public class Anexo extends CommonVO<Anexo> implements java.io.Serializable {

	private static final long serialVersionUID = 4719603620833131713L;

	private Number codigo = 0;
	private Timestamp fechaHora = null;
	@SuppressWarnings("unused")
	private final TIMESTAMP fechaHoraTS = null;
	private Number codigoInstancia = 0;
	private String estadoInstancia = "";
	private Number codigoTema = 0;
	private Number codigoUsuario = 0;
	private String nombre = "";
	private String archivo = "";
	private String estado = "A";

	private Boolean borrar = false;

	private String usuNombre = "";

	public Timestamp getFechaHora() {
		return fechaHora;
	}

	public void setFechaHora(Timestamp fechaHora) {
		this.fechaHora = fechaHora;
	}

	public TIMESTAMP getFechaHoraTs() {
		return new TIMESTAMP(fechaHora);
	}

	public void setFechaHoraTS(TIMESTAMP fechaHora) {
		try {
			this.fechaHora = fechaHora.timestampValue();
		} catch (Exception e) {
			e.printStackTrace();
			this.fechaHora = new Timestamp(new Date().getTime());
		}
	}

	public String getEstadoInstancia() {
		return estadoInstancia;
	}

	public void setEstadoInstancia(String estadoInstancia) {
		this.estadoInstancia = estadoInstancia;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getArchivo() {
		return archivo;
	}

	public void setArchivo(String archivo) {
		this.archivo = archivo;
	}

	@Override
	public String getEstado() {
		return estado;
	}

	@Override
	public void setEstado(String estado) {
		this.estado = estado;
	}

	public Boolean getBorrar() {
		return borrar;
	}

	public void setBorrar(Boolean borrar) {
		this.borrar = borrar;
	}

	public String getUsuNombre() {
		return usuNombre;
	}

	public void setUsuNombre(String usuNombre) {
		this.usuNombre = usuNombre;
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

	public Number getCodigoTema() {
		return codigoTema;
	}

	public void setCodigoTema(Number codigoTema) {
		this.codigoTema = codigoTema;
	}

	public Number getCodigoUsuario() {
		return codigoUsuario;
	}

	public void setCodigoUsuario(Number codigoUsuario) {
		this.codigoUsuario = codigoUsuario;
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
