package co.com.bbva.app.notas.contables.dto;

import java.io.Serializable;

public class Parametro extends CommonVO<Parametro> implements Serializable {

	public static final String CIERRE_MENSUAL = "cierre_mensual";
	public static final String DELTA_BORRADO_ANULACION = "delta_borr_anulacion";

	private static final long serialVersionUID = -3169142291034317040L;
	private String nombre = "";
	private Number valor = 0;

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public Number getValor() {
		return valor;
	}

	public void setValor(Number valor) {
		this.valor = valor;
	}

	@Override
	public Object getPK() {
		return nombre;
	}

	@Override
	public void restartPK(Object pk) {
		// no se reinicia nada
	}

}
