package co.com.bbva.app.notas.contables.schedule.actpendiente;

import co.com.bbva.app.notas.contables.carga.manager.CargaAltamiraScheduled;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ActPendienteJob implements Job {

	private final CargaAltamiraScheduled cargaAltamiraCierreScheduled = new CargaAltamiraScheduled();

	private static final Logger LOGGER = LoggerFactory.getLogger(ActPendienteJob.class);

	/**
	 * <p>
	 * Empty constructor for job initilization
	 * </p>
	 * <p>
	 * Quartz requires a public empty constructor so that the scheduler can instantiate the class whenever it needs.
	 * </p>
	 */
	public ActPendienteJob() {
	}

	/**
	 * <p>
	 * Called by the <code>{@link org.quartz.Scheduler}</code> when a <code>{@link org.quartz.Trigger}</code> fires that is associated with the <code>Job</code>.
	 * </p>
	 * 
	 * @throws JobExecutionException
	 *             if there is an exception while executing the job.
	 */
	public void execute(JobExecutionContext context) throws JobExecutionException {

		try {
			JobDataMap dataMap = context.getJobDetail().getJobDataMap();
			String mail = dataMap.getString("mail");
			cargaAltamiraCierreScheduled.loadProcesarActividadesPendientes(mail);
		} catch (Exception e) {
			LOGGER.error("Se presento error en ActPendienteJob" , e);
		}
	}

}