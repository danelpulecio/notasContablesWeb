package co.com.bbva.app.notas.contables.jsf.parametros;

import co.com.bbva.app.notas.contables.dto.Padrino;
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
 * Pagina para manejar la administración de parametros relacionados con la entidad Padrino
 * </p>
 * 
 */
@SessionScoped
@Named
public class PadrinoPage extends GeneralParametrosPage<Padrino, Padrino> {

	private static final long serialVersionUID = 1L;

	private static final Logger LOGGER = LoggerFactory.getLogger(PadrinoPage.class);
	private static final int cs_CODIGO_PADRINOS = 4;
	private List<SelectItem> sucursales;
	private List<SelectItem> usuarios;

//	Session session = getContablesSessionBean().getSessionTrace();

	@PostConstruct
	public void init() throws Exception {
		super._init();
		setDatos(new ArrayList<>(_buscarTodos()));
		LOGGER.info("postConstructo datos {}", getDatos().size());
		consultarListasAuxiliares();
	}

	public PadrinoPage() {
		super(true);
	}

	/**
	 * retorna una instancia de Padrino
	 * 
	 * @return
	 */
	@Override
	protected Padrino _getInstance() {
		return new Padrino();
	}

//	@Override
//	protected void _init() {
//		super._init();
//		consultarListasAuxiliares();
//	}

	/**
	 * Se realiza el proceso de busqueda completo de entidades de tipo Padrino
	 * 
	 * @return
	 */
	@Override
	public Collection<Padrino> _buscarTodos() throws Exception {
		return notasContablesManager.getPadrinos();
	}

	/**
	 * Realiza la busqueda de entidades de tipo Padrino filtrando segn lo indicado por el usuario
	 * 
	 * @return
	 */
	@Override
	public Collection<Padrino> _buscarPorFiltro() throws Exception {
		if(!param.isEmpty()){
//			LOGGER.info("{} Buscar padrino: {}", session.getTraceLog(),param );
		}
		return notasContablesManager.searchPadrino(param);
	}

	/**
	 * Funcion llamada cuando se desea inciar la edicion o creacion de registros de tipo Padrino
	 * 
	 * @return
	 */
	@Override
	protected void _editar() throws Exception {
		objActual = new Padrino();
		usuarios = new ArrayList<SelectItem>();
	}

	@Override
	protected boolean _guardar() throws Exception {
		Number codInicial = objActual.getCodigo();
		try {
			if (objActual.getCodigo().intValue() <= 0) {
//				LOGGER.info("{} Crea padrino: {}", session.getTraceLog(),objActual.getCodigoUsuario()+" "+ objActual.getCodigoUnidadAnalisis());
				notasContablesManager.addPadrino(objActual, getCodUsuarioLogueado());
			} else {
//				LOGGER.info("{} Actualiza padrino: {}", session.getTraceLog(),objActual.getCodigo()+" "+objActual.getCodigoUsuario()+" "+ objActual.getCodigoUnidadAnalisis() );
				notasContablesManager.updatePadrino(objActual, getCodUsuarioLogueado());
			}
		} catch (Exception e) {
			objActual.setCodigo(codInicial);
//			LOGGER.error("{} Ya existe un Padrino con los mismo datos: {}", session.getTraceLog(),codInicial , e);
			nuevoMensaje(FacesMessage.SEVERITY_ERROR, "Ya existe un Padrino con los mismo datos");

			return false;
		}
		return true;
	}

	@Override
	protected void _validar() throws Exception {
		validador.validarRequerido(objActual.getCodigoUsuario(), "Usuario");
		validador.validarRequerido(objActual.getCodigoUnidadAnalisis(), "Unidad de anlisis");
	}

	/**
	 * Cambia el estado de la instancia seleccionada de tipo Padrino
	 * 
	 * @return
	 */
	@Override
	public boolean _cambiarEstado() throws Exception {
//		LOGGER.info("{} Cambia estado padrino: {}", session.getTraceLog(),notasContablesManager.getPadrino(objActual).getCodigo()+" "+notasContablesManager.getPadrino(objActual).getEstado() );
		notasContablesManager.changeEstadoPadrino(notasContablesManager.getPadrino(objActual), getCodUsuarioLogueado());
		setDatos(new ArrayList<>(_buscarTodos()));
		return true;
	}

	/**
	 * Borra la informacion de la instancia seleccionada de tipo Padrino
	 * 
	 * @return
	 */
	@Override
	public boolean _borrar() throws Exception {
//		LOGGER.info("{} Borra padrino: {}", session.getTraceLog(),objActual.getCodigo()+" "+objActual.getCodigoUsuario()+" "+ objActual.getCodigoUnidadAnalisis() );
		notasContablesManager.deletePadrino(objActual, getCodUsuarioLogueado());
		return true;
	}

	private void consultarListasAuxiliares() {
		//TODO: if (esUltimaFase()) {
			try {
				sucursales = getSelectItemList(cargaAltamiraManager.getCVSucursal(), false);
			} catch (Exception e) {
				LOGGER.error("{} Error al inicializar el mdulo de administración de padrinos ",  e);
				nuevoMensaje(FacesMessage.SEVERITY_ERROR, "Error al inicializar el mdulo de administración de padrinos");

			}
//		}
	}

	public String buscarUsuarios() {
		try {
			usuarios = getSelectItemList(new TreeMap<String, String>(cargaAltamiraManager.getCVUsuarioAltamira(objActual.getCodigoUnidadAnalisis(), cs_CODIGO_PADRINOS, "A")), false);
			if (usuarios.isEmpty()) {
				nuevoMensaje(FacesMessage.SEVERITY_WARN, "No se han encontrado usuarios en el sistema para la sucursal seleccionada");
			}
		} catch (Exception e) {
			LOGGER.error("{} Error al iniciar los usuarios ", e);
			nuevoMensaje(FacesMessage.SEVERITY_ERROR, "Error al iniciar los usuarios");
		}
		return null;
	}

	@Override
	protected String _getPage() {
		return PADRINO;
	}

	public List<SelectItem> getSucursales() {
		return sucursales;
	}

	public void setSucursales(List<SelectItem> sucursales) {
		this.sucursales = sucursales;
	}

	public List<SelectItem> getUsuarios() {
		return usuarios;
	}

	public void setUsuarios(List<SelectItem> usuarios) {
		this.usuarios = usuarios;
	}
}
