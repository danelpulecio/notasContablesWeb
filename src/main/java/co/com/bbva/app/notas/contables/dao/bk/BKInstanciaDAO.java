package co.com.bbva.app.notas.contables.dao.bk;

import co.com.bbva.app.notas.contables.dto.Instancia;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.Collection;

public class BKInstanciaDAO extends BKCommonDAO<Instancia> {

	private static String cs_COLUMNAS = "CODIGO, FECHA_HORA_INICIO, CODIGO_NOTA_CONTABLE, CODIGO_SUCURSAL_ORIGEN, CODIGO_USUARIO_ACTUAL, ULTIMA_ACTUALIZACION, ESTADO";

	private static String cs_TABLA = "NC_BK_INSTANCIA";

	private static String cs_PK = "CODIGO";

	private static String SQL_UPDATE_SENTENCE = "UPDATE " + cs_TABLA + " SET CODIGO_SUCURSAL_ORIGEN = ?, CODIGO_USUARIO_ACTUAL = ?, ULTIMA_ACTUALIZACION = ?, ESTADO = ? WHERE (CODIGO = ?)";

	public BKInstanciaDAO() {
		super(cs_TABLA, cs_COLUMNAS, cs_PK, new Instancia());
	}

	@Override
	public void internalUpdate( Connection con,Instancia row) throws Exception {
		executeUpdate(con, SQL_UPDATE_SENTENCE, new Object[] { row.getCodigoSucursalOrigen(), row.getCodigoUsuarioActual().intValue(), row.getUltimaActualizacion(), row.getEstado(), row.getCodigo().intValue() });
	}

	public Collection<Instancia> findAll() throws Exception {
		return findByGeneral(SQL_SELECT_ALL_SENTENCE);
	}

	@Override
	protected Object[] getDataToAdd(@SuppressWarnings("unused") Connection con, Instancia row) throws Exception {
		return new Object[] { row.getCodigo().intValue(), row.getFechaHoraInicio(), row.getCodigoNotaContable().intValue(), row.getCodigoSucursalOrigen(), row.getCodigoUsuarioActual().intValue(), row.getUltimaActualizacion(), row.getEstado() };
	}

	@Override
	public Instancia getResultSetToVO(ResultSet result) throws Exception {
		Instancia row = new Instancia();

		row.setCodigo(result.getInt(1));
		row.setFechaHoraInicioTs(result.getTimestamp(2));
		row.setCodigoNotaContable(result.getInt(3));
		row.setCodigoSucursalOrigen(result.getString(4));
		row.setCodigoUsuarioActual(result.getInt(5));
		row.setUltimaActualizacionTs(result.getTimestamp(6));
		row.setEstado(result.getString(7));

		return row;
	}

}