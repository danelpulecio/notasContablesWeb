package co.com.bbva.app.notas.contables.jsf.carga;

import co.com.bbva.app.notas.contables.carga.dto.Departamento;
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
public class DepartamentoPage extends GeneralCargaPage<Departamento> {

	String param = getParam();
	/**
	 * 
	 */
	private static final long serialVersionUID = -8330009617976284212L;

	private static final Logger LOGGER = LoggerFactory.getLogger(DepartamentoPage.class);

	Session session = getContablesSessionBean().getSessionTrace();

	/**
	 * <p>
	 * Construct a new Page bean instance.
	 * </p>
	 */
	public DepartamentoPage() {
		super(true);
	}

	/**
	 * Se realiza el proceso de busqueda completo
	 * 
	 * @return
	 */
	@Override
	public Collection<Departamento> _buscarTodos() throws Exception {
		return cargaAltamiraManager.getDepartamentos();
	}

	/**
	 * Realiza la busqueda de actividades economicas por filtro
	 * 
	 * @return
	 */
	@Override
	public Collection<Departamento> _buscarPorFiltro() throws Exception {
		if(!param.isEmpty()){
			LOGGER.info("{} Buscar Departamento: {}", session.getTraceLog(),param );
		}
		return cargaAltamiraManager.searchDepartamento(param);
	}

	@Override
	protected String _getPage() {
		return DEPARTAMENTO;
	}
}
