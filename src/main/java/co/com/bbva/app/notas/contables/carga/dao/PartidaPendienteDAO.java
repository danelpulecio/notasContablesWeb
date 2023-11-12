/*
	Nombre DTO: PartidaPendiente
 */

package co.com.bbva.app.notas.contables.carga.dao;

import co.com.bbva.app.notas.contables.carga.dto.PartidaPendiente;
import co.com.bbva.app.notas.contables.dao.CommonDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collection;

public class PartidaPendienteDAO extends CommonDAO<PartidaPendiente> {

	private static String cs_COLUMNAS = "CUENTA, DESCRIPCION, SUCURSAL_DESTINO, NATURALEZA, REFERENCIA_CRUCE, DIVISA, IMPORTE, CONCEPTO, FECHA_CONTABLE, ESTADO_CARGA, FECHA_ULTIMA_CARGA";

	private static String cs_TABLA = "NC_PARTIDA_PENDIENTE";

	private static String cs_PK = "CUENTA";

	private static String SQL_SELECT_ALL_SENTENCE = "SELECT " + cs_COLUMNAS + " FROM " + cs_TABLA + " ORDER BY CUENTA";

	private static String SQL_SELECT_BY_ESTADO_CARGA_SENTENCE = "SELECT " + cs_COLUMNAS + " FROM " + cs_TABLA + " WHERE (ESTADO_CARGA = ?)";

	private static String SQL_SELECT_BY_SUCURSAL_SENTENCE = "SELECT " + cs_COLUMNAS + " FROM " + cs_TABLA + " WHERE (SUCURSAL_DESTINO = ?) ORDER BY IMPORTE DESC";

	private static String SQL_SELECT_DISTINCT_CUENTA_BY_SUCURSAL_SENTENCE = "SELECT DISTINCT CUENTA FROM " + cs_TABLA + " WHERE (SUCURSAL_DESTINO = ?) AND  (USADA IS NULL OR USADA='N') ";

	private static String SQL_SELECT_BY_SENTENCE = "SELECT " + cs_COLUMNAS + " FROM " + cs_TABLA
			+ " WHERE (CUENTA LIKE ?) OR (DESCRIPCION LIKE ?) OR (SUCURSAL_DESTINO LIKE ?) OR (NATURALEZA LIKE ?) OR (REFERENCIA_CRUCE LIKE ?) OR (DIVISA LIKE ?) OR (IMPORTE LIKE ?) OR (CONCEPTO LIKE ?) ORDER BY CUENTA";

	private static String SQL_SELECT_BY_ALL_AND_SUCURSAL_SENTENCE = "SELECT "
			+ cs_COLUMNAS
			+ " FROM "
			+ cs_TABLA
			+ " WHERE (SUCURSAL_DESTINO LIKE ?) AND ((CUENTA LIKE ?) OR (DESCRIPCION LIKE ?) OR (NATURALEZA LIKE ?) OR (REFERENCIA_CRUCE LIKE ?) OR (DIVISA LIKE ?) OR (IMPORTE LIKE ?) OR (CONCEPTO LIKE ?)) AND (USADA IS NULL OR USADA='N') ORDER BY CUENTA";

	private static int NUMERO_COLUMNAS_BUSQUEDA = 8;

	private static String SQL_UPDATE_ESTADO_CARGA_SENTENCE = "UPDATE " + cs_TABLA + " SET ESTADO_CARGA = ?";

	private static String SQL_UPDATE_SENTENCE = "UPDATE " + cs_TABLA
			+ " SET DESCRIPCION = ?, SUCURSAL_DESTINO = ?, NATURALEZA = ?, REFERENCIA_CRUCE = ?, DIVISA = ?, IMPORTE = ?, CONCEPTO = ?, FECHA_CONTABLE = ?, ESTADO_CARGA = ?, FECHA_ULTIMA_CARGA = ? WHERE (CUENTA = ?)";

	private static String SQL_UPDATE_USADA = "UPDATE " + cs_TABLA + " SET USADA = ? WHERE " + " SUCURSAL_DESTINO = ? AND NATURALEZA = ? AND REFERENCIA_CRUCE = ? AND DIVISA = ? AND IMPORTE = ? AND CONCEPTO = ? AND FECHA_CONTABLE = ? AND (CUENTA = ?)";

	public PartidaPendienteDAO() {
		super(cs_TABLA, cs_COLUMNAS, cs_PK, new PartidaPendiente());
	}

	@Override
	public void internalUpdate(Connection con, PartidaPendiente row) throws Exception {
		executeUpdate(con, SQL_UPDATE_SENTENCE, new Object[] { row.getDescripcion(), row.getSucursalDestino(), row.getNaturaleza(), row.getReferenciaCruce(), row.getDivisa(), row.getImporte(), row.getConcepto(), row.getFechaContable(),
				row.getEstadoCarga(), row.getFechaUltimaCarga(), row.getCuenta() });
	}

	public void updateUsada(Connection con, PartidaPendiente row, int codUsuario) throws Exception {
		update(con, row, codUsuario, SQL_UPDATE_USADA, new Object[] { row.getUsada(), row.getSucursalDestino(), row.getNaturaleza(), row.getReferenciaCruce(), row.getDivisa(), row.getImporte(), row.getConcepto(), row.getFechaContable(),
				row.getCuenta() });
	}

	public void updateEstadoCarga(String estadoCarga) throws Exception {
		executeUpdate(SQL_UPDATE_ESTADO_CARGA_SENTENCE, estadoCarga);
	}

	public Collection<PartidaPendiente> findAll() throws Exception {
		return findByGeneral(SQL_SELECT_ALL_SENTENCE);
	}

	public Collection<PartidaPendiente> findByEstadoCarga(String estadoCarga) throws Exception {
		return findByGeneral(SQL_SELECT_BY_ESTADO_CARGA_SENTENCE, estadoCarga);
	}

	public Collection<PartidaPendiente> findBySucursal(String codigoSucursal) throws Exception {
		return findByGeneral(SQL_SELECT_BY_SUCURSAL_SENTENCE, codigoSucursal);
	}

	public Collection<PartidaPendiente> findCuentasBySucursal(String codigoSucursal) throws Exception {
		ArrayList<PartidaPendiente> rows = new ArrayList<PartidaPendiente>();
		PartidaPendiente row = new PartidaPendiente();

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet result = null;
		try {
			con = getConexion();
			ps = con.prepareStatement(SQL_SELECT_DISTINCT_CUENTA_BY_SUCURSAL_SENTENCE);
			ps.setString(1, codigoSucursal);
			result = ps.executeQuery();
			while (result.next()) {
				row = new PartidaPendiente();
				row.setCuenta(result.getString(1) != null ? result.getString(1) : "");
				rows.add(row);
			}
			return rows;
		} catch (Exception exception) {
			throw exception;
		} finally {
			if (result != null) {
				result.close();
			}
			if (ps != null) {
				ps.close();
			}
			closeConnection(con);
		}
	}

	public Collection<PartidaPendiente> findBy(String palabraClave) throws Exception {
		return findByKeyWord(SQL_SELECT_BY_SENTENCE, palabraClave, NUMERO_COLUMNAS_BUSQUEDA);
	}

	public Collection<PartidaPendiente> findByAllAndSucursal(String codigoSucursal, String palabraClave) throws Exception {
		ArrayList<Object> parms = new ArrayList<Object>();
		parms.add(codigoSucursal);
		for (int count = 2; count <= NUMERO_COLUMNAS_BUSQUEDA; count++) {
			parms.add("%" + palabraClave + "%");
		}

		return findByGeneral(SQL_SELECT_BY_ALL_AND_SUCURSAL_SENTENCE, parms.toArray());
	}

	@Override
	protected Object[] getDataToAdd(@SuppressWarnings("unused") Connection con, PartidaPendiente row) throws Exception {
		return new Object[] { row.getCuenta(), row.getDescripcion(), row.getSucursalDestino(), row.getNaturaleza(), row.getReferenciaCruce(), row.getDivisa(), row.getImporte(), row.getConcepto(), row.getFechaContable(), row.getEstadoCarga(),
				row.getFechaUltimaCarga() };
	}

	@Override
	public PartidaPendiente getResultSetToVO(ResultSet result) throws Exception {
		PartidaPendiente row = new PartidaPendiente();

		row.setCuenta(result.getString(1) != null ? result.getString(1) : "");
		row.setDescripcion(result.getString(2) != null ? result.getString(2) : "");
		row.setSucursalDestino(result.getString(3) != null ? result.getString(3) : "");
		row.setNaturaleza(result.getString(4) != null ? result.getString(4) : "");
		row.setReferenciaCruce(result.getString(5) != null ? result.getString(5) : "");
		row.setDivisa(result.getString(6) != null ? result.getString(6) : "");
		row.setImporte(result.getDouble(7));
		row.setConcepto(result.getString(8) != null ? result.getString(8) : "");
		row.setFechaContable(result.getTimestamp(9));
		row.setEstadoCarga(result.getString(10) != null ? result.getString(10) : "");
		row.setFechaUltimaCarga(result.getTimestamp(11));

		return row;
	}
}