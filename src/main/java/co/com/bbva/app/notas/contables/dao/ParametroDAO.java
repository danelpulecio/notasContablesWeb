package co.com.bbva.app.notas.contables.dao;

import co.com.bbva.app.notas.contables.dto.Parametro;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.Collection;

public class ParametroDAO extends CommonSeqDAO<Parametro> {

	private static final String cs_PK = "NOMBRE";

	private static final String cs_COLUMNAS = "NOMBRE, VALOR";

	private static final String cs_TABLA = "NC_PARAMETRO";

	private static final String sql_SELECT_ALL_SENTENCE = "SELECT " + cs_COLUMNAS + " FROM " + cs_TABLA;

	private static String SQL_UPDATE_SENTENCE = "UPDATE " + cs_TABLA + " SET VALOR = ? WHERE NOMBRE = ?";

	public ParametroDAO() {
		super(cs_TABLA, cs_COLUMNAS, cs_PK, new Parametro());
	}

	@Override
	protected Object[] getDataToAdd(@SuppressWarnings("unused") Connection con, Parametro row) throws Exception {
		return new Object[] { row.getNombre(), row.getValor() };
	}

	@Override
	public Parametro getResultSetToVO(ResultSet result) throws Exception {
		Parametro row = new Parametro();

		row.setNombre(result.getString(1));
		row.setValor(result.getInt(2));

		return row;
	}

	public Collection<Parametro> findAll() throws Exception {
		return findByGeneral(sql_SELECT_ALL_SENTENCE);
	}

	@Override
	protected void internalUpdate(Connection con, Parametro row) throws Exception {
		executeUpdate(con, SQL_UPDATE_SENTENCE, new Object[] { row.getValor(), row.getNombre() });
	}
}