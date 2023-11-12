package co.com.bbva.app.notas.contables.carga.dto;

import java.io.Serializable;

public class NotasOKSalida implements Serializable, ISalida {

	private static final long serialVersionUID = 5808502661794649587L;

	private String notOk = "";
	private String numNota = "";
	private String fechaHora = "";

	public NotasOKSalida() {
		super();
	}

	public String getNotOk() {
		return notOk;
	}

	public void setNotOk(String notOk) {
		this.notOk = notOk;
	}

	public String getNumNota() {
		return numNota;
	}

	public void setNumNota(String numNota) {
		this.numNota = numNota;
	}

	public String getFechaHora() {
		return fechaHora;
	}

	public void setFechaHora(String fechaHora) {
		this.fechaHora = fechaHora;
	}

	public void produceFromString(String str) throws Exception {
		if (str == null || str.length() != 43) {
			throw new Exception("La cadena " + str + " no tiene un formato de longitud 43 y no puede ser procesada ");
		}
		String ss = str.substring(0, 10);
		notOk = ss;
		ss = str.substring(10, 25);
		numNota = ss;
		ss = str.substring(15, 33);
		fechaHora = ss;
	}

}
