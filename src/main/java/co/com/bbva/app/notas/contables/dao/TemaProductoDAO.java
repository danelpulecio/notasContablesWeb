package co.com.bbva.app.notas.contables.dao;

import co.com.bbva.app.notas.contables.dto.TemaProducto;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.Collection;

public class TemaProductoDAO extends CommonSeqDAO<TemaProducto> {

	private static String cs_COLUMNAS = "CODIGO, CODIGO_TEMA, CODIGO_PRODUCTO";

	private static String cs_TABLA = "NC_TEMA_PRODUCTO";

	private static String cs_PK = "CODIGO";

	private static String SQL_SELECT_BY_TEMA_SENTENCE = "SELECT " + cs_COLUMNAS + " FROM " + cs_TABLA + " WHERE (CODIGO_TEMA = ?) ORDER BY CODIGO_PRODUCTO";

	private static String SQL_DELETE_BY_TEMA_SENTENCE = "DELETE FROM " + cs_TABLA + " WHERE (CODIGO_TEMA = ?)";

	public TemaProductoDAO() {
		super(cs_TABLA, cs_COLUMNAS, cs_PK, new TemaProducto());
	}

	public void deleteByTema(Connection con, int codigoTema, int codigoUsuario) throws Exception {
		executeUpdate(con, SQL_DELETE_BY_TEMA_SENTENCE, codigoTema);
		/**
		// BLOQUEO BASE DE DATOS
		String xmlDataOriginal = getXMLDataByTema(con, codigoTema);
		if (!xmlDataOriginal.isEmpty()) {
			int idAuditoria = addRegistroAuditoria(con, codigoUsuario, "Borrar productos del tema", Tema.class.getSimpleName(), "" + codigoTema);
			addRegistroAuditoriaDetalle(con, idAuditoria, xmlDataOriginal, "");
		}
		**/
	}

	public Collection<TemaProducto> findByTema(int codigoTema) throws Exception {
		return findByGeneral(SQL_SELECT_BY_TEMA_SENTENCE, codigoTema);
	}

	public String getXMLDataByTema(Connection con, int codigoTema) throws Exception {
		return getXMLDataGeneral(con, SQL_SELECT_BY_TEMA_SENTENCE, codigoTema);
	}

	@Override
	protected Object[] getDataToAdd(Connection con, TemaProducto row) throws Exception {
		row.setCodigo(getMaxCode(con));
		return new Object[] { row.getCodigo(), row.getCodigoTema(), row.getCodigoProducto() };
	}

	@Override
	public TemaProducto getResultSetToVO(ResultSet result) throws Exception {
		TemaProducto row = new TemaProducto();

		row.setCodigo(result.getInt(1));
		row.setCodigoTema(result.getInt(2));
		row.setCodigoProducto(result.getString(3));

		return row;
	}

	@Override
	protected void internalUpdate(@SuppressWarnings("unused") Connection con, @SuppressWarnings("unused") TemaProducto row) throws Exception {
		throw new Exception("Mtodo no implementado");
	}
}