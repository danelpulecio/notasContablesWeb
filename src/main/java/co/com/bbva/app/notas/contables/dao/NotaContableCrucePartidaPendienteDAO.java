package co.com.bbva.app.notas.contables.dao;

import co.com.bbva.app.notas.contables.dto.NotaContableCrucePartidaPendiente;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.Collection;

public class NotaContableCrucePartidaPendienteDAO extends CommonSeqDAO<NotaContableCrucePartidaPendiente> {

	private static String cs_COLUMNAS = "CODIGO, CODIGO_NOTA_CONTABLE, CUENTA, DESCRIPCION, SUCURSAL_DESTINO, NATURALEZA, REFERENCIA_CRUCE, DIVISA, IMPORTE, CONCEPTO, FECHA_CONTABLE, NUMERO_ASIENTO, NUMERO_APUNTE";

	private static String cs_TABLA = "NC_NOT_CON_CRUCE_PART_PEND";

	private static String cs_PK = "CODIGO";

	private static String SQL_SELECT_BY_NOTA_CONTABLE_SENTENCE = "SELECT " + cs_COLUMNAS + " FROM " + cs_TABLA + " WHERE (CODIGO_NOTA_CONTABLE = ?) ORDER BY CODIGO";

	private static String SQL_UPDATE_SENTENCE = "UPDATE " + cs_TABLA + " SET NUMERO_ASIENTO = ?, NUMERO_APUNTE = ? WHERE (CODIGO = ?)";

	private static String SQL_UPDATE_FECHA_SENTENCE = "UPDATE " + cs_TABLA + " SET FECHA_CONTABLE = ? WHERE (CODIGO = ?)";

	private static String SQL_DELETE_BY_NOTA_CONTABLE_SENTENCE = "DELETE FROM " + cs_TABLA + " WHERE (CODIGO_NOTA_CONTABLE = ?)";

	private final static String QUERY_PARTIDAS = "SELECT  \n" + // 
			"  '0013'   \n" + // 
			" || LPAD(NVL(NC.CODIGO_SUCURSAL_ORIGEN,'0'), 4, '0') \n" + //  
			" || LPAD(NVL(NC.CODIGO_SUCURSAL_ORIGEN,'0'), 4, '0')  \n" + // 
			" || LPAD(NVL(NCC.SUCURSAL_DESTINO,'0'), 4, '0')  \n" + // 
			" || '5' || LPAD(NVL(NCC.CODIGO,'0'), 9, '0')  \n" + // el 5 indica registros de cruce de referencia
			" || TO_CHAR(NCC.FECHA_CONTABLE, 'YYYYMMDD')  \n" + // 
			" || RPAD(NVL(NCC.CUENTA,' '), 15, ' ') \n" + // 
			" || DECODE(NCC.NATURALEZA, 'H', '1','0') || DECODE(NCC.NATURALEZA, 'H', '0','1') \n" + // 
			" || DECODE(NCC.NATURALEZA, 'H', REPLACE(REPLACE(LTRIM(TO_CHAR(NVL(NCC.IMPORTE,'0'), '0000000000000D00')), ',', ''), '.', ''), '000000000000000') \n" + // 
			" || DECODE(NCC.NATURALEZA, 'H', '000000000000000', REPLACE(REPLACE(LTRIM(TO_CHAR(NVL(NCC.IMPORTE,'0'), '0000000000000D00')), ',', ''), '.', '')) \n" + // 
			" || RPAD(NVL(NCC.DIVISA, ' '), 3, ' ')  \n" + // 
			" || LPAD(NVL(NC.CODIGO_CONCEPTO, '0'), 3, '0')  \n" + // 
			" || '3' \n" + // 
			" || '000000100100112' \n" + // 
			" || '9' \n" + // 
			" || LPAD(NVL(NCC.REFERENCIA_CRUCE, '0'), 12, '0') \n" + //  
			" || '000000000000000000' \n" + // 
			" || '0000' \n" + // 
			" || '000'  \n" + // 
			" || '0'  \n" + // 
			" || '00000000000' \n" + // 
			" || '00000000000000' \n" + // 
			" || '0000' \n" + // 
			" || RPAD('Cruce de Referencia', 30, ' ') \n" + //  
			" || SUBSTR(NC.NUMERO_RADICACION, 5)  \n" + //  
			" FROM NC_NOT_CON_CRUCE_PART_PEND NCC \n" + // 
			" LEFT JOIN NC_NOTA_CONTABLE NC ON NCC.CODIGO_NOTA_CONTABLE = NC.CODIGO \n" + //  
			" WHERE NC.CODIGO = ? ORDER BY NCC.CODIGO";

	public NotaContableCrucePartidaPendienteDAO() {
		super(cs_TABLA, cs_COLUMNAS, cs_PK, new NotaContableCrucePartidaPendiente());
	}

	@Override
	public void internalUpdate(Connection con, NotaContableCrucePartidaPendiente row) throws Exception {
		executeUpdate(con, SQL_UPDATE_SENTENCE, new Object[] { row.getNumeroAsiento(), row.getNumeroApunte(), row.getCodigo() });
	}

	public void deleteByNotaContable(int codigoNotaContable) throws Exception {
		executeUpdate(SQL_DELETE_BY_NOTA_CONTABLE_SENTENCE, codigoNotaContable);
	}

	public Collection<NotaContableCrucePartidaPendiente> findByNotaContable(Connection con, int codigoNotaContable) throws Exception {
		return findByGeneral(con, SQL_SELECT_BY_NOTA_CONTABLE_SENTENCE, codigoNotaContable);
	}

	public Collection<NotaContableCrucePartidaPendiente> findByNotaContable(int codigoNotaContable) throws Exception {
		return findByGeneral(SQL_SELECT_BY_NOTA_CONTABLE_SENTENCE, codigoNotaContable);
	}

	public String getXMLDataByNotaContable(Connection con, int codigoNotaContable) throws Exception {
		return getXMLDataGeneral(con, SQL_SELECT_BY_NOTA_CONTABLE_SENTENCE, codigoNotaContable);
	}

	public Collection<String> getInfoPartidasArchAltamira(Integer notaContableCodigo) throws Exception {
		return findToStringByGeneral(QUERY_PARTIDAS, new Object[] { notaContableCodigo });
	}

	@Override
	protected Object[] getDataToAdd(Connection con, NotaContableCrucePartidaPendiente row) throws Exception {
		row.setCodigo(getMaxCode(con));
		return new Object[] { row.getCodigo(), row.getCodigoNotaContable(), row.getCuentaContable(), row.getDescripcionPartidaPendiente(), row.getCodigoSucursalDestino(), row.getNaturaleza(), row.getReferenciaCruce(), row.getDivisa(),
				row.getImporte(), row.getConcepto(), row.getFechaContable(), row.getNumeroAsiento(), row.getNumeroApunte() };
	}

	@Override
	public NotaContableCrucePartidaPendiente getResultSetToVO(ResultSet result) throws Exception {
		NotaContableCrucePartidaPendiente row = new NotaContableCrucePartidaPendiente();

		row.setCodigo(result.getInt(1));
		row.setCodigoNotaContable(result.getInt(2));
		row.setCuentaContable(result.getString(3) != null ? result.getString(3) : "");
		row.setDescripcionPartidaPendiente(result.getString(4) != null ? result.getString(4) : "");
		row.setCodigoSucursalDestino(result.getString(5) != null ? result.getString(5) : "");
		row.setNaturaleza(result.getString(6) != null ? result.getString(6) : "");
		row.setReferenciaCruce(result.getString(7) != null ? result.getString(7) : "");
		row.setDivisa(result.getString(8) != null ? result.getString(8) : "");
		row.setImporte(result.getDouble(9));
		row.setConcepto(result.getString(10) != null ? result.getString(10) : "");
		row.setFechaContable(result.getTimestamp(11));
		row.setNumeroAsiento(result.getString(12) != null ? result.getString(12) : "");
		row.setNumeroApunte(result.getString(13) != null ? result.getString(13) : "");

		return row;
	}

	public void updateFecha(NotaContableCrucePartidaPendiente t, int codUsuario) throws Exception {
		update(t, codUsuario, SQL_UPDATE_FECHA_SENTENCE, new Object[] { t.getFechaContable(), t.getCodigo() });
	}

}