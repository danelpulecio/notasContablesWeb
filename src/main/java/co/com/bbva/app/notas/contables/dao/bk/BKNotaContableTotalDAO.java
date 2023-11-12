/*
	Nombre DTO: NotaContableTotal
 */

package co.com.bbva.app.notas.contables.dao.bk;

import co.com.bbva.app.notas.contables.dto.NotaContableTotal;

import java.sql.Connection;
import java.sql.ResultSet;

public class BKNotaContableTotalDAO extends BKCommonDAO<NotaContableTotal> {

	private static String cs_COLUMNAS = "CODIGO, CODIGO_NOTA_CONTABLE, CODIGO_DIVISA, TOTAL";

	private static String cs_TABLA = "NC_BK_NOTA_CONTABLE_TOTAL";

	private static String cs_PK = "CODIGO";

	public BKNotaContableTotalDAO() {
		super(cs_TABLA, cs_COLUMNAS, cs_PK, new NotaContableTotal());
	}

	@Override
	protected Object[] getDataToAdd(@SuppressWarnings("unused") Connection con, NotaContableTotal row) throws Exception {
		return new Object[] { row.getCodigo(), row.getCodigoNotaContable(), row.getCodigoDivisa(), row.getTotal() };
	}

	@Override
	public NotaContableTotal getResultSetToVO(ResultSet result) throws Exception {
		NotaContableTotal row = new NotaContableTotal();

		row.setCodigo(result.getInt(1));
		row.setCodigoNotaContable(result.getInt(2));
		row.setCodigoDivisa(result.getString(3));
		row.setTotal(result.getDouble(4));

		return row;
	}

	@Override
	protected void internalUpdate( @SuppressWarnings("unused") Connection con,@SuppressWarnings("unused") NotaContableTotal row) throws Exception {
		throw new Exception("Mtodo no implementado");
	}
}