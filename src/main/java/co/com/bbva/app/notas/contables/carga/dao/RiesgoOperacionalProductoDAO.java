/*
	Nombre DTO: RiesgoOperacionalProducto
 */

package co.com.bbva.app.notas.contables.carga.dao;

import co.com.bbva.app.notas.contables.carga.dto.RiesgoOperacionalProducto;
import co.com.bbva.app.notas.contables.dao.CommonDAO;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.Collection;

public class RiesgoOperacionalProductoDAO extends CommonDAO<RiesgoOperacionalProducto> {

	private static String cs_COLUMNAS = "CODIGO, NOMBRE, ESTADO_CARGA, FECHA_ULTIMA_CARGA";

	private static String cs_TABLA = "NC_RIES_OPER_PROD";

	private static String cs_PK = "CODIGO";

	private static String SQL_SELECT_ALL_SENTENCE = "SELECT " + cs_COLUMNAS + " FROM " + cs_TABLA + " ORDER BY TO_NUMBER(CODIGO)";

	private static String SQL_SELECT_BY_ESTADO_CARGA_SENTENCE = "SELECT " + cs_COLUMNAS + " FROM " + cs_TABLA + " WHERE (ESTADO_CARGA = ?)";

	private static String SQL_SELECT_BY_SENTENCE = "SELECT " + cs_COLUMNAS + " FROM " + cs_TABLA + " WHERE (CODIGO LIKE ?) OR (UPPER(NOMBRE) LIKE ?) ORDER BY TO_NUMBER(CODIGO)";

	private static int NUMERO_COLUMNAS_BUSQUEDA = 2;

	private static String SQL_UPDATE_ESTADO_CARGA_SENTENCE = "UPDATE " + cs_TABLA + " SET ESTADO_CARGA = ?";

	private static String SQL_UPDATE_SENTENCE = "UPDATE " + cs_TABLA + " SET NOMBRE = ?, ESTADO_CARGA = ?, FECHA_ULTIMA_CARGA = ? WHERE (CODIGO = ?)";

	public RiesgoOperacionalProductoDAO() {
		super(cs_TABLA, cs_COLUMNAS, cs_PK, new RiesgoOperacionalProducto());
	}

	@Override
	public void internalUpdate( Connection con,RiesgoOperacionalProducto row) throws Exception {
		executeUpdate(con,SQL_UPDATE_SENTENCE, new Object[] { row.getNombre(), row.getEstadoCarga(), row.getFechaUltimaCarga(), row.getCodigo() });
	}

	public void updateEstadoCarga(String estadoCarga) throws Exception {
		executeUpdate(SQL_UPDATE_ESTADO_CARGA_SENTENCE, estadoCarga);
	}

	public Collection<RiesgoOperacionalProducto> findAll() throws Exception {
		return findByGeneral(SQL_SELECT_ALL_SENTENCE);
	}

	public Collection<RiesgoOperacionalProducto> findByEstadoCarga(String estadoCarga) throws Exception {
		return findByGeneral(SQL_SELECT_BY_ESTADO_CARGA_SENTENCE, estadoCarga);
	}

	public Collection<RiesgoOperacionalProducto> findBy(String palabraClave) throws Exception {
		return findByKeyWord(SQL_SELECT_BY_SENTENCE, palabraClave, NUMERO_COLUMNAS_BUSQUEDA);
	}

	@Override
	protected Object[] getDataToAdd(@SuppressWarnings("unused") Connection con, RiesgoOperacionalProducto row) throws Exception {
		return new Object[] { row.getCodigo(), row.getNombre(), row.getEstadoCarga(), row.getFechaUltimaCarga() };
	}

	@Override
	public RiesgoOperacionalProducto getResultSetToVO(ResultSet result) throws Exception {
		RiesgoOperacionalProducto row = new RiesgoOperacionalProducto();

		row.setCodigo(result.getString(1));
		row.setNombre(result.getString(2));
		row.setEstadoCarga(result.getString(3));
		row.setFechaUltimaCarga(result.getTimestamp(4));

		return row;
	}
}