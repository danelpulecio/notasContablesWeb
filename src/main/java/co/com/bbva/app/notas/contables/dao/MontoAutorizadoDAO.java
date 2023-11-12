package co.com.bbva.app.notas.contables.dao;

import co.com.bbva.app.notas.contables.dto.MontoAutorizado;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.Collection;

public class MontoAutorizadoDAO extends CommonSeqDAO<MontoAutorizado> {

	private static String cs_COLUMNAS = "CODIGO, CODIGO_TIPO_EVENTO, CODIGO_ENTE_AUTORIZADOR, CODIGO_TEMA_AUTORIZACION, MONTO, ESTADO";

	private static String cs_TABLA = "NC_MONTO_AUTORIZADO";

	private static String cs_PK = "CODIGO";

	private static String SQL_SELECT_ALL_SENTENCE = "select ma.codigo \"codigo\", " + //
			"ma.codigo_tipo_evento \"codigoTipoAutorizacion\", " + //
			"ma.codigo_ente_autorizador \"codigoEnteAutorizador\", " + //
			"ma.codigo_tema_autorizacion \"codigoTemaAutorizacion\", " + //
			"ma.monto \"monto\", " + //
			"ma.estado \"estado\", " + //
			"te.nombre \"nombreTipoEvento\", " + //
			"s.codigo \"codigoSucursal\", " + //
			"s.nombre \"nombreSucursal\", " + //
			"u.codigo_empleado \"codigoEmpleado\", " + //
			"ta.nombre \"nombreTeamAut\" " + //
			"from nc_monto_autorizado ma " + //
			"left join nc_tipo_evento te on ma.codigo_tipo_evento=te.codigo " + //
			"left join nc_ente_autorizador ea on ma.codigo_ente_autorizador=ea.codigo " + //
			"left join nc_sucursal s on ea.codigo_sucursal=s.codigo " + //
			"left join nc_tema_autorizacion ta on ma.codigo_tema_autorizacion=ta.codigo " + //
			"left join nc_usuario u on ea.codigo_usuario=u.codigo ";//

	private static String SQL_SELECT_BY_ESTADO_SENTENCE = "SELECT " + cs_COLUMNAS + " FROM " + cs_TABLA + " WHERE (ESTADO = ?) ORDER BY CODIGO_TIPO_EVENTO";

	private static String SQL_SELECT_BY_TEMA_AUTORIZACION_AND_TIPO_EVENTO_AND_ESTADO_SENTENCE = "SELECT " + cs_COLUMNAS + " FROM " + cs_TABLA + " WHERE (CODIGO_TEMA_AUTORIZACION = ?) AND (CODIGO_TIPO_EVENTO = ?) AND (ESTADO = ?) ORDER BY MONTO";

	private static String SQL_SELECT_BY_SENTENCE = SQL_SELECT_ALL_SENTENCE + " WHERE (te.NOMBRE LIKE ?) OR (ea.CODIGO_SUCURSAL LIKE ?) OR (s.NOMBRE LIKE ?) OR (ta.NOMBRE LIKE ?) ";

	private static String SQL_UPDATE_SENTENCE = "UPDATE " + cs_TABLA + " SET CODIGO_TIPO_EVENTO = ?, CODIGO_ENTE_AUTORIZADOR = ?, CODIGO_TEMA_AUTORIZACION = ?, MONTO = ?, ESTADO = ? WHERE (CODIGO = ?)";

	public MontoAutorizadoDAO() {
		super(cs_TABLA, cs_COLUMNAS, cs_PK, new MontoAutorizado());
	}

	@Override
	public void internalUpdate(Connection con, MontoAutorizado row) throws Exception {
		executeUpdate(con, SQL_UPDATE_SENTENCE, new Object[] { row.getCodigoTipoAutorizacion(), row.getCodigoEnteAutorizador(), row.getCodigoTemaAutorizacion(), row.getMonto(), row.getEstado(), row.getCodigo() });
	}

	public Collection<MontoAutorizado> findAll() throws Exception {
		return obtenerLista(SQL_SELECT_ALL_SENTENCE);
	}

	public Collection<MontoAutorizado> findByEstado(MontoAutorizado row) throws Exception {
		return findByGeneral(SQL_SELECT_BY_ESTADO_SENTENCE, row.getEstado());
	}

	public MontoAutorizado findByTemaAutorizacionAndTipoEventoNotaContableAndEstado(MontoAutorizado row) throws Exception {
		return getByGeneral(SQL_SELECT_BY_TEMA_AUTORIZACION_AND_TIPO_EVENTO_AND_ESTADO_SENTENCE, new Object[] { row.getCodigoTemaAutorizacion(), row.getCodigoTipoAutorizacion(), row.getEstado() });
	}

	public Collection<MontoAutorizado> findBy(String palabraClave) throws Exception {
		String param = COMODIN + palabraClave + COMODIN;
		return obtenerLista(SQL_SELECT_BY_SENTENCE, new Object[] { param, param, param, param });
	}

	@Override
	protected Object[] getDataToAdd(Connection con, MontoAutorizado row) throws Exception {
		row.setCodigo(getMaxCode(con));
		return new Object[] { row.getCodigo(), row.getCodigoTipoAutorizacion(), row.getCodigoEnteAutorizador(), row.getCodigoTemaAutorizacion(), row.getMonto(), row.getEstado() };
	}

	@Override
	public MontoAutorizado getResultSetToVO(ResultSet result) throws Exception {
		MontoAutorizado row = new MontoAutorizado();

		row.setCodigo(result.getInt(1));
		row.setCodigoTipoAutorizacion(result.getInt(2));
		row.setCodigoEnteAutorizador(result.getInt(3));
		row.setCodigoTemaAutorizacion(result.getInt(4));
		row.setMonto(result.getDouble(5));
		row.setEstado(result.getString(6));

		return row;
	}
}