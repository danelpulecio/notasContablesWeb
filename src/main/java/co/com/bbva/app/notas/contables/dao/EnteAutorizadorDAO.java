package co.com.bbva.app.notas.contables.dao;

import co.com.bbva.app.notas.contables.dto.EnteAutorizador;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.Collection;
import java.util.Map;

public class EnteAutorizadorDAO extends CommonSeqDAO<EnteAutorizador> {

	private static String cs_COLUMNAS = "CODIGO, CODIGO_SUCURSAL, CODIGO_USUARIO, ESTADO";

	private static String cs_TABLA = "NC_ENTE_AUTORIZADOR";

	private static String cs_PK = "CODIGO";

	private static String SQL_SELECT_ALL_SENTENCE = "select ea.codigo \"codigo\", " + //
			"ea.codigo_sucursal \"codigoSucursal\", " + //
			"ea.codigo_usuario \"codigoUsuarioModulo\", " + //
			"ua.codigo_empleado \"codigoEmpleado\", " + //
			"ea.estado \"estado\", " + //
			"ua.nombre_empleado \"nombreUsuario\", " + //
			"s.nombre \"nombreSucursal\" " + //
			"from nc_ente_autorizador ea " + //
			"left join nc_sucursal s on ea.codigo_sucursal=s.codigo " + //
			"left join nc_usuario u on ea.codigo_usuario=u.codigo " + //
			"left join nc_usuario_altamira ua on u.codigo_empleado=ua.codigo_empleado ";//

	private static String SQL_SELECT_CV_SENTENCE = "select ea.codigo CLAVE, " + //
			"ea.codigo_sucursal || ' - ' || s.nombre || ' - ' || ua.codigo_empleado VALOR " + //
			"from nc_ente_autorizador ea " + //
			"left join nc_sucursal s on ea.codigo_sucursal=s.codigo " + //
			"left join nc_usuario u on ea.codigo_usuario=u.codigo " + //
			"left join nc_usuario_altamira ua on u.codigo_empleado=ua.codigo_empleado " + //
			"WHERE ea.estado = 'A' order by ea.codigo_sucursal || ' - ' || s.nombre || ' - ' || ua.codigo_empleado";//

	private static String SQL_SELECT_BY_ESTADO_SENTENCE = "SELECT " + cs_COLUMNAS + " FROM " + cs_TABLA + " WHERE (ESTADO = ?) ORDER BY CODIGO_SUCURSAL";

	private static String SQL_SELECT_BY_CODIGO_SUCURSAL_AND_ESTADO_SENTENCE = "SELECT " + cs_COLUMNAS + " FROM " + cs_TABLA + " WHERE (CODIGO_SUCURSAL = ?) AND (ESTADO = ?) ORDER BY CODIGO_SUCURSAL";

	private static String SQL_SELECT_BY_CODIGO_USUARIO_SENTENCE = "SELECT " + cs_COLUMNAS + " FROM " + cs_TABLA + " WHERE (CODIGO_USUARIO = ?)";

	private static String SQL_SELECT_BY_SENTENCE = SQL_SELECT_ALL_SENTENCE + " WHERE (ea.CODIGO_SUCURSAL LIKE ?) OR (s.NOMBRE LIKE ?) OR (ua.CODIGO_EMPLEADO LIKE ?) OR (ua.NOMBRE_EMPLEADO LIKE ?) ORDER BY ea.CODIGO_SUCURSAL";

	private static String SQL_UPDATE_SENTENCE = "UPDATE " + cs_TABLA + " SET CODIGO_SUCURSAL = ?, CODIGO_USUARIO = ?, ESTADO = ? WHERE (CODIGO = ?)";

	public EnteAutorizadorDAO() {
		super(cs_TABLA, cs_COLUMNAS, cs_PK, new EnteAutorizador());
	}

	@Override
	public void internalUpdate(Connection con, EnteAutorizador row) throws Exception {
		executeUpdate(con, SQL_UPDATE_SENTENCE, new Object[] { row.getCodigoSucursal(), row.getCodigoUsuarioModulo(), row.getEstado(), row.getCodigo() });
	}

	public EnteAutorizador findByCodigoSucursalAndEstado(EnteAutorizador row) throws Exception {
		return getByGeneral(SQL_SELECT_BY_CODIGO_SUCURSAL_AND_ESTADO_SENTENCE, new Object[] { row.getCodigoSucursal(), row.getEstado() });
	}

	public EnteAutorizador findByCodigoUsuario(EnteAutorizador row) throws Exception {
		return getByGeneral(SQL_SELECT_BY_CODIGO_USUARIO_SENTENCE, row.getCodigoUsuarioModulo());
	}

	public EnteAutorizador findByCodigoUsuario(Connection con, EnteAutorizador row) throws Exception {
		return getByGeneral(con, SQL_SELECT_BY_CODIGO_USUARIO_SENTENCE, row.getCodigoUsuarioModulo());
	}

	public Collection<EnteAutorizador> findAll() throws Exception {
		return obtenerLista(SQL_SELECT_ALL_SENTENCE);
	}

	public Map<String, String> getCVEntesAut() throws Exception {
		return getCV(SQL_SELECT_CV_SENTENCE);
	}

	public Collection<EnteAutorizador> findByEstado(EnteAutorizador row) throws Exception {
		return findByGeneral(SQL_SELECT_BY_ESTADO_SENTENCE, row.getEstado());
	}

	public Collection<EnteAutorizador> findBy(String palabraClave) throws Exception {
		String param = COMODIN + palabraClave + COMODIN;
		return obtenerLista(SQL_SELECT_BY_SENTENCE, new Object[] { param, param, param, param });
	}

	@Override
	protected Object[] getDataToAdd(Connection con, EnteAutorizador row) throws Exception {
		row.setCodigo(getMaxCode(con));
		return new Object[] { row.getCodigo(), row.getCodigoSucursal(), row.getCodigoUsuarioModulo(), row.getEstado() };
	}

	@Override
	public EnteAutorizador getResultSetToVO(ResultSet result) throws Exception {
		EnteAutorizador row = new EnteAutorizador();

		row.setCodigo(result.getInt(1));
		row.setCodigoSucursal(result.getString(2));
		row.setCodigoUsuarioModulo(result.getInt(3));
		row.setEstado(result.getString(4));

		return row;
	}
}