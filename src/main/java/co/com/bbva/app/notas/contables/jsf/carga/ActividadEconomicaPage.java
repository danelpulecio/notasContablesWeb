package co.com.bbva.app.notas.contables.jsf.carga;

import co.com.bbva.app.notas.contables.carga.dto.ActividadEconomica;
import co.com.bbva.app.notas.contables.session.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import java.io.Serializable;
import java.util.Collection;

/**
 * <p>
 * Page bean that corresponds to a similarly named JSP page. This class contains component definitions (and initialization code) for all components that you have defined on this page, as well as lifecycle methods and event handlers where you may add
 * behavior to respond to incoming events.
 * </p>
 * 
 */
@RequestScoped
@Named
public class ActividadEconomicaPage extends GeneralCargaPage<ActividadEconomica> implements Serializable {



	private static final long serialVersionUID = -2215153957937462919L;

	private static final Logger LOGGER = LoggerFactory.getLogger(ActividadEconomicaPage.class);

//	Session session = getContablesSessionBean().getSessionTrace();

	/**
	 * <p>
	 * Construct a new Page bean instance.
	 * </p>
	 */
	public ActividadEconomicaPage() {
		super(true);
	}

	/**
	 * Se realiza el proceso de busqueda completo
	 * 
	 * @return
	 */
	@Override
	public Collection<ActividadEconomica> _buscarTodos() throws Exception {
		return cargaAltamiraManager.getActividadesEconomicas();
	}

	/**
	 * Realiza la busqueda de actividades economicas por filtro
	 * 
	 * @return
	 */
	@Override
	public Collection<ActividadEconomica> _buscarPorFiltro() throws Exception {
		if(!param.isEmpty()){
			LOGGER.info("<<<buscar por filtro en actividad Eco Page>>>");
//			LOGGER.info("{} Buscar Actividad Economica: {}", session.getTraceLog(),param );
		}LOGGER.info("<<<param {} >>>", param);
		Collection<ActividadEconomica> actividadEco = cargaAltamiraManager.searchActividadEconomica(param);
		LOGGER.info("<<<respuesta actividadEconomica {} >>>", actividadEco);
		return actividadEco;
	}

	@Override
	protected String _getPage() {
		return ACTIVIDAD_ECONOMICA;
	}
}
