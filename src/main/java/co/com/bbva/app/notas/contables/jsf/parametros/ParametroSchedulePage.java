package co.com.bbva.app.notas.contables.jsf.parametros;

import co.com.bbva.app.notas.contables.dto.Parametro;
import co.com.bbva.app.notas.contables.jsf.GeneralPage;
import co.com.bbva.app.notas.contables.jsf.IPages;
import co.com.bbva.app.notas.contables.session.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.inject.Named;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * Page bean that corresponds to a similarly named JSP page. This class contains component definitions (and initialization code) for all components that you have defined on this page, as well as lifecycle methods and event handlers where you may add
 * behavior to respond to incoming events.
 * </p>
 */
@SessionScoped
@Named(value = "parametrosSchedulePage")
public class ParametroSchedulePage extends GeneralPage implements IPages {

    List<Parametro> parametros = null;

    private static final Logger LOGGER = LoggerFactory.getLogger(ParametroSchedulePage.class);

//	Session session = getContablesSessionBean().getSessionTrace();

    @PostConstruct
    public void init() throws Exception {
        if (parametros == null) {
            parametros = new ArrayList<Parametro>(notasContablesManager.getParametros());
        }
    }

    @Override
    protected void _init() throws Exception {
        // TODO Auto-generated method stub
//        if (parametros == null) {
//            parametros = new ArrayList<Parametro>(notasContablesManager.getParametros());
//        }
    }

    public String inicializar() {
        return PARAM_SCHEDULE;
    }

    public String guardar() {
        try {
            notasContablesManager.updateParametros(parametros, getUsuarioLogueado().getUsuario().getCodigo().intValue());
//			LOGGER.info("{} Actualizar parametros SchedulePage ", session.getTraceLog() );
            nuevoMensaje(FacesMessage.SEVERITY_INFO, "La información ha sido actualizada");
        } catch (Exception e) {
//			LOGGER.error("{} Error guardando los parámetros ", session.getTraceLog(), e);
            nuevoMensaje(FacesMessage.SEVERITY_ERROR, "Error guardando los parámetros");

        }
        return null;
    }

    public Parametro getCopiaSeguridadPar() {
        for (Parametro p : parametros) {
            if (p.getNombre().equalsIgnoreCase(Parametro.CIERRE_MENSUAL)) {
                return p;
            }
        }
        return new Parametro();
    }

    public Parametro getDeltaBorradoPar() {
        for (Parametro p : parametros) {
            if (p.getNombre().equalsIgnoreCase(Parametro.DELTA_BORRADO_ANULACION)) {
                return p;
            }
        }
        return new Parametro();
    }

}
