package co.com.bbva.app.notas.contables.jsf.parametros;

import co.com.bbva.app.notas.contables.carga.dto.PUC;
import co.com.bbva.app.notas.contables.carga.dto.Sucursal;
import co.com.bbva.app.notas.contables.session.Session;
import co.com.bbva.app.notas.contables.util.StringUtils;
import org.primefaces.model.DualListModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.model.SelectItem;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * <p>
 * Pagina para manejar la administración de parametros relacionados con la entidad PUC
 * </p>
 */
public abstract class GeneralCentroPage extends GeneralParametrosPage<PUC, PUC> {


    private static final long serialVersionUID = 1L;

    private static final Logger LOGGER = LoggerFactory.getLogger(GeneralCentroPage.class);
    protected Boolean selectedAll = false;
    protected List<PUC> seleccionados = new ArrayList<PUC>();

    protected List<SelectItem> tiposCentrosAut = new ArrayList<SelectItem>();
    protected List<SelectItem> indicadores = new ArrayList<SelectItem>();
    protected Collection<Sucursal> sucursales = new ArrayList<Sucursal>();
    protected List<SelectItem> centrosAut = new ArrayList<SelectItem>();

    protected List<String> tipoCentrosSel = new ArrayList<String>();
    protected List<String> centrosAutSel = new ArrayList<String>();

    private List<SelectItem> centrosAutTarget = new ArrayList<>();
    private List<SelectItem> centrosAutSource = new ArrayList<>();
    private DualListModel<SelectItem> dualListModel;

    protected String indicadorSel = "I";

    private final Boolean origen;


    // variables picklist primefaces

    private DualListModel<String> centrosAutorizados;

    private Boolean isValidSelection = true;


//    protected List<String> centrosAutSelPF = new ArrayList<String>();
//    protected List<String> centrosAutPF = new ArrayList<String>();


//	Session session = getContablesSessionBean().getSessionTrace();

    @PostConstruct
    public void init() {
        super._init();
        crearListasFijas();
    }

    public GeneralCentroPage(boolean origen) {
        super(false);
        this.origen = origen;
        dualListModel = new DualListModel<>(centrosAut, centrosAutTarget);

    }

    /**
     * retorna una instancia de PUC
     *
     * @return
     */
    @Override
    protected PUC _getInstance() {
        return new PUC();
    }

    @Override
    public String buscarPorFiltro() {
        selectedAll = false;
        for (PUC p : getDatos()) {
            p.setSelected(selectedAll);
        }
        return super.buscarPorFiltro();
    }

//	@Override
//	protected void _init() {
//		super._init();
//		crearListasFijas();
//	}

    private void crearListasFijas() {
        LOGGER.info("Crear listas fijas");
        // se crean las lista fijas solo si es necesario
        if (tiposCentrosAut == null || tiposCentrosAut.isEmpty()) {
            tiposCentrosAut = new ArrayList<SelectItem>();
            tiposCentrosAut.add(new SelectItem("O", "O: Oficinas"));
            tiposCentrosAut.add(new SelectItem("S", "S: Departamentos"));
            tiposCentrosAut.add(new SelectItem("C", "C: reas Centrales Contables"));
            tiposCentrosAut.add(new SelectItem("A", "A: Administrativas"));
            tiposCentrosAut.add(new SelectItem("R", "R: Centros Refundidos"));

            indicadores = new ArrayList<SelectItem>();
            indicadores.add(new SelectItem("I", "Incluir"));
            indicadores.add(new SelectItem("E", "Excluir"));
        }
    }

    /**
     * Se realiza el proceso de busqueda completo de entidades de tipo PUC
     *
     * @return
     */
    @Override
    public Collection<PUC> _buscarTodos() throws Exception {
        return new ArrayList<PUC>();
    }

    /**
     * Realiza la busqueda de entidades de tipo PUC filtrando segn lo indicado por el usuario
     *
     * @return
     */
    @Override
    public Collection<PUC> _buscarPorFiltro() throws Exception {
        LOGGER.info("<<<<buscar por filtro {}>>>>", param);
        if (param.trim().length() >= 4) {
//			LOGGER.info("{} Buscar puc: {}", session.getTraceLog(),param );
            Collection<co.com.bbva.app.notas.contables.carga.dto.PUC> centrosBy = cargaAltamiraManager.findCentrosBy(param);
            LOGGER.info("<<<<<<busqueda por filtro {}>>>>>>", centrosBy);

            return centrosBy;
        }
        nuevoMensaje(FacesMessage.SEVERITY_WARN, "Por favor indique un número de partida de al menos 4 digitos");
        return new ArrayList<PUC>();
    }

    /**
     * Funcion llamada cuando se desea inciar la edicion o creacion de registros de tipo PUC
     *
     * @return
     */
    @Override
    protected void _editar() throws Exception {
        if (seleccionValida()) {
            LOGGER.info("Editar GeneralPage paso 2 {}", isValidSelection);
            // se limpian las variables para evitar informacion errada
            seleccionados = new ArrayList<PUC>();
            tipoCentrosSel = new ArrayList<String>();
            indicadorSel = "";
            centrosAutSel = new ArrayList<String>();

            // se obtiene la lista de cuentas seleccionadas con los checkBox
            for (PUC row : getDatos()) {
                if (row.getSelected()) {
                    seleccionados.add(row);
                    LOGGER.info("Seleccionados GeneralPage {}", seleccionados.size());
                }
            }

            // si existen cuentas seleccionadas, se setean las listas segun sea necesario
            if (!seleccionados.isEmpty()) {
                // se carga un objeto como base
                objActual = seleccionados.get(0);

                // se indican los tipos de centros seleccionados
                String tca = origen ? objActual.getTipoCentroOrigenAutorizado() : objActual.getTipoCentroDestinoAutorizado();
                addTca(tca, "O");
                addTca(tca, "S");
                addTca(tca, "C");
                addTca(tca, "A");
                addTca(tca, "R");

                // indicador
                indicadorSel = (origen ? objActual.getIndicadorCentroOrigen() : objActual.getIndicadorCentroDestino()).equals("I") ? "I" : "E";

                // centros autorizad
                for (Sucursal s : getSucursalesPUC(origen ? objActual.getCentrosOrigenAutorizados() : objActual.getCentrosDestinoAutorizados())) {
                    centrosAutSel.add(s.getTipoCentro() + s.getCodigo());
                }

                // se consultan las sucursales y se filtran segun las indicaciones de la cuenta actual
                sucursales = cargaAltamiraManager.getSucursales();
                LOGGER.info("Carga sucursales paso 3 centros autorizadosSel: {}", centrosAutSel);
                selccionarTipoCentro();
            } else {
                LOGGER.info("No selecciono registro");
                nuevoMensaje(FacesMessage.SEVERITY_WARN, "Debe seleccionar por lo menos un registro");
            }
        } else {
            LOGGER.info("is valid selection {}", isValidSelection);
            nuevoMensaje(FacesMessage.SEVERITY_ERROR, "Las cuentas que seleccione deben tener la misma configuración para Tipo Centro Autorizado, Indicador, Centros Autorizados");
        }
    }

    // adiciona una opcion de tipo de centro autorizado a la lista de seleccionados si la cuenta as lo indica
    protected void addTca(String tca, String s) {
        if (tca.indexOf(s) >= 0) {
            tipoCentrosSel.add(s);
        }
    }

    @Override
    protected boolean _guardar() throws Exception {
        // se formatean los centros autorizados seleccionados
        LOGGER.info("<<<<flujo Guardar>>>>");
        String centros = "";
        for (Object centroAutoSelect : dualListModel.getTarget()) {
            String c = centroAutoSelect.toString();
            String exp = "    " + Integer.valueOf(c.substring(1));
            centros += exp.substring(exp.length() - 4, exp.length());
        }
        // se formatean los tipos de centros autorizados seleccionados
        LOGGER.info("<<<<<<<<<<<centros {}>>>>>>>>>>>", centros);
        String tCentros = "";
        for (String c : tipoCentrosSel) {
            tCentros += c;
        }

        // se itera sobre las cuentas seleccionadas y se les actualiza la informacion
        for (PUC p : seleccionados) {
            if (origen) {
                p.setIndicadorCentroOrigen(indicadorSel);
                p.setCentrosOrigenAutorizados(centros);
                p.setTipoCentroOrigenAutorizado(tCentros);
            } else {
                p.setIndicadorCentroDestino(indicadorSel);
                p.setCentrosDestinoAutorizados(centros);
                p.setTipoCentroDestinoAutorizado(tCentros);
            }
        }
//		LOGGER.info("{} Actualiza puc: {}", session.getTraceLog(),getCodCuentasString(seleccionados) );
        cargaAltamiraManager.updatePUCs(seleccionados, getUsuarioLogueado().getUsuario().getCodigo().intValue());
        return true;
    }

    @Override
    protected void _validar() throws Exception {
        LOGGER.info("<<<<Validar datos generalCentroPage>>>>");
        validarCentrosAut();
    }

    /**
     * Cambia el estado de la instancia seleccionada de tipo PUC
     *
     * @return
     */
    @Override
    public boolean _cambiarEstado() throws Exception {
        // no implementado
        return false;
    }

    /**
     * Borra la informacion de la instancia seleccionada de tipo PUC
     *
     * @return
     */
    @Override
    public boolean _borrar() throws Exception {
        // no implementado
        return false;
    }

    public String selecccionar() {
        selectedAll = !selectedAll;
        for (PUC p : getDatos()) {
            p.setSelected(selectedAll);
        }
        return null;
    }

    /**
     * Valida que los centros autorizados sean acordes a los tipos de centros indicados. Adicionalmente se valida que la lista no sobrepase los 21 centros
     *
     * @return
     */
    public String validarCentrosAut() {
        LOGGER.info("validar Centro de destino {}", centrosAut.size());
        List<String> nuevosCentros = new ArrayList<String>();
        LOGGER.info("Datos centro destino target {}", dualListModel.getTarget().size());
//        actualizarCentrosAutSel();
        for (Object selectItem : dualListModel.getTarget()) {
            LOGGER.info("hola for");
            LOGGER.info("for list<String> class {}", selectItem.toString());
            String value = (String) selectItem;
            centrosAutSel.add(value);
        }
        LOGGER.info("validar centrosAutorizados Seleccionados {}", centrosAutSel.size());
        for (String centroAut : centrosAutSel) {

            if (nuevosCentros.size() < 21) {
                String strTipoCentro = centroAut.substring(0, 1).toUpperCase();

                boolean indValido = true;
                if (indicadorSel.equals("I")) { // Incluir
                    if (tipoCentrosSel.contains("O") && strTipoCentro.equals("O")) {
                        indValido = false;
                    }
                    if (tipoCentrosSel.contains("S") && strTipoCentro.equals("S")) {
                        indValido = false;
                    }
                    if (tipoCentrosSel.contains("C") && strTipoCentro.equals("C")) {
                        indValido = false;
                    }
                    if (tipoCentrosSel.contains("A") && strTipoCentro.equals("A")) {
                        indValido = false;
                    }
                    if (tipoCentrosSel.contains("R") && strTipoCentro.equals("R")) {
                        indValido = false;
                    }
                } else { // Excluir
                    indValido = false;
                    if (tipoCentrosSel.contains("O") && strTipoCentro.equals("O")) {
                        indValido = true;
                    }
                    if (tipoCentrosSel.contains("S") && strTipoCentro.equals("S")) {
                        indValido = true;
                    }
                    if (tipoCentrosSel.contains("C") && strTipoCentro.equals("C")) {
                        indValido = true;
                    }
                    if (tipoCentrosSel.contains("A") && strTipoCentro.equals("A")) {
                        indValido = true;
                    }
                    if (tipoCentrosSel.contains("R") && strTipoCentro.equals("R")) {
                        indValido = true;
                    }
                }
                // si ha seleccionado un tipo de centro autorizado con del mismo tipo a centro actual
                if (indValido) {
                    nuevosCentros.add(centroAut);
                } else {
                    nuevoMensaje(FacesMessage.SEVERITY_WARN, "No puede hacer esa operacin. La sucursal no es del Tipo de Centro Autorizado seleccionado");
                }
            } else {
                // se borran los adicionales
                nuevoMensaje(FacesMessage.SEVERITY_WARN, "Solamente puede incluir 21 Sucursales en la lista. El resto se omitirn");
                break;
            }
        }
        centrosAutSel = nuevosCentros;
        LOGGER.info("<<<<<<Nuevo centros Au {}>>>>>>", centrosAutSel);
        return null;
    }

    /**
     * Funcion invocada cuando vara algn tipo de centro o indicador para filtrar los centros autorizados a mostrar
     *
     * @return
     */
    public String selccionarTipoCentro() {
        centrosAut = new ArrayList<SelectItem>();
        // se filtran los centros autorizados segun lo seleccionado por el usuario
        for (Sucursal s : sucursales) {
            // si corresponde al tipo de centro y se trata de excluir
            if (indicadorSel.contains("E") && tipoCentrosSel.contains(s.getTipoCentro())) {
                centrosAut.add(new SelectItem(s.getTipoCentro() + s.getCodigo(), s.getTipoCentro() + "-" + s.getCodigo() + " " + s.getNombre()));
            } else if (indicadorSel.contains("I") && !tipoCentrosSel.contains(s.getTipoCentro())) {
                centrosAut.add(new SelectItem(s.getTipoCentro() + s.getCodigo(), s.getTipoCentro() + "-" + s.getCodigo() + " " + s.getNombre()));
            }
        }
        // si el usuario tenia centros autorizados seleccionados, se valida que sigan cumpliendo las condiciones de tipo centro e indicador
        if (!centrosAutSel.isEmpty()) {
            List<String> nuevosCentros = new ArrayList<String>();
            List<String> omitidos = new ArrayList<String>();
            for (String c : centrosAutSel) {
                if (!c.trim().isEmpty()) {
                    boolean valid = false;
                    for (SelectItem i : centrosAut) {
                        String val = i.getValue().toString();
                        // se revisa que los centros ya seleccionados coincidan con los nuevos centros
                        if (c.equals(val)) {
                            nuevosCentros.add(val);
                            valid = true;
                        }
                    }
                    // se guardan los omitidos para mostrarlos como mensaje
                    if (!valid) {
                        omitidos.add(c.substring(1));
                    }
                }
            }
            if (!omitidos.isEmpty()) {
                centrosAutSel = nuevosCentros;
                nuevoMensaje(FacesMessage.SEVERITY_WARN, "Al cambiar la información del indicador o de los tipos de centros autorizados, los siguientes centros se han omitido ya que no cumplen con el nuevo criterio:");
                nuevoMensaje(FacesMessage.SEVERITY_WARN, omitidos.toString());
            }
        }
        return null;
    }

    /**
     * Valida que el conjunto de cuentas seleccionadas sean adecuadas para la modificacion en conjunto
     *
     * @return
     */
    public boolean seleccionValida() {
        String tipoCentro = "";
        String indicadorCentro = "";
        String centrosAutorizados = "";
        int count = 0;
        boolean indValido = true;
        isValidSelection = true;
        for (PUC row : getDatos()) {
            // se evaluan las cuentas seleccionadas
            if (row.getSelected()) {
                if (count != 0) {
                    if (!tipoCentro.equals(origen ? row.getTipoCentroOrigenAutorizado() : row.getTipoCentroDestinoAutorizado())) {
                        indValido = false;
                    }
                    if (!indicadorCentro.equals(origen ? row.getIndicadorCentroOrigen() : row.getIndicadorCentroDestino())) {
                        indValido = false;
                    }
                    if (!centrosAutorizados.equals(origen ? row.getCentrosOrigenAutorizados() : row.getCentrosDestinoAutorizados())) {
                        indValido = false;
                    }
                } else {
                    if (origen) {
                        tipoCentro = row.getTipoCentroOrigenAutorizado() != null ? row.getTipoCentroOrigenAutorizado() : "";
                        indicadorCentro = row.getIndicadorCentroOrigen() != null ? row.getIndicadorCentroOrigen() : "";
                        centrosAutorizados = row.getCentrosOrigenAutorizados() != null ? row.getCentrosOrigenAutorizados() : "";
                    } else {
                        tipoCentro = row.getTipoCentroDestinoAutorizado() != null ? row.getTipoCentroDestinoAutorizado() : "";
                        indicadorCentro = row.getIndicadorCentroDestino() != null ? row.getIndicadorCentroDestino() : "";
                        centrosAutorizados = row.getCentrosDestinoAutorizados() != null ? row.getCentrosDestinoAutorizados() : "";
                    }
                    objActual = row;
                }
                if (!indValido) {
                    LOGGER.info("Carga sucursales paso 3 centros autorizadosSel: {}", centrosAutSel);
                    isValidSelection = false;
                    return false;
                }
                count++;
            }
        }
        return true;
    }

    /**
     * Toma la cadena de centros autorizados y los formatea para buscar las sucursales asociadas
     *
     * @param codigosSucursales
     * @return
     * @throws Exception
     */
    public Collection<Sucursal> getSucursalesPUC(String codigosSucursales) throws Exception {
        ArrayList<Sucursal> sucursales = new ArrayList<Sucursal>();
        Sucursal sucursal = new Sucursal();
        String codigoSucursal = "";

        if (codigosSucursales != null) {
            codigosSucursales = StringUtils.insertSplit(StringUtils.replace(codigosSucursales, " ", "0"), "-", 4);
            while (codigosSucursales.length() > 0) {
                codigoSucursal = codigosSucursales.substring(0, codigosSucursales.length() >= 4 ? 4 : codigosSucursales.length());
                if (!codigoSucursal.equals("0000")) {
                    sucursal = new Sucursal();
                    sucursal.setCodigo(codigoSucursal);
                    sucursal = cargaAltamiraManager.getSucursal(sucursal);
                    sucursales.add(sucursal);
                }
                try {
                    codigosSucursales = codigosSucursales.substring(5);
                } catch (Exception le_e) {
                    codigosSucursales = "";
                }
            }
        }

        return sucursales;
    }

    public Boolean getSelectedAll() {
        return selectedAll;
    }

    public void setSelectedAll(Boolean selectedAll) {
        this.selectedAll = selectedAll;
    }

    public boolean getHaySeleccion() {
        if (esUltimaFase()) {
            for (PUC p : getDatos()) {
                if (p.getSelected()) {
                    return true;
                }
            }
        }
        return false;
    }

    public List<PUC> getSeleccionados() {
        LOGGER.info("getSeleccionados {}", seleccionados.size());
        return seleccionados;
    }

    public void setSeleccionados(List<PUC> seleccionados) {
        this.seleccionados = seleccionados;
    }

    public List<SelectItem> getTiposCentrosAut() {
        LOGGER.info("getTiposCentrosAut {}", tiposCentrosAut.size());
        return tiposCentrosAut;
    }

    public void setTiposCentrosAut(List<SelectItem> tiposCentrosAut) {
        this.tiposCentrosAut = tiposCentrosAut;
    }

    public List<SelectItem> getIndicadores() {
        return indicadores;
    }

    public void setIndicadores(List<SelectItem> indicadores) {
        this.indicadores = indicadores;
    }

    public List<String> getTipoCentrosSel() {
        LOGGER.info("getTipoCentroSel {}", tipoCentrosSel.size());
        return tipoCentrosSel;
    }

    public void setTipoCentrosSel(List<String> tipoCentrosSel) {
        this.tipoCentrosSel = tipoCentrosSel;
    }

    public String getIndicadorSel() {
        LOGGER.info("getIndicadoresSeleccionados {}", indicadorSel);
        return indicadorSel;
    }

    public void setIndicadorSel(String indicadorSel) {
        this.indicadorSel = indicadorSel;
    }

    public List<String> getCentrosAutSel() {
        return centrosAutSel;
    }

    public void setCentrosAutSel(List<String> centrosAutSel) {
        this.centrosAutSel = centrosAutSel;
    }

    public List<SelectItem> getCentrosAut() {
        LOGGER.info("getCentros Autorizados: {}", centrosAut.size());
        return centrosAut;
    }

    public void setCentrosAut(List<SelectItem> centrosAut) {
        this.centrosAut = centrosAut;
    }

    public String getCodCuentasString(List<PUC> seleccionados) {

        List<String> cuentas = new ArrayList<String>();

        for (PUC pcu : seleccionados) {
            cuentas.add(pcu.getNumeroCuenta());
        }

        return cuentas.toString();
    }

    public DualListModel<SelectItem> getDualListModel() {
        LOGGER.info("Datos centro destino target {}", dualListModel.getTarget().size());
        centrosAutSource = new ArrayList<>();
        centrosAutTarget = new ArrayList<>();

        for (SelectItem centroAutorizado : centrosAut) {
            if (centrosAutSel.contains(centroAutorizado.getValue().toString()))
                centrosAutTarget.add(centroAutorizado);
            else
                centrosAutSource.add(centroAutorizado);
        }

        dualListModel = new DualListModel<>(centrosAutSource, centrosAutTarget);
        return dualListModel;
    }

    public void setDualListModel(DualListModel<SelectItem> dualListModel) {
        this.dualListModel = dualListModel;
    }

    /**
     * Method to cast Dualist to List<String>
     */
    public void actualizarCentrosAutSel() {
        LOGGER.info("pasar datos a list<String>");
        LOGGER.info("pasar datos a list<String> {}", dualListModel.getTarget());

        for (SelectItem selectItem : dualListModel.getTarget()) {
            LOGGER.info("for list<String> {}", selectItem.getValue());
            String value = (String) selectItem.getValue();
            centrosAutSel.add(value);
        }
        LOGGER.info("centros Autoeizados Sel metodo {}", centrosAutSel);
    }

    public DualListModel<String> getCentrosAutorizados() {
        return centrosAutorizados;
    }

    public void setCentrosAutorizados(DualListModel<String> centrosAutorizados) {
        this.centrosAutorizados = centrosAutorizados;
    }

    public Boolean getValidSelection() {
        LOGGER.info("valid Seleccion {}", isValidSelection);
        return isValidSelection;
    }

    public void setValidSelection(Boolean validSelection) {
        isValidSelection = validSelection;
    }
}
