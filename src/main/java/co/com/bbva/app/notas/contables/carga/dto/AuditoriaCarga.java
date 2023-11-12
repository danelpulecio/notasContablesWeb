package co.com.bbva.app.notas.contables.carga.dto;

import co.com.bbva.app.notas.contables.dto.CommonVO;

import java.sql.Timestamp;

public class AuditoriaCarga extends CommonVO<AuditoriaCarga> implements java.io.Serializable {

	private static final long serialVersionUID = -9169021443766736773L;
	private int codigo = 0;
	private Timestamp fecha = null;
	private String nombreArchivo = "";
	private String codigoRegistro = "";
	private String descripcionRegistro = "";
	private String notas = "";
	private String estado = "I";

	public int getCodigo() {
		return codigo;
	}

	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}

	public Timestamp getFecha() {
		return fecha;
	}

	public void setFecha(Timestamp fecha) {
		this.fecha = fecha;
	}

	public String getNombreArchivo() {
		return nombreArchivo;
	}

	public void setNombreArchivo(String nombreArchivo) {
		this.nombreArchivo = nombreArchivo;
	}

	public String getCodigoRegistro() {
		return codigoRegistro;
	}

	public void setCodigoRegistro(String codigoRegistro) {
		this.codigoRegistro = codigoRegistro;
	}

	public String getDescripcionRegistro() {
		return descripcionRegistro;
	}

	public void setDescripcionRegistro(String descripcionRegistro) {
		this.descripcionRegistro = descripcionRegistro;
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

}
