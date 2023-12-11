package co.com.bbva.app.notas.contables.jsf.carga;

import co.com.bbva.app.notas.contables.carga.dto.RiesgoOperacionalLineaOperativa;
import co.com.bbva.app.notas.contables.session.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import java.util.ArrayList;
import java.util.Collection;

/**
 * <p>
 * Page bean that corresponds to a similarly named JSP page. This class contains component definitions (and initialization code) for all components that you have defined on this page, as well as lifecycle methods and event handlers where you may add
 * behavior to respond to incoming events.
 * </p>
 * 
 */
//@SessionScoped

@Named
@SessionScoped
public class RiesgoOperacionalLineaOperativaPage extends GeneralCargaPage<RiesgoOperacionalLineaOperativa> {



	/**
	 * 
	 */
	private static final long serialVersionUID = -8330009617976284212L;

	private static final Logger LOGGER = LoggerFactory.getLogger(RiesgoOperacionalLineaOperativaPage.class);

	@PostConstruct
	public void init() throws Exception {
		setDatos(new ArrayList<>(_buscarTodos()));
		LOGGER.info("postConstructo datos {}", getDatos().size());
	}

//	Session session = getContablesSessionBean().getSessionTrace();

	/**
	 * <p>
	 * Construct a new Page bean instance.
	 * </p>
	 */
	public RiesgoOperacionalLineaOperativaPage() {
		super(true);
	}

	/**
	 * Se realiza el proceso de busqueda completo
	 * 
	 * @return
	 */
	@Override
	public Collection<RiesgoOperacionalLineaOperativa> _buscarTodos() throws Exception {
		return cargaAltamiraManager.getRiesgosOperacionalesLineasOperativas();
	}

	/**
	 * Realiza la busqueda de actividades economicas por filtro
	 * 
	 * @return
	 */
	@Override
	public Collection<RiesgoOperacionalLineaOperativa> _buscarPorFiltro() throws Exception {
		if(!param.isEmpty()){
//			LOGGER.info("{} Buscar riesgo operacional linea operativa: {}", session.getTraceLog(),param );
		}
		return cargaAltamiraManager.searchRiesgoOperacionalLineaOperativa(param);
	}

	@Override
	protected String _getPage() {
		return RIES_OPER_LIN_OPER;
	}
}
