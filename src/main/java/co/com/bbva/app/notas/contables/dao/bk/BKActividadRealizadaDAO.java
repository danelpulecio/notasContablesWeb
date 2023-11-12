/*
	Nombre DTO: ActividadRealizada
 */

package co.com.bbva.app.notas.contables.dao.bk;

import co.com.bbva.app.notas.contables.dto.ActividadRealizada;

import java.sql.Connection;
import java.sql.ResultSet;

public class BKActividadRealizadaDAO extends BKCommonDAO<ActividadRealizada> {

	private static final String cs_PK = "CODIGO";

	private static final String cs_COLUMNAS = "CODIGO, FECHA_HORA, CODIGO_INSTANCIA, ESTADO, CODIGO_USUARIO, VALOR1, VALOR2, VALOR3, VALOR4, VALOR5";

	private static final String cs_TABLA = "NC_BK_ACTIVIDAD_REALIZADA";

	public BKActividadRealizadaDAO() {
		super(cs_TABLA, cs_COLUMNAS, cs_PK, new ActividadRealizada());
	}

	@Override
	protected Object[] getDataToAdd(@SuppressWarnings("unused") Connection con, ActividadRealizada row) throws Exception {
		return new Object[] { row.getCodigo().intValue(), row.getFechaHora(), row.getCodigoInstancia().intValue(), row.getEstado(), row.getCodigoUsuario().intValue(), row.getValor1(), row.getValor2(), row.getValor3(), row.getValor4(),
				row.getValor5() };
	}

	@Override
	public ActividadRealizada getResultSetToVO(ResultSet result) throws Exception {
		ActividadRealizada row = new ActividadRealizada();

		row.setCodigo(result.getInt(1));
		row.setFechaHoraTs(result.getTimestamp(2));
		row.setCodigoInstancia(result.getInt(3));
		row.setEstado(result.getString(4));
		row.setCodigoUsuario(result.getInt(5));
		row.setValor1(result.getString(6) != null ? result.getString(6) : "");
		row.setValor2(result.getString(7) != null ? result.getString(7) : "");
		row.setValor3(result.getString(8) != null ? result.getString(8) : "");
		row.setValor4(result.getString(9) != null ? result.getString(9) : "");
		row.setValor5(result.getString(10) != null ? result.getString(10) : "");

		return row;
	}

	@Override
	protected void internalUpdate( @SuppressWarnings("unused") Connection con,@SuppressWarnings("unused") ActividadRealizada row) throws Exception {
		throw new Exception("Mtodo no implementado");
	}

}