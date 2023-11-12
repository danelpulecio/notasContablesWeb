package co.com.bbva.app.notas.contables.dao;

import co.com.bbva.app.notas.contables.dto.Rol;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.Collection;

public class RolDAO extends CommonSeqDAO<Rol> {

	private static String cs_COLUMNAS = "CODIGO, NOMBRE, ESTADO";

	private static String cs_TABLA = "NC_ROL";

	private static String cs_PK = "CODIGO";

	private static String SQL_SELECT_ALL_SENTENCE = "SELECT " + cs_COLUMNAS + " FROM " + cs_TABLA + " ORDER BY NOMBRE";

	private static String SQL_SELECT_BY_ESTADO_SENTENCE = "SELECT " + cs_COLUMNAS + " FROM " + cs_TABLA + " WHERE (ESTADO = ?) ORDER BY NOMBRE";

	private static String SQL_SELECT_BY_SENTENCE = "SELECT " + cs_COLUMNAS + " FROM " + cs_TABLA + " WHERE (NOMBRE LIKE ?) ORDER BY NOMBRE";

	private static int NUMERO_COLUMNAS_BUSQUEDA = 1;

	private static String SQL_UPDATE_SENTENCE = "UPDATE " + cs_TABLA + " SET NOMBRE = ?, ESTADO = ? WHERE (CODIGO = ?)";

	public RolDAO() {
		super(cs_TABLA, cs_COLUMNAS, cs_PK, new Rol());
	}

	@Override
	public void internalUpdate(Connection con, Rol row) throws Exception {
		executeUpdate(con, SQL_UPDATE_SENTENCE, new Object[] { row.getNombre(), row.getEstado(), row.getCodigo() });
	}

	public Collection<Rol> findAll() throws Exception {
		return findByGeneral(SQL_SELECT_ALL_SENTENCE);
	}

	public Collection<Rol> findByEstado(Rol row) throws Exception {
		return findByGeneral(SQL_SELECT_BY_ESTADO_SENTENCE, row.getEstado());
	}

	public Collection<Rol> findBy(String palabraClave) throws Exception {
		return findByKeyWord(SQL_SELECT_BY_SENTENCE, palabraClave, NUMERO_COLUMNAS_BUSQUEDA);
	}

	@Override
	protected Object[] getDataToAdd(Connection con, Rol row) throws Exception {
		row.setCodigo(getMaxCode(con));
		return new Object[] { row.getCodigo(), row.getNombre(), row.getEstado() };
	}

	@Override
	public Rol getResultSetToVO(ResultSet result) throws Exception {
		Rol row = new Rol();

		row.setCodigo(result.getInt(1));
		row.setNombre(result.getString(2));
		row.setEstado(result.getString(3));

		return row;
	}
}