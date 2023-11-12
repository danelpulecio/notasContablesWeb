package co.com.bbva.app.notas.contables.dao;

import co.com.bbva.app.notas.contables.dto.Observacion;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.Collection;

public class ObservacionDAO extends CommonSeqDAO<Observacion> {

	private static String cs_COLUMNAS = "CODIGO, FECHA_HORA, CODIGO_INSTANCIA, ESTADO_INSTANCIA, CODIGO_TEMA, CODIGO_USUARIO, NOTAS, ESTADO";

	private static String cs_TABLA = "NC_OBSERVACION";

	private static String cs_PK = "CODIGO";

	private static String SQL_SELECT_BY_CODIGO_TEMA_SENTENCE = "SELECT " + cs_COLUMNAS + " FROM " + cs_TABLA + " WHERE (CODIGO_TEMA = ?) ORDER BY CODIGO";
	private static String SQL_SELECT_BY_CODIGO_INSTANCIA_SENTENCE = "SELECT " + cs_COLUMNAS + " FROM " + cs_TABLA + " WHERE (CODIGO_INSTANCIA = ?) ORDER BY CODIGO";

	public ObservacionDAO() {
		super(cs_TABLA, cs_COLUMNAS, cs_PK, new Observacion());
	}

	public Collection<Observacion> findByCodigoTema(Observacion row) throws Exception {
		return findByGeneral(SQL_SELECT_BY_CODIGO_TEMA_SENTENCE, row.getCodigoTema());
	}

	@Override
	protected Object[] getDataToAdd(Connection con, Observacion row) throws Exception {
		row.setCodigo(getMaxCode(con));
		return new Object[] { row.getCodigo(), row.getFechaHora(), row.getCodigoInstancia(), row.getEstadoInstancia(), row.getCodigoTema(), row.getCodigoUsuario(), row.getNotas(), row.getEstado() };
	}

	@Override
	public Observacion getResultSetToVO(ResultSet result) throws Exception {
		Observacion row = new Observacion();

		row.setCodigo(result.getInt(1));
		row.setFechaHora(result.getTimestamp(2));
		row.setCodigoInstancia(result.getInt(3));
		row.setEstadoInstancia(result.getString(4));
		row.setCodigoTema(result.getInt(5));
		row.setCodigoUsuario(result.getInt(6));
		row.setNotas(result.getString(7));
		row.setEstado(result.getString(8));

		return row;
	}

	@Override
	protected void internalUpdate(@SuppressWarnings("unused") Connection con, @SuppressWarnings("unused") Observacion row) throws Exception {
		throw new Exception("Mtodo no implementado");
	}

	public Collection<Observacion> findByCodigoInstancia(int codInstancia) throws Exception {
		return findByGeneral(SQL_SELECT_BY_CODIGO_INSTANCIA_SENTENCE, codInstancia);
	}
}