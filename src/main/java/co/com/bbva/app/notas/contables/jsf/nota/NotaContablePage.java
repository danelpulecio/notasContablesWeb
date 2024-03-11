package co.com.bbva.app.notas.contables.jsf.nota;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.NumberFormat;
import java.util.*;

import javax.annotation.PreDestroy;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.servlet.ServletContext;

import org.primefaces.PrimeFaces;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.file.UploadedFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import co.com.bbva.app.notas.contables.carga.dto.Cliente;
import co.com.bbva.app.notas.contables.carga.dto.Contrato;
import co.com.bbva.app.notas.contables.carga.dto.Divisa;
import co.com.bbva.app.notas.contables.carga.dto.RiesgoOperacionalLineaOperativa;
import co.com.bbva.app.notas.contables.carga.dto.RiesgoOperacionalProceso;
import co.com.bbva.app.notas.contables.carga.dto.RiesgoOperacionalProducto;
import co.com.bbva.app.notas.contables.carga.dto.Sucursal;
import co.com.bbva.app.notas.contables.carga.dto.Tercero;
import co.com.bbva.app.notas.contables.carga.dto.TipoIdentificacion;
import co.com.bbva.app.notas.contables.dto.Anexo;
import co.com.bbva.app.notas.contables.dto.Canal;
import co.com.bbva.app.notas.contables.dto.CentroEspecial;
import co.com.bbva.app.notas.contables.dto.Concepto;
import co.com.bbva.app.notas.contables.dto.FechaHabilitada;
import co.com.bbva.app.notas.contables.dto.Instancia;
import co.com.bbva.app.notas.contables.dto.MontoMaximo;
import co.com.bbva.app.notas.contables.dto.NotaContable;
import co.com.bbva.app.notas.contables.dto.NotaContableTema;
import co.com.bbva.app.notas.contables.dto.NotaContableTemaImpuesto;
import co.com.bbva.app.notas.contables.dto.NotaContableTotal;
import co.com.bbva.app.notas.contables.dto.RiesgoOperacional;
import co.com.bbva.app.notas.contables.dto.Tema;
import co.com.bbva.app.notas.contables.dto.TemaProducto;
import co.com.bbva.app.notas.contables.dto.TipoEvento;
import co.com.bbva.app.notas.contables.dto.UsuarioModulo;
import co.com.bbva.app.notas.contables.util.DateUtils;
import co.com.bbva.app.notas.contables.util.StringUtils;

/**
 * <p>
 * Pagina para manejar la administración de parametros relacionados con la entidad TipoEvento
 * </p>
 */
@ViewScoped
@Named
public class NotaContablePage extends FlujoNotaContablePage implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = LoggerFactory.getLogger(NotaContablePage.class);
    private static int numArchivo = 1;
    private List<SelectItem> conceptos = new ArrayList<>();
    private List<SelectItem> tiposEvento = new ArrayList<>();
    private List<SelectItem> tiposEventoTemporal = new ArrayList<>();
    private List<SelectItem> divisas = new ArrayList<>();
    private List<SelectItem> sucursalesPartida = new ArrayList<>();
    private List<SelectItem> sucursalesContraPartida = new ArrayList<>();
    private List<SelectItem> tiposDoc = new ArrayList<>();
    private List<SelectItem> contratos1 = new ArrayList<>();
    private List<SelectItem> contratos2 = new ArrayList<>();
    private List<SelectItem> clasesRiesgo = new ArrayList<>();
    private List<SelectItem> perdidaOper = new ArrayList<>();
    private List<SelectItem> procesos = new ArrayList<>();
    private List<SelectItem> productos = new ArrayList<>();
    private List<SelectItem> lineasOperativas = new ArrayList<>();
    private List<SelectItem> horas = new ArrayList<>();
    private List<SelectItem> minutos = new ArrayList<>();
    private List<SelectItem> horario = new ArrayList<>();

    // Campos para CCS1-63 CE025
    private List<SelectItem> subProductos = new ArrayList<>();
    private List<SelectItem> canales = new ArrayList<>();
    private List<SelectItem> subcanales = new ArrayList<>();
    private List<SelectItem> clasesRiesgoN2 = new ArrayList<>();
    private List<SelectItem> clasesRiesgoN3 = new ArrayList<>();

    private Collection<TemaProducto> productosTema = new ArrayList<>();
    private ArrayList<Date> diasNoHabiles = new ArrayList<>();
    private Collection<MontoMaximo> montos = new ArrayList<>();
    private String minFecha = "";
    private String maxFecha = "";
    private Double montoAlertaCOP = 0d;
    private Double montoAlertaEXT = 0d;
    private Double montoAnt = 0d;
    private String concepto = "";
    // banderas para controlar el cierre de popups
    private boolean ocultarPopupTema = false;
    private boolean ocultarPopupRiesgo = false;
    private boolean ocultarPopupAnular = false;
    private boolean aplicaRecuperacion = false;
    //private Session session;
    private java.util.Date fechaContablePF = null;

    public NotaContablePage() {
        super();
        this.fechaContablePF = new Date();
    }

    public String iniciarPagina() {
        //session = getContablesSessionBean().getSessionTrace();
        try {
            LOGGER.info("{} Registro de NOTA CONTABLE - Configurando valores iniciales");
            Sucursal sucursal = getUsuarioLogueado().getSucursal();
            LOGGER.info("{} Obteniendo Info. de la sucursal {}", sucursal.getCodigo());
            String msg = notasContablesManager.verificarUsuarioSubGerente(sucursal.getCodigo());
            if (msg.isEmpty()) {
                LOGGER.info("{} Obteniendo Info. de los montos mximos");
                montos = notasContablesManager.getMontoMaximos();
                for (MontoMaximo monto : montos) {
                    if (monto.getEstado().equals("A") && monto.getDivisa().equals("1")) {
                        montoAlertaCOP = monto.getMontoMaximoAlerta();
                    } else if (monto.getEstado().equals("A")) {
                        montoAlertaEXT = monto.getMontoMaximoAlerta();
                    }
                }
                LOGGER.info("{} Obteniendo Info. de los das no hábiles");
                diasNoHabiles = new ArrayList<>(cargaAltamiraManager.getFestivosFecha());

                FechaHabilitada fechaHabilitada = new FechaHabilitada();
                fechaHabilitada.setCodigoSucursal(getUsuarioLogueado().getSucursal().getCodigo());
                fechaHabilitada = notasContablesManager.getFechaHabilitadaPorSucursal(fechaHabilitada);

                LOGGER.info("{} Estableciendo fechas lmite");
                maxFecha = StringUtils.getString(DateUtils.getSQLDate(DateUtils.getNextWorkDay(diasNoHabiles)), "dd-MM-yyyy");
                minFecha = StringUtils.getString(DateUtils.getDateTodayBeforeDays(fechaHabilitada.getDias().intValue(), diasNoHabiles), "dd-MM-yyyy");

                LOGGER.info("{} Obteniendo Info. de los tipos de evento");
                tiposEventoTemporal=getSelectItemList(notasContablesManager.getCV(TipoEvento.class), false);
//                tiposEvento = getSelectItemList(notasContablesManager.getCV(TipoEvento.class), false);
                for (SelectItem tipoEvento : tiposEventoTemporal){
                    tiposEvento.add(new SelectItem(String.valueOf(tipoEvento.getValue()),tipoEvento.getLabel()));
                }

                concepto = "";
                conceptos = new ArrayList<>();
                if (nota.getCodigo().intValue() > 0) {
                    LOGGER.info("{} Validando Info. de la Nota Contable");
                    consultarFlujo();
                    seleccionarConcepto();
                    nota.setPuedeEditar(true);
                    nota.setPuedeAnular(true);
                    conceptos.add(new SelectItem(nota.getConcepto().getCodigo(), nota.getConcepto().getNombre()));
                }
            } else {
                LOGGER.warn("{} No se tiene parametrizado un autorizador");
                nuevoMensaje(FacesMessage.SEVERITY_WARN, msg);
            }
        } catch (Exception e) {
            LOGGER.error("{} Error al intentar configurar los valores iniciales: NC", e);
            nuevoMensaje(FacesMessage.SEVERITY_ERROR, "Error: Hubo un problema, la aplicación no se pudo iniciar correctamente");
        }
        return null;
    }

    /**
     * Funcion llamada al iniciar el flujo de creacion de notas contables
     *
     * @return
     */
    @Override
    public String iniciar() {
        return NOTA_CONTABLE;
    }

    public String buscarConcepto() {
        LOGGER.info("{} Busqueda de Conceptos");
        conceptos = new ArrayList<>();
        try {
            Sucursal sucursal = getUsuarioLogueado().getSucursal();
            LOGGER.info("{} Obtener conceptos - Filtro: {}", concepto);
            TreeSet<Concepto> set = new TreeSet<>(notasContablesManager.searchConcepto(concepto, "A"));
            CentroEspecial centroEspecial = getUsuarioLogueado().getCentroEspecial();
            LOGGER.info("{} Procensando {} Conceptos", set.size());
            for (Concepto row : set) {
                boolean indView = false;
                if (row.getCentrosAutSucursales().equals("S") && sucursal.getTipoCentro().equals("O")) {
                    indView = true;
                } else if (row.getCentrosAutAreasCentrales().equals("S") && sucursal.getTipoCentro().equals("C")) {
                    indView = true;
                } else if (row.getCentrosAutCentroEspecial().equals("S") && centroEspecial.getCodigo().intValue() != 0) {
                    indView = true;
                }
                if (indView) {
                    conceptos.add(new SelectItem(row.getCodigo().toString(), row.getNombre()));
                }
            }
            LOGGER.info("{} Se encontraron {} Conceptos", conceptos.size());
        } catch (Exception e) {
            LOGGER.error("{} Error intentando consultar los conceptos", e);
            nuevoMensaje(FacesMessage.SEVERITY_ERROR, "Error: Hubo un problema consultando los conceptos");
        }
        return null;
    }

    public String seleccionarConcepto() {
        try {
            LOGGER.info("{} Procesando el concepto seleccionado");
            Concepto concepto = new Concepto();
            LOGGER.info("nota conceptoSt: {}", nota.getCodigoConceptoSt());
            LOGGER.info("nota concepto: {}", nota.getCodigoConcepto());
            LOGGER.info("nota concepto class: {}", nota.getCodigoConcepto().getClass());
            LOGGER.info("nota: {}", nota);
            Integer temporalCodConcepto = Integer.parseInt(String.valueOf(nota.getCodigoConcepto()));
            nota.setCodigoConcepto(temporalCodConcepto);
            LOGGER.info("nota concepto parse: {}", nota.getCodigoConcepto());
            LOGGER.info("nota concepto parse class: {}", nota.getCodigoConcepto().getClass());
            concepto.setCodigo(nota.getCodigoConcepto());
            LOGGER.info("{} Obteniendo Info. del concepto: {}", concepto.getCodigo().intValue());
            concepto = notasContablesManager.getConcepto(concepto);
            nota.setConcepto(concepto);
            LOGGER.info("{} Consultando los temas del concepto: {} - {}", nota.getConcepto().getCodigo(), nota.getConcepto().getNombre());
            temaActual = new NotaContableTema();
            if (nota.getCodigo().intValue() <= 0) {
                totalesNota = new ArrayList<>();
                nota.setCodigoSucursalOrigen(getUsuarioLogueado().getUsuario().getCodigoAreaModificado());
                Tema t = new Tema();
                t.setCodigoConcepto(nota.getCodigoConcepto());
                t.setEstado("A");
                LOGGER.info("{} Obteniendo los temas activos");
                Collection<Tema> temas = notasContablesManager.getTemasPorConceptoYEstado(t);
                temasNotaContable = new ArrayList<>();
                LOGGER.info("{} Procesando y configurando {} temas", temas.size());
                int cod = -1;
                for (Tema tema : temas) {
                    NotaContableTema temaNota = new NotaContableTema();
                    crearNotaContableTema(temaNota, tema, cod--, maxFecha);
                    temasNotaContable.add(temaNota);
                }
                nota.setPuedeEditar(true);
            } else {
                int cod = -1;
                for (NotaContableTema tema : temasNotaContable) {
                    temaActual = tema;
                    codigo = tema.getCodigo().intValue();
                    if (codigo > 0) {
                        verNota();
                    } else {
                        crearNotaContableTema(tema, tema.getTema(), cod--, maxFecha);
                    }
                }
                temaActual = new NotaContableTema();
            }
        } catch (Exception e) {
            LOGGER.error("{} Error al intentar procesar los temas del concepto seleccionado", e);
            nuevoMensaje(FacesMessage.SEVERITY_ERROR, "Error: Hubo un problema al consultar los temas del concepto seleccionado");
        }
        return null;
    }

    /**
     * Funcion llamada cuando se desea inciar la edicion o creacion de registros de tipo NotaContable
     *
     * @return
     */
    public String editar() {
        LOGGER.info("{} Inicio de la edicin del tema seleccionado: {}", codigo);
        try {
            LOGGER.info("{} Validando el tema a editar");
            for (NotaContableTema nct : temasNotaContable) {
                if (nct.getCodigo().intValue() == codigo) {
                    temaActual = nct;
                    if (temaActual.getCodigo().intValue() > 0 && !temaActual.isEditada()) {
                        verNota();
                        if (temaActual.getTema().getRiesgoOperacional().equals("S")) {
                            cargarModalRiesgo();
                        }
                        if (temaActual.getTema().getTercero1().equals("S")) {
                            buscarTercero1();
                        }
                        if (temaActual.getTema().getTercero2().equals("S")) {
                            buscarTercero2();
                        }
                    }
                    LOGGER.info("{} Obteniendo Info. y estableciendo valores inciales");
                    tiposDoc = getSelectItemList(notasContablesManager.getCV(TipoIdentificacion.class), false);
                    cargarSucursales();
                    cargarDivisas();
                    productosTema = notasContablesManager.getProductosPorTema(temaActual.getTema().getCodigo().intValue());
                    break;
                }
            }
        } catch (Exception e) {
            LOGGER.error("{} Error al intentar editar el tema seleccionado", e);
            nuevoMensaje(FacesMessage.SEVERITY_ERROR, "Error: Hubo un problema al intentar editar el tema");
        }
        return null;
    }

    private void cargarSucursales() throws Exception {
        sucursalesPartida = new ArrayList<>();
        sucursalesContraPartida = new ArrayList<>();
        String sucUsuario = getUsuarioLogueado().getSucursal().getCodigo();

        boolean sinSucPartida = temaActual.getCodigoSucursalDestinoPartida() == null || temaActual.getCodigoSucursalDestinoPartida().isEmpty();
        boolean sinSucContraPartida = temaActual.getCodigoSucursalDestinoContraPartida() == null || temaActual.getCodigoSucursalDestinoContraPartida().isEmpty();

        // si no ha indicado datos de sucursal origen y destino

        // si el concepto lo indica, se consultan las sucursales
        if (nota.getConcepto().getOrigenDestino().equals("N")) {
            Collection<Sucursal> sucursales = cargaAltamiraManager.getSucursales();
            for (Sucursal s : sucursales) {
                if (cargaAltamiraManager.isSucursalValidaPUCDestino(s, temaActual.getTema().getPucPartida())) {
                    // sucursal por defecto para partida
                    if (sinSucPartida && s.getCodigo().equals(sucUsuario)) {
                        temaActual.setCodigoSucursalDestinoPartida(sucUsuario);
                    }
                    sucursalesPartida.add(new SelectItem(s.getCodigo(), s.getCodigo() + " " + s.getNombre()));
                }
                if (cargaAltamiraManager.isSucursalValidaPUCDestino(s, temaActual.getTema().getPucContraPartida())) {
                    // sucursal por defecto para contrapartida
                    if (sinSucContraPartida && s.getCodigo().equals(sucUsuario)) {
                        temaActual.setCodigoSucursalDestinoContraPartida(sucUsuario);
                    }
                    sucursalesContraPartida.add(new SelectItem(s.getCodigo(), s.getCodigo() + " " + s.getNombre()));
                }
            }
        } else if (sinSucPartida && sinSucContraPartida) {
            // cuando el origen es igual al destino, se asigna la misma sucursal del usuario
            temaActual.setCodigoSucursalDestinoPartida(getUsuarioLogueado().getUsuario().getCodigoAreaModificado());
            temaActual.setCodigoSucursalDestinoContraPartida(getUsuarioLogueado().getUsuario().getCodigoAreaModificado());
        }
    }

    /**
     * Carga las divisas a mostrar dependiendo del tipo de tema
     *
     * @throws Exception
     */
    private void cargarDivisas() throws Exception {
        divisas = new ArrayList<>();
        Collection<Divisa> divMap = cargaAltamiraManager.getDivisas();
        String tipoDivisa = temaActual.getTema().getTipoDivisa();
        for (Divisa div : divMap) {
            String codDiv = div.getCodigo();
            boolean indDivisa = false;
            // si el tema indica que no es multidivisa
            if (!tipoDivisa.equals("M")) {
                // si el tema indica que es local y la divisa es de tipo pesos
                if (tipoDivisa.equals("L") && codDiv.equals("COP")) {
                    indDivisa = true;
                }
                // si el tema indica que es moneda extranjera y la divisa es diferente a pesos
                // colombianos
                else if (tipoDivisa.equals("E") && !codDiv.equals("COP")) {
                    indDivisa = true;
                }
            } else {
                // si es multidivisa se permite cualquier tipo
                indDivisa = true;
            }
            // si la partida contable no es nula
            String tipoPartida = temaActual.getTema().getPartidaContable().isEmpty() ? "" : temaActual.getTema().getPartidaContable().substring(0, 1);
            String tipoContraPartida = temaActual.getTema().getContraPartidaContable().isEmpty() ? "" : temaActual.getTema().getContraPartidaContable().substring(0, 1);
            if ((tipoPartida.equals("4") || tipoPartida.equals("5") || tipoContraPartida.equals("4") || tipoContraPartida.equals("5")) && !tipoDivisa.equals("L")) {
                if (codDiv.equals("COD")) {
                    indDivisa = true;
                } else {
                    indDivisa = false;
                }
            }
            if (indDivisa) {
                divisas.add(new SelectItem(codDiv, codDiv + " " + div.getNombre()));
            }
        }
    }

    public String validarFecha() {

        java.sql.Date fecha = new java.sql.Date(DateUtils.getDate(maxFecha, "dd-MM-yyyy").getTime());
        Date fechaActual = new Date();
        java.sql.Date fechaAct = DateUtils.getSQLDate(fechaActual);
        //temaActual.setFechaContable(DateUtils.getSQLDate(temaActual.getFechaContablePF()));
        temaActual.setFechaContable(DateUtils.getSQLDate(this.fechaContablePF));

        if (temaActual.getFechaContable().after(DateUtils.getSQLDate(fechaAct))) {
            nuevoMensaje(FacesMessage.SEVERITY_WARN, "La fecha no puede ser superior a HOY");
            temaActual.setFechaContable(fecha);
        } else if (diasNoHabiles.contains(temaActual.getFechaContable())) {
            nuevoMensaje(FacesMessage.SEVERITY_WARN, "La fecha debe ser un dia hábil");
            temaActual.setFechaContable(fecha);
        } else if (temaActual.getFechaContable().after(DateUtils.getDate(fecha))) {
            nuevoMensaje(FacesMessage.SEVERITY_WARN, "La fecha no puede ser superior a " + StringUtils.getString(fecha, "yyyy-MM-dd"));
            temaActual.setFechaContable(fecha);
        }
        return null;
    }

    public String cambiarSucursalPartida() {
        LOGGER.info("{} Cambio de Sucursal Destino - Partida");
        if (cambiarSucursal(temaActual.getCodigoSucursalDestinoPartida(), sucursalesContraPartida)) {
            LOGGER.info("{} Cambio Auto. Sucursal Destino - Contrapartida");
            temaActual.setCodigoSucursalDestinoContraPartida(temaActual.getCodigoSucursalDestinoPartida());
        }
        return null;
    }

    public String cambiarSucursalContraPartida() {
        LOGGER.info("{} Cambio de Sucursal Destino - Contrapartida");
        cambiarSucursal(temaActual.getCodigoSucursalDestinoContraPartida(), sucursalesPartida);
        return null;
    }

    public String cambiarContratoPartida() {
        LOGGER.info("{} Cambio del Contrato - Partida");
        if (temaActual.getNumeroIdentificacion1().equals(temaActual.getNumeroIdentificacion2())) {
            LOGGER.info("{} Cambio Auto. Contrato - Contrapartida");
            temaActual.setContrato2(temaActual.getContrato1());
        }
        return null;
    }

    public String cambiarContratoContraPartida() {
        LOGGER.info("{} Cambio del Contrato - Contrapartida");
        if (temaActual.getNumeroIdentificacion1().equals(temaActual.getNumeroIdentificacion2())) {
            LOGGER.info("{} Cambio Auto. Contrato - Partida");
            temaActual.setContrato1(temaActual.getContrato2());
        }
        return null;
    }

    private boolean cambiarSucursal(String sucursal, List<SelectItem> sucursales) {
        LOGGER.info("{} Validando si el cambio de sucursal aplica para la partida y contrapartida");
        String partida = temaActual.getTema().getPartidaContable().substring(0, 2);
        String contraPartida = temaActual.getTema().getContraPartidaContable().substring(0, 2);
        LOGGER.info("{} Verificando si es válido el cambio de sucursal: {} ", sucursal);
        if (partida.equals("61") && contraPartida.equals("62") || partida.equals("62") && contraPartida.equals("61")) {
            return true;
        }
        if (partida.equals("63") && contraPartida.equals("64") || partida.equals("64") && contraPartida.equals("63")) {
            return true;
        }
        if (partida.equals("81") && contraPartida.equals("83") || partida.equals("83") && contraPartida.equals("81")) {
            return true;
        }
        if (partida.equals("82") && contraPartida.equals("84") || partida.equals("84") && contraPartida.equals("82")) {
            return true;
        }
        for (SelectItem item : sucursales) {
            if (item.getValue().toString().equals(sucursal)) {
                return true;
            }
        }
        return false;
    }

    public String ajustarCargaImpositiva() {
        LOGGER.info("{} Clculo del impuesto correspondiente");
        for (NotaContableTemaImpuesto imp : temaActual.getImpuestoTema()) {
            imp.setBase(temaActual.getMonto().doubleValue());
            imp.setCalculado(imp.getBase() * imp.getImpuestoTema().getImpuesto().getValor() / 100);
            if (imp.getBoolExonera()) {
                imp.setExonera("S");
                imp.setCalculado(0);
                imp.setBase(0);
            } else {
                imp.setExonera("N");
            }
        }

        Double maCOP = getMontoAlertaCOP();
        Double maEXt = getMontoAlertaEXT();
        String divisa = getTemaActual().getCodigoDivisa();
        LOGGER.info("<<<<<<<<<<MONTO ALERTA COP: {}>>>>>>>>>>", maCOP);
        LOGGER.info("<<<<<<<<<<MONTO ALERTA EXT: {}>>>>>>>>>>", maEXt);
        LOGGER.info("<<<<<<<<<<CODIGO DIVISA: {}>>>>>>>>>>", divisa);
        LOGGER.info("<<<<<<<<<<divisa: {}>>>>>>>>>>", divisas.size());
        LOGGER.info("<<<<<<<<<<divisa value: {}>>>>>>>>>>", divisas.get(0).getValue());

        return null;
    }

    public String cargarModalRiesgo() {
        try {
            char partida = temaActual.getTema().getPartidaContable().charAt(0);
            char contrapartida = temaActual.getTema().getContraPartidaContable().charAt(0);

            if (partida == '4' || contrapartida == '4') {
                aplicaRecuperacion = true;
            } else {
                aplicaRecuperacion = false;
                temaActual.getRiesgoOperacional().setAppRecuperacion("N");
            }
            clasesRiesgo = getSelectItemList(cargaAltamiraManager.getCVClasRiesPorCuenta(temaActual.getTema().getPartidaContable()), false);

            if (temaActual.getRiesgoOperacional().getCodigoClaseRiesgo() == null || temaActual.getRiesgoOperacional().getCodigoClaseRiesgo().isEmpty()) {
                perdidaOper = new ArrayList<>();

            } else {
                buscarPerdidasCuenta();
            }

            horas = new ArrayList<>();

            for (int i = 1; i <= 12; i++) {
                String horast = Integer.toString(i);

                if (i < 10) {
                    horast = "0" + horast;
                }
                horas.add(new SelectItem(horast, horast));
            }

            minutos = new ArrayList<>();

            for (int j = 0; j <= 59; j++) {
                String mint = Integer.toString(j);

                if (j < 10) {
                    mint = "0" + mint;
                }
                minutos.add(new SelectItem(mint, mint));
            }

            // horario
            horario = new ArrayList<>();
            horario.add(new SelectItem("A.M", "A.M"));
            horario.add(new SelectItem("P.M", "P.M"));

            procesos = getSelectItemList(notasContablesManager.getCV(RiesgoOperacionalProceso.class), false);
            productos = getSelectItemList(notasContablesManager.getCV(RiesgoOperacionalProducto.class), false);

            if (temaActual.getRiesgoOperacional().getCodigoProducto() != null) {
                buscarSubProducto();
            }

            lineasOperativas = getSelectItemList(notasContablesManager.getCV(RiesgoOperacionalLineaOperativa.class), false);

            canales = getSelectItemList(notasContablesManager.getCV(Canal.class), false);
            if (temaActual.getRiesgoOperacional().getCodigoCanal() != null) {
                buscarSubCanal();
            }

            if (temaActual.getRiesgoOperacional().getClaseRiesgo() != null) {
                buscarClaseRiesgoN2();
            }

            if (temaActual.getRiesgoOperacional().getClaseRiesgoN2() != null) {
                buscarClaseRiesgoN3();
            }

        } catch (Exception e) {
            lanzarError(e, "Error al inicializar el editor de riesgo operacional para la nota actual");
        }
        return null;
    }

    public String buscarPerdidasCuenta() {
        try {
            perdidaOper = getSelectItemList(
                    cargaAltamiraManager.getCVPerdidaOper(temaActual.getTema().getPartidaContable(), temaActual.getRiesgoOperacional().getCodigoClaseRiesgo()), false);

            clasesRiesgoN2 = getSelectItemList(notasContablesManager.findByClaseN2(temaActual.getRiesgoOperacional().getCodigoClaseRiesgo()), false);
        } catch (Exception e) {
            lanzarError(e, "Error al buscar el listado de perdidas operacionales");
        }
        return null;
    }

    /**
     * Permite obtner la lista de subProductos dependiendo de un producto
     *
     * @return
     */
    public String buscarSubProducto() {
        try {
            subProductos = getSelectItemList(notasContablesManager.findByProducto(temaActual.getRiesgoOperacional().getCodigoProducto()), false);
        } catch (Exception e) {
            lanzarError(e, "Error al buscar el listado de subProductos operacionales");
        }
        return null;
    }

    /**
     * Permite obtener la lista de subcanales por medio del codigo del canal
     *
     * @return
     */
    public String buscarSubCanal() {
        try {
            subcanales = getSelectItemList(notasContablesManager.findByCanal(temaActual.getRiesgoOperacional().getCodigoCanal()), false);
        } catch (Exception e) {
            lanzarError(e, "Error al buscar el listado de subCanales");
        }
        return null;
    }

    /**
     * Permite obtner el listado de clase riesgo N2 dependiendo de la clase de riesgo
     * @return
     */
    public String buscarClaseRiesgoN2() {
        try {
            clasesRiesgoN2 = getSelectItemList(notasContablesManager.findByClaseN2(temaActual.getRiesgoOperacional().getCodigoClaseRiesgo()), false);
        } catch (Exception e) {
            lanzarError(e, "Error al buscar el listado de clase riesgo N2");
        }
        return null;
    }


    /**
     * Permite obtner el listado de clase riesgo N3 dependiendo de la clase de riesgo N3
     * @return
     */
    public String buscarClaseRiesgoN3() {
        try {
            clasesRiesgoN3 = getSelectItemList(notasContablesManager.findByClaseN3(temaActual.getRiesgoOperacional().getCodigoClaseRiesgoN2(), temaActual.getRiesgoOperacional().getCodigoClaseRiesgo()), false);
        } catch (Exception e) {
            lanzarError(e, "Error al buscar el listado de clase riesgo N3");
        }
        return null;
    }

    public String buscarTercero1() {
        buscarTercero(temaActual.getTipoIdentificacion1(), temaActual.getNumeroIdentificacion1(), true);
        return null;
    }

    public String buscarTercero2() {
        buscarTercero(temaActual.getTipoIdentificacion2(), temaActual.getNumeroIdentificacion2(), false);
        return null;
    }

    private void buscarTercero(String tipo, String numero, boolean tercero1) {
        try {
            LOGGER.info("{} Intentando consultar tercero: {}", numero);
            numero = StringUtils.getStringLeftPaddingFixed(numero, 15, '0');
            String numTercero = "";
            String dvTercero = "";
            String nombreTercero = "";
            List<SelectItem> items = new ArrayList<>();

            LOGGER.info("{} Buscando en Clientes");
            Cliente cliente = new Cliente();
            cliente.setTipoIdentificacion(tipo);
            cliente.setNumeroIdentificacion(numero);
            cliente = cargaAltamiraManager.getClientePorTipoYNumeroIdentificacion(cliente);

            LOGGER.info("{} Verificando si se encontró Info. en clientes");
            if (!cliente.getNumeroIdentificacion().equals("")) {
                LOGGER.info("{} Estableciendo propiedades del tercero:cliente");
                numTercero = cliente.getNumeroIdentificacion();
                dvTercero = cliente.getDigitoVerificacion();
                nombreTercero = cliente.getPrimerNombre().trim() + " " + cliente.getPrimerApellido().trim() + " " + cliente.getSegundoApellido().trim();

                LOGGER.info("{} Obteniedo los contratos asociados al tercero: {}", cliente.getNumeroCliente());
                if (tercero1 && temaActual.getTema().getContrato1().equals("S") || !tercero1 && temaActual.getTema().getContrato2().equals("S")) {
                    Contrato contrato = new Contrato();
                    contrato.setNumeroCliente(cliente.getNumeroCliente());
                    Collection<Contrato> contratos = cargaAltamiraManager.getContratosPorCliente(contrato);

                    for (Contrato c : contratos) {
                        String codProducto = c.getNumeroContrato().substring(8, 10);
                        if (productosTema.isEmpty() || notasContablesManager.isProductoIncluido(productosTema, codProducto)) {
                            items.add(new SelectItem(c.getNumeroContrato(), c.getNumeroContrato()));
                        }
                    }
                }
            } else {
                LOGGER.info("{} No se encontró Info. en Clientes - Buscando en Terceros");
                Tercero tercero = new Tercero();
                tercero.setTipoIdentificacion(tipo);
                tercero.setNumeroIdentificacion(numero);
                tercero = cargaAltamiraManager.getTerceroPorTipoYNumeroIdentificacion(tercero);

                LOGGER.info("{} Verificando si se encontró Info. en terceros");
                if (!tercero.getTipoIdentificacion().equals("")) {
                    LOGGER.info("{} Estableciendo propiedades del tercero:tercero");
                    numTercero = tercero.getNumeroIdentificacion();
                    dvTercero = tercero.getDigitoVerificacion();
                    nombreTercero = tercero.getPrimerNombre().trim() + " " + tercero.getPrimerApellido().trim() + " " + tercero.getSegundoApellido().trim();
                } else {
                    LOGGER.warn("{} No se encontró Info. del tercero consultado: {}", numero);
                    nuevoMensaje(FacesMessage.SEVERITY_WARN, "No se encontró ningún cliente ni tercero con la combinación tipo y número de identificación");
                }
            }
            LOGGER.info("{} Estableciendo atributos del tercero");
            if (tercero1) {
                temaActual.setNumeroIdentificacion1(numTercero);
                temaActual.setDigitoVerificacion1(dvTercero);
                temaActual.setNombreCompleto1(nombreTercero);
                if (tercero1 && temaActual.getTema().getContrato1().equals("S")) {
                    contratos1 = items;
                }
                if (temaActual.getTema().getTercero2().equals("S") && temaActual.getNombreCompleto2().isEmpty()) {
                    temaActual.setTipoIdentificacion2(temaActual.getTipoIdentificacion1());
                    temaActual.setNumeroIdentificacion2(numTercero);
                    temaActual.setDigitoVerificacion2(dvTercero);
                    temaActual.setNombreCompleto2(nombreTercero);
                    if (temaActual.getTema().getContrato2().equals("S")) {
                        contratos2 = items;
                    }
                }
            } else {
                temaActual.setNumeroIdentificacion2(numTercero);
                temaActual.setDigitoVerificacion2(dvTercero);
                temaActual.setNombreCompleto2(nombreTercero);
                if (!tercero1 && temaActual.getTema().getContrato2().equals("S")) {
                    contratos2 = items;
                }
            }
        } catch (Exception e) {
            LOGGER.error("{} Error intentando buscar el tercero: {}", numero, e);
            nuevoMensaje(FacesMessage.SEVERITY_ERROR, "Error: Hubo un problema al buscar el tercero con identificación " + numero);
        }
    }

    /**
     * Metodo encargado de cargar los archivos para cada tema
     *
     * @param event
     * @throws Exception
     */
    public void listener(FileUploadEvent event) throws Exception {
        LOGGER.info("{} Iniciando la carga del archivo (SOPORTE)");

        String fileName = "";

        try {
            UploadedFile uploadedFile = event.getFile();
            String originalFileName = uploadedFile.getFileName();
            String fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
            long currentTime = System.currentTimeMillis();

            String prefijo = getNumArchivo() + "_";
            fileName = (prefijo + currentTime + fileExtension).toUpperCase();
            String filePath = DIR_SOPORTES + fileName;

            LOGGER.info("{} Preparando datos del soporte para subir el archivo: {}", originalFileName);
            LOGGER.info("{} Path de carga del soporte: {}", filePath);

            try (InputStream is = uploadedFile.getInputStream();
                 FileOutputStream fos = new FileOutputStream(filePath)) {
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = is.read(buffer)) != -1) {
                    fos.write(buffer, 0, bytesRead);
                }
            }

            LOGGER.info("{} Carga del soporte completada");

            Anexo anexo = new Anexo();
            anexo.setArchivo(fileName);
            anexo.setNombre(originalFileName);
            anexo.setCodigoUsuario(getCodUsuarioLogueado());
            anexo.setFechaHora(new Timestamp(currentTime));
            anexo.setBorrar(false);
            anexo.setEstadoInstancia("1");
            anexo.setUsuNombre(getUsuarioLogueado().getUsuAltamira().getNombreEmpleado());
            temaActual.getAnexoTema().add(anexo);

            nuevoMensaje(FacesMessage.SEVERITY_INFO, "Successful: El archivo : " + DIR_SOPORTES + fileName + " se subio correctamente.");

        } catch (Exception e) {
            LOGGER.error("{} Error al intentar cargar el archivo (SOPORTE): {}", fileName, e);
            nuevoMensaje(FacesMessage.SEVERITY_ERROR, "Error: Hubo un problema al cargar el archivo: " + DIR_SOPORTES + fileName);
            throw e;
        }
    }

//	public void listener(UploadEvent event) throws Exception {
//		LOGGER.info("{} Iniciando la carga del archivo (SOPORTE)");
//		String fileName = "";
//		try {
//			long time = new Date().getTime();
//			UploadItem item = event.getUploadItem();
//			LOGGER.info("{} Preparando datos del soporte para subir el archivo: {}", item.getFileName());
//			String prefijo = getNumArchivo() + "_";
//			fileName = (prefijo + time + item.getFileName().substring(item.getFileName().lastIndexOf("."))).toUpperCase();
//			File localFile = new File(DIR_SOPORTES + fileName);
//
//			LOGGER.info("{} Path de carga del soporte: {}", localFile.getPath());
//			final InputStream is = new FileInputStream(item.getFile());
//			FileOutputStream fos = new FileOutputStream(localFile);
//			int count = 0;
//			int length = 1024;
//			final byte[] info = new byte[length];
//			LOGGER.info("{} Procesando la carga del soporte");
//			while (is.read(info) != -1) {
//				fos.write(info);
//				count += length;
//			}
//			is.close();
//			fos.close();
//			item.getFile().delete();
//
//			LOGGER.info("{} Creacin del anexo para el soporte cargado");
//			Anexo anexo = new Anexo();
//			anexo.setArchivo(fileName);
//			anexo.setNombre(item.getFileName());
//			anexo.setCodigoUsuario(getCodUsuarioLogueado());
//			anexo.setFechaHora(new Timestamp(new Date().getTime()));
//			anexo.setBorrar(false);
//			anexo.setEstadoInstancia("1");
//			anexo.setUsuNombre(getUsuarioLogueado().getUsuAltamira().getNombreEmpleado());
//			temaActual.getAnexoTema().add(anexo);
//		} catch (Exception e) {
//			LOGGER.error("{} Error al intentar cargar el archivo (SOPORTE): {}", fileName, e);
//			nuevoMensaje(FacesMessage.SEVERITY_ERROR, "Error: Hubo un problema al cargar el archivo: " + DIR_SOPORTES + fileName);
//			throw e;
//		}
//	}


    public String borrarAnexo() {
        LOGGER.info("{} Eliminando el soporte cargado y el registro del anexo");

        for (Anexo anexo : temaActual.getAnexoTema()) {
            if (anexo.getBorrar()) {
                temaActual.getAnexoTema().remove(anexo);
                ServletContext context = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
                File f = new File(context.getRealPath(anexo.getArchivo()));
                f.delete();
                nuevoMensaje(FacesMessage.SEVERITY_INFO, "El soporte " + anexo.getNombre() + " se ha borrado correctamente");
                return null;
            }
        }
        return null;
    }

    public String guardarNota() {
        try {
            LOGGER.info("{} Intentando grabar la Nota Contable");
            LOGGER.info("{} Procesando validaciones y Persistiendo Info.");
            if (validarNota()) {
                if (validarTotales()) {
                    nota.setTipoNota("R");
                    if (nota.getCodigo().intValue() > 0) {
                        notasContablesManager.updateNotaContableRegistro(nota, temasNotaContable, totalesNota, getCodUsuarioLogueado());
                        aprobar();
                    } else {
                        LOGGER.info("{} Creando registro de la Instancia en base de datos");
                        int idInstancia = notasContablesManager.crearInstanciaNotaContable(nota, temasNotaContable, new ArrayList<>(), new ArrayList<>(), totalesNota, new ArrayList<>(), getCodUsuarioLogueado());
                        LOGGER.info("<<<<<<<<paso>>>>>>>>");
                        if (idInstancia != 0) {
                            LOGGER.info("{} Obteniendo Info. de Instancia & Nota Contable");
                            Instancia instancia = new Instancia();
                            instancia.setCodigo(idInstancia);
                            instancia = notasContablesManager.getInstancia(instancia);

                            NotaContable notaContable = new NotaContable();
                            notaContable.setCodigo(instancia.getCodigoNotaContable().intValue());
                            notaContable = notasContablesManager.getNotaContable(notaContable);
                            LOGGER.info("{} El registro de la NOTA CONTABLE: {} fue exitoso", notaContable.getNumeroRadicacion());
                            cancelarNota();
                            nuevoMensaje(FacesMessage.SEVERITY_INFO, "La nota ha sido radicada correctamente con el número: " + notaContable.getNumeroRadicacion());
                            UsuarioModulo usuarioModulo = new UsuarioModulo();
                            try {
                                LOGGER.info("{} Intentando enviar email de la nota grabada");
                                notasContablesManager.sendMail(instancia, getUsuarioLogueado());
                            } catch (Exception e) {
                                LOGGER.error("{} Error al intentar enviar el email de la nota grabada", e);
                                nuevoMensaje(FacesMessage.SEVERITY_ERROR, "Error: Hubo un problema al enviar el correo electronico a " + usuarioModulo.getEMailModificado());
                            }
                        } else {
                            LOGGER.error("{} Error al crear la instancia de la Nota Contable");
                            nuevoMensaje(FacesMessage.SEVERITY_ERROR, "Error: Hubo un problema no fue posible crear la Nota Contable");
                        }
                    }
                } else {
                    LOGGER.warn("{} No ha registrado Info. para un tema");
                    nuevoMensaje(FacesMessage.SEVERITY_WARN, "No ha diligenciado la información de ningún tema para la Nota Contable");
                }
            }
        } catch (Exception e) {
            LOGGER.error("{} Error al intentar grabar la Nota Contable", e);
            nuevoMensaje(FacesMessage.SEVERITY_ERROR, "Error: Hubo un problema al guardar la nota contable");
        }
        return null;
    }

    public String cancelarNota() {
        LOGGER.info("{} ... Restaurando a Valores Iniciales ...");
        nota = new NotaContable();
        temaActual = new NotaContableTema();
        temasNotaContable = new ArrayList<>();
        totalesNota = new ArrayList<>();
        return null;
    }

    private boolean validarNota() {
        LOGGER.info("{} Validando campos requeridos");
        LOGGER.info("<<<<nota codigo tipo evento: {}>>>>", nota.getCodigoTipoEvento());
        LOGGER.info("<<<<nota codigo tipo evento class: {}>>>>", nota.getCodigoTipoEvento().getClass());
        validador.validarRequerido(nota.getCodigoTipoEvento(), "Tipo de evento");
        validador.validarRequerido(nota.getCodigoConcepto(), "Concepto");
        validarTemasObligatorios();
        return !getFacesContext().getMessages().hasNext();
    }

    private boolean validarTemasObligatorios() {
        LOGGER.info("{} Validando que temas obligatorios se encuentren diligenciado");
        for (NotaContableTema tema : temasNotaContable) {
            if (tema.isObligatorio() && tema.getMonto().doubleValue() <= 0) {
                nuevoMensaje(FacesMessage.SEVERITY_WARN, "No ha diligenciado la información de los temas obligatorios para la Nota Contable");
                return false;
            }
        }
        return true;
    }

    private boolean validarTotales() {
        LOGGER.info("{} Validando que los totales sean correctos");
        for (NotaContableTotal notaContableTotal : totalesNota) {
            if (notaContableTotal.getTotal() != 0) {
                return true;
            }
        }
        return false;
    }

    public String guardarRiesgo() {
        if (validarRiesgo()) {
            nuevoMensaje(FacesMessage.SEVERITY_INFO, "La información de riesgo operacional se ha guardado temporalmente");
            this.ocultarPopupRiesgo = true;
            PrimeFaces.current().executeScript("PF('modalRiesgo').hide();");
        }
        return null;
    }

    /**
     * Permite validar campos obligatorios para el popup riesgo operacional
     *
     * @return
     */
    private boolean validarRiesgo() {
        LOGGER.info("Se va guardar la Info. diligenciada en ventana modal de Riesgo para Nota Contable Tipo Tema.  ");
        validador.validarPositivo(temaActual.getRiesgoOperacional().getImporteParcial(), "Importe Parcial");
        validador.validarPositivo(temaActual.getRiesgoOperacional().getImporteTotal(), "Importe Total");

        temaActual.getRiesgoOperacional().setFechaEvento(DateUtils.getSQLDate(temaActual.getRiesgoOperacional().getFechaEventoPF()));
        validador.validarRequerido(temaActual.getRiesgoOperacional().getFechaEvento(), "Fecha Inicio del Evento");

        temaActual.getRiesgoOperacional().setFechaDescubrimientoEvento(DateUtils.getSQLDate(temaActual.getRiesgoOperacional().getFechaDescubrimientoEventoPF()));
        validador.validarRequerido(temaActual.getRiesgoOperacional().getFechaDescubrimientoEvento(), "Fecha del Descubrimiento");

        validador.validarSeleccion(temaActual.getRiesgoOperacional().getCodigoClaseRiesgo(), "Clase de Riesgo");

        temaActual.getRiesgoOperacional().setFechaFinEvento(DateUtils.getSQLDate(temaActual.getRiesgoOperacional().getFechaFinEventoPF()));
        validador.validarRequerido(temaActual.getRiesgoOperacional().getFechaFinEvento(), "Fecha Finalizacin del Evento");

        validador.validarRequerido(temaActual.getRiesgoOperacional().getHoraInicioEvento(), "Hora Inicio Evento");
        validador.validarRequerido(temaActual.getRiesgoOperacional().getMinutosInicioEvento(), "Minutos Inicio Evento");
        validador.validarRequerido(temaActual.getRiesgoOperacional().getHoraFinalEvento(), "Hora Finalizacion Evento");
        validador.validarRequerido(temaActual.getRiesgoOperacional().getMinutosFinalEvento(), "Minutos Fin Evento");
        validador.validarRequerido(temaActual.getRiesgoOperacional().getHoraDescubrimiento(), "Hora Descubrimiento");
        validador.validarRequerido(temaActual.getRiesgoOperacional().getMinutosDescubrimiento(), "Minutos Descubrimiento");
        validador.validarRequerido(temaActual.getRiesgoOperacional().getHorario(), "horario fecha inicial");
        validador.validarRequerido(temaActual.getRiesgoOperacional().getHorarioDescubre(), "horario descubrimiento");
        validador.validarRequerido(temaActual.getRiesgoOperacional().getHorarioFinal(), "horario fecha final");
        validador.validarSeleccion(temaActual.getRiesgoOperacional().getCodigoProceso(), "Proceso");
        validador.validarSeleccion(temaActual.getRiesgoOperacional().getCodigoLineaOperativa(), "Lnea Operativa");
        validador.validarSeleccion(temaActual.getRiesgoOperacional().getCodigoProducto(), "Producto");

        validador.validarSeleccion(temaActual.getRiesgoOperacional().getCodigoSubProducto(), "SubProducto Afectado");
        validador.validarSeleccion(temaActual.getRiesgoOperacional().getCodigoCanal(), "Canal");
        validador.validarSeleccion(temaActual.getRiesgoOperacional().getCodigoSubCanal(), "SubCanal");
        validador.validarSeleccion(temaActual.getRiesgoOperacional().getCodigoClaseRiesgoN2(), "SFC Clase Riesgo N2");
        validador.validarSeleccion(temaActual.getRiesgoOperacional().getCodigoClaseRiesgoN3(), "SFC Clase Riesgo N3");
        validador.validarRequerido(temaActual.getRiesgoOperacional().getNombreOutSource(), "Nombre Outsourcing");
        if (aplicaRecuperacion) {
            validador.validarSeleccion(temaActual.getRiesgoOperacional().getAppRecuperacion(), "Aplica Recuperacin");

            if (temaActual.getRiesgoOperacional().getAppRecuperacion().equals("S")) {

                temaActual.getRiesgoOperacional().setFechaRecuperacion(DateUtils.getSQLDate(temaActual.getRiesgoOperacional().getFechaRecuperacionPF()));
                validador.validarRequerido(temaActual.getRiesgoOperacional().getFechaRecuperacion(), "Fecha Recuperacion");

                validador.validarRequerido(temaActual.getRiesgoOperacional().getHoraRecuperacion(), "Hora Recuperacion");
                validador.validarRequerido(temaActual.getRiesgoOperacional().getMinutosRecuperacion(), "Minutos Recuperacion");
                validador.validarRequerido(temaActual.getRiesgoOperacional().getHorarioRecuperacion(), "horario Recuperacion");
                validador.validarSeleccion(temaActual.getRiesgoOperacional().getTipoRecuperacion(), "Tipo Recuperacin");
            }
        }

        try {
            if (temaActual.getRiesgoOperacional().getImporteParcial().doubleValue() > temaActual.getRiesgoOperacional().getImporteTotal().doubleValue()) {

                validador.DiferencaValor("Importe Parcial", "Importe Total");
            }
        } catch (NumberFormatException e) {
            e.getMessage();
        }
        if (temaActual.getRiesgoOperacional().getFechaEvento() != null && temaActual.getRiesgoOperacional().getFechaFinEvento() != null) {
            if (temaActual.getRiesgoOperacional().getFechaEvento().after(temaActual.getRiesgoOperacional().getFechaFinEvento())) {

                validador.Diferenciafecha("Fecha Inicio del Evento", "Fecha Finalizacin del Evento");

            }
        }
        if (temaActual.getRiesgoOperacional().getFechaEvento() != null && temaActual.getRiesgoOperacional().getFechaDescubrimientoEvento() != null) {
            if (temaActual.getRiesgoOperacional().getFechaFinEvento().after(temaActual.getRiesgoOperacional().getFechaDescubrimientoEvento())) {

                validador.Diferenciafecha("Fecha Finalizacin del Evento", "Fecha del Descubrimiento");
            }
        }

        // Validacion para ocultar el popup una vez que la validacion es exitosa
        if (getFacesContext().getMessageList().size() > 0) {
            this.ocultarPopupRiesgo = false;
        }
        return !getFacesContext().getMessages().hasNext();
    }

    public String cancelarRiesgo() {
        if (temaActual.getCodigo().intValue() <= 0) {
            temaActual.setRiesgoOperacional(new RiesgoOperacional());
        } else {
            try {

                temaActual.setRiesgoOperacional(
                        notasContablesManager.getRiesgoPorNotaContableYTemaNotaContable(temaActual.getCodigoNotaContable().intValue(), temaActual.getCodigo().intValue()));
            } catch (Exception e) {

                lanzarError(e, "Error recuperando la información de riesgo operacional");
            }
        }
        return null;
    }

    public String guardarTema() {
        LOGGER.info("{} Se va guardar la Info. diligenciada sobre el tema seleccionado");
        if (validarTema()) {
            temaActual.setEditada(true);
            ajustarCargaImpositiva();
            ajustarTotalesNota();
            temaActual = new NotaContableTema();
            montoAnt = 0d;
            nuevoMensaje(FacesMessage.SEVERITY_INFO, "El tema se ha guardado temporalmente");
            ocultarPopupTema = true;
            PrimeFaces.current().executeScript("PF('editor').hide();");
        }
        return null;
    }

    /**
     * Validaciones asociadas al tema actual
     *
     * @return
     */
    private boolean validarTema() {
        LOGGER.info("{} validación de campos requeridos");
        validarFecha();
        validador.validarRequerido(temaActual.getCodigoSucursalDestinoPartida(), "Sucursal destino de la Partida");
        validador.validarRequerido(temaActual.getCodigoSucursalDestinoContraPartida(), "Sucursal destino de la Contrapartida");
        if (temaActual.getTema().getReferencia1().equals("S")) {
            validador.validarRequerido(temaActual.getReferencia1(), "Referencia de la Partida");
        }
        if (temaActual.getTema().getReferencia2().equals("S")) {
            validador.validarRequerido(temaActual.getReferencia2(), "Referencia de la Contrapartida");
        }
        if (temaActual.getTema().getTercero1().equals("S")) {
            validador.validarSeleccion(temaActual.getTipoIdentificacion1(), "Tipo de documento del tercero asociado a la Partida");
            validador.validarRequerido(temaActual.getNumeroIdentificacion1(), "número de documento del tercero asociado a la Partida");

            if (temaActual.getNombreCompleto1().isEmpty()) {
                nuevoMensaje(FacesMessage.SEVERITY_WARN, "La información del tercero asociado a la Partida es requerida");
            }
            if (temaActual.getTema().getContrato1().equals("S")) {
                validador.validarSeleccion(temaActual.getContrato1(), "Contrato del tercero asociado a la Partida");
            }
        }
        if (temaActual.getTema().getTercero2().equals("S")) {
            validador.validarSeleccion(temaActual.getTipoIdentificacion2(), "Tipo de documento del tercero asociado a la Contrapartida");
            validador.validarRequerido(temaActual.getNumeroIdentificacion2(), "número de documento del tercero asociado a la Contrapartida");
            if (temaActual.getNombreCompleto2().isEmpty()) {
                nuevoMensaje(FacesMessage.SEVERITY_WARN, "La información del tercero asociado a la Contrapartida es requerida");
            }
            if (temaActual.getTema().getContrato2().equals("S")) {
                validador.validarSeleccion(temaActual.getContrato2(), "Contrato del tercero asociado a la Contrapartida");
            }
        }
        validador.validarRequerido(temaActual.getDescripcion(), "Descripción");
        validador.validarSeleccion(temaActual.getCodigoDivisa(), "Tipo de Divisa");
        validador.validarPositivo(temaActual.getMonto(), "Importe");

        for (MontoMaximo monto : montos) {
            if (monto.getDivisa().equals("1") && temaActual.getCodigoDivisa().equals("COP") && temaActual.getMonto().doubleValue() > monto.getMontoMaximo()
                    || !monto.getDivisa().equals("1") && !temaActual.getCodigoDivisa().equals("COP") && temaActual.getMonto().doubleValue() > monto.getMontoMaximo()) {
                NumberFormat f = NumberFormat.getCurrencyInstance();
                nuevoMensaje(FacesMessage.SEVERITY_WARN, "El importe supera el monto mximo autorizado (" + f.format(monto.getMontoMaximo()) + ")");
            }
        }
        if (temaActual.getTema().getRiesgoOperacional().equals("S")) {
            if (temaActual.getRiesgoOperacional().getImporteParcial().doubleValue() <= 0) {
                nuevoMensaje(FacesMessage.SEVERITY_WARN, "El tema requiere información de Riesgo Operacional");
            }
        }
        if (nota.getConcepto().getSoportes().equals("S") && temaActual.getAnexoTema().isEmpty()) {
            nuevoMensaje(FacesMessage.SEVERITY_WARN, "El concepto exige soportes para todos los temas, por favor agregue al menos uno");
        }
        if (getFacesContext().getMessageList().size() > 0) {
            ocultarPopupTema = false;
        }
        return !getFacesContext().getMessages().hasNext();
    }

    /**
     * Obtiene los totales de la nota agrupados por tipo de divisa
     */
    private void ajustarTotalesNota() {
        LOGGER.info("{} Estructura y agrupa los montos totales por divisa");
        totalesNota = new ArrayList<>();
        for (NotaContableTema tema : temasNotaContable) {
            if (!tema.getCodigoDivisa().equals("")) {
                boolean existeDivisa = false;
                for (NotaContableTotal totalNota : totalesNota) {
                    if (totalNota.getCodigoDivisa().equals(tema.getCodigoDivisa())) {
                        existeDivisa = true;
                        totalNota.setTotal(totalNota.getTotal() + tema.getMonto().doubleValue());
                        break;
                    }
                }
                if (!existeDivisa) {
                    NotaContableTotal totalNota = new NotaContableTotal();
                    totalNota.setCodigoDivisa(tema.getCodigoDivisa());
                    totalNota.setTotal(tema.getMonto().doubleValue());
                    totalesNota.add(totalNota);
                }
            }
        }
    }

    public String cancelarTema() {
        LOGGER.info("{} La operacin se cancel para el tema: {} ({})", temaActual.getCodigo(), temaActual.getDescripcion());
        ServletContext context = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
        for (NotaContableTema nct : temasNotaContable) {
            if (nct.getCodigo().intValue() == temaActual.getCodigo().intValue()) {
                codigo = nct.getCodigo().intValue();
                NotaContableTema newNota = new NotaContableTema();
                if (codigo <= 0) {
                    newNota.setFechaContable(new java.sql.Date(DateUtils.getDate(maxFecha, "dd-MM-yyyy").getTime()));
                    LOGGER.info("{} Borrando los soportes y anexos correspondientes");
                    for (Anexo anexo : temaActual.getAnexoTema()) {
                        File f = new File(context.getRealPath(DIR_SOPORTES + anexo.getArchivo()));
                        f.delete();
                    }
                    LOGGER.info("{} Restableciendo los parámetros iniciales");
                    newNota.setAnexoTema(new ArrayList<>());
                    newNota.setRiesgoOperacional(new RiesgoOperacional());
                    for (NotaContableTemaImpuesto impuestoTNC : nct.getImpuestoTema()) {
                        impuestoTNC.setExonera("N");
                        impuestoTNC.setBoolExonera(false);
                        impuestoTNC.setBase(0);
                        impuestoTNC.setCalculado(0);
                        newNota.getImpuestoTema().add(impuestoTNC);
                    }
                    nct.reset(newNota);
                } else {
                    try {
                        newNota = notasContablesManager.getTemaNotaContablePorCodigo(temaActual);
                        LOGGER.info("EN CANCELACION HORARIO INICIO {}", newNota.getRiesgoOperacional().getHorario());
                        LOGGER.info("EN CANCELACION HORARIO DESCUBRE {}", newNota.getRiesgoOperacional().getHorarioDescubre());
                        nct.reset(newNota);
                        verNota();
                    } catch (Exception e) {
                        LOGGER.error("{} Error al recuperar la Info. diligenciada del tema");
                        nuevoMensaje(FacesMessage.SEVERITY_ERROR, "Error: Hubo un problema al recuperar la información del tema");
                    }
                }
                temaActual = new NotaContableTema();
                ajustarTotalesNota();
                break;
            }
        }
        return null;
    }

    private synchronized int getNumArchivo() {
        return numArchivo++;
    }

    public List<SelectItem> getHoras() {
        return horas;
    }

    public void setHoras(List<SelectItem> horas) {
        this.horas = horas;
    }

    public List<SelectItem> getConceptos() {
        return conceptos;
    }

    public void setConceptos(List<SelectItem> conceptos) {
        this.conceptos = conceptos;
    }

    public List<SelectItem> getTiposEvento() {
        return tiposEvento;
    }

    public void setTiposEvento(List<SelectItem> tiposEvento) {
        this.tiposEvento = tiposEvento;
    }

    public List<SelectItem> getDivisas() {
        return divisas;
    }

    public void setDivisas(List<SelectItem> divisas) {
        this.divisas = divisas;
    }

    public List<SelectItem> getSucursalesPartida() {
        return sucursalesPartida;
    }

    public void setSucursalesPartida(List<SelectItem> sucursalesPartida) {
        this.sucursalesPartida = sucursalesPartida;
    }

    public List<SelectItem> getSucursalesContraPartida() {
        return sucursalesContraPartida;
    }

    public void setSucursalesContraPartida(List<SelectItem> sucursalesContraPartida) {
        this.sucursalesContraPartida = sucursalesContraPartida;
    }

    public List<SelectItem> getTiposDoc() {
        return tiposDoc;
    }

    public void setTiposDoc(List<SelectItem> tiposDoc) {
        this.tiposDoc = tiposDoc;
    }

    public List<SelectItem> getContratos1() {
        return contratos1;
    }

    public void setContratos1(List<SelectItem> contratos1) {
        this.contratos1 = contratos1;
    }

    public List<SelectItem> getContratos2() {
        return contratos2;
    }

    public void setContratos2(List<SelectItem> contratos2) {
        this.contratos2 = contratos2;
    }

    public Collection<TemaProducto> getProductosTema() {
        return productosTema;
    }

    public void setProductosTema(Collection<TemaProducto> productosTema) {
        this.productosTema = productosTema;
    }

    public ArrayList<Date> getDiasNoHabiles() {
        return diasNoHabiles;
    }

    public void setDiasNoHabiles(ArrayList<Date> diasNoHabiles) {
        this.diasNoHabiles = diasNoHabiles;
    }

    public String getMinFecha() {
        return minFecha;
    }

    public void setMinFecha(String minFecha) {
        this.minFecha = minFecha;
    }

    public String getMaxFecha() {
        return maxFecha;
    }

    public void setMaxFecha(String maxFecha) {
        this.maxFecha = maxFecha;
    }

    public List<SelectItem> getClasesRiesgo() {
        return clasesRiesgo;
    }

    public void setClasesRiesgo(List<SelectItem> clasesRiesgo) {
        this.clasesRiesgo = clasesRiesgo;
    }

    public List<SelectItem> getPerdidaOper() {
        return perdidaOper;
    }

    public void setPerdidaOper(List<SelectItem> perdidaOper) {
        this.perdidaOper = perdidaOper;
    }

    public List<SelectItem> getProcesos() {
        return procesos;
    }

    public void setProcesos(List<SelectItem> procesos) {
        this.procesos = procesos;
    }

    public List<SelectItem> getProductos() {
        return productos;
    }

    public void setProductos(List<SelectItem> productos) {
        this.productos = productos;
    }

    public List<SelectItem> getLineasOperativas() {
        return lineasOperativas;
    }

    public void setLineasOperativas(List<SelectItem> lineasOperativas) {
        this.lineasOperativas = lineasOperativas;
    }

    public Double getMontoAlertaCOP() {
        return montoAlertaCOP;
    }

    public void setMontoAlertaCOP(Double montoAlertaCOP) {
        this.montoAlertaCOP = montoAlertaCOP;
    }

    public Double getMontoAlertaEXT() {
        return montoAlertaEXT;
    }

    public void setMontoAlertaEXT(Double montoAlertaEXT) {
        this.montoAlertaEXT = montoAlertaEXT;
    }

    public Collection<MontoMaximo> getMontos() {
        return montos;
    }

    public void setMontos(Collection<MontoMaximo> montos) {
        this.montos = montos;
    }

    public Double getMontoAnt() {
        return montoAnt;
    }

    public void setMontoAnt(Double montoAnt) {
        this.montoAnt = montoAnt;
    }

    public String getConcepto() {
        return concepto;
    }

    public void setConcepto(String concepto) {
        this.concepto = concepto;
    }

    public List<SelectItem> getMinutos() {
        return minutos;
    }

    public void setMinutos(List<SelectItem> minutos) {
        this.minutos = minutos;
    }

    public List<SelectItem> getHorario() {
        return horario;
    }

    public void setHorario(List<SelectItem> horario) {
        this.horario = horario;
    }

    /**
     * @return the ocultarPopupTema
     */
    public boolean isOcultarPopupTema() {
        return ocultarPopupTema;
    }

    /**
     * @param ocultarPopupTema the ocultarPopupTema to set
     */
    public void setOcultarPopupTema(boolean ocultarPopupTema) {
        this.ocultarPopupTema = ocultarPopupTema;
    }

    /**
     * @return the ocultarPopupRiesgo
     */
    public boolean isOcultarPopupRiesgo() {
        return ocultarPopupRiesgo;
    }

    /**
     * @param ocultarPopupRiesgo the ocultarPopupRiesgo to set
     */
    public void setOcultarPopupRiesgo(boolean ocultarPopupRiesgo) {
        this.ocultarPopupRiesgo = ocultarPopupRiesgo;
    }

    /**
     * @return the ocultarPopupAnular
     */
    public boolean isOcultarPopupAnular() {
        return ocultarPopupAnular;
    }

    /**
     * @param ocultarPopupAnular the ocultarPopupAnular to set
     */
    public void setOcultarPopupAnular(boolean ocultarPopupAnular) {
        this.ocultarPopupAnular = ocultarPopupAnular;
    }

    /**
     * @return the subProductos
     */
    public List<SelectItem> getSubProductos() {
        return subProductos;
    }

    /**
     * @param subProductos the subProductos to set
     */
    public void setSubProductos(List<SelectItem> subProductos) {
        this.subProductos = subProductos;
    }

    /**
     * @return the canales
     */
    public List<SelectItem> getCanales() {
        return canales;
    }

    /**
     * @param canales the canales to set
     */
    public void setCanales(List<SelectItem> canales) {
        this.canales = canales;
    }

    /**
     * @return the subcanales
     */
    public List<SelectItem> getSubcanales() {
        return subcanales;
    }

    /**
     * @param subcanales the subcanales to set
     */
    public void setSubcanales(List<SelectItem> subcanales) {
        this.subcanales = subcanales;
    }

    /**
     * @return the clasesRiesgoN2
     */
    public List<SelectItem> getClasesRiesgoN2() {
        return clasesRiesgoN2;
    }

    /**
     * @param clasesRiesgoN2 the clasesRiesgoN2 to set
     */
    public void setClasesRiesgoN2(List<SelectItem> clasesRiesgoN2) {
        this.clasesRiesgoN2 = clasesRiesgoN2;
    }

    /**
     * @return the clasesRiesgoN3
     */
    public List<SelectItem> getClasesRiesgoN3() {
        return clasesRiesgoN3;
    }

    /**
     * @param clasesRiesgoN3 the clasesRiesgoN3 to set
     */
    public void setClasesRiesgoN3(List<SelectItem> clasesRiesgoN3) {
        this.clasesRiesgoN3 = clasesRiesgoN3;
    }

    /**
     * @return the aplicaRecuperacion
     */
    public boolean isAplicaRecuperacion() {
        return aplicaRecuperacion;
    }

    /**
     * @param aplicaRecuperacion the aplicaRecuperacion to set
     */
    public void setAplicaRecuperacion(boolean aplicaRecuperacion) {
        this.aplicaRecuperacion = aplicaRecuperacion;
    }

    public java.util.Date getFechaContablePF() {

        return fechaContablePF;
    }

    public void setFechaContablePF(java.util.Date fechaContablePF) {
        this.fechaContablePF = fechaContablePF;
    }


}
