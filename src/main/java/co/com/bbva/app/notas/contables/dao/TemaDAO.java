/*
	Nombre DTO: Tema
 */

package co.com.bbva.app.notas.contables.dao;

import co.com.bbva.app.notas.contables.dto.Tema;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.Collection;
import java.util.List;

public class TemaDAO extends CommonSeqDAO<Tema> {

	private static String cs_COLUMNAS = "CODIGO, CODIGO_CONCEPTO, NOMBRE, ACTIVAR, PARTIDA_CONTABLE, NATURALEZA1, REFERENCIA1, TERCERO1, CONTRATO1, RIESGO_OPERACIONAL, CONTRAPARTIDA_CONTABLE, NATURALEZA2, REFERENCIA2, TERCERO2, CONTRATO2, OBLIGATORIO, TIPO_DIVISA, ESTADO";

	private static String cs_TABLA = "NC_TEMA";

	private static String cs_PK = "CODIGO";

	private static String SQL_SELECT_ALL_SENTENCE = "SELECT " + cs_COLUMNAS + " FROM " + cs_TABLA + " ORDER BY CODIGO";

	private static String SQL_SELECT_BY_ESTADO_SENTENCE = "SELECT " + cs_COLUMNAS + " FROM " + cs_TABLA + " WHERE (ESTADO = ?) ORDER BY CODIGO";

	private static String SQL_SELECT_BY_PARTIDA_OR_CONTRAPARTIDA_SENTENCE = "SELECT " + cs_COLUMNAS + " FROM " + cs_TABLA + " WHERE ESTADO= 'A' AND (PARTIDA_CONTABLE = ? OR CONTRAPARTIDA_CONTABLE = ?) ORDER BY CODIGO";

	private static String SQL_SELECT_BY_CONCEPTO_SENTENCE = "SELECT " + cs_COLUMNAS + " FROM " + cs_TABLA + " WHERE (CODIGO_CONCEPTO = ?) ORDER BY CODIGO";

	private static String SQL_SELECT_BY_CONCEPTO_AND_ESTADO_SENTENCE = "SELECT " + cs_COLUMNAS + " FROM " + cs_TABLA + " WHERE (CODIGO_CONCEPTO = ?) AND (ESTADO = ?) ORDER BY CODIGO";

	private static String SQL_SELECT_BY_SENTENCE = "SELECT " + cs_COLUMNAS + " FROM " + cs_TABLA + " WHERE (CODIGO_CONCEPTO = ?) AND (NOMBRE LIKE ?) ORDER BY CODIGO";

	private static String SQL_SELECT_BY_CUENTAS_INACTIVAS_SENCENCE = "select c.nombre \"nombreConcepto\", t.nombre \"nombre\", p.numero_cuenta \"partidaContable\", p.naturaleza \"naturaleza1\" from nc_tema t"
			+ " left join nc_puc p on (p.numero_cuenta=t.partida_contable or p.numero_cuenta=t.contrapartida_contable) " + " left join nc_concepto c on c.codigo=t.codigo_concepto " + " where p.estado_carga= 'I' and t.estado = 'A' ";

	private static int NUMERO_COLUMNAS_BUSQUEDA = 1;

	private static String SQL_UPDATE_SENTENCE = "UPDATE "
			+ cs_TABLA
			+ " SET CODIGO_CONCEPTO = ?, NOMBRE = ?, ACTIVAR = ?, PARTIDA_CONTABLE = ?, NATURALEZA1 = ?, REFERENCIA1 = ?, TERCERO1 = ?, CONTRATO1 = ?, RIESGO_OPERACIONAL = ?, CONTRAPARTIDA_CONTABLE = ?, NATURALEZA2 = ?, REFERENCIA2 = ?, TERCERO2 = ?, CONTRATO2 = ?, OBLIGATORIO = ?, TIPO_DIVISA = ?, ESTADO = ? WHERE (CODIGO = ?)";

	public TemaDAO() {
		super(cs_TABLA, cs_COLUMNAS, cs_PK, new Tema());
	}

	@Override
	public void internalUpdate(Connection con, Tema row) throws Exception {
		executeUpdate(con, SQL_UPDATE_SENTENCE, new Object[] { row.getCodigoConcepto(), row.getNombre(), row.getActivar(), row.getPartidaContable(), row.getNaturaleza1(), row.getReferencia1(), row.getTercero1(), row.getContrato1(),
				row.getRiesgoOperacional(), row.getContraPartidaContable(), row.getNaturaleza2(), row.getReferencia2(), row.getTercero2(), row.getContrato2(), row.getObligatorio(), row.getTipoDivisa(), row.getEstado(), row.getCodigo() });
	}

	public Collection<Tema> findAll() throws Exception {
		return findByGeneral(SQL_SELECT_ALL_SENTENCE);
	}

	public Collection<Tema> findByEstado(Tema row) throws Exception {
		return findByGeneral(SQL_SELECT_BY_ESTADO_SENTENCE, row.getEstado());
	}

	public Collection<Tema> findByPartidaOrContraPartida(Tema row) throws Exception {
		return findByGeneral(SQL_SELECT_BY_PARTIDA_OR_CONTRAPARTIDA_SENTENCE, new Object[] { row.getPartidaContable(), row.getContraPartidaContable() });
	}

	public Collection<Tema> findByConcepto(Tema row) throws Exception {
		return findByGeneral(SQL_SELECT_BY_CONCEPTO_SENTENCE, row.getCodigoConcepto());
	}

	public Collection<Tema> findByConceptoAndEstado(Tema row) throws Exception {
		return findByGeneral(SQL_SELECT_BY_CONCEPTO_AND_ESTADO_SENTENCE, new Object[] { row.getCodigoConcepto(), row.getEstado() });
	}

	public Collection<Tema> findBy(String palabraClave) throws Exception {
		return findByKeyWord(SQL_SELECT_BY_SENTENCE, palabraClave, NUMERO_COLUMNAS_BUSQUEDA);
	}

	@Override
	protected Object[] getDataToAdd(Connection con, Tema row) throws Exception {
		row.setCodigo(getMaxCode(con));
		return new Object[] { row.getCodigo(), row.getCodigoConcepto(), row.getNombre(), row.getActivar(), row.getPartidaContable(), row.getNaturaleza1(), row.getReferencia1(), row.getTercero1(), row.getContrato1(), row.getRiesgoOperacional(),
				row.getContraPartidaContable(), row.getNaturaleza2(), row.getReferencia2(), row.getTercero2(), row.getContrato2(), row.getObligatorio(), row.getTipoDivisa(), row.getEstado() };
	}

	@Override
	public Tema getResultSetToVO(ResultSet result) throws Exception {
		Tema row = new Tema();

		row.setCodigo(result.getInt(1));
		row.setCodigoConcepto(result.getInt(2));
		row.setNombre(result.getString(3));
		row.setActivar(result.getString(4));
		row.setPartidaContable(result.getString(5));
		row.setNaturaleza1(result.getString(6));
		row.setReferencia1(result.getString(7));
		row.setTercero1(result.getString(8));
		row.setContrato1(result.getString(9));
		row.setRiesgoOperacional(result.getString(10));
		row.setContraPartidaContable(result.getString(11));
		row.setNaturaleza2(result.getString(12));
		row.setReferencia2(result.getString(13));
		row.setTercero2(result.getString(14));
		row.setContrato2(result.getString(15));
		row.setObligatorio(result.getString(16));
		row.setTipoDivisa(result.getString(17));
		row.setEstado(result.getString(18));

		return row;
	}

	public List<Tema> findByCuentasInactivas() throws Exception {
		return obtenerLista(SQL_SELECT_BY_CUENTAS_INACTIVAS_SENCENCE, new Object[] {});
	}
}