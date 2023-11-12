package co.com.bbva.app.notas.contables.dao;

import co.com.bbva.app.notas.contables.dto.TemaImpuesto;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.Collection;

public class TemaImpuestoDAO extends CommonSeqDAO<TemaImpuesto> {

	private static String cs_COLUMNAS = "CODIGO, CODIGO_TEMA, CODIGO_IMPUESTO";

	private static String cs_TABLA = "NC_TEMA_IMPUESTO";
	private static String cs_PK = "CODIGO";

	private static String SQL_SELECT_BY_TEMA_SENTENCE = "SELECT " + cs_COLUMNAS + " FROM " + cs_TABLA + " WHERE (CODIGO_TEMA = ?) ORDER BY CODIGO_IMPUESTO";

	private static String SQL_DELETE_BY_TEMA_SENTENCE = "DELETE FROM " + cs_TABLA + " WHERE (CODIGO_TEMA = ?)";

	public TemaImpuestoDAO() {
		super(cs_TABLA, cs_COLUMNAS, cs_PK, new TemaImpuesto());
	}

	public void deleteByTema(Connection con, int codigoTema, int codigoUsuario) throws Exception {
		executeUpdate(con, SQL_DELETE_BY_TEMA_SENTENCE, codigoTema);
		/**
		// BLOQUEO BASE DE DATOS
		String xmlDataOriginal = getXMLDataByTema(con, codigoTema);
		if (!xmlDataOriginal.isEmpty()) {
			int idAuditoria = addRegistroAuditoria(con, codigoUsuario, "Borrar impuestos del tema", Tema.class.getSimpleName(), "" + codigoTema);
			addRegistroAuditoriaDetalle(con, idAuditoria, xmlDataOriginal, "");
		}
		**/
	}

	public Collection<TemaImpuesto> findByTema(int codigoTema) throws Exception {
		return findByGeneral(SQL_SELECT_BY_TEMA_SENTENCE, codigoTema);
	}

	public String getXMLDataByTema(Connection con, int codigoTema) throws Exception {
		return getXMLDataGeneral(con, SQL_SELECT_BY_TEMA_SENTENCE, codigoTema);
	}

	@Override
	protected Object[] getDataToAdd(Connection con, TemaImpuesto row) throws Exception {
		row.setCodigo(getMaxCode(con));
		return new Object[] { row.getCodigo(), row.getCodigoTema(), row.getCodigoImpuesto() };
	}

	@Override
	public TemaImpuesto getResultSetToVO(ResultSet result) throws Exception {
		TemaImpuesto row = new TemaImpuesto();

		row.setCodigo(result.getInt(1));
		row.setCodigoTema(result.getInt(2));
		row.setCodigoImpuesto(result.getInt(3));

		return row;
	}

	@Override
	protected void internalUpdate(@SuppressWarnings("unused") Connection con, @SuppressWarnings("unused") TemaImpuesto row) throws Exception {
		throw new Exception("Mtodo no implementado");
	}
}