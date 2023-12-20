/*
	Nombre DTO: Sucursal
 */

package co.com.bbva.app.notas.contables.carga.dao;

import co.com.bbva.app.notas.contables.carga.dto.Sucursal;
import co.com.bbva.app.notas.contables.dao.CommonDAO;
import co.com.bbva.app.notas.contables.jsf.BasePage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.Collection;
import java.util.Map;

public class SucursalDAO extends CommonDAO<Sucursal> {

	private static final Logger LOGGER = LoggerFactory.getLogger(SucursalDAO.class);

	private static String cs_COLUMNAS = "CODIGO, NOMBRE, TIPO_CENTRO, CODIGO_CENTRO_SUPERIOR, NOMBRE_CENTRO_SUPERIOR, ESTADO_CARGA, FECHA_ULTIMA_CARGA";

	private static String cs_TABLA = "NC_SUCURSAL";

	private static String cs_PK = "CODIGO";

	private static String SQL_SELECT_ALL_SENTENCE = "SELECT " + cs_COLUMNAS + " FROM " + cs_TABLA + " ORDER BY CODIGO";

	private static String SQL_SELECT_BY_ESTADO_CARGA_SENTENCE = "SELECT " + cs_COLUMNAS + " FROM " + cs_TABLA + " WHERE (ESTADO_CARGA = ?)";

	private static String SQL_SELECT_BY_SENTENCE = "SELECT " + cs_COLUMNAS + " FROM " + cs_TABLA
			+ " WHERE (CODIGO LIKE ?) OR (NOMBRE LIKE ?) OR (TIPO_CENTRO LIKE ?) OR (CODIGO_CENTRO_SUPERIOR LIKE ?) OR (NOMBRE_CENTRO_SUPERIOR LIKE ?) ORDER BY CODIGO";

	private static int NUMERO_COLUMNAS_BUSQUEDA = 5;

	private static String SQL_SELECT_CV_SENTENCE = "SELECT UNA.CODIGO CLAVE, SUC.CODIGO||' - '||SUC.NOMBRE VALOR FROM NC_SUCURSAL SUC RIGHT JOIN NC_UNIDAD_ANALISIS UNA ON (UNA.CODIGO_SUCURSAL = SUC.CODIGO) WHERE UNA.ESTADO='A' order by SUC.CODIGO||' - '||SUC.NOMBRE";//

	private static String SQL_UPDATE_ESTADO_CARGA_SENTENCE = "UPDATE " + cs_TABLA + " SET ESTADO_CARGA = ?";

	private static String SQL_UPDATE_SENTENCE = "UPDATE " + cs_TABLA + " SET NOMBRE = ?, TIPO_CENTRO = ?, CODIGO_CENTRO_SUPERIOR = ?, NOMBRE_CENTRO_SUPERIOR = ?, ESTADO_CARGA = ?, FECHA_ULTIMA_CARGA = ? WHERE (CODIGO = ?)";

	public SucursalDAO() {
		super(cs_TABLA, cs_COLUMNAS, cs_PK, new Sucursal());
	}

	@Override
	public void internalUpdate( Connection con,Sucursal row) throws Exception {
		executeUpdate(con, SQL_UPDATE_SENTENCE, new Object[] { row.getNombre(), row.getTipoCentro(), row.getCodigoCentroSuperior(), row.getNombreCentroSuperior(), row.getEstadoCarga(), row.getFechaUltimaCarga(), row.getCodigo() });
	}

	public void updateEstadoCarga(String estadoCarga) throws Exception {
		executeUpdate(SQL_UPDATE_ESTADO_CARGA_SENTENCE, estadoCarga);
	}

	public Collection<Sucursal> findAll() throws Exception {
		Collection<Sucursal> sucursalesDb = findByGeneral(SQL_SELECT_ALL_SENTENCE);
		LOGGER.info("sucursales db {}",sucursalesDb);
		return sucursalesDb;
	}

	public Collection<Sucursal> findByEstadoCarga(String estadoCarga) throws Exception {
		return findByGeneral(SQL_SELECT_BY_ESTADO_CARGA_SENTENCE, estadoCarga);
	}

	public Collection<Sucursal> findByTipoCentro(String tiposCentro) throws Exception {
		String sentence = "SELECT " + cs_COLUMNAS + " FROM " + cs_TABLA + " WHERE (TIPO_CENTRO IN (" + tiposCentro + "))";
		return findByGeneral(sentence);
	}

	public Collection<Sucursal> findBy(String palabraClave) throws Exception {
		LOGGER.info("Palabra clave sucursal {}",palabraClave);
		Collection<Sucursal> responseSucursalesDB = findByKeyWord(SQL_SELECT_BY_SENTENCE, palabraClave, NUMERO_COLUMNAS_BUSQUEDA);
		LOGGER.info("sucursales db {}",responseSucursalesDB);
		return responseSucursalesDB;
	}

	public Map<String, String> getCVSucursal() throws Exception {
		return getCV(SQL_SELECT_CV_SENTENCE);
	}

	@Override
	protected Object[] getDataToAdd(@SuppressWarnings("unused") Connection con, Sucursal row) throws Exception {
		return new Object[] { row.getCodigo(), row.getNombre(), row.getTipoCentro(), row.getCodigoCentroSuperior(), row.getNombreCentroSuperior(), row.getEstadoCarga(), row.getFechaUltimaCarga() };
	}

	@Override
	public Sucursal getResultSetToVO(ResultSet result) throws Exception {
		Sucursal row = new Sucursal();

		row.setCodigo(result.getString(1));
		row.setNombre(result.getString(2));
		row.setTipoCentro(result.getString(3));
		row.setCodigoCentroSuperior(result.getString(4));
		row.setNombreCentroSuperior(result.getString(5));
		row.setEstadoCarga(result.getString(6));
		row.setFechaUltimaCarga(result.getTimestamp(7));

		return row;
	}
}