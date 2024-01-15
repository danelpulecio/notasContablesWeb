package co.com.bbva.app.notas.contables.jsf.parametros;

import co.com.bbva.app.notas.contables.dto.TipoEvento;
import co.com.bbva.app.notas.contables.session.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.inject.Named;
import java.util.ArrayList;
import java.util.Collection;

/**
 * <p>
 * Pagina para manejar la administracin de parametros relacionados con la entidad TipoEvento
 * </p>
 */
@SessionScoped
@Named
public class TipoEventoPage extends GeneralParametrosPage<TipoEvento, TipoEvento> {

    private static final long serialVersionUID = 1L;

    private static final Logger LOGGER = LoggerFactory.getLogger(TipoEventoPage.class);

//	Session session = getContablesSessionBean().getSessionTrace();

    @PostConstruct
    public void init() throws Exception {
        super._init();
        setDatos(new ArrayList<>(_buscarTodos()));
    }

    public TipoEventoPage() {
        super(true);
    }

    /**
     * retorna una instancia de TipoEvento
     *
     * @return
     */
    @Override
    protected TipoEvento _getInstance() {
        return new TipoEvento();
    }

//    @Override
//    protected void _init() {
//        super._init();
//    }

    /**
     * Se realiza el proceso de busqueda completo de entidades de tipo TipoEvento
     *
     * @return
     */
    @Override
    public Collection<TipoEvento> _buscarTodos() throws Exception {
        return notasContablesManager.getTiposEvento();
    }

    /**
     * Realiza la busqueda de entidades de tipo TipoEvento filtrando segn lo indicado por el usuario
     *
     * @return
     */
    @Override
    public Collection<TipoEvento> _buscarPorFiltro() throws Exception {
        if (!param.isEmpty()) {
//			LOGGER.info("{} Buscar tipo evento: {}", session.getTraceLog(),param );
        }
        return notasContablesManager.searchTipoEvento(param);
    }

    /**
     * Funcion llamada cuando se desea inciar la edicion o creacion de registros de tipo TipoEvento
     *
     * @return
     */
    @Override
    protected void _editar() throws Exception {
        objActual = new TipoEvento();
    }

    @Override
    protected boolean _guardar() throws Exception {
        Number codInicial = objActual.getCodigo();
        try {
            // si se trata de un objeto nuevo
            if (objActual.getCodigo().intValue() <= 0) {
//				LOGGER.info("{} Crea tipo evento: {}", session.getTraceLog(),objActual.getNombre() );
                notasContablesManager.addTipoEvento(objActual, getCodUsuarioLogueado());
            } else {// si se trata de una actualizacion y el cambio el valido
//				LOGGER.info("{} Actualiza tipo evento: {}", session.getTraceLog(),objActual.getCodigo() +" "+objActual.getNombre() );
                notasContablesManager.updateTipoEvento(objActual, getCodUsuarioLogueado());
            }
            return true;
        } catch (Exception e) {
            objActual.setCodigo(codInicial);
//			LOGGER.error("{} Ya existe un tipo de evento con el mismo nombre: {}", session.getTraceLog(),codInicial , e );
            nuevoMensaje(FacesMessage.SEVERITY_ERROR, "Ya existe un tipo de evento con el mismo nombre");
            return false;
        }
    }

    @Override
    protected void _validar() throws Exception {
        validador.validarRequerido(objActual.getNombre(), "nombre");
    }

    /**
     * Cambia el estado de la instancia seleccionada de tipo TipoEvento
     *
     * @return
     */
    @Override
    public boolean _cambiarEstado() throws Exception {
//		LOGGER.info("{} Cambio estado tipo evento: {}", session.getTraceLog(),notasContablesManager.getTipoEvento(objActual).getCodigo() +" "+notasContablesManager.getTipoEvento(objActual).getEstado() );
        notasContablesManager.changeEstadoTipoEvento(notasContablesManager.getTipoEvento(objActual), getCodUsuarioLogueado());
        setDatos(new ArrayList<>(_buscarTodos()));
        return true;
    }

    /**
     * Borra la informacion de la instancia seleccionada de tipo TipoEvento
     *
     * @return
     */
    @Override
    public boolean _borrar() throws Exception {
//		LOGGER.info("{} Borra tipo evento: {}", session.getTraceLog(),objActual.getCodigo() +" "+objActual.getNombre() );
        notasContablesManager.deleteTipoEvento(objActual, getCodUsuarioLogueado());
        return true;
    }

    @Override
    protected String _getPage() {
        return TIPO_EVENTO;
    }
}
