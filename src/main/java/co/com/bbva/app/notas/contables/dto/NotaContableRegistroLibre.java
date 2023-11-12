package co.com.bbva.app.notas.contables.dto;

import co.com.bbva.app.notas.contables.carga.dto.HADTAPL;
import co.com.bbva.app.notas.contables.carga.dto.PUC;
import co.com.bbva.app.notas.contables.carga.dto.Sucursal;
import co.com.bbva.app.notas.contables.util.DateUtils;
import co.com.bbva.app.notas.contables.util.StringUtils;

import java.sql.Date;

public class NotaContableRegistroLibre extends CommonVO<NotaContableRegistroLibre> implements java.io.Serializable {

	private static final long serialVersionUID = 6171981015973174249L;
	private int codigo = 0;
	private int codigoNotaContable = 0;
	private Date fechaContable = null;
	private String cuentaContable = "";
	private String naturalezaCuentaContable = "";
	private String codigoSucursalDestino = "";
	private String codigoDivisa = "";
	private double monto = 0;
	private String referencia = "00" + StringUtils.getString(DateUtils.getTimestamp(), "ddMMyy");
	private String tipoIdentificacion = "";
	private String numeroIdentificacion = "";
	private String digitoVerificacion = "";
	private String nombreCompleto = "";
	private String contrato = "";
	private RiesgoOperacional riesgoOperacional = new RiesgoOperacional();
	private String numeroAsiento = "";
	private String numeroApunte = "";

	private Sucursal sucursalDestino = new Sucursal();

	private String nombreTipoDoc = "";

	private PUC pucCuenta = new PUC();
	private HADTAPL hadtapl = new HADTAPL();

	private boolean editada = false;

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

	public Date getFechaContable() {
		return fechaContable;
	}

	public void setFechaContable(Date fechaContable) {
		this.fechaContable = fechaContable;
	}

	public String getCuentaContable() {
		return cuentaContable;
	}

	public void setCuentaContable(String cuentaContable) {
		this.cuentaContable = cuentaContable;
	}

	public String getNaturalezaCuentaContable() {
		return naturalezaCuentaContable;
	}

	public void setNaturalezaCuentaContable(String naturalezaCuentaContable) {
		this.naturalezaCuentaContable = naturalezaCuentaContable;
	}

	public String getCodigoSucursalDestino() {
		return codigoSucursalDestino;
	}

	public void setCodigoSucursalDestino(String codigoSucursalDestino) {
		this.codigoSucursalDestino = codigoSucursalDestino;
	}

	public String getCodigoDivisa() {
		return codigoDivisa;
	}

	public void setCodigoDivisa(String codigoDivisa) {
		this.codigoDivisa = codigoDivisa;
	}

	public double getMonto() {
		return monto;
	}

	public void setMonto(double monto) {
		this.monto = monto;
	}

	public String getReferencia() {
		return referencia;
	}

	public void setReferencia(String referencia) {
		this.referencia = referencia;
	}

	public String getTipoIdentificacion() {
		return tipoIdentificacion;
	}

	public void setTipoIdentificacion(String tipoIdentificacion) {
		this.tipoIdentificacion = tipoIdentificacion;
	}

	public String getNumeroIdentificacion() {
		return numeroIdentificacion;
	}

	public void setNumeroIdentificacion(String numeroIdentificacion) {
		this.numeroIdentificacion = numeroIdentificacion;
	}

	public String getDigitoVerificacion() {
		return digitoVerificacion;
	}

	public void setDigitoVerificacion(String digitoVerificacion) {
		this.digitoVerificacion = digitoVerificacion;
	}

	public String getNombreCompleto() {
		return nombreCompleto;
	}

	public void setNombreCompleto(String nombreCompleto) {
		this.nombreCompleto = nombreCompleto;
	}

	public String getContrato() {
		return contrato;
	}

	public void setContrato(String contrato) {
		this.contrato = contrato;
	}

	public RiesgoOperacional getRiesgoOperacional() {
		return riesgoOperacional;
	}

	public void setRiesgoOperacional(RiesgoOperacional riesgoOperacional) {
		this.riesgoOperacional = riesgoOperacional;
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

	public void setPucCuenta(PUC pucCuenta) {
		this.pucCuenta = pucCuenta;

	}

	public PUC getPucCuenta() {
		return pucCuenta;
	}

	public void setHadtapl(HADTAPL hadtapl) {
		this.hadtapl = hadtapl;
	}

	public HADTAPL getHadtapl() {
		return hadtapl;
	}

	public Sucursal getSucursalDestino() {
		return sucursalDestino;
	}

	public void setSucursalDestino(Sucursal sucursalDestino) {
		this.sucursalDestino = sucursalDestino;
	}

	public String getNombreTipoDoc() {
		return nombreTipoDoc;
	}

	public void setNombreTipoDoc(String nombreTipoDoc) {
		this.nombreTipoDoc = nombreTipoDoc;
	}

	public boolean isEditada() {
		return editada;
	}

	public void setEditada(boolean editada) {
		this.editada = editada;
	}

	@Override
	public Object getPK() {
		return codigo;
	}

	@Override
	public void restartPK(Object pk) {
		codigo = Integer.valueOf(pk.toString());
	}

	public void reset(NotaContableRegistroLibre reg) {
		this.codigoNotaContable = reg.codigoNotaContable;
		this.fechaContable = reg.fechaContable;
		this.cuentaContable = reg.cuentaContable;
		this.naturalezaCuentaContable = reg.naturalezaCuentaContable;
		this.codigoSucursalDestino = reg.codigoSucursalDestino;
		this.codigoDivisa = reg.codigoDivisa;
		this.monto = reg.monto;
		this.referencia = reg.referencia;
		this.tipoIdentificacion = reg.tipoIdentificacion;
		this.numeroIdentificacion = reg.numeroIdentificacion;
		this.digitoVerificacion = reg.digitoVerificacion;
		this.nombreCompleto = reg.nombreCompleto;
		this.contrato = reg.contrato;
		this.riesgoOperacional = reg.riesgoOperacional;
		this.numeroAsiento = reg.numeroAsiento;
		this.numeroApunte = reg.numeroApunte;
	}

}
