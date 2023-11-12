/*
	Nombre DTO: PerdidaOperacional
 */

package co.com.bbva.app.notas.contables.carga.dao;

import co.com.bbva.app.notas.contables.carga.dto.PerdidaOperacional;
import co.com.bbva.app.notas.contables.dao.CommonDAO;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

public class PerdidaOperacionalDAO extends CommonDAO<PerdidaOperacional> {

	private static String cs_COLUMNAS = "CODIGO, DESCRIPCION, ESTADO_CARGA, FECHA_ULTIMA_CARGA";

	private static String cs_TABLA = "NC_PERDIDA_OPERACION";

	private static String cs_PK = "CODIGO";

	private static String SQL_SELECT_ALL_SENTENCE = "SELECT " + cs_COLUMNAS + " FROM " + cs_TABLA + " ORDER BY CODIGO";

	private static String SQL_CV_POR_CUENTA_CLAS_RIES = "select a.codigo CLAVE, a.descripcion VALOR from NC_PERD_OPER_CLAS_RIES b left join NC_PERDIDA_OPERACION a on b.codigo_tipo_perdida=a.codigo where b.cuenta= ? and b.codigo_clase_riesgo= ?";

	private static String SQL_SELECT_BY_ESTADO_CARGA_SENTENCE = "SELECT " + cs_COLUMNAS + " FROM " + cs_TABLA + " WHERE (ESTADO_CARGA = ?)";

	private static String SQL_SELECT_BY_SENTENCE = "SELECT " + cs_COLUMNAS + " FROM " + cs_TABLA + " WHERE (CODIGO = ?) OR (DESCRIPCION LIKE ?)";

	private static int NUMERO_COLUMNAS_BUSQUEDA = 2;

	private static String SQL_UPDATE_ESTADO_CARGA_SENTENCE = "UPDATE " + cs_TABLA + " SET ESTADO_CARGA = ?";

	private static String SQL_UPDATE_SENTENCE = "UPDATE " + cs_TABLA + " SET DESCRIPCION = ?, ESTADO_CARGA = ?, FECHA_ULTIMA_CARGA = ? WHERE (CODIGO = ?)";

	public PerdidaOperacionalDAO() {
		super(cs_TABLA, cs_COLUMNAS, cs_PK, new PerdidaOperacional());
	}

	@Override
	public void internalUpdate( Connection con,PerdidaOperacional row) throws Exception {
		executeUpdate(con, SQL_UPDATE_SENTENCE, new Object[] { row.getDescripcion(), row.getEstadoCarga(), row.getFechaUltimaCarga(), row.getCodigo() });
	}

	public void updateEstadoCarga(String estadoCarga) throws Exception {
		executeUpdate(SQL_UPDATE_ESTADO_CARGA_SENTENCE, estadoCarga);
	}

	public Collection<PerdidaOperacional> findAll() throws Exception {
		return findByGeneral(SQL_SELECT_ALL_SENTENCE);
	}

	public Collection<PerdidaOperacional> findByEstadoCarga(String estadoCarga) throws Exception {
		return findByGeneral(SQL_SELECT_BY_ESTADO_CARGA_SENTENCE, estadoCarga);
	}

	public Collection<PerdidaOperacional> findBy(String palabraClave) throws Exception {
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
	protected Object[] getDataToAdd(@SuppressWarnings("unused") Connection con, PerdidaOperacional row) throws Exception {
		return new Object[] { row.getCodigo(), row.getDescripcion(), row.getEstadoCarga(), row.getFechaUltimaCarga() };
	}

	@Override
	public PerdidaOperacional getResultSetToVO(ResultSet result) throws Exception {
		PerdidaOperacional row = new PerdidaOperacional();

		row.setCodigo(result.getString(1));
		row.setDescripcion(result.getString(2));
		row.setEstadoCarga(result.getString(3));
		row.setFechaUltimaCarga(result.getTimestamp(4));

		return row;
	}

	public Map<?, String> getCVPorCuentaClaseRiesgo(String cuenta, String claseRiesgo) throws Exception {
		return getCV(SQL_CV_POR_CUENTA_CLAS_RIES, new Object[] { cuenta, claseRiesgo });
	}
}