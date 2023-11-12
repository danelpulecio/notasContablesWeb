/*
	Nombre DTO: Municipio
 */

package co.com.bbva.app.notas.contables.carga.dao;

import co.com.bbva.app.notas.contables.carga.dto.Municipio;
import co.com.bbva.app.notas.contables.dao.CommonDAO;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.Collection;

public class MunicipioDAO extends CommonDAO<Municipio> {

	private static String cs_COLUMNAS = "CODIGO_DEPARTAMENTO, CODIGO_MUNICIPIO, NOMBRE, ESTADO_CARGA, FECHA_ULTIMA_CARGA";

	private static String cs_TABLA = "NC_MUNICIPIO";
	private static String cs_PK = null;

	private static String SQL_SELECT_ALL_SENTENCE = "SELECT " + cs_COLUMNAS + " FROM " + cs_TABLA + " ORDER BY NOMBRE";

	private static String SQL_SELECT_BY_ESTADO_CARGA_SENTENCE = "SELECT " + cs_COLUMNAS + " FROM " + cs_TABLA + " WHERE (ESTADO_CARGA = ?) ORDER BY NOMBRE";

	private static String SQL_SELECT_BY_PRIMARY_KEY_SENTENCE = "SELECT " + cs_COLUMNAS + " FROM " + cs_TABLA + " WHERE (CODIGO_DEPARTAMENTO = ?) AND (CODIGO_MUNICIPIO = ?)";

	private static String SQL_SELECT_BY_SENTENCE = "SELECT " + cs_COLUMNAS + " FROM " + cs_TABLA + " WHERE (CODIGO_DEPARTAMENTO LIKE ?) OR (CODIGO_MUNICIPIO LIKE ?) OR (NOMBRE LIKE ?) ORDER BY NOMBRE";

	private static String SQL_SELECT_BY_DEPARTAMENTO_SENTENCE = "SELECT " + cs_COLUMNAS + " FROM " + cs_TABLA + " WHERE (CODIGO_DEPARTAMENTO LIKE ?) ORDER BY NOMBRE";

	private static int NUMERO_COLUMNAS_BUSQUEDA = 3;

	private static String SQL_UPDATE_ESTADO_CARGA_SENTENCE = "UPDATE " + cs_TABLA + " SET ESTADO_CARGA = ?";

	private static String SQL_UPDATE_SENTENCE = "UPDATE " + cs_TABLA + " SET NOMBRE = ?, ESTADO_CARGA = ?, FECHA_ULTIMA_CARGA = ? WHERE (CODIGO_DEPARTAMENTO = ?) AND (CODIGO_MUNICIPIO = ?)";

	private static String SQL_DELETE_SENTENCE = "DELETE FROM " + cs_TABLA + " WHERE (CODIGO_DEPARTAMENTO = ?) AND (CODIGO_MUNICIPIO = ?)";

	public MunicipioDAO() {
		super(cs_TABLA, cs_COLUMNAS, cs_PK, new Municipio());
	}

	@Override
	public void internalUpdate( Connection con,Municipio row) throws Exception {
		executeUpdate(con, SQL_UPDATE_SENTENCE, new Object[] { row.getNombre(), row.getEstadoCarga(), row.getFechaUltimaCarga(), row.getCodigoDepartamento(), row.getCodigoMunicipio() });
	}

	public void updateEstadoCarga(String estadoCarga) throws Exception {
		executeUpdate(SQL_UPDATE_ESTADO_CARGA_SENTENCE, estadoCarga);
	}

	@Override
	public void internalDelete(Connection con, Municipio row) throws Exception {
		executeUpdate(con, SQL_DELETE_SENTENCE, new Object[] { row.getCodigoDepartamento(), row.getCodigoMunicipio() });
	}

	@Override
	public Municipio findByPrimaryKey(Municipio row) throws Exception {
		return getByGeneral(SQL_SELECT_BY_PRIMARY_KEY_SENTENCE, new Object[] { row.getCodigoDepartamento(), row.getCodigoMunicipio() });
	}

	public Collection<Municipio> findAll() throws Exception {
		return findByGeneral(SQL_SELECT_ALL_SENTENCE);
	}

	public Collection<Municipio> findByEstadoCarga(String estadoCarga) throws Exception {
		return findByGeneral(SQL_SELECT_BY_ESTADO_CARGA_SENTENCE, estadoCarga);
	}

	public Collection<Municipio> findBy(String palabraClave) throws Exception {
		return findByKeyWord(SQL_SELECT_BY_SENTENCE, palabraClave, NUMERO_COLUMNAS_BUSQUEDA);
	}

	public Collection<Municipio> findByDepartamento(String codigoDepto) throws Exception {
		return findByGeneral(SQL_SELECT_BY_DEPARTAMENTO_SENTENCE, codigoDepto);
	}

	@Override
	protected Object[] getDataToAdd(@SuppressWarnings("unused") Connection con, Municipio row) throws Exception {
		return new Object[] { row.getCodigoDepartamento(), row.getCodigoMunicipio(), row.getNombre(), row.getEstadoCarga(), row.getFechaUltimaCarga() };
	}

	@Override
	public Municipio getResultSetToVO(ResultSet result) throws Exception {
		Municipio row = new Municipio();

		row.setCodigoDepartamento(result.getString(1));
		row.setCodigoMunicipio(result.getString(2));
		row.setNombre(result.getString(3));
		row.setEstadoCarga(result.getString(4));
		row.setFechaUltimaCarga(result.getTimestamp(5));

		return row;
	}
}