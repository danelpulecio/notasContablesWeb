package co.com.bbva.app.notas.contables.dto;

import co.com.bbva.app.notas.contables.anotacion.Columna;
import co.com.bbva.app.notas.contables.anotacion.ColumnaId;
import co.com.bbva.app.notas.contables.anotacion.Tabla;

@Tabla(nombreTabla = "NC_CAUSAL_DEVOLUCION")
public class CausalDevolucion extends CommonVO<CausalDevolucion> implements java.io.Serializable {

	private static final long serialVersionUID = -4530683845392202122L;
	@ColumnaId
	@Columna(nombreDB = "CODIGO", nombreApp = "codigo", esClave = true)
	private Number codigo = 0;
	@Columna(nombreDB = "NOMBRE", nombreApp = "nombre", esValor = true)
	private String nombre = "";
	@Columna(nombreDB = "ESTADO", nombreApp = "estado", esEstado = true)
	private String estado = "A";

	public Number getCodigo() {
		return codigo;
	}

	public void setCodigo(Number codigo) {
		this.codigo = codigo;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
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
