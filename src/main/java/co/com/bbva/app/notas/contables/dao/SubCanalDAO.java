package co.com.bbva.app.notas.contables.dao;

import co.com.bbva.app.notas.contables.dto.SubCanal;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.Map;

/**
 *
 * @author pjimenez
* @version 1.0, 20/09/2021
* @since JDK1.8
 *
 */
public class SubCanalDAO extends CommonSeqDAO<SubCanal>{
	

	private static final String cs_PK = "CODIGO";

	private static final String cs_COLUMNAS = "CODIGO, CODIGO_CANAL, DESCRIPCION, FECHA_ESTADO_CARGA";

	private static final String cs_TABLA = "NC_SUBCANALES";
	
	private static String SQL_SELECT_BY_CANAL = "SELECT CODIGO CLAVE, DESCRIPCION VALOR FROM " + cs_TABLA + " WHERE (CODIGO_CANAL = ?)";
	
	private static String SQL_SELECT_ALL_SENTENCE = "SELECT " + cs_COLUMNAS + " FROM " + cs_TABLA + " ORDER BY TO_NUMBER(CODIGO)";
	
	public SubCanalDAO() {
		super(cs_TABLA, cs_COLUMNAS, cs_PK, new SubCanal());
	}

	public Collection<SubCanal> findAll() throws Exception {
		return findByGeneral(SQL_SELECT_ALL_SENTENCE);
	}
	
	public Map<?, String> findByCanal(String codCanal) throws Exception {
		return getCV(SQL_SELECT_BY_CANAL, new Object[] { codCanal });

	}
	
	@Override
	protected void internalUpdate(Connection con, SubCanal row) throws Exception {
		// TODO Auto-generated method stub
		
	}
	

	@Override
	protected Object[] getDataToAdd(Connection con, SubCanal row) throws Exception {
		row.setCodigo(getMaxCode(con)+"");
		return new Object[] { row.getCodigo(), row.getCodigoCanal(), row.getDescripcion(), row.getFechaEstadoCarga()};
	}

	@Override
	protected SubCanal getResultSetToVO(ResultSet result) throws Exception {
		SubCanal row = new SubCanal();

		row.setCodigo(result.getString(1));
		row.setCodigoCanal(result.getString(2));
		row.setDescripcion(result.getString(3) != null ? result.getString(3) : "");		
		row.setFechaEstadoCarga((Timestamp) result.getObject(4));

		return row;
	}


}
