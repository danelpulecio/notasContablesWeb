package co.com.bbva.app.notas.contables.dao;

public interface IAuditoriaDetalleSentence {

	public static String cs_COLUMNAS = "CODIGO, CODIGO_AUDITORIA, REGISTRO_ORIGINAL, REGISTRO_MODIFICADO";

	public static String cs_TABLA = "NC_AUDITORIA_DETALLE";

	public static String cs_PK = "CODIGO";

	public static String SQL_SELECT_BY_CODIGO_AUDITORIA_SENTENCE = "SELECT " + cs_COLUMNAS + " FROM " + cs_TABLA + " WHERE (CODIGO_AUDITORIA = ?)";

	public static String sql_INSERT_SENTENCE = "INSERT INTO " + cs_TABLA + " (" + cs_COLUMNAS + ") VALUES (?,?,?,?)";

	public static String sql_SELECT_MAX_CODE_SENTENCE = "SELECT MAX(" + cs_PK + ") FROM " + cs_TABLA;

}
