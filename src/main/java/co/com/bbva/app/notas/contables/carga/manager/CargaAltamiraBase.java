package co.com.bbva.app.notas.contables.carga.manager;

import co.com.bbva.app.notas.contables.carga.dto.EstructuraCarga;
import co.com.bbva.app.notas.contables.carga.dto.RegistroCarga;
import co.com.bbva.app.notas.contables.facade.CargaAltamiraSession;
import co.com.bbva.app.notas.contables.facade.NotasContablesSession;
import co.com.bbva.app.notas.contables.facade.impl.CargaAltamiraSessionBean;
import co.com.bbva.app.notas.contables.facade.impl.NotasContablesSessionBean;
import co.com.bbva.app.notas.contables.util.DateUtils;

import java.io.*;

public abstract class CargaAltamiraBase {

	protected CargaAltamiraSession cargaAltamiraManager = new CargaAltamiraSessionBean();
	protected NotasContablesSession notasContablesManager = new NotasContablesSessionBean();

	protected void getDatos(String as_nombreTabla, File f, String as_nombreArchivo, EstructuraCarga[] estructurasCarga, boolean eliminar) throws Exception {
		RegistroCarga registroCarga = new RegistroCarga();
		String ls_linea = "";
		int contadorLineas = 0;

		registroCarga.setFechaInicio(DateUtils.getTimestamp());

		FileInputStream lfis_stream = null;
		DataInputStream in = null;
		BufferedReader lbr_bufferReader = null;
		try {
			lfis_stream = new FileInputStream(f);

			in = new DataInputStream(lfis_stream);
			lbr_bufferReader = new BufferedReader(new InputStreamReader(in));

			cargaAltamiraManager.beginLoad(as_nombreTabla, eliminar);
			while ((ls_linea = lbr_bufferReader.readLine()) != null) {
				try {
					cargaAltamiraManager.addRow(as_nombreTabla, ls_linea, estructurasCarga);

					if (contadorLineas % 1000 == 0) {
						//System.out.println("Registros Cargados: " + contadorLineas);
						// MODIFICACION DE LOGS EN ARCHIVO INDEPENDIENTE
						//Log.escribirLogInfo("Registros Cargados: " + contadorLineas);
					}
					
				} catch (Exception le_e) {
					/** MODIFICACION DE LOGS EN ARCHIVO INDEPENDIENTE**/
					le_e.printStackTrace();
				}
			}
			
			registroCarga.setFechaFin(DateUtils.getTimestamp());
			registroCarga.setNombreArchivo(as_nombreArchivo);
			registroCarga.setRegistrosCargados(contadorLineas);

			// se borra el archivo si todo fue ok
			f.delete();

		} catch (Exception e) {
			throw e;
		} finally {
			try {
				if (lbr_bufferReader != null) {
					lbr_bufferReader.close();
				}
				if (in != null) {
					in.close();
				}
				if (lfis_stream != null) {
					lfis_stream.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
	}

}
