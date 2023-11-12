/*
	Nombre DTO: NotaContableRegistroLibre
 */

package co.com.bbva.app.notas.contables.dao.bk;

import co.com.bbva.app.notas.contables.dto.NotaContableRegistroLibre;

import java.sql.Connection;
import java.sql.ResultSet;

public class BKNotaContableRegistroLibreDAO extends BKCommonDAO<NotaContableRegistroLibre> {

	private static String cs_COLUMNAS = "CODIGO, CODIGO_NOTA_CONTABLE, FECHA_CONTABLE, CUENTA_CONTABLE, NATURALEZA, COD_SUC_DESTINO, CODIGO_DIVISA, MONTO, REFERENCIA, TIPO_IDENTIFICACION, DIGITO_VERIFICACION, NOMBRE_COMPLETO, NUMERO_IDENTIFICACION, CONTRATO, NUMERO_ASIENTO, NUMERO_APUNTE";

	private static String cs_TABLA = "NC_BK_NOTA_CONT_REG_LIBRE";

	private static String cs_PK = "CODIGO";

	private static String SQL_UPDATE_SENTENCE = "UPDATE " + cs_TABLA + " SET NUMERO_ASIENTO = ?, NUMERO_APUNTE = ? WHERE (CODIGO = ?)";

	public BKNotaContableRegistroLibreDAO() {
		super(cs_TABLA, cs_COLUMNAS, cs_PK, new NotaContableRegistroLibre());
	}

	@Override
	public void internalUpdate( Connection con,NotaContableRegistroLibre row) throws Exception {
		executeUpdate(con, SQL_UPDATE_SENTENCE, new Object[] { row.getNumeroAsiento(), row.getNumeroApunte(), row.getCodigo() });
	}

	@Override
	protected Object[] getDataToAdd(@SuppressWarnings("unused") Connection con, NotaContableRegistroLibre row) throws Exception {
		return new Object[] { row.getCodigo(), row.getCodigoNotaContable(), row.getFechaContable(), row.getCuentaContable(), row.getNaturalezaCuentaContable(), row.getCodigoSucursalDestino(), row.getCodigoDivisa(), row.getMonto(),
				row.getReferencia(), row.getTipoIdentificacion(), row.getDigitoVerificacion(), row.getNombreCompleto(), row.getNumeroIdentificacion(), row.getContrato(), row.getNumeroAsiento(), row.getNumeroApunte() };
	}

	@Override
	public NotaContableRegistroLibre getResultSetToVO(ResultSet result) throws Exception {
		NotaContableRegistroLibre row = new NotaContableRegistroLibre();

		row.setCodigo(result.getInt(1));
		row.setCodigoNotaContable(result.getInt(2));
		row.setFechaContable(result.getDate(3));
		row.setCuentaContable(result.getString(4) != null ? result.getString(4) : "");
		row.setNaturalezaCuentaContable(result.getString(5) != null ? result.getString(5) : "");
		row.setCodigoSucursalDestino(result.getString(6) != null ? result.getString(6) : "");
		row.setCodigoDivisa(result.getString(7) != null ? result.getString(7) : "");
		row.setMonto(result.getDouble(8));
		row.setReferencia(result.getString(9) != null ? result.getString(9) : "");
		row.setTipoIdentificacion(result.getString(10) != null ? result.getString(10) : "");
		row.setNumeroIdentificacion(result.getString(11) != null ? result.getString(11) : "");
		row.setDigitoVerificacion(result.getString(12) != null ? result.getString(12) : "");
		row.setNombreCompleto(result.getString(13) != null ? result.getString(13) : "");
		row.setContrato(result.getString(14) != null ? result.getString(14) : "");
		row.setNumeroAsiento(result.getString(15) != null ? result.getString(15) : "");
		row.setNumeroApunte(result.getString(16) != null ? result.getString(16) : "");

		return row;
	}
}