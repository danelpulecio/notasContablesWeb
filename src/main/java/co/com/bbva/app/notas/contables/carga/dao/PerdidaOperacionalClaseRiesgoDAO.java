/*
	Nombre DTO: PerdidaOperacionalClaseRiesgo
 */

package co.com.bbva.app.notas.contables.carga.dao;

import co.com.bbva.app.notas.contables.carga.dto.PerdidaOperacionalClaseRiesgo;
import co.com.bbva.app.notas.contables.dao.CommonDAO;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.Collection;

public class PerdidaOperacionalClaseRiesgoDAO extends CommonDAO<PerdidaOperacionalClaseRiesgo> {

	private static String cs_COLUMNAS = "CUENTA, CODIGO_CLASE_RIESGO, CODIGO_TIPO_PERDIDA, ESTADO_CARGA, FECHA_ULTIMA_CARGA";

	private static String cs_TABLA = "NC_PERD_OPER_CLAS_RIES";

	private static String cs_PK = null;

	private static String SQL_SELECT_ALL_SENTENCE = "SELECT " + cs_COLUMNAS + " FROM " + cs_TABLA + " ORDER BY CODIGO_CLASE_RIESGO, CODIGO_TIPO_PERDIDA";

	private static String SQL_SELECT_BY_ESTADO_CARGA_SENTENCE = "SELECT " + cs_COLUMNAS + " FROM " + cs_TABLA + " WHERE (ESTADO_CARGA = ?)";

	private static String SQL_SELECT_BY_PRIMARY_KEY_SENTENCE = "SELECT " + cs_COLUMNAS + " FROM " + cs_TABLA + " WHERE (CUENTA = ? AND CODIGO_CLASE_RIESGO = ? AND CODIGO_TIPO_PERDIDA = ?)";

	private static String SQL_SELECT_BY_SENTENCE = "SELECT " + cs_COLUMNAS + " FROM " + cs_TABLA + " WHERE (CUENTA LIKE ?) OR (CODIGO_CLASE_RIESGO LIKE ?) OR (CODIGO_TIPO_PERDIDA LIKE ?) ORDER BY CODIGO_CLASE_RIESGO, CODIGO_TIPO_PERDIDA";

	private static int NUMERO_COLUMNAS_BUSQUEDA = 3;

	static String SQL_INSERT_SENTENCE = "INSERT INTO " + cs_TABLA + " (" + cs_COLUMNAS + ") VALUES (?, ?, ?, ?, ?)";

	private static String SQL_UPDATE_ESTADO_CARGA_SENTENCE = "UPDATE " + cs_TABLA + " SET ESTADO_CARGA = ?";

	private static String SQL_UPDATE_SENTENCE = "UPDATE " + cs_TABLA + " SET CODIGO_CLASE_RIESGO = ?, CODIGO_TIPO_PERDIDA = ?, ESTADO_CARGA = ?, FECHA_ULTIMA_CARGA = ? WHERE (CUENTA = ? AND CODIGO_CLASE_RIESGO = ? AND CODIGO_TIPO_PERDIDA = ?)";

	private static String SQL_DELETE_SENTENCE = "DELETE FROM " + cs_TABLA + " WHERE (CUENTA = ? AND CODIGO_CLASE_RIESGO = ? AND CODIGO_TIPO_PERDIDA = ?)";

	public PerdidaOperacionalClaseRiesgoDAO() {
		super(cs_TABLA, cs_COLUMNAS, cs_PK, new PerdidaOperacionalClaseRiesgo());
	}

	@Override
	public void internalUpdate(Connection con, PerdidaOperacionalClaseRiesgo row) throws Exception {
		executeUpdate(con, SQL_UPDATE_SENTENCE, new Object[] { row.getCodigoClaseRiesgo(), row.getCodigoTipoPerdida(), row.getEstadoCarga(), row.getFechaUltimaCarga(), row.getCuenta(), row.getCodigoClaseRiesgo(), row.getCodigoTipoPerdida() });
	}

	public void updateEstadoCarga(String estadoCarga) throws Exception {
		executeUpdate(SQL_UPDATE_ESTADO_CARGA_SENTENCE, estadoCarga);
	}

	@Override
	public void internalDelete(Connection con, PerdidaOperacionalClaseRiesgo row) throws Exception {
		executeUpdate(con, SQL_DELETE_SENTENCE, new Object[] { row.getCuenta(), row.getCodigoClaseRiesgo(), row.getCodigoTipoPerdida() });
	}

	@Override
	public PerdidaOperacionalClaseRiesgo findByPrimaryKey(PerdidaOperacionalClaseRiesgo row) throws Exception {
		return getByGeneral(SQL_SELECT_BY_PRIMARY_KEY_SENTENCE, new Object[] { row.getCuenta(), row.getCodigoClaseRiesgo(), row.getCodigoTipoPerdida() });
	}

	public Collection<PerdidaOperacionalClaseRiesgo> findAll() throws Exception {
		return findByGeneral(SQL_SELECT_ALL_SENTENCE);
	}

	public Collection<PerdidaOperacionalClaseRiesgo> findByEstadoCarga(String estadoCarga) throws Exception {
		return findByGeneral(SQL_SELECT_BY_ESTADO_CARGA_SENTENCE, estadoCarga);
	}

	public Collection<PerdidaOperacionalClaseRiesgo> findBy(String palabraClave) throws Exception {
		return findByKeyWord(SQL_SELECT_BY_SENTENCE, palabraClave, NUMERO_COLUMNAS_BUSQUEDA);
	}

	@Override
	protected Object[] getDataToAdd(@SuppressWarnings("unused") Connection con, PerdidaOperacionalClaseRiesgo row) throws Exception {
		return new Object[] { row.getCuenta(), row.getCodigoClaseRiesgo(), row.getCodigoTipoPerdida(), row.getEstadoCarga(), row.getFechaUltimaCarga() };
	}

	@Override
	public PerdidaOperacionalClaseRiesgo getResultSetToVO(ResultSet result) throws Exception {
		PerdidaOperacionalClaseRiesgo row = new PerdidaOperacionalClaseRiesgo();

		row.setCuenta(result.getString(1));
		row.setCodigoClaseRiesgo(result.getString(2));
		row.setCodigoTipoPerdida(result.getString(3));
		row.setEstadoCarga(result.getString(4));
		row.setFechaUltimaCarga(result.getTimestamp(5));

		return row;
	}
}