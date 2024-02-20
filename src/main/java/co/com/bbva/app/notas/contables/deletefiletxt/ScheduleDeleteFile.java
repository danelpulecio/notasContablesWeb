package co.com.bbva.app.notas.contables.deletefiletxt;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import javax.annotation.PreDestroy;

import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

/**
 * Clase utilizada para orquestar la tarea programada
 * @author Usuario
 *
 */
public class ScheduleDeleteFile {
	private final String pathName;
	private final String cronDeleteFile;
	private static final SchedulerFactory sf = new StdSchedulerFactory();
	private static Scheduler sched;
	
	public ScheduleDeleteFile(String pathName, String cron) {
		super();
		this.pathName = pathName;
		this.cronDeleteFile = cron;
	}
	
	public void run() throws SchedulerException {
		sched = sf.getScheduler();
		JobDetail job = newJob(DeleteFileJob.class).withIdentity("deleteFileJob", "group1").usingJobData("pathName", pathName).build();
		CronTrigger trigger = newTrigger().withIdentity("triggerDeleteFile", "group1").withSchedule(cronSchedule(cronDeleteFile)).build();
		sched.scheduleJob(job, trigger);

		sched.start();
	}
	@PreDestroy
	public void destroy() {
		try {
			if (sched != null && sched.isStarted()) {
				sched.shutdown(true);
			}
		} catch (Exception e) {
			System.err.println("Error deteniendo hilo ");
			e.printStackTrace();
		}
	}
}
