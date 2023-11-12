/*
	Nombre DTO: NotaContable
 */

package co.com.bbva.app.notas.contables.dao.bk;

import co.com.bbva.app.notas.contables.dto.NotaContable;
import co.com.bbva.app.notas.contables.util.DateUtils;

import java.sql.Connection;
import java.sql.ResultSet;

public class BKNotaContableDAO extends BKCommonDAO<NotaContable> {

	private static String cs_COLUMNAS = "CODIGO, FECHA_HORA_REGISTRO_MODULO, FECHA_HORA_REGISTRO_ALTAMIRA, CODIGO_SUCURSAL_ORIGEN, CODIGO_CONCEPTO, CODIGO_TIPO_EVENTO, NUMERO_RADICACION, TIPO_NOTA, DESCRIPCION, ASIENTO_CONTABLE, ESTADO";

	private static String cs_TABLA = "NC_BK_NOTA_CONTABLE";
	private static String cs_PK = "CODIGO";

	private static String SQL_UPDATE_SENTENCE = "UPDATE " + cs_TABLA + " SET FECHA_HORA_REGISTRO_ALTAMIRA = ?, CODIGO_SUCURSAL_ORIGEN = ?, CODIGO_CONCEPTO = ?, CODIGO_TIPO_EVENTO = ?, DESCRIPCION = ?, ESTADO = ? WHERE (CODIGO = ?)";

	public BKNotaContableDAO() {
		super(cs_TABLA, cs_COLUMNAS, cs_PK, new NotaContable());
	}

	@Override
	public void internalUpdate( Connection con,NotaContable row) throws Exception {
		executeUpdate(con,SQL_UPDATE_SENTENCE, new Object[] { row.getFechaRegistroAltamiraTs(), row.getCodigoSucursalOrigen(), row.getCodigoConcepto(), row.getCodigoTipoEvento(), row.getDescripcion(), row.getEstado(), row.getCodigo() });
	}

	@Override
	protected Object[] getDataToAdd(@SuppressWarnings("unused") Connection con, NotaContable row) throws Exception {
		return new Object[] { row.getCodigo(), DateUtils.getTimestamp(), null, row.getCodigoSucursalOrigen(), row.getCodigoConcepto(), row.getCodigoTipoEvento(), row.getNumeroRadicacion(), row.getTipoNota(), row.getDescripcion(),
				row.getAsientoContable(), row.getEstado() };
	}

	@Override
	public NotaContable getResultSetToVO(ResultSet result) throws Exception {
		NotaContable row = new NotaContable();

		row.setCodigo(result.getInt(1));
		row.setFechaRegistroModuloTs(result.getTimestamp(2));
		row.setFechaRegistroAltamiraTs(result.getTimestamp(3));
		row.setCodigoSucursalOrigen(result.getString(4));
		row.setCodigoConcepto(result.getInt(5));
		row.setCodigoTipoEvento(result.getInt(6));
		row.setNumeroRadicacion(result.getString(7));
		row.setTipoNota(result.getString(8));
		row.setDescripcion(result.getString(9));
		row.setEstado(result.getString(10));

		return row;
	}
}