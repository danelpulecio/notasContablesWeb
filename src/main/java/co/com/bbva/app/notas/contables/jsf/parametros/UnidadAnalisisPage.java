package co.com.bbva.app.notas.contables.jsf.parametros;

import co.com.bbva.app.notas.contables.carga.dto.Sucursal;
import co.com.bbva.app.notas.contables.dto.Padrino;
import co.com.bbva.app.notas.contables.dto.UnidadAnalisis;
import co.com.bbva.app.notas.contables.session.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.model.SelectItem;
import javax.inject.Named;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * <p>
 * Pagina para manejar la administracin de parametros relacionados con la entidad UnidadAnalisis
 * </p>
 * 
 */
@SessionScoped
@Named
public class UnidadAnalisisPage extends GeneralParametrosPage<UnidadAnalisis, UnidadAnalisis> {

	String param = getParam();
	private static final long serialVersionUID = 1L;

	private static final Logger LOGGER = LoggerFactory.getLogger(UnidadAnalisisPage.class);
	private List<SelectItem> sucursales;
	private List<SelectItem> autorizaciones;

//	Session session = getContablesSessionBean().getSessionTrace();

	public UnidadAnalisisPage() {
		super(true);
	}

	/**
	 * retorna una instancia de UnidadAnalisis
	 * 
	 * @return
	 */
	@Override
	protected UnidadAnalisis _getInstance() {
		return new UnidadAnalisis();
	}

	@Override
	protected void _init() {
		super._init();
		consultarListasAuxiliares();
	}

	/**
	 * Se realiza el proceso de busqueda completo de entidades de tipo UnidadAnalisis
	 * 
	 * @return
	 */
	@Override
	public Collection<UnidadAnalisis> _buscarTodos() throws Exception {
		return notasContablesManager.getUnidadesAnalisis();
	}

	/**
	 * Realiza la busqueda de entidades de tipo UnidadAnalisis filtrando segn lo indicado por el usuario
	 * 
	 * @return
	 */
	@Override
	public Collection<UnidadAnalisis> _buscarPorFiltro() throws Exception {
		if(!param.isEmpty()){
			LOGGER.info("{} Buscar unidad analisis: {}" );
		}
		return notasContablesManager.searchUnidadAnalisis(param);
	}

	/**
	 * Funcion llamada cuando se desea inciar la edicion o creacion de registros de tipo UnidadAnalisis
	 * 
	 * @return
	 */
	@Override
	protected void _editar() throws Exception {
		objActual = new UnidadAnalisis();
	}

	@Override
	protected boolean _guardar() throws Exception {
		Number codInicial = objActual.getCodigo();
		try {
			if (objActual.getCodigo().intValue() <= 0) {
				LOGGER.info("{} Crea unidad analisis: {}",objActual.getCodigoSucursal() +" "+ objActual.getAutorizaReferenciaCruce() );
				LOGGER.info("REMOTE_IP={} HOST={} [{}] Crea unidad analisis {}  usernme: {}", "ip", "localHost.getHostAddress()", "uuid", objActual.getCodigo()+ " "+ objActual.getNombreSucursal()  , "username");
				notasContablesManager.addUnidadAnalisis(objActual, getCodUsuarioLogueado());
			} else {
				LOGGER.info("{} Actualiza unidad analisis: {}",objActual.getCodigo() +" "+objActual.getCodigoSucursal() +" "+ objActual.getAutorizaReferenciaCruce() );
				notasContablesManager.updateUnidadAnalisis(objActual, getCodUsuarioLogueado());
			}
		} catch (Exception e) {
			objActual.setCodigo(codInicial);
			LOGGER.error("{} Ya existe una Unidad de Anlisis con la misma sucursal: {}", codInicial ,e );
			nuevoMensaje(FacesMessage.SEVERITY_ERROR, "Ya existe una Unidad de Anlisis con la misma sucursal");
			return false;
		}
		return true;
	}

	@Override
	protected void _validar() throws Exception {
		validador.validarRequerido(objActual.getCodigoSucursal(), "Sucursal");
		validador.validarRequerido(objActual.getAutorizaReferenciaCruce(), "Autorizacin");
		cambioValido();
	}

	protected boolean cambioValido() throws Exception {
		Padrino p = new Padrino();
		p.setCodigoUnidadAnalisis(objActual.getCodigo());
		p.setEstado("A");
		if (!notasContablesManager.getPadrinosPorUnidadAnalisisYEstado(p).isEmpty()) {
			nuevoMensaje(FacesMessage.SEVERITY_WARN, "No se puede cambiar el estado o borrar la unidad de analisis dado que tiene padrinos asociados");
			return false;
		}
		return true;
	}

	/**
	 * Cambia el estado de la instancia seleccionada de tipo UnidadAnalisis
	 * 
	 * @return
	 */
	@Override
	public boolean _cambiarEstado() throws Exception {
		if (cambioValido()) {
			LOGGER.info("{} Cambio estado unidad analisis: {}",notasContablesManager.getUnidadAnalisis(objActual).getCodigo() +" "+objActual.getCodigoSucursal() +" "+ notasContablesManager.getUnidadAnalisis(objActual).getEstado() );
			notasContablesManager.changeEstadoUnidadAnalisis(notasContablesManager.getUnidadAnalisis(objActual), getCodUsuarioLogueado());
			return true;
		}
		return false;
	}

	/**
	 * Borra la informacion de la instancia seleccionada de tipo UnidadAnalisis
	 * 
	 * @return
	 */
	@Override
	public boolean _borrar() throws Exception {
		try {
			LOGGER.info("{} Borra unidad analisis: {}",objActual.getCodigo() +" "+objActual.getCodigoSucursal() +" "+ objActual.getAutorizaReferenciaCruce() );
			notasContablesManager.deleteUnidadAnalisis(objActual, getCodUsuarioLogueado());
		} catch (Exception e) {
			LOGGER.error("{} No puede eliminar la Unidad de Anlisis porque contiene registros asociados ", e);
			nuevoMensaje(FacesMessage.SEVERITY_WARN, "No puede eliminar la Unidad de Anlisis porque contiene registros asociados");
			return false;
		}
		return true;
	}

	private void consultarListasAuxiliares() {
		if (esUltimaFase()) {
			try {
				sucursales = getSelectItemList(new TreeMap<String, String>(notasContablesManager.getCV(Sucursal.class)), true);
				Map<String, String> autMap = new TreeMap<String, String>();
				autMap.put("N", "No");
				autMap.put("S", "Si");
				autorizaciones = getSelectItemList(autMap, false);
			} catch (Exception e) {

				LOGGER.error("{} Error consultar ListasAuxiliares ", e);
				nuevoMensaje(FacesMessage.SEVERITY_ERROR, "Error al inicializar el mdulo de administracin de montos autorizados");
			}
		}
	}

	@Override
	protected String _getPage() {
		return UNIDAD_ANALISIS;
	}

	public List<SelectItem> getSucursales() {
		return sucursales;
	}

	public void setSucursales(List<SelectItem> sucursales) {
		this.sucursales = sucursales;
	}

	public List<SelectItem> getAutorizaciones() {
		return autorizaciones;
	}

	public void setAutorizaciones(List<SelectItem> autorizaciones) {
		this.autorizaciones = autorizaciones;
	}
}
