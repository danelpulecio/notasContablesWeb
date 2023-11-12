package co.com.bbva.app.notas.contables.jsf.parametros;

import co.com.bbva.app.notas.contables.dto.MontoAutorizadoGeneral;
import co.com.bbva.app.notas.contables.dto.Rol;
import co.com.bbva.app.notas.contables.dto.TemaAutorizacion;
import co.com.bbva.app.notas.contables.dto.TipoEvento;
import co.com.bbva.app.notas.contables.session.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.model.SelectItem;
import javax.inject.Named;
import java.util.Collection;
import java.util.List;

/**
 * <p>
 * Pagina para manejar la administracin de parametros relacionados con la entidad MontoAutorizadoGeneral
 * </p>
 * 
 */
@SessionScoped
@Named
public class MontoAutorizadoGeneralPage extends GeneralParametrosPage<MontoAutorizadoGeneral, MontoAutorizadoGeneral> {

	String param = getParam();
	private static final long serialVersionUID = 1L;

	private static final Logger LOGGER = LoggerFactory.getLogger(MontoAutorizadoGeneralPage.class);

	private List<SelectItem> tiposEvento;
	private List<SelectItem> roles;
	private List<SelectItem> temasAut;

	Session session = getContablesSessionBean().getSessionTrace();

	public MontoAutorizadoGeneralPage() {
		super(true);
	}

	/**
	 * retorna una instancia de MontoAutorizadoGeneral
	 * 
	 * @return
	 */
	@Override
	protected MontoAutorizadoGeneral _getInstance() {
		return new MontoAutorizadoGeneral();
	}

	@Override
	protected void _init() {
		super._init();
		consultarListasAuxiliares();
	}

	/**
	 * Se realiza el proceso de busqueda completo de entidades de tipo MontoAutorizadoGeneral
	 * 
	 * @return
	 */
	@Override
	public Collection<MontoAutorizadoGeneral> _buscarTodos() throws Exception {
		return notasContablesManager.getMontosAutorizadosGenerales();
	}

	/**
	 * Realiza la busqueda de entidades de tipo MontoAutorizadoGeneral filtrando segn lo indicado por el usuario
	 * 
	 * @return
	 */
	@Override
	public Collection<MontoAutorizadoGeneral> _buscarPorFiltro() throws Exception {
		if(!param.isEmpty()){
			LOGGER.info("{} Buscar monto autorizado general: {}", session.getTraceLog(),param );
		}
		return notasContablesManager.searchMontoAutorizadoGeneral(param);
	}

	/**
	 * Funcion llamada cuando se desea inciar la edicion o creacion de registros de tipo MontoAutorizadoGeneral
	 * 
	 * @return
	 */
	@Override
	protected void _editar() throws Exception {
		objActual = new MontoAutorizadoGeneral();
	}

	@Override
	protected boolean _guardar() throws Exception {
		Number codInicial = objActual.getCodigo();
		try {
			if (objActual.getCodigo().intValue() <= 0) {
				LOGGER.info("{} Crea monto autorizado general: {}", session.getTraceLog(),objActual.getCodigoTipoAutorizacion() + " "+objActual.getCodigoRol() + " "+ objActual.getCodigoTemaAutorizacion() );
				notasContablesManager.addMontoAutorizadoGeneral(objActual, getCodUsuarioLogueado());
			} else {
				LOGGER.info("{} Actualiza monto autorizado general: {}", session.getTraceLog(),objActual.getCodigoTipoAutorizacion() + " "+objActual.getCodigoRol() + " "+ objActual.getCodigoTemaAutorizacion() );
				notasContablesManager.updateMontoAutorizadoGeneral(objActual, getCodUsuarioLogueado());
			}
			return true;
		} catch (Exception e) {
			objActual.setCodigo(codInicial);
			LOGGER.error("{} Ya existe un registro con los mismos datos: {}", session.getTraceLog(),codInicial ,e );
			nuevoMensaje(FacesMessage.SEVERITY_ERROR, "Ya existe un registro con los mismos datos.");

			return false;
		}
	}

	@Override
	protected void _validar() throws Exception {
		validador.validarPositivo(objActual.getMonto(), "Lmite");
		if (objActual.getCodigoTipoAutorizacion().intValue() <= 0) {
			nuevoMensaje(FacesMessage.SEVERITY_WARN, "Debe seleccionar un tipo de enveto");
		}
		if (objActual.getCodigoRol().intValue() <= 0) {
			nuevoMensaje(FacesMessage.SEVERITY_WARN, "Debe seleccionar un rol");
		}
		if (objActual.getCodigoTemaAutorizacion().intValue() <= 0) {
			nuevoMensaje(FacesMessage.SEVERITY_WARN, "Debe seleccionar un tema de autorizacin");
		}
	}

	/**
	 * Cambia el estado de la instancia seleccionada de tipo MontoAutorizadoGeneral
	 * 
	 * @return
	 */
	@Override
	public boolean _cambiarEstado() throws Exception {
		LOGGER.info("{} Cambio monto autorizado general: {}", session.getTraceLog(),notasContablesManager.getMontoAutorizadoGeneral(objActual).getCodigo() + " " + notasContablesManager.getMontoAutorizadoGeneral(objActual).getEstado() );
		notasContablesManager.changeEstadoMontoAutorizadoGeneral(notasContablesManager.getMontoAutorizadoGeneral(objActual), getCodUsuarioLogueado());
		return true;
	}

	/**
	 * Borra la informacion de la instancia seleccionada de tipo MontoAutorizadoGeneral
	 * 
	 * @return
	 */
	@Override
	public boolean _borrar() throws Exception {
		LOGGER.info("{} Borra monto autorizado general: {}", session.getTraceLog(),notasContablesManager.getMontoAutorizadoGeneral(objActual).getCodigo()  );
		notasContablesManager.deleteMontoAutorizadoGeneral(objActual, getCodUsuarioLogueado());
		return true;
	}

	private void consultarListasAuxiliares() {
		if (esUltimaFase()) {
			try {
				tiposEvento = getSelectItemList(notasContablesManager.getCV(TipoEvento.class), false);
				temasAut = getSelectItemList(notasContablesManager.getCV(TemaAutorizacion.class), false);
				roles = getSelectItemList(notasContablesManager.getCV(Rol.class), false);
			} catch (Exception e) {
				LOGGER.error("{} Error al inicializar el mdulo de administracin de montos autorizados generales ", session.getTraceLog(), e);
				nuevoMensaje(FacesMessage.SEVERITY_ERROR, "Error al inicializar el mdulo de administracin de montos autorizados generales");

			}
		}
	}

	@Override
	protected String _getPage() {
		return MONTOS_AUTORIZADOS_GENERALES;
	}

	public List<SelectItem> getTiposEvento() {
		return tiposEvento;
	}

	public void setTiposEvento(List<SelectItem> tiposEvento) {
		this.tiposEvento = tiposEvento;
	}

	public List<SelectItem> getRoles() {
		return roles;
	}

	public void setRoles(List<SelectItem> roles) {
		this.roles = roles;
	}

	public List<SelectItem> getTemasAut() {
		return temasAut;
	}

	public void setTemasAut(List<SelectItem> temasAut) {
		this.temasAut = temasAut;
	}

}
