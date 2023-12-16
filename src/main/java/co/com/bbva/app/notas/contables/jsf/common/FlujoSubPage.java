package co.com.bbva.app.notas.contables.jsf.common;

import co.com.bbva.app.notas.contables.carga.dto.Festivo;
import co.com.bbva.app.notas.contables.dto.ActividadRealizada;
import co.com.bbva.app.notas.contables.jsf.BasePage;
import co.com.bbva.app.notas.contables.session.Session;
import co.com.bbva.app.notas.contables.util.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.inject.Named;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;

@Named(value = "flujoSubPage")
@SessionScoped
public class FlujoSubPage extends BasePage {

	private static final long serialVersionUID = 8619143247087744064L;

	private ActividadRealizada actividadRealizada;

	private ArrayList<ActividadRealizada> actividadesRealizadas;

	private static final Logger LOGGER = LoggerFactory.getLogger(FlujoSubPage.class);

//	Session session = getContablesSessionBean().getSessionTrace();

	public FlujoSubPage() {
		super();
		actividadRealizada = new ActividadRealizada();
		actividadesRealizadas = new ArrayList<ActividadRealizada>();
	}

	public String consultarFlujo() {
		try {
			actividadesRealizadas = new ArrayList<ActividadRealizada>(notasContablesManager.getActividadesRealizadasPorInstancia(actividadRealizada));
			for (int i = 0; i < actividadesRealizadas.size(); i++) {
				ActividadRealizada ac = actividadesRealizadas.get(i);
				long dif = 0L;
				// se obtienen los festivos
				Collection<Festivo> festivos = cargaAltamiraManager.getFestivos();
				int cantFest = 0;
				if (ac.getFechaHoraCierreTs() != null) {
					// se hace el calculo de duracion
					cantFest = DateUtils.getFestivosEntre(new Date(ac.getFechaHoraTs().getTime()), new Date(ac.getFechaHoraCierreTs().getTime()), new ArrayList<Festivo>(festivos));

					dif = ac.getFechaHoraCierreTs().getTime() - ac.getFechaHoraTs().getTime();
				} else {
					// se hace el calculo de duracion
					cantFest = DateUtils.getFestivosEntre(new Date(ac.getFechaHoraTs().getTime()), new Date(Calendar.getInstance().getTimeInMillis()), new ArrayList<Festivo>(festivos));

					dif = Calendar.getInstance().getTimeInMillis() - ac.getFechaHoraTs().getTime();
				}

				ac.setHorasEtapa(Math.max(1D, dif / 86400000 - cantFest));
			}
		} catch (Exception le_e) {
			actividadesRealizadas = new ArrayList<ActividadRealizada>();
//			LOGGER.error("{} Error en ejecucin consulta flujo ", session.getTraceLog(), le_e);
			nuevoMensaje(FacesMessage.SEVERITY_WARN, "Error en ejecucin " + le_e.getMessage());
		}
		return null;
	}

	public ActividadRealizada getActividadRealizada() {
		return actividadRealizada;
	}

	public void setActividadRealizada(ActividadRealizada actividadRealizada) {
		this.actividadRealizada = actividadRealizada;
	}

	public ArrayList<ActividadRealizada> getActividadesRealizadas() {
		return actividadesRealizadas;
	}

	public void setActividadesRealizadas(ArrayList<ActividadRealizada> actividadesRealizadas) {
		this.actividadesRealizadas = actividadesRealizadas;
	}
}
