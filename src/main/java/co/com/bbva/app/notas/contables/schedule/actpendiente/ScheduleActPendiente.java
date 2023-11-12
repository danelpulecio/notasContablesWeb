package co.com.bbva.app.notas.contables.schedule.actpendiente;

import org.quartz.CronTrigger;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerFactory;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

public class ScheduleActPendiente {
	private final String cron;
	private final String mail;
	private static final SchedulerFactory sf = new StdSchedulerFactory();
	private static Scheduler sched;

	private static final Logger LOGGER = LoggerFactory.getLogger(ScheduleActPendiente.class);
	public ScheduleActPendiente(String cron, String mail) {
		super();
		this.cron = cron;
		this.mail = mail;
	}

	public void run() throws Exception {

		sched = sf.getScheduler();

		JobDetail job = newJob(ActPendienteJob.class).withIdentity("jobActPendiente", "group7").usingJobData("mail", mail).build();
		CronTrigger trigger = newTrigger().withIdentity("triggerActPendiente", "group7").withSchedule(cronSchedule(cron)).build();
		sched.scheduleJob(job, trigger);

		sched.start();

	}

	public void destroy() {
		try {
			if (sched != null && sched.isStarted()) {
				sched.shutdown(true);
			}
		} catch (Exception e) {
			LOGGER.error("Error deteniendo hilo " , e);
		}
	}

}
