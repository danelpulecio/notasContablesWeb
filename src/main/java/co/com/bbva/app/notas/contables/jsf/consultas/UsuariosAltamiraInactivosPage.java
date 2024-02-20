package co.com.bbva.app.notas.contables.jsf.consultas;

import co.com.bbva.app.notas.contables.dto.UsuarioModulo;
import co.com.bbva.app.notas.contables.session.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.inject.Named;
import java.util.ArrayList;
import java.util.Collection;


@SessionScoped
@Named
public class UsuariosAltamiraInactivosPage extends GeneralConsultaPage<UsuarioModulo> {

	private static final long serialVersionUID = -6709113217662690209L;

	private static final Logger LOGGER = LoggerFactory.getLogger(UsuariosAltamiraInactivosPage.class);
	protected boolean stopPoll = false;

	public UsuariosAltamiraInactivosPage() {
		super();
	}

	@PostConstruct
	public void init() {
		super._init();
		this.cargarUsuariosInactivosAltamira();
	}
	public void cargarUsuariosInactivosAltamira() {
		try {
			if (getDatos() == null || getDatos().isEmpty()) {
				LOGGER.info("CONSULTANDO LOS USUARIOS INACTIVOS ALTAMIRA-...");
				setDatos(notasContablesManager.getUsuariosAltamiraInactivos());
				if(getDatos().isEmpty()) {
					nuevoMensaje(FacesMessage.SEVERITY_WARN, "No se encontraron usuarios altamira inactivos");
					LOGGER.info("No se encuentrán usuarios altamira inactivos. ");
				}
			}
			this.stopPoll = true;
		} catch (Exception e) {
			this.stopPoll = true;
			LOGGER.error("{} Error ingresar a UsuariosAltamiraInactivosPage ", e);
			nuevoMensaje(FacesMessage.SEVERITY_ERROR, "Error ingresar a UsuariosAltamiraInactivosPage");
		}
	}

	@Override
	protected Collection<UsuarioModulo> _buscar() throws Exception {
		return new ArrayList<UsuarioModulo>();
	}

	@Override
	protected void _validar() throws Exception {
	}

	@Override
	protected String _getPage() {
		return USUARIOS_INACTIVOS;
	}

	public boolean isStopPoll() {
		return stopPoll;
	}

	public void setStopPoll(boolean stopPoll) {
		this.stopPoll = stopPoll;
	}
}