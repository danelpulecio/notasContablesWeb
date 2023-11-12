/*
	Nombre DTO: NotaContableTemaImpuesto
 */

package co.com.bbva.app.notas.contables.dao.bk;

import co.com.bbva.app.notas.contables.dto.NotaContableTemaImpuesto;

import java.sql.Connection;
import java.sql.ResultSet;

public class BKNotaContableTemaImpuestoDAO extends BKCommonDAO<NotaContableTemaImpuesto> {

	private static String cs_COLUMNAS = "CODIGO, CODIGO_NOTA_CONTABLE, CODIGO_TEMA, CODIGO_IMPUESTO, BASE, CALCULADO, EXONERA, NUMERO_ASIENTO, NUMERO_APUNTE";

	private static String cs_TABLA = "NC_BK_NOTA_CONT_TEMA_IMPUESTO";

	private static String cs_PK = "CODIGO";

	private static String SQL_UPDATE_SENTENCE = "UPDATE " + cs_TABLA + " SET NUMERO_ASIENTO = ?, NUMERO_APUNTE = ? WHERE (CODIGO = ?)";

	public BKNotaContableTemaImpuestoDAO() {
		super(cs_TABLA, cs_COLUMNAS, cs_PK, new NotaContableTemaImpuesto());
	}

	@Override
	public void internalUpdate( Connection con,NotaContableTemaImpuesto row) throws Exception {
		executeUpdate(con, SQL_UPDATE_SENTENCE, new Object[] { row.getNumeroAsiento(), row.getNumeroApunte(), row.getCodigo() });
	}

	@Override
	protected Object[] getDataToAdd(@SuppressWarnings("unused") Connection con, NotaContableTemaImpuesto row) throws Exception {
		return new Object[] { row.getCodigo(), row.getCodigoNotaContable(), row.getCodigoTemaNotaContable(), row.getCodigoImpuesto(), row.getBase(), row.getCalculado(), row.getExonera(), row.getNumeroAsiento(), row.getNumeroApunte() };
	}

	@Override
	public NotaContableTemaImpuesto getResultSetToVO(ResultSet result) throws Exception {
		NotaContableTemaImpuesto row = new NotaContableTemaImpuesto();

		row.setCodigo(result.getInt(1));
		row.setCodigoNotaContable(result.getInt(2));
		row.setCodigoTemaNotaContable(result.getInt(3));
		row.setCodigoImpuesto(result.getInt(4));
		row.setBase(result.getDouble(5));
		row.setCalculado(result.getDouble(6));
		row.setExonera(result.getString(7));
		row.setNumeroAsiento(result.getString(8) != null ? result.getString(8) : "");
		row.setNumeroApunte(result.getString(9) != null ? result.getString(9) : "");

		return row;
	}

	// public void updateAsientoContra(NotaContableTemaImpuesto registro) throws Exception {
	// throw new Exception("updateAsientoContra: no implementado an");
	// }
}