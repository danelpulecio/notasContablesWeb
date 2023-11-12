package co.com.bbva.app.notas.contables.dao;

import co.com.bbva.app.notas.contables.dto.MontoAutorizadoGeneral;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collection;

public class MontoAutorizadoGeneralDAO extends CommonSeqDAO<MontoAutorizadoGeneral> {

	private static String cs_COLUMNAS = "CODIGO, CODIGO_TIPO_EVENTO, CODIGO_ROL, CODIGO_TEMA_AUTORIZACION, MONTO, ESTADO";

	private static String cs_TABLA = "NC_MONTO_AUT_GENERAL";

	private static String cs_PK = "CODIGO";
	private static String ORDER_BY = " ORDER BY CODIGO_TIPO_EVENTO, CODIGO_TEMA_AUTORIZACION, MONTO";

	private static String SQL_SELECT_ALL_SENTENCE = "select ma.codigo \"codigo\", " + //
			"ma.codigo_tipo_evento \"codigoTipoAutorizacion\", " + //
			"ma.codigo_rol \"codigoRol\", " + //
			"ma.codigo_tema_autorizacion \"codigoTemaAutorizacion\", " + //
			"ma.monto \"monto\", " + //
			"ma.estado \"estado\", " + //
			"te.nombre \"nombreTipoEvento\", " + //
			"r.nombre \"nombreRol\", " + //
			"ta.nombre \"nombreTeamAut\" " + //
			"from nc_monto_aut_general ma " + //
			"left join nc_tipo_evento te on ma.codigo_tipo_evento=te.codigo " + //
			"left join nc_rol r on ma.codigo_rol=r.codigo " + //
			"left join nc_tema_autorizacion ta on ma.codigo_tema_autorizacion=ta.codigo ";

	private static String SQL_SELECT_BY_ESTADO_SENTENCE = "SELECT " + cs_COLUMNAS + " FROM " + cs_TABLA + " WHERE (ESTADO = ?) ORDER BY CODIGO_TIPO_EVENTO, CODIGO_TEMA_AUTORIZACION";

	private static String SQL_SELECT_BY_TEMA_AUTORIZACION_AND_TIPO_EVENTO_AND_ESTADO_SENTENCE_MONTO = "SELECT " + cs_COLUMNAS + " FROM " + cs_TABLA
			+ " WHERE (CODIGO_TEMA_AUTORIZACION = ?) AND (CODIGO_TIPO_EVENTO = ?) AND (ESTADO = ?) AND MONTO >= ? ORDER BY MONTO";
	
	private static String SQL_SELECT_BY_TEMA_AUTORIZACION_AND_TIPO_EVENTO_AND_ESTADO_SENTENCE = "SELECT " + cs_COLUMNAS + " FROM " + cs_TABLA + " WHERE (CODIGO_TEMA_AUTORIZACION = ?) AND (CODIGO_TIPO_EVENTO = ?) AND (ESTADO = ?) ORDER BY MONTO";

	private static String SQL_SELECT_BY_SENTENCE = SQL_SELECT_ALL_SENTENCE + " WHERE (te.NOMBRE LIKE ?) OR (r.NOMBRE LIKE ?) OR (ta.NOMBRE LIKE ?)";

	private static String SQL_UPDATE_SENTENCE = "UPDATE " + cs_TABLA + " SET CODIGO_TIPO_EVENTO = ?, CODIGO_ROL = ?, CODIGO_TEMA_AUTORIZACION = ?, MONTO = ?, ESTADO = ? WHERE (CODIGO = ?)";

	public MontoAutorizadoGeneralDAO() {
		super(cs_TABLA, cs_COLUMNAS, cs_PK, new MontoAutorizadoGeneral());
	}

	@Override
	public void internalUpdate(Connection con, MontoAutorizadoGeneral row) throws Exception {
		executeUpdate(con, SQL_UPDATE_SENTENCE, new Object[] { row.getCodigoTipoAutorizacion(), row.getCodigoRol(), row.getCodigoTemaAutorizacion(), row.getMonto(), row.getEstado(), row.getCodigo() });
	}

	public Collection<MontoAutorizadoGeneral> findAll() throws Exception {
		return obtenerLista(SQL_SELECT_ALL_SENTENCE + ORDER_BY);
	}

	public Collection<MontoAutorizadoGeneral> findByEstado(MontoAutorizadoGeneral row) throws Exception {
		return findByGeneral(SQL_SELECT_BY_ESTADO_SENTENCE, row.getEstado());
	}

	public MontoAutorizadoGeneral findByTemaAutorizacionAndTipoEventoNotaContableAndEstado(MontoAutorizadoGeneral row, double montoNotaContable) throws Exception {
		return getByGeneral(SQL_SELECT_BY_TEMA_AUTORIZACION_AND_TIPO_EVENTO_AND_ESTADO_SENTENCE_MONTO, new Object[] { row.getCodigoTemaAutorizacion(), row.getCodigoTipoAutorizacion(), row.getEstado(), montoNotaContable });
	}

	/**COL514313I001449 MODIFICACION PARA RETORNAR LA POSICION DEL MONTO AUTORIZADO GENERAL SEGUN VALOR DE LA NOTA ****/
	public int getPositionByTemaAutorizacionAndTipoEventoNotaContableAndEstado(MontoAutorizadoGeneral row, double montoNotaContable) throws Exception {
		MontoAutorizadoGeneral montoAutorizado = new MontoAutorizadoGeneral();
		int pos = -1;
		int count = 0;
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet result = null;
		try {
			con = getConexion(false);
			ps = con.prepareStatement(SQL_SELECT_BY_TEMA_AUTORIZACION_AND_TIPO_EVENTO_AND_ESTADO_SENTENCE);
			ps.setInt(1, row.getCodigoTemaAutorizacion().intValue());
			ps.setInt(2, row.getCodigoTipoAutorizacion().intValue());
			ps.setString(3, row.getEstado());
			result = ps.executeQuery();
			while (result.next()) {
				montoAutorizado = getResultSetToVO(result);
				if (montoAutorizado.getMonto().doubleValue() >= montoNotaContable) {
						count++;
						pos++;
					break;
				}
				pos++;
			}
			return pos;
		} catch (Exception exception) {
			throw exception;
		} finally {
			if (result != null) {
				result.close();
			}
			if (ps != null) {
				ps.close();
			}
		}
	}
	/**COL514313I001449 CARGA UN  ARRAYLIST Y RETORNA TODOS LOS ELEMENTOS DE LA CONSULTA ****/
	public ArrayList<Object> posicion(MontoAutorizadoGeneral row, double montoNotaContable) throws Exception {
		ArrayList<Object> posicionElemento = new ArrayList<Object>();
		MontoAutorizadoGeneral montoAutorizado = new MontoAutorizadoGeneral();
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet result = null;
		try {
			con = getConexion(false);
			ps = con.prepareStatement(SQL_SELECT_BY_TEMA_AUTORIZACION_AND_TIPO_EVENTO_AND_ESTADO_SENTENCE);
			ps.setInt(1, row.getCodigoTemaAutorizacion().intValue());
			ps.setInt(2, row.getCodigoTipoAutorizacion().intValue());
			ps.setString(3, row.getEstado());
			result = ps.executeQuery();
			while (result.next()) {
				montoAutorizado = getResultSetToVO(result);
				posicionElemento.add(montoAutorizado);
			}
			return posicionElemento;
		} catch (Exception exception) {
			throw exception;
		} finally {
			if (result != null) {
				result.close();
			}
			if (ps != null) {
				ps.close();
			}
		}
	}
	
	public Collection<MontoAutorizadoGeneral> findBy(String palabraClave) throws Exception {
		String param = COMODIN + palabraClave + COMODIN;
		return obtenerLista(SQL_SELECT_BY_SENTENCE, new Object[] { param, param, param });
	}

	@Override
	protected Object[] getDataToAdd(Connection con, MontoAutorizadoGeneral row) throws Exception {
		row.setCodigo(getMaxCode(con));
		return new Object[] { row.getCodigo(), row.getCodigoTipoAutorizacion(), row.getCodigoRol(), row.getCodigoTemaAutorizacion(), row.getMonto(), row.getEstado() };
	}

	@Override
	public MontoAutorizadoGeneral getResultSetToVO(ResultSet result) throws Exception {
		MontoAutorizadoGeneral row = new MontoAutorizadoGeneral();

		row.setCodigo(result.getInt(1));
		row.setCodigoTipoAutorizacion(result.getInt(2));
		row.setCodigoRol(result.getInt(3));
		row.setCodigoTemaAutorizacion(result.getInt(4));
		row.setMonto(result.getDouble(5));
		row.setEstado(result.getString(6));

		return row;
	}
}