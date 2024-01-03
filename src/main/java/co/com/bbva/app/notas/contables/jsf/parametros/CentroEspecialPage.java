package co.com.bbva.app.notas.contables.jsf.parametros;

import co.com.bbva.app.notas.contables.carga.dto.Sucursal;
import co.com.bbva.app.notas.contables.dto.CentroEspecial;
import co.com.bbva.app.notas.contables.session.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.model.SelectItem;
import javax.inject.Named;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.TreeMap;


/**
 * <p>
 * Pagina para manejar la administracin de parametros relacionados con la entidad CentroEspecial
 * </p>
 *
 */
@SessionScoped
@Named
public class CentroEspecialPage extends GeneralParametrosPage<CentroEspecial, CentroEspecial> {

	private static final long serialVersionUID = 1L;
	protected List<SelectItem> sucursales;

	private static final Logger LOGGER = LoggerFactory.getLogger(CentroEspecialPage.class);

	//	Session session = getContablesSessionBean().getSessionTrace();
	@PostConstruct
	public void init() throws Exception {
		setDatos(new ArrayList<>(_buscarTodos()));
		LOGGER.info("postConstructo datos {}", getDatos().size());
	}

	public CentroEspecialPage() {
		super(true);
	}

	/**
	 * retorna una instancia de CentroEspecial
	 *
	 * @return
	 */
	@Override
	protected CentroEspecial _getInstance() {
		return new CentroEspecial();
	}


	@Override
	protected void _init() {
		super._init();
		consultarListasAuxiliares();
	}

	/**
	 * Se realiza el proceso de busqueda completo de entidades de tipo CentroEspecial
	 *
	 * @return
	 */
	@Override
	public Collection<CentroEspecial> _buscarTodos() throws Exception {
		return notasContablesManager.getCentrosEspeciales();
	}

	/**
	 * Realiza la busqueda de entidades de tipo CentroEspecial filtrando segn lo indicado por el usuario
	 *
	 * @return
	 */
	@Override
	public Collection<CentroEspecial> _buscarPorFiltro() throws Exception {
		if(!param.isEmpty()){
			LOGGER.info("{} Buscar centro especial: {}",param );
		}
		return notasContablesManager.searchCentroEspecial(param);
	}

	/**
	 * Funcion llamada cuando se desea inciar la edicion o creacion de registros de tipo CentroEspecial
	 *
	 * @return
	 */
	@Override
	protected void _editar() throws Exception {
		objActual = new CentroEspecial();
	}

	@Override
	protected boolean _guardar() throws Exception {
		Number codInicial = objActual.getCodigo();
		try {
			if (objActual.getCodigo().intValue() <= 0) {
				LOGGER.info("{} Crea centro especial: {}",  objActual.getNombreSucursal()+ " "+ objActual.getCodigoSucursal() );
				notasContablesManager.addCentroEspecial(objActual, getCodUsuarioLogueado());
			} else {
				LOGGER.info("{} Actualiza centro especial: {}",  objActual.getCodigo() +" "+  objActual.getNombreSucursal()+ " "+ objActual.getCodigoSucursal() );
				notasContablesManager.updateCentroEspecial(objActual, getCodUsuarioLogueado());
			}
		} catch (Exception e) {
			objActual.setCodigo(codInicial);
			LOGGER.error("{} Ya existe un centro especial con la misma sucursal: {}",   codInicial , e );
			nuevoMensaje(FacesMessage.SEVERITY_ERROR, "Ya existe un Centro Especial con la misma sucursal");
			return false;
		}
		return true;
	}

	@Override
	protected void _validar() throws Exception {
		validador.validarRequerido(objActual.getCodigoSucursal(), "Sucursal");
	}

	/**
	 * Cambia el estado de la instancia seleccionada de tipo CentroEspecial
	 *
	 * @return
	 */
	@Override
	public boolean _cambiarEstado() throws Exception {
		LOGGER.info(" getCodUsuarioLogueado : ->>>",  getCodUsuarioLogueado());

		LOGGER.info("{} Cambia el estado centro especial: {}",  notasContablesManager.getCentroEspecial(objActual).getCodigo() +" "+  notasContablesManager.getCentroEspecial(objActual).getCodigoSucursal()+ " "+ notasContablesManager.getCentroEspecial(objActual).getEstado() );
		notasContablesManager.changeEstadoCentroEspecial(notasContablesManager.getCentroEspecial(objActual), getCodUsuarioLogueado());
		return true;
	}

	/**
	 * Borra la informacion de la instancia seleccionada de tipo CentroEspecial
	 *
	 * @return
	 */
	@Override
	public boolean _borrar() throws Exception {
		try {
			LOGGER.info("{} Borra Centro Especial: {}",   objActual.getCodigo() +" "+  objActual.getCodigoSucursal() );
			notasContablesManager.deleteCentroEspecial(objActual, getCodUsuarioLogueado());
		} catch (Exception e) {
			LOGGER.error("{} No puede eliminar el Centro Especial porque contiene registros asociados: {}",   objActual.getCodigo() ,e );
			nuevoMensaje(FacesMessage.SEVERITY_WARN, "No puede eliminar el Centro Especial porque contiene registros asociados");
			return false;
		}
		return true;
	}

	private void consultarListasAuxiliares() {
		if (esUltimaFase()) {
			try {
				sucursales = getSelectItemList(new TreeMap<String, String>(notasContablesManager.getCV(Sucursal.class)), true);
			} catch (Exception e) {
				LOGGER.error("{} Error al inicializar el mdulo de administracin de montos autorizados ", e );
				nuevoMensaje(FacesMessage.SEVERITY_ERROR, "Error al inicializar el mdulo de administracin de montos autorizados" );

			}
		}
	}

	@Override
	protected String _getPage() {
		return CENTRO_ESPECIAL;
	}

	public List<SelectItem> getSucursales() {
		return sucursales;
	}

	public void setSucursales(List<SelectItem> sucursales) {
		this.sucursales = sucursales;
	}
}
