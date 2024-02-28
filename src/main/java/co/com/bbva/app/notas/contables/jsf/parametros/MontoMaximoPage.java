package co.com.bbva.app.notas.contables.jsf.parametros;

import co.com.bbva.app.notas.contables.dto.MontoMaximo;
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
 * Pagina para manejar la administración de parametros relacionados con la entidad MontoMaximo
 * </p>
 * 
 */
@ViewScoped
@Named
public class MontoMaximoPage extends GeneralParametrosPage<MontoMaximo, MontoMaximo> {

	private static final long serialVersionUID = 1L;

	private static final Logger LOGGER = LoggerFactory.getLogger(MontoMaximoPage.class);

//	Session session = getContablesSessionBean().getSessionTrace();
	@PostConstruct
	public void init() throws Exception {
		super._init();
		setDatos(new ArrayList<>(_buscarTodos()));
		LOGGER.info("postConstructo datos {}", getDatos().size());
	}

	public MontoMaximoPage() {
		super(true);
	}

	/**
	 * retorna una instancia de MontoMaximo
	 * 
	 * @return
	 */
	@Override
	protected MontoMaximo _getInstance() {
		return new MontoMaximo();
	}

	@Override
	protected void _init() {
		super._init();
	}

	/**
	 * Se realiza el proceso de busqueda completo de entidades de tipo MontoMaximo
	 * 
	 * @return
	 */
	@Override
	public Collection<MontoMaximo> _buscarTodos() throws Exception {
		return notasContablesManager.getMontoMaximos();
	}

	/**
	 * Realiza la busqueda de entidades de tipo MontoMaximo filtrando segn lo indicado por el usuario
	 * 
	 * @return
	 */
	@Override
	public Collection<MontoMaximo> _buscarPorFiltro() throws Exception {
		return notasContablesManager.getMontoMaximos();

	}

	/**
	 * Funcion llamada cuando se desea inciar la edicion o creacion de registros de tipo MontoMaximo
	 * 
	 * @return
	 */
	@Override
	protected void _editar() throws Exception {
		objActual = new MontoMaximo();
	}

	@Override
	protected boolean _guardar() throws Exception {
		int codInicial = objActual.getCodigo();
		try {
			if (objActual.getCodigo() <= 0) {
				LOGGER.info("{} Crea monto maximo: {}", objActual.getNombre()+" "+objActual.getDivisa()+" "+ objActual.getMontoMaximo() );
				notasContablesManager.addMontoMaximo(objActual, getCodUsuarioLogueado());
			} else {
				LOGGER.info("{} Actualiza monto maximo: {}", objActual.getCodigo()+" "+ objActual.getNombre()+" "+objActual.getDivisa()+" "+ objActual.getMontoMaximo() );
				notasContablesManager.updateMontoMaximo(objActual, getCodUsuarioLogueado());
			}
		} catch (Exception e) {
			objActual.setCodigo(codInicial);
			LOGGER.error("{} Ya existe un Monto Mximo con el mismo tipo de moneda: {}", codInicial , e );
			nuevoMensaje(FacesMessage.SEVERITY_ERROR, "Ya existe un Monto Mximo con el mismo tipo de moneda");
			return false;
		}
		return true;
	}

	@Override
	protected void _validar() throws Exception {
		if (objActual.getDivisa().equals("-1")) {
			nuevoMensaje(FacesMessage.SEVERITY_WARN, "Debe seleccionar una divisa");
		}

	}

	/**
	 * Cambia el estado de la instancia seleccionada de tipo MontoMaximo
	 * 
	 * @return
	 */
	@Override
	public boolean _cambiarEstado() throws Exception {
		LOGGER.info("{} Cambia estado monto maximo: {}", notasContablesManager.getMontoMaximo(objActual).getCodigo()+" "+ notasContablesManager.getMontoMaximo(objActual).getEstado() );
		notasContablesManager.changeEstadoMontoMaximo(notasContablesManager.getMontoMaximo(objActual), getCodUsuarioLogueado());
		setDatos(new ArrayList<>(_buscarTodos()));
		return true;
	}

	/**
	 * Borra la informacion de la instancia seleccionada de tipo MontoMaximo
	 * 
	 * @return
	 */
	@Override
	public boolean _borrar() throws Exception {
		LOGGER.info("{} Borra monto maximo: {}", objActual.getCodigo() );
		notasContablesManager.deleteMontoMaximo(objActual, getCodUsuarioLogueado());
		return true;
	}

	@Override
	protected String _getPage() {
		return MONTO_MAXIMO;
	}
}
