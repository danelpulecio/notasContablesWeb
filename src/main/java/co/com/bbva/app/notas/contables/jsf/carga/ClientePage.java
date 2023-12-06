package co.com.bbva.app.notas.contables.jsf.carga;

import co.com.bbva.app.notas.contables.carga.dto.Cliente;
import co.com.bbva.app.notas.contables.session.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.SessionScoped;
import javax.faces.view.ViewScoped;
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
@Named
@SessionScoped
public class ClientePage extends GeneralCargaPage<Cliente> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8330009617976284212L;

	private static final Logger LOGGER = LoggerFactory.getLogger(ClientePage.class);

//	Session session = getContablesSessionBean().getSessionTrace();

	/**
	 * <p>
	 * Construct a new Page bean instance.
	 * </p>
	 */
	public ClientePage() {
		super(true);
	}

	/**
	 * Se realiza el proceso de busqueda completo
	 * 
	 * @return
	 */
	@Override
	public Collection<Cliente> _buscarTodos() throws Exception {
//		LOGGER.info("Buscar todos los clientes");
//		Collection<Cliente> responseDbCustomer =cargaAltamiraManager.getClientes();
//		LOGGER.info("Respuesta buscoar todos los clientes {}", responseDbCustomer.size());
//		return responseDbCustomer;
		return new ArrayList<Cliente>();
	}

	/**
	 * Realiza la busqueda de actividades economicas por filtro
	 * 
	 * @return
	 */
	@Override
	public Collection<Cliente> _buscarPorFiltro() throws Exception {
		if(!param.isEmpty()){
//			LOGGER.info("{} Buscar cliente: {}", session.getTraceLog(),param );
		}
		return cargaAltamiraManager.searchCliente(param);
	}

	@Override
	protected String _getPage() {
		return CLIENTE;
	}
}
