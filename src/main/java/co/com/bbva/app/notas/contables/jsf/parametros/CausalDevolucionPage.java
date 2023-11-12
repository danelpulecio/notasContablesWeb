package co.com.bbva.app.notas.contables.jsf.parametros;

import co.com.bbva.app.notas.contables.dto.CausalDevolucion;
import co.com.bbva.app.notas.contables.session.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.inject.Named;
import java.util.Collection;

/**
 * <p>
 * Pagina para manejar la administracin de parametros relacionados con la entidad CausalDevolucion
 * </p>
 * 
 */
@SessionScoped
@Named
public class CausalDevolucionPage extends GeneralParametrosPage<CausalDevolucion, CausalDevolucion> {

	String param = getParam();
	private static final long serialVersionUID = 1L;

	private static final Logger LOGGER = LoggerFactory.getLogger(CausalDevolucionPage.class);

	Session session = getContablesSessionBean().getSessionTrace();

	public CausalDevolucionPage() {
		super(true);
	}

	/**
	 * retorna una instancia de CausalDevolucion
	 * 
	 * @return
	 */
	@Override
	protected CausalDevolucion _getInstance() {
		return new CausalDevolucion();
	}

	@Override
	protected void _init() {
		super._init();
	}

	/**
	 * Se realiza el proceso de busqueda completo de entidades de tipo CausalDevolucion
	 * 
	 * @return
	 */
	@Override
	public Collection<CausalDevolucion> _buscarTodos() throws Exception {
		return notasContablesManager.getCausalesDevolucion();
	}

	/**
	 * Realiza la busqueda de entidades de tipo CausalDevolucion filtrando segn lo indicado por el usuario
	 * 
	 * @return
	 */
	@Override
	public Collection<CausalDevolucion> _buscarPorFiltro() throws Exception {
		if(!param.isEmpty()){
			LOGGER.info("{} Buscar causal devolucin: {}", session.getTraceLog(),param );
		}
		return notasContablesManager.searchCausalDevolucion(param);
	}

	/**
	 * Funcion llamada cuando se desea inciar la edicion o creacion de registros de tipo CausalDevolucion
	 * 
	 * @return
	 */
	@Override
	protected void _editar() throws Exception {
		objActual = new CausalDevolucion();
	}

	@Override
	protected boolean _guardar() throws Exception {
		Number codInicial = objActual.getCodigo();
		try {
			if (objActual.getCodigo().intValue() <= 0) {
				LOGGER.info("{} Crea Causal devolucion: {}", session.getTraceLog(),  objActual.getNombre()  );
				notasContablesManager.addCausalDevolucion(objActual, getCodUsuarioLogueado());
			} else {
				LOGGER.info("{} Actualiza Causal devolucion: {}", session.getTraceLog(),  objActual.getCodigo() +" - " +objActual.getNombre()  );
				notasContablesManager.updateCausalDevolucion(objActual, getCodUsuarioLogueado());
			}

			return true;
		} catch (Exception e) {
			objActual.setCodigo(codInicial);
			LOGGER.error("{} Ya existe otra causal de devolucin con la misma informacin {}", session.getTraceLog() , e );
			nuevoMensaje(FacesMessage.SEVERITY_ERROR, "Ya existe otra causal de devolucin con la misma informacin");
			return false;
		}
	}

	@Override
	protected void _validar() throws Exception {
		validador.validarRequerido(objActual.getNombre(), "nombre");
	}

	/**
	 * Cambia el estado de la instancia seleccionada de tipo CausalDevolucion
	 * 
	 * @return
	 */
	@Override
	public boolean _cambiarEstado() throws Exception {
		LOGGER.info("{} Cambia Estado causal devolucion: {}", session.getTraceLog(),  notasContablesManager.getCausalDevolucion(objActual).getCodigo() + " - " +notasContablesManager.getCausalDevolucion(objActual).getEstado() );
		notasContablesManager.changeEstadoCausalDevolucion(notasContablesManager.getCausalDevolucion(objActual), getCodUsuarioLogueado());
		return true;
	}

	/**
	 * Borra la informacion de la instancia seleccionada de tipo CausalDevolucion
	 * 
	 * @return
	 */
	@Override
	public boolean _borrar() throws Exception {
		LOGGER.info("{} Elimina Causal devolucion {}", session.getTraceLog(),  objActual.getCodigo() +" - " +objActual.getNombre()  );
		notasContablesManager.deleteCausalDevolucion(objActual, getCodUsuarioLogueado());
		return true;
	}

	@Override
	protected String _getPage() {
		return CAUSAL_DEVOLUCION;
	}
}
