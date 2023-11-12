package co.com.bbva.app.notas.contables.dao;

import co.com.bbva.app.notas.contables.dto.TemaAutorizacion;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.Collection;

public class TemaAutorizacionDAO extends CommonSeqDAO<TemaAutorizacion> {

	private static String cs_COLUMNAS = "CODIGO, NOMBRE, ESTADO";

	private static String cs_TABLA = "NC_TEMA_AUTORIZACION";

	private static String cs_PK = "CODIGO";

	private static String SQL_SELECT_ALL_SENTENCE = "SELECT " + cs_COLUMNAS + " FROM " + cs_TABLA + " ORDER BY NOMBRE";

	private static String SQL_SELECT_BY_ESTADO_SENTENCE = "SELECT " + cs_COLUMNAS + " FROM " + cs_TABLA + " WHERE (ESTADO = ?) ORDER BY NOMBRE";

	private static String SQL_SELECT_BY_SENTENCE = "SELECT " + cs_COLUMNAS + " FROM " + cs_TABLA + " WHERE (NOMBRE LIKE ?) ORDER BY NOMBRE";

	private static int NUMERO_COLUMNAS_BUSQUEDA = 1;

	private static String SQL_UPDATE_SENTENCE = "UPDATE " + cs_TABLA + " SET NOMBRE = ?, ESTADO = ? WHERE (CODIGO = ?)";

	public TemaAutorizacionDAO() {
		super(cs_TABLA, cs_COLUMNAS, cs_PK, new TemaAutorizacion());
	}

	@Override
	public void internalUpdate(Connection con, TemaAutorizacion row) throws Exception {
		executeUpdate(con, SQL_UPDATE_SENTENCE, new Object[] { row.getNombre(), row.getEstado(), row.getCodigo().intValue() });
	}

	public Collection<TemaAutorizacion> findAll() throws Exception {
		return findByGeneral(SQL_SELECT_ALL_SENTENCE);
	}

	public Collection<TemaAutorizacion> findByEstado(TemaAutorizacion row) throws Exception {
		return findByGeneral(SQL_SELECT_BY_ESTADO_SENTENCE, row.getEstado());
	}

	public Collection<TemaAutorizacion> findBy(String palabraClave) throws Exception {
		return findByKeyWord(SQL_SELECT_BY_SENTENCE, palabraClave, NUMERO_COLUMNAS_BUSQUEDA);
	}

	@Override
	protected Object[] getDataToAdd(Connection con, TemaAutorizacion row) throws Exception {
		row.setCodigo(getMaxCode(con));
		return new Object[] { row.getCodigo().intValue(), row.getNombre(), row.getEstado() };
	}

	@Override
	public TemaAutorizacion getResultSetToVO(ResultSet result) throws Exception {
		TemaAutorizacion row = new TemaAutorizacion();

		row.setCodigo(result.getInt(1));
		row.setNombre(result.getString(2));
		row.setEstado(result.getString(3));

		return row;
	}
}