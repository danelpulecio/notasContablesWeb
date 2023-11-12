package co.com.bbva.app.notas.contables.dao;

import co.com.bbva.app.notas.contables.dto.RiesgoOperacional;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.Collection;

public class NotaContableTemaRiesgoDAO extends CommonSeqDAO<RiesgoOperacional> {

	// se adicionan campo krb
	private static String cs_COLUMNAS = "CODIGO, CODIGO_NOTA_CONTABLE, CODIGO_TEMA, IMPORTE_PARCIAL, IMPORTE_TOTAL, FECHA_EVENTO, CODIGO_TIPO_PERDIDA, CODIGO_CLASE_RIESGO, FECHA_DESCUBRIMIENTO, CODIGO_PROCESO, CODIGO_LINEA_OPER, CODIGO_PRODUCTO,"
			+ "FECHA_FIN_EVENTO, HORA_INICIO_EVENTO, HORA_FIN_EVENTO, HORA_DESCUBRIMIENTO, APP_RECUPERACION, CODIGO_OPER_N2_SFC, CODIGO_OPER_N3_SFC, NOMBRE_OUTSOURCE, CODIGO_SUBPRODUCTO, CODIGO_CANAL, CODIGO_SUBCANAL, FECHA_RECUPERACION, HORA_RECUPERACION, TIPO_RECUPERACION";

	private static String cs_TABLA = "NC_NOTA_CONTABLE_TEMA_RIESGO";

	private static String cs_PK = "CODIGO";

	private static String SQL_SELECT_BY_TEMA_NOTA_CONTABLE_SENTENCE = "SELECT " + cs_COLUMNAS + " FROM " + cs_TABLA + " WHERE (CODIGO_TEMA = ?) ORDER BY CODIGO";

	private static String SQL_SELECT_BY_NOTA_CONTABLE_SENTENCE = "SELECT " + cs_COLUMNAS + " FROM " + cs_TABLA + " WHERE (CODIGO_NOTA_CONTABLE = ?) ORDER BY CODIGO";

	private static String SQL_SELECT_BY_NOTA_CONTABLE_AND_TEMA_NOTA_CONTABLE_SENTENCE = "SELECT \r\n" + 
			"NC.CODIGO \"codigo\",\r\n" + 
			"NC.CODIGO_NOTA_CONTABLE \"codigoNotaContable\",\r\n" + 
			"NC.CODIGO_TEMA \"codigoTemaNotaContable\",\r\n" + 
			"NC.IMPORTE_PARCIAL \"importeParcial\",\r\n" + 
			"NC.IMPORTE_TOTAL \"importeTotal\",\r\n" + 
			"NC.FECHA_EVENTO \"fechaEventoTS\", \r\n" + 
			"NC.CODIGO_TIPO_PERDIDA \"codigoTipoPerdida\",\r\n" + 
			"NC.CODIGO_CLASE_RIESGO \"codigoClaseRiesgo\",\r\n" + 
			"NC.FECHA_DESCUBRIMIENTO \"fechaDescubrimientoEventoTS\",\r\n" + 
			"NC.CODIGO_PROCESO \"codigoProceso\",\r\n" + 
			"NC.CODIGO_LINEA_OPER \"codigoLineaOperativa\",\r\n" + 
			"NC.CODIGO_PRODUCTO \"codigoProducto\",\r\n" + 
			"NC.FECHA_FIN_EVENTO \"fechaFinalEventoTS\", \r\n" + 
			"NC.APP_RECUPERACION \"appRecuperacion\",\r\n" + 
			"NC.CODIGO_OPER_N2_SFC \"codigoClaseRiesgoN2\",\r\n" + 
			"NC.CODIGO_OPER_N3_SFC \"codigoClaseRiesgoN3\",\r\n" + 
			"NC.NOMBRE_OUTSOURCE \"nombreOutSource\",\r\n" + 
			"NC.CODIGO_SUBPRODUCTO \"codigoSubProducto\",\r\n" + 
			"NC.CODIGO_CANAL \"codigoCanal\",\r\n" + 
			"NC.CODIGO_SUBCANAL \"codigoSubCanal\",\r\n" + 
			"NC.FECHA_RECUPERACION \"fechaRecuperacionTS\",\r\n" + 
			"SUBSTR(NC.HORA_INICIO_EVENTO,0,2) \"horaInicioEvento\",\r\n" + 
			"SUBSTR(NC.HORA_INICIO_EVENTO,4,2) \"minutosInicioEvento\",\r\n" + 
			"SUBSTR(NC.HORA_INICIO_EVENTO,7,3) \"horario\", \r\n" + 
			"SUBSTR(NC.hora_fin_evento,0,2) \"horaFinalEvento\", \r\n" + 
			"SUBSTR(NC.hora_fin_evento,4,2) \"minutosFinalEvento\", \r\n" + 
			"SUBSTR(NC.hora_fin_evento,7,3) \"horarioFinal\",\r\n" + 
			"SUBSTR(NC.hora_descubrimiento,0,2) \"horaDescubrimiento\",\r\n" + 
			"SUBSTR(NC.hora_descubrimiento,4,2) \"minutosDescubrimiento\", \r\n" + 
			"SUBSTR(NC.hora_descubrimiento,7,3) \"horarioDescubre\",\r\n" + 
			"SUBSTR(NC.HORA_RECUPERACION,0,2) \"horaRecuperacion\",\r\n" + 
			"SUBSTR(NC.HORA_RECUPERACION,4,2) \"minutosRecuperacion\", \r\n" + 
			"SUBSTR(NC.HORA_RECUPERACION,7,3) \"horarioRecuperacion\",\r\n" + 
			"NC.TIPO_RECUPERACION \"tipoRecuperacion\",\r\n" + 
			"PER.DESCRIPCION \"tipoPerdida__descripcion\",\r\n" + 
			"CLA.NOMBRE \"claseRiesgo__nombre\",\r\n" + 
			"RIE.NOMBRE \"proceso__nombre\",\r\n" + 
			"RIEL.NOMBRE \"lineaOperativa__nombre\",\r\n" + 
			"RIEP.NOMBRE \"producto__nombre\",\r\n" + 
			"SUBPRODUCTO.NOMBRE \"subProductoAfectado__nombre\",\r\n" + 
			"OPER_N2.NOMBRE \"claseRiesgoN2__nombre\",\r\n" + 
			"OPER_N3.NOMBRE \"claseRiesgoN3__nombre\",\r\n" + 
			"CANAL.DESCRIPCION \"canal__descripcion\",\r\n" + 
			"SUBCANAL.DESCRIPCION \"subCanal__descripcion\",\r\n" + 
			"NC.HORA_INICIO_EVENTO \"horaTotalInicio\",\r\n" + 
			"NC.hora_fin_evento \"horaTotalFin\",\r\n" + 
			"NC.hora_descubrimiento \"horaTotalDescubre\",\r\n" + 
			"NC.HORA_RECUPERACION \"horaTotalRecuperacion\"\r\n" + 
			"FROM NC_NOTA_CONTABLE_TEMA_RIESGO NC \r\n" + 
			"LEFT JOIN NC_PERDIDA_OPERACION PER ON (PER.CODIGO = NC.CODIGO_TIPO_PERDIDA) \r\n" + 
			"LEFT JOIN NC_CLASE_RIESGO CLA ON (CLA.CODIGO = NC.CODIGO_CLASE_RIESGO) \r\n" + 
			"LEFT JOIN NC_RIES_OPER_PROC RIE ON (RIE.CODIGO = NC.CODIGO_PROCESO) \r\n" + 
			"LEFT JOIN NC_RIES_OPER_LINE_OPER RIEL ON (RIEL.CODIGO = NC.CODIGO_LINEA_OPER) \r\n" + 
			"LEFT JOIN NC_RIES_OPER_PROD RIEP ON (RIEP.CODIGO = NC.CODIGO_PRODUCTO) \r\n" +
			"LEFT JOIN NC_OPER_N2_SFC OPER_N2 ON (OPER_N2.CODIGO = NC.CODIGO_OPER_N2_SFC) AND (OPER_N2.CODIGO_NC_CLASE_RIESGO = NC.CODIGO_CLASE_RIESGO)  \r\n" +
			"LEFT JOIN NC_OPER_N3_SFC OPER_N3 ON (OPER_N3.CODIGO = NC.CODIGO_OPER_N3_SFC) AND (OPER_N3.CODIGO_N1_SFC = NC.CODIGO_CLASE_RIESGO)  AND (OPER_N3.CODIGO_N2_SFC = NC.CODIGO_OPER_N2_SFC) \r\n" +
			"LEFT JOIN NC_RIES_OPER_SUBPROD SUBPRODUCTO ON (SUBPRODUCTO.CODIGO = NC.CODIGO_SUBPRODUCTO)\r\n" +
			"LEFT JOIN NC_CANAL CANAL ON (CANAL.CODIGO = NC.CODIGO_CANAL)\r\n" + 
			"LEFT JOIN NC_SUBCANALES SUBCANAL ON(SUBCANAL.CODIGO = NC.CODIGO_SUBCANAL)\r\n" + 
			"WHERE (NC.CODIGO_NOTA_CONTABLE = ?) AND (NC.CODIGO_TEMA = ?) ORDER BY NC.CODIGO";

	private static String SQL_DELETE_BY_CODIGO_TEMA_SENTENCE = " DELETE FROM " + cs_TABLA + " WHERE (CODIGO_TEMA = ?)";

	public NotaContableTemaRiesgoDAO() {
		super(cs_TABLA, cs_COLUMNAS, cs_PK, new RiesgoOperacional());
	}

	public void deleteByTemaNotaContable(Connection con, int codigoTemaNotaContable, int codigoUsuario) throws Exception {
		executeUpdate(con, SQL_DELETE_BY_CODIGO_TEMA_SENTENCE, codigoTemaNotaContable);
		/**
		 * // BLOQUEO BASE DE DATOS String xmlDataOriginal = getXMLDataByTemaNotaContable(con, codigoTemaNotaContable); if
		 * (!xmlDataOriginal.isEmpty()) { int idAuditoria = addRegistroAuditoria(con, codigoUsuario, "Borrar Riesgo Operacional
		 * del tema ", Tema.class.getSimpleName(), "" + codigoTemaNotaContable); addRegistroAuditoriaDetalle(con, idAuditoria,
		 * xmlDataOriginal, ""); }
		 **/
	}

	public Collection<RiesgoOperacional> findByTemaNotaContable(int codigoTemaNotaContable) throws Exception {
		return findByGeneral(SQL_SELECT_BY_TEMA_NOTA_CONTABLE_SENTENCE, codigoTemaNotaContable);
	}

	public RiesgoOperacional findByNotaContableAndTemaNotaContable(int codigoNotaContable, int codigoTemaNotaContable) throws Exception {

		return obtenerObjeto(SQL_SELECT_BY_NOTA_CONTABLE_AND_TEMA_NOTA_CONTABLE_SENTENCE, codigoNotaContable, codigoTemaNotaContable);
	}

	public String getXMLDataByTemaNotaContable(Connection con, int codigoTemaNotaContable) throws Exception {
		return getXMLDataGeneral(con, SQL_SELECT_BY_TEMA_NOTA_CONTABLE_SENTENCE, codigoTemaNotaContable);
	}

	@Override
	protected Object[] getDataToAdd(Connection con, RiesgoOperacional row) throws Exception {
		row.setCodigo(getMaxCode(con));
		
		return new Object[] { row.getCodigo(), row.getCodigoNotaContable(), row.getCodigoTemaNotaContable(), row.getImporteParcial(), row.getImporteTotal(), row.getFechaEvento(),
				row.getCodigoTipoPerdida(), row.getCodigoClaseRiesgo(), row.getFechaDescubrimientoEvento(), row.getCodigoProceso(), row.getCodigoLineaOperativa(),
				row.getCodigoProducto(), row.getFechaFinEvento(), row.getHoraInicioEvento() + ":" + row.getMinutosInicioEvento() + ":" + row.getHorario(),
				row.getHoraFinalEvento() + ":" + row.getMinutosFinalEvento() + ":" + row.getHorarioFinal(),
				row.getHoraDescubrimiento() + ":" + row.getMinutosDescubrimiento() + ":" + row.getHorarioDescubre(), row.getAppRecuperacion(),
				row.getCodigoClaseRiesgoN2(), row.getCodigoClaseRiesgoN3(), row.getNombreOutSource(), row.getCodigoSubProducto(), row.getCodigoCanal(), row.getCodigoSubCanal(),
				row.getFechaRecuperacion(), row.getHoraRecuperacion() + ":" + row.getMinutosRecuperacion() + ":" + row.getHorarioRecuperacion(), row.getTipoRecuperacion() };
	}

	@Override
	public RiesgoOperacional getResultSetToVO(ResultSet result) throws Exception {
		RiesgoOperacional row = new RiesgoOperacional();

		row.setCodigo(result.getInt(1));
		row.setCodigoNotaContable(result.getInt(2));
		row.setCodigoTemaNotaContable(result.getInt(3));
		row.setImporteParcial(result.getDouble(4));
		row.setImporteTotal(result.getDouble(5));
		row.setFechaEvento(result.getDate(6));
		row.setCodigoTipoPerdida(result.getString(7));
		row.setCodigoClaseRiesgo(result.getString(8));
		row.setFechaDescubrimientoEvento(result.getDate(9));
		row.setCodigoProceso(result.getString(10));
		row.setCodigoLineaOperativa(result.getString(11));
		row.setCodigoProducto(result.getString(12));
		// krb adicion de nuevos campos
		row.setFechaFinEvento(result.getDate(13));
		row.setHoraInicioEvento(result.getString(14));
		row.setHoraFinalEvento(result.getString(15));
		row.setHorarioDescubre(result.getString(16));
		// Campos para CCS1-63 CE025		
		row.setAppRecuperacion(result.getString(17));
		row.setCodigoClaseRiesgoN2(result.getString(18));
		row.setCodigoClaseRiesgoN3(result.getString(19));
		row.setNombreOutSource(result.getString(20));
		row.setCodigoSubProducto(result.getString(21));
		row.setCodigoCanal(result.getString(22));
		row.setCodigoSubCanal(result.getString(23));
		row.setFechaRecuperacion(result.getDate(24));
		row.setHoraRecuperacion(result.getString(25));
		row.setTipoRecuperacion(result.getString(26));

		return row;
	}

	@Override
	protected void internalUpdate(@SuppressWarnings("unused") Connection con, RiesgoOperacional row) throws Exception {
		throw new Exception("Mtodo no implementado");
	}

	public Collection<RiesgoOperacional> findByNotaContable(int codNota) throws Exception {
		return findByGeneral(SQL_SELECT_BY_NOTA_CONTABLE_SENTENCE, codNota);
	}
}