package co.com.bbva.app.notas.contables.jsf.consultas;

import co.com.bbva.app.notas.contables.dto.*;
import co.com.bbva.app.notas.contables.session.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.inject.Named;
import java.util.ArrayList;

@SessionScoped
@Named
public class ConsultaMovimientosContablesPage extends ReporteGeneralPage {

	private static final long serialVersionUID = 5090751661160357021L;

	private static final Logger LOGGER = LoggerFactory.getLogger(ConsultaMovimientosContablesPage.class);

	Session session = getContablesSessionBean().getSessionTrace();

	public ConsultaMovimientosContablesPage() {
		super();
	}

	@Override
	protected String _getPage() {
		return CONSULTA_MOVIMIENTOS_CONTABLES;
	}

	@Override
	public String buscar() {
		try {
			tipoNota = "";
			estado = "6";
			descripcion = "";
			super.buscar();
			for (Instancia ins : getDatos()) {
				NotaContable nc = ins.getNC();
				if (nc.getTipoNota().equals(NotaContable.NORMAL)) {
					nc.setTemas(new ArrayList<NotaContableTema>(notasContablesManager.getTemasPorNotaContable(nc.getCodigo().intValue())));
				} else if (nc.getTipoNota().equals(NotaContable.LIBRE)) {
					nc.setTemasLibre(new ArrayList<NotaContableRegistroLibre>(notasContablesManager.getRegistrosNotaContableLibre(nc.getCodigo().intValue())));
				} else {
					nc.setTemasCruce(new ArrayList<NotaContableCrucePartidaPendiente>(notasContablesManager.getCrucesPartidasPendientesPorNotaContable(nc.getCodigo().intValue())));
				}
				ins.setNC(nc);
			}
		} catch (Exception e) {
			LOGGER.error("{} Error realizando la busqueda", session.getTraceLog() , e);
			nuevoMensaje(FacesMessage.SEVERITY_ERROR, "Error realizando la busqueda");

		}
		return null;
	}

}
