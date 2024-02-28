package co.com.bbva.app.notas.contables.jsf.parametros;

import co.com.bbva.app.notas.contables.carga.dto.PUC;
import co.com.bbva.app.notas.contables.carga.dto.Producto;
import co.com.bbva.app.notas.contables.dto.*;
import org.primefaces.model.DualListModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.faces.application.FacesMessage;
import javax.faces.model.SelectItem;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.TreeSet;

/**
 * <p>
 * Pagina para manejar la administración de parametros relacionados con la entidad Tema
 * </p>
 */
@ViewScoped
@Named(value = "temaPage")
public class TemaPage extends GeneralParametrosPage<Concepto, Tema> {

    private static final long serialVersionUID = 1L;

    private static final Logger LOGGER = LoggerFactory.getLogger(TemaPage.class);

    private List<SelectItem> productos = new ArrayList<SelectItem>();
    private List<SelectItem> impuestos = new ArrayList<SelectItem>();
    private List<SelectItem> cuentas = new ArrayList<SelectItem>();
    private List<SelectItem> cuentasContraPartida = new ArrayList<SelectItem>();
    private List<SelectItem> conceptos = new ArrayList<SelectItem>();

    private String partidaContable = "";
    private String contraPartidaContable = "";
    private String concepto = "";

    private List<String> impuestosSel = new ArrayList<String>();
    private List<String> productosSel = new ArrayList<String>();

    private List<SelectItem> productoSource = new ArrayList<>();
    private List<SelectItem> productoTarget = new ArrayList<>();
    private DualListModel<SelectItem> dualListModel;


    //	Session session = getContablesSessionBean().getSessionTrace();

//    @PostConstruct
//    protected void init() throws Exception {
//        getDualListModel();
//    }



    public TemaPage() {
        super(true);
        dualListModel = new DualListModel<>(productoSource, productoTarget);
    }

    /**
     * retorna una instancia de Tema
     *
     * @return
     */
    @Override
    protected Tema _getInstance() {
        return new Tema();
    }

    @Override
    protected void _init() {
        super._init();
    }

    /**
     * Se realiza el proceso de busqueda completo de entidades de tipo Tema
     *
     * @return
     */
    @Override
    public Collection<Concepto> _buscarTodos() throws Exception {
        LOGGER.info("Buscar todos tema page guardar");
        Concepto concepto = new Concepto();
        concepto.setEstado("A");
        Collection<Concepto> conceptos = notasContablesManager.getConceptosPorEstado(concepto);
        LOGGER.info("conceptos {}", conceptos);
        return getTemas(conceptos);
    }

    /**
     * Realiza la busqueda de entidades de tipo Tema filtrando segn lo indicado por el usuario
     *
     * @return
     */
    @Override
    public Collection<Concepto> _buscarPorFiltro() throws Exception {
        if (!param.isEmpty()) {
//            LOGGER.info("{} Buscar tema: {}", session.getTraceLog(), param);
        }
        Collection<Concepto> conceptos = notasContablesManager.searchConcepto(param, "A");
        return getTemas(conceptos);
    }

    public String buscarConcepto() {
        conceptos = new ArrayList<SelectItem>();
        try {
            TreeSet<Concepto> set = new TreeSet<Concepto>(notasContablesManager.searchConcepto(concepto, "A"));
            for (Concepto c : set) {
                conceptos.add(new SelectItem(c.getCodigo(), c.getNombre()));
            }
        } catch (Exception e) {
//            LOGGER.error("{} Error al buscar partidas ", session.getTraceLog(), e);
            nuevoMensaje(FacesMessage.SEVERITY_ERROR, "Error consultando los conceptos");

        }
        return null;
    }

    private Collection<Concepto> getTemas(Collection<Concepto> conceptos) throws Exception {
        Collection<Concepto> list = new ArrayList<Concepto>();
        for (Concepto c : conceptos) {
            Tema tema = new Tema();
            tema.setCodigoConcepto(c.getCodigo().intValue());
            c.getTemas().addAll(notasContablesManager.getTemasPorConcepto(tema));

            // esto se hace para mostrar solo conceptos con temas
            // if (!c.getTemas().isEmpty()) {
            list.add(c);
            // }
        }
        return list;
    }

    /**
     * Funcion llamada cuando se desea inciar la edicion o creacion de registros de tipo Tema
     *
     * @return
     */
    @Override
    protected void _editar() throws Exception {
        LOGGER.info("Editar Tema paso 2");
        cuentas = new ArrayList<SelectItem>();
        cuentasContraPartida = new ArrayList<SelectItem>();
        concepto = "";
        partidaContable = "";
        contraPartidaContable = "";
        impuestosSel = new ArrayList<String>();
        productosSel = new ArrayList<String>();

        impuestos = getSelectItemList(notasContablesManager.getCV(Impuesto.class), false);
        productos = getSelectItemList(notasContablesManager.getCV(Producto.class), false);
        LOGGER.info("PRODUCTO value{}", productos.get(7).getValue());
        LOGGER.info("PRODUCTO  label{}", productos.get(7).getLabel());
//        dualListModel = new DualListModel<>(productos, productos);
        conceptos = new ArrayList<SelectItem>();

        if (objActual.getCodigo().intValue() > 0) {
            // se consulta la informacion del objeto en base de datos
            objActual = notasContablesManager.getTema(objActual);

            Concepto concept = new Concepto();
            concept.setCodigo(objActual.getCodigoConcepto());
            concept = notasContablesManager.getConcepto(concept);
            conceptos.add(new SelectItem(concept.getCodigo(), concept.getNombre()));

            // consulta para mostrar los datos de partida y contrapartida
            partidaContable = objActual.getPartidaContable();
            contraPartidaContable = objActual.getContraPartidaContable();
            buscarPartidaContable();
            buscarContraPartidaContable();

            // busqueda de productos e impuestos
            Collection<TemaProducto> productosTema = notasContablesManager.getProductosPorTema(objActual.getCodigo().intValue());
            for (TemaProducto tm : productosTema) {
                productosSel.add(tm.getCodigoProducto());
            }
            LOGGER.info("SELECCIONADOS TARGE PICKLIST PASO 3 {}", productosSel);
            Collection<TemaImpuesto> impuestosTema = notasContablesManager.getImpuestosPorTema(objActual.getCodigo().intValue());
            for (TemaImpuesto ti : impuestosTema) {
                impuestosSel.add("" + ti.getCodigoImpuesto());
            }

        } else {
            objActual = new Tema();
        }
    }

    @Override
    protected boolean _guardar() throws Exception {
        Number codInicial = objActual.getCodigo();
        productosSel = new ArrayList<>();
        try {
            Collection<TemaImpuesto> impuestosTema = new ArrayList<TemaImpuesto>();
            Collection<TemaProducto> productosTema = new ArrayList<TemaProducto>();
            LOGGER.info("productos seleccionados 1 {}", productosSel.size());
            LOGGER.info("productos target  {}", dualListModel.getTarget().size());
            for (Object productSelected : dualListModel.getTarget()){
                productosSel.add(productSelected.toString());
            }
            LOGGER.info("<<<<<productos seleccionados 2 {}>>>>>", productosSel.size());
            if (objActual.getContrato1().equals("S") || objActual.getContrato2().equals("S")) {
                for (String cod : productosSel) {
                    TemaProducto tp = new TemaProducto();
                    tp.setCodigoProducto(cod);
                    productosTema.add(tp);
                }
            }
            for (String cod : impuestosSel) {
                TemaImpuesto ti = new TemaImpuesto();
                ti.setCodigoImpuesto(Integer.valueOf(cod));
                impuestosTema.add(ti);
            }
            if (objActual.getCodigo().intValue() <= 0) {
//                LOGGER.info("{} Crea tema: {}", session.getTraceLog(), objActual.getCodigoConcepto() + " " + objActual.getNombre());
                notasContablesManager.addTema(objActual, impuestosTema, productosTema, getCodUsuarioLogueado());
            } else {
//                LOGGER.info("{} Actualiza tema: {}", session.getTraceLog(), objActual.getCodigo() + " " + objActual.getCodigoConcepto() + " " + objActual.getNombre());
                notasContablesManager.updateTema(objActual, impuestosTema, productosTema, getCodUsuarioLogueado());
            }
            return true;
        } catch (Exception e) {
            objActual.setCodigo(codInicial);
//            LOGGER.error("{} Ya existe un Tema con el mismo nombre asociado al mismo concepto: {}", session.getTraceLog(), codInicial, e);
            nuevoMensaje(FacesMessage.SEVERITY_ERROR, "Ya existe un Tema con el mismo nombre asociado al mismo concepto");
            return false;
        }
    }

    @Override
    protected void _validar() throws Exception {
        validador.validarRequerido(objActual.getNombre(), "Nombre del tema");
        validador.validarSeleccion(objActual.getTipoDivisa(), "Divisa");
        // se verifica la seleccion de la partida
        if (objActual.getPartidaContable().trim().isEmpty()) {
            nuevoMensaje(FacesMessage.SEVERITY_WARN, "Partida Contable: No existente en el Plan nico de Cuentas");
        }
        // se verifica la seleccion de la contra partida
        if (objActual.getContraPartidaContable().trim().isEmpty()) {
            nuevoMensaje(FacesMessage.SEVERITY_WARN, "Contra Partida Contable: No existente en el Plan nico de Cuentas");
        }

        // si las partidas estan ok
        if (!getFacesContext().getMessages().hasNext()) {
            // se validan la dupla de partida y contrapartida
            String pc = objActual.getPartidaContable().substring(0, 2);
            String cpc = objActual.getContraPartidaContable().substring(0, 2);

            boolean ok = false;
            if (pc.equals("61") && cpc.equals("62")) {
                ok = true;
            } else if (pc.equals("62") && cpc.equals("61")) {
                ok = true;
            } else if (pc.equals("63") && cpc.equals("64")) {
                ok = true;
            } else if (pc.equals("64") && cpc.equals("63")) {
                ok = true;
            } else if (pc.equals("81") && cpc.equals("83")) {
                ok = true;
            } else if (pc.equals("83") && cpc.equals("81")) {
                ok = true;
            } else if (pc.equals("82") && cpc.equals("84")) {
                ok = true;
            } else if (pc.equals("84") && cpc.equals("82")) {
                ok = true;
            } else if (!pc.equals("61") && !pc.equals("62") && !pc.equals("63") && !pc.equals("64") && !pc.equals("81") && !pc.equals("82") && !pc.equals("83") && !pc.equals("84") && !cpc.equals("61") && !cpc.equals("62") && !cpc.equals("63")
                    && !cpc.equals("64") && !cpc.equals("81") && !cpc.equals("82") && !cpc.equals("83") && !cpc.equals("84")) {
                ok = true;
            }
            if (!ok) {
                nuevoMensaje(FacesMessage.SEVERITY_WARN, "La Partida y ContraPartida no son coincidentes. Por favor verifique:  (61-62), (63-64), (81-83), (82-84)");
            } else {
                validarDivisa(objActual.getPartidaContable());
                validarDivisa(objActual.getContraPartidaContable());
            }
        }
        if (objActual.getCodigoConcepto().intValue() <= 0) {
            nuevoMensaje(FacesMessage.SEVERITY_WARN, "Seleccione un Concepto");
        }
    }

    private void validarDivisa(String cuenta) throws Exception {
        PUC puc = new PUC();
        puc.setNumeroCuenta(cuenta);
        puc = cargaAltamiraManager.getPUC(puc);

        CuentaCOD cuentaCOD = new CuentaCOD();
        cuentaCOD.setCuentaContable(cuenta);
        cuentaCOD = notasContablesManager.getCuentaCODPorCuentaContable(cuentaCOD);

        if (!puc.getDescripcion().equals("")) {
            // Valida Moneda Partida
            String tipoD = objActual.getTipoDivisa();
            // TODO como puc.getTipoMoneda() puede retornal nulo, se va a asumir que es pq se trata de un cero y con base en eso se har lo siguiente
            String tipoM = puc.getTipoMoneda();
            if (tipoD.equals("L")) {
                if (tipoM == null || tipoM.equals("0")) {
                    return;
                } else if (cuentaCOD.getCodigo() != 0) {
                    return;
                }
            } else if (tipoM != null) {
                if (tipoD.equals("E")) {
                    if (tipoM.equals("1") || tipoM.equals("2")) {
                        return;
                    }
                } else if (tipoD.equals("M")) {
                    if (puc.getTipoMoneda().equals("1") || puc.getTipoMoneda().equals("2")) {
                        return;
                    }
                }
            }
        }
        nuevoMensaje(FacesMessage.SEVERITY_WARN, "La cuenta " + cuenta + " no permite afectaciones en el Tipo de Divisa seleccionado");
    }

    public String cambioNat1() {
        if (objActual.getNaturaleza1().equals("D")) {
            objActual.setNaturaleza2("H");
        } else {
            objActual.setNaturaleza2("D");
        }
        return null;
    }

    public String cambioNat2() {
        if (objActual.getNaturaleza2().equals("D")) {
            objActual.setNaturaleza1("H");
        } else {
            objActual.setNaturaleza1("D");
        }
        return null;
    }

    public String buscarPartidaContable() {
        cuentas = getCuentas(partidaContable);
        return null;
    }

    public String buscarContraPartidaContable() {
        cuentasContraPartida = getCuentas(contraPartidaContable);
        return null;
    }

    private List<SelectItem> getCuentas(String cuenta) {
        List<SelectItem> lista = new ArrayList<SelectItem>();
        if (cuenta.trim().length() >= 4) {
            try {
                Collection<PUC> pucList = cargaAltamiraManager.searchPUC(cuenta);
                if (pucList.isEmpty()) {
                    nuevoMensaje(FacesMessage.SEVERITY_WARN, "No se encontró información para el número de cuenta " + cuenta);
                }
                for (PUC puc : pucList) {
                    lista.add(new SelectItem(puc.getNumeroCuenta(), puc.getNumeroCuenta() + " " + puc.getDescripcion()));
                }
            } catch (Exception e) {
                LOGGER.error("{} Error realizando la consulta en el PUC ", e);
                nuevoMensaje(FacesMessage.SEVERITY_ERROR, "Error realizando la consulta en el PUC");

            }
        } else {
            nuevoMensaje(FacesMessage.SEVERITY_WARN, "Indique 4 dgitos o mas para realizar la búsqueda");
        }
        return lista;
    }

    /**
     * Cambia el estado de la instancia seleccionada de tipo Tema
     *
     * @return
     */
    @Override
    public boolean _cambiarEstado() throws Exception {
//        LOGGER.info("{} Cambio estado tema: {}", session.getTraceLog(), notasContablesManager.getTema(objActual).getCodigo() + " " + notasContablesManager.getTema(objActual).getCodigoConcepto() + " " + notasContablesManager.getTema(objActual).getNombre() + " " + notasContablesManager.getTema(objActual).getEstado());
        LOGGER.info("_cambiar estado {}", getUsuarioLogueado());
        LOGGER.info("_cambiar objeto {}", objActual);
        notasContablesManager.changeEstadoTema(notasContablesManager.getTema(objActual), getCodUsuarioLogueado());
        LOGGER.info("cambio estado correctamente????");
//        setDatos(new ArrayList<>(_buscarTodos()));
        return true;
    }

    /**
     * Borra la informacion de la instancia seleccionada de tipo Tema
     *
     * @return
     */
    @Override
    public boolean _borrar() throws Exception {
        try {
//            LOGGER.info("{} Borrar estado tema: {}", session.getTraceLog(), objActual.getCodigo() + " " + objActual.getCodigoConcepto() + " " + objActual.getNombre());
            notasContablesManager.deleteTema(objActual, getCodUsuarioLogueado());
            return true;
        } catch (Exception e) {
//            LOGGER.info("{} No puede eliminar el Tema porque contiene registros asociados: {}", session.getTraceLog(), objActual.getCodigo() + " " + objActual.getNombre(), e);
            nuevoMensaje(FacesMessage.SEVERITY_ERROR, "No puede eliminar el Tema porque contiene registros asociados");

            return false;
        }
    }

    @Override
    protected String _getPage() {
        return TEMA;
    }

    public List<SelectItem> getProductos() {
        return productos;
    }

    public void setProductos(List<SelectItem> productos) {
        this.productos = productos;
    }

    public List<SelectItem> getImpuestos() {
        return impuestos;
    }

    public void setImpuestos(List<SelectItem> impuestos) {
        this.impuestos = impuestos;
    }

    public List<SelectItem> getCuentas() {
        return cuentas;
    }

    public void setCuentas(List<SelectItem> cuentas) {
        this.cuentas = cuentas;
    }

    public List<SelectItem> getConceptos() {
        return conceptos;
    }

    public void setConceptos(List<SelectItem> conceptos) {
        this.conceptos = conceptos;
    }

    public List<SelectItem> getCuentasContraPartida() {
        return cuentasContraPartida;
    }

    public void setCuentasContraPartida(List<SelectItem> cuentasContraPartida) {
        this.cuentasContraPartida = cuentasContraPartida;
    }

    public String getPartidaContable() {
        return partidaContable;
    }

    public void setPartidaContable(String partidaContable) {
        this.partidaContable = partidaContable;
    }

    public String getContraPartidaContable() {
        return contraPartidaContable;
    }

    public void setContraPartidaContable(String contraPartidaContable) {
        this.contraPartidaContable = contraPartidaContable;
    }

    public List<String> getImpuestosSel() {
        return impuestosSel;
    }

    public void setImpuestosSel(List<String> impuestosSel) {
        this.impuestosSel = impuestosSel;
    }

    public List<String> getProductosSel() {
        return productosSel;
    }

    public void setProductosSel(List<String> productosSel) {
        this.productosSel = productosSel;
    }

    public String getConcepto() {
        return concepto;
    }

    public void setConcepto(String concepto) {
        this.concepto = concepto;
    }

    public DualListModel<SelectItem> getDualListModel() {
        LOGGER.info("<<<<<<<<<<<<<<get dual list source {}>>>>>>>>>>>>>> ", dualListModel.getSource().size());
        LOGGER.info("<<<<<<<<<<<<<<get dual list target {}>>>>>>>>>>>>>> ", dualListModel.getTarget().size());
        productoSource = new ArrayList<>();
        productoTarget = new ArrayList<>();
        for (SelectItem producto : productos) {
            if (productosSel.contains(producto.getValue()))
                productoTarget.add(producto);
            else
                productoSource.add(producto);
        }
        dualListModel = new DualListModel<>(productoSource, productoTarget);
        return dualListModel;
    }

    public void setDualListModel(DualListModel<SelectItem> dualListModel) {
        this.dualListModel = dualListModel;
    }
}
