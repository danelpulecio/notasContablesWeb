package co.com.bbva.app.notas.contables.jsf.parametros;

import co.com.bbva.app.notas.contables.dto.Concepto;
import co.com.bbva.app.notas.contables.dto.TemaAutorizacion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.model.SelectItem;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * <p>
 * Pagina para manejar la administración de parametros relacionados con la entidad Concepto
 * </p>
 * 
 */
@ViewScoped
@Named
public class ConceptoPage extends GeneralParametrosPage<Concepto, Concepto> {


	private static final long serialVersionUID = 1L;
	private String estado;

	private static final Logger LOGGER = LoggerFactory.getLogger(ConceptoPage.class);

	private List<SelectItem> unidadesAnalisis = new ArrayList<>();
	private List<SelectItem> unidadesAnalisisTem = new ArrayList<>();
	private List<SelectItem> temasAut = new ArrayList<>();
	private List<SelectItem> temasAutTemp = new ArrayList<>();

	private String defaultValue;

//	Session session = getContablesSessionBean().getSessionTrace();
	@PostConstruct
	public void init() throws Exception {
		super._init();
		setDatos(new ArrayList<>(_buscarTodos()));
		consultarListasAuxiliares();
		LOGGER.info("postConstructo datos conceptos codigo unidad analisis {}", getDatos().get(8).getCodigoUnidadAnalisis());
		LOGGER.info("postConstructo datos conceptos vistBuenoAnalisis {}", getDatos().get(8).getVistoBuenoAnalisis());
		LOGGER.info("postConstructo datos conceptos estado {}", getDatos().get(8).getEstado());
		LOGGER.info("postConstructo datos conceptos nombre {}", getDatos().get(8).getNombre());
		LOGGER.info("postConstructo datos conceptos codigo tema autorizado {}", getDatos().get(8).getCodigoTemaAutorizacion());
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
			LOGGER.info("Obj actual codTemaAut {} ", objActual.getCodigoTemaAutorizacion());
			LOGGER.info("Obj actual codTemaAut {} ", objActual.getCodigoTemaAutorizacionTemp());
			objActual.setCodigoTemaAutorizacion(objActual.getCodigoTemaAutorizacionTemp());
			LOGGER.info("Obj actual codTemaAut after {} ", objActual.getCodigoTemaAutorizacion());
			objActual.setCodigoUnidadAnalisis(objActual.getCodigoUnidadAnalisisTemp());
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
			validador.validarRequerido(objActual.getCodigoUnidadAnalisisTemp(), "Unidad de anlisis");
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
		setDatos(new ArrayList<>(_buscarTodos()));
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
				unidadesAnalisisTem = getSelectItemList(cargaAltamiraManager.getCVSucursal(), false);
				for (SelectItem unidadAnalisis : unidadesAnalisisTem){
					unidadesAnalisis.add(new SelectItem(Integer.parseInt(String.valueOf(unidadAnalisis.getValue())),unidadAnalisis.getLabel()));
				}
				temasAutTemp = getSelectItemList(notasContablesManager.getCV(TemaAutorizacion.class), false);
				for (SelectItem temaAut : temasAutTemp){
					temasAut.add(new SelectItem(Integer.parseInt(String.valueOf(temaAut.getValue())), temaAut.getLabel()));
				}
			} catch (Exception e) {
				LOGGER.error("{} Error al inicializar el mdulo de administración de conceptos",  e );
				nuevoMensaje(FacesMessage.SEVERITY_ERROR, "Error al inicializar el mdulo de administración de conceptos");

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
		LOGGER.info("unidad de analisis label{}", unidadesAnalisis.get(8).getLabel());
		LOGGER.info("unidad de analisis value{}", unidadesAnalisis.get(8).getValue());
		LOGGER.info("unidad de analisis class value{}", unidadesAnalisis.get(8).getValue().getClass());
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

	public String getDefaultValue() {
		for (SelectItem codeUd : unidadesAnalisis){
			if(codeUd.getValue().equals(objActual.getCodigoUnidadAnalisis()))
				defaultValue = codeUd.getLabel();
		}
		return defaultValue;
	}
}
