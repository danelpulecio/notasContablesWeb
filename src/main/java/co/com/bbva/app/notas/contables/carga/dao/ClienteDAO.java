/*
	Nombre DTO: Cliente
 */

package co.com.bbva.app.notas.contables.carga.dao;

import co.com.bbva.app.notas.contables.carga.dto.Cliente;
import co.com.bbva.app.notas.contables.dao.CommonDAO;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.Collection;

public class ClienteDAO extends CommonDAO<Cliente> {

	private static String cs_COLUMNAS = "NUMERO_CLIENTE, TIPO_IDENTIFICACION, NUMERO_IDENTIFICACION, DIGITO_VERIFICACION, PRIMER_APELLIDO, SEGUNDO_APELLIDO, PRIMER_NOMBRE, ESTADO_CARGA, FECHA_ULTIMA_CARGA";

	private static String cs_TABLA = "NC_CLIENTE";

	private static String cs_PK = "NUMERO_CLIENTE";

	private static String SQL_SELECT_ALL_SENTENCE = "SELECT " + cs_COLUMNAS + " FROM " + cs_TABLA + " ORDER BY PRIMER_APELLIDO, SEGUNDO_APELLIDO, PRIMER_NOMBRE";

	private static String SQL_SELECT_BY_ESTADO_CARGA_SENTENCE = "SELECT " + cs_COLUMNAS + " FROM " + cs_TABLA + " WHERE (ESTADO_CARGA = ?)";

	private static String SQL_SELECT_BY_TIPO_AND_NUMERO_IDENTIFICACION_SENTENCE = "SELECT " + cs_COLUMNAS + " FROM " + cs_TABLA + " WHERE (TIPO_IDENTIFICACION = ?) AND (NUMERO_IDENTIFICACION = ?)";

	private static String SQL_SELECT_BY_SENTENCE = "SELECT " + cs_COLUMNAS + " FROM " + cs_TABLA
			+ " WHERE (NUMERO_CLIENTE LIKE ?) OR (TIPO_IDENTIFICACION LIKE ?) OR (NUMERO_IDENTIFICACION LIKE ?) OR (DIGITO_VERIFICACION LIKE ?) OR (PRIMER_APELLIDO LIKE ?) OR (SEGUNDO_APELLIDO LIKE ?) OR (PRIMER_NOMBRE LIKE ?)";

	private static int NUMERO_COLUMNAS_BUSQUEDA = 7;

	private static String SQL_UPDATE_ESTADO_CARGA_SENTENCE = "UPDATE " + cs_TABLA + " SET ESTADO_CARGA = ?";

	private static String SQL_UPDATE_SENTENCE = "UPDATE " + cs_TABLA
			+ " SET TIPO_IDENTIFICACION = ?, NUMERO_IDENTIFICACION = ?, DIGITO_VERIFICACION = ?, PRIMER_APELLIDO = ?, SEGUNDO_APELLIDO = ?, PRIMER_NOMBRE = ?, ESTADO_CARGA = ?, FECHA_ULTIMA_CARGA = ? WHERE (NUMERO_CLIENTE = ?)";

	public ClienteDAO() {
		super(cs_TABLA, cs_COLUMNAS, cs_PK, new Cliente());
	}

	@Override
	public void internalUpdate(Connection con, Cliente row) throws Exception {
		executeUpdate(con,SQL_UPDATE_SENTENCE, new Object[] { row.getTipoIdentificacion(), row.getNumeroIdentificacion(), row.getDigitoVerificacion(), row.getPrimerApellido(), row.getSegundoApellido(), row.getPrimerNombre(), row.getEstadoCarga(),
				row.getFechaUltimaCarga(), row.getNumeroCliente() });
	}

	public void updateEstadoCarga(String estadoCarga) throws Exception {
		executeUpdate(SQL_UPDATE_ESTADO_CARGA_SENTENCE, estadoCarga);
	}

	public Cliente findByTipoAndNumeroIdentificacion(Cliente row) throws Exception {
		return getByGeneral(SQL_SELECT_BY_TIPO_AND_NUMERO_IDENTIFICACION_SENTENCE, new Object[] { row.getTipoIdentificacion(), row.getNumeroIdentificacion() });
	}

	public Collection<Cliente> findAll() throws Exception {
		return findByGeneral(SQL_SELECT_ALL_SENTENCE);
	}

	public Collection<Cliente> findByEstadoCarga(String estadoCarga) throws Exception {
		return findByGeneral(SQL_SELECT_BY_ESTADO_CARGA_SENTENCE, estadoCarga);
	}

	public Collection<Cliente> findBy(String palabraClave) throws Exception {
		return findByKeyWord(SQL_SELECT_BY_SENTENCE, palabraClave, NUMERO_COLUMNAS_BUSQUEDA);
	}

	@Override
	protected Object[] getDataToAdd(@SuppressWarnings("unused") Connection con, Cliente row) throws Exception {
		return new Object[] { row.getNumeroCliente(), row.getTipoIdentificacion(), row.getNumeroIdentificacion(), row.getDigitoVerificacion(), row.getPrimerApellido(), row.getSegundoApellido(), row.getPrimerNombre(), row.getEstadoCarga(),
				row.getFechaUltimaCarga() };
	}

	@Override
	public Cliente getResultSetToVO(ResultSet result) throws Exception {
		Cliente row = new Cliente();

		row.setNumeroCliente(result.getString(1) != null ? result.getString(1).trim() : "");
		row.setTipoIdentificacion(result.getString(2) != null ? result.getString(2).trim() : "");
		row.setNumeroIdentificacion(result.getString(3) != null ? result.getString(3).trim() : "");
		row.setDigitoVerificacion(result.getString(4) != null ? result.getString(4).trim() : "");
		row.setPrimerApellido(result.getString(5) != null ? result.getString(5).trim() : "");
		row.setSegundoApellido(result.getString(6) != null ? result.getString(6).trim() : "");
		row.setPrimerNombre(result.getString(7) != null ? result.getString(7).trim() : "");
		row.setEstadoCarga(result.getString(8) != null ? result.getString(8).trim() : "");
		row.setFechaUltimaCarga(result.getTimestamp(9));

		return row;
	}
}