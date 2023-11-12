package co.com.bbva.app.notas.contables.dao;

import co.com.bbva.app.notas.contables.dto.Menu;
import co.com.bbva.app.notas.contables.dto.SubMenu;
import co.com.bbva.app.notas.contables.dto.SubMenuRol;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.Collection;

public class SubMenuRolDAO extends CommonSeqDAO<SubMenuRol> {

	private final static String FIND_BY_ROL = "SELECT SMR.CODIGO_ROL, SM.ACCION, SM.NOMBRE, MEN.CODIGO, MEN.NOMBRE, MEN.ORDEN_VISUAL FROM NC_SUB_MENU_ROL SMR LEFT JOIN NC_SUB_MENU SM ON SM.CODIGO = SMR.CODIGO_SUB_MENU LEFT JOIN NC_MENU MEN ON MEN.CODIGO = SM.CODIGO_MENU WHERE SMR.CODIGO_ROL = ?";

	private static String cs_COLUMNAS = "CODIGO_ROL, CODIGO_SUB_MENU";

	private static String cs_TABLA = "NC_SUB_MENU_ROL";
	private static String cs_PK = null;

	public SubMenuRolDAO() {
		super(cs_TABLA, cs_COLUMNAS, cs_PK, new SubMenuRol());
	}

	@Override
	protected Object[] getDataToAdd(@SuppressWarnings("unused") Connection con, @SuppressWarnings("unused") SubMenuRol row) throws Exception {
		throw new Exception("Mtodo no implementado");
	}

	@Override
	public void internalDelete(@SuppressWarnings("unused") Connection con, @SuppressWarnings("unused") SubMenuRol row) throws Exception {
		throw new Exception("Mtodo no implementado");
	}

	@Override
	public SubMenuRol findByPrimaryKey(@SuppressWarnings("unused") SubMenuRol row) throws Exception {
		throw new Exception("Mtodo no implementado");
	}

	public Collection<SubMenuRol> findByRol(int codigoRol) throws Exception {
		return findByGeneral(FIND_BY_ROL, new Object[] { codigoRol }, 1);
	}

	@Override
	public SubMenuRol getResultSetToVO(ResultSet result) throws Exception {
		SubMenuRol smr = new SubMenuRol();
		smr.setCodigoRol(result.getInt(1));
		return smr;
	}

	@Override
	public SubMenuRol getResultSetToVO(ResultSet result, int idKind) throws Exception {
		if (idKind != 1) {
			throw new Exception("Este mtodo no ha sido implementado");
		}
		SubMenuRol smr = new SubMenuRol();
		smr.setCodigoRol(result.getInt(1));

		SubMenu sm = new SubMenu();
		sm.setAccion(result.getString(2));
		sm.setNombre(result.getString(3));

		Menu m = new Menu();
		m.setCodigo(result.getInt(4));
		m.setNombre(result.getString(5));
		m.setOrdenVisual(result.getInt(6));

		sm.setMenu(m);

		smr.setSubMenu(sm);

		return smr;
	}

	@Override
	protected void internalUpdate(@SuppressWarnings("unused") Connection con, @SuppressWarnings("unused") SubMenuRol row) throws Exception {
		throw new Exception("Mtodo no implementado");
	}

}
