package co.com.bbva.app.notas.contables.dao;

public interface IAuditoriaSentence {

	public static String cs_COLUMNAS = "CODIGO, FECHA_HORA, CODIGO_USUARIO, OPERACION, TIPO_REGISTRO, CODIGO_REGISTRO";

	public static String cs_TABLA = "NC_AUDITORIA";
	
	public static String cs_PK = "CODIGO";

	public static String SQL_SELECT_ALL_SENTENCE = "SELECT a.CODIGO \"codigo\", a.FECHA_HORA  \"fechaHora\", a.CODIGO_USUARIO \"codigoUsuario\", a.OPERACION \"operacion\", a.TIPO_REGISTRO \"tipoRegistro\", a.CODIGO_REGISTRO \"codigoRegistro\", case when u.codigo_empleado is null then 'sysadmin' else u.codigo_empleado end \"nombreUsuario\" FROM NC_AUDITORIA a left join nc_usuario u on a.codigo_usuario=u.codigo";
	
	/**COL521513I001176 VALIDA SI USUARIO ESTA LOGUEADO PARA REALIZAR CAMBIOS EN PERFIL**/
	public static String SQL_SELECT_ALL_SENTENCE_LOGUSUARIOSALIDA = "SELECT MAX(" + cs_PK + ") FROM " + cs_TABLA  + " WHERE (CODIGO_USUARIO = ?) AND (OPERACION LIKE '%SALIR%') ORDER BY FECHA_HORA ";
	
	/**COL514313I001449 ACTUALIZACION DE ESTADO LOGOUT A TODOS LOS USUARIOS PROCESO NOCTURNO**/
	public static String SQL_UPDATE_ALL_SENTENCE_LOGUSUARIOS = "UPDATE " + cs_TABLA  + " SET OPERACION = 'SALIR DE LA APLICACIN', TIPO_REGISTRO = 'LOGOUT', CODIGO_REGISTRO = 0 ";

	public static String SQL_SELECT_ALL_SENTENCE_LOGUSUARIOSINGRESO = "SELECT MAX(" + cs_PK + ") FROM " + cs_TABLA  + " WHERE (CODIGO_USUARIO = ?) AND (OPERACION LIKE '%INGRES%') ORDER BY FECHA_HORA ";
	
	public static String SQL_SELECT_BY_SENTENCE = SQL_SELECT_ALL_SENTENCE + " WHERE (UPPER(OPERACION) LIKE ?) OR (UPPER(TIPO_REGISTRO) LIKE ?) ";

	public static int NUMERO_COLUMNAS_BUSQUEDA = 2;

	public static String sql_INSERT_SENTENCE = "INSERT INTO " + cs_TABLA + " (" + cs_COLUMNAS + ") VALUES (?,?,?,?,?,?)";

	public static String sql_SELECT_MAX_CODE_SENTENCE = "SELECT MAX(" + cs_PK + ") FROM " + cs_TABLA;

}
