package co.com.bbva.app.notas.contables.jsf.parametros;

import co.com.bbva.app.notas.contables.dto.TemaAutorizacion;
import co.com.bbva.app.notas.contables.session.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.inject.Named;
import java.util.Collection;

/**
 * <p>
 * Pagina para manejar la administracin de parametros relacionados con la entidad TemaAutorizacion
 * </p>
 * 
 */
@SessionScoped
@Named
public class TemaAutorizacionPage extends GeneralParametrosPage<TemaAutorizacion, TemaAutorizacion> {


	String param = getParam();
	private static final long serialVersionUID = 1L;

	private static final Logger LOGGER = LoggerFactory.getLogger(TemaAutorizacionPage.class);

	Session session = getContablesSessionBean().getSessionTrace();

	public TemaAutorizacionPage() {
		super(true);
	}

	/**
	 * retorna una instancia de TemaAutorizacion
	 * 
	 * @return
	 */
	@Override
	protected TemaAutorizacion _getInstance() {
		return new TemaAutorizacion();
	}

	@Override
	protected void _init() {
		super._init();
	}

	/**
	 * Se realiza el proceso de busqueda completo de entidades de tipo TemaAutorizacion
	 * 
	 * @return
	 */
	@Override
	public Collection<TemaAutorizacion> _buscarTodos() throws Exception {
		return notasContablesManager.getTemasAutorizacion();
	}

	/**
	 * Realiza la busqueda de entidades de tipo TemaAutorizacion filtrando segn lo indicado por el usuario
	 * 
	 * @return
	 */
	@Override
	public Collection<TemaAutorizacion> _buscarPorFiltro() throws Exception {
		if(!param.isEmpty()){
			LOGGER.info("{} Buscar tema autorizacion: {}", session.getTraceLog(),param );
		}
		return notasContablesManager.searchTemaAutorizacion(param);
	}

	/**
	 * Funcion llamada cuando se desea inciar la edicion o creacion de registros de tipo TemaAutorizacion
	 * 
	 * @return
	 */
	@Override
	protected void _editar() throws Exception {
		objActual = new TemaAutorizacion();
	}

	@Override
	protected boolean _guardar() throws Exception {
		Number codInicial = objActual.getCodigo();
		try {
			// si se trata de un objeto nuevo
			if (objActual.getCodigo().intValue() <= 0) {
				LOGGER.info("{} Crea tema autorizacion: {}", session.getTraceLog(),objActual.getNombre() );
				notasContablesManager.addTemaAutorizacion(objActual, getCodUsuarioLogueado());
			} else {// si se trata de una actualizacion y el cambio el valido
				LOGGER.info("{} Actualiza tema autorizacion: {}", session.getTraceLog(),objActual.getCodigo()+ " "+ objActual.getNombre()  );
				notasContablesManager.updateTemaAutorizacion(objActual, getCodUsuarioLogueado());
			}
			return true;
		} catch (Exception e) {
			objActual.setCodigo(codInicial);
			LOGGER.error("{} Ya existe un tema de autorizacin con ese nombre: {}", session.getTraceLog(),codInicial ,e );
			nuevoMensaje(FacesMessage.SEVERITY_ERROR, "Ya existe un tema de autorizacin con ese nombre");
			return false;
		}
	}

	@Override
	protected void _validar() throws Exception {
		validador.validarRequerido(objActual.getNombre(), "nombre");
	}

	/**
	 * Cambia el estado de la instancia seleccionada de tipo TemaAutorizacion
	 * 
	 * @return
	 */
	@Override
	public boolean _cambiarEstado() throws Exception {
		LOGGER.info("{} Cambia estado tema autorizacion: {}", session.getTraceLog(),notasContablesManager.getTemaAutorizacion(objActual).getCodigo()+ " "+ notasContablesManager.getTemaAutorizacion(objActual).getNombre() + " "+ notasContablesManager.getTemaAutorizacion(objActual).getEstado()   );
		notasContablesManager.changeEstadoTemaAutorizacion(notasContablesManager.getTemaAutorizacion(objActual), getCodUsuarioLogueado());
		return true;
	}

	/**
	 * Borra la informacion de la instancia seleccionada de tipo TemaAutorizacion
	 * 
	 * @return
	 */
	@Override
	public boolean _borrar() throws Exception {
		LOGGER.info("{} Borra tema autorizacion: {}", session.getTraceLog(),objActual.getCodigo()+ " "+ objActual.getNombre() + " "+ objActual.getEstado()   );
		notasContablesManager.deleteTemaAutorizacion(objActual, getCodUsuarioLogueado());
		return true;
	}

	@Override
	protected String _getPage() {
		return TEMA_AUTORIZACION;
	}
}
