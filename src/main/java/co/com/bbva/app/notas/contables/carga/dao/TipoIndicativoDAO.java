/*
	Nombre DTO: TipoIndicativo
 */

package co.com.bbva.app.notas.contables.carga.dao;

import co.com.bbva.app.notas.contables.carga.dto.TipoIndicativo;
import co.com.bbva.app.notas.contables.dao.CommonDAO;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.Collection;

public class TipoIndicativoDAO extends CommonDAO<TipoIndicativo> {

	private static String cs_COLUMNAS = "CIUDAD, DEPARTAMENTO, INDICATIVO, ESTADO_CARGA, FECHA_ULTIMA_CARGA";

	private static String cs_TABLA = "NC_TIPO_INDICATIVO";

	private static String cs_PK = null;

	private static String SQL_SELECT_ALL_SENTENCE = "SELECT " + cs_COLUMNAS + " FROM " + cs_TABLA + " ORDER BY INDICATIVO, CIUDAD";

	private static String SQL_SELECT_BY_ESTADO_CARGA_SENTENCE = "SELECT " + cs_COLUMNAS + " FROM " + cs_TABLA + " WHERE (ESTADO_CARGA = ?)";

	private static String SQL_SELECT_BY_PRIMARY_KEY_SENTENCE = "SELECT " + cs_COLUMNAS + " FROM " + cs_TABLA + " WHERE (CIUDAD = ?) AND (DEPARTAMENTO = ?)";

	private static String SQL_SELECT_BY_SENTENCE = "SELECT " + cs_COLUMNAS + " FROM " + cs_TABLA + " WHERE (UPPER(CIUDAD) LIKE ?) OR (UPPER(DEPARTAMENTO) LIKE ?) OR (INDICATIVO LIKE ?) ORDER BY CIUDAD";

	private static int NUMERO_COLUMNAS_BUSQUEDA = 3;

	private static String SQL_UPDATE_ESTADO_CARGA_SENTENCE = "UPDATE " + cs_TABLA + " SET ESTADO_CARGA = ?";

	private static String SQL_UPDATE_SENTENCE = "UPDATE " + cs_TABLA + " SET INDICATIVO = ?,  ESTADO_CARGA = ?, FECHA_ULTIMA_CARGA = ? WHERE (CIUDAD = ?) AND (DEPARTAMENTO = ?)";

	private static String SQL_DELETE_SENTENCE = "DELETE FROM " + cs_TABLA + " WHERE (CIUDAD = ?) AND (DEPARTAMENTO = ?)";

	public TipoIndicativoDAO() {
		super(cs_TABLA, cs_COLUMNAS, cs_PK, new TipoIndicativo());
	}

	@Override
	public void internalUpdate(Connection con, TipoIndicativo row) throws Exception {
		executeUpdate(con, SQL_UPDATE_SENTENCE, new Object[] { row.getIndicativo(), row.getEstadoCarga(), row.getFechaUltimaCarga(), row.getCiudad(), row.getDepartamento() });
	}

	public void updateEstadoCarga(String estadoCarga) throws Exception {
		executeUpdate(SQL_UPDATE_ESTADO_CARGA_SENTENCE, estadoCarga);
	}

	@Override
	public void internalDelete(Connection con, TipoIndicativo row) throws Exception {
		executeUpdate(con, SQL_DELETE_SENTENCE, new Object[] { row.getCiudad(), row.getDepartamento() });
	}

	@Override
	public TipoIndicativo findByPrimaryKey(TipoIndicativo row) throws Exception {
		return getByGeneral(SQL_SELECT_BY_PRIMARY_KEY_SENTENCE, new Object[] { row.getCiudad(), row.getDepartamento() });
	}

	public Collection<TipoIndicativo> findAll() throws Exception {
		return findByGeneral(SQL_SELECT_ALL_SENTENCE);
	}

	public Collection<TipoIndicativo> findByEstadoCarga(String estadoCarga) throws Exception {
		return findByGeneral(SQL_SELECT_BY_ESTADO_CARGA_SENTENCE, estadoCarga);
	}

	public Collection<TipoIndicativo> findBy(String palabraClave) throws Exception {
		return findByKeyWord(SQL_SELECT_BY_SENTENCE, palabraClave, NUMERO_COLUMNAS_BUSQUEDA);
	}

	@Override
	protected Object[] getDataToAdd(@SuppressWarnings("unused") Connection con, TipoIndicativo row) throws Exception {
		return new Object[] { row.getCiudad(), row.getDepartamento(), row.getIndicativo(), row.getEstadoCarga(), row.getFechaUltimaCarga() };
	}

	@Override
	public TipoIndicativo getResultSetToVO(ResultSet result) throws Exception {
		TipoIndicativo row = new TipoIndicativo();

		row.setCiudad(result.getString(1));
		row.setDepartamento(result.getString(2));
		row.setIndicativo(result.getString(3));
		row.setEstadoCarga(result.getString(4));
		row.setFechaUltimaCarga(result.getTimestamp(5));

		return row;
	}
}