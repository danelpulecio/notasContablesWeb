package co.com.bbva.app.notas.contables.dao;

import co.com.bbva.app.notas.contables.dto.Instancia;
import co.com.bbva.app.notas.contables.util.IRol;
import co.com.bbva.app.notas.contables.util.StringUtils;

import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Collection;
import java.util.LinkedList;

public class InstanciaDAO extends CommonSeqDAO<Instancia> {

	private static String cs_COLUMNAS = "CODIGO, FECHA_HORA_INICIO, CODIGO_NOTA_CONTABLE, CODIGO_SUCURSAL_ORIGEN, CODIGO_USUARIO_ACTUAL, ULTIMA_ACTUALIZACION, ESTADO";

	private static String cs_TABLA = "NC_INSTANCIA";

	private static String cs_PK = "CODIGO";

	private static String SQL_SELECT_ALL_SENTENCE = "SELECT " + cs_COLUMNAS + " FROM " + cs_TABLA + " ORDER BY CODIGO";

	private static String SQL_SELECT_BY_ESTADO_SENTENCE = "SELECT " + cs_COLUMNAS + " FROM " + cs_TABLA + " WHERE (ESTADO = ?) ORDER BY CODIGO";

	private static String SQL_SELECT_BY_SUCURSAL_ORIGEN_SENTENCE = "SELECT " + cs_COLUMNAS + " FROM " + cs_TABLA + " WHERE (CODIGO_SUCURSAL_ORIGEN = ?) ORDER BY CODIGO";

	private static String SQL_SELECT_BY_ESTADO_AND_SUCURSAL_ORIGEN_SENTENCE = "SELECT " + cs_COLUMNAS + " FROM " + cs_TABLA + " WHERE (ESTADO = ?) AND (CODIGO_SUCURSAL_ORIGEN = ?) ORDER BY CODIGO";

	private static String SQL_SELECT_BY_USUARIO_SENTENCE = "SELECT NC.NUMERO_RADICACION  \"nC__numeroRadicacion\", SU.CODIGO \"sucursal__codigo\", SU.NOMBRE \"sucursal__nombre\", CO.NOMBRE \"concepto__nombre\", US.CODIGO_EMPLEADO \"usuMod__codigoEmpleado\", US.CODIGO_AREA \"usuMod__codigoAreaModificado\", US.NOMBRE_AREA \"usuMod__nombreAreaModificado\", RO.NOMBRE \"rol__nombre\", INS.CODIGO \"codigo\", INS.FECHA_HORA_INICIO \"fechaHoraInicio\", INS.CODIGO_NOTA_CONTABLE \"codigoNotaContable\", INS.CODIGO_SUCURSAL_ORIGEN \"codigoSucursalOrigen\",  INS.CODIGO_USUARIO_ACTUAL \"codigoUsuarioActual\", INS.ULTIMA_ACTUALIZACION \"ultimaActualizacion\", INS.ESTADO \"estado\", NC.TIPO_NOTA \"nC__tipoNota\" "
			+ //
			"FROM NC_INSTANCIA INS " + //
			"LEFT JOIN NC_NOTA_CONTABLE NC ON NC.CODIGO = INS.CODIGO_NOTA_CONTABLE " + //
			"LEFT JOIN NC_CONCEPTO CO ON CO.CODIGO = NC.CODIGO_CONCEPTO " + //
			"LEFT JOIN NC_SUCURSAL SU ON SU.CODIGO = INS.CODIGO_SUCURSAL_ORIGEN " + //
			"LEFT JOIN NC_USUARIO US ON US.CODIGO = INS.CODIGO_USUARIO_ACTUAL " + //
			"LEFT JOIN NC_ROL RO ON RO.CODIGO = US.CODIGO_ROL WHERE INS.CODIGO_USUARIO_ACTUAL = ? AND INS.ESTADO NOT IN (6, 9) ORDER BY INS.CODIGO";

	private static String SQL_SELECT_BY_ESTADO_AND_USUARIO_SENTENCE = "SELECT " + cs_COLUMNAS + " FROM " + cs_TABLA + " WHERE (ESTADO = ?) AND (CODIGO_USUARIO_ACTUAL = ?) ORDER BY CODIGO";

	private static String SQL_SELECT_BY_ESTADO_AND_SUCURSAL_ORIGEN_AND_USUARIO_SENTENCE = "SELECT " + cs_COLUMNAS + " FROM " + cs_TABLA + " WHERE (ESTADO = ?) AND (CODIGO_SUCURSAL_ORIGEN = ?) AND (CODIGO_USUARIO_ACTUAL = ?) ORDER BY CODIGO";

	private static String SQL_SELECT_BY_CODIGO_NOTA_CONTABLE_SENTENCE = "SELECT " + cs_COLUMNAS + " FROM " + cs_TABLA + " WHERE (CODIGO_NOTA_CONTABLE = ?)";

	private static final String SQL_SELECT = "SELECT DISTINCT NC.CODIGO \"nC__codigo\", NC.NUMERO_RADICACION \"nC__numeroRadicacion\", NC.FECHA_HORA_REGISTRO_MODULO \"nC__fechaRegistroModulo\", NC.FECHA_HORA_REGISTRO_ALTAMIRA \"nC__fechaRegistroAltamira\", SU.CODIGO \"sucursal__codigo\", SU.NOMBRE \"sucursal__nombre\", CO.NOMBRE \"concepto__nombre\", US.CODIGO_EMPLEADO \"usuMod__codigoEmpleado\", US.CODIGO_AREA \"usuMod__codigoAreaModificado\", US.NOMBRE_AREA \"usuMod__nombreAreaModificado\", RO.NOMBRE \"rol__nombre\", INS.CODIGO \"codigo\", INS.FECHA_HORA_INICIO \"fechaHoraInicio\", INS.CODIGO_NOTA_CONTABLE \"codigoNotaContable\", INS.CODIGO_SUCURSAL_ORIGEN \"codigoSucursalOrigen\",  INS.CODIGO_USUARIO_ACTUAL \"codigoUsuarioActual\", INS.ULTIMA_ACTUALIZACION \"ultimaActualizacion\", INS.ESTADO \"estado\" , NC.TIPO_NOTA \"nC__tipoNota\" "
			+ "FROM NC_INSTANCIA INS " + //
			"LEFT JOIN NC_NOTA_CONTABLE NC ON NC.CODIGO = INS.CODIGO_NOTA_CONTABLE " + //
			"LEFT JOIN NC_CONCEPTO CO ON CO.CODIGO = NC.CODIGO_CONCEPTO " + //
			"LEFT JOIN NC_SUCURSAL SU ON SU.CODIGO = INS.CODIGO_SUCURSAL_ORIGEN " + //
			"LEFT JOIN NC_USUARIO US ON US.CODIGO = INS.CODIGO_USUARIO_ACTUAL " + //
			"LEFT JOIN NC_ROL RO ON RO.CODIGO = US.CODIGO_ROL ";
	// private static String SQL_SELECT_BY_FECHA_ALTAMIRA_NOTA_CONTABLE_SENTENCE = "SELECT " + cs_COLUMNAS + " FROM " + cs_TABLA + " WHERE (FECHA_HORA_INICIO BETWEEN ? AND ?)";
	private static String SQL_SELECT_BY_FECHA_ALTAMIRA_NOTA_CONTABLE_SENTENCE = SQL_SELECT + " WHERE (FECHA_HORA_INICIO BETWEEN ? AND ?)";//

	private static String SQL_SELECT_BY_NUMERO_RADICACION_NOTA_CONTABLE_SENTENCE = SQL_SELECT + " WHERE (NC.NUMERO_RADICACION LIKE ?)";//

	private static String SQL_SELECT_BY_ASIENTO_CONTABLE_SENTENCE = SQL_SELECT + " LEFT JOIN nc_nota_contable_tema NCT ON nct.codigo_nota_contable=nc.codigo " + //
			" LEFT JOIN nc_nota_cont_registro_libre NCR ON ncr.codigo_nota_contable=nc.codigo " + //
			" LEFT JOIN nc_not_con_cruce_part_pend NCC ON ncc.codigo_nota_contable=nc.codigo " + //
			" WHERE (NCT.numero_asiento like ? AND TO_CHAR(NCT.fecha_contable ,'YYYY-MM-DD') = ?) OR (NCR.numero_asiento like ? AND TO_CHAR(ncr.fecha_contable ,'YYYY-MM-DD') = ?) OR (NCC.NUMERO_ASIENTO like ? AND  TO_CHAR(NCC.fecha_contable ,'YYYY-MM-DD') = ?)";//

	private static String SQL_SELECT_BY_CODIGO_SUCURSAL_AND_FECHAS_SENTENCE = "SELECT A.CODIGO, A.FECHA_HORA_INICIO, A.CODIGO_NOTA_CONTABLE, A.CODIGO_SUCURSAL_ORIGEN, A.CODIGO_USUARIO_ACTUAL, A.ULTIMA_ACTUALIZACION, A.ESTADO FROM NC_INSTANCIA A, NC_NOTA_CONTABLE B, NC_ACTIVIDAD_REALIZADA C WHERE ((A.CODIGO_NOTA_CONTABLE = B.CODIGO) AND (A.CODIGO = C.CODIGO_INSTANCIA)) AND (B.CODIGO_SUCURSAL_ORIGEN = ?) AND (C.CODIGO = ?) AND (C.FECHA_HORA BETWEEN ? AND ?) ORDER BY A.CODIGO";

	private static String SQL_UPDATE_SENTENCE = "UPDATE " + cs_TABLA + " SET CODIGO_SUCURSAL_ORIGEN = ?, CODIGO_USUARIO_ACTUAL = ?, ULTIMA_ACTUALIZACION = ?, ESTADO = ? WHERE (CODIGO = ?)";

	private static String sql_SELECT_PENDIENTES = "SELECT " + cs_COLUMNAS + " FROM " + cs_TABLA + " WHERE (ESTADO < 4) ORDER BY CODIGO_SUCURSAL_ORIGEN";

	public InstanciaDAO() {
		super(cs_TABLA, cs_COLUMNAS, cs_PK, new Instancia());
	}

	@Override
	public void internalUpdate(Connection con, Instancia row) throws Exception {
		executeUpdate(con, SQL_UPDATE_SENTENCE, new Object[] { row.getCodigoSucursalOrigen(), row.getCodigoUsuarioActual().intValue(), row.getUltimaActualizacion(), row.getEstado(), row.getCodigo().intValue() });
	}

	public Instancia findByNotaContable(Instancia row) throws Exception {
		return getByGeneral(SQL_SELECT_BY_CODIGO_NOTA_CONTABLE_SENTENCE, row.getCodigoNotaContable().intValue());
	}

	public Collection<Instancia> findByFecha(Timestamp fechaRegistroAltamira, String codSucUsuario, int codRol) throws Exception {
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(fechaRegistroAltamira.getTime());
		c.add(Calendar.DATE, 1);
		String query = SQL_SELECT_BY_FECHA_ALTAMIRA_NOTA_CONTABLE_SENTENCE;
		if (codRol == IRol.AUXILIAR_O_ANALISTA || codRol == IRol.SUBGERENTE_Y_RESPONSABLE_DE_AREA_CENTRAL || codRol == IRol.GERENTE) {
			query += " AND NC.CODIGO_SUCURSAL_ORIGEN = '" + codSucUsuario + "'";
		}
		return obtenerLista(query, new Object[] { fechaRegistroAltamira, new Timestamp(c.getTimeInMillis()) });
	}

	public Collection<Instancia> findAll() throws Exception {
		return findByGeneral(SQL_SELECT_ALL_SENTENCE);
	}

	public Collection<Instancia> findByEstado(Instancia row) throws Exception {
		return findByGeneral(SQL_SELECT_BY_ESTADO_SENTENCE, row.getEstado());
	}

	public Collection<Instancia> findBySucursalOrigen(Instancia row) throws Exception {
		return findByGeneral(SQL_SELECT_BY_SUCURSAL_ORIGEN_SENTENCE, row.getCodigoSucursalOrigen());
	}

	public Collection<Instancia> findByEstadoAndSucursalOrigen(Instancia row) throws Exception {
		return findByGeneral(SQL_SELECT_BY_ESTADO_AND_SUCURSAL_ORIGEN_SENTENCE, new Object[] { row.getEstado(), row.getCodigoSucursalOrigen() });
	}

	public Collection<Instancia> findByUsuario(Instancia row) throws Exception {
		return obtenerLista(SQL_SELECT_BY_USUARIO_SENTENCE, new Object[] { row.getCodigoUsuarioActual().intValue() });
	}

	public Collection<Instancia> findByEstadoAndUsuario(Instancia row) throws Exception {
		return findByGeneral(SQL_SELECT_BY_ESTADO_AND_USUARIO_SENTENCE, new Object[] { row.getEstado(), row.getCodigoUsuarioActual().intValue() });
	}

	public Collection<Instancia> findByEstadoAndSucursalOrigenAndUsuario(Instancia row) throws Exception {
		return findByGeneral(SQL_SELECT_BY_ESTADO_AND_SUCURSAL_ORIGEN_AND_USUARIO_SENTENCE, new Object[] { row.getEstado(), row.getCodigoSucursalOrigen(), row.getCodigoUsuarioActual().intValue() });
	}

	public Collection<Instancia> findByCodigoSucursalAndFechas(String codigoSucursal, Date fechaDesde, Date fechaHasta) throws Exception {
		return findByGeneral(SQL_SELECT_BY_CODIGO_SUCURSAL_AND_FECHAS_SENTENCE, new Object[] { codigoSucursal, 1, fechaDesde, fechaHasta });
	}

	@Override
	protected Object[] getDataToAdd(Connection con, Instancia row) throws Exception {
		row.setCodigo(getMaxCode(con));
		return new Object[] { row.getCodigo().intValue(), row.getFechaHoraInicio(), row.getCodigoNotaContable().intValue(), row.getCodigoSucursalOrigen(), row.getCodigoUsuarioActual().intValue(), row.getUltimaActualizacion(), row.getEstado() };
	}

	@Override
	public Instancia getResultSetToVO(ResultSet result) throws Exception {
		Instancia row = new Instancia();

		row.setCodigo(result.getInt(1));
		row.setFechaHoraInicioTs(result.getTimestamp(2));
		row.setCodigoNotaContable(result.getInt(3));
		row.setCodigoSucursalOrigen(result.getString(4));
		row.setCodigoUsuarioActual(result.getInt(5));
		row.setUltimaActualizacionTs(result.getTimestamp(6));
		row.setEstado(result.getString(7));

		return row;
	}

	public Collection<Instancia> findByNumeroRadicacion(String numeroRadicacion, String codSucUsuario, int codRol) throws Exception {
		String query = SQL_SELECT_BY_NUMERO_RADICACION_NOTA_CONTABLE_SENTENCE;
		if (codRol == IRol.AUXILIAR_O_ANALISTA || codRol == IRol.SUBGERENTE_Y_RESPONSABLE_DE_AREA_CENTRAL || codRol == IRol.GERENTE) {
			query += " AND NC.CODIGO_SUCURSAL_ORIGEN = '" + codSucUsuario + "'";
		}
		return obtenerLista(query, new Object[] { COMODIN + numeroRadicacion + COMODIN });
	}

	public Collection<Instancia> findByAsientoContableAndFecha(String asientoContable, Timestamp fechaRegistroAltamira, String codSucUsuario, int codRol) throws Exception {
		String fecha = StringUtils.getString(fechaRegistroAltamira, "yyyy-MM-dd");
		String query = SQL_SELECT_BY_ASIENTO_CONTABLE_SENTENCE;
		if (codRol == IRol.AUXILIAR_O_ANALISTA || codRol == IRol.SUBGERENTE_Y_RESPONSABLE_DE_AREA_CENTRAL || codRol == IRol.GERENTE) {
			query += " AND NC.CODIGO_SUCURSAL_ORIGEN = '" + codSucUsuario + "'";
		}
		return obtenerLista(query, new Object[] { COMODIN + asientoContable, fecha, COMODIN + asientoContable, fecha, COMODIN + asientoContable, fecha });
	}

	public Collection<Instancia> findNormalBy(String codigoSucursalOrigen, String codigoSucursalDestino, Integer codigoConcepto, Integer codigoTema, Integer codigoTipoEvento, String partidaContable, String numeroIdentificacion, Date fechaDesde,
			Date fechaHasta, String codigoDivisa, String codigoEstado, String descripcion, String codSucUsuario, int codRol) throws Exception {
		String sqlSelect = SQL_SELECT + " LEFT JOIN NC_NOTA_CONTABLE_TEMA B ON NC.CODIGO=B.CODIGO_NOTA_CONTABLE ";
		String sqlWhere = " WHERE NC.TIPO_NOTA = 'R' ";
		String sqlOrderBy = "ORDER BY SU.CODIGO";

		LinkedList<Object> params = new LinkedList<Object>();
		if (!codigoSucursalOrigen.equals("")) {
			sqlWhere += " AND NC.CODIGO_SUCURSAL_ORIGEN = ?";
			params.add(codigoSucursalOrigen);
		}
		
//		if(codigoSucursalDestino != null) {
			if (!codigoSucursalDestino.equals("")) {
				sqlWhere += " AND (B.COD_SUC_DEST_PART = ? OR B.COD_SUC_DEST_CONTPART = ?)";
				params.add(codigoSucursalDestino);
				params.add(codigoSucursalDestino);
			}
//		}
		if (codigoConcepto != null && codigoConcepto != 0) {
			sqlWhere += " AND NC.CODIGO_CONCEPTO = ?";
			params.add(codigoConcepto);
		}
		if (codigoTema != null && codigoTema != 0) {
			sqlWhere += " AND (B.CODIGO_TEMA = ?)";
			params.add(codigoTema);
		}
		if (codigoTipoEvento != null && codigoTipoEvento != 0) {
			sqlWhere += " AND NC.CODIGO_TIPO_EVENTO = ?";
			params.add(codigoTipoEvento);
		}
		if (!partidaContable.equals("")) {
			sqlWhere += " AND (B.PARTIDA_CONTABLE LIKE ? OR B.CONTRAPARTIDA_CONTABLE LIKE ?)";
			params.add(COMODIN + partidaContable + COMODIN);
			params.add(COMODIN + partidaContable + COMODIN);
		}
		if (!numeroIdentificacion.equals("")) {
			sqlWhere += " AND ((B.NUMERO_IDENTIFICACION1 LIKE ?) OR (B.NUMERO_IDENTIFICACION2 LIKE ?))";
			params.add(COMODIN + numeroIdentificacion + COMODIN);
			params.add(COMODIN + numeroIdentificacion + COMODIN);
		}
		if (!codigoDivisa.equals("")) {
			sqlWhere += " AND (B.CODIGO_DIVISA = ?)";
			params.add(codigoDivisa);
		}
		if (!descripcion.equals("")) {
			sqlWhere += " AND (B.DESCRIPCION LIKE ?)";
			params.add(COMODIN + descripcion + COMODIN);
		}
		if (!codigoEstado.equals("")) {
			sqlWhere += " AND (INS.ESTADO = ?)";
			params.add(codigoEstado);
		}
		if (codRol == IRol.AUXILIAR_O_ANALISTA || codRol == IRol.SUBGERENTE_Y_RESPONSABLE_DE_AREA_CENTRAL || codRol == IRol.GERENTE) {
			sqlWhere += " AND (NC.CODIGO_SUCURSAL_ORIGEN= ?)";
			params.add(codSucUsuario);
		}
		if (fechaDesde != null) {
			if (fechaHasta != null) {
				sqlWhere += " AND NC.FECHA_HORA_REGISTRO_MODULO BETWEEN ? AND ?";
				params.add(fechaDesde);
				params.add(fechaHasta);
			} else {
				sqlWhere += " AND NC.FECHA_HORA_REGISTRO_MODULO >= ?";
				params.add(fechaDesde);
			}
		} else if (fechaHasta != null) {
			sqlWhere += " AND NC.FECHA_HORA_REGISTRO_MODULO <= ?";
			params.add(fechaHasta);
		}
		Object[] paramsObj = new Object[params.size()];
		return super.obtenerLista(sqlSelect + " " + sqlWhere + " " + sqlOrderBy, params.toArray(paramsObj));
	}

	public Collection<Instancia> findRegLibreBy(String sucOrigen, String sucDestino, String partida, String numIdentificacion, Date desde, Date hasta, String divisa, String estado, String descripcion, String codSucUsuario, int codRol)
			throws Exception {
		String sqlSelect = SQL_SELECT + " LEFT JOIN NC_NOTA_CONT_REGISTRO_LIBRE B ON NC.CODIGO=B.CODIGO_NOTA_CONTABLE ";
		String sqlWhere = " WHERE NC.TIPO_NOTA = 'L' ";
		String sqlOrderBy = "ORDER BY SU.CODIGO";

		LinkedList<Object> params = new LinkedList<Object>();
		if (!sucOrigen.equals("")) {
			sqlWhere += " AND NC.CODIGO_SUCURSAL_ORIGEN = ?";
			params.add(sucOrigen);
		}
		if (!sucDestino.equals("")) {
			sqlWhere += " AND B.COD_SUC_DESTINO = ?";
			params.add(sucDestino);
		}
		if (!partida.equals("")) {
			sqlWhere += " AND B.CUENTA_CONTABLE LIKE ? ";
			params.add(COMODIN + partida + COMODIN);
		}
		if (!numIdentificacion.equals("")) {
			sqlWhere += " AND B.NUMERO_IDENTIFICACION1 LIKE ?";
			params.add(COMODIN + numIdentificacion + COMODIN);
		}
		if (!divisa.equals("")) {
			sqlWhere += " AND (B.CODIGO_DIVISA = ?)";
			params.add(divisa);
		}
		if (!descripcion.equals("")) {
			sqlWhere += " AND (B.DESCRIPCION LIKE ?)";
			params.add(COMODIN + descripcion + COMODIN);
		}
		if (!estado.equals("")) {
			sqlWhere += " AND (INS.ESTADO = ?)";
			params.add(estado);
		}
		if (codRol == IRol.AUXILIAR_O_ANALISTA || codRol == IRol.SUBGERENTE_Y_RESPONSABLE_DE_AREA_CENTRAL || codRol == IRol.GERENTE) {
			sqlWhere += " AND (NC.CODIGO_SUCURSAL_ORIGEN= ?)";
			params.add(codSucUsuario);
		}
		if (desde != null) {
			if (hasta != null) {
				sqlWhere += " AND NC.FECHA_HORA_REGISTRO_MODULO BETWEEN ? AND ?";
				params.add(desde);
				params.add(hasta);
			} else {
				sqlWhere += " AND NC.FECHA_HORA_REGISTRO_MODULO >= ?";
				params.add(desde);
			}
		} else if (hasta != null) {
			sqlWhere += " AND NC.FECHA_HORA_REGISTRO_MODULO <= ?";
			params.add(hasta);
		}
		Object[] paramsObj = new Object[params.size()];
		return super.obtenerLista(sqlSelect + " " + sqlWhere + " " + sqlOrderBy, params.toArray(paramsObj));
	}

	public Collection<Instancia> findCruceRefBy(String sucOrigen, String sucDestino, String partida, Date desde, Date hasta, String divisa, String estado, String codSucUsuario, int codRol) throws Exception {
		String sqlSelect = SQL_SELECT + " LEFT JOIN NC_NOT_CON_CRUCE_PART_PEND B ON NC.CODIGO=B.CODIGO_NOTA_CONTABLE ";
		String sqlWhere = " WHERE NC.TIPO_NOTA = 'C' ";
		String sqlOrderBy = "ORDER BY SU.CODIGO";

		LinkedList<Object> params = new LinkedList<Object>();
		if (!sucOrigen.equals("")) {
			sqlWhere += " AND NC.CODIGO_SUCURSAL_ORIGEN = ?";
			params.add(sucOrigen);
		}
		if (!sucDestino.equals("")) {
			sqlWhere += " AND B.SUCURSAL_DESTINO = ?";
			params.add(sucDestino);
		}
		if (!partida.equals("")) {
			sqlWhere += " AND B.CUENTA LIKE ? ";
			params.add(COMODIN + partida + COMODIN);
		}
		if (!divisa.equals("")) {
			sqlWhere += " AND (B.DIVISA = ?)";
			params.add(divisa);
		}
		if (!estado.equals("")) {
			sqlWhere += " AND (INS.ESTADO = ?)";
			params.add(estado);
		}
		if (codRol == IRol.AUXILIAR_O_ANALISTA || codRol == IRol.SUBGERENTE_Y_RESPONSABLE_DE_AREA_CENTRAL || codRol == IRol.GERENTE) {
			sqlWhere += " AND (NC.CODIGO_SUCURSAL_ORIGEN= ?)";
			params.add(codSucUsuario);
		}
		if (desde != null) {
			if (hasta != null) {
				sqlWhere += " AND NC.FECHA_HORA_REGISTRO_MODULO BETWEEN ? AND ?";
				params.add(desde);
				params.add(hasta);
			} else {
				sqlWhere += " AND NC.FECHA_HORA_REGISTRO_MODULO >= ?";
				params.add(desde);
			}
		} else if (hasta != null) {
			sqlWhere += " AND NC.FECHA_HORA_REGISTRO_MODULO <= ?";
			params.add(hasta);
		}
		Object[] paramsObj = new Object[params.size()];
		return super.obtenerLista(sqlSelect + " " + sqlWhere + " " + sqlOrderBy, params.toArray(paramsObj));
	}

	public Collection<Instancia> findPendientes() throws Exception {
		return findByGeneral(sql_SELECT_PENDIENTES);
	}
}