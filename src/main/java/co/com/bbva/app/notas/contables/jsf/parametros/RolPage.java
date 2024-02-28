package co.com.bbva.app.notas.contables.jsf.parametros;

import co.com.bbva.app.notas.contables.dto.Rol;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import java.util.ArrayList;
import java.util.Collection;

/**
 * <p>
 * Pagina para manejar la administracion de parametros relacionados con la entidad Rol
 * </p>
 * 
 */
@ViewScoped
@Named
public class RolPage extends GeneralParametrosPage<Rol, Rol> {

	private static final long serialVersionUID = 1L;

	private static final Logger LOGGER = LoggerFactory.getLogger(RolPage.class);

//	Session session = getContablesSessionBean().getSessionTrace();
	@PostConstruct
	public void init() throws Exception {
		super._init();
		setDatos(new ArrayList<>(_buscarTodos()));
		LOGGER.info("postConstructo datos {}", getDatos().size());
	}
	public RolPage() {
		super(true);
	}

	/**
	 * retorna una instancia de Rol
	 * 
	 * @return
	 */
	@Override
	protected Rol _getInstance() {
		return new Rol();
	}

	@Override
	protected void _init() {
		super._init();
	}

	/**
	 * Se realiza el proceso de busqueda completo de entidades de tipo Rol
	 * 
	 * @return
	 */
	@Override
	public Collection<Rol> _buscarTodos() throws Exception {
		return notasContablesManager.getRoles();
	}

	/**
	 * Realiza la busqueda de entidades de tipo Rol filtrando segn lo indicado por el usuario
	 * 
	 * @return
	 */
	@Override
	public Collection<Rol> _buscarPorFiltro() throws Exception {
		if(!param.isEmpty()){
			LOGGER.info("{} Buscar rol: {}", param );
		}
		return notasContablesManager.searchRol(param);
	}

	/**
	 * Funcion llamada cuando se desea inciar la edicion o creacion de registros de tipo Rol
	 * 
	 * @return
	 */
	@Override
	protected void _editar() throws Exception {
		// TODO agregar funcionalidad de edicion de datos
		// no se debera permitir la creacion o el borrado de roles
	}

	@Override
	protected boolean _guardar() throws Exception {
		LOGGER.info("{} Actualiza rol: {}", objActual.getCodigo()+ " "+ objActual.getNombre() );
		notasContablesManager.updateRol(objActual, getCodUsuarioLogueado());
		return true;
	}

	@Override
	protected void _validar() throws Exception {
		if (objActual.getNombre().trim().isEmpty()) {
			nuevoMensaje(FacesMessage.SEVERITY_WARN, "Debe indicar un nombre para el rol");
		}
	}

	/**
	 * Cambia el estado de la instancia seleccionada de tipo Rol
	 * 
	 * @return
	 */
	@Override
	public boolean _cambiarEstado() throws Exception {
		LOGGER.info("{} Cambio estado rol: {}", notasContablesManager.getRol(objActual).getCodigo()+ " "+ notasContablesManager.getRol(objActual).getNombre() + " "+ notasContablesManager.getRol(objActual).getEstado() );
		notasContablesManager.changeEstadoRol(notasContablesManager.getRol(objActual), getCodUsuarioLogueado());
		setDatos(new ArrayList<>(_buscarTodos()));
		return true;
	}

	/**
	 * Borra la informacion de la instancia seleccionada de tipo Rol
	 * 
	 * @return
	 */
	@Override
	public boolean _borrar() throws Exception {
		// notasContablesManager.deleteRol(objActual, getCodUsuarioLogueado());

		// no se debera permitir la creacion o el borrado de roles
		return true;
	}

	@Override
	protected String _getPage() {
		return ROLES;
	}
}
