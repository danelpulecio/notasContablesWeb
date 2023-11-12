package co.com.bbva.app.notas.contables.dao;

import co.com.bbva.app.notas.contables.dto.CuentaCOD;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.Collection;

public class CuentaCODDAO extends CommonSeqDAO<CuentaCOD> {

	private static String cs_COLUMNAS = "CODIGO, CUENTA_CONTABLE";

	private static String cs_TABLA = "NC_EXCEPCION_DIVISA";
	private static String cs_PK = "CODIGO";

	private static String SQL_SELECT_ALL_SENTENCE = "SELECT " + cs_COLUMNAS + " FROM " + cs_TABLA + " ORDER BY CUENTA_CONTABLE";

	private static String SQL_SELECT_BY_CUENTA_CONTABLE_SENTENCE = "SELECT " + cs_COLUMNAS + " FROM " + cs_TABLA + " WHERE (CUENTA_CONTABLE = ?)";

	private static String SQL_SELECT_BY_SENTENCE = "SELECT " + cs_COLUMNAS + " FROM " + cs_TABLA + " WHERE (CUENTA_CONTABLE LIKE ?) ORDER BY CUENTA_CONTABLE";

	private static int NUMERO_COLUMNAS_BUSQUEDA = 1;

	private static String SQL_UPDATE_SENTENCE = "UPDATE " + cs_TABLA + " SET CUENTA_CONTABLE = ? WHERE (CODIGO = ?)";

	public CuentaCODDAO() {
		super(cs_TABLA, cs_COLUMNAS, cs_PK, new CuentaCOD());
	}

	@Override
	public void internalUpdate(Connection con, CuentaCOD row) throws Exception {
		executeUpdate(con, SQL_UPDATE_SENTENCE, new Object[] { row.getCuentaContable(), row.getCodigo() });
	}

	public CuentaCOD findByCuentaContable(CuentaCOD row) throws Exception {
		return getByGeneral(SQL_SELECT_BY_CUENTA_CONTABLE_SENTENCE, row.getCuentaContable());
	}

	public Collection<CuentaCOD> findBy(String palabraClave) throws Exception {
		return findByKeyWord(SQL_SELECT_BY_SENTENCE, palabraClave, NUMERO_COLUMNAS_BUSQUEDA);
	}

	public Collection<CuentaCOD> findAll() throws Exception {
		return findByGeneral(SQL_SELECT_ALL_SENTENCE);
	}

	@Override
	protected Object[] getDataToAdd(Connection con, CuentaCOD row) throws Exception {
		row.setCodigo(getMaxCode(con));
		return new Object[] { row.getCodigo(), row.getCuentaContable() };
	}

	@Override
	public CuentaCOD getResultSetToVO(ResultSet result) throws Exception {
		CuentaCOD row = new CuentaCOD();

		row.setCodigo(result.getInt(1));
		row.setCuentaContable(result.getString(2));

		return row;
	}
}