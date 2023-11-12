package co.com.bbva.app.notas.contables.dao;

import co.com.bbva.app.notas.contables.dto.FechaHabilitada;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.Collection;

public class FechaHabilitadaDAO extends CommonSeqDAO<FechaHabilitada> {

	private static String cs_COLUMNAS = "CODIGO, CODIGO_SUCURSAL, DIAS";

	private static String cs_TABLA = "NC_FECHA_HABILITADA";

	private static String cs_PK = "CODIGO";

	private static String ORDER_BY = " ORDER BY CODIGO_SUCURSAL";

	private static String SQL_SELECT_ALL_SENTENCE = "select f.codigo \"codigo\", " + //
			"f.codigo_sucursal \"codigoSucursal\", " + //
			"f.dias \"dias\", " + //
			"s.nombre \"nombreSucursal\" " + //
			"from nc_fecha_habilitada f left join nc_sucursal s on f.codigo_sucursal=s.codigo ";

	private static String SQL_SELECT_BY_CODIGO_SUCURSAL_SENTENCE = "SELECT " + cs_COLUMNAS + " FROM " + cs_TABLA + " WHERE (CODIGO_SUCURSAL = ?)";

	private static String SQL_SELECT_BY_SENTENCE = SQL_SELECT_ALL_SENTENCE + " WHERE (f.CODIGO_SUCURSAL LIKE ?) OR (f.DIAS = ?) OR (s.NOMBRE LIKE ?)" + ORDER_BY;

	private static String SQL_UPDATE_SENTENCE = "UPDATE " + cs_TABLA + " SET CODIGO_SUCURSAL = ?, DIAS = ? WHERE (CODIGO = ?)";

	private static String SQL_UPDATE_ALL_SENTENCE = "UPDATE " + cs_TABLA + " SET DIAS = ?";

	public FechaHabilitadaDAO() {
		super(cs_TABLA, cs_COLUMNAS, cs_PK, new FechaHabilitada());
	}

	@Override
	public void internalUpdate(Connection con, FechaHabilitada row) throws Exception {
		executeUpdate(con, SQL_UPDATE_SENTENCE, new Object[] { row.getCodigoSucursal(), row.getDias(), row.getCodigo() });
	}

	public void updateAll(int dias, int codigoUsuario) throws Exception {
		Connection con = null;
		try {
			con = getConexion(false);
			executeUpdate(con, SQL_UPDATE_ALL_SENTENCE, dias);
			// BLOQUEO BASE DE DATOS
			//addRegistroAuditoria(con, codigoUsuario, ACCION_EDITAR_TODAS, FechaHabilitada.class.getSimpleName(), "");
			con.commit();
		} catch (Exception e) {
			con.rollback();
			throw e;
		} finally {
			closeConnection(con);
		}
	}

	public void updateAllSelected(String sucursales, int dias, int codigoUsuario) throws Exception {
		Connection con = null;
		try {
			con = getConexion(false);
			executeUpdate(con, "UPDATE " + cs_TABLA + " SET DIAS = " + dias + " WHERE (CODIGO_SUCURSAL IN (" + sucursales + "))");
			// BLOQUEO BASE DE DATOS
			//addRegistroAuditoria(con, codigoUsuario, ACCION_EDITAR_SELECCIONADAS, FechaHabilitada.class.getSimpleName(), "");
			con.commit();
		} catch (Exception e) {
			con.rollback();
			throw e;
		} finally {
			closeConnection(con);
		}
	}

	public FechaHabilitada findByCodigoSucursal(FechaHabilitada row) throws Exception {
		return getByGeneral(SQL_SELECT_BY_CODIGO_SUCURSAL_SENTENCE, row.getCodigoSucursal());
	}

	public Collection<FechaHabilitada> findAll() throws Exception {
		return obtenerLista(SQL_SELECT_ALL_SENTENCE + ORDER_BY);
	}

	public Collection<FechaHabilitada> findBy(String palabraClave) throws Exception {
		String param = COMODIN + palabraClave + COMODIN;
		int dias = -1;
		try {
			dias = Integer.valueOf(palabraClave);
		} catch (Exception e) {
		}
		return obtenerLista(SQL_SELECT_BY_SENTENCE, new Object[] { param, dias, param });
	}

	@Override
	protected Object[] getDataToAdd(Connection con, FechaHabilitada row) throws Exception {
		row.setCodigo(getMaxCode(con));
		return new Object[] { row.getCodigo(), row.getCodigoSucursal(), row.getDias() };
	}

	@Override
	public FechaHabilitada getResultSetToVO(ResultSet result) throws Exception {
		FechaHabilitada row = new FechaHabilitada();

		row.setCodigo(result.getInt(1));
		row.setCodigoSucursal(result.getString(2));
		row.setDias(result.getInt(3));

		return row;
	}
}