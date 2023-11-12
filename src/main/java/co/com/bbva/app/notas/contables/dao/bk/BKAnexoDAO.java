/*
	Nombre DTO: Anexo
 */

package co.com.bbva.app.notas.contables.dao.bk;

import co.com.bbva.app.notas.contables.dto.Anexo;

import java.sql.Connection;
import java.sql.ResultSet;

public class BKAnexoDAO extends BKCommonDAO<Anexo> {

	private static String cs_PK = "CODIGO";

	private static String cs_COLUMNAS = "CODIGO, FECHA_HORA, CODIGO_INSTANCIA, ESTADO_INSTANCIA, CODIGO_TEMA, CODIGO_USUARIO, NOMBRE, ARCHIVO, ESTADO";

	private static String cs_TABLA = "NC_BK_ANEXO";

	public BKAnexoDAO() {
		super(cs_TABLA, cs_COLUMNAS, cs_PK, new Anexo());
	}

	@Override
	protected Object[] getDataToAdd(@SuppressWarnings("unused") Connection con, Anexo row) throws Exception {
		return new Object[] { row.getCodigo(), row.getFechaHora(), row.getCodigoInstancia(), row.getEstadoInstancia(), row.getCodigoTema(), row.getCodigoUsuario(), row.getNombre(), row.getArchivo(), row.getEstado() };
	}

	@Override
	public Anexo getResultSetToVO(ResultSet result) throws Exception {
		Anexo row = new Anexo();

		row.setCodigo(result.getInt(1));
		row.setFechaHora(result.getTimestamp(2));
		row.setCodigoInstancia(result.getInt(3));
		row.setEstadoInstancia(result.getString(4));
		row.setCodigoTema(result.getInt(5));
		row.setCodigoUsuario(result.getInt(6));
		row.setNombre(result.getString(7));
		row.setArchivo(result.getString(8));
		row.setEstado(result.getString(9));

		return row;
	}

	@Override
	protected void internalUpdate( @SuppressWarnings("unused") Connection con,@SuppressWarnings("unused") Anexo row) throws Exception {
		throw new Exception("Mtodo no implementado");
	}

}