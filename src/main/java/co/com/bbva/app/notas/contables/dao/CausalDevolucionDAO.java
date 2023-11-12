package co.com.bbva.app.notas.contables.dao;

import co.com.bbva.app.notas.contables.dto.CausalDevolucion;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.Collection;

public class CausalDevolucionDAO extends CommonSeqDAO<CausalDevolucion> {

	private static String cs_COLUMNAS = "CODIGO, NOMBRE, ESTADO";

	private static String cs_TABLA = "NC_CAUSAL_DEVOLUCION";

	private static String cs_PK = "CODIGO";

	private static String SQL_SELECT_ALL_SENTENCE = "SELECT " + cs_COLUMNAS + " FROM " + cs_TABLA + " ORDER BY NOMBRE";

	private static String SQL_SELECT_BY_ESTADO_SENTENCE = "SELECT " + cs_COLUMNAS + " FROM " + cs_TABLA + " WHERE (ESTADO = ?) ORDER BY NOMBRE";

	private static String SQL_SELECT_BY_SENTENCE = "SELECT " + cs_COLUMNAS + " FROM " + cs_TABLA + " WHERE (NOMBRE LIKE ?) ORDER BY NOMBRE";

	private static int NUMERO_COLUMNAS_BUSQUEDA = 1;

	private static String SQL_UPDATE_SENTENCE = "UPDATE " + cs_TABLA + " SET NOMBRE = ?, ESTADO = ? WHERE (CODIGO = ?)";

	public CausalDevolucionDAO() {
		super(cs_TABLA, cs_COLUMNAS, cs_PK, new CausalDevolucion());
	}

	@Override
	public void internalUpdate(Connection con, CausalDevolucion row) throws Exception {
		executeUpdate(con, SQL_UPDATE_SENTENCE, new Object[] { row.getNombre(), row.getEstado(), row.getCodigo().intValue() });
	}

	public Collection<CausalDevolucion> findAll() throws Exception {
		return findByGeneral(SQL_SELECT_ALL_SENTENCE);
	}

	public Collection<CausalDevolucion> findByEstado(CausalDevolucion row) throws Exception {
		return findByGeneral(SQL_SELECT_BY_ESTADO_SENTENCE, row.getEstado());
	}

	public Collection<CausalDevolucion> findBy(String palabraClave) throws Exception {
		return findByKeyWord(SQL_SELECT_BY_SENTENCE, palabraClave, NUMERO_COLUMNAS_BUSQUEDA);
	}

	@Override
	protected Object[] getDataToAdd(Connection con, CausalDevolucion row) throws Exception {
		row.setCodigo(getMaxCode(con));
		return new Object[] { row.getCodigo().intValue(), row.getNombre(), row.getEstado() };
	}

	@Override
	public CausalDevolucion getResultSetToVO(ResultSet result) throws Exception {
		CausalDevolucion row = new CausalDevolucion();

		row.setCodigo(result.getInt(1));
		row.setNombre(result.getString(2));
		row.setEstado(result.getString(3));

		return row;
	}
}