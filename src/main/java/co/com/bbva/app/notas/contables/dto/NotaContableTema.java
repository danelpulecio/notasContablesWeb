package co.com.bbva.app.notas.contables.dto;

import co.com.bbva.app.notas.contables.carga.dto.Sucursal;
import co.com.bbva.app.notas.contables.util.DateUtils;
import co.com.bbva.app.notas.contables.util.StringUtils;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;

public class NotaContableTema extends CommonVO<NotaContableTema> implements java.io.Serializable {

	private static final long serialVersionUID = -1931903714644156940L;

	private Number codigo = 0;
	private boolean obligatorio = false;
	private Number codigoNotaContable = 0;
	private Number codigoTema = 0;
	private Date fechaContable = null;
//	private java.util.Date  fechaContablePF = null;
	@SuppressWarnings("unused")
	private final Timestamp fechaContableTS = null;
	private String partidaContable = "";
	private String naturalezaPartidaContable = "";
	private String contrapartidaContable = "";
	private String natContContable = "";
	private String codigoSucursalDestinoPartida = "";
	private String codSucDestContraPartida = "";
	private String codigoDivisa = "";
	private Number monto = 0;
	private String referencia1 = "00" + StringUtils.getString(DateUtils.getTimestamp(), "ddMMyy");
	private String referencia2 = "00" + StringUtils.getString(DateUtils.getTimestamp(), "ddMMyy");
	private String tipoIdentificacion1 = "";
	private String numeroIdentificacion1 = "";
	private String digitoVerificacion1 = "";
	private String nombreCompleto1 = "";
	private String tipoIdentificacion2 = "";
	private String numeroIdentificacion2 = "";
	private String digitoVerificacion2 = "";
	private String nombreCompleto2 = "";
	private String contrato1 = "";
	private String contrato2 = "";
	private String descripcion = "";
	private RiesgoOperacional riesgoOperacional = new RiesgoOperacional();
	private ArrayList<NotaContableTemaImpuesto> impuestoTema = new ArrayList<NotaContableTemaImpuesto>();
	private ArrayList<Anexo> anexoTema = new ArrayList<Anexo>();
	private String numeroAsiento = "";
	private String numeroApunte = "";


	// tema asociado a la nota
	private Tema tema = new Tema();
	// indica si un tema est autorizado para edicion por parte del usuario actual
	private Boolean autorizada = true;

	private Sucursal sucursalDestinoPartida = new Sucursal();
	private Sucursal sucursalDestinoContraPartida = new Sucursal();

	private String nombreTipoDoc1 = "";
	private String nombreTipoDoc2 = "";

	private boolean editada = false;

	public boolean isObligatorio() {
		return obligatorio;
	}

	public void setCodigoNotaContable(int codigoNotaContable) {
		this.codigoNotaContable = codigoNotaContable;
	}

	public Timestamp getFechaContableTS() {
		return new Timestamp(fechaContable.getTime());
	}

	public void setFechaContableTS(Timestamp fechaContable) {
		this.fechaContable = new Date(fechaContable.getTime());
	}

	public Date getFechaContable() {
		return fechaContable;
	}

	public void setFechaContable(Date fechaContable) {
		this.fechaContable = fechaContable;
	}
	
//	public java.util.Date getFechaContablePF() {
//		return fechaContablePF;
//	}
//
//	public void setFechaContablePF(java.util.Date fechaContablePF) {
//		this.fechaContablePF = fechaContablePF;
//	}

	public String getPartidaContable() {
		return partidaContable;
	}

	public void setPartidaContable(String partidaContable) {
		this.partidaContable = partidaContable;
	}

	public String getNaturalezaPartidaContable() {
		return naturalezaPartidaContable;
	}

	public void setNaturalezaPartidaContable(String naturalezaPartidaContable) {
		this.naturalezaPartidaContable = naturalezaPartidaContable;
	}

	public String getContrapartidaContable() {
		return contrapartidaContable;
	}

	public void setContrapartidaContable(String contrapartidaContable) {
		this.contrapartidaContable = contrapartidaContable;
	}

	public String getNaturalezaContrapartidaContable() {
		return natContContable;
	}

	public void setNaturalezaContrapartidaContable(String naturalezaContrapartidaContable) {
		this.natContContable = naturalezaContrapartidaContable;
	}

	public String getNatContContable() {
		return natContContable;
	}

	public void setNatContContable(String natContContable) {
		this.natContContable = natContContable;
	}

	public String getCodigoSucursalDestinoPartida() {
		return codigoSucursalDestinoPartida;
	}

	public void setCodigoSucursalDestinoPartida(String codigoSucursalDestinoPartida) {
		this.codigoSucursalDestinoPartida = codigoSucursalDestinoPartida;
	}

	public String getCodigoSucursalDestinoContraPartida() {
		return codSucDestContraPartida;
	}

	public void setCodigoSucursalDestinoContraPartida(String codigoSucursalDestinoContraPartida) {
		this.codSucDestContraPartida = codigoSucursalDestinoContraPartida;
	}

	public String getCodigoDivisa() {
		return codigoDivisa;
	}

	public void setCodigoDivisa(String codigoDivisa) {
		this.codigoDivisa = codigoDivisa;
	}

	public String getReferencia1() {
		return referencia1;
	}

	public void setReferencia1(String referencia1) {
		this.referencia1 = referencia1;
	}

	public String getReferencia2() {
		return referencia2;
	}

	public void setReferencia2(String referencia2) {
		this.referencia2 = referencia2;
	}

	public String getTipoIdentificacion1() {
		return tipoIdentificacion1;
	}

	public void setTipoIdentificacion1(String tipoIdentificacion1) {
		this.tipoIdentificacion1 = tipoIdentificacion1;
	}

	public String getNumeroIdentificacion1() {
		return numeroIdentificacion1;
	}

	public void setNumeroIdentificacion1(String numeroIdentificacion1) {
		this.numeroIdentificacion1 = numeroIdentificacion1;
	}

	public String getDigitoVerificacion1() {
		return digitoVerificacion1;
	}

	public void setDigitoVerificacion1(String digitoVerificacion1) {
		this.digitoVerificacion1 = digitoVerificacion1;
	}

	public String getNombreCompleto1() {
		return nombreCompleto1;
	}

	public void setNombreCompleto1(String nombreCompleto1) {
		this.nombreCompleto1 = nombreCompleto1;
	}

	public String getTipoIdentificacion2() {
		return tipoIdentificacion2;
	}

	public void setTipoIdentificacion2(String tipoIdentificacion2) {
		this.tipoIdentificacion2 = tipoIdentificacion2;
	}

	public String getNumeroIdentificacion2() {
		return numeroIdentificacion2;
	}

	public void setNumeroIdentificacion2(String numeroIdentificacion2) {
		this.numeroIdentificacion2 = numeroIdentificacion2;
	}

	public String getDigitoVerificacion2() {
		return digitoVerificacion2;
	}

	public void setDigitoVerificacion2(String digitoVerificacion2) {
		this.digitoVerificacion2 = digitoVerificacion2;
	}

	public String getNombreCompleto2() {
		return nombreCompleto2;
	}

	public void setNombreCompleto2(String nombreCompleto2) {
		this.nombreCompleto2 = nombreCompleto2;
	}

	public String getContrato1() {
		return contrato1;
	}

	public void setContrato1(String contrato1) {
		this.contrato1 = contrato1;
	}

	public String getContrato2() {
		return contrato2;
	}

	public void setContrato2(String contrato2) {
		this.contrato2 = contrato2;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public RiesgoOperacional getRiesgoOperacional() {
		return riesgoOperacional;
	}

	public void setRiesgoOperacional(RiesgoOperacional riesgoOperacional) {
		this.riesgoOperacional = riesgoOperacional;
	}

	public ArrayList<NotaContableTemaImpuesto> getImpuestoTema() {
		return impuestoTema;
	}

	public void setImpuestoTema(ArrayList<NotaContableTemaImpuesto> impuestoTema) {
		this.impuestoTema = impuestoTema;
	}

	public ArrayList<Anexo> getAnexoTema() {
		return anexoTema;
	}

	public void setAnexoTema(ArrayList<Anexo> anexoTema) {
		this.anexoTema = anexoTema;
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

	public Tema getTema() {
		return tema;
	}

	public void setTema(Tema tema) {
		this.tema = tema;
	}

	public Boolean getAutorizada() {
		return autorizada;
	}

	public void setAutorizada(Boolean autorizada) {
		this.autorizada = autorizada;
	}

	public Number getCodigo() {
		return codigo;
	}

	public void setCodigo(Number codigo) {
		this.codigo = codigo;
	}

	public Number getCodigoNotaContable() {
		return codigoNotaContable;
	}

	public void setCodigoNotaContable(Number codigoNotaContable) {
		this.codigoNotaContable = codigoNotaContable;
	}

	public Number getCodigoTema() {
		return codigoTema;
	}

	public void setCodigoTema(Number codigoTema) {
		this.codigoTema = codigoTema;
	}

	public void setObligatorio(boolean obligatorio) {
		this.obligatorio = obligatorio;
	}

	public Number getMonto() {
		return monto;
	}

	public void setMonto(Number monto) {
		this.monto = monto;
	}

	public Sucursal getSucursalDestinoPartida() {
		return sucursalDestinoPartida;
	}

	public void setSucursalDestinoPartida(Sucursal sucursalDestinoPartida) {
		this.sucursalDestinoPartida = sucursalDestinoPartida;
	}

	public Sucursal getSucursalDestinoContraPartida() {
		return sucursalDestinoContraPartida;
	}

	public void setSucursalDestinoContraPartida(Sucursal sucursalDestinoContraPartida) {
		this.sucursalDestinoContraPartida = sucursalDestinoContraPartida;
	}

	public String getCodSucDestContraPartida() {
		return codSucDestContraPartida;
	}

	public void setCodSucDestContraPartida(String codSucDestContraPartida) {
		this.codSucDestContraPartida = codSucDestContraPartida;
	}

	public String getNombreTipoDoc1() {
		return nombreTipoDoc1;
	}

	public void setNombreTipoDoc1(String nombreTipoDoc1) {
		this.nombreTipoDoc1 = nombreTipoDoc1;
	}

	public String getNombreTipoDoc2() {
		return nombreTipoDoc2;
	}

	public void setNombreTipoDoc2(String nombreTipoDoc2) {
		this.nombreTipoDoc2 = nombreTipoDoc2;
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

	public void reset(NotaContableTema nct) {
		setFechaContable(nct.fechaContable);
		setMonto(nct.monto);
		setCodigoDivisa(nct.codigoDivisa);
		setDescripcion(nct.descripcion);
		setTipoIdentificacion1(nct.tipoIdentificacion1);
		setNumeroIdentificacion1(nct.numeroIdentificacion1);
		setDigitoVerificacion1(nct.digitoVerificacion1);
		setNombreCompleto1(nct.nombreCompleto1);
		setContrato1(nct.contrato1);
		setCodigoSucursalDestinoPartida(nct.codigoSucursalDestinoPartida);

		setTipoIdentificacion2(nct.tipoIdentificacion2);
		setNumeroIdentificacion2(nct.numeroIdentificacion2);
		setDigitoVerificacion2(nct.digitoVerificacion2);
		setNombreCompleto2(nct.nombreCompleto2);
		setContrato2(nct.contrato2);
		setCodigoSucursalDestinoContraPartida(nct.getCodigoSucursalDestinoContraPartida());

		setAnexoTema(nct.getAnexoTema());

		setRiesgoOperacional(nct.getRiesgoOperacional());

		setImpuestoTema(nct.getImpuestoTema());

	}

}
