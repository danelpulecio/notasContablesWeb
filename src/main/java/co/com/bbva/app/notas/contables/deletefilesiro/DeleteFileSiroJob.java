package co.com.bbva.app.notas.contables.deletefilesiro;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.io.File;

/**
 *
 * @author pjimenez
* @version 1.0, 21/10/2021
* @since JDK1.8
 *
 */
public class DeleteFileSiroJob implements Job {
	
	private final DeleteTransmisionSiro deleteTransmision = new DeleteTransmisionSiro();
	private static final String FILE_BANDERA = "ARCHIVO_BANDERA_PHADTNT6_OK.TXT";
	private static final String FILE_NAME_1 = "ARCHIVO_NC_OPER_N2_SFC_NTCON.TXT";
	private static final String FILE_NAME_2 = "ARCHIVO_NC_OPER_N3_SFC_NTCON.TXT";
	private static final String FILE_NAME_3 = "ARCHIVO_SIRO_NTCON.TXT";
	
	public DeleteFileSiroJob() {
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
				System.out.println("Se elimina el archivo " + fileName + "de " + pathName);
			} catch (Exception e) {
			}
		} else {
			System.err.println("No se ha encontrado el archivo indicado para proceder con el borrado: " + pathName + fileName);
		}
	}

}
