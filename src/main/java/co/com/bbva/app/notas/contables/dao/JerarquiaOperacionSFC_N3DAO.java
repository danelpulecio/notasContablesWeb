package co.com.bbva.app.notas.contables.dao;

import co.com.bbva.app.notas.contables.dto.JerarquiaOperacionSFC_N3;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.Map;

/**
 *
 * @author pjimenez
 * @version 1.0, 15/09/2021
 * @since JDK1.8
 *
 */
public class JerarquiaOperacionSFC_N3DAO extends CommonSeqDAO<JerarquiaOperacionSFC_N3> {

	private static final String cs_PK = "CONSECUTIVO";

	private static final String cs_COLUMNAS = "CONSECUTIVO, CODIGO, NOMBRE, CODIGO_N1_SFC, CODIGO_N2_SFC, ESTADO_CARGA, FECHA_ESTADO_CARGA";

	private static final String cs_TABLA = "NC_OPER_N3_SFC";

	private static String SQL_SELECT_BY_CLASE_N3 = "SELECT CODIGO CLAVE, NOMBRE VALOR FROM " + cs_TABLA + " WHERE (CODIGO_N2_SFC = ?) AND (CODIGO_N1_SFC = ?)";
	
	private static String SQL_SELECT_ALL_SENTENCE = "SELECT " + cs_COLUMNAS + " FROM " + cs_TABLA + " ORDER BY TO_NUMBER(CONSECUTIVO)";

	public JerarquiaOperacionSFC_N3DAO() {
		super(cs_TABLA, cs_COLUMNAS, cs_PK, new JerarquiaOperacionSFC_N3());
	}
	
	public Collection<JerarquiaOperacionSFC_N3> findAll() throws Exception {
		return findByGeneral(SQL_SELECT_ALL_SENTENCE);
	}

	public Map<?, String> findByClaseRiesgoN3(String codClase, String codClaseN1) throws Exception {
		return getCV(SQL_SELECT_BY_CLASE_N3, new Object[] { codClase,  codClaseN1});

	}

	@Override
	protected void internalUpdate(Connection con, JerarquiaOperacionSFC_N3 row) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	protected Object[] getDataToAdd(@SuppressWarnings("unused")Connection con, JerarquiaOperacionSFC_N3 row) throws Exception {
		return new Object[] { row.getConsecutivo(), row.getCodigo(), row.getNombre(), row.getCodigoNcN1ClaseRiesgo(), row.getCodigoNcClaseRiesgo(), row.getEstadoCarga(), row.getFechaEstadoCarga() };
	}

	@Override
	protected JerarquiaOperacionSFC_N3 getResultSetToVO(ResultSet result) throws Exception {
		JerarquiaOperacionSFC_N3 row = new JerarquiaOperacionSFC_N3();
		row.setConsecutivo(result.getString(1));
		row.setCodigo(result.getString(2));
		row.setNombre(result.getString(3) != null ? result.getString(3) : "");
		row.setCodigoNcN1ClaseRiesgo(result.getString(4) != null ? result.getString(4) : "");
		row.setCodigoNcClaseRiesgo(result.getString(5) != null ? result.getString(5) : "");
		row.setEstadoCarga(result.getString(6));
		row.setFechaEstadoCarga((Timestamp) result.getObject(7));
		return row;
	}

}
