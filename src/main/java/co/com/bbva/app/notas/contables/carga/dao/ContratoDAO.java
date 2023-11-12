/*
	Nombre DTO: Contrato
 */

package co.com.bbva.app.notas.contables.carga.dao;

import co.com.bbva.app.notas.contables.carga.dto.Contrato;
import co.com.bbva.app.notas.contables.dao.CommonDAO;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.Collection;

public class ContratoDAO extends CommonDAO<Contrato> {

	private static String cs_COLUMNAS = "NUMERO_CLIENTE, NUMERO_CONTRATO, ESTADO_CARGA, FECHA_ULTIMA_CARGA";

	private static String cs_TABLA = "NC_CONTRATO";

	private static String cs_PK = null;

	private static String SQL_SELECT_ALL_SENTENCE = "SELECT " + cs_COLUMNAS + " FROM " + cs_TABLA + " ORDER BY NUMERO_CLIENTE, NUMERO_CONTRATO";

	private static String SQL_SELECT_BY_ESTADO_CARGA_SENTENCE = "SELECT " + cs_COLUMNAS + " FROM " + cs_TABLA + " WHERE (ESTADO_CARGA = ?)";

	private static String SQL_SELECT_BY_NUMERO_CLIENTE_SENTENCE = "SELECT " + cs_COLUMNAS + " FROM " + cs_TABLA + " WHERE (NUMERO_CLIENTE = ? AND NUMERO_CONTRATO!=-1)";

	private static String SQL_SELECT_BY_PRIMARY_KEY_SENTENCE = "SELECT " + cs_COLUMNAS + " FROM " + cs_TABLA + " WHERE (NUMERO_CLIENTE = ?) AND (NUMERO_CONTRATO = ?)";

	private static String SQL_SELECT_BY_SENTENCE = "SELECT " + cs_COLUMNAS + " FROM " + cs_TABLA + " WHERE (NUMERO_CLIENTE LIKE ?) OR (NUMERO_CONTRATO LIKE ?)";

	private static int NUMERO_COLUMNAS_BUSQUEDA = 2;

	private static String SQL_UPDATE_ESTADO_CARGA_SENTENCE = "UPDATE " + cs_TABLA + " SET ESTADO_CARGA = ?";

	private static String SQL_DELETE_SENTENCE = "DELETE FROM " + cs_TABLA + " WHERE (NUMERO_CLIENTE = ?) AND (NUMERO_CONTRATO = ?)";

	public ContratoDAO() {
		super(cs_TABLA, cs_COLUMNAS, cs_PK, new Contrato());
	}

	public void updateEstadoCarga(String estadoCarga) throws Exception {
		executeUpdate(SQL_UPDATE_ESTADO_CARGA_SENTENCE, estadoCarga);
	}

	@Override
	public void internalDelete(Connection con, Contrato row) throws Exception {
		executeUpdate(con, SQL_DELETE_SENTENCE, new Object[] { row.getNumeroCliente(), row.getNumeroCliente() });
	}

	@Override
	public Contrato findByPrimaryKey(Contrato row) throws Exception {
		return getByGeneral(SQL_SELECT_BY_PRIMARY_KEY_SENTENCE, new Object[] { row.getNumeroCliente(), row.getNumeroCliente() });
	}

	public Collection<Contrato> findAll() throws Exception {
		return findByGeneral(SQL_SELECT_ALL_SENTENCE);
	}

	public Collection<Contrato> findByEstadoCarga(String estadoCarga) throws Exception {
		return findByGeneral(SQL_SELECT_BY_ESTADO_CARGA_SENTENCE, estadoCarga);
	}

	public Collection<Contrato> findByNumeroCliente(Contrato row) throws Exception {
		return findByGeneral(SQL_SELECT_BY_NUMERO_CLIENTE_SENTENCE, row.getNumeroCliente());
	}

	public Collection<Contrato> findBy(String palabraClave) throws Exception {
		return findByKeyWord(SQL_SELECT_BY_SENTENCE, palabraClave, NUMERO_COLUMNAS_BUSQUEDA);
	}

	@Override
	protected Object[] getDataToAdd(@SuppressWarnings("unused") Connection con, Contrato row) throws Exception {
		return new Object[] { row.getNumeroCliente(), row.getNumeroContrato(), row.getEstadoCarga(), row.getFechaUltimaCarga() };
	}

	@Override
	public Contrato getResultSetToVO(ResultSet result) throws Exception {
		Contrato row = new Contrato();

		row.setNumeroCliente(result.getString(1));
		row.setNumeroContrato(result.getString(2));
		row.setEstadoCarga(result.getString(3));
		row.setFechaUltimaCarga(result.getTimestamp(4));

		return row;
	}

	@Override
	protected void internalUpdate( @SuppressWarnings("unused") Connection con,@SuppressWarnings("unused") Contrato row) throws Exception {
		throw new Exception("Mtodo no implementado");
	}
}