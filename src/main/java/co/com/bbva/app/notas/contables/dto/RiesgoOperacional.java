package co.com.bbva.app.notas.contables.dto;

import co.com.bbva.app.notas.contables.carga.dto.*;

import java.sql.Date;
import java.sql.Timestamp;

public class RiesgoOperacional extends CommonVO<RiesgoOperacional> implements java.io.Serializable {

	private static final long serialVersionUID = -6361802696095488112L;

	private Number codigo = 0;
	private Number codigoNotaContable = 0;
	private Number codigoTemaNotaContable = 0;
	private Number importeParcial = 0;
	private Number importeTotal = 0;
	private Date fechaEvento = null;
	
	private java.util.Date  fechaEventoPF = null;
	private java.util.Date  fechaFinEventoPF = null;
	private java.util.Date  fechaDescubrimientoEventoPF = null;
	private java.util.Date  fechaRecuperacionPF = null;
	
	
	@SuppressWarnings("unused")
	private final Timestamp fechaEventoTS = null;
	private String codigoTipoPerdida = "";
	private String codigoClaseRiesgo = "";
	private Date fechaDescubrimientoEvento = null;
	
	private String horaInicioEvento = ""; // se adiciona krb
	private String minutosInicioEvento = ""; // se adiciona krb
	private String horaFinalEvento = ""; // se adiciona krb
	private String horaTotalInicio = ""; // se adiciona krb

	private String horaTotalFin = ""; // se adiciona krb
	private String horaTotalDescubre = ""; // se adiciona krb
	private String horaTotalRecuperacion = "";

	private String minutosFinalEvento = ""; // se adiciona krb
	private String horaDescubrimiento = ""; // se adiciona krb
	private String minutosDescubrimiento = ""; // se adiciona krb
	private String horario = ""; // se adiciona krb
	private String horarioFinal = ""; // se adiciona krb
	private String horarioDescubre = ""; // se adiciona krb

	@SuppressWarnings("unused")
	private final Timestamp fechaDescubrimientoEventoTS = null;
	private final Timestamp fechaFinalEventoTS = null;
	private final Timestamp fechaRecuperacionTS = null;
	private String codigoProceso = "";
	private String codigoLineaOperativa = "";
	private String codigoProducto = "";

	// Campos para CCS1-63 CE025
	private String appRecuperacion = "";
	private String nombreOutSource = "";
	private String codigoSubProducto = "";
	private String codigoCanal = "";
	private String codigoSubCanal = "";
	private String codigoClaseRiesgoN2 = "";
	private String codigoClaseRiesgoN3 = "";
	private Date fechaRecuperacion = null;
	private String horaRecuperacion = "";
	private String minutosRecuperacion = "";
	private String horarioRecuperacion = "";
	private String tipoRecuperacion = "";

	private PerdidaOperacional tipoPerdida = new PerdidaOperacional();
	private ClaseRiesgo claseRiesgo = new ClaseRiesgo();
	private RiesgoOperacionalProceso proceso = new RiesgoOperacionalProceso();
	private RiesgoOperacionalLineaOperativa lineaOperativa = new RiesgoOperacionalLineaOperativa();
	private RiesgoOperacionalProducto producto = new RiesgoOperacionalProducto();

	// Campos para CCS1-63 CE025
	private Date fechaFinEvento = null;
	private RiesgoOperacionalSubProd subProductoAfectado = new RiesgoOperacionalSubProd();
	private Canal canal = new Canal();
	private SubCanal subCanal = new SubCanal();
	private JerarquiaOperacionSFC_N2 claseRiesgoN2 = new JerarquiaOperacionSFC_N2();
	private JerarquiaOperacionSFC_N3 claseRiesgoN3 = new JerarquiaOperacionSFC_N3();

	public Date getFechaEvento() {
		return fechaEvento;
	}

	public void setFechaEvento(Date fechaEvento) {
		this.fechaEvento = fechaEvento;
	}

	// se adiciona atributo para nueva adicion KRB

	public Timestamp getFechaFinalEventoTS() {
		return fechaFinalEventoTS;
	}

	// cambio

	public void setFechaFinalEventoTS(Timestamp FechaFinalEventoTS) {
		this.fechaFinEvento = new Date(FechaFinalEventoTS.getTime());
	}

	public String getHorario() {
		return horario;
	}

	public void setHorario(String horario) {

		this.horario = horario;

	}

	public String getHorarioFinal() {
		return horarioFinal;
	}

	public void setHorarioFinal(String horariofinal) {

		this.horarioFinal = horariofinal;

	}

	public String getHorarioDescubre() {
		return horarioDescubre;
	}

	public void setHorarioDescubre(String horarioDescubre) {

		this.horarioDescubre = horarioDescubre;

	}

	public String getHoraTotalInicio() {
		return horaTotalInicio;
	}

	public void setHoraTotalInicio(String horaTotalInicio) {
		this.horaTotalInicio = horaTotalInicio;
	}

	public String getHoraTotalFin() {
		return horaTotalFin;
	}

	public void setHoraTotalFin(String horaTotalFin) {
		this.horaTotalFin = horaTotalFin;
	}

	public String getHoraTotalDescubre() {
		return horaTotalDescubre;
	}

	public void setHoraTotalDescubre(String horaTotalDescubre) {
		this.horaTotalDescubre = horaTotalDescubre;
	}

	// fin adicion KRB

	/**
	 * @return the horaTotalRecuperacion
	 */
	public String getHoraTotalRecuperacion() {
		return horaTotalRecuperacion;
	}

	/**
	 * @param horaTotalRecuperacion the horaTotalRecuperacion to set
	 */
	public void setHoraTotalRecuperacion(String horaTotalRecuperacion) {
		this.horaTotalRecuperacion = horaTotalRecuperacion;
	}

	public Timestamp getFechaEventoTS() {
		return new Timestamp(fechaEvento.getTime());
	}

	public void setFechaEventoTS(Timestamp fechaEvento) {
		this.fechaEvento = new Date(fechaEvento.getTime());
	}

	public String getCodigoTipoPerdida() {
		return codigoTipoPerdida;
	}

	public void setCodigoTipoPerdida(String codigoTipoPerdida) {
		this.codigoTipoPerdida = codigoTipoPerdida;
	}

	public String getCodigoClaseRiesgo() {
		return codigoClaseRiesgo;
	}

	public void setCodigoClaseRiesgo(String codigoClaseRiesgo) {
		this.codigoClaseRiesgo = codigoClaseRiesgo;
	}

	public Date getFechaDescubrimientoEvento() {
		return fechaDescubrimientoEvento;
	}

	public void setFechaDescubrimientoEvento(Date fechaDescubrimientoEvento) {
		this.fechaDescubrimientoEvento = fechaDescubrimientoEvento;
	}

	public Timestamp getFechaDescubrimientoEventoTS() {
		return new Timestamp(fechaDescubrimientoEvento.getTime());
	}

	public void setFechaDescubrimientoEventoTS(Timestamp fechaDescubrimientoEvento) {
		this.fechaDescubrimientoEvento = new Date(fechaDescubrimientoEvento.getTime());
	}

	public String getCodigoProceso() {
		return codigoProceso;
	}

	public void setCodigoProceso(String codigoProceso) {
		this.codigoProceso = codigoProceso;
	}

	public String getCodigoLineaOperativa() {
		return codigoLineaOperativa;
	}

	public void setCodigoLineaOperativa(String codigoLineaOperativa) {
		this.codigoLineaOperativa = codigoLineaOperativa;
	}

	public String getCodigoProducto() {
		return codigoProducto;
	}

	public void setCodigoProducto(String codigoProducto) {
		this.codigoProducto = codigoProducto;
	}

	public PerdidaOperacional getTipoPerdida() {
		return tipoPerdida;
	}

	public void setTipoPerdida(PerdidaOperacional tipoPerdida) {
		this.tipoPerdida = tipoPerdida;
	}

	public ClaseRiesgo getClaseRiesgo() {
		return claseRiesgo;
	}

	public void setClaseRiesgo(ClaseRiesgo claseRiesgo) {
		this.claseRiesgo = claseRiesgo;
	}

	public RiesgoOperacionalProceso getProceso() {
		return proceso;
	}

	public void setProceso(RiesgoOperacionalProceso proceso) {
		this.proceso = proceso;
	}

	public RiesgoOperacionalLineaOperativa getLineaOperativa() {
		return lineaOperativa;
	}

	public void setLineaOperativa(RiesgoOperacionalLineaOperativa lineaOperativa) {
		this.lineaOperativa = lineaOperativa;
	}

	public RiesgoOperacionalProducto getProducto() {
		return producto;
	}

	public void setProducto(RiesgoOperacionalProducto producto) {
		this.producto = producto;
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

	public Number getCodigoTemaNotaContable() {
		return codigoTemaNotaContable;
	}

	public void setCodigoTemaNotaContable(Number codigoTemaNotaContable) {
		this.codigoTemaNotaContable = codigoTemaNotaContable;
	}

	public Number getImporteParcial() {
		return importeParcial;
	}

	public void setImporteParcial(Number importeParcial) {
		this.importeParcial = importeParcial;
	}

	public Number getImporteTotal() {
		return importeTotal;
	}

	public void setImporteTotal(Number importeTotal) {
		this.importeTotal = importeTotal;
	}

	public String getHoraInicioEvento() {
		return horaInicioEvento;
	}

	public void setHoraInicioEvento(String horaInicioEvento) {
		this.horaInicioEvento = horaInicioEvento;
	}

	public String getMinutosInicioEvento() {
		return minutosInicioEvento;
	}

	public void setMinutosInicioEvento(String minutosInicioEvento) {
		this.minutosInicioEvento = minutosInicioEvento;
	}

	public String getHoraFinalEvento() {
		return horaFinalEvento;
	}

	public void setHoraFinalEvento(String horaFinalEvento) {
		this.horaFinalEvento = horaFinalEvento;
	}

	public String getMinutosFinalEvento() {
		return minutosFinalEvento;
	}

	public void setMinutosFinalEvento(String minutosFinalEvento) {
		this.minutosFinalEvento = minutosFinalEvento;
	}

	public String getHoraDescubrimiento() {
		return horaDescubrimiento;
	}

	public void setHoraDescubrimiento(String horaDescubrimiento) {
		this.horaDescubrimiento = horaDescubrimiento;
	}

	public String getMinutosDescubrimiento() {
		return minutosDescubrimiento;
	}

	public void setMinutosDescubrimiento(String minutosDescubrimiento) {
		this.minutosDescubrimiento = minutosDescubrimiento;
	}

	/**
	 * @return the codigoSubProducto
	 */
	public String getCodigoSubProducto() {
		return codigoSubProducto;
	}

	/**
	 * @param codigoSubProducto the codigoSubProducto to set
	 */
	public void setCodigoSubProducto(String codigoSubProducto) {
		this.codigoSubProducto = codigoSubProducto;
	}

	/**
	 * @return the codigoCanal
	 */
	public String getCodigoCanal() {
		return codigoCanal;
	}

	/**
	 * @param codigoCanal the codigoCanal to set
	 */
	public void setCodigoCanal(String codigoCanal) {
		this.codigoCanal = codigoCanal;
	}

	/**
	 * @return the codigoSubCanal
	 */
	public String getCodigoSubCanal() {
		return codigoSubCanal;
	}

	/**
	 * @param codigoSubCanal the codigoSubCanal to set
	 */
	public void setCodigoSubCanal(String codigoSubCanal) {
		this.codigoSubCanal = codigoSubCanal;
	}

	/**
	 * @return the codigoClaseRiesgoN2
	 */
	public String getCodigoClaseRiesgoN2() {
		return codigoClaseRiesgoN2;
	}

	/**
	 * @param codigoClaseRiesgoN2 the codigoClaseRiesgoN2 to set
	 */
	public void setCodigoClaseRiesgoN2(String codigoClaseRiesgoN2) {
		this.codigoClaseRiesgoN2 = codigoClaseRiesgoN2;
	}

	/**
	 * @return the codigoClaseRiesgoN3
	 */
	public String getCodigoClaseRiesgoN3() {
		return codigoClaseRiesgoN3;
	}

	/**
	 * @param codigoClaseRiesgoN3 the codigoClaseRiesgoN3 to set
	 */
	public void setCodigoClaseRiesgoN3(String codigoClaseRiesgoN3) {
		this.codigoClaseRiesgoN3 = codigoClaseRiesgoN3;
	}

	/**
	 * @return the fechaFinEvento
	 */
	public Date getFechaFinEvento() {
		return fechaFinEvento;
	}

	/**
	 * @param fechaFinEvento the fechaFinEvento to set
	 */
	public void setFechaFinEvento(Date fechaFinEvento) {
		this.fechaFinEvento = fechaFinEvento;
	}

	/**
	 * @return the subProductoAfectado
	 */
	public RiesgoOperacionalSubProd getSubProductoAfectado() {
		return subProductoAfectado;
	}

	/**
	 * @param subProductoAfectado the subProductoAfectado to set
	 */
	public void setSubProductoAfectado(RiesgoOperacionalSubProd subProductoAfectado) {
		this.subProductoAfectado = subProductoAfectado;
	}

	/**
	 * @return the canal
	 */
	public Canal getCanal() {
		return canal;
	}

	/**
	 * @param canal the canal to set
	 */
	public void setCanal(Canal canal) {
		this.canal = canal;
	}

	/**
	 * @return the subCanal
	 */
	public SubCanal getSubCanal() {
		return subCanal;
	}

	/**
	 * @param subCanal the subCanal to set
	 */
	public void setSubCanal(SubCanal subCanal) {
		this.subCanal = subCanal;
	}

	/**
	 * @return the claseRiesgoN2
	 */
	public JerarquiaOperacionSFC_N2 getClaseRiesgoN2() {
		return claseRiesgoN2;
	}

	/**
	 * @param claseRiesgoN2 the claseRiesgoN2 to set
	 */
	public void setClaseRiesgoN2(JerarquiaOperacionSFC_N2 claseRiesgoN2) {
		this.claseRiesgoN2 = claseRiesgoN2;
	}

	/**
	 * @return the claseRiesgoN3
	 */
	public JerarquiaOperacionSFC_N3 getClaseRiesgoN3() {
		return claseRiesgoN3;
	}

	/**
	 * @param claseRiesgoN3 the claseRiesgoN3 to set
	 */
	public void setClaseRiesgoN3(JerarquiaOperacionSFC_N3 claseRiesgoN3) {
		this.claseRiesgoN3 = claseRiesgoN3;
	}

	

	/**
	 * @return the appRecuperacion
	 */
	public String getAppRecuperacion() {
		return appRecuperacion;
	}

	/**
	 * @param appRecuperacion the appRecuperacion to set
	 */
	public void setAppRecuperacion(String appRecuperacion) {
		this.appRecuperacion = appRecuperacion;
	}

	

	/**
	 * @return the nombreOutSource
	 */
	public String getNombreOutSource() {
		return nombreOutSource;
	}

	/**
	 * @param nombreOutSource the nombreOutSource to set
	 */
	public void setNombreOutSource(String nombreOutSource) {
		this.nombreOutSource = nombreOutSource;
	}

	/**
	 * @return the fechaRecuperacion
	 */
	public Date getFechaRecuperacion() {
		return fechaRecuperacion;
	}

	/**
	 * @param fechaRecuperacion the fechaRecuperacion to set
	 */
	public void setFechaRecuperacion(Date fechaRecuperacion) {
		this.fechaRecuperacion = fechaRecuperacion;
	}
	

	/**
	 * @return the horaRecuperacion
	 */
	public String getHoraRecuperacion() {
		return horaRecuperacion;
	}

	/**
	 * @param horaRecuperacion the horaRecuperacion to set
	 */
	public void setHoraRecuperacion(String horaRecuperacion) {
		this.horaRecuperacion = horaRecuperacion;
	}

	/**
	 * @return the minutosRecuperacion
	 */
	public String getMinutosRecuperacion() {
		return minutosRecuperacion;
	}

	/**
	 * @param minutosRecuperacion the minutosRecuperacion to set
	 */
	public void setMinutosRecuperacion(String minutosRecuperacion) {
		this.minutosRecuperacion = minutosRecuperacion;
	}

	/**
	 * @return the horarioRecuperacion
	 */
	public String getHorarioRecuperacion() {
		return horarioRecuperacion;
	}

	/**
	 * @param horarioRecuperacion the horarioRecuperacion to set
	 */
	public void setHorarioRecuperacion(String horarioRecuperacion) {
		this.horarioRecuperacion = horarioRecuperacion;
	}
	

	/**
	 * @return the tipoRecuperacion
	 */
	public String getTipoRecuperacion() {
		return tipoRecuperacion;
	}

	/**
	 * @param tipoRecuperacion the tipoRecuperacion to set
	 */
	public void setTipoRecuperacion(String tipoRecuperacion) {
		this.tipoRecuperacion = tipoRecuperacion;
	}
	
	public void setFechaRecuperacionTS(Timestamp fechaRecuperacionTS) {
		this.fechaRecuperacion = new Date(fechaRecuperacionTS.getTime());
	}
	

	/**
	 * @return the fechaRecuperacionTS
	 */
	public Timestamp getFechaRecuperacionTS() {
		return fechaRecuperacionTS;
	}

	@Override
	public Object getPK() {
		return codigo;
	}

	@Override
	public void restartPK(Object pk) {
		codigo = Integer.valueOf(pk.toString());
	}

	public java.util.Date getFechaEventoPF() {
		return fechaEventoPF;
	}

	public void setFechaEventoPF(java.util.Date fechaEventoPF) {
		this.fechaEventoPF = fechaEventoPF;
	}

	public java.util.Date getFechaFinEventoPF() {
		return fechaFinEventoPF;
	}

	public void setFechaFinEventoPF(java.util.Date fechaFinEventoPF) {
		this.fechaFinEventoPF = fechaFinEventoPF;
	}

	public java.util.Date getFechaDescubrimientoEventoPF() {
		return fechaDescubrimientoEventoPF;
	}

	public void setFechaDescubrimientoEventoPF(java.util.Date fechaDescubrimientoEventoPF) {
		this.fechaDescubrimientoEventoPF = fechaDescubrimientoEventoPF;
	}

	public java.util.Date getFechaRecuperacionPF() {
		return fechaRecuperacionPF;
	}

	public void setFechaRecuperacionPF(java.util.Date fechaRecuperacionPF) {
		this.fechaRecuperacionPF = fechaRecuperacionPF;
	}
	
}
