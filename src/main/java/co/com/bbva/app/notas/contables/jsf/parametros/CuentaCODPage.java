package co.com.bbva.app.notas.contables.jsf.parametros;

import co.com.bbva.app.notas.contables.carga.dto.PUC;
import co.com.bbva.app.notas.contables.dto.CuentaCOD;
import co.com.bbva.app.notas.contables.dto.Tema;
import co.com.bbva.app.notas.contables.session.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.model.SelectItem;
import javax.inject.Named;
import java.util.*;

/**
 * <p>
 * Pagina para manejar la administración de parametros relacionados con la entidad CuentaCOD
 * </p>
 * 
 */
@SessionScoped
@Named
public class CuentaCODPage extends GeneralParametrosPage<CuentaCOD, CuentaCOD> {

	private static final long serialVersionUID = 1L;

	private List<SelectItem> cuentas = new ArrayList<SelectItem>();

	private static final Logger LOGGER = LoggerFactory.getLogger(CuentaCODPage.class);

//	Session session = getContablesSessionBean().getSessionTrace();
@PostConstruct
public void init() throws Exception {
	setDatos(new ArrayList<>(_buscarTodos()));
	LOGGER.info("postConstructo datos {}", getDatos().size());
}

	private String filtroCuentas;

	public CuentaCODPage() {
		super(true);
	}

	/**
	 * retorna una instancia de CuentaCOD
	 * 
	 * @return
	 */
	@Override
	protected CuentaCOD _getInstance() {
		return new CuentaCOD();
	}

	@Override
	protected void _init() {
		super._init();
	}

	/**
	 * Se realiza el proceso de busqueda completo de entidades de tipo CuentaCOD
	 * 
	 * @return
	 */
	@Override
	public Collection<CuentaCOD> _buscarTodos() throws Exception {
		return notasContablesManager.getCuentasCOD();
	}

	/**
	 * Realiza la busqueda de entidades de tipo CuentaCOD filtrando segn lo indicado por el usuario
	 * 
	 * @return
	 */
	@Override
	public Collection<CuentaCOD> _buscarPorFiltro() throws Exception {
		if(!param.isEmpty()){
//			LOGGER.info("{} Buscar cuenta COD: {}", session.getTraceLog(),param );
		}

		return notasContablesManager.searchCuentaCOD(param);
	}

	public String buscarCuentas() {
		cuentas = new ArrayList<SelectItem>();
		if (filtroCuentas.length() < 4) {
			nuevoMensaje(FacesMessage.SEVERITY_WARN, "El filtro debe ser de longitud 4 o superior");
		} else {
			try {
				Map<?, String> mapa = new TreeMap<Object, String>(notasContablesManager.getCVBy(co.com.bbva.app.notas.contables.carga.dto.PUC.class, filtroCuentas));
				if (mapa.isEmpty()) {
					nuevoMensaje(FacesMessage.SEVERITY_INFO, "No se han encontrado registros");
				}
				for (final Object key : mapa.keySet()) {
					cuentas.add(new SelectItem(key, key + " - " + mapa.get(key)));
				}
			} catch (Exception e) {
//				LOGGER.error("{} Error al buscar cuenta", session.getTraceLog() , e );
				nuevoMensaje(FacesMessage.SEVERITY_ERROR, "Error al buscar cuenta");

			}
		}
		return null;
	}

	/**
	 * Funcion llamada cuando se desea inciar la edicion o creacion de registros de tipo CuentaCOD
	 * 
	 * @return
	 */
	@Override
	protected void _editar() throws Exception {
		cuentas = new ArrayList<SelectItem>();
		objActual = new CuentaCOD();
		filtroCuentas = "";
	}

	@Override
	protected boolean _guardar() throws Exception {
		String cuenta = objActual.getCuentaContable();
		if (cuenta.indexOf('-') > 0) {
			cuenta = objActual.getCuentaContable().substring(0, objActual.getCuentaContable().indexOf('-') - 1);
		}
		objActual.setCuentaContable(cuenta);

		int codInicial = objActual.getCodigo();
		try {
			if (objActual.getCodigo() <= 0) {
//				LOGGER.info("{} Crea cuenta COD: {}", session.getTraceLog(), objActual.getCuentaContable()  );
				notasContablesManager.addCuentaCOD(objActual, getCodUsuarioLogueado());
			} else {
//				LOGGER.info("{} Actualiza cuenta COD: {}", session.getTraceLog(),objActual.getCodigo() +" "+ objActual.getCuentaContable() );
				notasContablesManager.updateCuentaCOD(objActual, getCodUsuarioLogueado());
			}
			return true;
		} catch (Exception e) {
			objActual.setCodigo(codInicial);
//			LOGGER.error("{} Ya existe esa Cuenta registrada: {}", session.getTraceLog(), codInicial , e );
			nuevoMensaje(FacesMessage.SEVERITY_ERROR, "Ya existe esa Cuenta registrada");

		}
		return false;
	}

	@Override
	protected void _validar() throws Exception {
		String cuenta = objActual.getCuentaContable();
		if (cuenta.indexOf('-') > 0) {
			cuenta = objActual.getCuentaContable().substring(0, objActual.getCuentaContable().indexOf('-') - 1);
		}

		PUC puc = new PUC();
		puc.setNumeroCuenta(cuenta);
		puc = cargaAltamiraManager.getPUC(puc);
		if (!puc.getDescripcion().equals("")) {
			if (puc.getTipoMoneda() != null && !puc.getTipoMoneda().equals("1") && !puc.getTipoMoneda().equals("2")) {
				nuevoMensaje(FacesMessage.SEVERITY_WARN, "La cuenta contable debe ser Divisa o Multidivisa");
			}
		} else {
			nuevoMensaje(FacesMessage.SEVERITY_WARN, "La cuenta contable no es una cuenta válida");
		}
	}

	/**
	 * Cambia el estado de la instancia seleccionada de tipo CuentaCOD
	 * 
	 * @return
	 */
	@Override
	public boolean _cambiarEstado() throws Exception {
		return false;
	}

	/**
	 * Borra la informacion de la instancia seleccionada de tipo CuentaCOD
	 * 
	 * @return
	 */
	@Override
	public boolean _borrar() throws Exception {
		Tema tema = new Tema();
		tema.setPartidaContable(objActual.getCuentaContable());
		tema.setContraPartidaContable(objActual.getCuentaContable());

		if (notasContablesManager.getTemasPorPartidaOContraPartida(tema).size() != 0) {
			nuevoMensaje(FacesMessage.SEVERITY_WARN, "No se puede eliminar esta Cuenta COD porque est relacionada en uno o varios temas");
		} else {
			LOGGER.info("{} Borra cuenta COD: {}", objActual.getCuentaContable() );
			notasContablesManager.deleteCuentaCOD(objActual, getCodUsuarioLogueado());
		}
		return true;
	}

	@Override
	protected String _getPage() {
		return CUENTA_COD;
	}

	public List<SelectItem> getCuentas() {
		return cuentas;
	}

	public void setCuentas(List<SelectItem> cuentas) {
		this.cuentas = cuentas;
	}

	public String getFiltroCuentas() {
		return filtroCuentas;
	}

	public void setFiltroCuentas(String filtroCuentas) {
		this.filtroCuentas = filtroCuentas;
	}

}
