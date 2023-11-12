package co.com.bbva.app.notas.contables.dao;

import co.com.bbva.app.notas.contables.anotacion.Columna;
import co.com.bbva.app.notas.contables.anotacion.ColumnaId;
import co.com.bbva.app.notas.contables.anotacion.Tabla;
import co.com.bbva.app.notas.contables.datos.utilidades.DBUtils;
import co.com.bbva.app.notas.contables.dto.Auditoria;
import co.com.bbva.app.notas.contables.dto.AuditoriaDetalle;
import co.com.bbva.app.notas.contables.dto.CommonVO;
import co.com.bbva.app.notas.contables.util.DateUtils;

import javax.naming.InitialContext;
import javax.sql.DataSource;
import java.lang.reflect.Field;
import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class SuperDAO<T extends CommonVO<T>> extends DBUtils<T> implements IAuditoriaDetalleSentence, IAuditoriaSentence {
	protected DataSource dataSource = null;

	protected static String JNDI_DATASOURCE_NAME = "jdbc/notasContables";

	protected static final String CLAVE = " CLAVE ";
	protected static final String VALOR = " VALOR ";
	protected static final String COMA = ", ";
	protected static final String COMODIN = "%";
	private final String selectClaveValorQuery;
	private final String selectClaveValorByQuery;

	protected final T instance;
	// informacin importante de las anotaciones de cada clase
	protected final String TABLE_NAME;
	// comodines para reemplazar por la informacion adecuada
	protected static final String COMODIN_COLUMNAS = ":columnas";
	protected static final String COMODIN_TABLA = ":tabla";
	protected static final String COMODIN_ESTADO = ":estado";
	protected static final String COMODIN_ORDER_BY = ":order";
	// select por clave valor para mostrar en combos
	private static final String SELECT_CV_STRING = "SELECT " + COMODIN_COLUMNAS + " \nFROM " + COMODIN_TABLA + COMODIN_ESTADO + " \nORDER BY :order";

	public static final String ACCION_EDITAR = "Edicin";
	public static final String ACCION_EDITAR_TODAS = "Edicin Todas";
	public static final String ACCION_EDITAR_SELECCIONADAS = "Edicin Seleccionadas";
	public static final String ACCION_ELIMINAR = "Eliminacin";
	public static final String ACCION_ELIMINAR_TODAS = "Eliminacin Todas";
	public static final String ACCION_ADICIONAR = "Prueba Adicin";

	protected String sql_SELECT_BY_PRIMARY_KEY_SENTENCE = null;
	protected String sql_UPDATE_STATE_SENTENCE = null;
	private static String SQL_UPDATE_ESTADO_CARGA_SENTENCE = "UPDATE " + COMODIN_TABLA + " SET ESTADO_CARGA = ?";
	private static String SQL_DELETE_ALL_SENTENCE = "DELETE FROM " + COMODIN_TABLA;

	private final String COLUMN_NAMES;

	public SuperDAO(T instance) {
		try {
			InitialContext lic_context = new InitialContext();
			dataSource = (DataSource) lic_context.lookup(getJndiDatasourceName());
		} catch (Exception exception) {
			exception.printStackTrace();
		}
		this.instance = instance;
		if (instance != null && instance.getClass().getAnnotation(Tabla.class) != null) {
			List<String> columnasDB = new ArrayList<String>();
			List<String> columnasApp = new ArrayList<String>();
			TABLE_NAME = instance.getClass().getAnnotation(Tabla.class).nombreTabla();
			String clave = "";
			String valor = "";
			String estadoDB = "";
			String pkNameDB = "";
			@SuppressWarnings("unused")
			String pkNameApp = "";
			for (final Field field : this.instance.getClass().getDeclaredFields()) {
				final Columna col = field.getAnnotation(Columna.class);
				if (col != null) {
					if (col.esClave()) {
						clave = col.nombreDB();
					}
					if (col.esValor()) {
						valor = col.nombreDB();
					}
					if (col.esEstado()) {
						estadoDB = col.nombreDB();
					}
					columnasDB.add(col.nombreDB());
					columnasApp.add(col.nombreApp());
					final ColumnaId colId = field.getAnnotation(ColumnaId.class);
					if (colId != null) {
						pkNameDB = col.nombreDB();
						pkNameApp = col.nombreApp();
					}
				}
			}
			String columnNames = "";
			for (String str : columnasDB) {
				columnNames += str + COMA;
			}
			if (columnNames.length() > COMA.length()) {
				columnNames = columnNames.substring(0, columnNames.length() - COMA.length());
			}
			COLUMN_NAMES = columnNames;
			// generacion de consultas genericas
			selectClaveValorQuery = generarSelectClaveValorQuery(clave, valor, estadoDB);
			selectClaveValorByQuery = generarSelectClaveValorByQuery(clave, valor, estadoDB);

			sql_SELECT_BY_PRIMARY_KEY_SENTENCE = "SELECT " + COLUMN_NAMES + " FROM " + TABLE_NAME + " WHERE (" + pkNameDB + " = ?)";
			sql_UPDATE_STATE_SENTENCE = "UPDATE " + TABLE_NAME + " SET " + estadoDB + " = ? WHERE (" + pkNameDB + " =?)";
		} else {
			selectClaveValorQuery = null;
			selectClaveValorByQuery = null;
			TABLE_NAME = null;
			COLUMN_NAMES = null;
		}
	}

	protected String getJndiDatasourceName() {
		return JNDI_DATASOURCE_NAME;
	}

	protected DataSource getDatasource() throws Exception {
		if (getJndiDatasourceName() == null) {
			throw new Exception("No se ha encontrado la referencia al recurso " + getJndiDatasourceName());
		}
		return dataSource;
	}

	public Connection getConexion() throws Exception {
		return getConexion(true);
	}

	public Connection getConexion(boolean autoCommit) throws Exception {
		Connection con = getDatasource().getConnection();
		con.setAutoCommit(autoCommit);
		return con;
	}

	public void closeConnection(Connection con) throws Exception {
		if (con != null && !con.isClosed()) {
			con.close();
		}
	}

	protected Exception roolbak(final Connection con, final Exception e) {
		try {
			con.rollback();
		} catch (final Exception ex) {
			return ex;
		}
		return e;
	}

	// consulta para buscar por clave valor para pintar en combos
	private String generarSelectClaveValorQuery(final String clave, final String valor, String estado) {
		return generarSelectClaveValorQuery(getSelectCVString(), clave, valor, estado, "");
	}

	private String generarSelectClaveValorByQuery(final String clave, final String valor, String estado) {
		return generarSelectClaveValorQuery(getSelectCVByString(), clave, valor, estado, clave + " LIKE ?");
	}

	private String generarSelectClaveValorQuery(final String consulta, final String clave, final String valor, String estado, String filtro) {

		if (clave.length() > 0 && valor.length() > 0) {
			String query = consulta.replaceAll(COMODIN_COLUMNAS, clave + CLAVE + COMA + valor + VALOR);
			query = query.replaceAll(COMODIN_TABLA, TABLE_NAME);
			query = query.replaceAll(COMODIN_ORDER_BY, valor);

			// soporte para filtrar la consulta cuando la tabla cuenta con estado de activacion
			String filtroEstado = "";
			if (estado.length() > 0) {
				filtroEstado = " WHERE " + estado + "= 'A'";
				if (filtro.length() > 0) {
					filtroEstado += " AND " + filtro;
				}
			} else if (filtro.length() > 0) {
				filtroEstado += " WHERE " + filtro;
			}
			query = query.replaceAll(COMODIN_ESTADO, filtroEstado);
			return query;
		}
		return null;
	}

	/**
	 * Este mtodo ser sobreescrito en casos de necesitar establecer funcionalidad especfica en la consulta de bsqueda por comodn. Ver <code>PerfilDAO</code>
	 * 
	 * @return
	 */
	protected String getSelectCVString() {
		return SELECT_CV_STRING;
	}

	protected String getSelectCVByString() {
		return SELECT_CV_STRING;
	}

	/**
	 * Retorna un mapa clave valor sobre la tabla actual
	 * 
	 * @return
	 * @throws Exception
	 */
	public Map<String, String> getCV() throws Exception {
		Connection con = null;
		try {
			con = getConexion();

			return getCV(con, selectClaveValorQuery);
		} catch (final Exception e) {
			throw e;
		} finally {
			closeConnection(con);
		}
	}

	/**
	 * Retorma un mapa clave valor sobre la tabla actual, previa ejecucin de un filtro
	 * 
	 * @param filtro
	 * @return
	 * @throws Exception
	 */
	public Map<String, String> getCVBy(String filtro) throws Exception {
		Connection con = null;
		try {
			con = getConexion();

			return getCV(con, selectClaveValorByQuery, "%" + filtro + "%");
		} catch (final Exception e) {
			throw e;
		} finally {
			closeConnection(con);
		}
	}

	public Map<String, String> getCV(final String query, final Object... params) throws Exception {
		Connection con = null;
		try {
			con = getConexion();
			return getCV(con, query, params);
		} catch (final Exception e) {
			throw e;
		} finally {
			closeConnection(con);
		}
	}

	public Map<String, String> getCV(Connection con, final String query, final Object... params) throws Exception {
		if (query == null) {
			throw new Exception("No se puede obtener el mapa clave valor. Indique la clave y el valor en el vo");
		}
		try {
			final Map<String, String> cvMap = new LinkedHashMap<String, String>();
			final List<Map<String, Object>> list = ejecutarSentenciaSQL(con, 0, query, params);
			if (list != null && !list.isEmpty()) {
				for (final Map<String, Object> map : list) {
					cvMap.put(map.get(CLAVE.trim()).toString(), map.get(VALOR.trim()).toString());
				}
			}
			return cvMap;
		} catch (final Exception e) {
			throw e;
		}
	}

	/**
	 * Cambia el estado de un objeto, haciendo commit
	 * 
	 * @param row
	 * @param codigoUsuario
	 * @throws Exception
	 */
	public void changeEstado(T row, int codigoUsuario) throws Exception {
		final Connection con = getConexion(false);
		try {
			changeEstado(con, row, codigoUsuario);
			con.commit();
		} catch (final Exception e) {
			con.rollback();
			throw e;
		} finally {
			closeConnection(con);
		}
	}

	/**
	 * Cambia el estado de un objeto (No hace commit, asume que un metodo superior lo hace)
	 * 
	 * @param con
	 * @param row
	 * @param codigoUsuario
	 * @throws Exception
	 */
	public void changeEstado(Connection con, T row, int codigoUsuario) throws Exception {
		if (row.getEstado().equals("A")) {
			row.setEstado("I");
		} else {
			row.setEstado("A");
		}
		updateEstado(con, row, codigoUsuario);
	}

	private void updateEstado(Connection con, T row, int codigoUsuario) throws Exception {
		
		if (sql_UPDATE_STATE_SENTENCE != null && COLUMN_NAMES != null && TABLE_NAME != null) {

			// se obtiene la informacion original del registro
			String xmlDataOriginal = getXMLDataByPrimaryKey(con, row);

			// se actualiza el estado
			executeUpdate(con, sql_UPDATE_STATE_SENTENCE, new Object[] { row.getEstado(), row.getPK() });
			
			/** BLOQUEO BASE DE DATOS **/
			/**
			// se guarda el registro de auditoria
			if (!xmlDataOriginal.isEmpty()) {
				int idAuditoria = addRegistroAuditoria(con, codigoUsuario, ACCION_EDITAR, instance.getClass().getSimpleName(), row.getPK().toString());
				String xmlDataModificada = getXMLDataByPrimaryKey(con, row);
				addRegistroAuditoriaDetalle(con, idAuditoria, xmlDataOriginal, xmlDataModificada);
			}
			**/
		} 
		else {
			throw new Exception("Se debe definir la llave primaria para realizar la accin");
		}
	}

	public void executeUpdate(String sentence) throws Exception {
		executeUpdate(sentence, new Object[] {});
	}

	public void executeUpdate(String sentence, Object param) throws Exception {
		executeUpdate(sentence, new Object[] { param });
	}

	public void executeUpdate(String sentence, Object[] params) throws Exception {
		final Connection con = getConexion(false);
		try {
			executeUpdate(con, sentence, params);
			con.commit();
		} catch (final Exception e) {
			con.rollback();
			throw e;
		} finally {
			closeConnection(con);
		}
	}

	public void executeUpdate(Connection con, String sentence) throws Exception {
		executeUpdate(con, sentence, new Object[] {});
	}

	public void executeUpdate(Connection con, String sentence, Object param) throws Exception {
		executeUpdate(con, sentence, new Object[] { param });
	}

	public void executeUpdate(Connection con, String sentence, Object[] params) throws Exception {
		PreparedStatement preparedStatementSentence = null;
		try {
			preparedStatementSentence = con.prepareStatement(sentence);
			for (int i = 0; i < params.length; i++) {
				addParameter(preparedStatementSentence, i + 1, params[i]);
			}
			
			
			preparedStatementSentence.executeUpdate();
		} finally {
			if (preparedStatementSentence != null) {
				preparedStatementSentence.close();
			}
		}
	}

	public void updateEstadoCarga(String tabla, String estadoCarga) throws Exception {
		String query = SQL_UPDATE_ESTADO_CARGA_SENTENCE.replaceAll(COMODIN_TABLA, tabla);
		executeUpdate(query, estadoCarga);
	}

	public void deleteAll(String tabla) throws Exception {
		String query = SQL_DELETE_ALL_SENTENCE.replaceAll(COMODIN_TABLA, tabla);
		executeUpdate(query);
	}

	protected void addParameter(PreparedStatement preparedStatementSentence, int i, Object object) throws Exception {
		if (object instanceof String) {
			object = ((String) object).toUpperCase();
			preparedStatementSentence.setString(i, object.toString());
		} else {
			preparedStatementSentence.setObject(i, object);
		}
	}

	protected synchronized int getMaxCode(Connection con, String sentenceGetCode) throws Exception {
		int li_maxCode = 0;
		ResultSet result = null;
		Statement statement = null;
		try {
			statement = con.createStatement();
			result = statement.executeQuery(sentenceGetCode);
			if (result.next()) {
				li_maxCode = result.getInt(1);
			}

		} catch (Exception e) {
			throw e;
		} finally {
			if (statement != null) {
				statement.close();
			}
		}
		li_maxCode++;

		return li_maxCode;
	}

	public String getXMLDataByPrimaryKey(Connection con, T row) throws Exception {
		if (sql_SELECT_BY_PRIMARY_KEY_SENTENCE != null) {
			return getXMLDataGeneral(con, sql_SELECT_BY_PRIMARY_KEY_SENTENCE, row.getPK());
		}
		throw new Exception("Se debe definir la llave primaria para realizar la accin");
	}

	public final String getXMLDataGeneral(Connection con, String sentence) throws Exception {
		return getXMLDataGeneral(con, sentence, new Object[] {});
	}

	public final String getXMLDataGeneral(Connection con, String sentence, Object params) throws Exception {
		return getXMLDataGeneral(con, sentence, new Object[] { params });
	}

	public final String getXMLDataGeneral(Connection con, String sentence, Object[] params) throws Exception {
		String xmlData = "";
		PreparedStatement preparedStatementSentence = null;
		ResultSet result = null;
		try {
			preparedStatementSentence = con.prepareStatement(sentence);
			for (int i = 0; i < params.length; i++) {
				addParameter(preparedStatementSentence, i + 1, params[i]);
			}
			result = preparedStatementSentence.executeQuery();
			if (result.next()) {
				xmlData = getResultSetToStringXML(result);
			}
			return xmlData;
		} catch (Exception exception) {
			throw exception;
		} finally {
			if (result != null) {
				result.close();
			}
			if (preparedStatementSentence != null) {
				preparedStatementSentence.close();
			}
		}
	}
	
	/**COL521513I001176 CONSULTA USUARIO DE ACUERDO AL PARAMETRO QUE SE PASA **/
	public final String getConsultaGeneral(Connection con, String sentence, Object codigoEmpleado) throws Exception {
		String xmlData = "";
		PreparedStatement preparedStatementSentence = null;
		ResultSet result = null;
		try {
			preparedStatementSentence = con.prepareStatement(sentence);
			preparedStatementSentence.setString(1, codigoEmpleado.toString());
			result = preparedStatementSentence.executeQuery();
			if (result.next()) {
				xmlData = CodigoMaximoUsuario(result);
			}
			return xmlData;
		} catch (Exception exception) {
			throw exception;
		} finally {
			if (result != null) {
				result.close();
			}
			if (preparedStatementSentence != null) {
				preparedStatementSentence.close();
			}
		}
	}
	
	public final String getResultSetToStringXML(ResultSet result) throws Exception {
		ResultSetMetaData metaData = result.getMetaData();

		String xmlData = "";
		int numeroColumnas = metaData.getColumnCount();
		int count = 0;

		for (count = 1; count <= numeroColumnas; count++) {
			xmlData += "<b>" + metaData.getColumnName(count) + "</b>: " + result.getString(count) + "<br/>";
		}

		return xmlData;
	}
	
	
	/**COL521513I001176 GUARDA EL DATO GENERADO EN LA CONSULTA Y ALMACENA EN VARIABLE **/
	public final String CodigoMaximoUsuario(ResultSet result) throws Exception {
		ResultSetMetaData metaData = result.getMetaData();
		String xmlData = "";
		int numeroColumnas = metaData.getColumnCount();
		int count = 0;
		for (count = 1; count <= numeroColumnas; count++) {
			xmlData = result.getString(count);
		}
		return xmlData;
	}

	/** COL472313S318150 CONSULTA DE LOS OBJETOS QUE SE LLAMAN EN TODO EL APLICATIVO Y GENERA BLOQUEO BASE DE DATOS **/
	public int addRegistroAuditoria(Connection con, int codigoUsuario, String operacion, String tipoRegistro, String codigoRegistro) throws Exception {
		Auditoria row = new Auditoria();

		row.setCodigoUsuario(codigoUsuario);
		row.setOperacion(operacion);
		row.setTipoRegistro(tipoRegistro);
		row.setCodigoRegistro(codigoRegistro);
		row.setCodigo(getMaxCode(con, IAuditoriaSentence.sql_SELECT_MAX_CODE_SENTENCE));
		executeUpdate(con, IAuditoriaSentence.sql_INSERT_SENTENCE, new Object[] { row.getCodigo(), DateUtils.getTimestamp(), row.getCodigoUsuario(), row.getOperacion(), row.getTipoRegistro(), row.getCodigoRegistro() });
		return row.getCodigo().intValue();
	}

	// AuditoriaDetalle
	public void addRegistroAuditoriaDetalle(Connection con, int codigoAuditoria, String registroOriginal, String registroModificado) throws Exception {
		if (!registroOriginal.isEmpty()) {
			AuditoriaDetalle row = new AuditoriaDetalle();

			row.setCodigoAuditoria(codigoAuditoria);
			row.setRegistroOriginal(registroOriginal);
			row.setRegistroModificado(registroModificado);
			row.setCodigo(getMaxCode(con, IAuditoriaDetalleSentence.sql_SELECT_MAX_CODE_SENTENCE));

			executeUpdate(con, IAuditoriaDetalleSentence.sql_INSERT_SENTENCE, new Object[] { row.getCodigo(), row.getCodigoAuditoria(), row.getRegistroOriginal(), row.getRegistroModificado() });
		}

	}
	
}
