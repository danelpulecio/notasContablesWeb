package co.com.bbva.app.notas.contables.jsf.parametros;

import co.com.bbva.app.notas.contables.dto.Concepto;
import co.com.bbva.app.notas.contables.dto.TemaAutorizacion;
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

/**
 * <p>
 * Pagina para manejar la administracin de parametros relacionados con la entidad Concepto
 * </p>
 * 
 */
@SessionScoped
@Named
public class ConceptoPage extends GeneralParametrosPage<Concepto, Concepto> {


	private static final long serialVersionUID = 1L;
	private String estado;

	private static final Logger LOGGER = LoggerFactory.getLogger(ConceptoPage.class);

	private List<SelectItem> unidadesAnalisis;
	private List<SelectItem> temasAut;

//	Session session = getContablesSessionBean().getSessionTrace();
	@PostConstruct
	public void init() throws Exception {
		super._init();
		setDatos(new ArrayList<>(_buscarTodos()));
		consultarListasAuxiliares();
		LOGGER.info("postConstructo datos {}", getDatos().size());
	}
	public ConceptoPage() {
		super(true);
	}

	/**
	 * retorna una instancia de Concepto
	 * 
	 * @return
	 */
	@Override
	protected Concepto _getInstance() {
		return new Concepto();
	}


	/**
	 * Se realiza el proceso de busqueda completo de entidades de tipo Concepto
	 * 
	 * @return
	 */
	@Override
	public Collection<Concepto> _buscarTodos() throws Exception {
		return notasContablesManager.getConceptos();
	}

	/**
	 * Realiza la busqueda de entidades de tipo Concepto filtrando segn lo indicado por el usuario
	 * 
	 * @return
	 */
	@Override
	public Collection<Concepto> _buscarPorFiltro() throws Exception {
		if(!param.isEmpty()){
			LOGGER.info("{} Buscar concepto: {}", param );
		}
		return notasContablesManager.searchConcepto(param, estado);
	}

	/**
	 * Funcion llamada cuando se desea inciar la edicion o creacion de registros de tipo Concepto
	 * 
	 * @return
	 */
	@Override
	protected void _editar() throws Exception {
		objActual = new Concepto();
	}

	@Override
	protected boolean _guardar() throws Exception {
		Number codInicial = objActual.getCodigo();
		try {
			objActual.setAutorizacionTercero(getStringFromBool(objActual.getBoolAutorizacionTercero()));
			objActual.setCentrosAutAreasCentrales(getStringFromBool(objActual.getBoolCentrosAutAreasCentrales()));
			objActual.setCentrosAutCentroEspecial(getStringFromBool(objActual.getBoolCentrosAutCentroEspecial()));
			objActual.setCentrosAutSucursales(getStringFromBool(objActual.getBoolCentrosAutSucursales()));
			objActual.setOrigenDestino(getStringFromBool(objActual.getBoolOrigenDestino()));
			objActual.setSoportes(getStringFromBool(objActual.getBoolSoportes()));
			objActual.setVistoBuenoAnalisis(getStringFromBool(objActual.getBoolVistoBuenoAnalisis()));
			if (!objActual.getBoolVistoBuenoAnalisis()) {
				objActual.setCodigoUnidadAnalisis(0);
			}
			if (objActual.getCodigo().intValue() <= 0) {
				LOGGER.info("{} Crea concepto: {}", objActual.getNombre() );
				notasContablesManager.addConcepto(objActual, getCodUsuarioLogueado());
			} else {
				LOGGER.info("{} Actualiza concepto: {}", objActual.getCodigo() + " " +objActual.getNombre() );
				notasContablesManager.updateConcepto(objActual, getCodUsuarioLogueado());
			}
			return true;
		} catch (Exception e) {
			objActual.setCodigo(codInicial);
			LOGGER.error("{} Error al guardar el concepto: {}", codInicial , e );
			nuevoMensaje(FacesMessage.SEVERITY_ERROR, "Error al guardar el concepto");
			return false;
		}
	}

	private String getStringFromBool(boolean bool) {
		return bool ? "S" : "N";
	}

	@Override
	protected void _validar() throws Exception {
		validador.validarRequerido(objActual.getNombre(), "Nombre");
		if (objActual.getBoolVistoBuenoAnalisis()) {
			validador.validarRequerido(objActual.getCodigoUnidadAnalisis(), "Unidad de anlisis");
		}
		if (objActual.getBoolCentrosAutAreasCentrales() || objActual.getBoolCentrosAutCentroEspecial() || objActual.getBoolCentrosAutSucursales()) {
			return;
		}
		nuevoMensaje(FacesMessage.SEVERITY_WARN, "Debe seleccionar por lo menos un (1) Centro Autorizado");
	}

	/**
	 * Cambia el estado de la instancia seleccionada de tipo Concepto
	 * 
	 * @return
	 */
	@Override
	public boolean _cambiarEstado() throws Exception {
		LOGGER.info("{} Cambio estado concepto: {}", notasContablesManager.getConcepto(objActual).getCodigo() + " " +notasContablesManager.getConcepto(objActual).getEstado() );
		notasContablesManager.changeEstadoConcepto(notasContablesManager.getConcepto(objActual), getCodUsuarioLogueado());
		return true;
	}

	/**
	 * Borra la informacion de la instancia seleccionada de tipo Concepto
	 * 
	 * @return
	 */
	@Override
	public boolean _borrar() throws Exception {
		LOGGER.info("{} Elimina concepto: {}", notasContablesManager.getConcepto(objActual).getCodigo() + " " +notasContablesManager.getConcepto(objActual).getNombre() );
		notasContablesManager.deleteConcepto(objActual, getCodUsuarioLogueado());
		return true;
	}

	@Override
	protected String _getPage() {
		return CONCEPTO;
	}

	private void consultarListasAuxiliares() {
//		if (esUltimaFase()) {
			try {
				unidadesAnalisis = getSelectItemList(cargaAltamiraManager.getCVSucursal(), false);
				temasAut = getSelectItemList(notasContablesManager.getCV(TemaAutorizacion.class), false);
			} catch (Exception e) {
				LOGGER.error("{} Error al inicializar el mdulo de administracin de conceptos",  e );
				nuevoMensaje(FacesMessage.SEVERITY_ERROR, "Error al inicializar el mdulo de administracin de conceptos");

			}
//		}
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public List<SelectItem> getUnidadesAnalisis() {
		return unidadesAnalisis;
	}

	public void setUnidadesAnalisis(List<SelectItem> unidadesAnalisis) {
		this.unidadesAnalisis = unidadesAnalisis;
	}

	public List<SelectItem> getTemasAut() {
		return temasAut;
	}

	public void setTemasAut(List<SelectItem> temasAut) {
		this.temasAut = temasAut;
	}

}
