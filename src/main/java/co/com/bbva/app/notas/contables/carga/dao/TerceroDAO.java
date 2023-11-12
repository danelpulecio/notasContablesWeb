/*
	Nombre DTO: Tercero
 */

package co.com.bbva.app.notas.contables.carga.dao;

import co.com.bbva.app.notas.contables.carga.dto.Tercero;
import co.com.bbva.app.notas.contables.dao.CommonDAO;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.Collection;

public class TerceroDAO extends CommonDAO<Tercero> {

	private static String cs_COLUMNAS = "TIPO_IDENTIFICACION, NUMERO_IDENTIFICACION, DIGITO_VERIFICACION, PRIMER_APELLIDO, SEGUNDO_APELLIDO, PRIMER_NOMBRE, SEGUNDO_NOMBRE, PEATVIA, DIRECCION, DEPARTAMENTO, MUNICIPIO, ACTIVIDAD_ECONOMICA, TIPO_TELEFONO, INDICATIVO, TELEFONO, APLICATIVO, USUARIO, OFICINA_MODIFICACION, FECHA, ESTADO_CARGA, FECHA_ULTIMA_CARGA, PAIS, EXTENSION, EXTENSION2, INDICATIVO2, TELEFONO2, TIPO_TELEFONO2, MONEDA, INDICADOR_NOTAS_CONTABLES, EMAIL, REGIMEN_TRIBUTARIO, SEXO, INGRESO_APP";

	private static String cs_TABLA = "NC_TERCERO";

	private static String cs_PK = null;

	private static String SQL_SELECT_ALL_SENTENCE = "SELECT " + cs_COLUMNAS + " FROM " + cs_TABLA + " ORDER BY PRIMER_APELLIDO, SEGUNDO_APELLIDO, PRIMER_NOMBRE, SEGUNDO_NOMBRE";

	private static String SQL_SELECT_BY_ESTADO_CARGA_SENTENCE = "SELECT " + cs_COLUMNAS + " FROM " + cs_TABLA + " WHERE (ESTADO_CARGA = ?)";

	private static String SQL_SELECT_BY_PRIMARY_KEY_SENTENCE = "SELECT " + cs_COLUMNAS + " FROM " + cs_TABLA + " WHERE (TIPO_IDENTIFICACION = ?) AND (NUMERO_IDENTIFICACION = ?)";

	private static String SQL_SELECT_BY_TIPO_AND_NUMERO_IDENTIFICACION_SENTENCE = "SELECT " + cs_COLUMNAS + " FROM " + cs_TABLA + " WHERE (TIPO_IDENTIFICACION = ?) AND (NUMERO_IDENTIFICACION = ?)";

	private static String SQL_SELECT_BY_SENTENCE = "SELECT " + cs_COLUMNAS + " FROM " + cs_TABLA
			+ " WHERE (NUMERO_IDENTIFICACION LIKE ?) OR (UPPER(PRIMER_APELLIDO) LIKE ?) OR (UPPER(SEGUNDO_APELLIDO) LIKE ?) OR (UPPER(PRIMER_NOMBRE) LIKE ?) OR (UPPER(SEGUNDO_NOMBRE) LIKE ?)";

	private static int NUMERO_COLUMNAS_BUSQUEDA = 5;

	private static String SQL_UPDATE_ESTADO_CARGA_SENTENCE = "UPDATE " + cs_TABLA + " SET ESTADO_CARGA = ?";

	private static String SQL_UPDATE_SENTENCE = "UPDATE "
			+ cs_TABLA
			+ " SET PRIMER_APELLIDO = ?, SEGUNDO_APELLIDO = ?, PRIMER_NOMBRE = ?, SEGUNDO_NOMBRE = ?, PEATVIA = ?, DIRECCION = ?, DEPARTAMENTO = ?, MUNICIPIO = ?, ACTIVIDAD_ECONOMICA = ?, TIPO_TELEFONO = ?, INDICATIVO = ?, TELEFONO = ?, APLICATIVO = ?, USUARIO = ?, OFICINA_MODIFICACION = ?, FECHA = ?, ESTADO_CARGA = ?, FECHA_ULTIMA_CARGA = ? WHERE (TIPO_IDENTIFICACION = ?) AND (NUMERO_IDENTIFICACION = ?) AND (DIGITO_VERIFICACION = ?)";

	private static String SQL_DELETE_SENTENCE = "DELETE FROM " + cs_TABLA + " WHERE (TIPO_IDENTIFICACION = ?) AND (NUMERO_IDENTIFICACION = ?)";

	private final static String QUERY_EXPORT = " SELECT  \n" + //
			" RPAD(NVL(TIPO_IDENTIFICACION, '0'), 1 , ' ')   \n" + //
			" || RPAD(NVL(NUMERO_IDENTIFICACION, '0'), 15 , ' ')  \n" + //
			" || RPAD(NVL(DIGITO_VERIFICACION, '0'), 1 , ' ')  \n" + //
			" || TRANSLATE(RPAD(NVL(PRIMER_APELLIDO, ' '), 20 , ' ') ,'ѿ?','aeiouaeiouaoaeiooaeioucAEIOUAEIOUAOAEIOOAEIOUCnN    ')  \n" + //
			" || TRANSLATE(RPAD(NVL(SEGUNDO_APELLIDO, ' '), 20 , ' ') ,'ѿ?','aeiouaeiouaoaeiooaeioucAEIOUAEIOUAOAEIOOAEIOUCnN    ')   \n" + //
			" || TRANSLATE(RPAD(NVL(PRIMER_NOMBRE, ' '), 20 , ' ')  ,'ѿ?','aeiouaeiouaoaeiooaeioucAEIOUAEIOUAOAEIOOAEIOUCnN    ')   \n" + //
			" || TRANSLATE(RPAD(NVL(SEGUNDO_APELLIDO, ' '), 20 , ' ') ,'ѿ?','aeiouaeiouaoaeiooaeioucAEIOUAEIOUAOAEIOOAEIOUCnN    ')   \n" + //
			" || TRANSLATE(RPAD(NVL(DIRECCION, ' '), 50 , ' ')  ,'ѿ?','aeiouaeiouaoaeiooaeioucAEIOUAEIOUAOAEIOOAEIOUCnN    ')   \n" + //
			" || LPAD(NVL(DEPARTAMENTO, '0'), 2 , '0') \n" + //
			" || LPAD(NVL(MUNICIPIO, '0'), 3 , '0') \n" + //
			" || LPAD(NVL(ACTIVIDAD_ECONOMICA, '0'), 5 , '0')\n" + //
			" || RPAD(NVL(PAIS, ' '), 3 , ' ')\n" + //
			" || RPAD(NVL(TIPO_TELEFONO, ' '), 1 , ' ')  \n" + //
			" || LPAD(NVL(INDICATIVO, '0'), 6 , '0') \n" + //
			" || LPAD(NVL(TELEFONO, '0'), 7 , '0') \n" + //
			" || LPAD(NVL(EXTENSION, '0'), 3 , '0') \n" + //
			" || LPAD(NVL(TIPO_TELEFONO2, '0'), 1 , '0') \n" + //
			" || LPAD(NVL(INDICATIVO2, '0'), 6 , '0') \n" + //
			" || LPAD(NVL(TELEFONO2, '0'), 7 , '0')  \n" + //
			" || LPAD(NVL(EXTENSION2, '0'), 3 , '0') \n" + //
			" || 'COP'   \n" + // RPAD(NVL(MONEDA, ' '), 3 , ' ')
			" || ' '  \n" + //
			" || TRANSLATE(RPAD(NVL(EMAIL, ' '), 40 , ' '),'ѿ?','aeiouaeiouaoaeiooaeioucAEIOUAEIOUAOAEIOOAEIOUCnN    ') \n" + //
			" || '     ' \n" + //
			" || RPAD(NVL(SEXO, ' '), 1 , ' ') \n" + //
			" FROM NC_TERCERO WHERE INGRESO_APP = 1  ";

	public TerceroDAO() {
		super(cs_TABLA, cs_COLUMNAS, cs_PK, new Tercero());
		sql_SELECT_BY_PRIMARY_KEY_SENTENCE = "SELECT " + cs_COLUMNAS + " FROM " + cs_TABLA + " WHERE ( TIPO_IDENTIFICACION = ? AND NUMERO_IDENTIFICACION = ?)";
		;
	}

	@Override
	public void internalUpdate(Connection con, Tercero row) throws Exception {
		executeUpdate(con, SQL_UPDATE_SENTENCE, new Object[] { row.getPrimerApellido(), row.getSegundoApellido(), row.getPrimerNombre(), row.getSegundoNombre(), row.getPEATVIA(), row.getDireccion(), row.getDepartamento(), row.getMunicipio(),
				row.getActividadEconomica(), row.getTipoTelefono(), row.getIndicativo(), row.getTelefono(), row.getAplicativo(), row.getUsuario(), row.getOficinaModificacion(), row.getFecha(), row.getEstadoCarga(), row.getFechaUltimaCarga(),
				row.getTipoIdentificacion(), row.getNumeroIdentificacion(), row.getDigitoVerificacion() });
	}

	public void updateEstadoCarga(String estadoCarga) throws Exception {
		executeUpdate(SQL_UPDATE_ESTADO_CARGA_SENTENCE, estadoCarga);
	}

	@Override
	public void internalDelete(Connection con, Tercero row) throws Exception {
		executeUpdate(con, SQL_DELETE_SENTENCE, new Object[] { row.getTipoIdentificacion(), row.getNumeroIdentificacion() });
	}

	@Override
	public Tercero findByPrimaryKey(Tercero row) throws Exception {
		return getByGeneral(SQL_SELECT_BY_PRIMARY_KEY_SENTENCE, new Object[] { row.getTipoIdentificacion(), row.getNumeroIdentificacion() });
	}

	public Tercero findByTipoAndNumeroIdentificacion(Tercero row) throws Exception {
		return getByGeneral(SQL_SELECT_BY_TIPO_AND_NUMERO_IDENTIFICACION_SENTENCE, new Object[] { row.getTipoIdentificacion(), row.getNumeroIdentificacion() });
	}

	public Collection<Tercero> findAll() throws Exception {
		return findByGeneral(SQL_SELECT_ALL_SENTENCE);
	}

	public Collection<Tercero> findByEstadoCarga(String estadoCarga) throws Exception {
		return findByGeneral(SQL_SELECT_BY_ESTADO_CARGA_SENTENCE, estadoCarga);
	}

	public Collection<Tercero> findBy(String palabraClave) throws Exception {
		return findByKeyWord(SQL_SELECT_BY_SENTENCE, palabraClave, NUMERO_COLUMNAS_BUSQUEDA);
	}

	public Collection<String> getTercerosArch() throws Exception {
		return findToStringByGeneral(QUERY_EXPORT, new Object[] {});
	}

	@Override
	protected Object[] getDataToAdd(@SuppressWarnings("unused") Connection con, Tercero row) throws Exception {
		return new Object[] { row.getTipoIdentificacion(), row.getNumeroIdentificacion(), row.getDigitoVerificacion(), row.getPrimerApellido(), row.getSegundoApellido(), row.getPrimerNombre(), row.getSegundoNombre(), row.getPEATVIA(),
				row.getDireccion(), row.getDepartamento(), row.getMunicipio(), row.getActividadEconomica(), row.getTipoTelefono(), row.getIndicativo(), row.getTelefono(), row.getAplicativo(), row.getUsuario(), row.getOficinaModificacion(),
				row.getFecha(), row.getEstadoCarga(), row.getFechaUltimaCarga(), row.getPais(), row.getExtension(), row.getExtension2(), row.getIndicativo2(), row.getTelefono2(), row.getTipoTelefono2(), row.getMoneda(),
				row.getIndicadorNotasContables(), row.getEMail(), row.getRegimenTributario(), row.getSexo(), row.getIngresoApp() };
	}

	@Override
	public String getXMLDataByPrimaryKey(Connection con, Tercero row) throws Exception {
		if (sql_SELECT_BY_PRIMARY_KEY_SENTENCE != null) {
			return getXMLDataGeneral(con, sql_SELECT_BY_PRIMARY_KEY_SENTENCE, new Object[] { row.getTipoIdentificacion(), row.getNumeroIdentificacion() });
		}
		throw new Exception("Se debe definir la llave primaria para realizar la accin");
	}

	@Override
	public Tercero getResultSetToVO(ResultSet result) throws Exception {
		Tercero row = new Tercero();

		row.setTipoIdentificacion(result.getString(1) != null ? result.getString(1).trim() : "");
		row.setNumeroIdentificacion(result.getString(2) != null ? result.getString(2).trim() : "");
		row.setDigitoVerificacion(result.getString(3) != null ? result.getString(3).trim() : "");
		row.setPrimerApellido(result.getString(4) != null ? result.getString(4).trim() : "");
		row.setSegundoApellido(result.getString(5) != null ? result.getString(5).trim() : "");
		row.setPrimerNombre(result.getString(6) != null ? result.getString(6).trim() : "");
		row.setSegundoNombre(result.getString(7) != null ? result.getString(7).trim() : "");
		row.setPEATVIA(result.getString(8) != null ? result.getString(8).trim() : "");
		row.setDireccion(result.getString(9) != null ? result.getString(9).trim() : "");
		row.setDepartamento(result.getString(10) != null ? result.getString(10).trim() : "");
		row.setMunicipio(result.getString(11) != null ? result.getString(11).trim() : "");
		row.setActividadEconomica(result.getString(12) != null ? result.getString(12).trim() : "");
		row.setTipoTelefono(result.getString(13) != null ? result.getString(13).trim() : "");
		row.setIndicativo(result.getString(14) != null ? result.getString(14).trim() : "");
		row.setTelefono(result.getString(15) != null ? result.getString(15).trim() : "");
		row.setAplicativo(result.getString(16) != null ? result.getString(16).trim() : "");
		row.setUsuario(result.getString(17) != null ? result.getString(17).trim() : "");
		row.setOficinaModificacion(result.getString(18) != null ? result.getString(18).trim() : "");
		row.setFecha(result.getString(19) != null ? result.getString(19).trim() : "");
		row.setEstadoCarga(result.getString(20) != null ? result.getString(20).trim() : "");
		row.setFechaUltimaCarga(result.getTimestamp(21));
		row.setPais(result.getString(22) != null ? result.getString(22).trim() : "");
		row.setExtension(result.getLong(23));
		row.setExtension2(result.getLong(24));
		row.setIndicativo2(result.getString(25) != null ? result.getString(25).trim() : "");
		row.setTelefono2(result.getString(26) != null ? result.getString(26).trim() : "");
		row.setTipoTelefono2(result.getString(27) != null ? result.getString(27).trim() : "");
		row.setMoneda(result.getString(28) != null ? result.getString(28).trim() : "");
		row.setIndicadorNotasContables(result.getString(29) != null ? result.getString(29).trim() : "");
		row.setEMail(result.getString(30) != null ? result.getString(30).trim() : "");
		row.setRegimenTributario(result.getString(31) != null ? result.getString(31).trim() : "");
		row.setSexo(result.getString(32) != null ? result.getString(32).trim() : "");
		row.setIngresoApp(result.getLong(33));

		return row;
	}
}