package co.com.bbva.app.notas.contables.dao;

import co.com.bbva.app.notas.contables.dto.UnidadAnalisis;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.Collection;

public class UnidadAnalisisDAO extends CommonSeqDAO<UnidadAnalisis> {

	private static String cs_COLUMNAS = "CODIGO, CODIGO_SUCURSAL, AUTORIZA_REF_CRUCE, ESTADO";

	static final String cs_TABLA = "NC_UNIDAD_ANALISIS";

	private static String cs_PK = "CODIGO";

	private static String SQL_SELECT_ALL_SENTENCE = "SELECT UA.CODIGO \"codigo\", " + //
			"UA.CODIGO_SUCURSAL \"codigoSucursal\", " + //
			"UA.AUTORIZA_REF_CRUCE \"autorizaReferenciaCruce\", " + //
			"UA.ESTADO \"estado\", " + //
			"SU.NOMBRE \"nombreSucursal\" " + //
			"FROM NC_UNIDAD_ANALISIS UA LEFT JOIN NC_SUCURSAL SU ON (SU.CODIGO=UA.CODIGO_SUCURSAL)";

	private static String SQL_SELECT_BY_ESTADO_SENTENCE = "SELECT " + cs_COLUMNAS + " FROM " + cs_TABLA + " WHERE (ESTADO = ?) ORDER BY CODIGO_SUCURSAL";

	private static String SQL_SELECT_BY_AUTORIZA_REF_CRUCE_AND_ESTADO_SENTENCE = "SELECT " + cs_COLUMNAS + " FROM " + cs_TABLA + " WHERE (AUTORIZA_REF_CRUCE = ?) AND (ESTADO = ?) ORDER BY CODIGO_SUCURSAL";

	private static String SQL_SELECT_BY_SENTENCE = SQL_SELECT_ALL_SENTENCE + " WHERE ((UA.CODIGO_SUCURSAL LIKE ?) OR (SU.NOMBRE LIKE ?))";

	private static String SQL_UPDATE_SENTENCE = "UPDATE " + cs_TABLA + " SET CODIGO_SUCURSAL = ?, AUTORIZA_REF_CRUCE = ?, ESTADO = ? WHERE (CODIGO = ?)";

	public UnidadAnalisisDAO() {
		super(cs_TABLA, cs_COLUMNAS, cs_PK, new UnidadAnalisis());
	}

	@Override
	public void internalUpdate(Connection con, UnidadAnalisis row) throws Exception {
		executeUpdate(con, SQL_UPDATE_SENTENCE, new Object[] { row.getCodigoSucursal(), row.getAutorizaReferenciaCruce(), row.getEstado(), row.getCodigo().intValue() });
	}

	public UnidadAnalisis findByAutorizaReferenciaCruceAndEstado(UnidadAnalisis row) throws Exception {
		return getByGeneral(SQL_SELECT_BY_AUTORIZA_REF_CRUCE_AND_ESTADO_SENTENCE, new Object[] { row.getAutorizaReferenciaCruce(), row.getEstado() });
	}

	public Collection<UnidadAnalisis> findAll() throws Exception {
		return obtenerLista(SQL_SELECT_ALL_SENTENCE);
	}

	public Collection<UnidadAnalisis> findByEstado(UnidadAnalisis row) throws Exception {
		return findByGeneral(SQL_SELECT_BY_ESTADO_SENTENCE, row.getEstado());
	}

	public Collection<UnidadAnalisis> findBy(String palabraClave) throws Exception {
		String param = COMODIN + palabraClave + COMODIN;
		return obtenerLista(SQL_SELECT_BY_SENTENCE, new Object[] { param, param });
	}

	@Override
	protected Object[] getDataToAdd(Connection con, UnidadAnalisis row) throws Exception {
		row.setCodigo(getMaxCode(con));
		return new Object[] { row.getCodigo().intValue(), row.getCodigoSucursal(), row.getAutorizaReferenciaCruce(), row.getEstado() };
	}

	@Override
	public UnidadAnalisis getResultSetToVO(ResultSet result) throws Exception {
		UnidadAnalisis row = new UnidadAnalisis();

		row.setCodigo(result.getInt(1));
		row.setCodigoSucursal(result.getString(2));
		row.setAutorizaReferenciaCruce(result.getString(3));
		row.setEstado(result.getString(4));

		return row;
	}
}