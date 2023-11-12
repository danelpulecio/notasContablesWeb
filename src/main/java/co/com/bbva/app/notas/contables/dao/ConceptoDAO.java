package co.com.bbva.app.notas.contables.dao;

import co.com.bbva.app.notas.contables.dto.Concepto;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collection;

public class ConceptoDAO extends CommonSeqDAO<Concepto> {

	private static String cs_COLUMNAS = "CODIGO, NOMBRE, CODIGO_UNIDAD_ANALISIS, CODIGO_TEMA_AUTORIZACION, CEN_AUT_SUC, CEN_AUT_ARE, CEN_AUT_CEN, VISTO_BUENO_ANALISIS, AUTORIZACION_TERCERO, SOPORTE, ORIGEN_IGUAL_DESTINO, ESTADO";

	private static String cs_TABLA = "NC_CONCEPTO";
	private static String cs_PK = "CODIGO";

	private static String SQL_SELECT_ALL_SENTENCE = "SELECT c.CODIGO \"codigo\", " + //
			"c.NOMBRE \"nombre\", " + //
			"c.CODIGO_UNIDAD_ANALISIS \"codigoUnidadAnalisis\", " + //
			"c.CODIGO_TEMA_AUTORIZACION \"codigoTemaAutorizacion\", " + //
			"c.CEN_AUT_SUC \"centrosAutSucursales\", " + //
			"c.CEN_AUT_ARE \"centrosAutAreasCentrales\", " + //
			"c.CEN_AUT_CEN \"centrosAutCentroEspecial\", " + //
			"c.VISTO_BUENO_ANALISIS \"vistoBuenoAnalisis\", " + //
			"c.AUTORIZACION_TERCERO \"autorizacionTercero\", " + //
			"c.SOPORTE \"soportes\", " + //
			"c.ORIGEN_IGUAL_DESTINO \"origenDestino\", " + //
			"c.ESTADO \"estado\", " + //
			"s.codigo \"codSucursal\", " + //
			"s.nombre \"nombreSucursal\" " + //
			"FROM NC_CONCEPTO c left join nc_unidad_analisis ua on c.codigo_unidad_analisis=ua.codigo " + //
			"left join nc_sucursal s on ua.codigo_sucursal=s.codigo";//

	private static String SQL_SELECT_BY_ESTADO_SENTENCE = SQL_SELECT_ALL_SENTENCE + " WHERE (c.ESTADO = ?) ORDER BY CODIGO";

	private static String SQL_SELECT_BY_ESTADO_PARA_CONCEPTO_SENTENCE = "SELECT " + cs_COLUMNAS + " FROM " + cs_TABLA + " WHERE (ESTADO = ?) ORDER BY NOMBRE";

	private static String SQL_SELECT_BY_SENTENCE = SQL_SELECT_ALL_SENTENCE + " WHERE ((c.NOMBRE LIKE ?) OR (s.CODIGO LIKE ?) OR (s.NOMBRE LIKE ?))";

	private static String SQL_SELECT_BY_NOMBRE_SENTENCE = "SELECT DISTINCT A.CODIGO, A.NOMBRE, A.CODIGO_UNIDAD_ANALISIS, A.CODIGO_TEMA_AUTORIZACION, A.CEN_AUT_SUC, A.CEN_AUT_ARE, A.CEN_AUT_CEN, A.VISTO_BUENO_ANALISIS, A.AUTORIZACION_TERCERO, A.SOPORTE, A.ORIGEN_IGUAL_DESTINO, A.ESTADO FROM NC_CONCEPTO A WHERE (A.NOMBRE LIKE ?) AND (A.ESTADO = ?) ORDER BY A.CODIGO";

	private static String SQL_UPDATE_SENTENCE = "UPDATE "
			+ cs_TABLA
			+ " SET NOMBRE = ?, CODIGO_UNIDAD_ANALISIS = ?, CODIGO_TEMA_AUTORIZACION = ?, CEN_AUT_SUC = ?, CEN_AUT_ARE = ?, CEN_AUT_CEN = ?, VISTO_BUENO_ANALISIS = ?, AUTORIZACION_TERCERO = ?, SOPORTE = ?, ORIGEN_IGUAL_DESTINO = ?, ESTADO = ? WHERE (CODIGO = ?)";

	public ConceptoDAO() {
		super(cs_TABLA, cs_COLUMNAS, cs_PK, new Concepto());
	}

	@Override
	public void internalUpdate(Connection con, Concepto row) throws Exception {
		ArrayList<Object> params = new ArrayList<Object>();
		params.add(row.getNombre());
		params.add(row.getCodigoUnidadAnalisis());
		params.add(row.getCodigoTemaAutorizacion());
		params.add(row.getCentrosAutSucursales());
		params.add(row.getCentrosAutAreasCentrales());
		params.add(row.getCentrosAutCentroEspecial());
		params.add(row.getVistoBuenoAnalisis());
		params.add(row.getAutorizacionTercero());
		params.add(row.getSoportes());
		params.add(row.getOrigenDestino());
		params.add(row.getEstado());
		params.add(row.getCodigo());

		executeUpdate(con, SQL_UPDATE_SENTENCE, params.toArray());
	}

	public Collection<Concepto> findAll() throws Exception {
		return obtenerLista(SQL_SELECT_ALL_SENTENCE);
	}

	public Collection<Concepto> findByEstado(Concepto row) throws Exception {
		return obtenerLista(SQL_SELECT_BY_ESTADO_SENTENCE, new Object[] { row.getEstado() });
	}

	public Collection<Concepto> findByEstadoParaConcepto(Concepto row) throws Exception {
		return findByGeneral(SQL_SELECT_BY_ESTADO_PARA_CONCEPTO_SENTENCE, row.getEstado());
	}

	public Collection<Concepto> findBy(String palabraClave, String estado) throws Exception {
		String param = COMODIN + palabraClave + COMODIN;
		if (estado != null && !estado.equals("")) {
			String sqlSentence = SQL_SELECT_BY_SENTENCE;
			sqlSentence += " AND (c.ESTADO = ?)";
			return obtenerLista(sqlSentence, new Object[] { param, param, param, estado });
		}
		return obtenerLista(SQL_SELECT_BY_SENTENCE, new Object[] { param, param, param });

	}

	public Collection<Concepto> findByNombre(String palabraClave) throws Exception {
		return findByGeneral(SQL_SELECT_BY_NOMBRE_SENTENCE, new Object[] { "%" + palabraClave + "%", "A" });
	}

	@Override
	protected Object[] getDataToAdd(Connection con, Concepto row) throws Exception {
		row.setCodigo(getMaxCode(con));
		return new Object[] { row.getCodigo(), row.getNombre(), row.getCodigoUnidadAnalisis(), row.getCodigoTemaAutorizacion(), row.getCentrosAutSucursales(), row.getCentrosAutAreasCentrales(), row.getCentrosAutCentroEspecial(),
				row.getVistoBuenoAnalisis(), row.getAutorizacionTercero(), row.getSoportes(), row.getOrigenDestino(), row.getEstado() };
	}

	@Override
	public Concepto getResultSetToVO(ResultSet result) throws Exception {
		Concepto row = new Concepto();

		row.setCodigo(result.getInt(1));
		row.setNombre(result.getString(2));
		row.setCodigoUnidadAnalisis(result.getInt(3));
		row.setCodigoTemaAutorizacion(result.getInt(4));
		row.setCentrosAutSucursales(result.getString(5) != null ? result.getString(5) : "");
		row.setCentrosAutAreasCentrales(result.getString(6) != null ? result.getString(6) : "");
		row.setCentrosAutCentroEspecial(result.getString(7) != null ? result.getString(7) : "");
		row.setVistoBuenoAnalisis(result.getString(8));
		row.setAutorizacionTercero(result.getString(9));
		row.setSoportes(result.getString(10));
		row.setOrigenDestino(result.getString(11));
		row.setEstado(result.getString(12));

		return row;
	}
}