package co.com.bbva.app.notas.contables.controller;


import java.util.ArrayList;
import java.util.List;

public abstract class CategoriaController {

    public CategoriaController() {
        listar();
    }

    //    private String categoria = "Tecnologia";
    private List<Producto> lista = new ArrayList<>();

    public String getCategoria() {
        return "categoria";
    }

    public List<Producto> getLista() {
        if(lista==null)
            lista = new ArrayList<>();
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

}
