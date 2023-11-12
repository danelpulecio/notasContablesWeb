package co.com.bbva.app.notas.contables.carga.dto;

import co.com.bbva.app.notas.contables.anotacion.Columna;
import co.com.bbva.app.notas.contables.anotacion.Tabla;
import co.com.bbva.app.notas.contables.dto.CommonVO;

import java.sql.Timestamp;

@Tabla(nombreTabla = "NC_PRODUCTO")
public class Producto extends CommonVO<Producto> implements java.io.Serializable {

	private static final long serialVersionUID = 1268167139466871837L;
	@Columna(nombreDB = "CODIGO", nombreApp = "codigo", esClave = true)
	private String codigo = "";
	@Columna(nombreDB = "NOMBRE", nombreApp = "nombre", esValor = true)
	private String nombre = "";
	private String estadoCarga = "";
	private Timestamp fechaUltimaCarga = null;

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getEstadoCarga() {
		return estadoCarga;
	}

	public void setEstadoCarga(String estadoCarga) {
		this.estadoCarga = estadoCarga;
	}

	public Timestamp getFechaUltimaCarga() {
		return fechaUltimaCarga;
	}

	public void setFechaUltimaCarga(Timestamp fechaUltimaCarga) {
		this.fechaUltimaCarga = fechaUltimaCarga;
	}

	@Override
	public Object getPK() {
		return codigo;
	}

}
