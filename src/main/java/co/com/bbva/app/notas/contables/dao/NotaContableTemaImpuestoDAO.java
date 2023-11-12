package co.com.bbva.app.notas.contables.dao;

import co.com.bbva.app.notas.contables.dto.NotaContableTemaImpuesto;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.Collection;

public class NotaContableTemaImpuestoDAO extends CommonSeqDAO<NotaContableTemaImpuesto> {

	private static String cs_COLUMNAS = "CODIGO, CODIGO_NOTA_CONTABLE, CODIGO_TEMA, CODIGO_IMPUESTO, BASE, CALCULADO, EXONERA, NUMERO_ASIENTO, NUMERO_APUNTE";

	private static String cs_TABLA = "NC_NOTA_CONTABLE_TEMA_IMPUESTO";

	private static String cs_PK = "CODIGO";

	private static String SQL_SELECT_BY_TEMA_NOTA_CONTABLE_SENTENCE = "SELECT " + cs_COLUMNAS + " FROM " + cs_TABLA + " WHERE (CODIGO_TEMA = ?) ORDER BY CODIGO";

	private static String SQL_SELECT_BY_NOTA_CONTABLE_SENTENCE = "SELECT " + cs_COLUMNAS + " FROM " + cs_TABLA + " WHERE (CODIGO_NOTA_CONTABLE = ?) ORDER BY CODIGO";

	private static String SQL_SELECT_BY_NOTA_CONTABLE_AND_TEMA_NOTA_CONTABLE_AND_IMPUESTO_SENTENCE = "SELECT " + cs_COLUMNAS + " FROM " + cs_TABLA + " WHERE (CODIGO_NOTA_CONTABLE = ?) AND (CODIGO_TEMA = ?) AND (CODIGO_IMPUESTO = ?) ORDER BY CODIGO";

	private static String SQL_UPDATE_SENTENCE = "UPDATE " + cs_TABLA + " SET NUMERO_ASIENTO = ?, NUMERO_APUNTE = ? WHERE (CODIGO = ?)";

	private static String SQL_DELETE_BY_CODIGO_TEMA_SENTENCE = "DELETE FROM " + cs_TABLA + " WHERE (CODIGO_TEMA = ?)";

	public NotaContableTemaImpuestoDAO() {
		super(cs_TABLA, cs_COLUMNAS, cs_PK, new NotaContableTemaImpuesto());
		sql_SELECT_SEQUENCE_SENTENCE = "SELECT SEQ_NOTA_CONT_TEMA_IMPUESTO.nextval FROM DUAL";
	}

	@Override
	public void internalUpdate(Connection con, NotaContableTemaImpuesto row) throws Exception {
		executeUpdate(con, SQL_UPDATE_SENTENCE, new Object[] { row.getNumeroAsiento(), row.getNumeroApunte(), row.getCodigo() });
	}

	public void deleteByTemaNotaContable(Connection con, int codigoTemaNotaContable, int codigoUsuario) throws Exception {
		executeUpdate(con, SQL_DELETE_BY_CODIGO_TEMA_SENTENCE, codigoTemaNotaContable);
		/**
		// BLOQUEO BASE DE DATOS
		String xmlDataOriginal = getXMLDataByTemaNotaContable(con, codigoTemaNotaContable);
		if (!xmlDataOriginal.isEmpty()) {
			//int idAuditoria = addRegistroAuditoria(con, codigoUsuario, "Borrar impuestos del tema", Tema.class.getSimpleName(), "" + codigoTemaNotaContable);
			//addRegistroAuditoriaDetalle(con, idAuditoria, xmlDataOriginal, "");
		}
		**/
	}

	public Collection<NotaContableTemaImpuesto> findByTemaNotaContable(int codigoTemaNotaContable) throws Exception {
		return findByGeneral(SQL_SELECT_BY_TEMA_NOTA_CONTABLE_SENTENCE, codigoTemaNotaContable);
	}

	public NotaContableTemaImpuesto findByNotaContableAndTemaNotaContableAndImpuesto(int codigoNotaContable, int codigoTemaNotaContable, int codigoImpuesto) throws Exception {
		return getByGeneral(SQL_SELECT_BY_NOTA_CONTABLE_AND_TEMA_NOTA_CONTABLE_AND_IMPUESTO_SENTENCE, new Object[] { codigoNotaContable, codigoTemaNotaContable, codigoImpuesto });
	}

	public String getXMLDataByTemaNotaContable(Connection con, int codigoTemaNotaContable) throws Exception {
		return getXMLDataGeneral(con, SQL_SELECT_BY_TEMA_NOTA_CONTABLE_SENTENCE, codigoTemaNotaContable);
	}

	@Override
	protected Object[] getDataToAdd(Connection con, NotaContableTemaImpuesto row) throws Exception {
		row.setCodigo(getMaxCode(con, sql_SELECT_SEQUENCE_SENTENCE));
		return new Object[] { row.getCodigo(), row.getCodigoNotaContable(), row.getCodigoTemaNotaContable(), row.getCodigoImpuesto(), row.getBase(), row.getCalculado(), row.getExonera(), row.getNumeroAsiento(), row.getNumeroApunte() };
	}

	@Override
	public NotaContableTemaImpuesto getResultSetToVO(ResultSet result) throws Exception {
		NotaContableTemaImpuesto row = new NotaContableTemaImpuesto();

		row.setCodigo(result.getInt(1));
		row.setCodigoNotaContable(result.getInt(2));
		row.setCodigoTemaNotaContable(result.getInt(3));
		row.setCodigoImpuesto(result.getInt(4));
		row.setBase(result.getDouble(5));
		row.setCalculado(result.getDouble(6));
		row.setExonera(result.getString(7));
		row.setNumeroAsiento(result.getString(8) != null ? result.getString(8) : "");
		row.setNumeroApunte(result.getString(9) != null ? result.getString(9) : "");

		return row;
	}

	public Collection<NotaContableTemaImpuesto> findByNotaContable(int codigoNota) throws Exception {
		return findByGeneral(SQL_SELECT_BY_NOTA_CONTABLE_SENTENCE, codigoNota);
	}

	// public void updateAsientoContra(NotaContableTemaImpuesto registro) throws Exception {
	// throw new Exception("updateAsientoContra: no implementado an");
	// }
}