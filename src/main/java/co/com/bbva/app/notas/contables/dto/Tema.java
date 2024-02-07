package co.com.bbva.app.notas.contables.dto;

import co.com.bbva.app.notas.contables.anotacion.Columna;
import co.com.bbva.app.notas.contables.anotacion.ColumnaId;
import co.com.bbva.app.notas.contables.anotacion.Tabla;
import co.com.bbva.app.notas.contables.carga.dto.PUC;

@Tabla(nombreTabla = "NC_TEMA")
public class Tema extends CommonVO<Tema> implements java.io.Serializable {

	private static final long serialVersionUID = -8970002437078802545L;

	@ColumnaId
	@Columna(nombreDB = "CODIGO", nombreApp = "codigo")
	private Number codigo = 0;
	@Columna(nombreDB = "CODIGO_CONCEPTO", nombreApp = "codigoConcepto")
	private Number codigoConcepto = 0;
	@Columna(nombreDB = "NOMBRE", nombreApp = "nombre")
	private String nombre = "";
	@Columna(nombreDB = "ACTIVAR", nombreApp = "activar")
	private String activar = "S";
	@Columna(nombreDB = "PARTIDA_CONTABLE", nombreApp = "partidaContable")
	private String partidaContable = "";
	@Columna(nombreDB = "NATURALEZA1", nombreApp = "naturaleza1")
	private String naturaleza1 = "D";
	@Columna(nombreDB = "REFERENCIA1", nombreApp = "referencia1")
	private String referencia1 = "N";
	@Columna(nombreDB = "TERCERO1", nombreApp = "tercero1")
	private String tercero1 = "N";
	@Columna(nombreDB = "CONTRATO1", nombreApp = "contrato1")
	private String contrato1 = "N";
	@Columna(nombreDB = "RIESGO_OPERACIONAL", nombreApp = "riesgoOperacional")
	private String riesgoOperacional = "N";
	@Columna(nombreDB = "CONTRAPARTIDA_CONTABLE", nombreApp = "contraPartidaContable")
	private String contraPartidaContable = "";
	@Columna(nombreDB = "NATURALEZA2", nombreApp = "naturaleza2")
	private String naturaleza2 = "H";
	@Columna(nombreDB = "REFERENCIA2", nombreApp = "referencia2")
	private String referencia2 = "N";
	@Columna(nombreDB = "TERCERO2", nombreApp = "tercero2")
	private String tercero2 = "N";
	@Columna(nombreDB = "CONTRATO2", nombreApp = "contrato2")
	private String contrato2 = "N";
	@Columna(nombreDB = "OBLIGATORIO", nombreApp = "obligatorio")
	private String obligatorio = "N";
	@Columna(nombreDB = "TIPO_DIVISA", nombreApp = "tipoDivisa")
	private String tipoDivisa = "L";
	@Columna(nombreDB = "ESTADO", nombreApp = "estado", esEstado = true)
	private String estado = "A";

	private PUC pucPartida;
	private PUC pucContraPartida;
	private String nombreConcepto;

	public Number getCodigo() {
		return codigo;
	}

	public void setCodigo(Number codigo) {
		this.codigo = codigo;
	}

	public Number getCodigoConcepto() {
		return codigoConcepto;
	}

	public void setCodigoConcepto(Number codigoConcepto) {
		this.codigoConcepto = codigoConcepto;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getActivar() {
		return activar;
	}

	public void setActivar(String activar) {
		this.activar = activar;
	}

	public String getPartidaContable() {
		return partidaContable;
	}

	public void setPartidaContable(String partidaContable) {
		this.partidaContable = partidaContable;
	}

	public String getNaturaleza1() {
		return naturaleza1;
	}

	public void setNaturaleza1(String naturaleza1) {
		this.naturaleza1 = naturaleza1;
	}

	public String getReferencia1() {
		return referencia1;
	}

	public void setReferencia1(String referencia1) {
		this.referencia1 = referencia1;
	}

	public String getTercero1() {
		return tercero1;
	}

	public void setTercero1(String tercero1) {
		this.tercero1 = tercero1;
	}

	public String getContrato1() {
		return contrato1;
	}

	public void setContrato1(String contrato1) {
		this.contrato1 = contrato1;
	}

	public String getRiesgoOperacional() {
		return riesgoOperacional;
	}

	public void setRiesgoOperacional(String riesgoOperacional) {
		this.riesgoOperacional = riesgoOperacional;
	}

	public String getContraPartidaContable() {
		return contraPartidaContable;
	}

	public void setContraPartidaContable(String contraPartidaContable) {
		this.contraPartidaContable = contraPartidaContable;
	}

	public String getNaturaleza2() {
		return naturaleza2;
	}

	public void setNaturaleza2(String naturaleza2) {
		this.naturaleza2 = naturaleza2;
	}

	public String getReferencia2() {
		return referencia2;
	}

	public void setReferencia2(String referencia2) {
		this.referencia2 = referencia2;
	}

	public String getTercero2() {
		return tercero2;
	}

	public void setTercero2(String tercero2) {
		this.tercero2 = tercero2;
	}

	public String getContrato2() {
		return contrato2;
	}

	public void setContrato2(String contrato2) {
		this.contrato2 = contrato2;
	}

	public String getObligatorio() {
		return obligatorio;
	}

	public void setObligatorio(String obligatorio) {
		this.obligatorio = obligatorio;
	}

	public String getTipoDivisa() {
		return tipoDivisa;
	}

	public void setTipoDivisa(String tipoDivisa) {
		this.tipoDivisa = tipoDivisa;
	}

	@Override
	public String getEstado() {
		return estado;
	}

	@Override
	public void setEstado(String estado) {
		this.estado = estado;
	}

	public PUC getPucPartida() {
		return pucPartida;
	}

	public void setPucPartida(PUC pucPartida) {
		this.pucPartida = pucPartida;
	}

	public PUC getPucContraPartida() {
		return pucContraPartida;
	}

	public void setPucContraPartida(PUC pucContraPartida) {
		this.pucContraPartida = pucContraPartida;
	}

	public String getNombreConcepto() {
		return nombreConcepto;
	}

	public void setNombreConcepto(String nombreConcepto) {
		this.nombreConcepto = nombreConcepto;
	}

	@Override
	public Object getPK() {
		return codigo;
	}

	@Override
	public void restartPK(Object pk) {
		codigo = Integer.valueOf(pk.toString());
	}

	@Override
	public String toString() {
		return "Tema{" +
				"codigo=" + codigo +
				", codigoConcepto=" + codigoConcepto +
				", nombre='" + nombre + '\'' +
				", activar='" + activar + '\'' +
				", partidaContable='" + partidaContable + '\'' +
				", naturaleza1='" + naturaleza1 + '\'' +
				", referencia1='" + referencia1 + '\'' +
				", tercero1='" + tercero1 + '\'' +
				", contrato1='" + contrato1 + '\'' +
				", riesgoOperacional='" + riesgoOperacional + '\'' +
				", contraPartidaContable='" + contraPartidaContable + '\'' +
				", naturaleza2='" + naturaleza2 + '\'' +
				", referencia2='" + referencia2 + '\'' +
				", tercero2='" + tercero2 + '\'' +
				", contrato2='" + contrato2 + '\'' +
				", obligatorio='" + obligatorio + '\'' +
				", tipoDivisa='" + tipoDivisa + '\'' +
				", estado='" + estado + '\'' +
				", pucPartida=" + pucPartida +
				", pucContraPartida=" + pucContraPartida +
				", nombreConcepto='" + nombreConcepto + '\'' +
				'}';
	}
}
