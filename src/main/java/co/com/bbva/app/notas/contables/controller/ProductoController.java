package co.com.bbva.app.notas.contables.controller;


//import co.com.bbva.app.notas.contables.entities.Producto;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Model;
import javax.enterprise.inject.Produces;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

//contexto


//@ManagedBean
@ViewScoped
@Named
//@Model esterotipo que junta rquestscoped y name, lo unico es q el nombre del bean queda por defecto el nombre de la clase
public class ProductoController extends CategoriaController implements Serializable {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProductoController.class);
    private Producto selectedDato;

    public Producto getSelectedDato() {
        return selectedDato;
    }
    protected String param = "";

    public void setSelectedDato(Producto selectedDato) {
        this.selectedDato = selectedDato;
    }

    @PostConstruct
    public void init() {
        listar();
    }

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

    List<Producto> listaFiltrada = new ArrayList<>();


    @Produces
    @Model
    public String titulo() {
        return "Hola mundo JavaServer Face 3.0";
    }

    public String buscarPorFiltro() {
        LOGGER.info("<<<<<<<param {}>>>>>>>", getParam());
        LOGGER.info("<<<<<<<param {}>>>>>>>", lista);
        listaFiltrada.clear(); // Limpiar la lista filtrada

        for (Producto prd : lista){
            if (param.equalsIgnoreCase(prd.getFruta())){
                listaFiltrada.add(prd);
            }
        }
        lista =listaFiltrada;

        LOGGER.info("<<<<<<<listaFiltrada {}>>>>>>>", listaFiltrada);
        LOGGER.info("<<<<<<<lista {}>>>>>>>", lista);
        return null;
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
    public List<Producto> getListaFiltrada() {
        return listaFiltrada;
    }

    public void setListaFiltrada(List<Producto> listaFiltrada) {
        this.listaFiltrada = listaFiltrada;
    }

    public String getParam() {
        return param;
    }

    public void setParam(String param) {
        this.param = param;
    }
}
