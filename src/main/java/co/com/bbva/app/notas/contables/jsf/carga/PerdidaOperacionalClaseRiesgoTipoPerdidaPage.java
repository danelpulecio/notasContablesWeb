package co.com.bbva.app.notas.contables.jsf.carga;

import co.com.bbva.app.notas.contables.carga.dto.PerdidaOperacionalClaseRiesgo;
import co.com.bbva.app.notas.contables.session.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import java.util.Collection;

/**
 * <p>
 * Page bean that corresponds to a similarly named JSP page. This class contains component definitions (and initialization code) for all components that you have defined on this page, as well as lifecycle methods and event handlers where you may add
 * behavior to respond to incoming events.
 * </p>
 * 
 */
@SessionScoped
@Named
public class PerdidaOperacionalClaseRiesgoTipoPerdidaPage extends GeneralCargaPage<PerdidaOperacionalClaseRiesgo> {

	String param = getParam();
	private static final long serialVersionUID = -7767388810824414064L;

	private static final Logger LOGGER = LoggerFactory.getLogger(PerdidaOperacionalClaseRiesgoTipoPerdidaPage.class);

//	Session session = getContablesSessionBean().getSessionTrace();

	/**
	 * <p>
	 * Construct a new Page bean instance.
	 * </p>
	 */
	public PerdidaOperacionalClaseRiesgoTipoPerdidaPage() {
		super(true);
	}

	/**
	 * Se realiza el proceso de busqueda completo
	 * 
	 * @return
	 */
	@Override
	public Collection<PerdidaOperacionalClaseRiesgo> _buscarTodos() throws Exception {
		return cargaAltamiraManager.getPerdidaOperacionalClaseRiesgo();
	}

	/**
	 * Realiza la busqueda de actividades economicas por filtro
	 * 
	 * @return
	 */
	@Override
	public Collection<PerdidaOperacionalClaseRiesgo> _buscarPorFiltro() throws Exception {
		if(!param.isEmpty()){
//			LOGGER.info("{} Buscar perdida de operacion clase riesgo: {}", session.getTraceLog(),param );
		}
		return cargaAltamiraManager.searchPerdidaOperacionalClaseRiesgo(param);
	}

	@Override
	protected String _getPage() {
		return PERD_OPER_CLAS_RIES_TI_PER;
	}
}
