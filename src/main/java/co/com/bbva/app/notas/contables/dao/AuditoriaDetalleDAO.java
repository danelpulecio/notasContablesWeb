package co.com.bbva.app.notas.contables.dao;

import co.com.bbva.app.notas.contables.dto.AuditoriaDetalle;

import java.sql.Connection;
import java.sql.ResultSet;

public class AuditoriaDetalleDAO extends CommonSeqDAO<AuditoriaDetalle> {

	public AuditoriaDetalleDAO() {
		super(IAuditoriaDetalleSentence.cs_TABLA, IAuditoriaDetalleSentence.cs_COLUMNAS, IAuditoriaDetalleSentence.cs_PK, new AuditoriaDetalle());
	}

	public AuditoriaDetalle getByCodigoAuditoria(AuditoriaDetalle row) throws Exception {
		return getByGeneral(SQL_SELECT_BY_CODIGO_AUDITORIA_SENTENCE, row.getCodigoAuditoria());
	}

	@Override
	protected Object[] getDataToAdd(Connection con, AuditoriaDetalle row) throws Exception {
		row.setCodigo(getMaxCode(con));
		return new Object[] { row.getCodigo(), row.getCodigoAuditoria(), row.getRegistroOriginal(), row.getRegistroModificado() };
	}

	@Override
	public AuditoriaDetalle getResultSetToVO(ResultSet result) throws Exception {
		AuditoriaDetalle row = new AuditoriaDetalle();

		row.setCodigo(result.getInt(1));
		row.setCodigoAuditoria(result.getInt(2));
		row.setRegistroOriginal(result.getString(3) != null ? result.getString(3) : "");
		row.setRegistroModificado(result.getString(4) != null ? result.getString(4) : "");

		return row;
	}

	@Override
	protected void internalUpdate(@SuppressWarnings("unused") Connection con, @SuppressWarnings("unused") AuditoriaDetalle row) throws Exception {
		throw new Exception("Mtodo no implementado");
	}
}