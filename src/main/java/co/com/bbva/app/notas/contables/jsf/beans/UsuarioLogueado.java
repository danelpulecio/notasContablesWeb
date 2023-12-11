package co.com.bbva.app.notas.contables.jsf.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.Application;
import javax.inject.Named;

import org.primefaces.model.menu.DefaultMenuItem;
import org.primefaces.model.menu.DefaultMenuModel;
import org.primefaces.model.menu.DefaultSubMenu;
import org.primefaces.model.menu.MenuModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import co.com.bbva.app.notas.contables.carga.dto.MenuItem;
import co.com.bbva.app.notas.contables.carga.dto.Sucursal;
import co.com.bbva.app.notas.contables.carga.dto.UsuarioAltamira;
import co.com.bbva.app.notas.contables.dto.CentroEspecial;
import co.com.bbva.app.notas.contables.dto.Menu;
import co.com.bbva.app.notas.contables.dto.Rol;
import co.com.bbva.app.notas.contables.dto.SubMenu;
import co.com.bbva.app.notas.contables.dto.UsuarioModulo;

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
	

	private MenuModel model = new DefaultMenuModel();

	private Application application;
	
	private MenuModel menuButton = new DefaultMenuModel();
	
	
	public UsuarioLogueado(UsuarioModulo usuario) {
		
		this.usuario = usuario;
		usuAltamira = new UsuarioAltamira();
		rolActual = null;
		roles = new ArrayList<Rol>();
		menu = new TreeMap<Menu, TreeSet<SubMenu>>();
		sucursal = new Sucursal(); 
		
	}
	
	@PostConstruct
	public void init() {
		this.cargarMenu();
		this.cargarMenuButton();
	}
	
	public MenuModel getModel() {
		this.cargarMenu();
		//this.cargarMenuButton();

		return this.model;
	}
	
	public MenuModel cargarMenu() {
		if(this.model == null || this.model.getElements().isEmpty()) {
			for (Menu m : menu.keySet()) {
				
				DefaultSubMenu submenu = new DefaultSubMenu();
				submenu.setId( String.valueOf(m.getCodigo()) );
				submenu.setLabel(m.getNombre());
//				submenu.setStyle("overflow: auto;");
				
				
				for (SubMenu sm : menu.get(m)) {
					DefaultMenuItem item = new DefaultMenuItem();
					item.setId(sm.getNombre());
					item.setValue(sm.getNombre());
					item.setAjax(false);
					item.setUpdate("globalForm");
					item.setCommand(sm.getAccion());
					item.setStyle("text-align: left;");
					
					submenu.getElements().add(item);
				}
				
				this.model.getElements().add(submenu);
//				this.model.generateUniqueIds();
				
			}
			

			if(this.model.getElements().size() > 0){
				
				DefaultMenuItem itemOff = new DefaultMenuItem();
				itemOff.setId("off");
				itemOff.setValue("Salir");
				itemOff.setAjax(false);
				itemOff.setUpdate("globalForm");
				itemOff.setCommand("#{homePage.logout}");
				itemOff.setIcon("pi pi-fw pi-power-off");
//				itemOff.setStyle("padding-left: 100px;");
				
				this.model.getElements().add(itemOff);
			}
				
		}
		
		return model;
	}
	
	
	public MenuModel getMenuButton() {
		
		if(this.menuButton == null || this.menuButton.getElements().isEmpty()) {
			for (Menu m : menu.keySet()) {
				DefaultSubMenu submenu = DefaultSubMenu.builder().label(m.getNombre()).build();
				for (SubMenu sm : menu.get(m)) {
					DefaultMenuItem itemButton = new DefaultMenuItem();
					itemButton.setCommand(sm.getAccion());
					itemButton.setValue(sm.getNombre());
					itemButton.setUpdate("globalForm");
					itemButton.setAjax(true);
					submenu.getElements().add(itemButton);
				}
				this.menuButton.getElements().add(submenu);
			}
		}
		
		return menuButton;
	}
	
	public MenuModel cargarMenuButton() {
		
		if(this.menuButton == null || this.menuButton.getElements().isEmpty()) {
			for (Menu m : menu.keySet()) {
				DefaultSubMenu submenu = DefaultSubMenu.builder().label(m.getNombre()).build();
				submenu.setId( String.valueOf(m.getCodigo()) );
				for (SubMenu sm : menu.get(m)) {
					DefaultMenuItem ajaxAction = new DefaultMenuItem();
					ajaxAction.setCommand(sm.getAccion());
					ajaxAction.setValue(sm.getNombre());
					ajaxAction.setUpdate("globalForm");
					ajaxAction.setAjax(true);
					submenu.getElements().add(ajaxAction);
				}
				this.menuButton.getElements().add(submenu);
			}
		}
		return this.menuButton;
	}



	public void setMenuButton(MenuModel menuButton) {
		this.menuButton = menuButton;
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
	
	public void setModel(MenuModel model) {
		this.model = model;
	}

	public ArrayList<MenuVisual> getOpcionesMenu() {
		if (opcionesMenu == null || opcionesMenu.isEmpty()) {
			opcionesMenu = new ArrayList<MenuVisual>();
			for (Menu m : menu.keySet()) {
				MenuVisual menuVisual = new MenuVisual(m);
				menuVisual.setMenuItems(getMenuButtonList(menu.get(m)));
				opcionesMenu.add(menuVisual);
			}
		}
		return opcionesMenu;
	}
	
	private List<MenuItem> getMenuButtonList(TreeSet<SubMenu> opciones){
		ArrayList<MenuItem> menuList = new ArrayList<>();
		for (SubMenu sm : opciones) {
			MenuItem menuItem = new MenuItem();
			Class<?>[] params = {};
			//MethodExpression actionExpression = application.getExpressionFactory().createMethodExpression(FacesContext.getCurrentInstance().getELContext(), sm.getAccion(), String.class, params);
			//menuItem.setActionNC(actionExpression.toString());  
			menuItem.setAction(sm.getAccion());
			menuItem.setValue(sm.getNombre());
			menuList.add(menuItem);			
		}
		return menuList;
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
