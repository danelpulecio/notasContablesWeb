/*
	Nombre DTO: ActividadEconomica
 */

package co.com.bbva.app.notas.contables.carga.dao;

import co.com.bbva.app.notas.contables.carga.dto.Pais;
import co.com.bbva.app.notas.contables.dao.CommonDAO;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class PaisDAO extends CommonDAO<Pais> {

	private static String cs_COLUMNAS = "PREFIJO, NOMBRE";

	private static String cs_TABLA = "NC_PAIS";

	private static String cs_PK = "PREFIJO";

	private static String SQL_SELECT_ALL_SENTENCE = "SELECT " + cs_COLUMNAS + " FROM " + cs_TABLA + " ORDER BY NOMBRE";

	private static String SQL_SELECT_BY_SENTENCE = "SELECT " + cs_COLUMNAS + " FROM " + cs_TABLA + " WHERE (PREFIJO = ?) OR (NOMBRE LIKE ?) ORDER BY NOMBRE";

	private static int NUMERO_COLUMNAS_BUSQUEDA = 2;

	public PaisDAO() {
		super(cs_TABLA, cs_COLUMNAS, cs_PK, new Pais());
	}

	@Override
	public void internalUpdate( @SuppressWarnings("unused") Connection con,@SuppressWarnings("unused") Pais row) throws Exception {
		throw new Exception("Mtodo no implementado");
	}

	public Collection<Pais> findAll() throws Exception {
		return findByGeneral(SQL_SELECT_ALL_SENTENCE);
	}

	public Collection<Pais> findBy(String palabraClave) throws Exception {
		List<Object> params = new ArrayList<Object>();
		for (int count = 1; count <= NUMERO_COLUMNAS_BUSQUEDA; count++) {
			if (count != 1) {
				params.add("%" + palabraClave + "%");
			} else {
				try {
					params.add(Integer.parseInt(palabraClave));
				} catch (Exception le_e) {
					params.add(Integer.valueOf(0));
				}
			}
		}
		return findByGeneral(SQL_SELECT_BY_SENTENCE, params.toArray());
	}

	@Override
	protected Object[] getDataToAdd(@SuppressWarnings("unused") Connection con, Pais row) throws Exception {
		return new Object[] { row.getPrefijo(), row.getNombre() };
	}

	@Override
	public Pais getResultSetToVO(ResultSet result) throws Exception {
		Pais row = new Pais();
		row.setPrefijo(result.getString(1));
		row.setNombre(result.getString(2));
		return row;
	}
}