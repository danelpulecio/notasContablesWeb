package co.com.bbva.app.notas.contables.carga.dto;

import co.com.bbva.app.notas.contables.dto.CommonVO;

import java.io.Serializable;

public class RechazoSalida extends CommonVO<RechazoSalida> implements Serializable, ISalida {

	private static final long serialVersionUID = -1260457731355627331L;
	private Number codigo;
	private String ceOrigen = "";
	private String ceDestin = "";
	private Number consecutivo = 0;
	private String numNota = "";
	private String fecha = "";
	private String cuenta = "";
	private String divisa = "";
	private Number codError = 0;
	private String desError = "";
	private String fechaGen = "";
	private String horasGen = "";

	public RechazoSalida() {
		super();
	}

	public String getCeOrigen() {
		return ceOrigen;
	}

	public void setCeOrigen(String ceOrigen) {
		this.ceOrigen = ceOrigen;
	}

	public String getCeDestin() {
		return ceDestin;
	}

	public void setCeDestin(String ceDestin) {
		this.ceDestin = ceDestin;
	}

	public Number getConsecutivo() {
		return consecutivo;
	}

	public void setConsecutivo(Number consecutivo) {
		this.consecutivo = consecutivo;
	}

	public String getNumNota() {
		return numNota;
	}

	public void setNumNota(String numNota) {
		this.numNota = numNota;
	}

	public String getFecha() {
		return fecha;
	}

	public void setFecha(String fecha) {
		this.fecha = fecha;
	}

	public String getCuenta() {
		return cuenta;
	}

	public void setCuenta(String cuenta) {
		this.cuenta = cuenta;
	}

	public String getDivisa() {
		return divisa;
	}

	public void setDivisa(String divisa) {
		this.divisa = divisa;
	}

	public Number getCodError() {
		return codError;
	}

	public void setCodError(Number codError) {
		this.codError = codError;
	}

	public String getDesError() {
		return desError;
	}

	public void setDesError(String desError) {
		this.desError = desError;
	}

	public String getFechaGen() {
		return fechaGen;
	}

	public void setFechaGen(String fechaGen) {
		this.fechaGen = fechaGen;
	}

	public String getHorasGen() {
		return horasGen;
	}

	public void setHorasGen(String horasGen) {
		this.horasGen = horasGen;
	}

	public Number getCodigo() {
		return codigo;
	}

	public void setCodigo(Number codigo) {
		this.codigo = codigo;
	}

	@Override
	public String toString() {
		return ceOrigen + ";" + ceDestin + ";" + consecutivo + ";" + numNota + ";" + fecha + ";" + cuenta + ";" + divisa + ";" + codError + ";" + desError + ";" + fechaGen + ";" + horasGen;
	}

	public void produceFromString(String str) throws Exception {
		if (str == null || str.length() != 178) {
			throw new Exception("La cadena " + str + " no tiene un formato de longitud 178 y no puede ser procesada");
		}
		String ss = str.substring(0, 4);
		// empresa = ss;
		// ss = str.substring(4, 8);
		// ceOpe = ss;
		ss = str.substring(8, 12);
		ceOrigen = ss;
		ss = str.substring(12, 16);
		ceDestin = ss;
		ss = str.substring(16, 26);
		consecutivo = Long.valueOf(ss);
		ss = str.substring(26, 41);
		numNota = ss;
		ss = str.substring(41, 49);
		fecha = ss;
		ss = str.substring(49, 64);
		cuenta = ss;
		ss = str.substring(64, 67);
		divisa = ss;
		ss = str.substring(67, 71);
		codError = Integer.valueOf(ss);
		ss = str.substring(71, 160);
		desError = ss;
		ss = str.substring(160, 170);
		fechaGen = ss;
		ss = str.substring(170, 178);
		horasGen = ss;
	}

	@Override
	public Object getPK() {
		return codigo;
	}

}
