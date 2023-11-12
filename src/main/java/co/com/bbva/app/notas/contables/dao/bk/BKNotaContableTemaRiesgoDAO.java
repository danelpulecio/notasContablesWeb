/*
	Nombre DTO: RiesgoOperacional
 */

package co.com.bbva.app.notas.contables.dao.bk;

import co.com.bbva.app.notas.contables.dto.RiesgoOperacional;

import java.sql.Connection;
import java.sql.ResultSet;

public class BKNotaContableTemaRiesgoDAO extends BKCommonDAO<RiesgoOperacional> {

	private static String cs_COLUMNAS = "CODIGO, CODIGO_NOTA_CONTABLE, CODIGO_TEMA, IMPORTE_PARCIAL, IMPORTE_TOTAL, FECHA_EVENTO, CODIGO_TIPO_PERDIDA, CODIGO_CLASE_RIESGO, FECHA_DESCUBRIMIENTO, CODIGO_PROCESO, CODIGO_LINEA_OPER, CODIGO_PRODUCTO";

	private static String cs_TABLA = "NC_BK_NOTA_CONT_TEMA_RIESGO";

	private static String cs_PK = "CODIGO";

	public BKNotaContableTemaRiesgoDAO() {
		super(cs_TABLA, cs_COLUMNAS, cs_PK, new RiesgoOperacional());
	}

	@Override
	protected Object[] getDataToAdd(@SuppressWarnings("unused") Connection con, RiesgoOperacional row) throws Exception {
		return new Object[] { row.getCodigo(), row.getCodigoNotaContable(), row.getCodigoTemaNotaContable(), row.getImporteParcial(), row.getImporteTotal(), row.getFechaEvento(), row.getCodigoTipoPerdida(), row.getCodigoClaseRiesgo(),
				row.getFechaDescubrimientoEvento(), row.getCodigoProceso(), row.getCodigoLineaOperativa(), row.getCodigoProducto() };
	}

	@Override
	public RiesgoOperacional getResultSetToVO(ResultSet result) throws Exception {
		RiesgoOperacional row = new RiesgoOperacional();

		row.setCodigo(result.getInt(1));
		row.setCodigoNotaContable(result.getInt(2));
		row.setCodigoTemaNotaContable(result.getInt(3));
		row.setImporteParcial(result.getDouble(4));
		row.setImporteTotal(result.getDouble(5));
		row.setFechaEvento(result.getDate(6));
		row.setCodigoTipoPerdida(result.getString(7));
		row.setCodigoClaseRiesgo(result.getString(8));
		row.setFechaDescubrimientoEvento(result.getDate(9));
		row.setCodigoProceso(result.getString(10));
		row.setCodigoLineaOperativa(result.getString(11));
		row.setCodigoProducto(result.getString(12));

		return row;
	}

	@Override
	protected void internalUpdate( @SuppressWarnings("unused") Connection con,@SuppressWarnings("unused") RiesgoOperacional row) throws Exception {
		throw new Exception("Mtodo no implementado");
	}
}