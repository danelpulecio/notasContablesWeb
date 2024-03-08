package co.com.bbva.app.notas.contables.jsf.consultas;

import co.com.bbva.app.notas.contables.dto.Instancia;
import co.com.bbva.app.notas.contables.jsf.adminnota.FileGenerator;
import co.com.bbva.app.notas.contables.jsf.beans.ContablesSessionBean;
import co.com.bbva.app.notas.contables.session.Session;
import co.com.bbva.app.notas.contables.util.ReportesExcel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;

@SessionScoped
@Named
public class ReporteDocumentoSoportePage extends GeneralConsultaPage<Instancia> {

    private static final long serialVersionUID = -1692536682820509776L;

    private static final Logger LOGGER = LoggerFactory.getLogger(ReporteDocumentoSoportePage.class);

    //private final Session session = getContablesSessionBean().getSessionTrace();

    private static final String DOC_SOPORTE_FILE_NAME = "DOC_SOPORTE_MNC.TXT";

    private static final String FORMAT_DATE = "dd-MM-yy";

    protected Date desde;

    protected Date hasta;

    private final FileGenerator fileGenerator;

    protected boolean mostrarArchExc = false;

    protected String excelFileName = "DocumentoSoporte.xls";

    protected String style = "disabled ";

    protected Collection<Instancia> _buscar() throws Exception {
        return null;
    }

    protected String _getPage() {
        return REPORTE_DOCUMENTO_SOPORTE;
    }

    @Override
    protected void _validar() throws Exception {
        // not implemented
    }


    public ReporteDocumentoSoportePage() {
        super();
        LOGGER.info("usuario logueado: {}", 1);
//        fileGenerator = new FileGenerator(notasContablesManager, cargaAltamiraManager, 1);
        fileGenerator = new FileGenerator(notasContablesManager, cargaAltamiraManager, 1);
        LOGGER.info("usuario logueado ok: {}");

    }

    public String generarArchivoExcel() {

        try {

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

            DateFormat dateFormat_Desde = new SimpleDateFormat(FORMAT_DATE);
            String date_desde = dateFormat_Desde.format(desdeD);
            DateFormat dateFormat_hasta = new SimpleDateFormat(FORMAT_DATE);
            String date_hasta = dateFormat_hasta.format(hastaD);

            //excelFileName = "Documento_Soporte.xls";
            ReportesExcel reportesExcel = new ReportesExcel();
            reportesExcel.setNombreArchivo(excelFileName);
            reportesExcel.setStrPath(DIR_REPORTES_EXCEL);
            reportesExcel.getReporteDocumentoSoporte("DocumentoSoporte", date_desde ,date_hasta );


            mostrarArchExc = true;
            style = "";

            LOGGER.info("{} Generar Excel Documento soportes : {}", desdeD +" "+ hastaD );

            nuevoMensaje(FacesMessage.SEVERITY_INFO, "Inicio el proceso y el archivo se ha  generado en: " + DIR_REPORTES_EXCEL+ excelFileName);




        } catch (Exception e) {
            //LOGGER.error("{} Ocurrio un error al generar el archivo de Interfaz Contable", session.getTraceLog() , e);
            nuevoMensaje(FacesMessage.SEVERITY_ERROR, "Ocurrio un error al generar el archivo de Interfaz Contable");
        }
        return REPORTE_DOCUMENTO_SOPORTE;



    }

    public String generarArchivoExcelXCcuentas() {

        try {

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

            DateFormat dateFormat_Desde = new SimpleDateFormat(FORMAT_DATE);
            String date_desde = dateFormat_Desde.format(desdeD);
            DateFormat dateFormat_hasta = new SimpleDateFormat(FORMAT_DATE);
            String date_hasta = dateFormat_hasta.format(hastaD);

            excelFileName = "DocumentoSoporteXcuentas.xls";
            ReportesExcel reportesExcel = new ReportesExcel();
            reportesExcel.setNombreArchivo(excelFileName);
            reportesExcel.setStrPath(DIR_REPORTES_EXCEL);
            reportesExcel.getReporteDocumentoSoporteXCuentas("DocumentoSoporte", date_desde ,date_hasta );


            mostrarArchExc = true;
            style = "";

            //LOGGER.info("{} Generar Excel Documento soportes : {}", session.getTraceLog(), desdeD +" "+ hastaD );

            nuevoMensaje(FacesMessage.SEVERITY_INFO, "Inicio el proceso y el archivo se ha  generado en: " + DIR_REPORTES_EXCEL+ excelFileName);




        } catch (Exception e) {
            //LOGGER.error("{} Ocurrio un error al generar el archivo de Interfaz Contable", session.getTraceLog() , e);
            nuevoMensaje(FacesMessage.SEVERITY_ERROR, "Ocurrio un error al generar el archivo de Interfaz Contable");
        }
        return REPORTE_DOCUMENTO_SOPORTE;



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

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }

}
