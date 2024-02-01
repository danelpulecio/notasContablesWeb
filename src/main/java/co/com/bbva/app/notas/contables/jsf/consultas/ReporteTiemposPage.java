package co.com.bbva.app.notas.contables.jsf.consultas;

import co.com.bbva.app.notas.contables.dto.ActividadRealizada;
import co.com.bbva.app.notas.contables.dto.UsuarioModulo;
import co.com.bbva.app.notas.contables.session.Session;
import co.com.bbva.app.notas.contables.util.DateUtils;
import co.com.bbva.app.notas.contables.util.ReportesExcel;
import co.com.bbva.app.notas.contables.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.model.SelectItem;
import javax.inject.Named;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;

@SessionScoped
@Named
public class ReporteTiemposPage extends GeneralConsultaPage<UsuarioModulo> {

    enum MESES {
        ENERO, FEBRERO, MARZO, ABRIL, MAYO, JUNIO, JULIO, AGOSTO, SEPTIEMBRE, OCTUBRE, NOVIEMBRE, DICIEMBRE
    }

    private static final long serialVersionUID = -6709113217662690209L;

    private static final int inicio = Calendar.getInstance().get(Calendar.YEAR) - 2;
    private static final int fin = inicio + 30;

    protected String excelFileName = "";
    protected boolean mostrarArchExc = false;

    // variables para combos
    protected List<SelectItem> meses = new ArrayList<SelectItem>();
    protected List<SelectItem> anios = new ArrayList<SelectItem>();

    private String anio = "";

    private String mes = "";

    private static final Logger LOGGER = LoggerFactory.getLogger(ReporteTiemposPage.class);

//	Session session = getContablesSessionBean().getSessionTrace();


	public ReporteTiemposPage() {
		super();
	}

	@PostConstruct
	public void init() {
	    super._init();
	    objActual = new UsuarioModulo();
	    anios = new ArrayList<SelectItem>();
	    for (Integer i = inicio; i < fin; i++) {
	        anios.add(new SelectItem(i.toString(), i.toString()));
	    }
	    meses = new ArrayList<SelectItem>();
	    for (Integer i = 0; i < MESES.values().length; i++) {
	        meses.add(new SelectItem(i.toString(), MESES.values()[i].name()));
	    }
	}

	@Override
	protected Collection<UsuarioModulo> _buscar() throws Exception {
		
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(0);
		c.set(Calendar.YEAR, Integer.valueOf(anio));
		c.set(Calendar.MONTH, Integer.valueOf(mes)-1);
		Timestamp desde = new Timestamp(c.getTimeInMillis());
		List<UsuarioModulo> usuarios = new ArrayList<UsuarioModulo>(notasContablesManager.getTiemposPorUsuario(desde));
		setDatos(usuarios);
		return usuarios;
	}

	public String generarArchivoExcel() {
		try {
			ReportesExcel reportesExcel = new ReportesExcel();
			reportesExcel.setStrPath(DIR_REPORTES_EXCEL);
			reportesExcel.getReporteTiempos("Tiempos", getDatos());

			excelFileName = "REPORTE_TIEMPOS_" + StringUtils.getString(DateUtils.getTimestamp(), "ddMMyyyyhhmmss") + ".xls";
			reportesExcel.setNombreArchivo(excelFileName);
			reportesExcel.getReporteTiempos("REP_TIEMPOS", getDatos());
			LOGGER.info("{} Generar el archivo reporte tiempos: {}");
			nuevoMensaje(FacesMessage.SEVERITY_INFO, "El archivo de excel ha sido generado y guardado en " + DIR_REPORTES_EXCEL + excelFileName);
			mostrarArchExc = true;
		} catch (Exception e) {
			LOGGER.error("{} Ocurrio un error al generar el archivo de Excel: {}",e );
			lanzarError(e, "Ocurrio un error al generar el archivo de Excel ");
		}
		return null;
	}

	@Override
	protected void _validar() throws Exception {
	}

	@Override
	protected String _getPage() {
		return REPORTE_TIEMPOS;
	}

	public String obtenerListaDetalle() {
		try {
			objActual.setActividades(new ArrayList<ActividadRealizada>(notasContablesManager.getActividadesParaReporteTiempos(objActual.getCodigo().intValue())));
		} catch (Exception e) {
			LOGGER.error("{} Error consultando el detalle de tiempos para el usuario ", e);
			nuevoMensaje(FacesMessage.SEVERITY_ERROR, "Error consultando el detalle de tiempos para el usuario");
		}
		return null;
	}

	public List<SelectItem> getMeses() {
		return meses;
	}

	public void setMeses(List<SelectItem> meses) {
		this.meses = meses;
	}

	public List<SelectItem> getAnios() {
		return anios;
	}

	public void setAnios(List<SelectItem> anios) {
		this.anios = anios;
	}

	public String getAnio() {
		return anio;
	}

	public void setAnio(String anio) {
		this.anio = anio;
	}

	public String getMes() {
		return mes;
	}

	public void setMes(String mes) {
		this.mes = mes;
	}

	public String getExcelFileName() {
		return excelFileName;
	}

	public void setExcelFileName(String excelFileName) {
		this.excelFileName = excelFileName;
	}

	public boolean isMostrarArchExc() {
		return mostrarArchExc;
	}

	public void setMostrarArchExc(boolean mostrarArchExc) {
		this.mostrarArchExc = mostrarArchExc;
	}

}
