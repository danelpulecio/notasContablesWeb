package co.com.bbva.app.notas.contables.schedule.historico;

import co.com.bbva.app.notas.contables.carga.manager.CargaAltamiraScheduled;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class CierreMensualAnularJob implements Job {
	private final CargaAltamiraScheduled cargaAltamiraScheduled = new CargaAltamiraScheduled();

	public CierreMensualAnularJob() {
	}

	public void execute(JobExecutionContext context) throws JobExecutionException {
		cargaAltamiraScheduled.procesarAnular();
	}
}
