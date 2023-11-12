package co.com.bbva.app.notas.contables.dao;

import co.com.bbva.app.notas.contables.dto.JerarquiaOperacionSFC_N2;

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
public class JerarquiaOperacionSFC_N2DAO extends CommonSeqDAO<JerarquiaOperacionSFC_N2> {

	private static final String cs_PK = "CONSECUTIVO";

	private static final String cs_COLUMNAS = "CONSECUTIVO, CODIGO, NOMBRE, CODIGO_NC_CLASE_RIESGO, FECHA_ESTADO_CARGA";

	private static final String cs_TABLA = "NC_OPER_N2_SFC";
	
	private static String SQL_SELECT_BY_CLASE = "SELECT CODIGO CLAVE, NOMBRE VALOR FROM " + cs_TABLA + " WHERE (CODIGO_NC_CLASE_RIESGO = ?)";
	
	private static String SQL_SELECT_ALL_SENTENCE = "SELECT " + cs_COLUMNAS + " FROM " + cs_TABLA + " ORDER BY TO_NUMBER(CONSECUTIVO)";

	public JerarquiaOperacionSFC_N2DAO() {
		super(cs_TABLA, cs_COLUMNAS, cs_PK, new JerarquiaOperacionSFC_N2());
	}

	public Collection<JerarquiaOperacionSFC_N2> findAll() throws Exception {
		return findByGeneral(SQL_SELECT_ALL_SENTENCE);
	}
	
	public Map<?, String> findByClaseRiesgoN2(String codClase) throws Exception {
		return getCV(SQL_SELECT_BY_CLASE, new Object[] { codClase });

	}
	
	@Override
	protected void internalUpdate(Connection con, JerarquiaOperacionSFC_N2 row) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	protected Object[] getDataToAdd(@SuppressWarnings("unused") Connection con, JerarquiaOperacionSFC_N2 row) throws Exception {	
		return new Object[] {row.getConsecutivo(), row.getCodigo(), row.getNombre(), row.getCodigoNcClaseRiesgo(), row.getFechaEstadoCarga() };
	}

	@Override
	protected JerarquiaOperacionSFC_N2 getResultSetToVO(ResultSet result) throws Exception {
		JerarquiaOperacionSFC_N2 row = new JerarquiaOperacionSFC_N2();

		row.setConsecutivo(result.getString(1));
		row.setCodigo(result.getString(2));
		row.setNombre(result.getString(3) != null ? result.getString(3) : "");
		row.setCodigoNcClaseRiesgo(result.getString(4) != null ? result.getString(4) : "");
		row.setFechaEstadoCarga((Timestamp) result.getObject(5));

		return row;
	}

}
