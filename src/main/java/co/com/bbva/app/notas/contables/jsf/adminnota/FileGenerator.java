package co.com.bbva.app.notas.contables.jsf.adminnota;

import co.com.bbva.app.notas.contables.dto.*;
import co.com.bbva.app.notas.contables.facade.CargaAltamiraSession;
import co.com.bbva.app.notas.contables.facade.NotasContablesSession;
import co.com.bbva.app.notas.contables.util.DateUtils;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;

public class FileGenerator {

	private final NotasContablesSession notasContablesManager;
	private final CargaAltamiraSession cargaAltamiraManager;
	private final int codigoUsuario;
	private static final String FLAG_COMPLETADO_INTERCONTA_NTCON = "FLAG_COMPLETADO_INTERCONTA_NTCON.TXT";
	private static final String FLAG_COMPLETADO_PUC_NTCON = "FLAG_COMPLETADO_PUC_NTCON.TXT";
	private static final String FLAG_COMPLETADO_TERCEROS_NTCON = "FLAG_COMPLETADO_TERCEROS_NTCON.TXT";
	private static final String FLAG_COMPLETADO_SIRO_NTCON = "FLAG_COMPLETADO_SIRO_NTCON.TXT";

	// private static int consecutivo = 0;

	public FileGenerator(NotasContablesSession notasContablesManager, CargaAltamiraSession cargaAltamiraManager, int codigoUsuario) {
		super();
		this.notasContablesManager = notasContablesManager;
		this.cargaAltamiraManager = cargaAltamiraManager;
		this.codigoUsuario = codigoUsuario;
	}

	public void generarPUC(String rutaArchivo, String dir_tx_altamira) throws Exception {
		FileWriter fichero = new FileWriter(rutaArchivo);
		PrintWriter pw = new PrintWriter(fichero);
		for (String str : cargaAltamiraManager.getPUCArchAltamira()) {
			pw.println(str);
		}
		pw.close();
		FlagsGeneratorPUC(rutaArchivo, dir_tx_altamira);
	}

	public void generarTerceros(String rutaArchivo, String dir_tx_altamira) throws Exception {
		FileWriter fichero = new FileWriter(rutaArchivo);
		PrintWriter pw = new PrintWriter(fichero);
		for (String str : cargaAltamiraManager.getTercerosArch()) {
			pw.println(str);
		}
		pw.close();
		FlagsGeneratorTerceros(rutaArchivo, dir_tx_altamira);
	}
	/**GP10266 CREACION ARCHIVO VACIO INDICA EL PRECIERRE**/
	public void generarIndicaPrecierre(String rutaArchivo) throws Exception {
		FileWriter fichero = new FileWriter(rutaArchivo);
		PrintWriter pw = new PrintWriter(fichero);
		String str="";
			pw.println(str);
		pw.close();
	}
	
	/**GP10266 CREACION ARCHIVO CON LETRA C DE CIERRE**/
	public void generarIndicaCierre(String rutaArchivo) throws Exception {
		FileWriter fichero = new FileWriter(rutaArchivo);
		PrintWriter pw = new PrintWriter(fichero);
		String str="C";
			pw.println(str);
		pw.close();
	}

	public void FlagsGeneratorInterconta(String rutaArchivo, String dir_tx_altamira) throws Exception
	{
		FileWriter fichero = new FileWriter(dir_tx_altamira+FLAG_COMPLETADO_INTERCONTA_NTCON);
		PrintWriter pw = new PrintWriter(fichero);
		String str="";
			pw.println(str);
		pw.close();	
	}
	public void FlagsGeneratorSIRO(String rutaArchivo, String dir_tx_altamira) throws Exception
	{
		FileWriter fichero = new FileWriter(dir_tx_altamira+FLAG_COMPLETADO_SIRO_NTCON);
		PrintWriter pw = new PrintWriter(fichero);
		String str="";
			pw.println(str);
		pw.close();	
	}
	public void FlagsGeneratorPUC(String rutaArchivo, String dir_tx_altamira) throws Exception
	{
		FileWriter fichero = new FileWriter(dir_tx_altamira+FLAG_COMPLETADO_PUC_NTCON);
		PrintWriter pw = new PrintWriter(fichero);
		String str="";
			pw.println(str);
		pw.close();
	}
	
	public void FlagsGeneratorTerceros(String rutaArchivo, String dir_tx_altamira) throws Exception
	{
		FileWriter fichero = new FileWriter(dir_tx_altamira+FLAG_COMPLETADO_TERCEROS_NTCON);
		PrintWriter pw = new PrintWriter(fichero);
		String str="";
			pw.println(str);
		pw.close();
	}
	
	/**
	 * Permite generar archivo siro
	 * @param rutaArchivo
	 * @throws Exception
	 */
	public void generarArchivoSiro(String rutaArchivo, String dir_tx_altamira)throws Exception {
		FileWriter fichero = new FileWriter(rutaArchivo);
		PrintWriter pw = new PrintWriter(fichero);
		for (String str : notasContablesManager.getInformacionSiro()) {
			pw.println(str);
		}
		pw.close();
		FlagsGeneratorSIRO(rutaArchivo, dir_tx_altamira);
	}

	public void generarArchivoConciliacion(String rutaArchivo, String dir_tx_altamira , String fechaDesde , String fechaHasta)throws Exception {
		FileWriter fichero = new FileWriter(rutaArchivo);
		PrintWriter pw = new PrintWriter(fichero);
		for (String str : notasContablesManager.getDataConciliation(fechaDesde , fechaHasta)) {
			pw.println(str);
		}
		pw.close();
	}

	public void generarInterfazContable(String rutaArchivo, final List<Instancia> instancias, String dir_tx_altamira) throws Exception {
		FileWriter fichero = new FileWriter(rutaArchivo);
		PrintWriter pw = new PrintWriter(fichero);
		ArrayList<java.util.Date> diasNoHabiles = new ArrayList<java.util.Date>(cargaAltamiraManager.getFestivosFecha());
		for (Instancia instancia : instancias) {
			NotaContable notaContable = instancia.getNC();
			if (notaContable.getTipoNota().equals(NotaContable.NORMAL)) {

				Collection<NotaContableTema> temasNota = notasContablesManager.getTemasPorNotaContable(notaContable.getCodigo().intValue());
				/******************INCIDENCIA GENERACION PRECIERRE SABADOS**************/
				for (NotaContableTema t : temasNota) {
					// si es necesario, se actualiza la fecha contable de la nota
					
					if (fechaInvalida(t.getCodigoSucursalDestinoPartida(), t.getFechaContable(), diasNoHabiles) || fechaInvalida(t.getCodigoSucursalDestinoContraPartida(), t.getFechaContable(), diasNoHabiles)) {
						t.setFechaContable(new Date(Calendar.getInstance().getTimeInMillis()));
						notasContablesManager.updateFechaNotaContableTema(t, codigoUsuario);
						break;
					}
					//fechaNota = t.getFechaContable();
					//existeDiaNoHabil = diasNoHabiles.contains(fechaNota);
				}				
				for (String str : notasContablesManager.getInfoGeneralArchAltamira(notaContable.getCodigo().intValue())) {
					pw.println(str);
				}
			} else if (notaContable.getTipoNota().equals(NotaContable.CRUCE_REFERENCIA)) {
				ArrayList<NotaContableCrucePartidaPendiente> temasNotaContable = new ArrayList<NotaContableCrucePartidaPendiente>(notasContablesManager.getCrucesPartidasPendientesPorNotaContable(notaContable.getCodigo().intValue()));
				boolean update = true;  //inicial false PARA REFERENCIA DE CRUCE LA FECHA SIEMPRE ES LA DEL DIA
				for (NotaContableCrucePartidaPendiente t : temasNotaContable) {
					 //'si es necesario, se actualiza la fecha contable de la nota
					if (fechaInvalida(t.getCodigoSucursalDestino(), new Date(t.getFechaContable().getTime()), diasNoHabiles)) {
						update = true;
						break;
					}
				}
				if (update) {
					for (NotaContableCrucePartidaPendiente t : temasNotaContable) {
						// 'si es necesario, se actualiza la fecha contable de la nota
						/******************INCIDENCIA GENERACION PRECIERRE SABADOS**************/
						//cambioFechaNoValida(t, diasNoHabiles);
						t.setFechaContable(new Timestamp(Calendar.getInstance().getTimeInMillis()));
						notasContablesManager.updateFechaNotaContableCruceRef(t, codigoUsuario);
					}
				} 
				for (String str : notasContablesManager.getInfoCruceArchAltamira(notaContable.getCodigo().intValue())) {
					pw.println(str);
				}
			} else if (notaContable.getTipoNota().equals(NotaContable.LIBRE)) {
				ArrayList<NotaContableRegistroLibre> temasNotaContable = new ArrayList<NotaContableRegistroLibre>(notasContablesManager.getRegistrosNotaContableLibre(notaContable.getCodigo()));
				boolean update = false;
				for (NotaContableRegistroLibre t : temasNotaContable) {
					// si es necesario, se actualiza la fecha contable de la nota
					if (fechaInvalida(t.getCodigoSucursalDestino(), t.getFechaContable(), diasNoHabiles)) {
						update = true;
						break;
					}
				}
				if (update) {
					for (NotaContableRegistroLibre t : temasNotaContable) {
						// si es necesario, se actualiza la fecha contable de la nota
						t.setFechaContable(new Date(Calendar.getInstance().getTimeInMillis()));
						notasContablesManager.updateFechaNotaContableRegLibre(t, codigoUsuario);
						/******************INCIDENCIA GENERACION PRECIERRE SABADOS**************/						 
					}
				}
				for (String str : notasContablesManager.getInfoLibreArchAltamira(notaContable.getCodigo().intValue())) {
					pw.println(str);
				}
			}
		}
		pw.close();
		FlagsGeneratorInterconta(rutaArchivo, dir_tx_altamira);
	}

	/**
	 * Verifica si es necesario cambiar la fecha segun los dias habilitados para cada sucursal
	 * 
	 * @param suc
	 * @param fechaContable
	 * @param diasNoHabiles
	 * @return
	 * @throws Exception
	 */
	private boolean fechaInvalida(String suc, Date fechaContable, ArrayList<java.util.Date> diasNoHabiles) throws Exception {
		FechaHabilitada fecha = new FechaHabilitada();
		fecha.setCodigoSucursal(suc);
		fecha = notasContablesManager.getFechaHabilitadaPorSucursal(fecha);

		java.util.Date minFechaSuc = DateUtils.getDateTodayBeforeDays(fecha.getDias().intValue(), diasNoHabiles);

		// si la fecha contable es menor a la minima permitida, se debe ajustar al da de hoy
		return fechaContable.getTime() < minFechaSuc.getTime();
	}
		
}
