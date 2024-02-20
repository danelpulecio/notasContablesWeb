package co.com.bbva.app.notas.contables.schedule.historico;

import org.quartz.CronTrigger;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerFactory;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PreDestroy;

import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

public class ScheduleCierreMensual {

	private final String cronCierreHist;
	private final String cronCierreAnulado;
	private final String cronCierreAnular;
	private static final SchedulerFactory sf = new StdSchedulerFactory();
	private static Scheduler sched;

	private static final Logger LOGGER = LoggerFactory.getLogger(ScheduleCierreMensual.class);


	public ScheduleCierreMensual(String cronCierreHist, String cronCierreAnulado, String cronCierreAnular) {
		super();
		this.cronCierreHist = cronCierreHist;
		this.cronCierreAnulado = cronCierreAnulado;
		this.cronCierreAnular = cronCierreAnular;
	}

	public void run() throws Exception {

		sched = sf.getScheduler();

		JobDetail job = newJob(CierreMensualHistJob.class).withIdentity("jobCierreMensual", "group3").build();
		CronTrigger trigger = newTrigger().withIdentity("triggerCierreMensual", "group3").withSchedule(cronSchedule(cronCierreHist)).build();
		JobDetail job1 = newJob(CierreMensualAnuladoJob.class).withIdentity("jobCierreMensualAnuladoJob", "group4").build();
		CronTrigger trigger1 = newTrigger().withIdentity("triggerCierreMensualAnuladoJob", "group4").withSchedule(cronSchedule(cronCierreAnulado)).build();
		JobDetail job2 = newJob(CierreMensualAnularJob.class).withIdentity("jobCierreMensualAnularJob", "group5").build();
		CronTrigger trigger2 = newTrigger().withIdentity("triggerCierreMensualAnularJob", "group5").withSchedule(cronSchedule(cronCierreAnular)).build();

		sched.scheduleJob(job, trigger);
		sched.scheduleJob(job1, trigger1);
		sched.scheduleJob(job2, trigger2);

		sched.start();

	}
	@PreDestroy
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
