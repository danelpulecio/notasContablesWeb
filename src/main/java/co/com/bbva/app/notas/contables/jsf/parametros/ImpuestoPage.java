package co.com.bbva.app.notas.contables.jsf.parametros;

import co.com.bbva.app.notas.contables.carga.dto.PUC;
import co.com.bbva.app.notas.contables.dto.Impuesto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.model.SelectItem;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import java.util.*;

/**
 * <p>
 * Pagina para manejar la administración de parametros relacionados con la entidad Impuesto
 * </p>
 */
@ViewScoped
@Named
public class ImpuestoPage extends GeneralParametrosPage<Impuesto, Impuesto> {

    private static final long serialVersionUID = 1L;

    private static final Logger LOGGER = LoggerFactory.getLogger(ImpuestoPage.class);

    private List<SelectItem> cuentas = new ArrayList<SelectItem>();
    private String filtroPUC;

    //	Session session = getContablesSessionBean().getSessionTrace();
    @PostConstruct
    public void init() throws Exception {
        super._init();
        setDatos(new ArrayList<>(_buscarTodos()));
        LOGGER.info("postConstructo datos {}", getDatos().size());
    }

    public ImpuestoPage() {
        super(true);
    }

    /**
     * retorna una instancia de Impuesto
     *
     * @return
     */
    @Override
    protected Impuesto _getInstance() {
        return new Impuesto();
    }

    @Override
    protected void _init() {
        super._init();
    }

    public String buscarPartidas() {
        cuentas = new ArrayList<SelectItem>();
        if (filtroPUC.length() < 4) {
            nuevoMensaje(FacesMessage.SEVERITY_WARN, "El filtro debe ser de longitud 4 o superior");
        } else {
            try {
                Map<String, String> mapa = notasContablesManager.getCVBy(co.com.bbva.app.notas.contables.carga.dto.PUC.class, filtroPUC);
                for (final Object key : new TreeSet<String>(mapa.keySet())) {
                    cuentas.add(new SelectItem(key, key + " - " + mapa.get(key)));
                }
            } catch (Exception e) {
                LOGGER.error("{} Error al buscar partidas ",  e);
            }

        }
        return null;
    }

    /**
     * Se realiza el proceso de busqueda completo de entidades de tipo Impuesto
     *
     * @return
     */
    @Override
    public Collection<Impuesto> _buscarTodos() throws Exception {
        return notasContablesManager.getImpuestos();
    }

    /**
     * Realiza la busqueda de entidades de tipo Impuesto filtrando segn lo indicado por el usuario
     *
     * @return
     */
    @Override
    public Collection<Impuesto> _buscarPorFiltro() throws Exception {
        if (!param.isEmpty()) {
            LOGGER.info("{} Buscar impuesto: {}", param);
        }
        return notasContablesManager.searchImpuesto(param);
    }

    /**
     * Funcion llamada cuando se desea inciar la edicion o creacion de registros de tipo Impuesto
     *
     * @return
     */
    @Override
    protected void _editar() throws Exception {
        objActual = new Impuesto();
        filtroPUC = "";
        cuentas = new ArrayList<SelectItem>();
        if (objActual.getPartidaContable() != null && !objActual.getPartidaContable().isEmpty()) {
            filtroPUC = objActual.getPartidaContable();
        }
    }

    @Override
    protected boolean _guardar() throws Exception {
        String cuenta = objActual.getPartidaContable();
        if (cuenta.indexOf('-') > 0) {
            cuenta = cuenta.substring(0, cuenta.indexOf('-') - 1);
        }
        objActual.setPartidaContable(cuenta);

        Number codInicial = objActual.getCodigo();
        try {
            if (objActual.getCodigo().intValue() <= 0) {
                LOGGER.info("{} Crea impuesto: {}",  objActual.getNombre() + " " + objActual.getValor());
                notasContablesManager.addImpuesto(objActual, getCodUsuarioLogueado());
            } else {
                LOGGER.info("{} Actualiza impuesto: {}", objActual.getCodigo() + " " + objActual.getNombre() + " " + objActual.getValor());
                notasContablesManager.updateImpuesto(objActual, getCodUsuarioLogueado());
            }
        } catch (Exception e) {
            objActual.setCodigo(codInicial);
            LOGGER.error("{} Ya existe esa Cuenta registrada: {}",  codInicial, e);
            nuevoMensaje(FacesMessage.SEVERITY_ERROR, "Ya existe esa Cuenta registrada");

            return false;
        }
        return true;

    }

    @Override
    protected void _validar() throws Exception {
        if (objActual.getValor() <= 0 || objActual.getValor() > 100) {
            nuevoMensaje(FacesMessage.SEVERITY_WARN, "Porcentaje incorrecto, verifique por favor");
        }

        String cuenta = objActual.getPartidaContable();
        if (cuenta.indexOf('-') > 0) {
            cuenta = cuenta.substring(0, cuenta.indexOf('-') - 1);
        }

        PUC puc = new PUC();
        puc.setNumeroCuenta(cuenta);
        puc = cargaAltamiraManager.getPUC(puc);
        if (puc.getDescripcion().equals("")) {
            nuevoMensaje(FacesMessage.SEVERITY_WARN, "La cuenta contable no es una cuenta válida");
        }
    }

    /**
     * Cambia el estado de la instancia seleccionada de tipo Impuesto
     *
     * @return
     */
    @Override
    public boolean _cambiarEstado() throws Exception {
        LOGGER.info("{} Cambia estado impuesto: {}",  notasContablesManager.getImpuesto(objActual).getCodigo() + " " + notasContablesManager.getImpuesto(objActual).getNombre() + " " + notasContablesManager.getImpuesto(objActual).getEstado());
        notasContablesManager.changeEstadoImpuesto(notasContablesManager.getImpuesto(objActual), getCodUsuarioLogueado());

        setDatos(new ArrayList<>(_buscarTodos()));
        return true;
    }

    /**
     * Borra la informacion de la instancia seleccionada de tipo Impuesto
     *
     * @return
     */
    @Override
    public boolean _borrar() throws Exception {
        LOGGER.info("{} Borrar estado impuesto: {}", objActual.getCodigo() + " " + objActual.getNombre());
        notasContablesManager.deleteImpuesto(objActual, getCodUsuarioLogueado());
        return true;
    }

    @Override
    protected String _getPage() {
        return IMPUESTO;
    }

    public List<SelectItem> getCuentas() {
        return cuentas;
    }

    public void setCuentas(List<SelectItem> cuentas) {
        this.cuentas = cuentas;
    }

    public String getFiltroPUC() {
        return filtroPUC;
    }

    public void setFiltroPUC(String filtroPUC) {
        this.filtroPUC = filtroPUC;
    }
}
