package co.com.bbva.app.notas.contables.controller;


//import co.com.bbva.app.notas.contables.entities.Producto;

import javax.annotation.ManagedBean;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.SessionScoped;
import javax.enterprise.inject.Model;
import javax.enterprise.inject.Produces;
import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

//contexto


//@ManagedBean
@RequestScoped
@Named
//@Model esterotipo que junta rquestscoped y name, lo unico es q el nombre del bean queda por defecto el nombre de la clase
public class ProductoController extends CategoriaController implements Serializable {




    private int count = 0;

    private int scrollPage = 1;

    public int getScrollPage() {
        return scrollPage;
    }

    public void setScrollPage(int scrollPage) {
        this.scrollPage = scrollPage;
    }

    public ProductoController() {
        //default
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }






    @Produces
    @Model
    public String titulo() {
        return "Hola mundo JavaServer Face 3.0";
    }

}
