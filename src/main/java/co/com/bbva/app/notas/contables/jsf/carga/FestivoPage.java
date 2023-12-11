package co.com.bbva.app.notas.contables.jsf.carga;

import co.com.bbva.app.notas.contables.carga.dto.Festivo;
import co.com.bbva.app.notas.contables.session.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
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
@SessionScoped
@Named
public class FestivoPage extends GeneralCargaPage<Festivo> {

	private static final long serialVersionUID = -8330009617976284212L;
	private static final Logger LOGGER = LoggerFactory.getLogger(FestivoPage.class);
	private Session session;

	@PostConstruct
	public void init() throws Exception {
		setDatos(new ArrayList<>(_buscarTodos()));
		LOGGER.info("postConstructo datos {}", getDatos().size());
	}

	/**
	 * <p>
	 * Construct a new Page bean instance.
	 * </p>
	 */
	public FestivoPage() {
		super(true);
	}

	/**
	 * Se realiza el proceso de busqueda completo
	 * 
	 * @return
	 */
	@Override
	public Collection<Festivo> _buscarTodos() throws Exception {
		session = getContablesSessionBean().getSessionTrace();
		LOGGER.info("{} Listando los Festivos Parametrizados", session.getTraceLog() );
		return cargaAltamiraManager.getFestivos();
	}

	/**
	 * Realiza la busqueda de actividades economicas por filtro
	 * 
	 * @return
	 */
	@Override
	public Collection<Festivo> _buscarPorFiltro() throws Exception {
		return new ArrayList<Festivo>();
	}

	@Override
	protected String _getPage() {
		return FESTIVO;
	}
}
