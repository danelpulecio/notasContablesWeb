/*
	Nombre DTO: ActividadEconomica
 */

package co.com.bbva.app.notas.contables.carga.dao;

import co.com.bbva.app.notas.contables.carga.dto.RechazoSalida;
import co.com.bbva.app.notas.contables.dao.CommonDAO;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class RechazoSalidaDAO extends CommonDAO<RechazoSalida> {

	private static String cs_COLUMNAS = "CODIGO,CENTRO_ORIGEN,CENTRO_DESTINO,CONSECUTIVO,CODIGO_NOTA,FECHA,CUENTA,DIVISA,COD_ERROR,DES_ERROR,FECHA_GEN,HORAS_GEN";

	private static String cs_TABLA = "NC_RECHAZO_SALIDA";

	private static String cs_PK = "CODIGO";

	private static String SQL_SELECT_ALL_SENTENCE = "SELECT " + cs_COLUMNAS + " FROM " + cs_TABLA;

	private static String SQL_SELECT_BY_SENTENCE = "SELECT " + cs_COLUMNAS + " FROM " + cs_TABLA + " WHERE (CODIGO = ?)";

	private static String SQL_SELECT_BY_NOTA_CONTABLE = "SELECT " + cs_COLUMNAS + " FROM " + cs_TABLA + " WHERE (CODIGO_NOTA = ?)";

	private static int NUMERO_COLUMNAS_BUSQUEDA = 1;

	public RechazoSalidaDAO() {
		super(cs_TABLA, cs_COLUMNAS, cs_PK, new RechazoSalida());
	}

	@Override
	public void internalUpdate(@SuppressWarnings("unused") Connection con, @SuppressWarnings("unused") RechazoSalida row) throws Exception {
		throw new Exception("El mtodo no ha sido creado");
	}

	public Collection<RechazoSalida> findAll() throws Exception {
		return findByGeneral(SQL_SELECT_ALL_SENTENCE);
	}

	public Collection<RechazoSalida> findByNotaContable(String numRadicacion) throws Exception {
		return findByGeneral(SQL_SELECT_BY_NOTA_CONTABLE, numRadicacion);
	}

	public Collection<RechazoSalida> findBy(String palabraClave) throws Exception {
		List<Object> params = new ArrayList<Object>();
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
	protected Object[] getDataToAdd(Connection con, RechazoSalida row) throws Exception {
		row.setCodigo(getMaxCode(con));
		return new Object[] { row.getCodigo(), row.getCeOrigen(), row.getCeDestin(), row.getConsecutivo(), row.getNumNota(), row.getFecha(), row.getCuenta(), row.getDivisa(), row.getCodError(), row.getDesError(), row.getFechaGen(), row.getHorasGen() };
	}

	@Override
	public RechazoSalida getResultSetToVO(ResultSet result) throws Exception {
		RechazoSalida row = new RechazoSalida();
		row.setCodigo(result.getInt(1));
		row.setCeOrigen(result.getString(2));
		row.setCeDestin(result.getString(3));
		row.setConsecutivo(result.getInt(4));
		row.setNumNota(result.getString(5));
		row.setFecha(result.getString(6));
		row.setCuenta(result.getString(7));
		row.setDivisa(result.getString(8));
		row.setCodError(result.getInt(9));
		row.setDesError(result.getString(10));
		row.setFechaGen(result.getString(11));
		row.setHorasGen(result.getString(12));
		return row;
	}
}