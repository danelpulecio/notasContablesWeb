/*
	Nombre DTO: PUC
 */

package co.com.bbva.app.notas.contables.carga.dao;

import co.com.bbva.app.notas.contables.carga.dto.PUC;
import co.com.bbva.app.notas.contables.dao.CommonDAO;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collection;

public class PUCDAO extends CommonDAO<PUC> {

	private static String cs_COLUMNAS = "NUMERO_CUENTA, DIGITO_VERIFICACION, DESCRIPCION, ESTADO_CUENTA, CENTRO_RESPONSABLE, INDICADOR_CUENTA, NATURALEZA, TIPO_CUENTA, FORMA_ACTUALIZACION, CONTRAPARTIDA_DEBE, CONTRAPARTIDA_HABER, TIPO_APUNTE, TIPO_CENTRO_ORIGEN_AUT, INDICADOR_CENTRO_ORIGEN, CENTROS_ORIGEN_AUTORIZADOS, TIPO_CENTRO_DESTINO_AUT, INDICADOR_CENTRO_DESTINO, CENTROS_DESTINO_AUTORIZADOS, TIPO_MONEDA, INDICADOR_SIRO, INDICADOR_TERCERO, ESTADO_CARGA, FECHA_ULTIMA_CARGA";

	private static String cs_TABLA = "NC_PUC";

	private static String cs_PK = "NUMERO_CUENTA";

	private static String SQL_SELECT_ALL_SENTENCE = "SELECT " + cs_COLUMNAS + " FROM " + cs_TABLA + " ORDER BY NUMERO_CUENTA";

	private static String SQL_SELECT_BY_ESTADO_CARGA_SENTENCE = "SELECT " + cs_COLUMNAS + " FROM " + cs_TABLA + " WHERE (ESTADO_CARGA = ?)";

	private static String SQL_SELECT_BY_CUENTA_SENTENCE = "SELECT " + cs_COLUMNAS + " FROM " + cs_TABLA + " WHERE ESTADO_CARGA = 'A' AND NUMERO_CUENTA LIKE ? ORDER BY NUMERO_CUENTA";

	private static String SQL_SELECT_BY_SENTENCE = "SELECT "
			+ cs_COLUMNAS
			+ " FROM "
			+ cs_TABLA
			+ " WHERE (NUMERO_CUENTA LIKE ?) OR (DESCRIPCION LIKE ?) OR (CENTRO_RESPONSABLE LIKE ?) OR (NATURALEZA LIKE ?) OR (CONTRAPARTIDA_DEBE LIKE ?) OR (CONTRAPARTIDA_HABER LIKE ?) OR (TIPO_APUNTE LIKE ?) OR (CENTROS_ORIGEN_AUTORIZADOS LIKE ?) OR (CENTROS_DESTINO_AUTORIZADOS LIKE ?)";

	private static String SQL_SELECT_BY_CENTROS = "SELECT " + cs_COLUMNAS + " FROM " + cs_TABLA + " WHERE (NUMERO_CUENTA LIKE ?) ORDER BY NUMERO_CUENTA";

	private static int NUMERO_COLUMNAS_BUSQUEDA = 9;

	private static String SQL_UPDATE_ESTADO_CARGA_SENTENCE = "UPDATE " + cs_TABLA + " SET ESTADO_CARGA = ?";

	private static String SQL_UPDATE_SENTENCE = "UPDATE "
			+ cs_TABLA
			+ " SET DIGITO_VERIFICACION = ?, DESCRIPCION = ?, ESTADO_CUENTA = ?, CENTRO_RESPONSABLE = ?, INDICADOR_CUENTA = ?, NATURALEZA = ?, TIPO_CUENTA = ?, FORMA_ACTUALIZACION = ?, CONTRAPARTIDA_DEBE = ?, CONTRAPARTIDA_HABER = ?, TIPO_APUNTE = ?, TIPO_CENTRO_ORIGEN_AUT = ?, INDICADOR_CENTRO_ORIGEN = ?, CENTROS_ORIGEN_AUTORIZADOS = ?, TIPO_CENTRO_DESTINO_AUT = ?, INDICADOR_CENTRO_DESTINO = ?, CENTROS_DESTINO_AUTORIZADOS = ?, TIPO_MONEDA = ?, INDICADOR_SIRO = ?, INDICADOR_TERCERO = ?, ESTADO_CARGA = ?, FECHA_ULTIMA_CARGA = ? WHERE (NUMERO_CUENTA = ?)";

	private final static String QUERY_ALTAMIRA = "SELECT RPAD(NVL(P.NUMERO_CUENTA,' '), 15, ' ') \n" + //
			"||RPAD(NVL(P.DIGITO_VERIFICACION,' '), 2, ' ') \n" + //
			"||RPAD(NVL(P.DESCRIPCION,' '), 65, ' ') \n" + //
			"||RPAD(NVL(P.ESTADO_CUENTA,' '), 1, ' ') \n" + //
			"||LPAD(NVL(P.CENTRO_RESPONSABLE,'0'), 4, '0') \n" + //
			"||RPAD(NVL(P.INDICADOR_CUENTA,' '), 1, ' ') \n" + //
			"||RPAD(NVL(P.NATURALEZA,' '), 1, ' ') \n" + //
			"||RPAD(NVL(P.TIPO_CUENTA,' '), 1, ' ') \n" + //
			"||RPAD(NVL(P.FORMA_ACTUALIZACION,' '), 1, ' ') \n" + //
			"||RPAD(NVL(P.CONTRAPARTIDA_DEBE,' '), 15, ' ') \n" + //
			"||RPAD(NVL(P.CONTRAPARTIDA_HABER,' '), 15, ' ') \n" + //
			"||RPAD(NVL(P.TIPO_APUNTE,' '), 1, ' ') \n" + //
			"||RPAD(NVL(P.TIPO_CENTRO_ORIGEN_AUT,' '), 5, ' ') \n" + //
			"||RPAD(NVL(P.INDICADOR_CENTRO_ORIGEN,' '), 1, ' ') \n" + //
			"||LPAD(NVL(P.CENTROS_ORIGEN_AUTORIZADOS,'0'), 84, '0') \n" + //
			"||RPAD(NVL(P.TIPO_CENTRO_DESTINO_AUT,'0'), 5, ' ') \n" + //
			"||RPAD(NVL(P.INDICADOR_CENTRO_DESTINO,' '), 1, ' ') \n" + //
			"||LPAD(NVL(P.CENTROS_DESTINO_AUTORIZADOS,'0'), 84, '0') \n" + //
			"||RPAD(NVL(P.TIPO_MONEDA,' '), 1, ' ') \n" + //
			"||RPAD(NVL(P.INDICADOR_SIRO,' '), 1, ' ') \n" + //
			"FROM NC_PUC P";

	public PUCDAO() {
		super(cs_TABLA, cs_COLUMNAS, cs_PK, new PUC());
	}

	@Override
	public void internalUpdate(Connection con, PUC row) throws Exception {
		executeUpdate(con, SQL_UPDATE_SENTENCE, new Object[] { row.getDigitoVerificacion(), row.getDescripcion(), row.getEstadoCuenta(), row.getCentroResponsable(), row.getIndicadorCuenta(), row.getNaturaleza(), row.getTipoCuenta(),
				row.getFormaActualizacion(), row.getContrapartidaDebe(), row.getContrapartidaHaber(), row.getTipoApunte(), row.getTipoCentroOrigenAutorizado(), row.getIndicadorCentroOrigen(), row.getCentrosOrigenAutorizados(),
				row.getTipoCentroDestinoAutorizado(), row.getIndicadorCentroDestino(), row.getCentrosDestinoAutorizados(), row.getTipoMoneda(), row.getIndicadorSIRO(), row.getIndicadorTercero(), row.getEstadoCarga(), row.getFechaUltimaCarga(),
				row.getNumeroCuenta() });
	}

	public void updateEstadoCarga(String estadoCarga) throws Exception {
		executeUpdate(SQL_UPDATE_ESTADO_CARGA_SENTENCE, estadoCarga);
	}

	public Collection<PUC> findAll() throws Exception {
		return findByGeneral(SQL_SELECT_ALL_SENTENCE);
	}

	public Collection<PUC> findByEstadoCarga(String estadoCarga) throws Exception {
		return findByGeneral(SQL_SELECT_BY_ESTADO_CARGA_SENTENCE, estadoCarga);
	}

	public Collection<String> getPUCArchAltamira() throws Exception {
		return findToStringByGeneral(QUERY_ALTAMIRA, new Object[] {});
	}

	public Collection<PUC> findCentrosBy(String numCuenta) throws Exception {
		return findByGeneral(SQL_SELECT_BY_CENTROS, new Object[] { COMODIN + numCuenta + COMODIN });

	}

	public Collection<PUC> findBy(String palabraClave) throws Exception {
		ArrayList<Object> params = new ArrayList<Object>();
		for (int count = 1; count <= NUMERO_COLUMNAS_BUSQUEDA; count++) {
			if (count != 1) {
				params.add(COMODIN + palabraClave + COMODIN);
			} else {
				params.add(palabraClave + COMODIN);
			}
		}
		return findByGeneral(SQL_SELECT_BY_SENTENCE, params.toArray());
	}

	@Override
	protected Object[] getDataToAdd(@SuppressWarnings("unused") Connection con, PUC row) throws Exception {
		return new Object[] { row.getNumeroCuenta(), row.getDigitoVerificacion(), row.getDescripcion(), row.getEstadoCuenta(), row.getCentroResponsable(), row.getIndicadorCuenta(), row.getNaturaleza(), row.getTipoCuenta(),
				row.getFormaActualizacion(), row.getContrapartidaDebe(), row.getContrapartidaHaber(), row.getTipoApunte(), row.getTipoCentroOrigenAutorizado(), row.getIndicadorCentroOrigen(), row.getCentrosOrigenAutorizados(),
				row.getTipoCentroDestinoAutorizado(), row.getIndicadorCentroDestino(), row.getCentrosDestinoAutorizados(), row.getTipoMoneda(), row.getIndicadorSIRO(), row.getIndicadorTercero(), row.getEstadoCarga(), row.getFechaUltimaCarga() };
	}

	@Override
	public PUC getResultSetToVO(ResultSet result) throws Exception {
		PUC row = new PUC();

		row.setNumeroCuenta(result.getString(1));
		row.setDigitoVerificacion(result.getString(2));
		row.setDescripcion(result.getString(3));
		row.setEstadoCuenta(result.getString(4));
		row.setCentroResponsable(result.getString(5));
		row.setIndicadorCuenta(result.getString(6));
		row.setNaturaleza(result.getString(7));
		row.setTipoCuenta(result.getString(8));
		row.setFormaActualizacion(result.getString(9));
		row.setContrapartidaDebe(result.getString(10));
		row.setContrapartidaHaber(result.getString(11));
		row.setTipoApunte(result.getString(12));
		row.setTipoCentroOrigenAutorizado(result.getString(13) != null ? result.getString(13) : "");
		row.setIndicadorCentroOrigen(result.getString(14) != null ? result.getString(14) : "");
		row.setCentrosOrigenAutorizados(result.getString(15) != null ? result.getString(15) : "");
		row.setTipoCentroDestinoAutorizado(result.getString(16) != null ? result.getString(16) : "");
		row.setIndicadorCentroDestino(result.getString(17) != null ? result.getString(17) : "");
		row.setCentrosDestinoAutorizados(result.getString(18) != null ? result.getString(18) : "");
		row.setTipoMoneda(result.getString(19));
		row.setIndicadorSIRO(result.getString(20));
		row.setIndicadorTercero(result.getString(21));
		row.setEstadoCarga(result.getString(22));
		row.setFechaUltimaCarga(result.getTimestamp(23));

		return row;
	}

	public Collection<PUC> searchPUCPorCuenta(String cuenta) throws Exception {
		return findByGeneral(SQL_SELECT_BY_CUENTA_SENTENCE, COMODIN + cuenta + COMODIN);
	}
}