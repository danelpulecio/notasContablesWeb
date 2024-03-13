package co.com.bbva.app.notas.contables.jsf.consultas;

import co.com.bbva.app.notas.contables.dto.*;
import co.com.bbva.app.notas.contables.session.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import java.util.ArrayList;

@Named
@ViewScoped
public class ConsultaMovimientosContablesPage extends ReporteGeneralPage {

	private static final long serialVersionUID = 5090751661160357021L;

	private static final Logger LOGGER = LoggerFactory.getLogger(ConsultaMovimientosContablesPage.class);


	//Session session = getContablesSessionBean().getSessionTrace();

	public ConsultaMovimientosContablesPage() {
		super();
	}

	@Override
	protected String _getPage() {
		return CONSULTA_MOVIMIENTOS_CONTABLES;
	}

	@Override
	public String buscar() {
		LOGGER.info("::::: buscar ContulstaMovimientosContablesPage ::::: ");
		try {
			
			if(this.desde != null && this.hasta != null) {
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
			}else {
				nuevoMensaje(FacesMessage.SEVERITY_WARN, "Debe seleccionar un rango de fechas para realizár la consulta.");
			}
		} catch (Exception e) {
			LOGGER.error("{} Error realizando la busqueda", e);
			nuevoMensaje(FacesMessage.SEVERITY_ERROR, "Error realizando la busqueda");

		}
		return null;
	}

}
