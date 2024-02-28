package co.com.bbva.app.notas.contables.dao;

import co.com.bbva.app.notas.contables.dto.CommonVO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class CommonDAO<T extends CommonVO<T>> extends SuperDAO<T> {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(CommonDAO.class);

	private String sql_SELECT_MAX_CODE_SENTENCE = null;

	protected String sql_SELECT_SEQUENCE_SENTENCE = null;

	protected String sql_INSERT_SENTENCE = null;

	private String sql_DELETE_SENTENCE = null;

	private String sql_DELETE_ALL_SENTENCE = null;

	private int maxRows = 1000000000;

	protected abstract void internalUpdate(Connection con, T row) throws Exception;

	protected abstract Object[] getDataToAdd(Connection con, T row) throws Exception;

	protected abstract T getResultSetToVO(ResultSet result) throws Exception;

	protected CommonDAO(String tableName, String columnNames, String pkName, T instance) {
		super(instance);
		initResources(tableName, columnNames, pkName);
	}

	protected void initResources(String tableName, String columnNames, String pkName) {
		try {
			String par = "";
			for (int i = 0; i < columnNames.split("[,]").length; i++) {
				par += "?, ";
			}
			par = par.substring(0, par.length() - 2);
			sql_INSERT_SENTENCE = "INSERT INTO " + tableName + " (" + columnNames + ") VALUES (" + par + ")";
			sql_DELETE_ALL_SENTENCE = "DELETE FROM " + tableName;

			if (pkName != null && !pkName.isEmpty()) {
				sql_SELECT_BY_PRIMARY_KEY_SENTENCE = "SELECT " + columnNames + " FROM " + tableName + " WHERE (" + pkName + " = ?)";
				sql_DELETE_SENTENCE = "DELETE FROM " + tableName + " WHERE (" + pkName + " = ?)";
				sql_SELECT_MAX_CODE_SENTENCE = "SELECT MAX(" + pkName + ") FROM " + tableName;
				sql_SELECT_SEQUENCE_SENTENCE = "SELECT " + getSeqName(tableName) + ".nextval FROM DUAL";
			}
		} catch (Exception exception) {
			exception.printStackTrace();
		}
	}

	public void setDataSource(String as_dataSourceName) {
		JNDI_DATASOURCE_NAME = as_dataSourceName;
	}

	public String getDataSource() {
		return JNDI_DATASOURCE_NAME;
	}

	public void removeResources() {
		dataSource = null;
	}

	protected int getMaxCode(Connection con) throws Exception {
		return getMaxCode(con, sql_SELECT_MAX_CODE_SENTENCE);
	}

	protected String getSeqName(String tableName) {
		return tableName.replace("NC_", "SEQ_");
	}

	protected void internalAdd(Connection con, T row) throws Exception {
		executeUpdate(con, sql_INSERT_SENTENCE, getDataToAdd(con, row));
	}

	protected void internalDeleteAll(Connection con) throws Exception {
		executeUpdate(con, sql_DELETE_ALL_SENTENCE);
	}

	protected void internalDelete(Connection con, T row) throws Exception {
		executeUpdate(con, sql_DELETE_SENTENCE, row.getPK());
	}

	final public int addConAuditoria(T row) throws Exception {
		return add(row, 0);
	}

	final public int addSinAuditoria(T row) throws Exception {
		return add(row, -1);
	}

	final public int add(T row, int codigoUsuario) throws Exception {
		final Connection con = getConexion(false);
		try {
			int result = add(con, row, codigoUsuario);
			con.commit();
			return result;
		} catch (final Exception e) {
			con.rollback();
			throw e;
		} finally {
			closeConnection(con);
		}
	}

	/**
	 * Por defecto se genera auditora y auditora detalle
	 *
	 * @param row
	 * @param codigoUsuario
	 * @return
	 * @throws Exception
	 */
	final public int add(Connection con, T row, int codigoUsuario) throws Exception {
		LOGGER.info("CommonDAO : add : Connection : {}", con);
		LOGGER.info("CommonDAO : add : T row : {}", row);
		LOGGER.info("CommonDAO : add : codigoUsuario : {}", codigoUsuario);
		if (sql_INSERT_SENTENCE != null) {
			Object idAnt = row.getPK();
			LOGGER.info("CommonDAO : add : if : {}", idAnt);
			try {
				int id = 0;
				internalAdd(con, row);
				if (codigoUsuario >= 0) {
					if (sql_SELECT_MAX_CODE_SENTENCE != null) {
						id = ((Integer) row.getPK()).intValue();
					}
					/**
					 * BLOQUEO BASE DE DATOS
					 * int idAuditoria = addRegistroAuditoria(con, codigoUsuario, ACCION_ADICIONAR, instance.getClass().getSimpleName(), "" + id);
					 * if (instance.getPK() != null) {
					 *	String xmlDataOriginal = getXMLDataByPrimaryKey(con, row);
					 *	addRegistroAuditoriaDetalle(con, idAuditoria, xmlDataOriginal, "");
					 *}
					 **/
				}
				return id;
			} catch (Exception e) {
				// si hay excepcion, se restaura el codigo asignado previamente
				if (sql_SELECT_MAX_CODE_SENTENCE != null) {
					row.restartPK(idAnt);
				}
				throw e;
			}
		}
		throw new Exception("Se debe definir la llave primaria para realizar la acción");
	}

	final public void deleteAllSinAuditoria() throws Exception {
		deleteAll(-1);
	}

	final public void deleteAllConAuditoria() throws Exception {
		deleteAll(0);
	}

	final public void deleteAll(int codigoUsuario) throws Exception {
		final Connection con = getConexion(false);
		try {
			deleteAll(con, codigoUsuario);
			con.commit();
		} catch (final Exception e) {
			con.rollback();
			throw e;
		} finally {
			closeConnection(con);
		}
	}

	final public void deleteAll(Connection con, int codigoUsuario) throws Exception {
		if (sql_DELETE_ALL_SENTENCE != null) {
			internalDeleteAll(con);
			/**
			 // BLOQUEO BASE DE DATOS
			 if (codigoUsuario >= 0) {
			 addRegistroAuditoria(con, codigoUsuario, ACCION_ELIMINAR_TODAS, instance.getClass().getSimpleName(), "0");
			 }
			 **/
		} else {
			throw new Exception("Se debe definir la llave primaria para realizar la acción");
		}
	}

	final public void deleteConAuditoria(T row) throws Exception {
		delete(row, 0);
	}

	final public void deleteSinAuditoria(T row) throws Exception {
		delete(row, -1);
	}

	final public void delete(T row, int codigoUsuario) throws Exception {
		final Connection con = getConexion(false);
		try {
			delete(con, row, codigoUsuario);
			con.commit();
		} catch (final Exception e) {
			con.rollback();
			throw e;
		} finally {
			closeConnection(con);
		}
	}

	/**
	 * Por defecto genera auditora y auditora detalle
	 *
	 * @param row
	 * @param codigoUsuario
	 * @throws Exception
	 */
	final public void delete(Connection con, T row, int codigoUsuario) throws Exception {
		if (sql_DELETE_SENTENCE != null) {
			String xmlDataOriginal = "";

			if (codigoUsuario >= 0) {
				xmlDataOriginal = getXMLDataByPrimaryKey(con, row);
			}
			internalDelete(con, row);
			/**
			 // BLOQUEO BASE DE DATOS
			 if (codigoUsuario >= 0) {
			 int idAuditoria = addRegistroAuditoria(con, codigoUsuario, ACCION_ELIMINAR, instance.getClass().getSimpleName(), row.getPK().toString());
			 addRegistroAuditoriaDetalle(con, idAuditoria, xmlDataOriginal, "");
			 }
			 **/
		} else {
			throw new Exception("Se debe definir la llave primaria para realizar la acción");
		}
	}

	final public void updateConAuditoria(T row) throws Exception {
		update(row, -1);
	}

	final public void updateSinAuditoria(T row) throws Exception {
		update(row, 0);
	}

	final public void update(T row, int codigoUsuario) throws Exception {
		final Connection con = getConexion(false);
		try {
			update(con, row, codigoUsuario);
			con.commit();
		} catch (final Exception e) {
			con.rollback();
			throw e;
		} finally {
			closeConnection(con);
		}
	}

	/**
	 * Por defecto se genera auditora y auditora detalle
	 *
	 * @param row
	 * @param codigoUsuario
	 * @throws Exception
	 */
	final public void update(Connection con, T row, int codigoUsuario) throws Exception {
		if (sql_INSERT_SENTENCE != null) {
			String xmlDataOriginal = "";
			if (codigoUsuario >= 0) {
				xmlDataOriginal = getXMLDataByPrimaryKey(con, row);
			}
			internalUpdate(con, row);

			if (codigoUsuario >= 0) {

				// BLOQUEO BASE DE DATOS
				//int idAuditoria = addRegistroAuditoria(con, codigoUsuario, ACCION_EDITAR, instance.getClass().getSimpleName(), row.getPK().toString());

				//String xmlDataModificada = getXMLDataByPrimaryKey(con, row);
				//addRegistroAuditoriaDetalle(con, idAuditoria, xmlDataOriginal, xmlDataModificada);
			}
		} else {
			throw new Exception("Se debe definir la llave primaria para realizar la acción");
		}
	}

	final public void update(T row, int codigoUsuario, String sentence, Object[] params) throws Exception {
		final Connection con = getConexion(false);
		try {
			update(con, row, codigoUsuario, sentence, params);
			con.commit();
		} catch (final Exception e) {
			con.rollback();
			throw e;
		} finally {
			closeConnection(con);
		}
	}

	final public void update(Connection con, T row, int codigoUsuario, String sentence, Object[] params) throws Exception {
		if (sql_INSERT_SENTENCE != null) {
			String xmlDataOriginal = "";
			if (codigoUsuario >= 0) {
				xmlDataOriginal = getXMLDataByPrimaryKey(con, row);
			}

			executeUpdate(con, sentence, params);

			if (codigoUsuario >= 0) {
				// BLOQUEO BASE DE DATOS
				//int idAuditoria = addRegistroAuditoria(con, codigoUsuario, ACCION_EDITAR, instance.getClass().getSimpleName(), row.getPK().toString());
				//String xmlDataModificada = getXMLDataByPrimaryKey(con, row);
				//addRegistroAuditoriaDetalle(con, idAuditoria, xmlDataOriginal, xmlDataModificada);
			}
		} else {
			throw new Exception("Se debe definir la llave primaria para realizar la acción");
		}
	}

	public T findByPrimaryKey(T row) throws Exception {
		Connection con = null;
		try {
			con = getConexion();

			return findByPrimaryKey(con, row);
		} catch (final Exception e) {
			throw e;
		} finally {
			closeConnection(con);
		}
	}

	@SuppressWarnings("unchecked")
	public T findByPrimaryKey(Connection con, T row) throws Exception {
		if (sql_SELECT_BY_PRIMARY_KEY_SENTENCE != null) {
			T ret = getByGeneral(con, sql_SELECT_BY_PRIMARY_KEY_SENTENCE, row.getPK());
			if (ret == null) {
				try {
					ret = (T) instance.getClass().newInstance();
				} catch (Exception e) {
					e.printStackTrace();
					throw new Exception("Ha existido un error al crear una nueva instancia del elemento", e);
				}
			}
			return ret;
		}
		throw new Exception("Se debe definir la llave primaria para realizar la acción");
	}

	public T getByGeneral(String sentence) throws Exception {
		Connection con = null;
		try {
			con = getConexion();

			return getByGeneral(con, sentence);
		} catch (final Exception e) {
			throw e;
		} finally {
			closeConnection(con);
		}
	}

	public T getByGeneral(Connection con, String sentence) throws Exception {
		return getByGeneral(con, sentence, new Object[] {});
	}

	public T getByGeneral(String sentence, Object parm) throws Exception {
		Connection con = null;
		try {
			con = getConexion();

			return getByGeneral(con, sentence, parm);
		} catch (final Exception e) {
			throw e;
		} finally {
			closeConnection(con);
		}
	}

	public T getByGeneral(Connection con, String sentence, Object parm) throws Exception {
		return getByGeneral(con, sentence, new Object[] { parm });
	}

	public T getByGeneral(String sentence, Object[] params) throws Exception {
		Connection con = null;
		try {
			con = getConexion();

			return getByGeneral(con, sentence, params);
		} catch (final Exception e) {
			throw e;
		} finally {
			closeConnection(con);
		}
	}

	public T getByGeneral(Connection con, String sentence, Object[] params) throws Exception {
		return getByGeneral(con, sentence, params, 0);
	}

	public T getByGeneral(String sentence, Object[] params, int transformationKind) throws Exception {
		Connection con = null;
		try {
			con = getConexion();

			return getByGeneral(con, sentence, params, transformationKind);
		} catch (final Exception e) {
			throw e;
		} finally {
			closeConnection(con);
		}
	}

	public T getByGeneral(Connection con, String sentence, Object[] params, int transformationKind) throws Exception {
		PreparedStatement ps = null;
		ResultSet result = null;
		try {
			imprimirInfo("getByGeneral", sentence, params);
			for (Object o : params) {
				print(o.toString());
			}
			ps = con.prepareStatement(sentence);
			for (int i = 0; i < params.length; i++) {
				addParameter(ps, i + 1, params[i]);
			}
			result = ps.executeQuery();
			if (result.next()) {
				try {
					if (transformationKind == 0) {
						return getResultSetToVO(result);
					}
					return getResultSetToVO(result, transformationKind);
				} catch (Exception e) {
					println("Error al convertir resultado a TO ");
					e.printStackTrace();
					throw e;
				}
			}
			return instance;
		} catch (Exception exception) {
			throw exception;
		} finally {
			if (result != null) {
				result.close();
			}
			if (ps != null) {
				ps.close();
			}
		}
	}

	public Collection<T> findByGeneral(String sentence) throws Exception {
		Connection con = null;
		try {
			con = getConexion();

			return findByGeneral(con, sentence);
		} catch (final Exception e) {
			throw e;
		} finally {
			closeConnection(con);
		}
	}

	public Collection<T> findByGeneral(Connection con, String sentence) throws Exception {
		return findByGeneral(con, sentence, new Object[] {});
	}

	public Collection<T> findByGeneral(String sentence, Object param) throws Exception {
		Connection con = null;
		try {
			con = getConexion();

			return findByGeneral(con, sentence, param);
		} catch (final Exception e) {
			throw e;
		} finally {
			closeConnection(con);
		}
	}

	public Collection<T> findByGeneral(Connection con, String sentence, Object param) throws Exception {
		return findByGeneral(con, sentence, new Object[] { param });
	}

	public Collection<T> findByGeneral(String sentence, Object[] params) throws Exception {
		Connection con = null;
		try {
			con = getConexion();

			return findByGeneral(con, sentence, params);
		} catch (final Exception e) {
			throw e;
		} finally {
			closeConnection(con);
		}
	}

	public Collection<T> findByGeneral(Connection con, String sentence, Object[] params) throws Exception {
		return findByGeneral(con, sentence, params, 0);
	}

	public Collection<T> findByGeneral(String sentence, Object[] params, int transformationKind) throws Exception {
		Connection con = null;
		try {
			con = getConexion();

			return findByGeneral(con, sentence, params, transformationKind);
		} catch (final Exception e) {
			throw e;
		} finally {
			closeConnection(con);
		}
	}

	public Collection<T> findByGeneral(Connection con, String sentence, Object[] params, int transformationKind) throws Exception {
		ArrayList<T> rows = new ArrayList<T>();
		PreparedStatement ps = null;
		ResultSet result = null;
		try {
			imprimirInfo("findByGeneral", sentence, params);
			for (Object o : params) {
				print(o.toString());
			}
			ps = con.prepareStatement(sentence);
			for (int i = 0; i < params.length; i++) {
				addParameter(ps, i + 1, params[i]);
			}
			ps.setMaxRows(maxRows);
			result = ps.executeQuery();
			while (result.next()) {
				try {
					if (transformationKind == 0) {
						rows.add(getResultSetToVO(result));
					} else {
						rows.add(getResultSetToVO(result, transformationKind));
					}
				} catch (Exception e) {
					println("Error al convertir resultado a TO ");
					e.printStackTrace();
					throw e;
				}
			}
			return rows;
		} catch (Exception exception) {
			throw exception;
		} finally {
			if (result != null) {
				result.close();
			}
			if (ps != null) {
				ps.close();
			}
		}
	}

	public Collection<String> findToStringByGeneral(String sentence, Object[] params) throws Exception {
		Connection con = null;
		try {
			con = getConexion();

			return findToStringByGeneral(con, sentence, params);
		} catch (final Exception e) {
			throw e;
		} finally {
			closeConnection(con);
		}
	}

	public Collection<String> findToStringByGeneral(Connection con, String sentence, Object[] params) throws Exception {
		ArrayList<String> rows = new ArrayList<String>();
		PreparedStatement ps = null;
		ResultSet result = null;
		try {
			long time = imprimirInfo("findByGeneral", sentence, params);
			for (Object o : params) {
				print(o.toString());
			}
			ps = con.prepareStatement(sentence);
			for (int i = 0; i < params.length; i++) {
				addParameter(ps, i + 1, params[i]);
			}
			ps.setMaxRows(0);
			result = ps.executeQuery();
			while (result.next()) {
				rows.add(result.getString(1));
			}
			println("duracin: " + (System.currentTimeMillis() - time));
			return rows;
		} catch (Exception exception) {
			throw exception;
		} finally {
			if (result != null) {
				result.close();
			}
			if (ps != null) {
				ps.close();
			}
		}
	}

	public Collection<T> findByKeyWord(String sentence, String keyWord, int cantSearchColumns) throws Exception {
		Connection con = null;
		try {
			con = getConexion();

			return findByKeyWord(con, sentence, keyWord, cantSearchColumns);
		} catch (final Exception e) {
			throw e;
		} finally {
			closeConnection(con);
		}
	}

	public Collection<T> findByKeyWord(Connection con, String sentence, String keyWord, int cantSearchColumns) throws Exception {
		ArrayList<String> params = new ArrayList<String>();
		for (int count = 0; count < cantSearchColumns; count++) {
			params.add("%" + keyWord + "%");
		}
		return findByGeneral(con, sentence, params.toArray());
	}

	protected T getResultSetToVO(@SuppressWarnings("unused") ResultSet result, @SuppressWarnings("unused") int idKind) throws Exception {
		throw new Exception("Este mtodo no ha sido implementado");
	}

	/********************************* METODOS DE CONSULTAS POR INTROSPECCION **********************************/

	public T obtenerObjeto(final String query, final Object... params) throws Exception {
		Connection con = null;
		try {
			con = getConexion();
			//System.out.println("** QUERY "+ query);
			return obtenerObjeto(con, query, params);
		} catch (final Exception e) {
			throw e;
		} finally {
			closeConnection(con);
		}
	}

	public T obtenerObjeto(Connection con, final String query, final Object... params) throws Exception {
		try {
			long time = imprimirInfo("obtenerObjeto", query, params);
			final List<Map<String, Object>> list = ejecutarSentenciaSQL(con, 1, query, params);
			if (list != null && !list.isEmpty()) {
				println("duracin: " + (System.currentTimeMillis() - time));
				return ObtenerEntidad(list.get(0), instance);
			}
			println("duracin: " + (System.currentTimeMillis() - time));
			return instance;
		} catch (Exception exception) {
			throw exception;
		}
	}

	/**
	 * Retorna una lista de objetos de tipo T, seteando automaticamente los resultados mediante introspeccion
	 *
	 * @param query
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public List<T> obtenerLista(final String query, final Object... params) throws Exception {
		Connection con = null;
		try {
			con = getConexion();

			return obtenerLista(con, query, params);
		} catch (final Exception e) {
			throw e;
		} finally {
			closeConnection(con);
		}
	}

	public List<T> obtenerLista(final Connection con, final String query, final Object... params) throws Exception {
		try {
			long time = imprimirInfo("obtenerLista", query, params);
			// se ejecuta la consulta
			final List<Map<String, Object>> list = ejecutarSentenciaSQL(con, maxRows, query, params);
			// se construyen la lista de objetos a retornar usando instrospeccion
			if (list != null && !list.isEmpty()) {
				final List<T> objetos = ObtenerLista(list, instance);
				println("duracin: " + (System.currentTimeMillis() - time));
				return objetos;
			}
			println("duracin: " + (System.currentTimeMillis() - time));
			return new ArrayList<T>();
		} catch (Exception exception) {
			throw exception;
		}
	}

	private long imprimirInfo(String name, String sentence, Object[] params) {
		long time = System.currentTimeMillis();
		println("Ejecucin de " + name + " de la clase " + getClass().getSimpleName());
		println("Sentencia:\t" + sentence);
		if (params.length > 0) {
			print("Parmetros:\t");
			for (Object o : params) {
				print("[" + o.toString() + "] ");
			}
			println();
		}
		return time;
	}

	private void println(String string) {
		//System.out.println(string);
		/** MODIFICACION DE LOGS EN ARCHIVO INDEPENDIENTE**/
		//String linea = string;
		//Log.escribirLogInfo(string);
	}

	private void println() {
		println("");
	}

	private void print(String string) {
		//System.out.print(string);
		/** MODIFICACION DE LOGS EN ARCHIVO INDEPENDIENTE**/
		//String linea = string;
		//Log.escribirLogInfo(string);
	}

	protected void setMaxRows(int maxRows) {
		this.maxRows = maxRows;
	}

}
