package co.com.bbva.app.notas.contables.deletefilesiro;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

/**
 *
 * @author pjimenez
* @version 1.0, 21/10/2021
* @since JDK1.8
 *
 */
public class ScheduleDeleteFileSiro {
	
	private final String pathName;
	private final String cronDeleteFileSiro;
	private static final SchedulerFactory sf = new StdSchedulerFactory();
	private static Scheduler sched;
	
	public ScheduleDeleteFileSiro(String pathName, String cron) {
		super();
		this.pathName = pathName;
		this.cronDeleteFileSiro = cron;
	}
	
	public void run() throws SchedulerException {
		sched = sf.getScheduler();
		JobDetail job = newJob(DeleteFileSiroJob.class).withIdentity("deleteFileSiroJob", "groupSiro").usingJobData("pathName", pathName).build();
		CronTrigger trigger = newTrigger().withIdentity("triggerDeleteFileSiro", "groupSiro").withSchedule(cronSchedule(cronDeleteFileSiro)).build();
		sched.scheduleJob(job, trigger);

		sched.start();
	}
	
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
