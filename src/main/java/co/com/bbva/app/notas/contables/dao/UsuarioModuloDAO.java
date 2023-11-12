package co.com.bbva.app.notas.contables.dao;

import co.com.bbva.app.notas.contables.datos.interfaces.IConsultasUsuario;
import co.com.bbva.app.notas.contables.dto.UsuarioModulo;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.List;

public class UsuarioModuloDAO extends CommonSeqDAO<UsuarioModulo> implements IConsultasUsuario {

	public UsuarioModuloDAO() {
		super(IConsultasUsuario.cs_TABLA, IConsultasUsuario.cs_COLUMNAS, IConsultasUsuario.cs_PK, new UsuarioModulo());
	}

	@Override
	public void internalUpdate(Connection con, UsuarioModulo row) throws Exception {
		executeUpdate(con, SQL_UPDATE_SENTENCE, new Object[] { row.getCodigoEmpleado(), row.getCodigoRol(), row.getCodigoAreaModificado(), row.getNombreAreaModificado(), row.getCodigoPerfilModificado(), row.getNombrePerfilModificado(),
				row.getActualizarAutomatico(), row.getEMailModificado(), row.getEstado(), row.getLastUpdate() , row.getCodigo() });
	}

	public UsuarioModulo findByCodigoEmpleado(UsuarioModulo row) throws Exception {
		return getByGeneral(SQL_SELECT_BY_CODIGO_EMPLEADO_SENTENCE, row.getCodigoEmpleado());
	}

	public Collection<UsuarioModulo> findAllByCodigoEmpleado(UsuarioModulo row) throws Exception {
		return findByGeneral(SQL_SELECT_ACTIVOS_BY_CODIGO_EMPLEADO_SENTENCE, row.getCodigoEmpleado());
	}

	public Collection<UsuarioModulo> findAll() throws Exception {
		return obtenerLista(IConsultasUsuario.SQL_SELECT_ALL_SENTENCE);
	}

	public Collection<UsuarioModulo> findByEstado(UsuarioModulo row) throws Exception {
		return findByGeneral(SQL_SELECT_BY_ESTADO_SENTENCE, row.getEstado());
	}

	public UsuarioModulo getUsuarioByCodEmpAndRol(String codigoEmp, int codigoRol) throws Exception {
		return getByGeneral(SQL_SELECT_BY_ID_AND_ROL_SENTENCE, new Object[] { codigoEmp, codigoRol });
	}

	public Collection<UsuarioModulo> findByRolAndEstado(int codigoRol, String estado) throws Exception {
		return findByGeneral(SQL_SELECT_BY_ROL_AND_ESTADO_SENTENCE, new Object[] { codigoRol, estado });
	}

	public Collection<UsuarioModulo> findByAreaAndRolAndEstado(String codigoArea, int codigoRol, String estado) throws Exception {
		return obtenerLista(SQL_SELECT_BY_AREA_AND_ROL_AND_ESTADO_SENTENCE, new Object[] { codigoArea, codigoRol, estado });
	}

	public Collection<UsuarioModulo> findBy(String palabraClave) throws Exception {
		String param = COMODIN + palabraClave + COMODIN;
		return obtenerLista(SQL_SELECT_ALL_BY_FILTER, new Object[] { param, param, palabraClave, param, param, param, param });
	}

	// public String getVOTOXMLString(UsuarioModulo row) {
	// String xmlData = "";
	//
	// xmlData = "<UsuarioModulo>";
	//
	// return xmlData;
	// }

	@Override
	protected Object[] getDataToAdd(Connection con, UsuarioModulo row) throws Exception {
		row.setCodigo(getMaxCode(con));
		return new Object[] { row.getCodigo(), row.getCodigoEmpleado(), row.getCodigoRol(), row.getCodigoAreaModificado(), row.getNombreAreaModificado(), row.getCodigoPerfilModificado(), row.getNombrePerfilModificado(),
				row.getActualizarAutomatico(), row.getEMailModificado(), row.getEstado() , row.getLastUpdate() };
	}

	@Override
	public UsuarioModulo getResultSetToVO(ResultSet result) throws Exception {
		UsuarioModulo row = new UsuarioModulo();

		row.setCodigo(result.getInt(1));
		row.setCodigoEmpleado(result.getString(2));
		row.setCodigoRol(result.getInt(3));
		row.setCodigoAreaModificado(result.getString(4) != null ? result.getString(4) : "");
		row.setNombreAreaModificado(result.getString(5) != null ? result.getString(5) : "");
		row.setCodigoPerfilModificado(result.getString(6) != null ? result.getString(6) : "");
		row.setNombrePerfilModificado(result.getString(7) != null ? result.getString(7) : "");
		row.setActualizarAutomatico(result.getString(8) != null ? result.getString(8) : "");
		row.setEMailModificado(result.getString(9) != null ? result.getString(9) : "");
		row.setEstado(result.getString(10));

		return row;
	}

	public List<UsuarioModulo> findByAltamiraInactivos() throws Exception {
		return obtenerLista(SQL_SELECT_BY_ALTAMIRA_INACTIVOS_SENTENCE);
	}

	public Collection<UsuarioModulo> findPorTiempos(Timestamp desde) throws Exception {
		return obtenerLista(SQL_SELECT_POR_TIEMPOS, desde);
	}

	public void changeUserStatus(Connection con, UsuarioModulo row) throws Exception {
		String msgLastUpdate;
		if (row.getEstado().equals("A")) {
			row.setEstado("I");
			msgLastUpdate = "USER WAS DISABLED - APP";
		} else {
			row.setEstado("A");
			msgLastUpdate = "USER WAS ENABLED - APP";
		}
		row.setLastUpdate(msgLastUpdate);
		executeUpdate(con, SQL_UPDATE_ESTADO_SENTENCE, new Object[] { row.getEstado(), row.getLastUpdate(), row.getCodigo() });
	}
}