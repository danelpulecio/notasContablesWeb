package co.com.bbva.app.notas.contables.dto;

import java.sql.Timestamp;

public class Observacion extends CommonVO<Observacion> implements java.io.Serializable {

	private static final long serialVersionUID = 4678081570258492461L;

	private int codigo = 0;
	private Timestamp fechaHora = null;
	private int codigoInstancia = 0;
	private String estadoInstancia = "";
	private int codigoTema = 0;
	private int codigoUsuario = 0;
	private String notas = "";
	private String estado = "A";

	public int getCodigo() {
		return codigo;
	}

	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}

	public Timestamp getFechaHora() {
		return fechaHora;
	}

	public void setFechaHora(Timestamp fechaHora) {
		this.fechaHora = fechaHora;
	}

	public int getCodigoInstancia() {
		return codigoInstancia;
	}

	public void setCodigoInstancia(int codigoInstancia) {
		this.codigoInstancia = codigoInstancia;
	}

	public String getEstadoInstancia() {
		return estadoInstancia;
	}

	public void setEstadoInstancia(String estadoInstancia) {
		this.estadoInstancia = estadoInstancia;
	}

	public int getCodigoTema() {
		return codigoTema;
	}

	public void setCodigoTema(int codigoTema) {
		this.codigoTema = codigoTema;
	}

	public int getCodigoUsuario() {
		return codigoUsuario;
	}

	public void setCodigoUsuario(int codigoUsuario) {
		this.codigoUsuario = codigoUsuario;
	}

	public String getNotas() {
		return notas;
	}

	public void setNotas(String notas) {
		this.notas = notas;
	}

	@Override
	public String getEstado() {
		return estado;
	}

	@Override
	public void setEstado(String estado) {
		this.estado = estado;
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
