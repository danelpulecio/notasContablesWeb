package co.com.bbva.app.notas.contables.util;

import co.com.bbva.app.notas.contables.carga.dto.Sucursal;
import co.com.bbva.app.notas.contables.dto.*;
import co.com.bbva.app.notas.contables.facade.impl.CargaAltamiraSessionBean;
import co.com.bbva.app.notas.contables.facade.impl.NotasContablesSessionBean;
import co.com.bbva.app.notas.contables.jsf.adminnota.PrecierreCierrePage;

import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileOutputStream;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.List;

public class ReportesExcel {

	private String strPath = null;
	private String nombreArchivo = null;
	private final NotasContablesSessionBean notasContablesManager = new NotasContablesSessionBean();
	private final CargaAltamiraSessionBean cargaAltamiraManager = new CargaAltamiraSessionBean();
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ReportesExcel.class);

	class DetalleNota implements Serializable {
		private static final long serialVersionUID = 1L;
		private Number codigoTema = 0;
		private String partidaContable = "";
		private String naturalezaPartidaContable = "";
		private String contrapartidaContable = "";
		private String natContContable = "";
		private String codigoSucursalDestinoPartida = "";
		private String codSucDestContraPartida = "";
		private String codigoDivisa = "";
		private Number monto = 0;
		private String numeroIdentificacion1 = "";
		private String nombreCompleto1 = "";
		private String numeroIdentificacion2 = "";
		private String nombreCompleto2 = "";
		private String contrato1 = "";
		private String contrato2 = "";
		private String descripcion = "";
		private String asiento = "";
		private final Timestamp fechaContableTS;

		public DetalleNota(NotaContableTema reg) {
			codigoTema = reg.getCodigoTema();
			partidaContable = reg.getPartidaContable();
			naturalezaPartidaContable = reg.getNaturalezaPartidaContable();
			contrapartidaContable = reg.getContrapartidaContable();
			natContContable = reg.getNaturalezaContrapartidaContable();
			codigoSucursalDestinoPartida = reg.getCodigoSucursalDestinoPartida();
			codSucDestContraPartida = reg.getCodigoSucursalDestinoContraPartida();
			codigoDivisa = reg.getCodigoDivisa();
			monto = reg.getMonto();
			numeroIdentificacion1 = reg.getNumeroIdentificacion1();
			nombreCompleto1 = reg.getNombreCompleto1();
			numeroIdentificacion2 = reg.getNumeroIdentificacion2();
			nombreCompleto2 = reg.getNombreCompleto2();
			contrato1 = reg.getContrato1();
			contrato2 = reg.getContrato2();
			descripcion = reg.getDescripcion();
			asiento = reg.getNumeroAsiento();
			fechaContableTS = reg.getFechaContableTS();
		}

		public DetalleNota(NotaContableRegistroLibre reg) {
			//codigoTema = reg.getCodigo();
			partidaContable = reg.getCuentaContable();
			naturalezaPartidaContable = reg.getNaturalezaCuentaContable();
			codigoSucursalDestinoPartida = reg.getCodigoSucursalDestino();
			codigoDivisa = reg.getCodigoDivisa();
			monto = reg.getMonto();
			numeroIdentificacion1 = reg.getNumeroIdentificacion();
			nombreCompleto1 = reg.getNombreCompleto();
			contrato1 = reg.getContrato();
			asiento = reg.getNumeroAsiento();
			fechaContableTS = new Timestamp(reg.getFechaContable().getTime());
		}

		public DetalleNota(NotaContableCrucePartidaPendiente reg) {
			//codigoTema = reg.getCodigo();
			partidaContable = reg.getCuentaContable();
			naturalezaPartidaContable = reg.getNaturaleza();
			codigoSucursalDestinoPartida = reg.getCodigoSucursalDestino();
			codigoDivisa = reg.getDivisa();
			monto = reg.getImporte();
			asiento = reg.getNumeroAsiento();
			fechaContableTS = reg.getFechaContable();
		}
	}

	public void getReporteGeneral(String reportTitle, Collection<NotaContable> rows) {
		try {
			
			LOGGER.info(" :::: ENTRANDO A GENERAR EL REPORTE ::::: ");

			HSSFWorkbook wb = new HSSFWorkbook();
			HSSFSheet sh = wb.createSheet("Notas Contables");
			sh.setDefaultColumnWidth((short) 30);
			HSSFRow row;

			// Titulo del reporte
			row = sh.createRow((short) 0);
			row.setHeightInPoints(30);
			HSSFCell cell = row.createCell((short) 0);
			cell.setCellValue(reportTitle);
			//cell.setCellType(HSSFCell.CELL_TYPE_STRING);
			cell.setCellType(CellType.STRING);
			//sh.addMergedRegion(new Region(0, (short) 0, 0, (short) 26));
			sh.addMergedRegion(new CellRangeAddress(0, (short) 0, 0, (short) 26));

			HSSFCellStyle cellstyle = wb.createCellStyle();

			HSSFFont font = wb.createFont();
			//font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
			font.setBold(true);
			font.setFontName("Verdana");
			font.setFontHeightInPoints((short) 14);

			//cellstyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
			cellstyle.setAlignment(HorizontalAlignment.CENTER);
			//cellstyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
			cellstyle.setVerticalAlignment(VerticalAlignment.CENTER);
			cellstyle.setFont(font);
			cellstyle.setWrapText(true);
			cell.setCellStyle(cellstyle);

			// Labels del reporte
			String[] labels = { "NÚMERO RADICACIÓN", "NÚMERO ASIENTO", "FECHA DE REGISTRO", "FECHA ALTAMIRA", "TIPO", "CÓD. SUC. ORIGEN", "NOMBRE SUCURSAL ORIGEN", "ESTADO", "CONCEPTO", "TEMA", "PARTIDA", "NAT1", "COD. SUC. DESTINO",
					"NOMBRE SUC. DESTINO", "CONTRAPARTIDA", "NAT2", "COD. SUC. DESTINO", "NOMBRE SUC. DESTINO", "DIVISA", "MONTO", "IDENTIFICACIÓN 1", "NOMBRE COMPLETO 1", "CONTRATO 1", "IDENTIFICACIÓN 2", "NOMBRE COMPLETO 2", "CONTRATO 2",
					"DESCRIPCIÓN" };
			row = sh.createRow((short) 1);
			labelsReport(row, labels, wb);

			// Creaci?n Reporte
			int li_count = 0;
			for (NotaContable nota : rows) {
				if (nota.getTipoNota().equals(NotaContable.NORMAL)) {
					for (NotaContableTema reg : notasContablesManager.getTemasPorNotaContable(nota.getCodigo().intValue())) {
						li_count++;
						row = sh.createRow((short) (li_count + 1));
						reporteGeneralRow(row, nota, new DetalleNota(reg));
					}
				} else if (nota.getTipoNota().equals(NotaContable.LIBRE)) {
					for (NotaContableRegistroLibre reg : notasContablesManager.getRegistrosLibresPorNotaContable(nota.getCodigo().intValue())) {
						li_count++;
						row = sh.createRow((short) (li_count + 1));
						DetalleNota detalle = new DetalleNota(reg);
						detalle.descripcion = nota.getDescripcion();
						reporteGeneralRow(row, nota, detalle);
					}
				} else if (nota.getTipoNota().equals(NotaContable.CRUCE_REFERENCIA)) {
					for (NotaContableCrucePartidaPendiente reg : notasContablesManager.getCrucesPartidasPendientesPorNotaContable(nota.getCodigo().intValue())) {
						li_count++;
						row = sh.createRow((short) (li_count + 1));
						reporteGeneralRow(row, nota, new DetalleNota(reg));
					}
				}
			}
			FileOutputStream fileOut = new FileOutputStream(strPath + nombreArchivo);
			wb.write(fileOut);
			fileOut.close();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private HSSFRow reporteGeneralRow(HSSFRow row, NotaContable notaContable, DetalleNota detalle) {
		HSSFCell cell;

		// Instancia
		Instancia instancia = new Instancia();
		instancia.setCodigoNotaContable(notaContable.getCodigo().intValue());
		try {
			instancia = notasContablesManager.getInstanciaPorNotaContable(instancia);
		} catch (Exception le_e) {
		}

		// Concepto
		Concepto concepto = new Concepto();
		concepto.setCodigo(notaContable.getCodigoConcepto());
		try {
			concepto = notasContablesManager.getConcepto(concepto);
		} catch (Exception le_e) {
		}

		// TipoEvento
		TipoEvento tipoEvento = new TipoEvento();
		tipoEvento.setCodigo(notaContable.getCodigoTipoEvento());
		try {
			tipoEvento = notasContablesManager.getTipoEvento(tipoEvento);
		} catch (Exception le_e) {
		}


		// Sucursal Origen
		Sucursal sucursalOrigen = new Sucursal();
		sucursalOrigen.setCodigo(notaContable.getCodigoSucursalOrigen());
		try {
			sucursalOrigen = cargaAltamiraManager.getSucursal(sucursalOrigen);
		} catch (Exception le_e) {
		}

		// UsuarioModulo
		UsuarioModulo usuarioModulo = new UsuarioModulo();
		usuarioModulo.setCodigo(instancia.getCodigoUsuarioActual().intValue());
		try {
			usuarioModulo = notasContablesManager.getUsuarioModulo(usuarioModulo);
		} catch (Exception le_e) {
		}

		// Rol
		Rol rol = new Rol();
		rol.setCodigo(usuarioModulo.getCodigoRol().intValue());
		try {
			rol = notasContablesManager.getRol(rol);
		} catch (Exception le_e) {
		}

		// Estado
		String nombreEstado = "";
		if (instancia.getEstado().equals("0")) {
			nombreEstado = "Pendiente de aprobación por Subgenrente, Gerente, Área Central";
		} else if (instancia.getEstado().equals("1")) {
			nombreEstado = "Pendiente de revisión";
		} else if (instancia.getEstado().equals("2")) {
			nombreEstado = "Pendiente de aprobación por Padrino - Unidad de Análisis";
		} else if (instancia.getEstado().equals("3")) {
			nombreEstado = "Pendiente de aprobación ente Autorizador";
		} else if (instancia.getEstado().equals("4")) {
			nombreEstado = "Precierre";
		} else if (instancia.getEstado().equals("5")) {
			nombreEstado = "Cierre";
		} else if (instancia.getEstado().equals("6")) {
			nombreEstado = "En Firme";
		} else if (instancia.getEstado().equals("9")) {
			nombreEstado = "Anulada";
		}

		// Filas de Excel

		cell = row.createCell((short) 0);
		cell.setCellValue(notaContable.getNumeroRadicacion());

		cell = row.createCell((short) 1);
		cell.setCellValue(detalle.asiento != null ? detalle.asiento : "");

		cell = row.createCell((short) 2);
		cell.setCellValue((notaContable.getFechaRegistroModuloTs() != null ? StringUtils.getString(notaContable.getFechaRegistroModuloTs(), "dd-MMM-yyyy") : ""));

		cell = row.createCell((short) 3);
		// cell.setCellValue((notaContable.getFechaRegistroAltamiraTs() != null ? StringUtils.getString(notaContable.getFechaRegistroAltamiraTs(), "dd-MMM-yyyy") : ""));
		cell.setCellValue((detalle.fechaContableTS != null ? StringUtils.getString(detalle.fechaContableTS, "dd-MMM-yyyy") : ""));

		cell = row.createCell((short) 4);
		cell.setCellValue((notaContable.getTipoNota().equals(NotaContable.NORMAL) ? "Registro" : notaContable.getTipoNota().equals(NotaContable.CRUCE_REFERENCIA) ? "Cancelación de transitorias por referencia de cruce" : "Contabilidad libre"));

		cell = row.createCell((short) 5);
		cell.setCellValue(sucursalOrigen.getCodigo());

		cell = row.createCell((short) 6);
		cell.setCellValue(sucursalOrigen.getNombre());

		cell = row.createCell((short) 7);
		cell.setCellValue(nombreEstado);

		cell = row.createCell((short) 8);
		cell.setCellValue(concepto.getNombre());

		// Tema
		Tema tema = new Tema();
		if (detalle.codigoTema != null) {
			tema.setCodigo(detalle.codigoTema);
			try {
				tema = notasContablesManager.getTema(tema);
			} catch (Exception le_e) {
			}
		}
		cell = row.createCell((short) 9);
		cell.setCellValue(tema.getNombre());

		cell = row.createCell((short) 10);
		cell.setCellValue(detalle.partidaContable);


		//cell.setCellValue(detalle.naturalezaPartidaContable);
		cell = row.createCell((short) 11);
		if (notaContable.getTipoNota().equals(NotaContable.CRUCE_REFERENCIA)) {
			if (detalle.naturalezaPartidaContable.equals("D")) {
				cell.setCellValue("H");
			} else if (detalle.naturalezaPartidaContable.equals("H")) {
				cell.setCellValue("D");
			}
		} else {
			cell.setCellValue(detalle.naturalezaPartidaContable);
		}

		// Sucursal Destino
		Sucursal sucursalDestino = new Sucursal();
		sucursalDestino.setCodigo(detalle.codigoSucursalDestinoPartida);
		try {
			sucursalDestino = cargaAltamiraManager.getSucursal(sucursalDestino);
		} catch (Exception le_e) {
		}

		cell = row.createCell((short) 12);
		cell.setCellValue(sucursalDestino.getCodigo());

		cell = row.createCell((short) 13);
		cell.setCellValue(sucursalDestino.getNombre());

		cell = row.createCell((short) 14);
		cell.setCellValue(detalle.contrapartidaContable);

		cell = row.createCell((short) 15);
		cell.setCellValue(detalle.natContContable);

		sucursalDestino.setCodigo(detalle.codSucDestContraPartida);
		try {
			sucursalDestino = cargaAltamiraManager.getSucursal(sucursalDestino);
		} catch (Exception le_e) {
		}

		cell = row.createCell((short) 16);
		cell.setCellValue(sucursalDestino.getCodigo());

		cell = row.createCell((short) 17);
		cell.setCellValue(sucursalDestino.getNombre());

		cell = row.createCell((short) 18);
		cell.setCellValue(detalle.codigoDivisa);

		cell = row.createCell((short) 19);
		cell.setCellValue(detalle.monto.doubleValue());

		cell = row.createCell((short) 20);
		cell.setCellValue(detalle.numeroIdentificacion1);

		cell = row.createCell((short) 21);
		cell.setCellValue(detalle.nombreCompleto1);

		cell = row.createCell((short) 22);
		cell.setCellValue(detalle.contrato1);

		cell = row.createCell((short) 23);
		cell.setCellValue(detalle.numeroIdentificacion2);

		cell = row.createCell((short) 24);
		cell.setCellValue(detalle.nombreCompleto2);

		cell = row.createCell((short) 25);
		cell.setCellValue(detalle.contrato2);

		cell = row.createCell((short) 26);
		cell.setCellValue(detalle.descripcion);

		return row;
	}

	private HSSFRow labelsReport(HSSFRow row, String[] labels, HSSFWorkbook wb) {
		HSSFCell cell;
		HSSFCellStyle cellstyle;
		HSSFFont font = wb.createFont();
		//font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		font.setBold(true);
		font.setFontName("Verdana");
		cellstyle = wb.createCellStyle();
		//cellstyle.setBorderBottom(HSSFCellStyle.ALIGN_CENTER);
		cellstyle.setAlignment(HorizontalAlignment.CENTER);
		//cellstyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		cellstyle.setVerticalAlignment(VerticalAlignment.CENTER);
		cellstyle.setFont(font);
		cellstyle.setWrapText(false);

		for (int i = 0; i < labels.length; i++) {
			cell = row.createCell((short) i);
			cell.setCellValue(labels[i]);
			cell.setCellStyle(cellstyle);
		}

		return row;
	}

	public void setNombreArchivo(String string) {
		nombreArchivo = string;
	}

	public String getNombreArchivo() {
		return nombreArchivo;
	}

	public void setStrPath(String string) {
		strPath = string;
	}

	public void getReporteTiempos(String reportTitle, List<UsuarioModulo> datos) {
		try {

			HSSFWorkbook wb = new HSSFWorkbook();
			HSSFSheet sh = wb.createSheet("Notas Contables");
			sh.setDefaultColumnWidth((short) 30);
			HSSFRow row;

			// Titulo del reporte
			row = sh.createRow((short) 0);
			row.setHeightInPoints(30);
			HSSFCell cell = row.createCell((short) 0);
			cell.setCellValue(reportTitle);
			//cell.setCellType(HSSFCell.CELL_TYPE_STRING);
			cell.setCellType(CellType.STRING);
			//sh.addMergedRegion(new Region(0, (short) 0, 0, (short) 26));
			sh.addMergedRegion(new CellRangeAddress(0, (short) 0, 0, (short) 26));

			HSSFCellStyle cellstyle = wb.createCellStyle();

			HSSFFont font = wb.createFont();
			//font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
			font.setBold(true);
			font.setFontName("Verdana");
			font.setFontHeightInPoints((short) 14);

			//cellstyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
			cellstyle.setAlignment(HorizontalAlignment.CENTER);
			//cellstyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
			cellstyle.setVerticalAlignment(VerticalAlignment.CENTER);


			cellstyle.setFont(font);
			cellstyle.setWrapText(true);
			cell.setCellStyle(cellstyle);

			// Labels del reporte
			String[] labels = { "COD EMPLEADO", "NOMBRE EMPLEADO", "COD ÁREA", "NOMBRE ÁREA", "ROL", "DURACIÓN PROMDIO" };
			row = sh.createRow((short) 1);
			labelsReport(row, labels, wb);

			// Creaci?n Reporte
			int li_count = 0;
			for (UsuarioModulo u : datos) {
				li_count++;
				row = sh.createRow((short) (li_count + 1));
				cell = row.createCell((short) 0);
				cell.setCellValue(u.getCodigoEmpleado());

				cell = row.createCell((short) 1);
				cell.setCellValue(u.getUsuAlt().getNombreEmpleado());

				cell = row.createCell((short) 2);
				cell.setCellValue(u.getCodigoAreaModificado());

				cell = row.createCell((short) 3);
				cell.setCellValue(u.getNombreAreaModificado());

				cell = row.createCell((short) 4);
				cell.setCellValue(u.getRol().getNombre());

				cell = row.createCell((short) 5);
				cell.setCellValue(u.getDuracionPromedio().doubleValue());
			}
			FileOutputStream fileOut = new FileOutputStream(strPath + nombreArchivo);
			wb.write(fileOut);
			fileOut.close();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public void getReporteDocumentoSoporte(String reportTitle, String fechaDesde , String fechaHasta ) {
		try {
			String var_SEPARATOR = "//";
			HSSFWorkbook wb = new HSSFWorkbook();
			HSSFSheet sh = wb.createSheet("Documento Soporte");
			sh.setDefaultColumnWidth((short) 30);
			HSSFRow row;

			HSSFCell cell;

			// Labels del reporte
			String[] labels = { "TIPO IDENTIFICACION", "IDENTIFICACION DEL PROVEEDOR", "DV", "NOMBRE O RAZON SOCIAL", "FECHA DE LA OPERACION", "CONCEPTO DE PAGO" , "IMPORTE OPERACION", "IVA" };
			row = sh.createRow((short) 0);
			labelsReport(row, labels, wb);

			// Creación Reporte
			int li_count = 0;
			for (String str : notasContablesManager.getDocumentoSoporteByDate(fechaDesde ,fechaHasta )) {
				li_count++;
				String [] fields = str.split(var_SEPARATOR);

				String v_tipo_identificacion = fields[0];
				String v_identificacion = fields[1];
				String v_DV = fields[2];
				String v_nombre_razon_social = fields[3];
				String v_fecha = fields[4];
				String v_concepto = fields[5];
				String v_importe = fields[6];
				String v_iva = fields[7];

				row = sh.createRow((short) (li_count + 1));
				cell = row.createCell((short) 0);
				cell.setCellValue(v_tipo_identificacion);

				cell = row.createCell((short) 1);
				cell.setCellValue(v_identificacion);

				cell = row.createCell((short) 2);
				cell.setCellValue(v_DV);

				cell = row.createCell((short) 3);
				cell.setCellValue(v_nombre_razon_social);

				cell = row.createCell((short) 4);
				cell.setCellValue(v_fecha);

				cell = row.createCell((short) 5);
				cell.setCellValue(v_concepto);

				cell = row.createCell((short) 6);
				cell.setCellValue(Double.parseDouble(v_importe) );

				cell = row.createCell((short) 7);
				cell.setCellValue(Double.parseDouble(v_iva) );

			}

			FileOutputStream fileOut = new FileOutputStream(strPath + nombreArchivo);
			wb.write(fileOut);
			fileOut.close();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public void getReporteDocumentoSoporteXCuentas(String reportTitle, String fechaDesde , String fechaHasta ) {
		try {
			String var_SEPARATOR = "//";
			HSSFWorkbook wb = new HSSFWorkbook();
			HSSFSheet sh = wb.createSheet("Documento Soporte Cuentas");
			sh.setDefaultColumnWidth((short) 30);
			HSSFRow row;

			HSSFCell cell;

			// Labels del reporte
			String[] labels = { "CODIGO" , "CODIGO_NOTA_CONTABLE" , "FECHA_CONTABLE", "PARTIDA_CONTABLE1" , "NATURALEZA1" , "CONTRAPARTIDA_CONTABLE"
					, "NATURALEZA2" , "CODIGO_DIVISA" , "MONTO" , "TIPO_IDENTIFICACION1" , "DIGITO_VERIFICACION1" ,"NOMBRE_COMPLETO1" ,
					"NUMERO_IDENTIFICACION1" , "TIPO_IDENTIFICACION2", "NUMERO_IDENTIFICACION2" , "DIGITO_VERIFICACION2" , "NOMBRE_COMPLETO2" , "b_codigo"
					,"b_CODIGO_NOTA_CONTABLE" , "estado" , "a_CODIGO" , "a_NUMERO_RADICACION" ,"a_TIPO_NOTA" ,"a_ESTADO" , "CODIGO_IMPUESTO"
					, "base" , "CALCULADO" , "PARTIDA_CONTABLE_impuesto" , "NOMBRE_IMPUESTO" , "VALOR" , "status1" , "status2" , "doc_soporte_1" ,
					"doc_soporte_2" , "descripcion" , "des_tema"  , "codigo_tema" , "NOM_TIPO_IDENTIFIACION1"  , "NOM_TIPO_IDENTIFIACION2" };
			row = sh.createRow((short) 0);
			labelsReport(row, labels, wb);

			// Creación Reporte
			int li_count = 0;
			for (String str : notasContablesManager.getDocumentoSoporteXCuentaByDate(fechaDesde ,fechaHasta )) {
				li_count++;
				row = sh.createRow((short) (li_count + 1));
				cell = row.createCell((short) 0);
				cell.setCellValue(str);


			}

			FileOutputStream fileOut = new FileOutputStream(strPath + nombreArchivo);
			wb.write(fileOut);
			fileOut.close();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}