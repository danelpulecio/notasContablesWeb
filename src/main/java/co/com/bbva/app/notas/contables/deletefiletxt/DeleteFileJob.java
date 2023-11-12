package co.com.bbva.app.notas.contables.deletefiletxt;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

public class DeleteFileJob implements Job {

	private final DeleteTransmisionBase deleteTransmision = new DeleteTransmisionBase();
	private static final String FILE_BANDERA = "ARCHIVO_BANDERA_03CIER_SUBEOK.TXT";
	private static final String FILE_NAME_1 = "ARCHIVO_ENT_CREATERC_NTCON.TXT";
	private static final String FILE_NAME_2 = "ARCHIVO_ENT_INTERCONTA_NTCON.TXT";
	private static final String FILE_NAME_3 = "ARCHIVO_ENT_PLANCTAS_NTCON.TXT";
	private static final String FILE_NAME_4 = "ARCHIVO_NC_INDICAPC_NTCON.TXT";
	private static final String FILE_FLAG_1 = "FLAG_COMPLETADO_INTERCONTA_NTCON.TXT";
	private static final String FILE_FLAG_2 = "FLAG_COMPLETADO_PUC_NTCON.TXT";
	private static final String FILE_FLAG_3 = "FLAG_COMPLETADO_TERCEROS_NTCON.TXT";
	private static final String FILE_FLAG_4 = "FLAG_COMPLETADO_SIRO_NTCON.TXT";

	private static final Logger LOGGER = LoggerFactory.getLogger(DeleteFileJob.class);

	public DeleteFileJob() {
	}

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {

		JobDataMap dataMap = context.getJobDetail().getJobDataMap();
		String pathName = dataMap.getString("pathName");

		File fileBandera = new File(pathName + FILE_BANDERA);
		
		if (fileBandera.exists()) {
			executeDelete(pathName, FILE_NAME_1);
			executeDelete(pathName, FILE_NAME_2);
			executeDelete(pathName, FILE_NAME_3);
			executeDelete(pathName, FILE_NAME_4);
			executeDelete(pathName, FILE_FLAG_1);
			executeDelete(pathName, FILE_FLAG_2);
			executeDelete(pathName, FILE_FLAG_3);
			executeDelete(pathName, FILE_FLAG_4);
			executeDelete(pathName, FILE_BANDERA);
		}
	}

	/**
	 * Permite eliminar el archivo
	 * 
	 * @param pathName localizacin del archivo
	 * @param fileName nombre del archivo
	 */
	private void executeDelete(String pathName, String fileName) {
		File file = new File(pathName + fileName);
		if (file.exists()) {
			try {
				deleteTransmision.deleteFile(file);
				LOGGER.info("Se elimina el archivo " + fileName + "de " + pathName);
			} catch (Exception e) {

			}

		} else {
			LOGGER.error("No se ha encontrado el archivo indicado para proceder con el borrado: " + pathName + fileName);
		}
	}

}
