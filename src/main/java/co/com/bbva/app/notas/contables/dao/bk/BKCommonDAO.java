package co.com.bbva.app.notas.contables.dao.bk;

import co.com.bbva.app.notas.contables.dao.CommonDAO;
import co.com.bbva.app.notas.contables.dto.CommonVO;

public abstract class BKCommonDAO<T extends CommonVO<T>> extends CommonDAO<T> {

	protected BKCommonDAO(String tableName, String columnNames, String pkName, T instance) {
		super(tableName, columnNames, pkName, instance);
	}

	@Override
	protected String getJndiDatasourceName() {
		return "jdbc/notasContables";
	}

}
