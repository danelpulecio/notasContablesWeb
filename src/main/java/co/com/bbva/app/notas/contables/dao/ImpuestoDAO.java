package co.com.bbva.app.notas.contables.dao;

import co.com.bbva.app.notas.contables.dto.Impuesto;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.Collection;

public class ImpuestoDAO extends CommonSeqDAO<Impuesto> {

	private static String cs_COLUMNAS = "CODIGO, NOMBRE, VALOR, PARTIDA_CONTABLE, CONTRAPARTIDA_CONTABLE, ESTADO";

	private static String cs_TABLA = "NC_IMPUESTO";

	private static String cs_PK = "CODIGO";

	private static String SQL_SELECT_ALL_SENTENCE = "SELECT " + cs_COLUMNAS + " FROM " + cs_TABLA + " ORDER BY NOMBRE";

	private static String SQL_SELECT_BY_ESTADO_SENTENCE = "SELECT " + cs_COLUMNAS + " FROM " + cs_TABLA + " WHERE (ESTADO = ?) ORDER BY NOMBRE";

	private static String SQL_SELECT_BY_SENTENCE = "SELECT " + cs_COLUMNAS + " FROM " + cs_TABLA + " WHERE (NOMBRE LIKE ?) OR (PARTIDA_CONTABLE LIKE ?) OR (CONTRAPARTIDA_CONTABLE LIKE ?) ORDER BY NOMBRE";

	private static int NUMERO_COLUMNAS_BUSQUEDA = 3;

	private static String SQL_UPDATE_SENTENCE = "UPDATE " + cs_TABLA + " SET NOMBRE = ?, VALOR = ?, PARTIDA_CONTABLE = ?, CONTRAPARTIDA_CONTABLE = ?, ESTADO = ? WHERE (CODIGO = ?)";

	public ImpuestoDAO() {
		super(cs_TABLA, cs_COLUMNAS, cs_PK, new Impuesto());
	}

	@Override
	public void internalUpdate(Connection con, Impuesto row) throws Exception {
		executeUpdate(con, SQL_UPDATE_SENTENCE, new Object[] { row.getNombre(), row.getValor(), row.getPartidaContable(), row.getContraPartidaContable(), row.getEstado(), row.getCodigo() });
	}

	public Collection<Impuesto> findAll() throws Exception {
		return findByGeneral(SQL_SELECT_ALL_SENTENCE);
	}

	public Collection<Impuesto> findByEstado(Impuesto row) throws Exception {
		return findByGeneral(SQL_SELECT_BY_ESTADO_SENTENCE, row.getEstado());
	}

	public Collection<Impuesto> findBy(String palabraClave) throws Exception {
		return findByKeyWord(SQL_SELECT_BY_SENTENCE, palabraClave, NUMERO_COLUMNAS_BUSQUEDA);
	}

	@Override
	protected Object[] getDataToAdd(Connection con, Impuesto row) throws Exception {
		row.setCodigo(getMaxCode(con));
		return new Object[] { row.getCodigo(), row.getNombre(), row.getValor(), row.getPartidaContable(), row.getContraPartidaContable(), row.getEstado() };
	}

	@Override
	public Impuesto getResultSetToVO(ResultSet result) throws Exception {
		Impuesto row = new Impuesto();

		row.setCodigo(result.getInt(1));
		row.setNombre(result.getString(2));
		row.setValor(result.getDouble(3));
		row.setPartidaContable(result.getString(4));
		row.setContraPartidaContable(result.getString(5));
		row.setEstado(result.getString(6));

		return row;
	}
}