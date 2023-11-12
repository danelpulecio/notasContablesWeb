/*
	Nombre DTO: NotaContableTema
 */

package co.com.bbva.app.notas.contables.dao.bk;

import co.com.bbva.app.notas.contables.dto.NotaContableTema;

import java.sql.Connection;
import java.sql.ResultSet;

public class BKNotaContableTemaDAO extends BKCommonDAO<NotaContableTema> {

	private static String cs_COLUMNAS = "CODIGO, CODIGO_NOTA_CONTABLE, CODIGO_TEMA, FECHA_CONTABLE, PARTIDA_CONTABLE, NATURALEZA1, CONTRAPARTIDA_CONTABLE, NATURALEZA2, COD_SUC_DEST_PART, COD_SUC_DEST_CONTPART, CODIGO_DIVISA, MONTO, REFERENCIA1, REFERENCIA2, TIPO_IDENTIFICACION1, NUMERO_IDENTIFICACION1, DIGITO_VERIFICACION1, NOMBRE_COMPLETO1, TIPO_IDENTIFICACION2, NUMERO_IDENTIFICACION2, DIGITO_VERIFICACION2, NOMBRE_COMPLETO2, CONTRATO1, CONTRATO2, DESCRIPCION, NUMERO_ASIENTO, NUMERO_APUNTE";

	private static String cs_TABLA = "NC_BK_NOTA_CONTABLE_TEMA";

	private static String cs_PK = "CODIGO";

	private static String SQL_UPDATE_SENTENCE = "UPDATE " + cs_TABLA + " SET NUMERO_ASIENTO = ?, NUMERO_APUNTE = ? WHERE (CODIGO = ?)";

	public BKNotaContableTemaDAO() {
		super(cs_TABLA, cs_COLUMNAS, cs_PK, new NotaContableTema());
	}

	@Override
	public void internalUpdate( Connection con,NotaContableTema row) throws Exception {
		executeUpdate(con, SQL_UPDATE_SENTENCE, new Object[] { row.getNumeroAsiento(), row.getNumeroApunte(), row.getCodigo() });
	}

	@Override
	protected Object[] getDataToAdd(@SuppressWarnings("unused") Connection con, NotaContableTema row) throws Exception {
		return new Object[] { row.getCodigo(), row.getCodigoNotaContable(), row.getCodigoTema(), row.getFechaContable(), row.getPartidaContable(), row.getNaturalezaPartidaContable(), row.getContrapartidaContable(),
				row.getNaturalezaContrapartidaContable(), row.getCodigoSucursalDestinoPartida(), row.getCodigoSucursalDestinoContraPartida(), row.getCodigoDivisa(), row.getMonto().doubleValue(), row.getReferencia1(), row.getReferencia2(),
				row.getTipoIdentificacion1(), row.getNumeroIdentificacion1(), row.getDigitoVerificacion1(), row.getNombreCompleto1(), row.getTipoIdentificacion2(), row.getNumeroIdentificacion2(), row.getDigitoVerificacion2(),
				row.getNombreCompleto2(), row.getContrato1(), row.getContrato2(), row.getDescripcion(), row.getNumeroAsiento(), row.getNumeroApunte() };
	}

	@Override
	public NotaContableTema getResultSetToVO(ResultSet result) throws Exception {
		NotaContableTema row = new NotaContableTema();

		row.setCodigo(result.getInt(1));
		row.setCodigoNotaContable(result.getInt(2));
		row.setCodigoTema(result.getInt(3));
		row.setFechaContable(result.getDate(4));
		row.setPartidaContable(result.getString(5) != null ? result.getString(5) : "");
		row.setNaturalezaPartidaContable(result.getString(6) != null ? result.getString(6) : "");
		row.setContrapartidaContable(result.getString(7) != null ? result.getString(7) : "");
		row.setNaturalezaContrapartidaContable(result.getString(8) != null ? result.getString(8) : "");
		row.setCodigoSucursalDestinoPartida(result.getString(9) != null ? result.getString(9) : "");
		row.setCodigoSucursalDestinoContraPartida(result.getString(10) != null ? result.getString(10) : "");
		row.setCodigoDivisa(result.getString(11) != null ? result.getString(11) : "");
		row.setMonto(result.getDouble(12));
		row.setReferencia1(result.getString(13) != null ? result.getString(13) : "");
		row.setReferencia2(result.getString(14) != null ? result.getString(14) : "");
		row.setTipoIdentificacion1(result.getString(15) != null ? result.getString(15) : "");
		row.setNumeroIdentificacion1(result.getString(16) != null ? result.getString(16) : "");
		row.setDigitoVerificacion1(result.getString(17) != null ? result.getString(17) : "");
		row.setNombreCompleto1(result.getString(18) != null ? result.getString(18) : "");
		row.setTipoIdentificacion2(result.getString(19) != null ? result.getString(19) : "");
		row.setNumeroIdentificacion2(result.getString(20) != null ? result.getString(20) : "");
		row.setDigitoVerificacion2(result.getString(21) != null ? result.getString(21) : "");
		row.setNombreCompleto2(result.getString(22) != null ? result.getString(22) : "");
		row.setContrato1(result.getString(23) != null ? result.getString(23) : "");
		row.setContrato2(result.getString(24) != null ? result.getString(24) : "");
		row.setDescripcion(result.getString(25) != null ? result.getString(25) : "");
		row.setNumeroAsiento(result.getString(26) != null ? result.getString(26) : "");
		row.setNumeroApunte(result.getString(27) != null ? result.getString(27) : "");

		return row;
	}

	// public void updateAsientoContra(NotaContableTema registro) throws Exception {
	// throw new Exception("updateAsientoContra: no implementado an");
	// }
}