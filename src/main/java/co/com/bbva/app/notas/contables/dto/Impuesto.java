package co.com.bbva.app.notas.contables.dto;

import co.com.bbva.app.notas.contables.anotacion.Columna;
import co.com.bbva.app.notas.contables.anotacion.ColumnaId;
import co.com.bbva.app.notas.contables.anotacion.Tabla;

@Tabla(nombreTabla = "NC_IMPUESTO")
public class Impuesto extends CommonVO<Impuesto> implements java.io.Serializable {

	private static final long serialVersionUID = 4074840711140878603L;
	@ColumnaId
	@Columna(nombreDB = "CODIGO", nombreApp = "codigo", esClave = true)
	private Number codigo = 0;
	@Columna(nombreDB = "NOMBRE", nombreApp = "nombre", esValor = true)
	private String nombre = "";
	@Columna(nombreDB = "VALOR", nombreApp = "valor")
	private double valor = 0;
	@Columna(nombreDB = "PARTIDA_CONTABLE", nombreApp = "partidaContable")
	String partidaContable = "";
	@Columna(nombreDB = "CONTRAPARTIDA_CONTABLE", nombreApp = "contraPartidaContable")
	String contraPartidaContable = "";
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

	public double getValor() {
		return valor;
	}

	public void setValor(double valor) {
		this.valor = valor;
	}

	public String getPartidaContable() {
		return partidaContable;
	}

	public void setPartidaContable(String partidaContable) {
		this.partidaContable = partidaContable;
	}

	public String getContraPartidaContable() {
		return contraPartidaContable;
	}

	public void setContraPartidaContable(String contraPartidaContable) {
		this.contraPartidaContable = contraPartidaContable;
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
