package co.com.bbva.app.notas.contables.dto;

public class SubMenu implements java.io.Serializable, Comparable<SubMenu> {

	private static final long serialVersionUID = -4411745472093058822L;

	private int codigo = 0;
	private int ordenVisual = 0;
	private String nombre = "";
	private String accion = "";
	private Menu menu = new Menu();

	public int getCodigo() {
		return codigo;
	}

	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}

	public int getOrdenVisual() {
		return ordenVisual;
	}

	public void setOrdenVisual(int ordenVisual) {
		this.ordenVisual = ordenVisual;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public Menu getMenu() {
		return menu;
	}

	public void setMenu(Menu menu) {
		this.menu = menu;
	}

	public int compareTo(SubMenu o) {
		return getNombre().compareTo(o.getNombre());
	}

	public String getAccion() {
		return accion;
	}

	public void setAccion(String accion) {
		this.accion = accion;
	}

}
