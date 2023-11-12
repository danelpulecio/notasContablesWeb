/*
	Nombre DTO: UsuarioAltamira
 */

package co.com.bbva.app.notas.contables.carga.dao;

import co.com.bbva.app.notas.contables.carga.dto.UsuarioAltamira;
import co.com.bbva.app.notas.contables.dao.CommonDAO;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.Collection;
import java.util.Map;

public class UsuarioAltamiraDAO extends CommonDAO<UsuarioAltamira> {

	private static String cs_COLUMNAS = "CODIGO_EMPLEADO, NOMBRE_EMPLEADO, CODIGO_AREA, NOMBRE_AREA, CODIGO_PERFIL, NOMBRE_PERFIL, CORREO_ELECTRONICO, ESTADO_CARGA, FECHA_ULTIMA_CARGA";

	private static String cs_TABLA = "NC_USUARIO_ALTAMIRA";

	private static String cs_PK = "CODIGO_EMPLEADO";

	private static String SQL_SELECT_ALL_SENTENCE = "SELECT " + cs_COLUMNAS + " FROM " + cs_TABLA + " ORDER BY CODIGO_EMPLEADO";

	private static String SQL_SELECT_BY_ESTADO_CARGA_SENTENCE = "SELECT " + cs_COLUMNAS + " FROM " + cs_TABLA + " WHERE (ESTADO_CARGA = ?)";

	private static String SQL_SELECT_BY_SENTENCE = "SELECT " + cs_COLUMNAS + " FROM " + cs_TABLA
			+ " WHERE (CODIGO_EMPLEADO LIKE ?) OR (NOMBRE_EMPLEADO LIKE ?) OR (CODIGO_AREA LIKE ?) OR (NOMBRE_AREA LIKE ?) OR (CODIGO_PERFIL LIKE ?) OR (NOMBRE_PERFIL LIKE ?) OR (CORREO_ELECTRONICO LIKE ?) ORDER BY CODIGO_EMPLEADO";

	private static int NUMERO_COLUMNAS_BUSQUEDA = 7;

	private static String SQL_UPDATE_ESTADO_CARGA_SENTENCE = "UPDATE " + cs_TABLA + " SET ESTADO_CARGA = ?";

	private static String SQL_UPDATE_SENTENCE = "UPDATE " + cs_TABLA
			+ " SET NOMBRE_EMPLEADO = ?, CODIGO_AREA = ?, NOMBRE_AREA = ?, CODIGO_PERFIL = ?, NOMBRE_PERFIL = ?, CORREO_ELECTRONICO = ?, ESTADO_CARGA = ?, FECHA_ULTIMA_CARGA = ? WHERE (CODIGO_EMPLEADO = ?)";

	private static String SQL_SELECT_CV_SENTENCE = "SELECT A.CODIGO CLAVE, B.CODIGO_EMPLEADO||' - '||B.NOMBRE_EMPLEADO VALOR FROM NC_USUARIO A LEFT JOIN NC_UNIDAD_ANALISIS UNA ON (A.CODIGO_AREA=UNA.CODIGO_SUCURSAL) LEFT JOIN NC_USUARIO_ALTAMIRA B ON (A.CODIGO_EMPLEADO = B.CODIGO_EMPLEADO) WHERE (UNA.CODIGO = ?) AND (A.CODIGO_ROL = ?) AND (A.ESTADO = ?) ORDER BY A.CODIGO_EMPLEADO";//

	public UsuarioAltamiraDAO() {
		super(cs_TABLA, cs_COLUMNAS, cs_PK, new UsuarioAltamira());
	}

	@Override
	public void internalUpdate( Connection con,UsuarioAltamira row) throws Exception {
		executeUpdate(con, SQL_UPDATE_SENTENCE, new Object[] { row.getNombreEmpleado(), row.getCodigoArea(), row.getNombreArea(), row.getCodigoPerfil(), row.getNombrePerfil(), row.getCorreoElectronico(), row.getEstadoCarga(), row.getFechaUltimaCarga(),
				row.getCodigoEmpleado() });
	}

	public void updateEstadoCarga(String estadoCarga) throws Exception {
		executeUpdate(SQL_UPDATE_ESTADO_CARGA_SENTENCE, estadoCarga);
	}

	public Collection<UsuarioAltamira> findAll() throws Exception {
		return findByGeneral(SQL_SELECT_ALL_SENTENCE);
	}

	public Collection<UsuarioAltamira> findByEstadoCarga(String estadoCarga) throws Exception {
		return findByGeneral(SQL_SELECT_BY_ESTADO_CARGA_SENTENCE, estadoCarga);
	}

	public Collection<UsuarioAltamira> findBy(String palabraClave) throws Exception {
		return findByKeyWord(SQL_SELECT_BY_SENTENCE, palabraClave, NUMERO_COLUMNAS_BUSQUEDA);
	}

	public Map<String, String> getCVUsuarioAltamira(Number codigoSucursal, int codPadrino, String estado) throws Exception {
		return getCV(SQL_SELECT_CV_SENTENCE, new Object[] { codigoSucursal, codPadrino, estado });
	}

	@Override
	protected Object[] getDataToAdd(@SuppressWarnings("unused") Connection con, UsuarioAltamira row) throws Exception {
		return new Object[] { row.getCodigoEmpleado(), row.getNombreEmpleado(), row.getCodigoArea(), row.getNombreArea(), row.getCodigoPerfil(), row.getNombrePerfil(), row.getCorreoElectronico(), row.getEstadoCarga(), row.getFechaUltimaCarga() };
	}

	@Override
	public UsuarioAltamira getResultSetToVO(ResultSet result) throws Exception {
		UsuarioAltamira row = new UsuarioAltamira();

		row.setCodigoEmpleado(result.getString(1));
		row.setNombreEmpleado(result.getString(2));
		row.setCodigoArea(result.getInt(3));
		row.setNombreArea(result.getString(4));
		row.setCodigoPerfil(result.getString(5));
		row.setNombrePerfil(result.getString(6));
		row.setCorreoElectronico(result.getString(7) != null ? result.getString(7) : "");
		row.setEstadoCarga(result.getString(8));
		row.setFechaUltimaCarga(result.getTimestamp(9));

		return row;
	}
}