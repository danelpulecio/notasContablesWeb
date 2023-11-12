package co.com.bbva.app.notas.contables.dao;

import co.com.bbva.app.notas.contables.dto.CentroEspecial;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.Collection;

public class CentroEspecialDAO extends CommonSeqDAO<CentroEspecial> {

	private static String cs_COLUMNAS = "CODIGO, CODIGO_SUCURSAL, ESTADO";

	private static String cs_TABLA = "NC_CENTRO_ESPECIAL";

	private static String cs_PK = "CODIGO";

	private static String SQL_SELECT_ALL_SENTENCE = "SELECT CE.CODIGO \"codigo\", CE.CODIGO_SUCURSAL \"codigoSucursal\", CE.ESTADO \"estado\", SU.NOMBRE \"nombreSucursal\" FROM NC_CENTRO_ESPECIAL CE LEFT JOIN NC_SUCURSAL SU ON (SU.CODIGO=CE.CODIGO_SUCURSAL)";

	private static String SQL_SELECT_BY_ESTADO_SENTENCE = "SELECT " + cs_COLUMNAS + " FROM " + cs_TABLA + " WHERE (ESTADO = ?) ORDER BY CODIGO_SUCURSAL";

	private static String SQL_SELECT_BY_CODIGO_SUCURSAL_SENTENCE = "SELECT " + cs_COLUMNAS + " FROM " + cs_TABLA + " WHERE (CODIGO_SUCURSAL = ?)";

	private static String SQL_SELECT_BY_SENTENCE = SQL_SELECT_ALL_SENTENCE + " WHERE ((CE.CODIGO_SUCURSAL LIKE ?) OR (SU.NOMBRE LIKE ?)) ORDER BY CE.CODIGO_SUCURSAL";

	private static String SQL_UPDATE_SENTENCE = "UPDATE " + cs_TABLA + " SET CODIGO_SUCURSAL = ?, ESTADO = ? WHERE (CODIGO = ?)";

	public CentroEspecialDAO() {
		super(cs_TABLA, cs_COLUMNAS, cs_PK, new CentroEspecial());
	}

	@Override
	public void internalUpdate(Connection con, CentroEspecial row) throws Exception {
		executeUpdate(con, SQL_UPDATE_SENTENCE, new Object[] { row.getCodigoSucursal(), row.getEstado(), row.getCodigo().intValue() });
	}

	public CentroEspecial findByCodigoSucursal(CentroEspecial row) throws Exception {
		return getByGeneral(SQL_SELECT_BY_CODIGO_SUCURSAL_SENTENCE, row.getCodigoSucursal());
	}

	public Collection<CentroEspecial> findAll() throws Exception {
		return obtenerLista(SQL_SELECT_ALL_SENTENCE);
	}

	public Collection<CentroEspecial> findByEstado(CentroEspecial row) throws Exception {
		return findByGeneral(SQL_SELECT_BY_ESTADO_SENTENCE, row.getEstado());
	}

	public Collection<CentroEspecial> findBy(String palabraClave) throws Exception {
		String param = COMODIN + palabraClave + COMODIN;
		return obtenerLista(SQL_SELECT_BY_SENTENCE, new Object[] { param, param });
	}

	@Override
	protected Object[] getDataToAdd(Connection con, CentroEspecial row) throws Exception {
		row.setCodigo(getMaxCode(con));
		return new Object[] { row.getCodigo().intValue(), row.getCodigoSucursal(), row.getEstado() };
	}

	@Override
	public CentroEspecial getResultSetToVO(ResultSet result) throws Exception {
		CentroEspecial row = new CentroEspecial();

		row.setCodigo(result.getInt(1));
		row.setCodigoSucursal(result.getString(2));
		row.setEstado(result.getString(3));

		return row;
	}
}