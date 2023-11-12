/*
	Nombre DTO: Festivo
 */

package co.com.bbva.app.notas.contables.carga.dao;

import co.com.bbva.app.notas.contables.carga.dto.Festivo;
import co.com.bbva.app.notas.contables.dao.CommonDAO;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.Collection;

public class FestivoDAO extends CommonDAO<Festivo> {

	private static String cs_COLUMNAS = "FECHA, ESTADO_CARGA, FECHA_ULTIMA_CARGA";

	private static String cs_TABLA = "NC_FESTIVO";

	private static String cs_PK = null;

	private static String SQL_SELECT_ALL_SENTENCE = "SELECT " + cs_COLUMNAS + " FROM " + cs_TABLA + " ORDER BY FECHA";

	private static String SQL_SELECT_BY_ESTADO_CARGA_SENTENCE = "SELECT " + cs_COLUMNAS + " FROM " + cs_TABLA + " WHERE (ESTADO_CARGA = ?)";

	private static String SQL_UPDATE_ESTADO_CARGA_SENTENCE = "UPDATE " + cs_TABLA + " SET ESTADO_CARGA = ?";

	public FestivoDAO() {
		super(cs_TABLA, cs_COLUMNAS, cs_PK, new Festivo());
	}

	public void updateEstadoCarga(String estadoCarga) throws Exception {
		executeUpdate(SQL_UPDATE_ESTADO_CARGA_SENTENCE, estadoCarga);
	}

	public Collection<Festivo> findAll() throws Exception {
		return findByGeneral(SQL_SELECT_ALL_SENTENCE);
	}

	public Collection<Festivo> findByEstadoCarga(String estadoCarga) throws Exception {
		return findByGeneral(SQL_SELECT_BY_ESTADO_CARGA_SENTENCE, estadoCarga);
	}

	@Override
	protected Object[] getDataToAdd(@SuppressWarnings("unused") Connection con, Festivo row) throws Exception {
		return new Object[] { row.getFecha(), row.getEstadoCarga(), row.getFechaUltimaCarga() };
	}

	@Override
	public Festivo getResultSetToVO(ResultSet result) throws Exception {
		Festivo row = new Festivo();

		row.setFecha(result.getDate(1));
		row.setEstadoCarga(result.getString(2));
		row.setFechaUltimaCarga(result.getTimestamp(3));

		return row;
	}

	@Override
	protected void internalUpdate( @SuppressWarnings("unused") Connection con,@SuppressWarnings("unused") Festivo row) throws Exception {
		throw new Exception("Mtodo no implementado");
	}
}