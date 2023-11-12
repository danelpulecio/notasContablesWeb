package co.com.bbva.app.notas.contables.dao;

import co.com.bbva.app.notas.contables.dto.Padrino;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.Collection;

public class PadrinoDAO extends CommonSeqDAO<Padrino> {

	private static String cs_COLUMNAS = "CODIGO, CODIGO_USUARIO, CODIGO_UNIDAD_ANALISIS, ESTADO";

	private static String cs_TABLA = "NC_PADRINO";

	private static String cs_PK = "CODIGO";

	private static String SQL_SELECT_ALL_SENTENCE = "SELECT A.CODIGO \"codigo\", A.CODIGO_USUARIO \"codigoUsuario\", A.CODIGO_UNIDAD_ANALISIS \"codigoUnidadAnalisis\", A.ESTADO \"estado\", B.NOMBRE \"nombreSucursal\", B.CODIGO \"codigoSucursal\", C.NOMBRE_EMPLEADO \"nombreEmpleado\", C.CODIGO_EMPLEADO \"codigoEmpleado\" FROM NC_PADRINO A LEFT JOIN NC_USUARIO D ON (A.CODIGO_USUARIO = D.CODIGO) LEFT JOIN NC_UNIDAD_ANALISIS E ON (A.CODIGO_UNIDAD_ANALISIS = E.CODIGO) LEFT JOIN NC_SUCURSAL B ON (E.CODIGO_SUCURSAL = B.CODIGO) LEFT JOIN NC_USUARIO_ALTAMIRA C ON (D.CODIGO_EMPLEADO = C.CODIGO_EMPLEADO)";

	private static String SQL_SELECT_BY_ESTADO_SENTENCE = "SELECT A.CODIGO, A.CODIGO_USUARIO, A.CODIGO_UNIDAD_ANALISIS, A.ESTADO FROM NC_PADRINO A, NC_SUCURSAL B, NC_USUARIO_ALTAMIRA C, NC_USUARIO D, NC_UNIDAD_ANALISIS E WHERE ((A.CODIGO_UNIDAD_ANALISIS = E.CODIGO) AND (A.CODIGO_USUARIO = D.CODIGO) AND (D.CODIGO_EMPLEADO = C.CODIGO_EMPLEADO)) AND (E.CODIGO_SUCURSAL = B.CODIGO) AND ((A.ESTADO = ?)) ORDER BY B.CODIGO";

	private static String SQL_SELECT_BY_CODIGO_UNIDAD_ANALISIS_AND_ESTADO_SENTENCE = "SELECT A.CODIGO, A.CODIGO_USUARIO, A.CODIGO_UNIDAD_ANALISIS, A.ESTADO FROM NC_PADRINO A, NC_SUCURSAL B, NC_USUARIO_ALTAMIRA C, NC_USUARIO D, NC_UNIDAD_ANALISIS E WHERE ((A.CODIGO_UNIDAD_ANALISIS = E.CODIGO) AND (A.CODIGO_USUARIO = D.CODIGO) AND (D.CODIGO_EMPLEADO = C.CODIGO_EMPLEADO)) AND (E.CODIGO_SUCURSAL = B.CODIGO) AND ((A.CODIGO_UNIDAD_ANALISIS = ?) AND (A.ESTADO = ?)) ORDER BY B.CODIGO";

	private static String SQL_SELECT_BY_CODIGO_USUARIO_SENTENCE = "SELECT " + cs_COLUMNAS + " FROM " + cs_TABLA + " WHERE (CODIGO_USUARIO = ?)";

	private static String SQL_SELECT_BY_SENTENCE = SQL_SELECT_ALL_SENTENCE + " WHERE ((E.CODIGO_SUCURSAL LIKE ?) OR (B.NOMBRE LIKE ?) OR (C.CODIGO_EMPLEADO LIKE ?) OR (C.NOMBRE_EMPLEADO LIKE ?)) ORDER BY B.CODIGO";

	private static String SQL_UPDATE_SENTENCE = "UPDATE " + cs_TABLA + " SET CODIGO_USUARIO = ?, CODIGO_UNIDAD_ANALISIS = ?, ESTADO = ? WHERE (CODIGO = ?)";

	public PadrinoDAO() {
		super(cs_TABLA, cs_COLUMNAS, cs_PK, new Padrino());
	}

	@Override
	public void internalUpdate(Connection con, Padrino row) throws Exception {
		executeUpdate(con, SQL_UPDATE_SENTENCE, new Object[] { row.getCodigoUsuario().intValue(), row.getCodigoUnidadAnalisis().intValue(), row.getEstado(), row.getCodigo().intValue() });
	}

	public Padrino findByCodigoUsuario(Padrino row) throws Exception {
		return getByGeneral(SQL_SELECT_BY_CODIGO_USUARIO_SENTENCE, row.getCodigoUsuario().intValue());
	}

	public Padrino findByCodigoUsuario(Connection con, Padrino row) throws Exception {
		return getByGeneral(con, SQL_SELECT_BY_CODIGO_USUARIO_SENTENCE, row.getCodigoUsuario().intValue());
	}

	public Collection<Padrino> findAll() throws Exception {
		return obtenerLista(SQL_SELECT_ALL_SENTENCE);
	}

	public Collection<Padrino> findByEstado(Padrino row) throws Exception {
		return findByGeneral(SQL_SELECT_BY_ESTADO_SENTENCE, row.getEstado());
	}

	public Collection<Padrino> findByCodigoUnidadAnalisisAndEstado(Padrino row) throws Exception {
		return findByGeneral(SQL_SELECT_BY_CODIGO_UNIDAD_ANALISIS_AND_ESTADO_SENTENCE, new Object[] { row.getCodigoUnidadAnalisis().intValue(), row.getEstado() });
	}

	public Collection<Padrino> findBy(String palabraClave) throws Exception {
		String param = COMODIN + palabraClave + COMODIN;
		return obtenerLista(SQL_SELECT_BY_SENTENCE, new Object[] { param, param, param, param });
	}

	@Override
	protected Object[] getDataToAdd(Connection con, Padrino row) throws Exception {
		row.setCodigo(getMaxCode(con));
		return new Object[] { row.getCodigo().intValue(), row.getCodigoUsuario().intValue(), row.getCodigoUnidadAnalisis().intValue(), row.getEstado() };
	}

	@Override
	public Padrino getResultSetToVO(ResultSet result) throws Exception {
		Padrino row = new Padrino();

		row.setCodigo(result.getInt(1));
		row.setCodigoUsuario(result.getInt(2));
		row.setCodigoUnidadAnalisis(result.getInt(3));
		row.setEstado(result.getString(4));

		return row;
	}
}