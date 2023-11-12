package co.com.bbva.app.notas.contables.jsf.parametros;

import co.com.bbva.app.notas.contables.dto.MontoAutorizado;
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
 * Pagina para manejar la administracin de parametros relacionados con la entidad MontoAutorizado
 * </p>
 * 
 */
@SessionScoped
@Named
public class MontoAutorizadoPage extends GeneralParametrosPage<MontoAutorizado, MontoAutorizado> {

	String param = getParam();
	private static final long serialVersionUID = 1L;

	private static final Logger LOGGER = LoggerFactory.getLogger(MontoAutorizadoPage.class);

	private List<SelectItem> tiposEvento;
	private List<SelectItem> entesAut;
	private List<SelectItem> temasAut;

	Session session = getContablesSessionBean().getSessionTrace();

	public MontoAutorizadoPage() {
		super(true);
	}

	/**
	 * retorna una instancia de MontoAutorizado
	 * 
	 * @return
	 */
	@Override
	protected MontoAutorizado _getInstance() {
		return new MontoAutorizado();
	}

	@Override
	protected void _init() {
		super._init();
		consultarListasAuxiliares();
	}

	/**
	 * Se realiza el proceso de busqueda completo de entidades de tipo MontoAutorizado
	 * 
	 * @return
	 */
	@Override
	public Collection<MontoAutorizado> _buscarTodos() throws Exception {
		return notasContablesManager.getMontosAutorizados();
	}

	/**
	 * Realiza la busqueda de entidades de tipo MontoAutorizado filtrando segn lo indicado por el usuario
	 * 
	 * @return
	 */
	@Override
	public Collection<MontoAutorizado> _buscarPorFiltro() throws Exception {
		if(!param.isEmpty()){
			LOGGER.info("{} Buscar monto autorizado: {}", session.getTraceLog(),param );
		}
		return notasContablesManager.searchMontoAutorizado(param);
	}

	/**
	 * Funcion llamada cuando se desea inciar la edicion o creacion de registros de tipo MontoAutorizado
	 * 
	 * @return
	 */
	@Override
	protected void _editar() throws Exception {
		objActual = new MontoAutorizado();
	}

	@Override
	protected boolean _guardar() throws Exception {
		Number codInicial = objActual.getCodigo();
		try {
			if (objActual.getCodigo().intValue() <= 0) {
				LOGGER.info("{} Crea monto autorizado: {}", session.getTraceLog(),objActual.getNombreTipoEvento()+" "+objActual.getCodigoEnteAutorizador()+" "+objActual.getCodigoTemaAutorizacion() );
				notasContablesManager.addMontoAutorizado(objActual, getCodUsuarioLogueado());
			} else {
				LOGGER.info("{} Actualiza monto autorizado: {}", session.getTraceLog(),objActual.getCodigo()+" "+objActual.getNombreTipoEvento()+" "+objActual.getCodigoEnteAutorizador()+" "+objActual.getCodigoTemaAutorizacion() );
				notasContablesManager.updateMontoAutorizado(objActual, getCodUsuarioLogueado());
			}
		} catch (Exception e) {
			objActual.setCodigo(codInicial);
			LOGGER.error("{} Ya existe un ente Monto Autorizado con el mismo tipo de evento, ente autorizador y tema de autorizacin: {}", session.getTraceLog(),codInicial , e );
			nuevoMensaje(FacesMessage.SEVERITY_ERROR, "Ya existe un ente Monto Autorizado con el mismo tipo de evento, ente autorizador y tema de autorizacin");

			return false;
		}
		return true;
	}

	@Override
	protected void _validar() throws Exception {
		if (objActual.getCodigoTipoAutorizacion().intValue() <= 0) {
			nuevoMensaje(FacesMessage.SEVERITY_WARN, "Debe seleccionar el tipo de enveto");
		}
		if (objActual.getCodigoEnteAutorizador().intValue() <= 0) {
			nuevoMensaje(FacesMessage.SEVERITY_WARN, "Debe seleccionar el ente Autorizador");
		}
		if (objActual.getCodigoTemaAutorizacion().intValue() <= 0) {
			nuevoMensaje(FacesMessage.SEVERITY_WARN, "Debe seleccionar un tema de autorizacin");
		}
		validador.validarPositivo(objActual.getMonto(), "Lmite");
	}

	/**
	 * Cambia el estado de la instancia seleccionada de tipo MontoAutorizado
	 * 
	 * @return
	 */
	@Override
	public boolean _cambiarEstado() throws Exception {
		LOGGER.info("{} Cambia estado monto autorizado: {}", session.getTraceLog(),notasContablesManager.getMontoAutorizado(objActual).getCodigo()+" "+notasContablesManager.getMontoAutorizado(objActual).getEstado() );
		notasContablesManager.changeEstadoMontoAutorizado(notasContablesManager.getMontoAutorizado(objActual), getCodUsuarioLogueado());
		return true;
	}

	/**
	 * Borra la informacion de la instancia seleccionada de tipo MontoAutorizado
	 * 
	 * @return
	 */
	@Override
	public boolean _borrar() throws Exception {
		LOGGER.info("{} Borra monto autorizado: {}", session.getTraceLog(),objActual.getCodigo() );
		notasContablesManager.deleteMontoAutorizado(objActual, getCodUsuarioLogueado());
		return true;
	}

	private void consultarListasAuxiliares() {
		if (esUltimaFase()) {
			try {
				tiposEvento = getSelectItemList(notasContablesManager.getCV(TipoEvento.class), false);
				temasAut = getSelectItemList(notasContablesManager.getCV(TemaAutorizacion.class), false);
				entesAut = getSelectItemList(notasContablesManager.getCVEntesAut(), false);
			} catch (Exception e) {
				LOGGER.error("{} Error al inicializar el mdulo de administracin de montos autorizados ", session.getTraceLog(), e);
				nuevoMensaje(FacesMessage.SEVERITY_ERROR, "Error al inicializar el mdulo de administracin de montos autorizados");

			}
		}
	}

	@Override
	protected String _getPage() {
		return MONTOS_AUTORIZADOS;
	}

	public List<SelectItem> getTiposEvento() {
		return tiposEvento;
	}

	public void setTiposEvento(List<SelectItem> tiposEvento) {
		this.tiposEvento = tiposEvento;
	}

	public List<SelectItem> getEntesAut() {
		return entesAut;
	}

	public void setEntesAut(List<SelectItem> entesAut) {
		this.entesAut = entesAut;
	}

	public List<SelectItem> getTemasAut() {
		return temasAut;
	}

	public void setTemasAut(List<SelectItem> temasAut) {
		this.temasAut = temasAut;
	}

}
