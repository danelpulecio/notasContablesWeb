package co.com.bbva.app.notas.contables.jsf.parametros;

import co.com.bbva.app.notas.contables.dto.CommonVO;
import co.com.bbva.app.notas.contables.jsf.carga.GeneralCargaPage;
import co.com.bbva.app.notas.contables.session.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.faces.application.FacesMessage;
import java.util.ArrayList;

/**
 * <p>
 * Page bean that corresponds to a similarly named JSP page. This class contains component definitions (and
 * initialization code) for all components that you have defined on this page, as well as lifecycle methods and event
 * handlers where you may add behavior to respond to incoming events.
 * </p>
 * 
 */
public abstract class GeneralParametrosPage<T extends CommonVO<T>, D> extends GeneralCargaPage<T> {

	private static final long serialVersionUID = -4039536732981625179L;

	protected D objActual;

	// banderas para controlar el cierre de popups
	private boolean ocultarPopupGuardar = false;

	private static final Logger LOGGER = LoggerFactory.getLogger(GeneralCentroPage.class);
	Session session = getContablesSessionBean().getSessionTrace();

	/**
	 * Crea una instnacia del objeto a manejar
	 * 
	 */
	protected abstract D _getInstance();

	/**
	 * Logica adicional para motrar el editor. Propia de cada pgina
	 * 
	 * @throws Exception
	 */
	protected abstract void _editar() throws Exception;

	/**
	 * Logica propia de guardado de informacion
	 * 
	 * @return
	 * @throws Exception
	 */
	protected abstract boolean _guardar() throws Exception;

	/**
	 * Validacion propia de informacion
	 * 
	 * @throws Exception
	 */
	protected abstract void _validar() throws Exception;

	/**
	 * Logica propia de cambio de estado
	 * 
	 * @return
	 * @throws Exception
	 */
	protected abstract boolean _cambiarEstado() throws Exception;

	/**
	 * Logica propia de borrado de datos
	 * 
	 * @return
	 * @throws Exception
	 */
	protected abstract boolean _borrar() throws Exception;

	/**
	 * <p>
	 * Construct a new Page bean instance.
	 * </p>
	 */
	public GeneralParametrosPage(boolean buscarTodos) {
		super(buscarTodos);
	}

	/**
	 * <p>
	 * Automatically managed component initialization. <strong>WARNING:</strong> This method is automatically generated, so
	 * any user-specified code inserted here is subject to being replaced.
	 * </p>
	 *
	 */
	@Override
	protected void _init() {
		super._init();
	}

	/**
	 * Metodo que enmascara la logica de validacion de informacion antes de guardar o actualizar
	 * 
	 * @return
	 * @throws Exception
	 */
	protected boolean datosValidos() throws Exception {
		_validar();
		// valicacion porque getFacesContext().MaxSeverity llena null a pagina
		if (getFacesContext().getMessageList().size() > 0) {
			ocultarPopupGuardar = false;
		}
		return !getFacesContext().getMessages().hasNext();
	}

	/**
	 * Metodo que enmascara la logica encargada de inicializar el editor de informacion
	 * 
	 * @return
	 */
	public String editar() {
		try {
			_editar();
		} catch (Exception e) {
			LOGGER.error("{} Error al inicializar el editor", session.getTraceLog() , e );
			nuevoMensaje(FacesMessage.SEVERITY_ERROR, "Error al inicializar el editor");

		}
		return null;
	}

	/**
	 * Metodo que enmascara la logica encargada de guardar o actualizar la informacion del registro objActual
	 * 
	 * @return
	 */
	public String guardar() {
		try {
			if (datosValidos() && _guardar()) {
				setDatos(new ArrayList<T>(_buscarPorFiltro()));
				nuevoMensaje(FacesMessage.SEVERITY_INFO, "La informacin ha sido guardada correctamente");
				ocultarPopupGuardar = true;
			}
		} catch (Exception e) {
			LOGGER.error("{} Error guardar la informacin", session.getTraceLog() , e );
			nuevoMensaje(FacesMessage.SEVERITY_ERROR, "Error guardar la informacin");

		}
		return null;
	}

	/**
	 * Metodo que enmascara la logica encargada de cambiar el estado del objActual
	 * 
	 * @return
	 */
	public String cambiarEstado() {
		try {
			if (_cambiarEstado()) {
				setDatos(new ArrayList<T>(_buscarPorFiltro()));
				nuevoMensaje(FacesMessage.SEVERITY_INFO, "El estado ha sido cambiado correctamente");
			} else {
				nuevoMensaje(FacesMessage.SEVERITY_WARN, "No es posible cambiar el estado de este registro");
			}
		} catch (Exception e) {
			LOGGER.error("{} Error al cambiar el estado", session.getTraceLog() , e);
			nuevoMensaje(FacesMessage.SEVERITY_ERROR, "Error al cambiar el estado");

		}
		return null;
	}

	/**
	 * Metodo que enmascara la logica encargada del borrado de la informacion del registro objActual
	 * 
	 * @return
	 */
	public String borrar() {
		try {
			if (_borrar()) {
				setDatos(new ArrayList<T>(_buscarPorFiltro()));
				nuevoMensaje(FacesMessage.SEVERITY_INFO, "La informacin ha sido borrada correctamente");
			}
		} catch (Exception e) {
			LOGGER.error("{} Error al borrar la informacin", session.getTraceLog() , e );
			nuevoMensaje(FacesMessage.SEVERITY_ERROR, "Error al borrar la informacin");

		}
		return null;
	}

	public D getObjActual() {
		if (objActual == null) {
			objActual = _getInstance();
		}

		return objActual;
	}

	public void setObjActual(D objActual) {
		this.objActual = objActual;
	}

	/**
	 * @return the ocultarPopupGuardar
	 */
	public boolean isOcultarPopupGuardar() {
		return ocultarPopupGuardar;
	}

	/**
	 * @param ocultarPopupGuardar the ocultarPopupGuardar to set
	 */
	public void setOcultarPopupGuardar(boolean ocultarPopupGuardar) {
		this.ocultarPopupGuardar = ocultarPopupGuardar;
	}

}
