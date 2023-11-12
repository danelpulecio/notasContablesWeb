package co.com.bbva.app.notas.contables.dto;

import co.com.bbva.app.notas.contables.anotacion.Columna;
import co.com.bbva.app.notas.contables.anotacion.ColumnaId;
import co.com.bbva.app.notas.contables.anotacion.Tabla;

@Tabla(nombreTabla = "NC_MONTO_MAXIMO")
public class MontoMaximo extends CommonVO<MontoMaximo> implements java.io.Serializable {

	private static final long serialVersionUID = -6022839440926398069L;

	@ColumnaId
	@Columna(nombreDB = "CODIGO", nombreApp = "codigo")
	private int codigo = 0;
	@Columna(nombreDB = "NOMBRE", nombreApp = "nombre")
	private String nombre = "";
	@Columna(nombreDB = "DIVISA", nombreApp = "divisa")
	private String divisa = "";
	@Columna(nombreDB = "MONTO_MAXIMO", nombreApp = "montoMaximo")
	private double montoMaximo = 0;
	@Columna(nombreDB = "MONTO_MAX_ALERTA", nombreApp = "montoMaximoAlerta")
	private double montoMaximoAlerta = 0;
	@Columna(nombreDB = "ESTADO", nombreApp = "estado", esEstado = true)
	private String estado = "A";

	public int getCodigo() {
		return codigo;
	}

	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getDivisa() {
		return divisa;
	}

	public void setDivisa(String divisa) {
		this.divisa = divisa;
	}

	public double getMontoMaximo() {
		return montoMaximo;
	}

	public void setMontoMaximo(double montoMaximo) {
		this.montoMaximo = montoMaximo;
	}

	public double getMontoMaximoAlerta() {
		return montoMaximoAlerta;
	}

	public void setMontoMaximoAlerta(double montoMaximoAlerta) {
		this.montoMaximoAlerta = montoMaximoAlerta;
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
