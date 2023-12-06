package co.com.bbva.app.notas.contables.jsf.beans;

import co.com.bbva.app.notas.contables.carga.dto.MenuItem;
import co.com.bbva.app.notas.contables.dto.Menu;
import org.primefaces.component.menuitem.UIMenuItem;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import java.util.ArrayList;
import java.util.List;

@Named
@SessionScoped
public class MenuVisual implements java.io.Serializable, Comparable<MenuVisual> {

	public MenuVisual() {
	}

	private static final long serialVersionUID = -4411745472093058822L;

	private int codigo = 0;
	private int ordenVisual = 0;
	private String nombre = "";

	private List<MenuItem> menuItems = new ArrayList<>();
	
	

	public MenuVisual(Menu m) {
		this.codigo = m.getCodigo();
		this.nombre = m.getNombre();
	}

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

	public int compareTo(MenuVisual o) {
		return o.getOrdenVisual() - getOrdenVisual();
	}

	public List<MenuItem> getMenuItems() {
		return menuItems;
	}

	public void setMenuItems(List<MenuItem> menuItems) {
		this.menuItems = menuItems;
	}


	
}
