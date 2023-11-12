package co.com.bbva.app.notas.contables.dao;

import co.com.bbva.app.notas.contables.dto.Canal;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.Collection;

/**
 *
 * @author pjimenez
* @version 1.0, 20/09/2021
* @since JDK1.8
 *
 */
public class CanalDAO extends CommonSeqDAO<Canal> {
	
	private static final String cs_PK = "CODIGO";

	private static final String cs_COLUMNAS = "CODIGO, DESCRIPCION, FECHA_ESTADO_CARGA";

	private static final String cs_TABLA = "NC_CANAL";
	
	private static String SQL_SELECT_ALL_SENTENCE = "SELECT " + cs_COLUMNAS + " FROM " + cs_TABLA + " ORDER BY TO_NUMBER(CODIGO)";

	public CanalDAO() {
		super(cs_TABLA, cs_COLUMNAS, cs_PK, new Canal());
	}
	
	public Collection<Canal> findAll() throws Exception {
		return findByGeneral(SQL_SELECT_ALL_SENTENCE);
	}
	
	@Override
	protected void internalUpdate(Connection con, Canal row) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected Object[] getDataToAdd(Connection con, Canal row) throws Exception {
		row.setCodigo(getMaxCode(con)+"");
		return new Object[] { row.getCodigo(), row.getDescripcion(), row.getFechaEstadoCarga()};
	}

	@Override
	protected Canal getResultSetToVO(ResultSet result) throws Exception {
		Canal row = new Canal();

		row.setCodigo(result.getString(1));
		row.setDescripcion(result.getString(2) != null ? result.getString(2) : "");		
		row.setFechaEstadoCarga((Timestamp) result.getObject(3));

		return row;
	}

}
