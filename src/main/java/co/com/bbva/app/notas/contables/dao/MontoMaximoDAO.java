package co.com.bbva.app.notas.contables.dao;

import co.com.bbva.app.notas.contables.dto.MontoMaximo;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.Collection;

public class MontoMaximoDAO extends CommonSeqDAO<MontoMaximo> {

	private static String cs_COLUMNAS = "CODIGO, NOMBRE, DIVISA, MONTO_MAXIMO, MONTO_MAX_ALERTA, ESTADO";

	private static String cs_TABLA = "NC_MONTO_MAXIMO";

	private static String cs_PK = "CODIGO";

	private static String SQL_SELECT_ALL_SENTENCE = "SELECT " + cs_COLUMNAS + " FROM " + cs_TABLA + " ORDER BY NOMBRE";

	private static String SQL_SELECT_BY_ESTADO_SENTENCE = "SELECT " + cs_COLUMNAS + " FROM " + cs_TABLA + " WHERE (ESTADO = ?) ORDER BY NOMBRE";

	private static String SQL_UPDATE_SENTENCE = "UPDATE " + cs_TABLA + " SET NOMBRE = ?, DIVISA = ?, MONTO_MAXIMO = ?, MONTO_MAX_ALERTA = ?, ESTADO = ? WHERE (CODIGO = ?)";

	public MontoMaximoDAO() {
		super(cs_TABLA, cs_COLUMNAS, cs_PK, new MontoMaximo());
	}

	@Override
	public void internalUpdate(Connection con, MontoMaximo row) throws Exception {
		executeUpdate(con, SQL_UPDATE_SENTENCE, new Object[] { row.getNombre(), row.getDivisa(), row.getMontoMaximo(), row.getMontoMaximoAlerta(), row.getEstado(), row.getCodigo() });
	}

	public Collection<MontoMaximo> findAll() throws Exception {
		return findByGeneral(SQL_SELECT_ALL_SENTENCE);
	}

	public Collection<MontoMaximo> findByEstado(MontoMaximo row) throws Exception {
		return findByGeneral(SQL_SELECT_BY_ESTADO_SENTENCE, row.getEstado());
	}

	@Override
	protected Object[] getDataToAdd(Connection con, MontoMaximo row) throws Exception {
		row.setCodigo(getMaxCode(con));
		return new Object[] { row.getCodigo(), row.getNombre(), row.getDivisa(), row.getMontoMaximo(), row.getMontoMaximoAlerta(), row.getEstado() };
	}

	@Override
	public MontoMaximo getResultSetToVO(ResultSet result) throws Exception {
		MontoMaximo row = new MontoMaximo();

		row.setCodigo(result.getInt(1));
		row.setNombre(result.getString(2));
		row.setDivisa(result.getString(3));
		row.setMontoMaximo(result.getDouble(4));
		row.setMontoMaximoAlerta(result.getDouble(5));
		row.setEstado(result.getString(6));
		return row;
	}
}