/*
	Nombre DTO: AuditoriaCarga
 */

package co.com.bbva.app.notas.contables.carga.dao;

import co.com.bbva.app.notas.contables.carga.dto.AuditoriaCarga;
import co.com.bbva.app.notas.contables.dao.CommonDAO;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.Collection;

public class AuditoriaCargaDAO extends CommonDAO<AuditoriaCarga> {

	private static String cs_COLUMNAS = "CODIGO, FECHA, NOMBRE_ARCHIVO, CODIGO_REGISTRO, DESCRIPCION_REGISTRO, NOTAS, ESTADO";

	private static String cs_TABLA = "NC_AUDITORIA_CARGA";

	private static String cs_PK = "CODIGO";

	private static String SQL_SELECT_ALL_SENTENCE = "SELECT " + cs_COLUMNAS + " FROM " + cs_TABLA + " ORDER BY FECHA DESC, NOMBRE_ARCHIVO, CODIGO_REGISTRO";

	private static String SQL_SELECT_BY_SENTENCE = "SELECT " + cs_COLUMNAS + " FROM " + cs_TABLA + " WHERE (NOMBRE_ARCHIVO LIKE ?) OR (DESCRIPCION_REGISTRO LIKE ?) OR (NOTAS LIKE ?)";

	private static int NUMERO_COLUMNAS_BUSQUEDA = 3;

	private static String SQL_UPDATE_SENTENCE = "UPDATE " + cs_TABLA + " SET FECHA = ?, NOMBRE_ARCHIVO = ?, CODIGO_REGISTRO = ?, DESCRIPCION_REGISTRO = ?, NOTAS = ?, ESTADO = ? WHERE (CODIGO = ?)";

	public AuditoriaCargaDAO() {
		super(cs_TABLA, cs_COLUMNAS, cs_PK, new AuditoriaCarga());
	}

	@Override
	public void internalUpdate(Connection con, AuditoriaCarga row) throws Exception {
		executeUpdate(con, SQL_UPDATE_SENTENCE, new Object[] { row.getFecha(), row.getNombreArchivo(), row.getCodigoRegistro(), row.getDescripcionRegistro(), row.getNotas(), row.getEstado(), row.getCodigo() });
	}

	public Collection<AuditoriaCarga> findAll() throws Exception {
		return findByGeneral(SQL_SELECT_ALL_SENTENCE);
	}

	public Collection<AuditoriaCarga> findBy(String palabraClave) throws Exception {
		return findByKeyWord(SQL_SELECT_BY_SENTENCE, palabraClave, NUMERO_COLUMNAS_BUSQUEDA);
	}

	@Override
	protected Object[] getDataToAdd(Connection con, AuditoriaCarga row) throws Exception {
		row.setCodigo(getMaxCode(con));
		return new Object[] { row.getCodigo(), row.getFecha(), row.getNombreArchivo(), row.getCodigoRegistro(), row.getDescripcionRegistro(), row.getNotas(), row.getEstado() };
	}

	@Override
	public AuditoriaCarga getResultSetToVO(ResultSet result) throws Exception {
		AuditoriaCarga row = new AuditoriaCarga();

		row.setCodigo(result.getInt(1));
		row.setFecha(result.getTimestamp(2));
		row.setNombreArchivo(result.getString(3));
		row.setCodigoRegistro(result.getString(4));
		row.setDescripcionRegistro(result.getString(5));
		row.setNotas(result.getString(6));
		row.setEstado(result.getString(7));

		return row;
	}
}