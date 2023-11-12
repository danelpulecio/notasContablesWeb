package co.com.bbva.app.notas.contables.jsf.adminnota;

import co.com.bbva.app.notas.contables.carga.dto.ErrorValidacion;
import co.com.bbva.app.notas.contables.carga.dto.RechazoSalida;
import co.com.bbva.app.notas.contables.dto.Instancia;
import co.com.bbva.app.notas.contables.dto.NotaContable;
import co.com.bbva.app.notas.contables.dto.NotaContableTotal;
import co.com.bbva.app.notas.contables.jsf.consultas.GeneralConsultaPage;
import co.com.bbva.app.notas.contables.session.Session;
import co.com.bbva.app.notas.contables.util.DateUtils;
import co.com.bbva.app.notas.contables.util.ReportesExcel;
import co.com.bbva.app.notas.contables.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.servlet.ServletContext;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@SessionScoped
@Named
public class PrecierreCierrePage extends GeneralConsultaPage<Instancia> {

	private static final long serialVersionUID = -6709113217662690209L;

	protected final ServletContext context;
	private final FileGenerator fileGenerator;
	private boolean esPrecierre;
	protected List<NotaContableTotal> totales;
	protected String excelFileName = "";
	protected boolean mostrarArchAlt = false;
	protected boolean mostrarArchExc = false;
	private String paginaActual = "";

	private static final String ARCHIVO_SIRO = "ARCHIVO_SIRO_NTCON.TXT";

	private static final Logger LOGGER = LoggerFactory.getLogger(PrecierreCierrePage.class);

	Session session = getContablesSessionBean().getSessionTrace();

	public PrecierreCierrePage() {
		super();
		context = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
		fileGenerator = new FileGenerator(notasContablesManager, cargaAltamiraManager, getUsuarioLogueado().getUsuario().getCodigo().intValue());
	}

	@Override
	protected void _init() {
		super._init();
	}

	public String mostrar() {

		String pagina = "";
		if (this.paginaActual.equals(ADMIN_PRECIERRE)) {
			pagina = ADMIN_PRECIERRE;
		}
		if (this.paginaActual.equals(ADMIN_CIERRE)) {
			pagina = ADMIN_CIERRE;
		}
		if (this.paginaActual.equals(REPORTE_GENERAL)) {
			pagina = REPORTE_GENERAL;
		}

		if (this.paginaActual.equals(CONSULTA_MOVIMIENTOS_CONTABLES)) {
			pagina = CONSULTA_MOVIMIENTOS_CONTABLES;
		}
		return pagina;
	}

	public void cargarRegistros(boolean esPrecierre) {

		LOGGER.info("{} cargarRegistros Precierre cierre: {}", session.getTraceLog(), (esPrecierre ? "en Precierre" : "cerradas") );
		this.esPrecierre = esPrecierre;
		try {
			Collection<NotaContable> notasContables = notasContablesManager.getNotaContablesPrecierreCierre(esPrecierre);
			ArrayList<Instancia> instancias = new ArrayList<Instancia>();
			for (NotaContable nc : notasContables) {
				String rechazo = "";
				Collection<RechazoSalida> res = notasContablesManager.getRechazoSalidaByNotaContable(nc.getNumeroRadicacion());
				nc.setRechazos(new ArrayList<RechazoSalida>(res));
				if (!res.isEmpty()) {
					rechazo = "<table><th>Cuenta</th><th>Divisa</th><th>Destino</th><th>Error</th>";
					for (RechazoSalida rs : res) {
						ErrorValidacion rv = new ErrorValidacion();
						rv.setCodigo(StringUtils.getStringLeftPadding(rs.getCodError().toString(), 4, '0'));
						rv = cargaAltamiraManager.getErrorValidacion(rv);
						rechazo += "<tr><td>" + rs.getCuenta() + "</td> <td>" + rs.getDivisa() + "</td> <td>" + rs.getCeDestin() + "</td> <td>" + rv.getNombre() + "</td> </tr>";
					}
					rechazo += "</table>";
				}
				nc.setCausalDeRechazo(rechazo);
				Instancia instancia = new Instancia();
				instancia.setCodigoNotaContable(nc.getCodigo());
				instancia = notasContablesManager.getInstanciaPorNotaContable(instancia);
				instancia.setNC(nc);
				instancias.add(instancia);
			}

			totales = notasContablesManager.getDatosDeInstancias(instancias, true);
			setDatos(instancias);
			this.paginaActual = ADMIN_PRECIERRE;

			if (getDatos().isEmpty() && !hayMensajes()) {
				LOGGER.info("{} No hay notas: {}", session.getTraceLog(), (esPrecierre ? "en Precierre" : "cerradas") );
				nuevoMensaje(FacesMessage.SEVERITY_INFO, "No hay notas " + (esPrecierre ? "en Precierre" : "cerradas"));
			}
		} catch (final Exception e) {
			LOGGER.error("{} Ocurrio un error al realizar la consulta de Precierre: {}", session.getTraceLog(), (esPrecierre ? "en Precierre" : "cerradas") , e );
			nuevoMensaje(FacesMessage.SEVERITY_ERROR, "Ocurrio un error al realizar la consulta de Precierre");
		}
	}

	public String generarArchivoExcel(String path, String file, String title, String paginaInvocacion) {
		try {
			LOGGER.info("{} Generar ArchivoExcel precierre cierre: {}", session.getTraceLog(), title );

			List<NotaContable> notas = new ArrayList<NotaContable>();
			excelFileName = file + StringUtils.getString(DateUtils.getTimestamp(), "ddMMyyyyhhmmss") + ".xls";
			for (Instancia i : getDatos()) {
				notas.add(i.getNC());
			}
			ReportesExcel reportesExcel = new ReportesExcel();
			reportesExcel.setStrPath(path);
			reportesExcel.setNombreArchivo(excelFileName);
			reportesExcel.getReporteGeneral(title, notas);

			mostrarArchExc = true;
			LOGGER.info("{} Crea archivo excel", session.getTraceLog() );
			nuevoMensaje(FacesMessage.SEVERITY_INFO, "El archivo de excel ha sido generado y guardado en " + path + excelFileName);

		} catch (Exception e) {
			LOGGER.info("{} Error creando archivo excel", session.getTraceLog() , e  );
			nuevoMensaje(FacesMessage.SEVERITY_ERROR, "Ocurrio un error al generar el archivo de Excel");
		}
		this.paginaActual = paginaInvocacion;
		return mostrar();
	}

	protected String generarArchivoAltamira(String path, String interfaz, String puc, String terceros, String indicaPc, String paginaInvocacion) {
		try {
			fileGenerator.generarInterfazContable(path + interfaz, getDatos(),this.DIR_TRANSMISION_ALTAMIRA);
			fileGenerator.generarPUC(path + puc,this.DIR_TRANSMISION_ALTAMIRA);
			fileGenerator.generarTerceros(path + terceros,this.DIR_TRANSMISION_ALTAMIRA);
			if (!esPrecierre) {
				LOGGER.info("{} Crea archivo altamria cierre", session.getTraceLog() );
				fileGenerator.generarIndicaCierre(path + indicaPc);
			}else {
				LOGGER.info("{} Crea archivo altamria precierre", session.getTraceLog() );
				fileGenerator.generarIndicaPrecierre(path + indicaPc);
			}
			mostrarArchAlt = true;

			generarArchivoSiro(DIR_SIRO, ARCHIVO_SIRO, ADMIN_CIERRE);
			nuevoMensaje(FacesMessage.SEVERITY_INFO, "Los archivos se han generado correctamente");

		} catch (Exception e) {
			LOGGER.error("{} Error crando archivo altamria", session.getTraceLog() , e );
			nuevoMensaje(FacesMessage.SEVERITY_INFO, "Ocurrio un error al generar el archivo de Interfaz Contable");
		}
		this.paginaActual = paginaInvocacion;
		return mostrar();
	}

	/**
	 * Permite generar el archivo plano correspondiente a siro
	 * @param path
	 * @param nombre
	 * @param paginaInvocacion
	 * @return
	 */
	protected String generarArchivoSiro(String path, String nombre, String paginaInvocacion) {
		try {
			fileGenerator.generarArchivoSiro(path + nombre,this.DIR_TRANSMISION_ALTAMIRA);
			LOGGER.info("{} Generar Archivo Siro", session.getTraceLog() );
			nuevoMensaje(FacesMessage.SEVERITY_INFO, "El archivo plano para SIRO se ha generado correctamente" );

		} catch (Exception e) {
			LOGGER.error("{} currio un error al generar el archivo de Siro", session.getTraceLog() , e );
			nuevoMensaje(FacesMessage.SEVERITY_INFO, "Ocurrio un error al generar el archivo de Siro");
		}
		this.paginaActual = paginaInvocacion;
		return mostrar();
	}

	@Override
	protected String _getPage() {
		return mostrar();
	}

	@Override
	protected Collection<Instancia> _buscar() throws Exception {
		return new ArrayList<Instancia>();
	}

	@Override
	protected void _validar() throws Exception {
	}

	public List<NotaContableTotal> getTotales() {
		return totales;
	}

	public void setTotales(List<NotaContableTotal> totales) {
		this.totales = totales;
	}

	public boolean isMostrarArchAlt() {
		return mostrarArchAlt;
	}

	public void setMostrarArchAlt(boolean mostrarArchAlt) {
		this.mostrarArchAlt = mostrarArchAlt;
	}

	public boolean isMostrarArchExc() {
		return mostrarArchExc;
	}

	public void setMostrarArchExc(boolean mostrarArchExc) {
		this.mostrarArchExc = mostrarArchExc;
	}

	public String getExcelFileName() {
		return excelFileName;
	}

	public void setExcelFileName(String excelFileName) {
		this.excelFileName = excelFileName;
	}

	public boolean isEsPrecierre() {
		return esPrecierre;
	}

	public void setEsPrecierre(boolean esPrecierre) {
		this.esPrecierre = esPrecierre;
	}

}
