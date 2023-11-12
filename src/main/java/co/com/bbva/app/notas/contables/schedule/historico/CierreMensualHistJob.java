package co.com.bbva.app.notas.contables.schedule.historico;

import co.com.bbva.app.notas.contables.carga.manager.CargaAltamiraScheduled;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class CierreMensualHistJob implements Job {
	private final CargaAltamiraScheduled cargaAltamiraScheduled = new CargaAltamiraScheduled();

	public CierreMensualHistJob() {
	}

	public void execute(JobExecutionContext context) throws JobExecutionException {
		cargaAltamiraScheduled.procesarHistorico();
	}
}
