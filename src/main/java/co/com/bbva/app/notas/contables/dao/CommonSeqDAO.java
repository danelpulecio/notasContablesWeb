package co.com.bbva.app.notas.contables.dao;

import co.com.bbva.app.notas.contables.dto.CommonVO;

import java.sql.Connection;

public abstract class CommonSeqDAO<T extends CommonVO<T>> extends CommonDAO<T> {

	protected CommonSeqDAO(String tableName, String columnNames, String pkName, T instance) {
		super(tableName, columnNames, pkName, instance);
	}

	@Override
	protected int getMaxCode(Connection con) throws Exception {
		return getMaxCode(con, sql_SELECT_SEQUENCE_SENTENCE);
	}
}
