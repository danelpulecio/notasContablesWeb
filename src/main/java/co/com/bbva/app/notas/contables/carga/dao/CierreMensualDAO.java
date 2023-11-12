/*
	Nombre DTO: CierreMensual
 */

package co.com.bbva.app.notas.contables.carga.dao;

import co.com.bbva.app.notas.contables.carga.dto.CierreMensual;
import co.com.bbva.app.notas.contables.dao.CommonDAO;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.Collection;

public class CierreMensualDAO extends CommonDAO<CierreMensual> {

	private static String cs_COLUMNAS = "ANO, MES, FECHA_ULTIMA_CARGA";

	private static String cs_TABLA = "NC_CIERRE_MENSUAL";

	private static String cs_PK = null;

	private static String SQL_SELECT_ALL_SENTENCE = "SELECT " + cs_COLUMNAS + " FROM " + cs_TABLA + " ORDER BY ANO DESC, MES DESC";

	private static String SQL_SELECT_BY_PRIMARY_KEY_SENTENCE = "SELECT " + cs_COLUMNAS + " FROM " + cs_TABLA + " WHERE (ANO = ?) AND (MES = ?)";

	private static String SQL_DELETE_SENTENCE = "DELETE FROM " + cs_TABLA + " WHERE (ANO = ?) AND (MES = ?)";

	public CierreMensualDAO() {
		super(cs_TABLA, cs_COLUMNAS, cs_PK, new CierreMensual());
	}

	@Override
	public void internalDelete(Connection con, CierreMensual row) throws Exception {
		executeUpdate(con, SQL_DELETE_SENTENCE, new Object[] { row.getYear(), row.getMes() });
	}

	@Override
	public CierreMensual findByPrimaryKey(CierreMensual row) throws Exception {
		return getByGeneral(SQL_SELECT_BY_PRIMARY_KEY_SENTENCE, new Object[] { row.getYear(), row.getMes() });
	}

	public Collection<CierreMensual> findAll() throws Exception {
		return findByGeneral(SQL_SELECT_ALL_SENTENCE);
	}

	@Override
	protected Object[] getDataToAdd(@SuppressWarnings("unused") Connection con, CierreMensual row) throws Exception {
		return new Object[] { row.getYear(), row.getMes(), row.getFechaUltimaCarga() };
	}

	@Override
	public CierreMensual getResultSetToVO(ResultSet result) throws Exception {
		CierreMensual row = new CierreMensual();

		row.setYear(result.getInt(1));
		row.setMes(result.getInt(2));
		row.setFechaUltimaCarga(result.getTimestamp(3));

		return row;
	}

	@Override
	protected void internalUpdate( @SuppressWarnings("unused") Connection con,@SuppressWarnings("unused") CierreMensual row) throws Exception {
		throw new Exception("Mtodo no implementado");
	}
}