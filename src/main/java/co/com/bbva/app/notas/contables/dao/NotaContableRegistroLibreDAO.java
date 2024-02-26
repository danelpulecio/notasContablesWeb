package co.com.bbva.app.notas.contables.dao;

import co.com.bbva.app.notas.contables.dto.NotaContableRegistroLibre;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.Collection;

public class NotaContableRegistroLibreDAO extends CommonSeqDAO<NotaContableRegistroLibre> {

	private static String cs_COLUMNAS = "CODIGO, CODIGO_NOTA_CONTABLE, FECHA_CONTABLE, CUENTA_CONTABLE, NATURALEZA, COD_SUC_DESTINO, CODIGO_DIVISA, MONTO, REFERENCIA, TIPO_IDENTIFICACION, DIGITO_VERIFICACION, NOMBRE_COMPLETO, NUMERO_IDENTIFICACION, CONTRATO, NUMERO_ASIENTO, NUMERO_APUNTE";

	private static String cs_TABLA = "NC_NOTA_CONT_REGISTRO_LIBRE";

	private static String cs_PK = "CODIGO";

	private static String SQL_SELECT_BY_NOTA_CONTABLE_SENTENCE = "SELECT " + cs_COLUMNAS + " FROM " + cs_TABLA + " WHERE (CODIGO_NOTA_CONTABLE = ?) ORDER BY CODIGO";

	private static String SQL_SELECT_BY_NOTA_CONTABLE_AND_CODIGO_SENTENCE = "SELECT " + cs_COLUMNAS + " FROM " + cs_TABLA + " WHERE (CODIGO_NOTA_CONTABLE = ?) AND (CODIGO = ?)";

	private static String SQL_UPDATE_SENTENCE = "UPDATE " + cs_TABLA + " SET NUMERO_ASIENTO = ?, NUMERO_APUNTE = ? WHERE (CODIGO = ?)";

	private static String SQL_UPDATE_FECHA_SENTENCE = "UPDATE " + cs_TABLA + " SET FECHA_CONTABLE = ? WHERE (CODIGO = ?)";

	private static String SQL_DELETE_BY_NOTA_CONTABLE_SENTENCE = "DELETE FROM " + cs_TABLA + " WHERE (CODIGO_NOTA_CONTABLE = ?)";

	private final static String QUERY_PARTIDAS = "SELECT  \n" + // 
			" '0013'    \n" + // 
			" || LPAD(NVL(NC.CODIGO_SUCURSAL_ORIGEN,'0'), 4, '0') \n" + //  
			" || LPAD(NVL(NC.CODIGO_SUCURSAL_ORIGEN,'0'), 4, '0')   \n" + // 
			" || LPAD(NVL(NCC.COD_SUC_DESTINO,'0'), 4, '0')   \n" + // 
			" || '4' || LPAD(NVL(NCC.CODIGO,'0'), 9, '0')   \n" + // el 4 indica registros de contabilidad libre
			" || TO_CHAR(NCC.FECHA_CONTABLE, 'YYYYMMDD')   \n" + // 
			" || RPAD(NVL(NCC.CUENTA_CONTABLE,' '), 15, ' ') \n" + //  
			" || DECODE(NCC.NATURALEZA, 'D', '1','0') || DECODE(NCC.NATURALEZA, 'D', '0','1') \n" + //  
			" || DECODE(NCC.NATURALEZA, 'D', REPLACE(REPLACE(LTRIM(TO_CHAR(NVL(NCC.MONTO,'0'), '0000000000000D00')), ',', ''), '.', ''), '000000000000000') \n" + //  
			" || DECODE(NCC.NATURALEZA, 'D', '000000000000000', REPLACE(REPLACE(LTRIM(TO_CHAR(NVL(NCC.MONTO,'0'), '0000000000000D00')), ',', ''), '.', ''))  \n" + // 
			" || RPAD(NVL(NCC.CODIGO_DIVISA, ' '), 3, ' ')   \n" + // 
			" || LPAD(NVL(NC.CODIGO_CONCEPTO, '0'), 3, '0') \n" + // 
			" || DECODE(NVL(NCC.TIPO_IDENTIFICACION, '0'), '0', 0, NCC.TIPO_IDENTIFICACION) \n" + //  
			" || LPAD(NVL(NCC.NUMERO_IDENTIFICACION, '0'), 15, '0')  \n" + // 
			" || NVL(NCC.DIGITO_VERIFICACION,'0')  \n" + // 
			" || LPAD(NCC.REFERENCIA, 12, '0')  \n" + // 
			" || LPAD(NVL(NCC.CONTRATO, '0'), 18, '0')  \n" + // 
			" || LPAD(NVL(NCTR.CODIGO_TIPO_PERDIDA, '0'), 4, '0') \n" + //  
			" || LPAD(NVL(NCTR.CODIGO_CLASE_RIESGO, '0'), 3, '0')  \n" + // 
			" || '0'  \n" + // 
			" || LPAD(NVL(NCTR.CODIGO_PRODUCTO, '0'), 11, '0') \n" + //
			/** Incidencia COL396113I000838**/
			" || LPAD(NVL(TO_CHAR(NCTR.FECHA_EVENTO, 'YYYYMMDD'), '0'), 8, '0')\n" +
			" || LPAD(NVL(NCTR.CODIGO_PROCESO, '0'), 6, '0')  \n" + // 
			" || LPAD(NVL(NCTR.CODIGO_LINEA_OPER, '0'), 4, '0')  \n" + // 
			/**" || REPLACE(REPLACE(RPAD(NVL(NC.DESCRIPCION, '0'), 30, ' '), chr(13), ' '), chr(10), ' ')   \n" + **/
			" || TRANSLATE(REPLACE(REPLACE(RPAD(NVL(NC.DESCRIPCION, '0'), 30, ' '), chr(13), ' '), chr(10), ' '),'áéíóúàèìòùãõâêîôôäëïöüçÁÉÍÓÚÀÈÌÒÙÃÕÂÊÎÔÛÄËÏÖÜÇñÑ¿?°ª¡!·'||CHR(160), 'aeiouaeiouaoaeiooaeioucAEIOUAEIOUAOAEIOOAEIOUCnN        ')   \n" + //
			" || SUBSTR(NC.NUMERO_RADICACION, 5)  \n" + // 
			" FROM NC_NOTA_CONT_REGISTRO_LIBRE NCC  \n" + // 
			" LEFT JOIN NC_NOTA_CONTABLE NC ON NCC.CODIGO_NOTA_CONTABLE = NC.CODIGO \n" + //  
			" LEFT JOIN NC_NOTA_CONTABLE_TEMA_RIESGO NCTR ON NCTR.CODIGO_NOTA_CONTABLE = NC.CODIGO AND NCTR.CODIGO_TEMA = NCC.CODIGO \n" + //  
			" WHERE NC.CODIGO = ? ORDER BY NCC.CODIGO";

	public NotaContableRegistroLibreDAO() {
		super(cs_TABLA, cs_COLUMNAS, cs_PK, new NotaContableRegistroLibre());
	}

	@Override
	public void internalUpdate(Connection con, NotaContableRegistroLibre row) throws Exception {
		executeUpdate(con, SQL_UPDATE_SENTENCE, new Object[] { row.getNumeroAsiento(), row.getNumeroApunte(), row.getCodigo() });
	}

	public void deleteByNotaContable(Connection con, int codigoNotaContable, int codigoUsuario) throws Exception {
		executeUpdate(con, SQL_DELETE_BY_NOTA_CONTABLE_SENTENCE, codigoNotaContable);
		/**
		// BLOQUEO BASE DE DATOS
		String xmlDataOriginal = getXMLDataByNotaContable(con, codigoNotaContable);
		
		if (!xmlDataOriginal.isEmpty()) {
			int idAuditoria = addRegistroAuditoria(con, codigoUsuario, "Borrar productos del tema", Tema.class.getSimpleName(), "" + codigoNotaContable);
			addRegistroAuditoriaDetalle(con, idAuditoria, xmlDataOriginal, "");
		}
		**/
	}

	public Collection<NotaContableRegistroLibre> findByNotaContable(int codigoNotaContable) throws Exception {
		return findByGeneral(SQL_SELECT_BY_NOTA_CONTABLE_SENTENCE, codigoNotaContable);
	}

	public NotaContableRegistroLibre findByNotaContableAndCodigo(int codigoNotaContable, int codigo) throws Exception {
		return getByGeneral(SQL_SELECT_BY_NOTA_CONTABLE_AND_CODIGO_SENTENCE, new Object[] { codigoNotaContable, codigo });
	}

	public String getXMLDataByNotaContable(Connection con, int codigoNotaContable) throws Exception {
		return getXMLDataGeneral(con, SQL_SELECT_BY_NOTA_CONTABLE_SENTENCE, codigoNotaContable);
	}

	public Collection<String> getInfoPartidasArchAltamira(Integer notaContableCodigo) throws Exception {
		return findToStringByGeneral(QUERY_PARTIDAS, new Object[] { notaContableCodigo });
	}

	@Override
	protected Object[] getDataToAdd(Connection con, NotaContableRegistroLibre row) throws Exception {
		row.setCodigo(getMaxCode(con));
		return new Object[] { row.getCodigo(), row.getCodigoNotaContable(), row.getFechaContable(), row.getCuentaContable(), row.getNaturalezaCuentaContable(), row.getCodigoSucursalDestino(), row.getCodigoDivisa(), row.getMonto(),
				row.getReferencia(), row.getTipoIdentificacion(), row.getDigitoVerificacion(), row.getNombreCompleto(), row.getNumeroIdentificacion(), row.getContrato(), row.getNumeroAsiento(), row.getNumeroApunte() };
	}

	@Override
	public NotaContableRegistroLibre getResultSetToVO(ResultSet result) throws Exception {
		NotaContableRegistroLibre row = new NotaContableRegistroLibre();

		row.setCodigo(result.getInt(1));
		row.setCodigoNotaContable(result.getInt(2));
		row.setFechaContable(result.getDate(3));
		row.setCuentaContable(result.getString(4) != null ? result.getString(4) : "");
		row.setNaturalezaCuentaContable(result.getString(5) != null ? result.getString(5) : "");
		row.setCodigoSucursalDestino(result.getString(6) != null ? result.getString(6) : "");
		row.setCodigoDivisa(result.getString(7) != null ? result.getString(7) : "");
		row.setMonto(result.getDouble(8));
		row.setReferencia(result.getString(9) != null ? result.getString(9) : "");
		row.setTipoIdentificacion(result.getString(10) != null ? result.getString(10) : "");
		row.setDigitoVerificacion(result.getString(11) != null ? result.getString(11) : "");
		row.setNombreCompleto(result.getString(12) != null ? result.getString(12) : "");
		row.setNumeroIdentificacion(result.getString(13) != null ? result.getString(13) : "");
		row.setContrato(result.getString(14) != null ? result.getString(14) : "");
		row.setNumeroAsiento(result.getString(15) != null ? result.getString(15) : "");
		row.setNumeroApunte(result.getString(16) != null ? result.getString(16) : "");

		return row;
	}

	public void updateFecha(NotaContableRegistroLibre t, int codUsuario) throws Exception {
		update(t, codUsuario, SQL_UPDATE_FECHA_SENTENCE, new Object[] { t.getFechaContable(), t.getCodigo() });
	}
}