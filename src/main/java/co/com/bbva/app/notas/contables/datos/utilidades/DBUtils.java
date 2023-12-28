package co.com.bbva.app.notas.contables.datos.utilidades;

//import oracle.sql.TIMESTAMP;
import java.sql.*;

import oracle.sql.TIMESTAMP;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.management.RuntimeErrorException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.*;
import java.util.Date;

public class DBUtils<T> {
	private static final boolean MOSTRAR_SQL = false;

	protected static final String PREFIJO_SET = "set";
	protected static final String PREFIJO_GET = "get";
	final static String BREAKER = "__";
	final static TreeMap<String, Method> cache = new TreeMap<String, Method>();

	private static final Logger LOGGER = LoggerFactory.getLogger(DBUtils.class);

	@SuppressWarnings("unchecked")
	protected T ObtenerEntidad(final Map<String, Object> map, final T returnType) throws Exception {
		T ret = null;
		if (map == null) {
			return ret;
		}
		try {
			ret = (T) returnType.getClass().newInstance();
			for (final String fieldName : map.keySet()) {
				setearValor(ret, fieldName, map.get(fieldName));
			}
		} catch (final Exception e) {
			e.printStackTrace();
			throw new Exception(e);
		}
		return ret;
	}

	/**
	 * Produce una coleccin de elementos con informacin bsica para ser mostrada y el identificador
	 *
	 * @param collection
	 * @param returnType
	 * @return
	 * @throws NCException
	 */
	@SuppressWarnings("unchecked")
	protected List<T> ObtenerLista(final Collection<Map<String, Object>> collection, final T returnType) throws Exception {
		final List<T> ret = new ArrayList<T>();
		if (collection == null) {
			return ret;
		}
		for (final Map<String, Object> data : collection) {
			try {
				final T o = (T) returnType.getClass().newInstance();
				for (final String fieldName : data.keySet()) {
					setearValor(o, fieldName, data.get(fieldName));
				}
				ret.add(o);
			} catch (final Exception e) {
				e.printStackTrace();
				throw new Exception(e);
			}
		}
		return ret;
	}

	protected List<Map<String, Object>> ejecutarSentenciaSQL(final Connection con, final int limiteFilas, final String query, final Object... params) throws Exception {
		final List<Map<String, Object>> res = new LinkedList<Map<String, Object>>();
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			pintarSql(query, params);
			pst = con.prepareStatement(query);
			pst.setMaxRows(limiteFilas);
			setParams(pst, params);
			rs = pst.executeQuery();

			final ResultSetMetaData rsMD = rs.getMetaData();
			while (rs.next()) {
				final Map<String, Object> mapa = new LinkedHashMap<String, Object>();
				for (int i = 1; i <= rsMD.getColumnCount(); i++) {
					mapa.put(rsMD.getColumnName(i), rs.getObject(i));
				}
				res.add(mapa);
			}
			return res;
		} catch (final Exception sqlE) {
			throw sqlE;
		} finally {
			try {
				if (rs != null && !rs.isClosed()) {
					rs.close();
				}
				if (pst != null && !pst.isClosed()) {
					pst.close();
				}
			} catch (final Exception e) {
				throw e;
			}
		}
	}

	protected void pintarSql(final String query, final Object[] params) {
		if (MOSTRAR_SQL) {
			if (params.length > 0) {
				for (final Object param : params) {
					System.out.print("[" + param.toString() + "] ");
				}
			}
			System.out.println();
		}
	}

	protected void setearValor(final Object o, String param, final Object value) {
		setearValor(o, param, value, param);
	}

	protected void setearValor(final Object o, String param, final Object value, String originalParam) {
		if (value != null) {
			Method m = null;
			try {
				if (param.contains(BREAKER)) {
					final String[] params = param.split(BREAKER);
					final String string = params[0];
					m = o.getClass().getMethod(PREFIJO_GET + string.substring(0, 1).toUpperCase() + string.substring(1));
					final Object n = m.invoke(o);
					param = param.replace(string + BREAKER, "");

					setearValor(n, param, value, originalParam);
				} else {
					final Class<?> tipo = o.getClass().getDeclaredField(param).getType();
					String key = o.getClass().getSimpleName() + BREAKER + originalParam;
					if (!cache.containsKey(key)) {
						final String fieldName = param.substring(0, 1).toUpperCase() + param.substring(1);
						m = o.getClass().getMethod(PREFIJO_SET + fieldName, tipo);
						cache.put(key, m);
					} else {
						m = cache.get(key);
					}
					if (tipo == Boolean.class || tipo == boolean.class) {
						if (value.toString().equals("0")) {
							m.invoke(o, new Boolean(false));
						} else if (value.toString().equals("1")) {
							m.invoke(o, new Boolean(true));
						}
					} else if(tipo == TIMESTAMP.class){
						String valueStringDate = String.valueOf(value);
						SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
						Date valueDate = formatDate.parse(valueStringDate);
						long valorDateLong = valueDate.getTime();
						Timestamp javaSqlTimestamp = new Timestamp(valorDateLong);

						m.invoke(o,new TIMESTAMP(javaSqlTimestamp));
					}
					else {
						m.invoke(o, value);
					}
				}

			} catch (final Exception e) {
				final String msg = "No pude asignar el valor: " + value + " al campo " + param + "\tTipo esperado: " + (m != null ? m.getParameterTypes()[0] : "NA") + "\t Tipo enviado: " + value.getClass();
				LOGGER.error(msg);
				throw new RuntimeErrorException(new Error(msg));
			}
		}
	}

	protected Object getValor(final Object o, String param) {
		try {
			param = param.replaceAll("\"", "");
			Method m = null;
			if (param.contains(BREAKER)) {
				final String[] params = param.split(BREAKER);
				final String string = params[0];
				m = o.getClass().getMethod(PREFIJO_GET + string.substring(0, 1).toUpperCase() + string.substring(1));
				final Object n = m.invoke(o);
				param = param.replace(string + BREAKER, "");
				return getValor(n, param);
			}
			String key = o.getClass().getSimpleName() + BREAKER + param;
			if (!cache.containsKey(key)) {
				final String fieldName = param.substring(0, 1).toUpperCase() + param.substring(1);
				m = o.getClass().getMethod(PREFIJO_GET + fieldName);
				cache.put(key, m);
			} else {
				m = cache.get(key);
			}
			return m.invoke(o);
		} catch (final Exception e) {
			throw new RuntimeErrorException(new Error("metodo no encontrado: " + param));
		}
	}

	protected void setParams(final PreparedStatement pst, final Object... params) throws Exception {
		setParams(pst, 1, params);
	}

	protected void setParams(final PreparedStatement pst, final int inicio, final Object... params) throws Exception {
		int numParam = inicio;
		for (final Object obj : params) {
			if (obj instanceof String) {
				pst.setString(numParam++, obj.toString().toUpperCase());
			} else {
				pst.setObject(numParam++, obj);
			}
		}
	}

}