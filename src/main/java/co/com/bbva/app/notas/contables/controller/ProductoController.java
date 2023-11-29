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


    private static List<Producto> lista = new ArrayList<>();

    private int count = 0;

    private int scrollPage = 100;

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

    public List<Producto> getLista() {
        listar();
        return lista;
    }

    public void listar() {
        if (lista.isEmpty()) {
            Producto pr1 = new Producto("Pera", "10");
            Producto pr2 = new Producto("Manzana", "20");
            Producto pr4 = new Producto("Naranja", "30");
            Producto pr5 = new Producto("Mango", "40");
            Producto pr6 = new Producto("Limon", "50");
            Producto pr7 = new Producto("Papaya", "60");
            Producto pr8 = new Producto("mandarina", "70");
            lista.add(pr1);
            lista.add(pr2);
            lista.add(pr4);
            lista.add(pr5);
            lista.add(pr6);
            lista.add(pr7);
            lista.add(pr8);
        }
    }


    @Produces
    @Model
    public String titulo() {
        return "Hola mundo JavaServer Face 3.0";
    }

}
