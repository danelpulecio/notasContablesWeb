package co.com.bbva.app.notas.contables.jsf.adminnota;

import co.com.bbva.app.notas.contables.dto.Instancia;
import co.com.bbva.app.notas.contables.jsf.consultas.GeneralConsultaPage;
import co.com.bbva.app.notas.contables.session.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.inject.Named;
import java.util.ArrayList;
import java.util.Collection;

@SessionScoped
@Named
public class PendientePage extends GeneralConsultaPage<Instancia> {

	private static final long serialVersionUID = -6709113217662690209L;
	private static final Logger LOGGER = LoggerFactory.getLogger(PendientePage.class);

	public PendientePage() {
		super();
	}

	@Override
	protected void _init() {
		super._init();
		cargarPendientes();
	}

	public void cargarPendientes() {
		Session session = getContablesSessionBean().getSessionTrace();
		try {
			Instancia instancia = new Instancia();
			instancia.setCodigoUsuarioActual(getUsuarioLogueado().getUsuario().getCodigo());
			LOGGER.info("{} Consultando de pendientes", session.getTraceLog());
			ArrayList<Instancia> instancias = new ArrayList<>(notasContablesManager.getInstanciasPorUsuario(instancia));
			setDatos(instancias);
			if (esUltimaFase() && getDatos().isEmpty() && !hayMensajes()) {
				nuevoMensaje(FacesMessage.SEVERITY_INFO, "Usted no tiene pendientes");
				LOGGER.info("{} El usuario no tiene pendientes ", session.getTraceLog());
			}
		} catch (final Exception e) {
			LOGGER.error("{} Error al consultar los pendientes ", session.getTraceLog() , e);
			nuevoMensaje(FacesMessage.SEVERITY_ERROR, "Error: Hubo un problema al realizar la consulta de pendientes" );
		}
	}

	public String mostrar() {
		return ADMIN_PENDIENTES;
	}

	@Override
	protected Collection<Instancia> _buscar() throws Exception {
		return new ArrayList<>();
	}

	@Override
	protected void _validar() throws Exception {
	}

	@Override
	protected String _getPage() {
		return ADMIN_PENDIENTES;
	}

}
