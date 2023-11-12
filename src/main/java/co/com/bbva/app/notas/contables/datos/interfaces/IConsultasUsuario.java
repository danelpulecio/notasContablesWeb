package co.com.bbva.app.notas.contables.datos.interfaces;

public interface IConsultasUsuario {

	static String cs_COLUMNAS = "CODIGO, CODIGO_EMPLEADO, CODIGO_ROL, CODIGO_AREA, NOMBRE_AREA, CODIGO_PERFIL, NOMBRE_PERFIL, ACTUALIZAR_AUT, EMAIL_USUARIO, ESTADO , LAST_UPDATE ";

	static String cs_TABLA = "NC_USUARIO";

	static String cs_PK = "CODIGO";

	static String SQL_SELECT_BY_ESTADO_SENTENCE = "SELECT " + cs_COLUMNAS + " FROM " + cs_TABLA + " WHERE (ESTADO = ?) ORDER BY CODIGO_EMPLEADO";

	static String SQL_SELECT_BY_ID_AND_ROL_SENTENCE = "SELECT " + cs_COLUMNAS + " FROM " + cs_TABLA + " WHERE CODIGO_EMPLEADO = ? AND CODIGO_ROL = ? AND ESTADO = 'A'";

	static String SQL_SELECT_BY_CODIGO_EMPLEADO_SENTENCE = "SELECT " + cs_COLUMNAS + " FROM " + cs_TABLA + " WHERE (UPPER(CODIGO_EMPLEADO) = ?) ";

	static String SQL_SELECT_ACTIVOS_BY_CODIGO_EMPLEADO_SENTENCE = SQL_SELECT_BY_CODIGO_EMPLEADO_SENTENCE + " AND  ESTADO = 'A'";

	static String SQL_SELECT_BY_ROL_AND_ESTADO_SENTENCE = "SELECT A.CODIGO, A.CODIGO_EMPLEADO, A.CODIGO_ROL, A.CODIGO_AREA, A.NOMBRE_AREA, A.CODIGO_PERFIL, A.NOMBRE_PERFIL, A.ACTUALIZAR_AUT, A.EMAIL_USUARIO, A.ESTADO FROM NC_USUARIO A, NC_USUARIO_ALTAMIRA B WHERE (A.CODIGO_EMPLEADO = B.CODIGO_EMPLEADO) AND (A.CODIGO_ROL = ?) AND (A.ESTADO = ?) ORDER BY A.CODIGO_EMPLEADO";

	static String SQL_SELECT_BY_AREA_AND_ROL_AND_ESTADO_SENTENCE = "SELECT A.CODIGO \"codigo\", A.CODIGO_EMPLEADO \"codigoEmpleado\", B.NOMBRE_EMPLEADO \"usuAlt__nombreEmpleado\", A.CODIGO_ROL \"codigoRol\", A.CODIGO_AREA \"codigoAreaModificado\", A.NOMBRE_AREA \"nombreAreaModificado\", A.CODIGO_PERFIL \"codigoPerfilModificado\", A.NOMBRE_PERFIL \"nombrePerfilModificado\", A.ACTUALIZAR_AUT \"actualizarAutomatico\", A.EMAIL_USUARIO \"eMailModificado\", A.ESTADO \"estado\" FROM NC_USUARIO A, NC_USUARIO_ALTAMIRA B WHERE (A.CODIGO_EMPLEADO = B.CODIGO_EMPLEADO) AND (A.CODIGO_AREA = ?) AND (A.CODIGO_ROL = ?) AND (A.ESTADO = ?) ORDER BY A.CODIGO_EMPLEADO";

	static String SQL_SELECT_BY_ALTAMIRA_INACTIVOS_SENTENCE = "select ua.codigo_empleado \"usuAlt__codigoEmpleado\", ua.nombre_empleado \"usuAlt__nombreEmpleado\", u.codigo_area \"codigoAreaModificado\", u.nombre_area \"nombreAreaModificado\", u.codigo_perfil \"codigoPerfilModificado\", u.nombre_perfil \"nombrePerfilModificado\", r.nombre \"rol__nombre\" "
			+ "from NC_USUARIO U " + "LEFT JOIN NC_USUARIO_ALTAMIRA UA ON ua.codigo_empleado=u.codigo_empleado " + "LEFT JOIN NC_ROL R ON r.codigo=u.codigo_rol " + "WHERE ua.estado_carga = 'I' AND u.estado='A' ";

	static int NUMERO_COLUMNAS_BUSQUEDA = 7;

	static String SQL_UPDATE_SENTENCE = "UPDATE " + cs_TABLA + " SET CODIGO_EMPLEADO = ?, CODIGO_ROL = ?, CODIGO_AREA = ?, NOMBRE_AREA = ?, CODIGO_PERFIL = ?, NOMBRE_PERFIL = ?, ACTUALIZAR_AUT = ?, EMAIL_USUARIO = ?, ESTADO = ? ,  LAST_UPDATE = ? WHERE (CODIGO = ?)";

	static String SQL_UPDATE_ESTADO_SENTENCE = "UPDATE " + cs_TABLA + " SET  ESTADO = ? , LAST_UPDATE = ? WHERE CODIGO = ?";

	/**
	 * Consulta modificada para usar instrospeccion.
	 * 
	 * @ConsultaOriginal: "SELECT " + cs_COLUMNAS + " FROM " + cs_TABLA + " ORDER BY CODIGO_EMPLEADO";
	 */
	static String SQL_SELECT_ALL_SENTENCE = "SELECT u.codigo \"codigo\", " + //
			"ua.codigo_empleado \"codigoEmpleado\", " + //
			"r.codigo \"codigoRol\", " + //
			"ua.codigo_empleado \"usuAlt__codigoEmpleado\", " + //
			"ua.nombre_empleado \"usuAlt__nombreEmpleado\", " + //
			"u.codigo_area \"codigoAreaModificado\", " + //
			"u.nombre_area \"nombreAreaModificado\", " + //
			"u.codigo_perfil \"codigoPerfilModificado\", " + //
			"u.nombre_perfil \"nombrePerfilModificado\", " + //
			"u.email_usuario \"eMailModificado\", " + //
			"r.nombre \"rol__nombre\", " + //
			"u.actualizar_aut \"actualizarAutomatico\", " + //
			"u.estado \"estado\" " + //
			"FROM nc_usuario u LEFT JOIN nc_usuario_altamira ua ON u.codigo_empleado=ua.codigo_empleado LEFT JOIN nc_rol r ON u.codigo_rol=r.codigo";

	static String SQL_SELECT_ALL_BY_FILTER = SQL_SELECT_ALL_SENTENCE
			+ " WHERE ((ua.CODIGO_EMPLEADO LIKE ?) OR (u.CODIGO_AREA LIKE ?) OR (u.NOMBRE_AREA LIKE ?) OR (u.CODIGO_PERFIL LIKE ?) OR (u.NOMBRE_PERFIL LIKE ?) OR (r.NOMBRE LIKE ?) OR (ua.NOMBRE_EMPLEADO LIKE ?)) ORDER BY ua.CODIGO_EMPLEADO";

	static final String SQL_SELECT_POR_TIEMPOS = "SELECT US.CODIGO \"codigo\", US.CODIGO_AREA \"codigoAreaModificado\" , US.NOMBRE_AREA \"nombreAreaModificado\", USA.CODIGO_EMPLEADO \"codigoEmpleado\", USA.NOMBRE_EMPLEADO \"usuAlt__nombreEmpleado\", AVG(AR.DURACION_ACTIVIDAD) \"duracionPromedio\", RO.NOMBRE \"rol__nombre\""
			+ //
			" FROM NC_ACTIVIDAD_REALIZADA AR  " + //
			" INNER JOIN NC_USUARIO US ON US.CODIGO = AR.CODIGO_USUARIO " + //
			" INNER JOIN NC_USUARIO_ALTAMIRA USA ON US.CODIGO_EMPLEADO = USA.CODIGO_EMPLEADO " + "INNER JOIN NC_ROL RO ON RO.CODIGO = US.CODIGO_ROL " + //
			" WHERE AR.FECHA_HORA_CIERRE IS NOT NULL AND AR.FECHA_HORA_CIERRE <= ? " + //
			" GROUP BY US.CODIGO, US.CODIGO_AREA, US.NOMBRE_AREA, USA.CODIGO_EMPLEADO, USA.NOMBRE_EMPLEADO, RO.NOMBRE "; //

}
