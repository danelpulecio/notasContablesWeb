package co.com.bbva.app.notas.contables.schedule;

import org.quartz.SchedulerException;

public class ScheduleActualizaUsuarios {
	
	private final String cronUpdateUsuarios;

	public ScheduleActualizaUsuarios(String cronUpdateUsuarios){
		super();
		this.cronUpdateUsuarios = cronUpdateUsuarios;
	}
	public void execute() throws SchedulerException {

		}
}
