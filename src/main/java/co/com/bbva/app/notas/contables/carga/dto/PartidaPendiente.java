package co.com.bbva.app.notas.contables.carga.dto;

import co.com.bbva.app.notas.contables.dto.CommonVO;

import java.sql.Timestamp;

public class PartidaPendiente extends CommonVO<PartidaPendiente> implements java.io.Serializable, Comparable<PartidaPendiente> {

	private static final long serialVersionUID = 5553800077715494800L;
	private String cuenta = "";
	private String descripcion = "";
	private String sucursalDestino = "";
	private String naturaleza = "";
	private String referenciaCruce = "";
	private String divisa = "";
	private double importe = 0;
	private String concepto = "";
	private Timestamp fechaContable = null;
	private String estadoCarga = "";
	private String numeroAsiento = "";
	private Timestamp fechaUltimaCarga = null;
	private String usada = "N";

	// campo temporal usado para saber la seleccion temporal al momento de crear cruces de referencia
	private Boolean seleccionada = false;

	public String getCuenta() {
		return cuenta;
	}

	public void setCuenta(String cuenta) {
		this.cuenta = cuenta;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getSucursalDestino() {
		return sucursalDestino;
	}

	public void setSucursalDestino(String sucursalDestino) {
		this.sucursalDestino = sucursalDestino;
	}

	public String getNaturaleza() {
		return naturaleza;
	}

	public void setNaturaleza(String naturaleza) {
		this.naturaleza = naturaleza;
	}

	public String getReferenciaCruce() {
		return referenciaCruce;
	}

	public void setReferenciaCruce(String referenciaCruce) {
		this.referenciaCruce = referenciaCruce;
	}

	public String getDivisa() {
		return divisa;
	}

	public void setDivisa(String divisa) {
		this.divisa = divisa;
	}

	public double getImporte() {
		return importe;
	}

	public void setImporte(double importe) {
		this.importe = importe;
	}

	public String getConcepto() {
		return concepto;
	}

	public void setConcepto(String concepto) {
		this.concepto = concepto;
	}

	public Timestamp getFechaContable() {
		return fechaContable;
	}

	public void setFechaContable(Timestamp fechaContable) {
		this.fechaContable = fechaContable;
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
		return cuenta;
	}

	public String getUsada() {
		return usada;
	}

	public void setUsada(String usada) {
		this.usada = usada;
	}

	public Boolean getSeleccionada() {
		return seleccionada;
	}

	public void setSeleccionada(Boolean seleccionada) {
		this.seleccionada = seleccionada;
	}

	public String getNumeroAsiento() {
		return numeroAsiento;
	}

	public void setNumeroAsiento(String numeroAsiento) {
		this.numeroAsiento = numeroAsiento;
	}

	@Override
	public boolean equals(Object o) {
		PartidaPendiente p = (PartidaPendiente) o;
		if (!cuenta.equals(p.cuenta)) {
			return false;
		}
		if (!descripcion.equals(p.descripcion)) {
			return false;
		}
		if (!sucursalDestino.equals(p.sucursalDestino)) {
			return false;
		}
		if (!naturaleza.equals(p.naturaleza)) {
			return false;
		}
		if (!referenciaCruce.equals(p.referenciaCruce)) {
			return false;
		}
		if (!divisa.equals(p.divisa)) {
			return false;
		}
		if (!concepto.equals(p.concepto)) {
			return false;
		}
		if (importe != p.importe) {
			return false;
		}
		if (!fechaContable.equals(p.fechaContable)) {
			return false;
		}
		return true;
	}

	@Override
	public int compareTo(PartidaPendiente o) {
		return o.getImporte() > importe ? -1 : 1;
	}
}
