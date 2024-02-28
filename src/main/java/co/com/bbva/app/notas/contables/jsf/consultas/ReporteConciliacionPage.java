package co.com.bbva.app.notas.contables.jsf.consultas;

import co.com.bbva.app.notas.contables.carga.dto.Festivo;
import co.com.bbva.app.notas.contables.dto.Instancia;
import co.com.bbva.app.notas.contables.jsf.adminnota.FileGenerator;
import co.com.bbva.app.notas.contables.session.Session;
import co.com.bbva.app.notas.contables.util.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.model.SelectItem;
import javax.inject.Named;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.*;

@SessionScoped
@Named
public class ReporteConciliacionPage extends GeneralConsultaPage<Instancia> {

    private static final long serialVersionUID = -1692536682820509775L;
    private static final Logger LOGGER = LoggerFactory.getLogger(ReporteConciliacionPage.class);
    private static final String FORMAT_SELECT_MONTH = "yyyy-MM";
    private static final String FORMAT_DATE = "dd-MM-yy";
    private static final String INTERFAZ_CONTABLE_FILE_NAME = "CA_ReporteNotasCKR1_MNC.TXT";
    protected String excelFileName = "";
    protected boolean mostrarArchExc = false;
    protected List<SelectItem> anios = new ArrayList<>();
    private final FileGenerator fileGenerator;
//    private final Session session = getContablesSessionBean().getSessionTrace();
    private String anio = "";
    private String mes = "";

    @Override
    protected Collection<Instancia> _buscar() throws Exception {
        return new ArrayList<>();
    }

    protected String _getPage() {
        return REPORTE_CONCILIACION;
    }

    protected void _validar() throws Exception {
    }

    public ReporteConciliacionPage() {
        super();
        fileGenerator = new FileGenerator(notasContablesManager, cargaAltamiraManager,1);
//        fileGenerator = new FileGenerator(notasContablesManager, cargaAltamiraManager, getUsuarioLogueado().getUsuario().getCodigo().intValue());
    }
    
    @PostConstruct
    protected void _init() {
        super._init();
        anios = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();

        calendar.add(Calendar.MONTH, -3);
        Date firstDate = calendar.getTime();
        String firstMonthToSelect = new SimpleDateFormat(FORMAT_SELECT_MONTH).format(firstDate);

        calendar.add(Calendar.MONTH, 1);
        Date secondDate = calendar.getTime();
        String secondMonthToSelect = new SimpleDateFormat(FORMAT_SELECT_MONTH).format(secondDate);

        calendar.add(Calendar.MONTH, 1);
        Date thirdDate = calendar.getTime();
        String thirdMonthToSelect = new SimpleDateFormat(FORMAT_SELECT_MONTH).format(thirdDate);

        anios.add(new SelectItem(firstMonthToSelect, firstMonthToSelect));
        anios.add(new SelectItem(secondMonthToSelect, secondMonthToSelect));
        anios.add(new SelectItem(thirdMonthToSelect, thirdMonthToSelect));
    }

    public String generarArchivoExcel() {
        return null;
    }

    public String generarArchivoTXT() {
        try {
            String sDate1 = anio;
            String fechaValida = sDate1 + "-01";

            Date date_desde = new SimpleDateFormat("yyyy-MM-dd").parse(fechaValida);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date_desde);
            calendar.add(Calendar.MONTH, + 1);
            Date date_hasta = calendar.getTime();
            String fecha1String = new SimpleDateFormat(FORMAT_DATE).format(date_desde);
            String fecha2String = new SimpleDateFormat(FORMAT_DATE).format(date_hasta);

            Collection<Festivo> festivos = cargaAltamiraManager.getFestivos();
            Date fecha_actual = new Date();
            Date fecha_fin_validacion =   new Date(fecha_actual.getTime() + (1000 * 60 * 60 * 24));
            int cantFest = DateUtils.getFestivosEntre(fecha_actual, fecha_fin_validacion , new ArrayList<>(festivos));

            if (cantFest == 0) {
                String pattern = "k"; // El formato de fecha est especificado
                SimpleDateFormat simpleDate = new SimpleDateFormat(pattern); // La cadena de formato de fecha se pasa como un argumento al objeto
                String hour = simpleDate.format(fecha_actual); // El formato de fecha se aplica a la fecha actual

                if (Integer.valueOf(hour) <= 21) {
                    File directory = new File(DIR_TRANSMISION_ALTAMIRA);
                    deleteFiles("traceLog", directory);
                    fileGenerator.generarArchivoConciliacion(DIR_TRANSMISION_ALTAMIRA + INTERFAZ_CONTABLE_FILE_NAME,this.DIR_TRANSMISION_ALTAMIRA , fecha1String ,fecha2String );
                    LOGGER.info("{} Generar Archivo Conciliacin : {}", fecha1String +" "+ fecha2String );
                    nuevoMensaje(FacesMessage.SEVERITY_INFO, "Inicio el proceso y el archivo se ha  generado en: " + DIR_TRANSMISION_ALTAMIRA+ INTERFAZ_CONTABLE_FILE_NAME);
                } else {
                    nuevoMensaje(FacesMessage.SEVERITY_WARN, "La generacin de la conciliacin puede ejecutarse los das hábiles antes de las 10 pm, en caso contrario el procesamiento no ser exitoso.");
                }
            } else {
                nuevoMensaje(FacesMessage.SEVERITY_WARN, "La generacin de la conciliacin puede ejecutarse los das hábiles antes de las 10 pm, en caso contrario el procesamiento no ser exitoso.");
            }
        } catch (Exception e) {
//            LOGGER.error("{} Ocurrio un error al generar el archivo de Interfaz Contable", session.getTraceLog() , e);
            nuevoMensaje(FacesMessage.SEVERITY_ERROR, "Ocurrio un error al generar el archivo de Interfaz Contable");
        }
        return REPORTE_CONCILIACION;
    }

    private static void deleteFiles(String traceLog, File ruta) {
        LOGGER.info("{} Depurando el servidor de archivos previamente generados", traceLog);
        File[] archivo = ruta.listFiles();
        if (archivo != null) {
            for (int i = 0; i < archivo.length; i++) {
                File Arc = archivo[i];
                if (archivo[i].isDirectory()) {
                } else {
                    String nombre_archivo = archivo[i].getName();
                    if(nombre_archivo.contains(".ReporteNotasCKR1_MNC")){
                        archivo[i].delete();
                    }
                }
            }
        }
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
