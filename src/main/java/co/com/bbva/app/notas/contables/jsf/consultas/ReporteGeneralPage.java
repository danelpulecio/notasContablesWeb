package co.com.bbva.app.notas.contables.jsf.consultas;

import co.com.bbva.app.notas.contables.carga.dto.Divisa;
import co.com.bbva.app.notas.contables.carga.dto.Sucursal;
import co.com.bbva.app.notas.contables.dto.*;
import co.com.bbva.app.notas.contables.jsf.adminnota.PrecierreCierrePage;
import co.com.bbva.app.notas.contables.session.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.model.SelectItem;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.*;

public class ReporteGeneralPage extends GeneralConsultaPage<Instancia> {

	
	private PrecierreCierrePage precierreCierrePage;

	private static final long serialVersionUID = -6709113217662690209L;

	@PostConstruct
	public void init() throws Exception {
		sucursales = getSelectItemList(notasContablesManager.getCV(Sucursal.class));
		setSucursales(new ArrayList<>(sucursales));
		divisas = getSelectItemList(notasContablesManager.getCV(Divisa.class));
		setDivisas(new ArrayList<>(divisas));

	}

	// variables para combos
	private List<SelectItem> sucursales = new ArrayList<SelectItem>();
	protected List<SelectItem> conceptos = new ArrayList<SelectItem>();
	protected List<SelectItem> temas = new ArrayList<SelectItem>();
	protected List<SelectItem> tiposEvento = new ArrayList<SelectItem>();
	protected List<SelectItem> divisas = new ArrayList<SelectItem>();

	// variables para guardar los datos del filtro
	protected String tipoNota;
	protected String sucOrigen;
	protected String sucDestino;
	protected Integer concepto;
	protected Integer tema;
	protected Integer tipoEvento;
	protected String divisa;
	protected String partida;
	protected String numIdentificacion;
	protected Date desde;
	protected Date hasta;
	protected String estado;
	protected String descripcion;

	protected List<NotaContableTotal> totales = new ArrayList<NotaContableTotal>();

	private static final Logger LOGGER = LoggerFactory.getLogger(ReporteGeneralPage.class);

//	Session session = getContablesSessionBean().getSessionTrace();

	/**
	 * indica el numero de pagina del scroller de actividades
	 */
	private Integer scrollPageAct = 1;

	public ReporteGeneralPage() {
		super();
	}

	@Override
	protected void _init() {
		super._init();
		if (sucursales == null || sucursales.isEmpty()) {
			try {
				sucursales = getSelectItemList(notasContablesManager.getCV(Sucursal.class));
				LOGGER.info("Init sucursales if {}", sucursales.size());
				conceptos = getSelectItemList(notasContablesManager.getCV(Concepto.class), false);
				temas = new ArrayList<SelectItem>();
				tiposEvento = getSelectItemList(notasContablesManager.getCV(TipoEvento.class), false);
				divisas = getSelectItemList(notasContablesManager.getCV(Divisa.class));
			} catch (Exception e) {
//				LOGGER.error("{} EError inicializando filtro de busqueda", session.getTraceLog() ,e);
				nuevoMensaje(FacesMessage.SEVERITY_ERROR, "Error inicializando filtro de busqueda ");
			}
		}
	}

	/**
	 * Permite generar un archivo excel para reporte general
	 * 
	 * @return
	 */
	public String generarArchivoExcel() {
		PrecierreCierrePage bean = precierreCierrePage;
		try {
			for (Instancia ins : getDatos()) {
				NotaContable nc = new NotaContable();
				nc.setCodigo(ins.getCodigoNotaContable());
				nc = notasContablesManager.getNotaContable(nc);
				ins.setNC(nc);
			}
			bean.setDatos(getDatos());
			//System.out.println("getPage --> " + _getPage());
			LOGGER.info("getPage --> " + _getPage());
			return bean.generarArchivoExcel(DIR_REPORTES_EXCEL, "NC_PRECIERRE_", "Reporte", REPORTE_GENERAL);
		} catch (Exception e) {
			LOGGER.error("{} Error generando el reporte Excel",e);
			nuevoMensaje(FacesMessage.SEVERITY_ERROR, "Error generando el reporte Excel");
		}
		return null;
	}

	/**
	 * Permite generar un archivo excel para consulta de movimiento contable
	 * 
	 * @return
	 */
	public String generarArchivoExcelMovimiento() {
		PrecierreCierrePage bean = precierreCierrePage;
//		PrecierreCierrePage bean = new PrecierreCierrePage();
		LOGGER.info(":: DIR_REPORTES_EXCEL :: " + DIR_REPORTES_EXCEL );
		try {
			for (Instancia ins : getDatos()) {
				NotaContable nc = new NotaContable();
				nc.setCodigo(ins.getCodigoNotaContable());
				nc = notasContablesManager.getNotaContable(nc);
				ins.setNC(nc);
			}
			bean.setDatos(getDatos());
			return bean.generarArchivoExcel(DIR_REPORTES_EXCEL, "NC_PRECIERRE_", "Reporte", CONSULTA_MOVIMIENTOS_CONTABLES);
		} catch (Exception e) {
			LOGGER.error("{} Error generando el reporte ExcelMovimiento",e);
			nuevoMensaje(FacesMessage.SEVERITY_ERROR, "Error generando el reporte ExcelMovimiento");
		}
		return null;
	}

	@Override
	protected Collection<Instancia> _buscar() throws Exception {

		Collection<Instancia> instancias = new ArrayList<Instancia>();
		java.sql.Date desdeD = desde != null ? new java.sql.Date(desde.getTime()) : null;
		java.sql.Date hastaD = hasta != null ? new java.sql.Date(hasta.getTime()) : null;
		if (hastaD != null) {
			Calendar c = Calendar.getInstance();
			c.setTime(hastaD);
			c.set(Calendar.DATE, c.get(Calendar.DATE) + 1);
			c.set(Calendar.HOUR, 0);
			c.set(Calendar.MINUTE, 0);
			c.set(Calendar.MILLISECOND, 0);
			hastaD.setTime(c.getTimeInMillis());
		}
		
		if (sucOrigen == null) {
			sucOrigen = "";
		}

		if (tipoNota.trim().equals("")) {
//			LOGGER.info("{} Busca Instancias All ", session.getTraceLog() );
			instancias = notasContablesManager.getInstanciasPor(sucOrigen, sucDestino, concepto, tema, tipoEvento, partida, numIdentificacion, desdeD, hastaD, divisa, estado,
					descripcion, getUsuarioLogueado().getSucursal().getCodigo(), getUsuarioLogueado().getRolActual().getCodigo().intValue());
			instancias.addAll(notasContablesManager.getInstanciasRegLibrePor(sucOrigen, sucDestino, partida, numIdentificacion, desdeD, hastaD, divisa, estado, descripcion,
					getUsuarioLogueado().getSucursal().getCodigo(), getUsuarioLogueado().getRolActual().getCodigo().intValue()));
			instancias.addAll(notasContablesManager.getInstanciasCruceRefPor(sucOrigen, sucDestino, partida, desdeD, hastaD, divisa, estado,
					getUsuarioLogueado().getSucursal().getCodigo(), getUsuarioLogueado().getRolActual().getCodigo().intValue()));
		} else {
			if (tipoNota.trim().equals(NotaContable.NORMAL)) {
//				LOGGER.info("{} Busca Instancias NORMAL ", session.getTraceLog() );
				instancias = notasContablesManager.getInstanciasPor(sucOrigen, sucDestino, concepto, tema, tipoEvento, partida, numIdentificacion, desdeD, hastaD, divisa, estado,
						descripcion, getUsuarioLogueado().getSucursal().getCodigo(), getUsuarioLogueado().getRolActual().getCodigo().intValue());
			} else if (tipoNota.trim().equals(NotaContable.LIBRE)) {
//				LOGGER.info("{} Busca Instancias LIBRE ", session.getTraceLog() );
				instancias = notasContablesManager.getInstanciasRegLibrePor(sucOrigen, sucDestino, partida, numIdentificacion, desdeD, hastaD, divisa, estado, descripcion,
						getUsuarioLogueado().getSucursal().getCodigo(), getUsuarioLogueado().getRolActual().getCodigo().intValue());
			} else {// if (tipoNota.equals(NotaContable.CRUCE_REFERENCIA)) {
//				LOGGER.info("{} Busca Instancias CRUCE REFERENCIA ", session.getTraceLog() );
				instancias = notasContablesManager.getInstanciasCruceRefPor(sucOrigen, sucDestino, partida, desdeD, hastaD, divisa, estado,
						getUsuarioLogueado().getSucursal().getCodigo(), getUsuarioLogueado().getRolActual().getCodigo().intValue());
			}
		}
		totales = notasContablesManager.getDatosDeInstancias(instancias, true);
		return instancias;
	}

	public String buscarTemas() {
		temas = new ArrayList<SelectItem>();
		if (concepto.intValue() > 0) {
			try {
				Tema row = new Tema();
				row.setCodigoConcepto(concepto.intValue());
				for (Tema t : notasContablesManager.getTemasPorConcepto(row)) {
					temas.add(new SelectItem(t.getCodigo(), t.getNombre()));
				}
			} catch (Exception e) {
//				LOGGER.error("{} Error Consultando los temas asociados al concepto seleccionado ", session.getTraceLog(), e);
				nuevoMensaje(FacesMessage.SEVERITY_ERROR, "Error Consultando los temas asociados al concepto seleccionado");

			}
		}
		return null;
	}

	@Override
	protected void _validar() throws Exception {
	}

	@Override
	protected String _getPage() {
		return REPORTE_GENERAL;
	}

	public List<SelectItem> getSucursales() {
		LOGGER.info("getSucursales {}", sucursales.size());
		return sucursales;
	}

	public void setSucursales(List<SelectItem> sucursales) {
		LOGGER.info("SetSucursales");
		this.sucursales = sucursales;
	}

	public List<SelectItem> getConceptos() {
		return conceptos;
	}

	public void setConceptos(List<SelectItem> conceptos) {
		this.conceptos = conceptos;
	}

	public List<SelectItem> getTemas() {
		return temas;
	}

	public void setTemas(List<SelectItem> temas) {
		this.temas = temas;
	}

	public List<SelectItem> getTiposEvento() {
		return tiposEvento;
	}

	public void setTiposEvento(List<SelectItem> tiposEvento) {
		this.tiposEvento = tiposEvento;
	}

	public List<SelectItem> getDivisas() {
		LOGGER.info("Divisa {}", divisas.size());
		return divisas;
	}

	public void setDivisas(List<SelectItem> divisas) {
		this.divisas = divisas;
	}

	public String getSucOrigen() {
		LOGGER.info("getSucOrigen {}", sucOrigen);
		return sucOrigen;
	}

	public void setSucOrigen(String sucOrigen) {
		this.sucOrigen = sucOrigen == null ? "" : sucOrigen;
		LOGGER.info("setSucOrigen {}", sucOrigen);
	}

	public String getSucDestino() {
		LOGGER.info("getSucDestino {}", sucDestino);
		return sucDestino;
	}

	public void setSucDestino(String sucDestino) {
		this.sucDestino = sucDestino == null ? "" : sucDestino;
		LOGGER.info("setSucDestino {}", sucDestino);
	}

	public Integer getConcepto() {
		return concepto;
	}

	public void setConcepto(Integer concepto) {
		this.concepto = concepto;
	}

	public Integer getTema() {
		return tema;
	}

	public void setTema(Integer tema) {
		this.tema = tema;
	}

	public Integer getTipoEvento() {
		return tipoEvento;
	}

	public void setTipoEvento(Integer tipoEvento) {
		this.tipoEvento = tipoEvento;
	}

	public String getDivisa() {
		return divisa;
	}

	public void setDivisa(String divisa) {
		this.divisa = divisa == null ? "" : divisa;
	}

	public String getPartida() {
		return partida;
	}

	public void setPartida(String partida) {
		this.partida = partida == null ? "" : partida;
	}

	public String getNumIdentificacion() {
		return numIdentificacion;
	}

	public void setNumIdentificacion(String numIdentificacion) {
		this.numIdentificacion = numIdentificacion == null ? "" : numIdentificacion;
	}

	public Date getDesde() {
		return desde;
	}

	public void setDesde(Date desde) {
		this.desde = desde;
	}

	public Date getHasta() {
		return hasta;
	}

	public void setHasta(Date hasta) {
		this.hasta = hasta;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado == null ? "" : estado;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion == null ? "" : descripcion;
	}

	public String getTipoNota() {
		return tipoNota;
	}

	public void setTipoNota(String tipoNota) {
		this.tipoNota = tipoNota == null ? "" : tipoNota;
	}

}
