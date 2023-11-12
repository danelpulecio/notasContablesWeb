/*
	Nombre DTO: RegistroCarga
 */

package co.com.bbva.app.notas.contables.carga.dao;

import co.com.bbva.app.notas.contables.carga.dto.RegistroCarga;
import co.com.bbva.app.notas.contables.dao.CommonDAO;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.Collection;

public class RegistroCargaDAO extends CommonDAO<RegistroCarga> {

	private static String cs_COLUMNAS = "CODIGO, FECHA_INICIO, FECHA_FIN, NOMBRE_ARCHIVO, REGISTROS_CARGADOS";

	private static String cs_TABLA = "NC_REGISTRO_CARGA";

	private static String cs_PK = "CODIGO";

	private static String SQL_SELECT_ALL_SENTENCE = "SELECT " + cs_COLUMNAS + " FROM " + cs_TABLA + " ORDER BY FECHA_INICIO DESC, NOMBRE_ARCHIVO";

	private static String SQL_SELECT_BY_SENTENCE = "SELECT " + cs_COLUMNAS + " FROM " + cs_TABLA + " WHERE (NOMBRE_ARCHIVO LIKE ?)";

	private static String SQL_SELECT_BY_NOMBRE_ARCHIVO_SENTENCE = "SELECT " + cs_COLUMNAS + " FROM " + cs_TABLA + " WHERE (NOMBRE_ARCHIVO LIKE ?) ORDER BY CODIGO DESC";

	private static int NUMERO_COLUMNAS_BUSQUEDA = 1;

	private static String SQL_UPDATE_SENTENCE = "UPDATE " + cs_TABLA + " SET FECHA_INICIO = ?, FECHA_FIN = ?, NOMBRE_ARCHIVO = ?, REGISTROS_CARGADOS = ? WHERE (CODIGO = ?)";

	public RegistroCargaDAO() {
		super(cs_TABLA, cs_COLUMNAS, cs_PK, new RegistroCarga());
	}

	@Override
	public void internalUpdate( Connection con,RegistroCarga row) throws Exception {
		executeUpdate(con, SQL_UPDATE_SENTENCE, new Object[] { row.getFechaInicio(), row.getFechaFin(), row.getNombreArchivo(), row.getRegistrosCargados(), row.getCodigo() });
	}

	public Collection<RegistroCarga> findAll() throws Exception {
		return findByGeneral(SQL_SELECT_ALL_SENTENCE);
	}

	public Collection<RegistroCarga> findBy(String filtro) throws Exception {
		return findByKeyWord(SQL_SELECT_BY_SENTENCE, filtro, NUMERO_COLUMNAS_BUSQUEDA);
	}

	public RegistroCarga getByNombreArchivo(String nombreArchivo) throws Exception {
		return getByGeneral(SQL_SELECT_BY_NOMBRE_ARCHIVO_SENTENCE, nombreArchivo);
	}

	@Override
	protected Object[] getDataToAdd(Connection con, RegistroCarga row) throws Exception {
		row.setCodigo(getMaxCode(con));
		return new Object[] { row.getCodigo(), row.getFechaInicio(), row.getFechaFin(), row.getNombreArchivo(), row.getRegistrosCargados() };
	}

	@Override
	public RegistroCarga getResultSetToVO(ResultSet result) throws Exception {
		RegistroCarga row = new RegistroCarga();

		row.setCodigo(result.getInt(1));
		row.setFechaInicio(result.getTimestamp(2));
		row.setFechaFin(result.getTimestamp(3));
		row.setNombreArchivo(result.getString(4));
		row.setRegistrosCargados(result.getInt(5));

		return row;
	}
}