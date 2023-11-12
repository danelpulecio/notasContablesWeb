package co.com.bbva.app.notas.contables.dao;

import co.com.bbva.app.notas.contables.dto.RiesgoOperacionalSubProd;

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
public class RiesgoOperacionalSubProdDAO extends CommonSeqDAO<RiesgoOperacionalSubProd> {

	private static final String cs_PK = "CODIGO";

	private static final String cs_COLUMNAS = "CODIGO, NOMBRE, CODIGO_PRODUCTO, FECHA_ESTADO_CARGA";

	private static final String cs_TABLA = "NC_RIES_OPER_SUBPROD";

	private static String SQL_SELECT_BY_PRODUCTO = "SELECT CODIGO CLAVE, NOMBRE VALOR FROM " + cs_TABLA + " WHERE (CODIGO_PRODUCTO = ?)";

	private static String SQL_SELECT_ALL_SENTENCE = "SELECT " + cs_COLUMNAS + " FROM " + cs_TABLA + " ORDER BY TO_NUMBER(CODIGO)";

	public RiesgoOperacionalSubProdDAO() {
		super(cs_TABLA, cs_COLUMNAS, cs_PK, new RiesgoOperacionalSubProd());
	}

	@Override
	protected void internalUpdate(Connection con, RiesgoOperacionalSubProd row) throws Exception {
		// TODO Auto-generated method stub

	}

	public Map<?, String> findByProducto(String codProducto) throws Exception {
		return getCV(SQL_SELECT_BY_PRODUCTO, new Object[] { codProducto });

	}

	public Collection<RiesgoOperacionalSubProd> findAll() throws Exception {
		return findByGeneral(SQL_SELECT_ALL_SENTENCE);
	}

	@Override
	protected Object[] getDataToAdd(Connection con, RiesgoOperacionalSubProd row) throws Exception {
		row.setCodigo(getMaxCode(con) + "");
		return new Object[] { row.getCodigo(), row.getNombre(), row.getCodigoProducto(), row.getFechaEstadoCarga() };
	}

	@Override
	protected RiesgoOperacionalSubProd getResultSetToVO(ResultSet result) throws Exception {
		RiesgoOperacionalSubProd row = new RiesgoOperacionalSubProd();

		row.setCodigo(result.getString(1));
		row.setNombre(result.getString(2));
		row.setCodigoProducto(result.getString(3) != null ? result.getString(3) : "");
		row.setFechaEstadoCarga((Timestamp) result.getObject(4));

		return row;
	}

}
