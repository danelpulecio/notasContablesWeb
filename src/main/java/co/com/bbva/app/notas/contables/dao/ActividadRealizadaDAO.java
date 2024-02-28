package co.com.bbva.app.notas.contables.dao;

import co.com.bbva.app.notas.contables.dto.ActividadRealizada;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.Collection;

public class ActividadRealizadaDAO extends CommonSeqDAO<ActividadRealizada> {

	private static final String cs_PK = "CODIGO";

	private static final String cs_COLUMNAS = "CODIGO, FECHA_HORA, CODIGO_INSTANCIA, ESTADO, CODIGO_USUARIO, VALOR1, VALOR2, VALOR3, VALOR4, VALOR5, FECHA_HORA_CIERRE, DURACION_ACTIVIDAD";

	private static final String cs_TABLA = "NC_ACTIVIDAD_REALIZADA";

	private static final String SQL_SELECT_BY_CODIGO_INSTANCIA_SENTENCE = "SELECT ACT.CODIGO \"codigo\", ACT.FECHA_HORA \"fechaHora\", ACT.FECHA_HORA_CIERRE \"fechaHoraCierre\", ACT.CODIGO_INSTANCIA \"codigoInstancia\", ACT.ESTADO \"estado\", ACT.CODIGO_USUARIO \"codigoUsuario\", ACT.VALOR1 \"valor1\", ACT.VALOR2 \"valor2\", ACT.VALOR3 \"valor3\", ACT.VALOR4 \"valor4\", ACT.VALOR5 \"valor5\", ACT.DURACION_ACTIVIDAD \"duracionActividad\", "
			+ "USU.CODIGO_EMPLEADO \"usuMod__codigoEmpleado\", USU.CODIGO_AREA \"usuMod__codigoAreaModificado\", USU.NOMBRE_AREA \"usuMod__nombreAreaModificado\", RO.NOMBRE \"rol__nombre\", DEV.NOMBRE \"cauDev__nombre\" "
			+ "FROM NC_ACTIVIDAD_REALIZADA ACT "
			+ "LEFT JOIN NC_USUARIO USU ON USU.CODIGO = ACT.CODIGO_USUARIO "
			+ "LEFT JOIN NC_ROL RO ON RO.CODIGO = USU.CODIGO_ROL "
			+ "LEFT JOIN NC_CAUSAL_DEVOLUCION DEV ON DEV.CODIGO = ACT.VALOR1 "
			+ "WHERE (ACT.CODIGO_INSTANCIA = ?) ORDER BY ACT.CODIGO";

	private static final String SQL_SELECT_CODIGO_USUARIO_ULTIMA_RADICACION = "SELECT  ACT.CODIGO_USUARIO FROM NC_ACTIVIDAD_REALIZADA ACT LEFT JOIN NC_INSTANCIA INS ON (INS.CODIGO = ACT.CODIGO_INSTANCIA) LEFT JOIN NC_NOTA_CONTABLE NOC ON (NOC.CODIGO = INS.CODIGO_NOTA_CONTABLE) WHERE (NOC.NUMERO_RADICACION = ?) ORDER BY ACT.CODIGO DESC";

	private static final String SQL_UPDATE_CIERRE_SENTENCE = "UPDATE NC_ACTIVIDAD_REALIZADA SET FECHA_HORA_CIERRE = ?, DURACION_ACTIVIDAD = ? WHERE CODIGO_INSTANCIA= ? AND FECHA_HORA_CIERRE  IS NULL ";

	private static final String SQL_SELECT_ULTIMA_SENTENCE = "SELECT " + cs_COLUMNAS + " FROM NC_ACTIVIDAD_REALIZADA WHERE CODIGO_INSTANCIA= ? ORDER BY FECHA_HORA_CIERRE DESC";

	private static final String SQL_SELECT_REPORTE_TIEMPOS = "SELECT NC.NUMERO_RADICACION \"numeroRadicacionNC\", AR.FECHA_HORA \"fechaHora\", AR.FECHA_HORA_CIERRE \"fechaHoraCierre\", AR.DURACION_ACTIVIDAD \"duracionActividad\" " + //
			" FROM NC_NOTA_CONTABLE NC " + //
			" INNER JOIN NC_INSTANCIA INS ON INS.CODIGO_NOTA_CONTABLE = NC.CODIGO " + //
			" INNER JOIN NC_ACTIVIDAD_REALIZADA AR ON AR.CODIGO_INSTANCIA = INS.CODIGO " + //
			" INNER JOIN NC_USUARIO US ON US.CODIGO = AR.CODIGO_USUARIO " + //
			" WHERE US.CODIGO = ?";

	public ActividadRealizadaDAO() {
		super(cs_TABLA, cs_COLUMNAS, cs_PK, new ActividadRealizada());
	}

	public Collection<ActividadRealizada> findByCodigoInstancia(ActividadRealizada row) throws Exception {
		return obtenerLista(SQL_SELECT_BY_CODIGO_INSTANCIA_SENTENCE, new Object[] { row.getCodigoInstancia().intValue() });
	}

	public Collection<ActividadRealizada> findParaReporteTiempos(Integer codUsuario) throws Exception {
		return obtenerLista(SQL_SELECT_REPORTE_TIEMPOS, new Object[] { codUsuario });
	}

	public ActividadRealizada getUltima(ActividadRealizada row) throws Exception {
		return getByGeneral(SQL_SELECT_ULTIMA_SENTENCE, new Object[] { row.getCodigoInstancia().intValue() });
	}

	@Override
	protected Object[] getDataToAdd(Connection con, ActividadRealizada row) throws Exception {
		row.setCodigo(getMaxCode(con));
		return new Object[] { row.getCodigo().intValue(), row.getFechaHoraTs(), row.getCodigoInstancia().intValue(), row.getEstado(), row.getCodigoUsuario().intValue(), row.getValor1(), row.getValor2(), row.getValor3(), row.getValor4(),
				row.getValor5(), row.getFechaHoraCierreTs(), row.getDuracionActividad() };
	}

	@Override
	public ActividadRealizada getResultSetToVO(ResultSet result) throws Exception {
		ActividadRealizada row = new ActividadRealizada();

		row.setCodigo(result.getInt(1));
		row.setFechaHoraTs(result.getTimestamp(2));
		row.setCodigoInstancia(result.getInt(3));
		row.setEstado(result.getString(4));
		row.setCodigoUsuario(result.getInt(5));
		row.setValor1(result.getString(6) != null ? result.getString(6) : "");
		row.setValor2(result.getString(7) != null ? result.getString(7) : "");
		row.setValor3(result.getString(8) != null ? result.getString(8) : "");
		row.setValor4(result.getString(9) != null ? result.getString(9) : "");
		row.setValor5(result.getString(10) != null ? result.getString(10) : "");
		row.setFechaHoraCierreTs(result.getTimestamp(11));
		row.setDuracionActividad(result.getInt(12));
		return row;
	}

	@Override
	protected void internalUpdate(Connection con, ActividadRealizada row) throws Exception {
		super.executeUpdate(con, SQL_UPDATE_CIERRE_SENTENCE, new Object[] { row.getFechaHoraCierre(), row.getDuracionActividad(), row.getCodigoInstancia() });
		row.setDuracionActividad(null);
	}

	public int getUltimoUsuarioPorRadicacion(String numRadicacion) throws Exception {
		return obtenerObjeto(SQL_SELECT_CODIGO_USUARIO_ULTIMA_RADICACION, numRadicacion).getCodigoUsuario().intValue();
	}
}