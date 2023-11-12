package co.com.bbva.app.notas.contables.servlet;

import co.com.bbva.app.notas.contables.deletefilesiro.ScheduleDeleteFileSiro;
import co.com.bbva.app.notas.contables.deletefiletxt.ScheduleDeleteFile;
import co.com.bbva.app.notas.contables.schedule.ScheduleActualizaUsuarios;
import co.com.bbva.app.notas.contables.schedule.ScheduleLoadData;
import co.com.bbva.app.notas.contables.schedule.actpendiente.ScheduleActPendiente;
import co.com.bbva.app.notas.contables.schedule.cierre.ScheduleLoadCierreFile;
import co.com.bbva.app.notas.contables.schedule.historico.ScheduleCierreMensual;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

public class ExecuteScheduleServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private static boolean executed = false;
	private static ScheduleLoadData st;
	private static ScheduleLoadCierreFile stLC;
	private static ScheduleCierreMensual stCM;
	private static ScheduleActPendiente stAP;
	private static ScheduleActualizaUsuarios stActUsu;
	private static ScheduleDeleteFile stDelFile;
	private static ScheduleDeleteFileSiro stsDelFileSiro;

	private static final Logger LOGGER = LoggerFactory.getLogger(ExecuteScheduleServlet.class);

	public ExecuteScheduleServlet() {
		super();
	}

	@Override
	public void init(ServletConfig config) throws ServletException {
		if (executed) {
			return;
		}
		executed = true;
		super.init();
		if (st == null) {
			try {
				st = new ScheduleLoadData(config.getServletContext().getInitParameter("DIR_CARGA"), config.getInitParameter("cron"));
				st.run();
			} catch (Exception e) {
				LOGGER.error("Error iniciando tarea de carga de archivos " , e);
			}
		}
		/*****************************************/
		if (stActUsu == null) {
			try {
				stActUsu = new ScheduleActualizaUsuarios(config.getInitParameter("cronUpdateUsuarios"));
				stActUsu.execute();
			} catch (Exception e) {
				LOGGER.error("Error iniciando tarea de carga de archivos " , e);

			} 
		}
		
		/*****************************************/
		
		if (stLC == null) {
			try {
				stLC = new ScheduleLoadCierreFile(config.getServletContext().getInitParameter("DIR_RECEPCION_ALTAMIRA"), config.getInitParameter("cron"));
				stLC.run();
			} catch (Exception e) {
				LOGGER.error("Error iniciando tarea de recepcin de altamira (precierre-cierre) " , e);
			}
		}
		/*****************************************/
		if (stCM == null) {
			try {
				stCM = new ScheduleCierreMensual(config.getInitParameter("cronCierreHist"), config.getInitParameter("cronCierreAnulado"), config.getInitParameter("cronCierreAnular"));
				stCM.run();
			} catch (Exception e) {
				LOGGER.error("Error iniciando tarea de cierre de notas y copia a tablas de historicos " , e);
			}
		}
		/*****************************************/
		if (stAP == null) {
			try {
				stAP = new ScheduleActPendiente(config.getInitParameter("cronActPendiente"), config.getInitParameter("mail"));
				stAP.run();
			} catch (Exception e) {
				LOGGER.error("Error iniciando tarea de envio de correos para actividades pendientes " , e);
			}
		}
		/*****************************************/
		if (stDelFile == null) {
			try {
				stDelFile = new ScheduleDeleteFile(config.getServletContext().getInitParameter("DIR_TRANSMISION_ALTAMIRA"), config.getInitParameter("cronDeleteFile"));
				stDelFile.run();
			} catch (Exception e) {
				LOGGER.error("Error iniciando tarea al eliminar el archivo. " , e);
			}
		}
		/*****************************************/
		if (stsDelFileSiro == null) {
			try {
				stsDelFileSiro = new ScheduleDeleteFileSiro(config.getServletContext().getInitParameter("DIR_SIRO"), config.getInitParameter("cronDeleteSiro"));
				stsDelFileSiro.run();
			} catch (Exception e) {
				LOGGER.error("Error iniciando tarea al eliminar el archivo de siro. " , e);
			}
		}
	}

	@Override
	public void destroy() {
		super.destroy();
		if (st != null) {
			st.destroy();
		}
		if (stLC != null) {
			stLC.destroy();
		}
		if (st != null) {
			stCM.destroy();
		}
		if (stAP != null) {
			stAP.destroy();
		}
		if(stDelFile != null) {
			stDelFile.destroy();
		}
		if(stsDelFileSiro != null) {
			stsDelFileSiro.destroy();
		}

	}
}