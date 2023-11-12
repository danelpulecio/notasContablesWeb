/*
	Nombre DTO: HADTAPL
 */

package co.com.bbva.app.notas.contables.carga.dao;

import co.com.bbva.app.notas.contables.carga.dto.HADTAPL;
import co.com.bbva.app.notas.contables.dao.CommonDAO;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.Collection;

public class HADTAPLDAO extends CommonDAO<HADTAPL> {

	private static String cs_COLUMNAS = "CUENTA_CONTABLE, INDICADOR_CONTRATO, ESTADO_CARGA, FECHA_ULTIMA_CARGA";

	private static String cs_TABLA = "NC_HADTAPL";

	private static String cs_PK = "CUENTA_CONTABLE";

	private static String SQL_SELECT_ALL_SENTENCE = "SELECT " + cs_COLUMNAS + " FROM " + cs_TABLA + " ORDER BY CUENTA_CONTABLE";

	private static String SQL_SELECT_BY_ESTADO_CARGA_SENTENCE = "SELECT " + cs_COLUMNAS + " FROM " + cs_TABLA + " WHERE (ESTADO_CARGA = ?)";

	private static String SQL_SELECT_BY_SENTENCE = "SELECT " + cs_COLUMNAS + " FROM " + cs_TABLA + " WHERE (CUENTA_CONTABLE LIKE ?) OR (INDICADOR_CONTRATO LIKE ?) ORDER BY CUENTA_CONTABLE";

	private static String SQL_SELECT_BY_CUENTA = "SELECT " + cs_COLUMNAS + " FROM " + cs_TABLA + " WHERE (CUENTA_CONTABLE = ?)";

	private static int NUMERO_COLUMNAS_BUSQUEDA = 2;

	private static String SQL_UPDATE_ESTADO_CARGA_SENTENCE = "UPDATE " + cs_TABLA + " SET ESTADO_CARGA = ?";

	private static String SQL_UPDATE_SENTENCE = "UPDATE " + cs_TABLA + " SET INDICADOR_CONTRATO = ?, ESTADO_CARGA = ?, FECHA_ULTIMA_CARGA = ? WHERE (CUENTA_CONTABLE = ?)";

	public HADTAPLDAO() {
		super(cs_TABLA, cs_COLUMNAS, cs_PK, new HADTAPL());
	}

	@Override
	public void internalUpdate( Connection con,HADTAPL row) throws Exception {
		executeUpdate(con, SQL_UPDATE_SENTENCE, new Object[] { row.getIndicadorContrato(), row.getEstadoCarga(), row.getFechaUltimaCarga(), row.getCuentaContable() });
	}

	public void updateEstadoCarga(String estadoCarga) throws Exception {
		executeUpdate(SQL_UPDATE_ESTADO_CARGA_SENTENCE, estadoCarga);
	}

	public Collection<HADTAPL> findAll() throws Exception {
		return findByGeneral(SQL_SELECT_ALL_SENTENCE);
	}

	public Collection<HADTAPL> findByEstadoCarga(String estadoCarga) throws Exception {
		return findByGeneral(SQL_SELECT_BY_ESTADO_CARGA_SENTENCE, estadoCarga);
	}

	public HADTAPL getByCuenta(HADTAPL row) throws Exception {
		return getByGeneral(SQL_SELECT_BY_CUENTA, row.getCuentaContable());
	}
	
	public Collection<HADTAPL> findBy(String palabraClave) throws Exception {
		return findByKeyWord(SQL_SELECT_BY_SENTENCE, palabraClave, NUMERO_COLUMNAS_BUSQUEDA);
	}

	@Override
	protected Object[] getDataToAdd(@SuppressWarnings("unused") Connection con, HADTAPL row) throws Exception {
		return new Object[] { row.getCuentaContable(), row.getIndicadorContrato(), row.getEstadoCarga(), row.getFechaUltimaCarga() };
	}

	@Override
	public HADTAPL getResultSetToVO(ResultSet result) throws Exception {
		HADTAPL row = new HADTAPL();

		row.setCuentaContable(result.getString(1));
		row.setIndicadorContrato(result.getString(2));
		row.setEstadoCarga(result.getString(3));
		row.setFechaUltimaCarga(result.getTimestamp(4));

		return row;
	}
}