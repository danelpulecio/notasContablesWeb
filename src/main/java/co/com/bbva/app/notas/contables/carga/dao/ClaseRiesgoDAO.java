/*
	Nombre DTO: ClaseRiesgo
 */

package co.com.bbva.app.notas.contables.carga.dao;

import co.com.bbva.app.notas.contables.carga.dto.ClaseRiesgo;
import co.com.bbva.app.notas.contables.dao.CommonDAO;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

public class ClaseRiesgoDAO extends CommonDAO<ClaseRiesgo> {

	private static String cs_COLUMNAS = "CODIGO, NOMBRE, ESTADO_CARGA, FECHA_ULTIMA_CARGA";

	private static String cs_TABLA = "NC_CLASE_RIESGO";

	private static String cs_PK = "CODIGO";

	private static String SQL_SELECT_ALL_SENTENCE = "SELECT " + cs_COLUMNAS + " FROM " + cs_TABLA + " ORDER BY CODIGO";

	private static String SQL_CV_POR_CUENTA = "SELECT a.CODIGO CLAVE, a.NOMBRE VALOR FROM NC_PERD_OPER_CLAS_RIES b LEFT JOIN NC_CLASE_RIESGO a ON a.CODIGO=b.CODIGO_CLASE_RIESGO WHERE b.CUENTA = ?";

	private static String SQL_SELECT_BY_ESTADO_CARGA_SENTENCE = "SELECT " + cs_COLUMNAS + " FROM " + cs_TABLA + " WHERE (ESTADO_CARGA = ?)";

	private static String SQL_SELECT_BY_SENTENCE = "SELECT " + cs_COLUMNAS + " FROM " + cs_TABLA + " WHERE (CODIGO = ?) OR (NOMBRE LIKE ?)";

	private static int NUMERO_COLUMNAS_BUSQUEDA = 2;

	private static String SQL_UPDATE_ESTADO_CARGA_SENTENCE = "UPDATE " + cs_TABLA + " SET ESTADO_CARGA = ?";

	private static String SQL_UPDATE_SENTENCE = "UPDATE " + cs_TABLA + " SET NOMBRE = ?, ESTADO_CARGA = ?, FECHA_ULTIMA_CARGA = ? WHERE (CODIGO = ?)";

	public ClaseRiesgoDAO() {
		super(cs_TABLA, cs_COLUMNAS, cs_PK, new ClaseRiesgo());
	}

	@Override
	public void internalUpdate( Connection con,ClaseRiesgo row) throws Exception {
		executeUpdate(con, SQL_UPDATE_SENTENCE, new Object[] { row.getNombre(), row.getEstadoCarga(), row.getFechaUltimaCarga(), row.getCodigo() });
	}

	public void updateEstadoCarga(String estadoCarga) throws Exception {
		executeUpdate(SQL_UPDATE_ESTADO_CARGA_SENTENCE, estadoCarga);
	}

	public Collection<ClaseRiesgo> findAll() throws Exception {
		return findByGeneral(SQL_SELECT_ALL_SENTENCE);
	}

	public Collection<ClaseRiesgo> findByEstadoCarga(String estadoCarga) throws Exception {
		return findByGeneral(SQL_SELECT_BY_ESTADO_CARGA_SENTENCE, estadoCarga);
	}

	public Collection<ClaseRiesgo> findBy(String palabraClave) throws Exception {
		ArrayList<Object> params = new ArrayList<Object>();
		for (int count = 1; count <= NUMERO_COLUMNAS_BUSQUEDA; count++) {
			if (count != 1) {
				params.add("%" + palabraClave + "%");
			} else {
				try {
					params.add(Integer.parseInt(palabraClave));
				} catch (Exception le_e) {
					params.add(Integer.valueOf(0));
				}
			}
		}
		return findByGeneral(SQL_SELECT_BY_SENTENCE, params.toArray());
	}

	@Override
	protected Object[] getDataToAdd(@SuppressWarnings("unused") Connection con, ClaseRiesgo row) throws Exception {
		return new Object[] { row.getCodigo(), row.getNombre(), row.getEstadoCarga(), row.getFechaUltimaCarga() };
	}

	@Override
	public ClaseRiesgo getResultSetToVO(ResultSet result) throws Exception {
		ClaseRiesgo row = new ClaseRiesgo();

		row.setCodigo(result.getString(1));
		row.setNombre(result.getString(2));
		row.setEstadoCarga(result.getString(3));
		row.setFechaUltimaCarga(result.getTimestamp(4));

		return row;
	}

	public Map<?, String> getCVClasRiesPorCuenta(String cuenta) throws Exception{
		return getCV(SQL_CV_POR_CUENTA, cuenta);
	}
}