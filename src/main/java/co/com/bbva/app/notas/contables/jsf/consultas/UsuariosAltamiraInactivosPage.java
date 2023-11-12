package co.com.bbva.app.notas.contables.jsf.consultas;

import co.com.bbva.app.notas.contables.dto.UsuarioModulo;
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
public class UsuariosAltamiraInactivosPage extends GeneralConsultaPage<UsuarioModulo> {

	private static final long serialVersionUID = -6709113217662690209L;

	private static final Logger LOGGER = LoggerFactory.getLogger(UsuariosAltamiraInactivosPage.class);

	Session session = getContablesSessionBean().getSessionTrace();

	public UsuariosAltamiraInactivosPage() {
		super();
	}

	@Override
	protected void _init() {
		super._init();
		try {
			if (getDatos() == null || getDatos().isEmpty()) {
				setDatos(notasContablesManager.getUsuariosAltamiraInactivos());
			}
		} catch (Exception e) {

			LOGGER.error("{} Error ingresar a UsuariosAltamiraInactivosPage ", session.getTraceLog(), e);
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

}
