package co.com.bbva.app.notas.contables.dao;

import co.com.bbva.app.notas.contables.dto.TipoEvento;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.Collection;

public class TipoEventoDAO extends CommonSeqDAO<TipoEvento> {

	private static String cs_COLUMNAS = "CODIGO, NOMBRE, ESTADO";

	private static String cs_TABLA = "NC_TIPO_EVENTO";

	private static String cs_PK = "CODIGO";

	private static String SQL_SELECT_ALL_SENTENCE = "SELECT " + cs_COLUMNAS + " FROM " + cs_TABLA + " ORDER BY NOMBRE";

	private static String SQL_SELECT_BY_ESTADO_SENTENCE = "SELECT " + cs_COLUMNAS + " FROM " + cs_TABLA + " WHERE (ESTADO = ?) ORDER BY NOMBRE";

	private static String SQL_SELECT_BY_SENTENCE = "SELECT " + cs_COLUMNAS + " FROM " + cs_TABLA + " WHERE (NOMBRE LIKE ?) ORDER BY NOMBRE";

	private static int NUMERO_COLUMNAS_BUSQUEDA = 1;

	private static String SQL_UPDATE_SENTENCE = "UPDATE " + cs_TABLA + " SET NOMBRE = ?, ESTADO = ? WHERE (CODIGO = ?)";

	public TipoEventoDAO() {
		super(cs_TABLA, cs_COLUMNAS, cs_PK, new TipoEvento());
	}

	@Override
	public void internalUpdate(Connection con, TipoEvento row) throws Exception {
		executeUpdate(con, SQL_UPDATE_SENTENCE, new Object[] { row.getNombre(), row.getEstado(), row.getCodigo().intValue() });
	}

	public Collection<TipoEvento> findAll() throws Exception {
		return findByGeneral(SQL_SELECT_ALL_SENTENCE);
	}

	public Collection<TipoEvento> findByEstado(TipoEvento row) throws Exception {
		return findByGeneral(SQL_SELECT_BY_ESTADO_SENTENCE, row.getEstado());
	}

	public Collection<TipoEvento> findBy(String palabraClave) throws Exception {
		return findByKeyWord(SQL_SELECT_BY_SENTENCE, palabraClave, NUMERO_COLUMNAS_BUSQUEDA);
	}

	@Override
	protected Object[] getDataToAdd(Connection con, TipoEvento row) throws Exception {
		row.setCodigo(getMaxCode(con));
		return new Object[] { row.getCodigo().intValue(), row.getNombre(), row.getEstado() };
	}

	@Override
	public TipoEvento getResultSetToVO(ResultSet result) throws Exception {
		TipoEvento row = new TipoEvento();

		row.setCodigo(result.getInt(1));
		row.setNombre(result.getString(2));
		row.setEstado(result.getString(3));

		return row;
	}
}