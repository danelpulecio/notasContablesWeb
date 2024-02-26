package co.com.bbva.app.notas.contables.jsf.carga;

import co.com.bbva.app.notas.contables.carga.dto.UsuarioAltamira;
import co.com.bbva.app.notas.contables.session.Session;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
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
 */
@Named
@ViewScoped
public class UsuarioAltamiraPage extends GeneralCargaPage<UsuarioAltamira> {

    /**
     *
     */
    private static final long serialVersionUID = -8330009617976284212L;

    private static final Logger LOGGER = LoggerFactory.getLogger(UsuarioAltamiraPage.class);

    //	Session session = getContablesSessionBean().getSessionTrace();
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
    public UsuarioAltamiraPage() {
        super(true);
    }

    /**
     * Se realiza el proceso de busqueda completo
     *
     * @return
     */
    @Override
    public Collection<UsuarioAltamira> _buscarTodos() throws Exception {
        return cargaAltamiraManager.getUsuariosAltamira();
    }

    /**
     * Realiza la busqueda de actividades economicas por filtro
     *
     * @return
     */
    @Override
    public Collection<UsuarioAltamira> _buscarPorFiltro() throws Exception {
        if (!param.isEmpty()) {
//			LOGGER.info("{} Buscar usuario altamira: {}", session.getTraceLog(),param );
        }
        return cargaAltamiraManager.searchUsuarioAltamira(param);
    }

    @Override
    protected String _getPage() {
        return USUARIO_ALTAMIRA;
    }
}
