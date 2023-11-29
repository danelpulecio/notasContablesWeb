package co.com.bbva.app.notas.contables.jsf.beans;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import org.primefaces.model.menu.DefaultMenuItem;
import org.primefaces.model.menu.DefaultMenuModel;
import org.primefaces.model.menu.DefaultSubMenu;
import org.primefaces.model.menu.MenuModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;

@Named
@RequestScoped
public class MenuView implements Serializable {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(MenuView.class);
	
	private static final long serialVersionUID = -7578896067766477600L;
 
    private MenuModel model;
 
    @PostConstruct
    public void init() {
        model = new DefaultMenuModel();
 
        //First submenu
        DefaultSubMenu firstSubmenu = DefaultSubMenu.builder()
                .label("Dynamic Submenu")
                .build();
 
        DefaultMenuItem item = DefaultMenuItem.builder()
                .value("External")
                .url("http://www.primefaces.org")
                .icon("pi pi-home")
                .build();
        firstSubmenu.getElements().add(item);
 
        model.getElements().add(firstSubmenu);
 
        //Second submenu
        DefaultSubMenu secondSubmenu = DefaultSubMenu.builder()
                .label("Dynamic Actions")
                .build();
 
        item = DefaultMenuItem.builder()
                .value("Save")
                .icon("pi pi-save")
                .command("#{menuView.save}")
                .update("messages")
                .build();
        secondSubmenu.getElements().add(item);
 
        item = DefaultMenuItem.builder()
                .value("Delete")
                .icon("pi pi-times")
                .command("#{menuView.delete}")
                .ajax(false)
                .build();
        secondSubmenu.getElements().add(item);
 
        item = DefaultMenuItem.builder()
                .value("Redirect")
                .icon("pi pi-search")
                .command("#{menuView.redirect}")
                .build();
        secondSubmenu.getElements().add(item);
 
        model.getElements().add(secondSubmenu);
    }
 
    public MenuModel getModel() {
        return model;
    }
 
    public void save() {
    	LOGGER.info("Success Save Data");
    	//addMessage("Success","Save Data");
    }
 
    public void update() {
    	LOGGER.info("Success Data updated");
//        addMessage("Success", "Data updated");
    }
 
    public void delete() {
    	LOGGER.info("Success Data deleted");
//        addMessage("Success", "Data deleted");
    }
 
    public void addMessage(String summary, String detail) {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, summary, detail);
        FacesContext.getCurrentInstance().addMessage(null, message);
    }
}