package co.com.bbva.app.notas.contables.jsf.beans;

import javax.inject.Named;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Model;
import javax.enterprise.inject.Produces;

@Named
@RequestScoped
public class UserBean {
    private String name;

    // Getter y Setter para 'name'

    @Produces
    @Model
    public String greet() {
        // Lgica para saludar
        return "saludooooooo";
    }
    @Produces
    @Model
    public String getName(){
        return "Danel";
    }
}
