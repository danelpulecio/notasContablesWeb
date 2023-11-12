package co.com.bbva.app.notas.contables.schedule;

import co.com.bbva.app.notas.contables.carga.manager.CargaAltamiraScheduled;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

public class LoadDataJob implements Job {

	private final CargaAltamiraScheduled cargaAltamiraScheduled = new CargaAltamiraScheduled();
	private static final String FILE_NAME = "ESTRUCTURA.DAT";

	private static final Logger LOGGER = LoggerFactory.getLogger(LoadDataJob.class);

	/**
	 * <p>
	 * Empty constructor for job initilization
	 * </p>
	 * <p>
	 * Quartz requires a public empty constructor so that the scheduler can instantiate the class whenever it needs.
	 * </p>
	 */
	public LoadDataJob() {
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

		File file = new File(pathName + FILE_NAME);
		if (file.exists()) {
			try {
				cargaAltamiraScheduled.loadDatosAltamiraScheduled(file);
			} catch (Exception e) {
				LOGGER.error("Error al intentar cargar el archivo: {} recibido desde Host - Altamira", pathName + FILE_NAME, e);
			}
		} else {
			LOGGER.info("No se ha encontrado el archivo indicado para procesar los archivos de carga desde altamira: " + pathName + FILE_NAME);
		}

	}

}