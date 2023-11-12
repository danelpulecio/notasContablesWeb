package co.com.bbva.app.notas.contables.carga.dto;

import java.io.Serializable;

public class AsientoSalida implements Serializable, ISalida {

	private static final long serialVersionUID = -1920370942604349394L;
	private String consecutivo;
	private String numNota;
	private Number asiento;
	private Number apunte;

	public AsientoSalida() {
		super();
		consecutivo = "";
		numNota = "";
		asiento = 0;
		apunte = 0;
	}

	public String getConsecutivo() {
		return consecutivo;
	}

	public void setConsecutivo(String consecutivo) {
		this.consecutivo = consecutivo;
	}

	public String getNumNota() {
		return numNota;
	}

	public void setNumNota(String numNota) {
		this.numNota = numNota;
	}

	public Number getAsiento() {
		return asiento;
	}

	public void setAsiento(Number asiento) {
		this.asiento = asiento;
	}

	public Number getApunte() {
		return apunte;
	}

	public void setApunte(Number apunte) {
		this.apunte = apunte;
	}

	public void produceFromString(String str) throws Exception {
		if (str == null || str.length() != 39) {
			throw new Exception("La cadena " + str + " no tiene un formato de longitud 39 y no puede ser procesada");
		}
		String ss = str.substring(0, 10);
		consecutivo = ss;
		ss = str.substring(10, 25);
		numNota = ss;
		ss = str.substring(25, 32);
		asiento = Integer.valueOf(ss);
		ss = str.substring(32, 39);
		apunte = Integer.valueOf(ss);
	}

	@Override
	public String toString() {
		return consecutivo + ";" + numNota + ";" + asiento + ";" + apunte;
	}

}
