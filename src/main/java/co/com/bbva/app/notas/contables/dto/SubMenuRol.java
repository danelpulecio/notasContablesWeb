package co.com.bbva.app.notas.contables.dto;

public class SubMenuRol extends CommonVO<SubMenuRol> implements java.io.Serializable {

	private static final long serialVersionUID = -4411745472093058822L;

	private int codigoRol = 0;
	private SubMenu subMenu = new SubMenu();

	public int getCodigoRol() {
		return codigoRol;
	}

	public void setCodigoRol(int codigoRol) {
		this.codigoRol = codigoRol;
	}

	public SubMenu getSubMenu() {
		return subMenu;
	}

	public void setSubMenu(SubMenu subMenu) {
		this.subMenu = subMenu;
	}

	@Override
	public Object getPK() {
		return codigoRol;
	}

	@Override
	public void restartPK(Object pk) {
		codigoRol = Integer.valueOf(pk.toString());
	}

}
