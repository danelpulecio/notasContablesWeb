package co.com.bbva.app.notas.contables.schedule.cierre;

import co.com.bbva.app.notas.contables.carga.manager.CargaAltamiraCierreScheduled;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;

public class LoadCierreFileJob implements Job {

	private final CargaAltamiraCierreScheduled cargaAltamiraCierreScheduled = new CargaAltamiraCierreScheduled();

	private static final Logger LOGGER = LoggerFactory.getLogger(LoadCierreFileJob.class);

	/**
	 * <p>
	 * Empty constructor for job initilization
	 * </p>
	 * <p>
	 * Quartz requires a public empty constructor so that the scheduler can instantiate the class whenever it needs.
	 * </p>
	 */
	public LoadCierreFileJob() {
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

		JobDataMap dataMap = context.getJobDetail().getJobDataMap();
		String pathName = dataMap.getString("pathName");

		File file = new File(pathName);
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if (file.exists() && file.isDirectory()) {
			try {
				cargaAltamiraCierreScheduled.loadDatosAltamiraScheduled(file);
			} catch (Exception e) {
			}
		} else {
			LOGGER.error("No se ha encontrado el directorio indicado para procesar los archivos de precierre/cierre: " + pathName);

		}

		// Say Hello to the World and display the date/time
	}

}