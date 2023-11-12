package co.com.bbva.app.notas.contables.dao;

import co.com.bbva.app.notas.contables.dto.NotaContableTotal;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.Collection;

public class NotaContableTotalDAO extends CommonSeqDAO<NotaContableTotal> {

	private static String cs_COLUMNAS = "CODIGO, CODIGO_NOTA_CONTABLE, CODIGO_DIVISA, TOTAL";

	private static String cs_TABLA = "NC_NOTA_CONTABLE_TOTAL";

	private static String cs_PK = "CODIGO";

	private static String SQL_SELECT_BY_NOTA_CONTABLE_SENTENCE = " SELECT " + cs_COLUMNAS + " FROM " + cs_TABLA + " WHERE (CODIGO_NOTA_CONTABLE = ?) ORDER BY CODIGO";

	private static String SQL_DELETE_BY_NOTA_CONTABLE_SENTENCE = " DELETE FROM " + cs_TABLA + " WHERE (CODIGO_NOTA_CONTABLE = ?)";

	public NotaContableTotalDAO() {
		super(cs_TABLA, cs_COLUMNAS, cs_PK, new NotaContableTotal());
	}

	public void deleteByNotaContable(Connection con, int codigoNotaContable, int codigoUsuario) throws Exception {
		executeUpdate(con, SQL_DELETE_BY_NOTA_CONTABLE_SENTENCE, codigoNotaContable);
		/**
		// BLOQUEO BASE DE DATOS
		String xmlDataOriginal = getXMLDataByNotaContable(con, codigoNotaContable);
		if (!xmlDataOriginal.isEmpty()) {
			int idAuditoria = addRegistroAuditoria(con, codigoUsuario, "Borrar totales de la nota contable", Tema.class.getSimpleName(), "" + codigoNotaContable);
			addRegistroAuditoriaDetalle(con, idAuditoria, xmlDataOriginal, "");
		}
		**/
	}

	public Collection<NotaContableTotal> findByNotaContable(int codigoNotaContable) throws Exception {
		return findByGeneral(SQL_SELECT_BY_NOTA_CONTABLE_SENTENCE, codigoNotaContable);
	}

	public String getXMLDataByNotaContable(Connection con, int codigoNotaContable) throws Exception {
		return getXMLDataGeneral(con, SQL_SELECT_BY_NOTA_CONTABLE_SENTENCE, codigoNotaContable);
	}

	@Override
	protected Object[] getDataToAdd(Connection con, NotaContableTotal row) throws Exception {
		row.setCodigo(getMaxCode(con));
		return new Object[] { row.getCodigo(), row.getCodigoNotaContable(), row.getCodigoDivisa(), row.getTotal() };
	}

	@Override
	public NotaContableTotal getResultSetToVO(ResultSet result) throws Exception {
		NotaContableTotal row = new NotaContableTotal();

		row.setCodigo(result.getInt(1));
		row.setCodigoNotaContable(result.getInt(2));
		row.setCodigoDivisa(result.getString(3));
		row.setTotal(result.getDouble(4));

		return row;
	}

	@Override
	protected void internalUpdate(@SuppressWarnings("unused") Connection con, @SuppressWarnings("unused") NotaContableTotal row) throws Exception {
		throw new Exception("Mtodo no implementado");
	}
}