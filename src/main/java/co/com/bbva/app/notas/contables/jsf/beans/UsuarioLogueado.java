package co.com.bbva.app.notas.contables.jsf.beans;

import co.com.bbva.app.notas.contables.carga.dto.MenuItem;
import co.com.bbva.app.notas.contables.carga.dto.Sucursal;
import co.com.bbva.app.notas.contables.carga.dto.UsuarioAltamira;
import co.com.bbva.app.notas.contables.dto.*;


import org.primefaces.model.menu.DefaultMenuItem;
import org.primefaces.model.menu.DefaultMenuModel;
import org.primefaces.model.menu.DefaultSubMenu;
import org.primefaces.model.menu.MenuModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



import java.io.Serializable;
import java.util.*;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.Application;
import javax.inject.Named;

@Named
@SessionScoped
public class UsuarioLogueado implements Serializable {


	/**
	 * 
	 */
	private static final long serialVersionUID = 7935040122801785429L;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(UsuarioLogueado.class);
	
	private UsuarioModulo usuario;
	private UsuarioAltamira usuAltamira;
	private Sucursal sucursal;
	private CentroEspecial centroEspecial;
	private Rol rolActual;

	private TreeMap<Menu, TreeSet<SubMenu>> menu;
	private ArrayList<MenuVisual> opcionesMenu;
	private Collection<Rol> roles;
	

	private MenuModel model;

	private Application application;

	public UsuarioLogueado(UsuarioModulo usuario) {
		
		this.usuario = usuario;
		usuAltamira = new UsuarioAltamira();
		rolActual = null;
		roles = new ArrayList<Rol>();
		menu = new TreeMap<Menu, TreeSet<SubMenu>>();
		sucursal = new Sucursal(); 
	}
	
	public UsuarioModulo getUsuario() {
		return usuario;
	}

	public void setUsuario(UsuarioModulo usuario) {
		this.usuario = usuario;
	}

	public Rol getRolActual() {
		return rolActual;
	}

	public void setRolActual(Rol rolActual) {
		this.rolActual = rolActual;
	} 

	public Collection<Rol> getRoles() {
		return roles;
	}

	public void setRoles(Collection<Rol> roles) {
		this.roles = roles;
	}

	public UsuarioAltamira getUsuAltamira() {
		return usuAltamira;
	}

	public void setUsuAltamira(UsuarioAltamira usuAltamira) {
		this.usuAltamira = usuAltamira;
	}

	public TreeMap<Menu, TreeSet<SubMenu>> getMenu() {
		return menu;
	}

	public void setMenu(TreeMap<Menu, TreeSet<SubMenu>> menu, Application application) {
		this.menu = menu;
		this.application = application;
	}
	public Sucursal getSucursal() {
		return sucursal;
	}

	public void setSucursal(Sucursal sucursal) {
		this.sucursal = sucursal;
	}

	public CentroEspecial getCentroEspecial() {
		return centroEspecial;
	}

	public void setCentroEspecial(CentroEspecial centroEspecial) {
		this.centroEspecial = centroEspecial;
	}
	
	public MenuModel getModel() {
		
		model = new DefaultMenuModel();
        
		for (Menu m : menu.keySet()) {
			DefaultSubMenu submenu = DefaultSubMenu.builder().label(m.getNombre()).build();

			for (SubMenu sm : menu.get(m)) {

				DefaultMenuItem item = DefaultMenuItem.builder()
					.value(sm.getNombre())
	                .command(sm.getAccion())
	                .id(Integer.toString(sm.getCodigo()))
	                .build();
					submenu.getElements().add(item);
			}
			submenu.setId(Integer.toString(m.getCodigo()));
			model.getElements().add(submenu);
			//model.generateUniqueIds();

		}
		
		return model;
	}

	public void setModel(MenuModel model) {
		this.model = model;
	}

	public ArrayList<MenuVisual> getOpcionesMenu() {
		if (opcionesMenu == null || opcionesMenu.isEmpty()) {
			opcionesMenu = new ArrayList<MenuVisual>();
			for (Menu m : menu.keySet()) {
				MenuVisual menuVisual = new MenuVisual(m);
				//menuVisual.setMenuItems(getMenuList(menu.get(m)));
				opcionesMenu.add(menuVisual);
			}
		}
		return opcionesMenu;
	} 
  
//	private List<HtmlMenuItem> getMenuList(TreeSet<SubMenu> opciones) {
//		LinkedList<HtmlMenuItem> menuList = new LinkedList<HtmlMenuItem>();
//		for (SubMenu sm : opciones) {
//			// make binding
//			HtmlMenuItem htmlMenuItem = new HtmlMenuItem();
//			Class<?>[] params = {};
//			MethodExpression actionExpression = application.getExpressionFactory().createMethodExpression(FacesContext.getCurrentInstance().getELContext(), sm.getAccion(), String.class, params);
//			htmlMenuItem.setActionExpression(actionExpression);  
//			htmlMenuItem.setDisabled(false);
//			htmlMenuItem.setValue(sm.getNombre());
//			menuList.add(htmlMenuItem);
//		}
//		return menuList;
//	}
//	
	
}
