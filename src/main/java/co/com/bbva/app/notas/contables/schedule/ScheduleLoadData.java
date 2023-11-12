package co.com.bbva.app.notas.contables.schedule;

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

public class ScheduleLoadData {
	private final String pathName;
	private final String cron;
	private static final SchedulerFactory sf = new StdSchedulerFactory();
	private static Scheduler sched;

	private static final Logger LOGGER = LoggerFactory.getLogger(ScheduleLoadData.class);

	public ScheduleLoadData(String pathName, String cron) {
		super();
		this.pathName = pathName;
		this.cron = cron;
	}

	public void run() throws Exception {

		sched = sf.getScheduler();
		JobDetail job = newJob(LoadDataJob.class).withIdentity("jobLoadData", "group1").usingJobData("pathName", pathName).build();
		CronTrigger trigger = newTrigger().withIdentity("triggerLoadData", "group1").withSchedule(cronSchedule(cron)).build();
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
