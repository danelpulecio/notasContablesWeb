/*
	Nombre DTO: ActividadEconomica
 */

package co.com.bbva.app.notas.contables.dao.bk;

import co.com.bbva.app.notas.contables.carga.dto.RechazoSalida;

import java.sql.Connection;
import java.sql.ResultSet;

public class BKRechazoSalidaDAO extends BKCommonDAO<RechazoSalida> {

	private static String cs_COLUMNAS = "CODIGO,CENTRO_ORIGEN,CENTRO_DESTINO,CONSECUTIVO,CODIGO_NOTA,FECHA,CUENTA,DIVISA,COD_ERROR,DES_ERROR,FECHA_GEN,HORAS_GEN";

	private static String cs_TABLA = "NC_BK_RECHAZO_SALIDA";

	private static String cs_PK = "CODIGO";

	public BKRechazoSalidaDAO() {
		super(cs_TABLA, cs_COLUMNAS, cs_PK, new RechazoSalida());
	}

	@Override
	public void internalUpdate(@SuppressWarnings("unused") Connection con, @SuppressWarnings("unused") RechazoSalida row) throws Exception {
		throw new Exception("El mtodo no ha sido creado");
	}

	@Override
	protected Object[] getDataToAdd(@SuppressWarnings("unused") Connection con, RechazoSalida row) throws Exception {
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