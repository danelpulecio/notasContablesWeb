package co.com.bbva.app.notas.contables.dto;

import java.sql.Timestamp;

public class NotaContableCrucePartidaPendiente extends CommonVO<NotaContableCrucePartidaPendiente> implements java.io.Serializable {

	private static final long serialVersionUID = 8094027933007298833L;

	private int codigo = 0;
	private int codigoNotaContable = 0;
	private String cuentaContable = "";
	private String descripcionPartidaPendiente = "";
	private String codigoSucursalDestino = "";
	private String naturaleza = "";
	private String referenciaCruce = "";
	private String divisa = "";
	private double importe = 0;
	private String concepto = "";
	private Timestamp fechaContable = null;
	private String numeroAsiento = "";
	private String numeroApunte = "";

	public int getCodigo() {
		return codigo;
	}

	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}

	public int getCodigoNotaContable() {
		return codigoNotaContable;
	}

	public void setCodigoNotaContable(int codigoNotaContable) {
		this.codigoNotaContable = codigoNotaContable;
	}

	public String getCuentaContable() {
		return cuentaContable;
	}

	public void setCuentaContable(String cuentaContable) {
		this.cuentaContable = cuentaContable;
	}

	public String getDescripcionPartidaPendiente() {
		return descripcionPartidaPendiente;
	}

	public void setDescripcionPartidaPendiente(String descripcionPartidaPendiente) {
		this.descripcionPartidaPendiente = descripcionPartidaPendiente;
	}

	public String getCodigoSucursalDestino() {
		return codigoSucursalDestino;
	}

	public void setCodigoSucursalDestino(String codigoSucursalDestino) {
		this.codigoSucursalDestino = codigoSucursalDestino;
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

	public String getNumeroAsiento() {
		return numeroAsiento;
	}

	public void setNumeroAsiento(String numeroAsiento) {
		this.numeroAsiento = numeroAsiento;
	}

	public String getNumeroApunte() {
		return numeroApunte;
	}

	public void setNumeroApunte(String numeroApunte) {
		this.numeroApunte = numeroApunte;
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
