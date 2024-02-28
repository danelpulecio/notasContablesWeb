package co.com.bbva.app.notas.contables.jsf.carga;

import co.com.bbva.app.notas.contables.carga.dto.TipoIndicativo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
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
@ViewScoped
@Named
public class TipoIndicativoPage extends GeneralCargaPage<TipoIndicativo> {

    /**
     *
     */
    private static final long serialVersionUID = -8330009617976284212L;

    private static final Logger LOGGER = LoggerFactory.getLogger(TipoIndicativoPage.class);

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
    public TipoIndicativoPage() {
        super(true);
    }

    /**
     * Se realiza el proceso de busqueda completo
     *
     * @return
     */
    @Override
    public Collection<TipoIndicativo> _buscarTodos() throws Exception {
        return cargaAltamiraManager.getTiposIndicativos();
    }

    /**
     * Realiza la busqueda de actividades economicas por filtro
     *
     * @return
     */
    @Override
    public Collection<TipoIndicativo> _buscarPorFiltro() throws Exception {
        if (!param.isEmpty()) {
//			LOGGER.info("{} Buscar tipo Indicativo: {}", session.getTraceLog(),param );
        }
        return cargaAltamiraManager.searchTipoIndicativo(param);
    }

    @Override
    protected String _getPage() {
        return TIPO_INDICATIVO;
    }
}
