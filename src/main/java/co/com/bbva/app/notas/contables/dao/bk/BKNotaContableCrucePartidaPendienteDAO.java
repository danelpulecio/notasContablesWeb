/*
	Nombre DTO: NotaContableCrucePartidaPendiente
 */

package co.com.bbva.app.notas.contables.dao.bk;

import co.com.bbva.app.notas.contables.dto.NotaContableCrucePartidaPendiente;

import java.sql.Connection;
import java.sql.ResultSet;

public class BKNotaContableCrucePartidaPendienteDAO extends BKCommonDAO<NotaContableCrucePartidaPendiente> {

	private static String cs_COLUMNAS = "CODIGO, CODIGO_NOTA_CONTABLE, CUENTA, DESCRIPCION, SUCURSAL_DESTINO, NATURALEZA, REFERENCIA_CRUCE, DIVISA, IMPORTE, CONCEPTO, FECHA_CONTABLE, NUMERO_ASIENTO, NUMERO_APUNTE";

	private static String cs_TABLA = "NC_BK_NOT_CON_CRU_PAR_PEN";

	private static String cs_PK = "CODIGO";

	private static String SQL_UPDATE_SENTENCE = "UPDATE " + cs_TABLA + " SET NUMERO_ASIENTO = ?, NUMERO_APUNTE = ? WHERE (CODIGO = ?)";

	public BKNotaContableCrucePartidaPendienteDAO() {
		super(cs_TABLA, cs_COLUMNAS, cs_PK, new NotaContableCrucePartidaPendiente());
	}

	@Override
	public void internalUpdate( Connection con,NotaContableCrucePartidaPendiente row) throws Exception {
		executeUpdate(con,SQL_UPDATE_SENTENCE, new Object[] { row.getNumeroAsiento(), row.getNumeroApunte(), row.getCodigo() });
	}

	@Override
	protected Object[] getDataToAdd(@SuppressWarnings("unused") Connection con, NotaContableCrucePartidaPendiente row) throws Exception {
		return new Object[] { row.getCodigo(), row.getCodigoNotaContable(), row.getCuentaContable(), row.getDescripcionPartidaPendiente(), row.getCodigoSucursalDestino(), row.getNaturaleza(), row.getReferenciaCruce(), row.getDivisa(),
				row.getImporte(), row.getConcepto(), row.getFechaContable(), row.getNumeroAsiento(), row.getNumeroApunte() };
	}

	@Override
	public NotaContableCrucePartidaPendiente getResultSetToVO(ResultSet result) throws Exception {
		NotaContableCrucePartidaPendiente row = new NotaContableCrucePartidaPendiente();

		row.setCodigo(result.getInt(1));
		row.setCodigoNotaContable(result.getInt(2));
		row.setCuentaContable(result.getString(3) != null ? result.getString(3) : "");
		row.setDescripcionPartidaPendiente(result.getString(4) != null ? result.getString(4) : "");
		row.setCodigoSucursalDestino(result.getString(5) != null ? result.getString(5) : "");
		row.setNaturaleza(result.getString(6) != null ? result.getString(6) : "");
		row.setReferenciaCruce(result.getString(7) != null ? result.getString(7) : "");
		row.setDivisa(result.getString(8) != null ? result.getString(8) : "");
		row.setImporte(result.getDouble(9));
		row.setConcepto(result.getString(10) != null ? result.getString(10) : "");
		row.setFechaContable(result.getTimestamp(11));
		row.setNumeroAsiento(result.getString(12) != null ? result.getString(12) : "");
		row.setNumeroApunte(result.getString(13) != null ? result.getString(13) : "");

		return row;
	}

}